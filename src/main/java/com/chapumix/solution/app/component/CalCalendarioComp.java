package com.chapumix.solution.app.component;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.chapumix.solution.app.models.entity.CalCalendario;
import com.chapumix.solution.app.models.service.ICalCalendarioService;
import com.sun.mail.smtp.SMTPTransport;

@Component
public class CalCalendarioComp {
	
	@Autowired
	private ICalCalendarioService iCalCalendarioService;	
	
	@Autowired
	private ResourceLoader loader;
	
	private Logger logger = LoggerFactory.getLogger(CalCalendarioComp.class);
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
	private static final String SMTP_SERVER = "correo.dongee.com";
    private static final String USERNAME = "humanizacion@hospitalsanjose.gov.co";
    private static final String PASSWORD = "Migracion2018";
    private static final String EMAIL_FROM = "humanizacion@hospitalsanjose.gov.co";
    private static final String EMAIL_SUBJECT = "FELIZ CUMPLEA\u00d1OS te desea HOSPITAL UNIVERSITARIO SAN JOSE";    
    private static final String EMAIL_TEXT = "";
    
    public Calendar cal;
    public int mesCumple;
    public int diaCumple;
    public Date fechaHoy;
    public int mesHoy;
    public int diaHoy;
    public Properties prop;
    public Session session;
    public Message msg;
    public MimeBodyPart p1;
    public MimeBodyPart p2;
    public FileDataSource fds;
    public Multipart mp;
    public SMTPTransport t;
	
	//metodo que se ejecuta de forma automatica todos los dias a la 7:00 AM formato de 24 horas
    @Scheduled(cron = "00 00 07 * * *", zone="America/Bogota")    
    public void cronCumpSch() {
	    
    	    //verificamos la hora y fecha de ejecucion
    		logger.info("Hora y fecha de ejecución      = {}", dateFormat.format(new Date()));
			//buscamos todos los empleados en base de datos
    		List<CalCalendario> listEmpleado = iCalCalendarioService.findAll();
			if(listEmpleado != null) {
				listEmpleado.forEach(dato -> {
					
					if (!dato.getCorreo().equals(null) && !dato.getFechaNacimiento().equals(null)) {
						
						logger.info("Consulto al mechudo del cumpleaños");
						String fechaCumpleC = convertirFechaString(dato.getFechaNacimiento());
						String fechaHoyC = convertirFechaString(new Date());
						cal = Calendar.getInstance();
						try {
							Date fechaCumple = convertirStringFecha(fechaCumpleC);
							cal.setTime(fechaCumple);
							mesCumple = cal.get(2);
							++mesCumple;
							diaCumple = cal.get(5);
		                    fechaHoy = convertirStringFecha(fechaHoyC);
		                    cal.setTime(fechaHoy);
		                    mesHoy = cal.get(2);
		                    ++mesHoy;
		                    diaHoy = cal.get(5);
		                    logger.info("Arma la fecha del cumpleaños para comparar");
		                    System.out.println("Compara dia cumple:" + diaCumple + " y la fecha dia de hoy: " + diaHoy);
		                    System.out.println("Compara mes cumple:" + mesCumple + " y la fecha dia de hoy: " + mesHoy);
		                    
		                    if(diaCumple == diaHoy && mesCumple == mesHoy) {
		                    	logger.info("Valida fecha de cumpleaños");
		                    	prop = System.getProperties();
		                        prop.put("mail.smtp.auth", "true");
		                        logger.info("Paso 1");
		                        session = Session.getInstance(prop, (Authenticator)null);
		                        msg = (Message)new MimeMessage(session);
		                        msg.setFrom((Address)new InternetAddress(EMAIL_FROM));
		                        msg.setRecipients(Message.RecipientType.TO, (Address[])InternetAddress.parse(dato.getCorreo(), false));
		                        logger.info("Paso 2");
		                        msg.setSubject(EMAIL_SUBJECT);
		                        logger.info("Paso 3");
		                        p1 = new MimeBodyPart();
		                        logger.info("Paso 4");
		                        p1.setText(EMAIL_TEXT);
		                        logger.info("Paso 5");
		                        p2 = new MimeBodyPart();
		                        logger.info("Paso 6");
		                        Resource resource = loader.getResource("classpath:static/dist/img/cumpleanos.jpg");
		                        logger.info("Paso 7");
		                        File dbAsFile = resource.getFile();                  
		                        logger.info("Paso 8");
		                        fds = new FileDataSource(dbAsFile);
		                        p2.setDataHandler(new DataHandler((DataSource)fds));
		                        p2.setFileName(fds.getName());	                        
		                        mp = (Multipart)new MimeMultipart();
		                        mp.addBodyPart((BodyPart)p1);
		                        mp.addBodyPart((BodyPart)p2);		                        
		                        msg.setContent(mp);
		                        t = (SMTPTransport)session.getTransport("smtp");
		                        logger.info("Hizo la conexino smtp");
		                        t.connect(SMTP_SERVER, USERNAME , PASSWORD);
		                        logger.info("Valido las credenciales");
		                        t.sendMessage(msg, msg.getAllRecipients());
		                        logger.info("Envio el mensaje");
		                        t.close();
		                        logger.info("Cerro la conexion");
		                        dato.setEnviado(true);
		                        iCalCalendarioService.save(dato);
		                        logger.info("Actualizo estado de envio");
		                    }			
							
							
						} catch (ParseException | MessagingException | IOException e) {						
							e.printStackTrace();
						}
						
					}
					
				});		
    		}
		
	   }    
    
    
  //Se usa para convertir una fecha Date con un String con formato
  	private String convertirFechaString(Date fechaSolicitudInterno) {
  		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
  		String strDate = dateFormat.format(fechaSolicitudInterno);  
  		return strDate;
  	}
  	
  //Se usa para convertir una string cadena a fecha Date con formato
  	private Date convertirStringFecha(String fecha) throws ParseException {
  		Date fechaTranformada = new SimpleDateFormat("dd-MM-yyyy").parse(fecha);  
  		return fechaTranformada;
  	}

}
