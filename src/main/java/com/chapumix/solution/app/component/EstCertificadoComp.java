package com.chapumix.solution.app.component;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import javax.mail.internet.AddressException;
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

import com.chapumix.solution.app.models.entity.EstSerial;
import com.chapumix.solution.app.models.service.IEstCertificadoService;
import com.chapumix.solution.app.models.service.IEstSerialService;
import com.sun.mail.smtp.SMTPTransport;

@Component
public class EstCertificadoComp {
	
	@Autowired
	private IEstSerialService iEstSerialService;
	
	@Autowired
	private ResourceLoader loader;
	
	private Logger logger = LoggerFactory.getLogger(EstCertificadoComp.class);
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
	private static final String SMTP_SERVER = "correo.dongee.com";
    private static final String USERNAME = "desarrollo.sistemas@hospitalsanjose.gov.co";
    private static final String PASSWORD = "HusjequiposP0";
    private static final String EMAIL_FROM = "desarrollo.sistemas@hospitalsanjose.gov.co";
    private static final String EMAIL_SUBJECT = "CERTIFICADOS DE DEFUNCION Y NACIDOS VIVOS AL LIMITE";    
    private static final String EMAIL_TEXT = "";
    private static final String EMAIL_DESTINATION = "estadistica@hospitalsanjose.gov.co";
    
    
    public Properties prop;
    public Session session;
    public Message msg;
    public MimeBodyPart p1;
    public MimeBodyPart p2;
    public FileDataSource fds;
    public Multipart mp;
    public SMTPTransport t;
	
	//metodo que se ejecuta de forma automatica todos los dias cada hora formato de 24 horas
    @Scheduled(cron = "00 00 00/01 * * *", zone="America/Bogota")   
    public void cronCantidadCertificadosSch() throws AddressException, MessagingException {	    
    	
    	logger.info("SE ESTA EJECUTANDO");
    	List<EstSerial> serialesDefuncion = iEstSerialService.countSerialDefuncion();
		List<EstSerial> serialesNacidoVivo = iEstSerialService.countSerialNacidoVivo();		
		
		if(serialesDefuncion.size() < 5 || serialesNacidoVivo.size() < 5) {
			
			String htmlContent = "<table class=\"x_mce-item-table\" style=\"width:531.54px; margin-left:auto; margin-right:auto\">\r\n" + 
					"					<tbody>\r\n" + 
					"					<tr style=\"height:29px\">\r\n" + 
					"					<td style=\"text-align:center; width:526.54px; height:29px\">\r\n" + 
					"					<span style=\"font-family:helvetica,arial,sans-serif; font-size:18pt; color:#105ea8; font-weight:bold\">¡Hola, Señores Estadística</span>\r\n" + 
					"					</td>\r\n" + 
					"					</tr>\r\n" + 
					"					<tr style=\"height:190.173px\">\r\n" + 
					"					<td style=\"width:526.54px; height:190.173px\"><br>\r\n" + 
					"					<b style=\"color:#0059a4; font-family:Roboto,sans-serif; font-size:16px; text-align:center\"><span color=\"#0059a4\" face=\"Roboto, sans-serif\"><b><span style=\"font-family:helvetica,arial,sans-serif\"><span style=\"color:#999999; font-size:14pt\">El motivo de este correo es para notificar que alguno de los seriales está en su limite.&nbsp;</span></span></b></span></b><br>\r\n" + 
					"					<br>\r\n" + 
					"					<span style=\"font-size:12pt; font-family:helvetica,arial,sans-serif\"><span color=\"#0059a4\" face=\"Roboto, sans-serif\"><span style=\"color:#999999\">Certificados de defunción: "+serialesDefuncion.size()+"</span></span><br>\r\n" + 
					"					<span color=\"#0059a4\" face=\"Roboto, sans-serif\"><span style=\"color:#999999\">Certificados de defunción: "+serialesNacidoVivo.size()+"</span><br>\r\n" + 
					"					</td>\r\n" + 
					"					</tr>\r\n" + 
					"					</tbody>\r\n" + 
					"			</table>";
			
			
			prop = System.getProperties();
            prop.put("mail.smtp.auth", "true");
            session = Session.getInstance(prop, (Authenticator)null);
            msg = (Message)new MimeMessage(session);
            msg.setFrom((Address)new InternetAddress(EMAIL_FROM));
            msg.setRecipients(Message.RecipientType.TO, (Address[])InternetAddress.parse(EMAIL_DESTINATION, false));
            msg.setSubject(EMAIL_SUBJECT);
            p1 = new MimeBodyPart();
            //p1.setText(EMAIL_TEXT);
            p1.setContent(htmlContent, "text/html;charset=UTF-8");
            p2 = new MimeBodyPart();                        
            mp = (Multipart)new MimeMultipart();
            mp.addBodyPart((BodyPart)p1);           
            msg.setContent(mp);
            t = (SMTPTransport)session.getTransport("smtp");            
            t.connect(SMTP_SERVER, USERNAME , PASSWORD);            
            t.sendMessage(msg, msg.getAllRecipients());
            logger.info("MENSAJE ENVIADO");
            t.close();            
		}
		
	   }  

}
