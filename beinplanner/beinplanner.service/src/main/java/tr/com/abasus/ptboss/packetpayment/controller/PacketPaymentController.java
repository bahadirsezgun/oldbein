package tr.com.abasus.ptboss.packetpayment.controller;

import java.io.IOException;
import java.util.List;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import tr.com.abasus.ptboss.facade.payment.PaymentClassFacade;
import tr.com.abasus.ptboss.facade.payment.PaymentFacadeService;
import tr.com.abasus.ptboss.facade.payment.PaymentMembershipFacade;
import tr.com.abasus.ptboss.facade.payment.PaymentPersonalFacade;
import tr.com.abasus.ptboss.mail.facade.MailService;
import tr.com.abasus.ptboss.packetpayment.entity.PacketPaymentClass;
import tr.com.abasus.ptboss.packetpayment.entity.PacketPaymentDetailFactory;
import tr.com.abasus.ptboss.packetpayment.entity.PacketPaymentFactory;
import tr.com.abasus.ptboss.packetpayment.entity.PacketPaymentInteface;
import tr.com.abasus.ptboss.packetpayment.entity.PacketPaymentMembership;
import tr.com.abasus.ptboss.packetpayment.entity.PacketPaymentPersonal;
import tr.com.abasus.ptboss.packetpayment.entity.PaymentConfirmQueryObj;
import tr.com.abasus.ptboss.ptuser.entity.User;
import tr.com.abasus.ptboss.result.entity.HmiResultObj;
import tr.com.abasus.util.PaymentConfirmUtil;
import tr.com.abasus.util.ProgramTypes;
import tr.com.abasus.util.ResultStatuObj;
import tr.com.abasus.util.SessionUtil;
import tr.com.abasus.util.UserTypes;

@Controller
@RequestMapping(value="/packetpayment")
public class PacketPaymentController {

	@Autowired
	PacketPaymentClass packetPaymentClass;
	
	@Autowired
	PacketPaymentMembership packetPaymentMembership;
	
	@Autowired
	PacketPaymentPersonal packetPaymentPersonal;
	
	@Autowired
	PaymentPersonalFacade paymentPersonalFacade;
	
	@Autowired
	PaymentClassFacade paymentClassFacade;
	
	@Autowired
	PaymentMembershipFacade paymentMembershipFacade;
	
	
	@Autowired
	MailService mailService;
	
	
	
	@RequestMapping(value="/findPaymentsToConfirm", method = RequestMethod.POST) 
	public @ResponseBody List<PacketPaymentFactory>	findPaymentsToConfirm(@RequestBody PaymentConfirmQueryObj paymentConfirmQueryObj , HttpServletRequest request){
		User user=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if((user.getUserType()==UserTypes.USER_TYPE_MEMBER_INT)){
			return null;
		}
		return packetPaymentPersonal.findPaymentToConfirm(paymentConfirmQueryObj.getUserName()+"%", paymentConfirmQueryObj.getUserSurname()+"%", paymentConfirmQueryObj.getConfirmed(), paymentConfirmQueryObj.getUnConfirmed());
	}
	
	
	
	@RequestMapping(value="/updatePaymentToConfirm", method = RequestMethod.POST) 
	public @ResponseBody PacketPaymentFactory	updatePaymentToConfirm(@RequestBody PacketPaymentFactory packetPaymentFactory, HttpServletRequest request){
		User user=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if((user.getUserType()==UserTypes.USER_TYPE_MEMBER_INT)){
			return null;
		}
		PacketPaymentInteface packetPaymentInteface=null;
		
		
		if(packetPaymentFactory instanceof PacketPaymentClass){
			packetPaymentInteface=packetPaymentClass;
		}else if(packetPaymentFactory instanceof PacketPaymentMembership){
			packetPaymentInteface=packetPaymentMembership;
		}else if(packetPaymentFactory instanceof PacketPaymentPersonal){
			packetPaymentInteface=packetPaymentPersonal;
		}
		
		if(packetPaymentFactory.getPayConfirm()==PaymentConfirmUtil.PAYMENT_UNCONFIRM){
			packetPaymentFactory.setPayConfirm(PaymentConfirmUtil.PAYMENT_CONFIRM);
		}else{
			packetPaymentFactory.setPayConfirm(PaymentConfirmUtil.PAYMENT_UNCONFIRM);
		}
		
		HmiResultObj hmiResultObj=packetPaymentInteface.updatePacketPayment(packetPaymentFactory);
		
		
		
		
		if(hmiResultObj.getResultStatu()==ResultStatuObj.RESULT_STATU_SUCCESS){
			
			List<PacketPaymentDetailFactory> packetPaymentDetailFactories=packetPaymentInteface.findPacketPaymentDetail(packetPaymentFactory.getPayId());
			if(packetPaymentDetailFactories.size()>0){
				for (PacketPaymentDetailFactory packetPaymentDetailFactory : packetPaymentDetailFactories) {
					packetPaymentDetailFactory.setPayConfirm(packetPaymentFactory.getPayConfirm());
					packetPaymentInteface.updatePacketPaymentDetail(packetPaymentDetailFactory);
				}
			}
			
			
			return packetPaymentFactory;
		}else{
			if(packetPaymentFactory.getPayConfirm()==1){
				packetPaymentFactory.setPayConfirm(0);
			}else{
				packetPaymentFactory.setPayConfirm(1);
			}
			
			return packetPaymentFactory;
		}
		
		
	}
	
	@RequestMapping(value="/findPacketPaymentBySaleId/{saleId}/{type}", method = RequestMethod.POST) 
	public @ResponseBody PacketPaymentFactory	findPacketPaymentBySaleId(@PathVariable("saleId") long saleId,@PathVariable("type") String type, HttpServletRequest request){
		User user=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if((user.getUserType()==UserTypes.USER_TYPE_MEMBER_INT)){
			return null;
		}
		PacketPaymentInteface packetPaymentInteface=null;
		
		
		if(type.equals(ProgramTypes.PACKET_PAYMENT_CLASS)){
			packetPaymentInteface=packetPaymentClass;
		}else if(type.equals(ProgramTypes.PACKET_PAYMENT_MEMBERSHIP)){
			packetPaymentInteface=packetPaymentMembership;
		}else if(type.equals(ProgramTypes.PACKET_PAYMENT_PERSONAL)){
			packetPaymentInteface=packetPaymentPersonal;
		}
		return packetPaymentInteface.findPacketPaymentBySaleId(saleId);
	}
	
	@RequestMapping(value="/findPacketPaymentByPayId/{payId}/{type}", method = RequestMethod.POST) 
	public @ResponseBody PacketPaymentFactory	findPacketPaymentByPayId(@PathVariable("payId") long payId,@PathVariable("type") String type, HttpServletRequest request){
		PacketPaymentInteface packetPaymentInteface=null;
		
		if(type.equals(ProgramTypes.PACKET_PAYMENT_CLASS)){
			packetPaymentInteface=packetPaymentClass;
		}else if(type.equals(ProgramTypes.PACKET_PAYMENT_MEMBERSHIP)){
			packetPaymentInteface=packetPaymentMembership;
		}else if(type.equals(ProgramTypes.PACKET_PAYMENT_PERSONAL)){
			packetPaymentInteface=packetPaymentPersonal;
		}
		
		return packetPaymentInteface.findPacketPaymentByPayId(payId);
		
		
	}
	
	
	@RequestMapping(value="/createPacketPayment", method = RequestMethod.POST) 
	public @ResponseBody HmiResultObj  createPacketPayment(@RequestBody PacketPaymentFactory packetPaymentFactory, HttpServletRequest request){
		
		User user=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if((user.getUserType()==UserTypes.USER_TYPE_MEMBER_INT)){
			return null;
		}
		
		PacketPaymentInteface packetPaymentInteface=null;
		if(packetPaymentFactory instanceof PacketPaymentPersonal){
			packetPaymentInteface=packetPaymentPersonal;
		}else if(packetPaymentFactory instanceof PacketPaymentClass){
			packetPaymentInteface=packetPaymentClass;
		}else if(packetPaymentFactory instanceof PacketPaymentMembership){
			packetPaymentInteface=packetPaymentMembership;
		}
		
		try {
			mailService.sendMailForPacketPayment(packetPaymentFactory,user.getFirmId());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		
		return packetPaymentInteface.createPacketPayment(packetPaymentFactory);
	}
	
	@RequestMapping(value="/deletePacketPayment", method = RequestMethod.POST) 
	public @ResponseBody HmiResultObj  deletePacketPayment(@RequestBody PacketPaymentFactory packetPaymentFactory, HttpServletRequest request){
		
		User user=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if((user.getUserType()==UserTypes.USER_TYPE_MEMBER_INT)){
			return null;
		}
		
		PaymentFacadeService paymentFacadeService=null;
		PacketPaymentInteface packetPaymentInteface=null;
		if(packetPaymentFactory instanceof PacketPaymentPersonal){
			packetPaymentInteface=packetPaymentPersonal;
			paymentFacadeService=paymentPersonalFacade;
		}else if(packetPaymentFactory instanceof PacketPaymentClass){
			packetPaymentInteface=packetPaymentClass;
			paymentFacadeService=paymentClassFacade;
		}else if(packetPaymentFactory instanceof PacketPaymentMembership){
			packetPaymentInteface=packetPaymentMembership;
			paymentFacadeService=paymentMembershipFacade;
		}
		
		if(!paymentFacadeService.canPaymentDelete(packetPaymentFactory)){
			HmiResultObj hmiResultObj=new HmiResultObj();
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
			hmiResultObj.setResultMessage("packetPaymentConfirmed");
			return hmiResultObj;
		}
		
		PacketPaymentFactory ppfInDb=packetPaymentInteface.findPacketPaymentByPayId(packetPaymentFactory.getPayId());
		
		
		try {
			mailService.sendMailForPacketPaymentDelete(ppfInDb,user.getFirmId());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		
		return packetPaymentInteface.deletePacketPayment(packetPaymentFactory.getPayId());
	}
	
	
	@RequestMapping(value="/deletePacketPaymentDetail/{payDetId}/{payId}/{type}", method = RequestMethod.POST) 
	public @ResponseBody HmiResultObj  deletePacketPaymentDetail(@PathVariable("payDetId") long payDetId,@PathVariable("payId") long payId,@PathVariable("type") String type, HttpServletRequest request){
		HmiResultObj hmiResultObj=new HmiResultObj();
		User user=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if((user.getUserType()==UserTypes.USER_TYPE_MEMBER_INT)){
			return null;
		}
		PacketPaymentInteface packetPaymentInteface=null;
		PaymentFacadeService paymentFacadeService=null;
		
		if(type.equals(ProgramTypes.PACKET_PAYMENT_CLASS)){
			packetPaymentInteface=packetPaymentClass;
			paymentFacadeService=paymentPersonalFacade;
		}else if(type.equals(ProgramTypes.PACKET_PAYMENT_MEMBERSHIP)){
			packetPaymentInteface=packetPaymentMembership;
			paymentFacadeService=paymentClassFacade;
		}else if(type.equals(ProgramTypes.PACKET_PAYMENT_PERSONAL)){
			packetPaymentInteface=packetPaymentPersonal;
			paymentFacadeService=paymentMembershipFacade;
		}
		
		if(!paymentFacadeService.canPaymentDetailDelete(payDetId, payId)){
			
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
			hmiResultObj.setResultMessage("packetPaymentConfirmed");
			return hmiResultObj;
		}
		
		
		
		hmiResultObj=packetPaymentInteface.deletePacketPaymentDetail(payDetId);
		PacketPaymentFactory packetPaymentFactory=packetPaymentInteface.findPacketPaymentByPayId(payId);
		
		List<PacketPaymentDetailFactory> packetPaymentDetailFactories=packetPaymentInteface.findPacketPaymentDetail(payId);
		if(packetPaymentDetailFactories.size()==0){
			packetPaymentInteface.deletePacketPayment(payId);
		}else{
			double totalPayAmount=0;
			for (PacketPaymentDetailFactory packetPaymentDetailFactory : packetPaymentDetailFactories) {
				totalPayAmount+=packetPaymentDetailFactory.getPayAmount();
			}
			packetPaymentFactory.setPayAmount(totalPayAmount);
			packetPaymentInteface.updatePacketPayment(packetPaymentFactory);
		}
		
		
      
		
		
		
		return hmiResultObj;
	}
	
}
