package com.chapumix.solution.app.component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import com.chapumix.solution.app.entity.dto.GenPacienDTO;
import com.chapumix.solution.app.models.entity.ComGenero;
import com.chapumix.solution.app.models.entity.ComTipoDocumento;
import com.chapumix.solution.app.models.entity.ComTipoDocumentoMipres;
import com.chapumix.solution.app.models.entity.ComTipoTecnologia;
import com.chapumix.solution.app.models.entity.ComTokenMipres;
import com.chapumix.solution.app.models.entity.FarMipres;
import com.chapumix.solution.app.models.entity.GenPacien;
import com.chapumix.solution.app.models.service.IComGeneroService;
import com.chapumix.solution.app.models.service.IComTipoDocumentoMipresService;
import com.chapumix.solution.app.models.service.IComTipoDocumentoService;
import com.chapumix.solution.app.models.service.IComTipoTecnologiaService;
import com.chapumix.solution.app.models.service.IComTokenMipresService;
import com.chapumix.solution.app.models.service.IFarMipresService;
import com.chapumix.solution.app.models.service.IGenPacienService;

@Component
public class FarMipresComp {
	
	public static final String MetodoGetPrescripcion = "https://wsmipres.sispro.gov.co/WSSUMMIPRESNOPBS/api/EntregaXPrescripcion/"; //url mipres metodo para get consulta
	
	public static final String MetodoGetConsultaFecha = "https://wsmipres.sispro.gov.co/wsmipresnopbs/api/Prescripcion/"; //url mipres metodo para get consulta por fecha 
	
	public static final String URLPaciente = "http://localhost:9000/api/pacientegeneral"; //se obtuvo de API REST de GenPacienRestController
	
	//private Logger logger = LoggerFactory.getLogger(FarMipresComp.class);
	
	@Autowired
	private IFarMipresService iFarMipresService;
	
	@Autowired
	private IComTokenMipresService iComTokenMipresService;
	
	@Autowired
	private IComTipoTecnologiaService iComTipoTecnologiaService;
	
	@Autowired
	private IComTipoDocumentoMipresService iComTipoDocumentoMipresService;	
	
	@Autowired
	private IGenPacienService iGenPacienService;
	
	@Autowired
	private IComGeneroService iComGeneroService;
	
	@Autowired
	private IComTipoDocumentoService iComTipoDocumentoService;
	
	@Autowired
	private RestTemplate restTemplate;
	
	
	//metodo que se ejecuta de forma automatica todos los dias a las 11:58PM   	
	@Scheduled(cron = "00 58 23 * * *", zone="America/Bogota")	
	public void cronSincronizaPrescripciones() throws Exception {    	
    	
		//convierto String a Date 
		Date fecha = new Date();
				
		//convierto Date a LocalDate
		LocalDate fechaLocal = fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();		
				
		//Me sirve para guardar los tipos de tecnologias
		Map<String, Object> map = new HashMap<>();
			
		//obtengo los datos del token primario guardados en solution
		ComTokenMipres comTokenMipres = iComTokenMipresService.findById(2L);
		
		//genero la url para consultar
		String urlEncadenada = MetodoGetConsultaFecha+comTokenMipres.getNit()+'/'+fechaLocal+'/'+comTokenMipres.getTokenPrincipal();
		
		//Especificamos la URL y configuro el objeto HttpClient			
		HttpClient httpclient = HttpClientBuilder.create().build();			
		HttpResponse response = null;
		
		//Se crea una solicitud GET (si es post HttpPost y si es es put) y pasamos el URL del recurso y también asigne encabezados a este objeto de colocación
		HttpGet httpGet = new HttpGet(urlEncadenada);			
		httpGet.setHeader("Accept", "application/json");
		httpGet.setHeader("Content-type", "application/json");			
		
		response = httpclient.execute(httpGet);
		
		//creo un String para guardar la respuesta y convertir en un arreglo JSON	        
        String content = EntityUtils.toString(response.getEntity());	        
        JSONArray arregloJSON = new JSONArray(content);	        
        //recorro el arreglo para tranformarlo en un objeto JSON
        for (int i = 0; i < arregloJSON.length(); i++) {
        	//jsonContent = new JSONObject(content.replaceAll("\\[", "").replaceAll("\\]", ""));
        	JSONObject objetoJSON = arregloJSON.getJSONObject(i);
        	Integer codigoAmbitoAtencion = objetoJSON.getJSONObject("prescripcion").getInt("CodAmbAte");
        	Integer estPres = objetoJSON.getJSONObject("prescripcion").getInt("EstPres");
        	
        	if((codigoAmbitoAtencion == 22 || codigoAmbitoAtencion == 30) && estPres == 4)   {            	
        	
        	String prescripcion = objetoJSON.getJSONObject("prescripcion").getString("NoPrescripcion");            	
        	String tipoDocumento = objetoJSON.getJSONObject("prescripcion").getString("TipoIDPaciente");
        	String numDocumento = objetoJSON.getJSONObject("prescripcion").getString("NroIDPaciente");
        	String primerNombre = objetoJSON.getJSONObject("prescripcion").getString("PNPaciente");
        	String segundoNombre = objetoJSON.getJSONObject("prescripcion").getString("SNPaciente");
        	String primerApellido = objetoJSON.getJSONObject("prescripcion").getString("PAPaciente");
        	String segundoApellido = objetoJSON.getJSONObject("prescripcion").getString("SAPaciente");
        	String codEps = objetoJSON.getJSONObject("prescripcion").getString("CodEPS");
        	String fechaEntrega = objetoJSON.getJSONObject("prescripcion").getString("FPrescripcion");
        	
        	
        	String fechaEntregaCustomString = fechaEntrega.replace("T00:00:00", "");
        	Date fechaEntregaCustomDate = convertirFechaParametro(fechaEntregaCustomString);        	
        	            	
        	//limpiamos el Map
        	map.clear();
        	
        	//contamos los medicamentos dados
        	JSONArray arrayMedicamentos = objetoJSON.getJSONArray("medicamentos");            	
        	if(arrayMedicamentos.length() >= 1) {
            	
        		for(int m=0; m < arrayMedicamentos.length(); m++) {          			
        			map.put("cantidadEntregada"+m, arrayMedicamentos.getJSONObject(m).getString("CantTotalF"));
        			map.put("nombreMedicamento"+m, arrayMedicamentos.getJSONObject(m).getString("DescMedPrinAct"));
                    map.put("tipoTecnologia"+m, "M");	
                    map.put("consecutivoTecnologia"+m, arrayMedicamentos.getJSONObject(m).getInt("ConOrden"));
            	}
        	} 
        	
        	//contamos los procedimientos dados
        	JSONArray arrayProcedimientos = objetoJSON.getJSONArray("procedimientos");            	
        	if(arrayProcedimientos.length() >= 1) {
        		
        		for(int p=0; p < arrayProcedimientos.length(); p++) {           			
        			map.put("cantidadEntregada"+p, arrayProcedimientos.getJSONObject(p).getString("CantTotal"));
                    map.put("tipoTecnologia"+p, "P");
                    map.put("consecutivoTecnologia"+p, arrayProcedimientos.getJSONObject(p).getInt("ConOrden"));
            	}
        	}
        	
        	//contamos los productos nutricionales dados
        	JSONArray arrayProductosNutricionales = objetoJSON.getJSONArray("productosnutricionales");            	
        	if(arrayProductosNutricionales.length() >= 1) {
        		
        		for(int n=0; n < arrayProductosNutricionales.length(); n++) {            			
        			map.put("cantidadEntregada"+n, arrayProductosNutricionales.getJSONObject(n).getString("CantTotalF"));
                    map.put("tipoTecnologia"+n, "N");
                    map.put("consecutivoTecnologia"+n, arrayProductosNutricionales.getJSONObject(n).getInt("ConOrden"));
            	}           		
        	}
        	
        	//contamos los servicios complementarios dados
        	JSONArray arrayServicioComplementario = objetoJSON.getJSONArray("serviciosComplementarios");            	
        	if(arrayServicioComplementario.length() >= 1) {
        		
        		for(int c=0; c < arrayServicioComplementario.length(); c++) {           			
        			map.put("cantidadEntregada"+c, arrayServicioComplementario.getJSONObject(c).getString("CantTotal"));
                    map.put("tipoTecnologia"+c, "S");
                    map.put("consecutivoTecnologia"+c, arrayServicioComplementario.getJSONObject(c).getInt("ConOrden"));
            	}          		
        	}        
            
        	//este if es cuando es solo una sola tecnologia
        	if(map.size() == 3 || map.size() == 4) {
        		
        		FarMipres buscarSolution = iFarMipresService.findByDocumentoPrescripcionConsecutivoTecnologiaCantidad(numDocumento, prescripcion, map.get("cantidadEntregada0").toString(), (Integer) map.get("consecutivoTecnologia0"));
                
                if(buscarSolution == null) {         	
                	
                	FarMipres farMipres = new FarMipres();
                    
                	//buscamos el tipo documento paciente para guardar
                	ComTipoDocumentoMipres comTipoDocumentoMipres = iComTipoDocumentoMipresService.tipoDocumento(tipoDocumento);
                	
                	//buscamos el numero de documento del paciente para guardar
                	GenPacien genPacien = iGenPacienService.findByNumberDoc(numDocumento);
                	if(genPacien == null) {
                		GenPacien obtengoPaciente =  SicronizarPacientePorDocumento(tipoDocumento, numDocumento, primerNombre, segundoNombre, primerApellido, segundoApellido);
                		farMipres.setGenPacien(obtengoPaciente);
                	}  else {
                		farMipres.setGenPacien(genPacien);
                	}
                	
                	//buscamos la tecnologia para guardar                	  
                	ComTipoTecnologia comTipoTecnologia = iComTipoTecnologiaService.tipoTecnologia(map.get("tipoTecnologia0").toString());  
                	
                	//validamos si el medicamento es vacio
                	if(map.containsKey("nombreMedicamento0")) {
                		farMipres.setNombreMedicamento(map.get("nombreMedicamento0").toString());
                	}else {
                		farMipres.setNombreMedicamento("SIN INFORMACION");
                	}
                	
                	farMipres.setNumeroPrescripcion(prescripcion);
                    farMipres.setCantidadEntregada(map.get("cantidadEntregada0").toString());
                    farMipres.setProcesadoEntrega(false);
                    farMipres.setProcesadoReporteEntrega(false);
                    farMipres.setProcesadoFacturacion(false);                    
                    farMipres.setComTipoDocumentoMipres(comTipoDocumentoMipres);                    
                    farMipres.setFechaEntrega(fechaEntregaCustomDate);
                    farMipres.setConsecutivoTecnologia(Integer.parseInt(map.get("consecutivoTecnologia0").toString()));
                    farMipres.setComTipoTecnologia(comTipoTecnologia);
                    farMipres.setCodigoServicio("SIN INFORMACION");
                    farMipres.setNumeroEntrega(1);
                    farMipres.setEntregaTotal(1);
                    farMipres.setCausaNoEntrega(0);
                    farMipres.setLoginUsrAlta("76328021");
                    farMipres.setCodEps(codEps);                
                    
                	iFarMipresService.save(farMipres);            	
                }   
            //este else es cuando son varias tecnologias
        	}else {
        		if(map.size() == 6 || map.size() == 8) {
        			int hasta = 2;
        			procesarGuardar(hasta, map, numDocumento, prescripcion, tipoDocumento, primerNombre, segundoNombre, primerApellido, segundoApellido, fechaEntregaCustomDate, "76328021", codEps);
        		}
        		if(map.size() == 9 || map.size() == 12) {
        			int hasta = 3;
        			procesarGuardar(hasta, map, numDocumento, prescripcion, tipoDocumento, primerNombre, segundoNombre, primerApellido, segundoApellido, fechaEntregaCustomDate, "76328021", codEps);
        		}
        	}
        }	
        	 	
        }//fin for
		
    	
	   }

	
	
	/* ----------------------------------------------------------
     * METODOS ADICIONALES 
     * ---------------------------------------------------------- */	
	
	//Se usa para convertir parametro fecha solicitud de String a fecha Date con formato
	private static Date convertirFechaParametro(String fecha) throws ParseException {
		Date fechaTranformada = new SimpleDateFormat("yyyy-MM-dd").parse(fecha);		
		return fechaTranformada;
	}
	
	//Se usa para gurdar la prescripcion en la la base de solution
	private void procesarGuardar(int hasta, Map<String, Object> map, String numDocumento, String prescripcion, String tipoDocumento, String primerNombre, String segundoNombre, String primerApellido,
			String segundoApellido, Date fechaEntregaCustomDate, String usuario, String codEps) {
		for(int z=0; z<hasta; z++) {
			
			String cantidadEntregada = map.get("cantidadEntregada"+z).toString();                    	
        	String tipoTecnologia = map.get("tipoTecnologia"+z).toString();
        	Integer consecutivoTecnologia = Integer.parseInt(map.get("consecutivoTecnologia"+z).toString());
			
			FarMipres buscarSolution = iFarMipresService.findByDocumentoPrescripcionConsecutivoTecnologiaCantidad(numDocumento, prescripcion, cantidadEntregada, consecutivoTecnologia);
            
            if(buscarSolution == null) {                
			
			FarMipres farMipres = new FarMipres();
            
        	//buscamos el tipo documento paciente para guardar
        	ComTipoDocumentoMipres comTipoDocumentoMipres = iComTipoDocumentoMipresService.tipoDocumento(tipoDocumento);
        	
        	//buscamos el numero de documento del paciente para guardar
        	GenPacien genPacien = iGenPacienService.findByNumberDoc(numDocumento);
        	if(genPacien == null) {
        		GenPacien obtengoPaciente =  SicronizarPacientePorDocumento(tipoDocumento, numDocumento, primerNombre, segundoNombre, primerApellido, segundoApellido);
        		farMipres.setGenPacien(obtengoPaciente);
        	}  else {
        		farMipres.setGenPacien(genPacien);
        	}                    	
        	
        	//buscamos la tecnologia para guardar                	  
        	ComTipoTecnologia comTipoTecnologia = iComTipoTecnologiaService.tipoTecnologia(tipoTecnologia);  
        	
        	//validamos si el medicamento es vacio
        	if(map.containsKey("nombreMedicamento"+z)) {
        		farMipres.setNombreMedicamento(map.get("nombreMedicamento"+z).toString());
        	}else {
        		farMipres.setNombreMedicamento("SIN INFORMACION");
        	}
        	
        	farMipres.setNumeroPrescripcion(prescripcion);
            farMipres.setCantidadEntregada(cantidadEntregada);
            farMipres.setProcesadoEntrega(false);
            farMipres.setProcesadoReporteEntrega(false);
            farMipres.setProcesadoFacturacion(false);                    
            farMipres.setComTipoDocumentoMipres(comTipoDocumentoMipres);                    
            farMipres.setFechaEntrega(fechaEntregaCustomDate);
            farMipres.setConsecutivoTecnologia(Integer.parseInt(map.get("consecutivoTecnologia"+z).toString()));                        
            farMipres.setComTipoTecnologia(comTipoTecnologia);
            farMipres.setCodigoServicio("SIN INFORMACION");
            farMipres.setNumeroEntrega(1);
            farMipres.setEntregaTotal(0);
            farMipres.setCausaNoEntrega(0);
            farMipres.setLoginUsrAlta(usuario);
            farMipres.setCodEps(codEps);                        
        	iFarMipresService.save(farMipres);
        	
            	}
			}		
	}
	
	
	//Se usa para sincronizar los pacientes de dinamica a solution
	private GenPacien SicronizarPacientePorDocumento(String tipoDocumento, String numDocumento, String primerNombre, String segundoNombre, String primerApellido, String segundoApellido) {
		// proceso API para buscar el paciente
		ResponseEntity<List<GenPacienDTO>> respuestaa = restTemplate.exchange(URLPaciente + '/' + numDocumento, HttpMethod.GET, null,new ParameterizedTypeReference<List<GenPacienDTO>>() {});
		List<GenPacienDTO> dinamica = respuestaa.getBody();
		
		List<GenPacienDTO> filtrarPaciente = dinamica.stream().filter(c -> c.getPacNumDoc().equals(numDocumento)).collect(Collectors.toList());
		
		if(!filtrarPaciente.isEmpty()) {
			GenPacien guardarPaciente = pacienteAgregar(filtrarPaciente);					
			iGenPacienService.save(guardarPaciente);
			GenPacien obtengoPaciente = iGenPacienService.findByNumberDoc(numDocumento);		
			return obtengoPaciente;
		}else {
			GenPacien guardarPaciente = pacienteNoExisteDinamica(tipoDocumento, numDocumento, primerNombre, segundoNombre, primerApellido, segundoApellido);
			iGenPacienService.save(guardarPaciente);
			GenPacien obtengoPaciente = iGenPacienService.findByNumberDoc(numDocumento);		
			return obtengoPaciente;
		}
			
	}
	
	//Se usa para crear un paciente no existente en dinamica
	private GenPacien pacienteNoExisteDinamica(String tipoDocumento, String numDocumento, String primerNombre, String segundoNombre, String primerApellido, String segundoApellido) {
		GenPacien agregarPaciente = new GenPacien();
		//buscamos el sexo del paciente			
		ComGenero sexoPaciente = iComGeneroService.findById(2L);
			
		//buscamos el tipo de documento del paciente			
		ComTipoDocumento tipoDocumentoMipres = iComTipoDocumentoService.tipoDocumento(tipoDocumento);
				
		agregarPaciente.setOid(null);
		agregarPaciente.setPacNumDoc(numDocumento);
		agregarPaciente.setPacPriNom(primerNombre);
		agregarPaciente.setPacSegNom(segundoNombre);
		agregarPaciente.setPacPriApe(primerApellido);
		agregarPaciente.setPacSegApe(segundoApellido);
		agregarPaciente.setGpafecnac(new Date());
		agregarPaciente.setComGenero(sexoPaciente);
		agregarPaciente.setComTipoDocumento(tipoDocumentoMipres);
		return agregarPaciente;
	}
	
	
	//Se usa para obtener el paciente a guardar
	private GenPacien pacienteAgregar(List<GenPacienDTO> dinamica) {
		GenPacien agregarPaciente = new GenPacien();
		//buscamos el sexo del paciente			
		ComGenero sexoPaciente = iComGeneroService.findById(dinamica.get(0).getGpasexpac().longValue());
				
		//buscamos el tipo de documento del paciente			
		ComTipoDocumento tipoDocumento = iComTipoDocumentoService.findById(dinamica.get(0).getPacTipDoc().longValue());		
				
		agregarPaciente.setOid(dinamica.get(0).getOid());
		agregarPaciente.setPacNumDoc(dinamica.get(0).getPacNumDoc());
		agregarPaciente.setPacPriNom(dinamica.get(0).getPacPriNom());
		agregarPaciente.setPacSegNom(dinamica.get(0).getPacSegNom());
		agregarPaciente.setPacPriApe(dinamica.get(0).getPacPriApe());
		agregarPaciente.setPacSegApe(dinamica.get(0).getPacSegApe());
		agregarPaciente.setGpafecnac(dinamica.get(0).getGpafecnac());
		agregarPaciente.setComGenero(sexoPaciente);
		agregarPaciente.setComTipoDocumento(tipoDocumento);
		return agregarPaciente;
	}	
}
