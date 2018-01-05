package tr.com.abasus.ptboss.mail.facade;

import java.io.IOException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import tr.com.abasus.ptboss.mail.MailObj;
import tr.com.abasus.ptboss.mail.MultiMailSenderThread;
import tr.com.abasus.ptboss.packetpayment.entity.PacketPaymentFactory;
import tr.com.abasus.ptboss.packetsale.entity.PacketSaleFactory;
import tr.com.abasus.ptboss.ptuser.entity.User;
import tr.com.abasus.ptboss.result.entity.HmiResultObj;
import tr.com.abasus.util.GlobalUtil;
import tr.com.abasus.util.LangUtil;
import tr.com.abasus.util.ResultStatuObj;
import tr.com.abasus.util.RuleUtil;

@Service
public class MailService {

	
	public HmiResultObj sendMailForPasswordReminder(@RequestBody MailObj mailObj) throws IOException, MessagingException{
			
			MimeMultipart content = new MimeMultipart();
			MimeBodyPart mainPart = new MimeBodyPart();
			  
		    mainPart.setText(mailObj.getContent(),"UTF-8", "plain");
		    mainPart.addHeader("Content-Type", "text/plain; charset=UTF-8"); 
		    
		    content.addBodyPart(mainPart);
		    
			MimeBodyPart htmlPart = new MimeBodyPart();
			htmlPart.setContent( mailObj.getHtmlContent(), "text/html; charset=utf-8" );
			
			content.addBodyPart(htmlPart);
			
			mailObj.setMultipartMessage(content);
			
			MultiMailSenderThread mailSenderThread=new MultiMailSenderThread(mailObj);
			Thread thr=new Thread(mailSenderThread);
			thr.start();
		
			HmiResultObj hmiResultObj=new HmiResultObj();
			hmiResultObj.setResultMessage(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
		return hmiResultObj;
	}
	
	
	
	public HmiResultObj sendMailForPacketSale(PacketSaleFactory packetSaleFactory,int firmId) throws IOException, MessagingException{
		
	if(GlobalUtil.rules.getRuleNotice()==RuleUtil.RULE_OK)	{
		
		if(!GlobalUtil.defFirms.get(firmId).getFirmEmail().equals("")){
		
		
				MimeMultipart content = new MimeMultipart();
				MimeBodyPart mainPart = new MimeBodyPart();
				  
				MailObj mailObj=new MailObj();
				mailObj.setToWho(new String[]{GlobalUtil.defFirms.get(firmId).getFirmEmail()});
				
				////System.out.println("Mail "+GlobalUtil.defFirms.get(firmId).getFirmEmail());
				
				String htmlContent="<table>";
				
				for (User user : packetSaleFactory.getSaledMembers()) {
					 htmlContent+="<tr>";
					 htmlContent+="<td>"+user.getUserName()+" "+user.getUserSurname()+"</td>";
					 htmlContent+="<td>"+packetSaleFactory.getSalesDateStr()+"</td>";
					 htmlContent+="<td>"+packetSaleFactory.getProgName()+"</td>";
					 htmlContent+="<td><strong>"+(packetSaleFactory.getPacketPrice())+"</strong></td>";
					 htmlContent+="</tr>";
				}
				 htmlContent+="<table>";
				
				 
				 htmlContent+="<br/>";
				 htmlContent+=LangUtil.LANG_BRANCH+" : <strong>"+GlobalUtil.defFirms.get(firmId).getFirmName()+"</strong>";
				 
				 
			 if(packetSaleFactory.getMailContent()==null){
				 packetSaleFactory.setMailContent("");
			 }
			    mainPart.setText(packetSaleFactory.getMailContent(),"UTF-8", "plain");
			    mainPart.addHeader("Content-Type", "text/plain; charset=UTF-8"); 
			    
			    content.addBodyPart(mainPart);
			    
				MimeBodyPart htmlPart = new MimeBodyPart();
				htmlPart.setContent( htmlContent, "text/html; charset=utf-8" );
				
				content.addBodyPart(htmlPart);
				mailObj.setSubject(packetSaleFactory.getMailSubject());
				mailObj.setMultipartMessage(content);
				
				MultiMailSenderThread mailSenderThread=new MultiMailSenderThread(mailObj);
				Thread thr=new Thread(mailSenderThread);
				thr.start();
		}
	}
		HmiResultObj hmiResultObj=new HmiResultObj();
		hmiResultObj.setResultMessage(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
	return hmiResultObj;
 }
	
	
public HmiResultObj sendMailForPacketPayment(PacketPaymentFactory packetPaymentFactory,int firmId) throws IOException, MessagingException{
	
	if(GlobalUtil.rules.getRuleNotice()==RuleUtil.RULE_OK)	{	
	if(!GlobalUtil.defFirms.get(firmId).getFirmEmail().equals("")){
			
		MimeMultipart content = new MimeMultipart();
		MimeBodyPart mainPart = new MimeBodyPart();
		  
		MailObj mailObj=new MailObj();
		mailObj.setToWho(new String[]{GlobalUtil.defFirms.get(firmId).getFirmEmail()});
		
		
		
		
		
		String htmlContent="<table>";
		
			 htmlContent+="<tr>";
			 htmlContent+="<td>"+packetPaymentFactory.getUserName()+" "+packetPaymentFactory.getUserSurname()+"</td>";
			 htmlContent+="<td>"+packetPaymentFactory.getPayDateStr()+"</td>";
			 htmlContent+="<td>"+packetPaymentFactory.getPayAmount()+"</td>";
			 htmlContent+="</tr>";
		
		 htmlContent+="<table>";
		
		 htmlContent+="<br/>";
		 htmlContent+=	packetPaymentFactory.getPayComment();
		 
		 htmlContent+="<br/>";
		 htmlContent+=LangUtil.LANG_BRANCH+" : <strong>"+GlobalUtil.defFirms.get(firmId).getFirmName()+"</strong>";
		 
	
		 if(packetPaymentFactory.getMailContent()==null){
			 packetPaymentFactory.setMailContent("");
		 }
		 
	    mainPart.setText(packetPaymentFactory.getMailContent(),"UTF-8", "plain");
	    mainPart.addHeader("Content-Type", "text/plain; charset=UTF-8"); 
	    
	    content.addBodyPart(mainPart);
	    
		MimeBodyPart htmlPart = new MimeBodyPart();
		htmlPart.setContent( htmlContent, "text/html; charset=utf-8" );
		
		content.addBodyPart(htmlPart);
		mailObj.setSubject(packetPaymentFactory.getMailSubject());
		mailObj.setMultipartMessage(content);
		
		MultiMailSenderThread mailSenderThread=new MultiMailSenderThread(mailObj);
		Thread thr=new Thread(mailSenderThread);
		thr.start();
	
	}
	}	
		HmiResultObj hmiResultObj=new HmiResultObj();
		hmiResultObj.setResultMessage(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
	return hmiResultObj;
 }
	
public HmiResultObj sendMailForPacketPaymentDelete(PacketPaymentFactory packetPaymentFactory,int firmId) throws IOException, MessagingException{
	if(GlobalUtil.rules.getRuleNotice()==RuleUtil.RULE_OK)	{
	if(!GlobalUtil.defFirms.get(firmId).getFirmEmail().equals("")){
		
			MimeMultipart content = new MimeMultipart();
			MimeBodyPart mainPart = new MimeBodyPart();
			  
			MailObj mailObj=new MailObj();
			mailObj.setToWho(new String[]{GlobalUtil.defFirms.get(firmId).getFirmEmail()});
			
			String htmlContent="<table>";
			
				 htmlContent+="<tr>";
				 htmlContent+="<td>"+packetPaymentFactory.getUserName()+" "+packetPaymentFactory.getUserSurname()+"</td>";
				 htmlContent+="<td>"+packetPaymentFactory.getPayDateStr()+"</td>";
				 htmlContent+="<td>"+packetPaymentFactory.getPayAmount()+"</td>";
				 htmlContent+="</tr>";
			
			 htmlContent+="<table>";
			
		    mainPart.setText(packetPaymentFactory.getMailContent(),"UTF-8", "plain");
		    mainPart.addHeader("Content-Type", "text/plain; charset=UTF-8"); 
		    
		    content.addBodyPart(mainPart);
		    
			MimeBodyPart htmlPart = new MimeBodyPart();
			htmlPart.setContent( htmlContent, "text/html; charset=utf-8" );
			
			content.addBodyPart(htmlPart);
			mailObj.setSubject(packetPaymentFactory.getMailSubject());
			mailObj.setMultipartMessage(content);
			
			MultiMailSenderThread mailSenderThread=new MultiMailSenderThread(mailObj);
			Thread thr=new Thread(mailSenderThread);
			thr.start();

	}
	}
	HmiResultObj hmiResultObj=new HmiResultObj();
	hmiResultObj.setResultMessage(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
return hmiResultObj;
}
	
}
