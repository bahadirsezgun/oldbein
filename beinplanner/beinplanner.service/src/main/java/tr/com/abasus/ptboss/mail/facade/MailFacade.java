package tr.com.abasus.ptboss.mail.facade;

import java.io.IOException;
import java.net.URL;

import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;

import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import tr.com.abasus.ptboss.mail.MailObj;
import tr.com.abasus.ptboss.mail.MailSenderThread;
import tr.com.abasus.ptboss.mail.MultiMailSenderThread;
import tr.com.abasus.ptboss.packetpayment.entity.PacketPaymentFactory;
import tr.com.abasus.ptboss.packetsale.entity.PacketSaleFactory;
import tr.com.abasus.ptboss.ptuser.entity.User;
import tr.com.abasus.ptboss.ptuser.entity.UserPotential;
import tr.com.abasus.ptboss.result.entity.HmiResultObj;
import tr.com.abasus.util.GlobalUtil;
import tr.com.abasus.util.OhbeUtil;
import tr.com.abasus.util.ResultStatuObj;
import tr.com.abasus.util.RuleUtil;

@Controller
@RequestMapping(value="/mail")
public class MailFacade {

	
	public String sendMailForBonusPayment(){
		
		
		return null;
	}
	
	@RequestMapping(value="/sendMailForPotential", method = RequestMethod.POST) 
	public @ResponseBody HmiResultObj sendMailForPotential(@RequestBody UserPotential userPotential){
		if(GlobalUtil.rules.getRuleNotice()==RuleUtil.RULE_OK){
			MailObj mailObj=new MailObj();
			mailObj.setContent(userPotential.getMailContent());
			mailObj.setSubject(userPotential.getMailSubject());
			
			String[] toWho=new String[]{GlobalUtil.mailSettings.getFromName()};
			
			
			mailObj.setToWho(toWho);
			mailObj.setToWhom(GlobalUtil.mailSettings.getFromName());
			
			
			MailSenderThread mailSenderThread=new MailSenderThread(mailObj);
			Thread thr=new Thread(mailSenderThread);
			thr.start();
		}
		
		HmiResultObj hmiResultObj=new HmiResultObj();
		hmiResultObj.setResultMessage(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
	return hmiResultObj;
	}
	
	
	
    
	@RequestMapping(value="/sendMailForPacketSale", method = RequestMethod.POST) 
	public @ResponseBody HmiResultObj sendMailForPacketSale(@RequestBody PacketSaleFactory packetSaleFactory){
		if(GlobalUtil.rules.getRuleNotice()==RuleUtil.RULE_OK){
			MailObj mailObj=new MailObj();
			mailObj.setContent(packetSaleFactory.getMailContent());
			mailObj.setSubject(packetSaleFactory.getMailSubject());
			
			String[] toWho=new String[]{GlobalUtil.mailSettings.getFromName()};
			
			
			mailObj.setToWho(toWho);
			mailObj.setToWhom(GlobalUtil.mailSettings.getFromName());
			
			
			MailSenderThread mailSenderThread=new MailSenderThread(mailObj);
			Thread thr=new Thread(mailSenderThread);
			thr.start();
		}
		
		HmiResultObj hmiResultObj=new HmiResultObj();
		hmiResultObj.setResultMessage(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
	return hmiResultObj;
	}
    
	@RequestMapping(value="/sendMailForPacketPayment", method = RequestMethod.POST) 
	public @ResponseBody HmiResultObj sendMailForPacketPayment(@RequestBody PacketPaymentFactory packetPaymentFactory){
		if(GlobalUtil.rules.getRuleNotice()==RuleUtil.RULE_OK){
			MailObj mailObj=new MailObj();
			mailObj.setContent(packetPaymentFactory.getMailContent());
			mailObj.setSubject(packetPaymentFactory.getMailSubject());
			
			String[] toWho=new String[]{GlobalUtil.mailSettings.getFromName()};
			
			
			mailObj.setToWho(toWho);
			mailObj.setToWhom(GlobalUtil.mailSettings.getFromName());
			
			
			MailSenderThread mailSenderThread=new MailSenderThread(mailObj);
			Thread thr=new Thread(mailSenderThread);
			thr.start();
		}
		
		
		HmiResultObj hmiResultObj=new HmiResultObj();
		hmiResultObj.setResultMessage(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
	return hmiResultObj;
	}
    
	@RequestMapping(value="/sendMailForSpecialDates", method = RequestMethod.POST) 
	public @ResponseBody HmiResultObj sendMailForSpecialDates(@RequestBody User user) throws IOException, MessagingException{
			MailObj mailObj=new MailObj();
			
			mailObj.setSubject(user.getMailSubject());
			
			String[] toWho=new String[]{user.getUserEmail()};
			MimeMultipart content = new MimeMultipart();

		    MimeBodyPart mainPart = new MimeBodyPart();
			  
		    mainPart.setText(user.getMailContent(),"UTF-8", "plain");
		    mainPart.addHeader("Content-Type", "text/plain; charset=UTF-8"); 
		    
		    content.addBodyPart(mainPart);
		    
			MimeBodyPart imagePart = new MimeBodyPart();
			imagePart.attachFile(OhbeUtil.ROOT_STOCK_FOLDER+"/birth.JPG");
			
			mailObj.setToWho(toWho);
			mailObj.setToWhom(GlobalUtil.mailSettings.getFromName());
			
			content.addBodyPart(imagePart);
			
			mailObj.setMultipartMessage(content);
			
			MultiMailSenderThread mailSenderThread=new MultiMailSenderThread(mailObj);
			Thread thr=new Thread(mailSenderThread);
			thr.start();
		
			HmiResultObj hmiResultObj=new HmiResultObj();
			hmiResultObj.setResultMessage(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
		return hmiResultObj;
	}
	
	
    
}
