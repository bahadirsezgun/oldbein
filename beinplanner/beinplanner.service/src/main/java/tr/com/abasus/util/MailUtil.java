package tr.com.abasus.util;


import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

import org.apache.tools.ant.taskdefs.email.Message;

import tr.com.abasus.ptboss.settings.entity.DbMailTbl;

public class MailUtil {

	public static void main(String[] args) {
		String[] to=new String[1];
		to[0]="abasusptboss@gmail.com";
		
		MailUtil.sendEmail(to, "bbcsezgun@gmail.com", "subject", "text");
	}
	
	public static void sendEmail(String[] to, String from,String subject,String text){
		
		Properties props = new Properties();
	    props.put("mail.smtp.host", "smtp.gmail.com");
	    /*props.put("mail.smtp.socketFactory.port", "465");
	    props.put("mail.smtp.socketFactory.class",
	            "javax.net.ssl.SSLSocketFactory");*/
	    props.put("mail.smtp.auth", "true");
	    props.put("mail.smtp.port", "465"); 
        props.put("mail.smtp.ssl.enable", "true");

        SmtpAuthenticator authentication = new SmtpAuthenticator();
		Session session = Session.getDefaultInstance(props, authentication);
		
		
		
		try {

			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress("bbcsezgun@gmail.com"));
			message.setRecipients(MimeMessage.RecipientType.TO,
				InternetAddress.parse(to[0]));
			message.setSubject("Testing Subject");
			message.setText("Dear Mail Crawler,"
				+ "\n\n No spam to my email, please!");

			Transport.send(message);

			////System.out.println("Done");

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
	
	
	public static void sendEmailFromPtBoss(String[] to, String from,String subject,String text){
		try {
			
			DbMailTbl dbMailTbl=GlobalUtil.mailSettings;
			
			
			//String host = "mail.abasus.com.tr";
			String host = dbMailTbl.getHostName();
			String port =""+dbMailTbl.getSmtpPort(); 
			String auth=(dbMailTbl.getSmtpAuth()==0?"true":"false");
			
			Properties props = System.getProperties();
			//props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.host", host);
			props.put("mail.smtp.user", from);
		    props.put("mail.smtp.port", port);
			props.put("mail.smtp.auth", auth);
			if(dbMailTbl.getUseSsl()==0)
			 props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

			
			
			SmtpAuthenticator authentication = new SmtpAuthenticator();
			Session session = Session.getDefaultInstance(props, authentication);
			
			
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));
			InternetAddress[] toAddress = new InternetAddress[to.length];
			for (int i = 0; i < to.length; i++) {
				toAddress[i] = new InternetAddress(to[i]);
			}
 
			for (int i = 0; i < toAddress.length; i++) {
				message.addRecipient(RecipientType.TO, toAddress[i]);
			}
			// başlık
			message.setSubject(subject);
			// içerik
			message.setText(text);
			Transport transport = session.getTransport("smtp");
			transport.connect(host ,from, null);
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void sendEmailFromAbasus(String[] to, String from,String subject,String text){
		try {
			
			DbMailTbl dbMailTbl=GlobalUtil.mailSettings;
			if(dbMailTbl==null){
				dbMailTbl=new DbMailTbl();
				dbMailTbl.setFromName("info@abasus.com.tr");
				dbMailTbl.setHostName("mail.abasus.com.tr");
				dbMailTbl.setMailUsername("info@abasus.com.tr");
				dbMailTbl.setMailPassword("xqEwff3k6LMy22fdsweeewsaa");
				
			}
			
			//String host = "mail.abasus.com.tr";
			String host = dbMailTbl.getHostName();
			String port =""+dbMailTbl.getSmtpPort(); 
			String auth=(dbMailTbl.getSmtpAuth()==0?"true":"false");
			
			Properties props = System.getProperties();
			//props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.host", host);
			props.put("mail.smtp.user", from);
		    props.put("mail.smtp.port", port);
			props.put("mail.smtp.auth", auth);
 
			SmtpAuthenticator authentication = new SmtpAuthenticator();
			Session session = Session.getDefaultInstance(props, authentication);
			
			
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));
			InternetAddress[] toAddress = new InternetAddress[to.length];
			for (int i = 0; i < to.length; i++) {
				toAddress[i] = new InternetAddress(to[i]);
			}
 
			for (int i = 0; i < toAddress.length; i++) {
				message.addRecipient(RecipientType.TO, toAddress[i]);
			}
			// başlık
			message.setSubject(subject);
			// içerik
			message.setText(text);
			Transport transport = session.getTransport("smtp");
			transport.connect(host, from, null);
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}


