package com.chapumix.solution.app.models.service;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.chapumix.solution.app.custom.CustomUserDetails;
import com.chapumix.solution.app.models.dao.IComUsuarioDao;
import com.chapumix.solution.app.models.entity.ComRole;
import com.chapumix.solution.app.models.entity.ComUsuario;

@Service("jpaComUsuarioDetalleService")
public class JpaComUsuarioDetalleService implements UserDetailsService{
	
	/*@Autowired
	private IComUsuarioDao comUsuarioDao;*/
	
	@Autowired
	private IComUsuarioDao comUsuarioDao;	
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private RestTemplate restTemplate;
		
	private Logger logger = LoggerFactory.getLogger(JpaComUsuarioDetalleService.class);
	private ArrayList<String> lista = new ArrayList<String>();
	public static final String URL = "http://localhost:9000/api/usuarios/username/";	
	

	@Override
	//@Transactional //en este caso me toco deshabilitar para poder validar los usuario que vienen de dinamica, si no fuera asi debo habilitarlo y en la clase ComUsuario 
					 //cambiar fetch = FetchType.EAGER por fetch = FetchType.LAZY
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		//obtener el datos extras del usuario
		//ComUsuario user = (ComUsuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();		
		//user.getNombreCompleto();		
		
		//boolean result = passwordEncoder.matches(password_plan_text_here, encoded_password_here);  //comparar contraseñas
		
		//obtengo la clave para luego ser comparada en dinamica
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String password = request.getParameter("password"); // get from request parameter	
		
		
		consultaAPI(URL+username, username, password); //consulta el usuario en dinamica atravez de la API		
	
		
		//hace la busqueda y validacion en la base de datos de Solution
		ComUsuario usuario = comUsuarioDao.findByUsuario(username);		
		if(usuario == null) {
			logger.error("Error login: no existe el usuario  '"+ username+"'");
			throw new UsernameNotFoundException("Usuario "+ username + "no existe en el sistema!");
		}
		
		List<GrantedAuthority> roles = new ArrayList<GrantedAuthority>();
		
		for (ComRole role: usuario.getRoles()) {
			logger.info("Role: ".concat(role.getNombre()));
			roles.add(new SimpleGrantedAuthority(role.getNombre()));
		}
		
		if(roles.isEmpty()) {
			logger.error("Error login: usuario  '"+ username+"' no tiene roles asignados");
			throw new UsernameNotFoundException("Error login: usuario  '"+ username +"' no tiene roles asignados");
		}
		
		//return new User(usuario.getUsuario(), usuario.getContrasena(), usuario.getEstado(), true, true, true, roles);
		return new CustomUserDetails(usuario.getUsuario(), usuario.getContrasena(), usuario.getNombreCompleto(), usuario.getNombreCorto(), usuario.getEstado(), true, true, true, roles);
	}



	//metodo para consultar API	
	private void consultaAPI(String url, String username, String password) {				
		if(url != null) {
			guardarJSONEnArray(url, username, password);
		}
	}
	

	//metodo para guardar en un ArrayList la informacion API que viene de dinamica
	private void guardarJSONEnArray(String ruta, String username, String password) {
	
		// hacemos una solicitud HTTP GET
		String json = restTemplate.getForObject(ruta, String.class);
		
		//validamos que la consulta a la API no venga null.
		if(json != null) {
			JsonParser springParser = JsonParserFactory.getJsonParser();
			Map<String, Object> map = springParser.parseMap(json);
			
			int i = 0;
			for(Map.Entry<String, Object> entry : map.entrySet()) {
				System.out.println(entry.getKey() + " = " + entry.getValue());
				if(entry.getValue() != null) {
					this.lista.add(i, entry.getValue().toString());
				}else {
					this.lista.add(i, "");
				}				
				i++;				
			}		
		}else {
			logger.error("Error consulta: El usuario no se encuentra en dinamica");
			throw new UsernameNotFoundException("Error consulta: El usuario no se encuentra en dinamica");
		}
		
		//valida que el usuario este activo en dinamica para ser sincronizado
		if(!lista.get(2).isEmpty() && lista.get(5).equals("1")) {
			sincronizaMYSQL(username, password);
		}		
		
	}
	
	
	//metodo para sincronizar los usuarios de dinamica en Solution
	private void sincronizaMYSQL(String username, String password) {
		
		//se hace un hash de lo digitado en el formulario
		String digitadoSolution = hashValue(password);		
		
		
		if(lista.get(2).equals(username) && lista.get(4).equals(digitadoSolution)) {
			
			ComUsuario usuarioGuardar = comUsuarioDao.findByUsuario(lista.get(2).toString());
			
			if(usuarioGuardar != null) {
				String nombreCorto = recortar(lista.get(3).toString());					
				usuarioGuardar.setUsuario(lista.get(2).toString()); //cedula
				usuarioGuardar.setNombreCompleto(lista.get(3).toString()); //nombre completo
				usuarioGuardar.setNombreCorto(nombreCorto); //nombre corto 
				usuarioGuardar.setContrasena(passwordEncoder.encode(password)); //contraseña nueva
				
				if(lista.get(5).equals("1")) {
					usuarioGuardar.setEstado(Boolean.parseBoolean("true")); //estado
				}				
				comUsuarioDao.save(usuarioGuardar);
				
			}else {
				ComUsuario usuarioGuardar1 = new ComUsuario();
				ComRole role = new ComRole();
				List<ComRole> roles = new ArrayList<>();
				String nombreCorto = recortar(lista.get(3).toString());					
				
				
				usuarioGuardar1.setUsuario(lista.get(2).toString()); //cedula
				usuarioGuardar1.setNombreCompleto(lista.get(3).toString()); //nombre completo
				usuarioGuardar1.setNombreCorto(nombreCorto); //nombre corto 
				usuarioGuardar1.setContrasena(passwordEncoder.encode(password)); //contraseña nueva
				role.setNombre("ROLE_TMP");
				roles.add(role);
				usuarioGuardar1.setRoles(roles);
				
				if(lista.get(5).equals("1")) {
					usuarioGuardar1.setEstado(Boolean.parseBoolean("true")); //estado
				}			
				comUsuarioDao.save(usuarioGuardar1);
			}
		}
		
	}


	//metodo para validar contraseña de dinamica y contraseña digitada en Solution
	private String hashValue(String contrasena) {
		 //String digest = null;
	    try {
	        MessageDigest md = MessageDigest.getInstance("MD5");
	        byte[] hash = md.digest(contrasena.getBytes("UTF-16LE")); // <-- note encoding
	        return new String(Base64.encodeBase64(hash));
	    } catch (NoSuchAlgorithmException e) {
	        e.printStackTrace();
	    } catch (UnsupportedEncodingException e) {
	        e.printStackTrace();
	    }
	    return null;
		
	}

	//metodo para obtener el primer nombre del funcionario
	private String recortar(String cadena) {
		String datos[] = cadena.split(" ");		
		return datos[0];
	}

}
