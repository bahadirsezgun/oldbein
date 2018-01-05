package tr.com.abasus.ptboss.mail;

import java.util.Properties;

import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

import tr.com.abasus.ptboss.settings.entity.DbMailTbl;
import tr.com.abasus.util.GlobalUtil;
import tr.com.abasus.util.SmtpAuthenticator;

public class MailSenderThread implements Runnable {

	private MailObj mailObj; 
	
	
	
	
	public MailSenderThread(MailObj mailObj) {
		super();
		this.mailObj = mailObj;
	}


	@Override
	public void run() {
		
		
      try {
			
			DbMailTbl dbMailTbl=GlobalUtil.mailSettings;
			
			if(dbMailTbl!=null){
					//String host = "mail.abasus.com.tr";
					String host = dbMailTbl.getHostName();
					String port =""+dbMailTbl.getSmtpPort(); 
					String auth=(dbMailTbl.getSmtpAuth()==0?"true":"false");
					String from=dbMailTbl.getMailUsername();
					
					
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
					InternetAddress[] toAddress = new InternetAddress[mailObj.getToWho().length];
					for (int i = 0; i < mailObj.getToWho().length; i++) {
						toAddress[i] = new InternetAddress(mailObj.getToWho()[i]);
					}
		 
					for (int i = 0; i < toAddress.length; i++) {
						message.addRecipient(RecipientType.TO, toAddress[i]);
					}
					// başlık
					message.setSubject(mailObj.getSubject());
					// içerik
					message.setText(mailObj.getContent());
					
					
					
					
					Transport transport = session.getTransport("smtp");
					transport.connect(host ,from, null);
					transport.sendMessage(message, message.getAllRecipients());
					transport.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}


	public MailObj getMailObj() {
		return mailObj;
	}


	public void setMailObj(MailObj mailObj) {
		this.mailObj = mailObj;
	}

	
}
