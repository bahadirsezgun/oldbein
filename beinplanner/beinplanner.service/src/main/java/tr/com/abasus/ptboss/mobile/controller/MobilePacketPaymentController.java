package tr.com.abasus.ptboss.mobile.controller;

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

import tr.com.abasus.ptboss.exceptions.UnAuthorizedUserException;
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
import tr.com.abasus.ptboss.ptuser.entity.User;
import tr.com.abasus.ptboss.ptuser.service.ProcessUserService;
import tr.com.abasus.ptboss.result.entity.HmiResultObj;
import tr.com.abasus.util.ProgramTypes;
import tr.com.abasus.util.ResultStatuObj;
import tr.com.abasus.util.SessionUtil;
import tr.com.abasus.util.UserTypes;

@Controller
@RequestMapping(value="/mobile/packetpayment")
public class MobilePacketPaymentController {

	
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
	
	
	@Autowired 
	ProcessUserService processUserService;
	
	
	@RequestMapping(value="/findPacketPaymentBySaleId/{saleId}/{type}/{userName}/{password}", method = RequestMethod.POST) 
	public @ResponseBody PacketPaymentFactory	findPacketPaymentBySaleId(@PathVariable("saleId") long saleId,@PathVariable("type") String type,@PathVariable String userName,@PathVariable String password, HttpServletRequest request) throws UnAuthorizedUserException{
		User user=processUserService.loginUserControl(userName, password);
		if(user==null){
			throw new UnAuthorizedUserException("");
    	}else if((user.getUserType()==UserTypes.USER_TYPE_MEMBER_INT)){
			throw new UnAuthorizedUserException("");
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
	
	@RequestMapping(value="/createPacketPayment/{userName}/{password}", method = RequestMethod.POST) 
	public @ResponseBody HmiResultObj  createPacketPayment(@PathVariable String userName,@PathVariable String password,@RequestBody PacketPaymentFactory packetPaymentFactory, HttpServletRequest request) throws UnAuthorizedUserException{
		
		User user=processUserService.loginUserControl(userName, password);
		if(user==null){
			throw new UnAuthorizedUserException("");
    	}else if((user.getUserType()==UserTypes.USER_TYPE_MEMBER_INT)){
			throw new UnAuthorizedUserException("");
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
	
	@RequestMapping(value="/deletePacketPayment/{payId}/{payType}/{userName}/{password}", method = RequestMethod.POST) 
	public @ResponseBody HmiResultObj  deletePacketPayment(@PathVariable long payId,@PathVariable String payType,@PathVariable String userName,@PathVariable String password, HttpServletRequest request) throws UnAuthorizedUserException{
		
		User user=processUserService.loginUserControl(userName, password);
		if(user==null){
			throw new UnAuthorizedUserException("");
    	}else if((user.getUserType()==UserTypes.USER_TYPE_MEMBER_INT)){
			throw new UnAuthorizedUserException("");
		}
		
		PaymentFacadeService paymentFacadeService=null;
		PacketPaymentInteface packetPaymentInteface=null;
		PacketPaymentFactory packetPaymentFactory=null;
		if(payType.equals(ProgramTypes.PACKET_PAYMENT_PERSONAL)){
			packetPaymentInteface=packetPaymentPersonal;
			paymentFacadeService=paymentPersonalFacade;
			packetPaymentFactory=new PacketPaymentPersonal();
		}else if(payType.equals(ProgramTypes.PACKET_PAYMENT_CLASS)){
			packetPaymentInteface=packetPaymentClass;
			paymentFacadeService=paymentClassFacade;
			packetPaymentFactory=new PacketPaymentClass();
		}else if(payType.equals(ProgramTypes.PACKET_PAYMENT_MEMBERSHIP)){
			packetPaymentInteface=packetPaymentMembership;
			paymentFacadeService=paymentMembershipFacade;
			packetPaymentFactory=new PacketPaymentMembership();
		}
		
		packetPaymentFactory.setPayId(payId);
		
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
	
	
	@RequestMapping(value="/deletePacketPaymentDetail/{payDetId}/{payId}/{type}/{userName}/{password}", method = RequestMethod.POST) 
	public @ResponseBody HmiResultObj  deletePacketPaymentDetail(@PathVariable("payDetId") long payDetId,@PathVariable("payId") long payId,@PathVariable("type") String type,@PathVariable String userName,@PathVariable String password, HttpServletRequest request) throws UnAuthorizedUserException{
		HmiResultObj hmiResultObj=new HmiResultObj();
		User user=processUserService.loginUserControl(userName, password);
		if(user==null){
			throw new UnAuthorizedUserException("");
    	}else if((user.getUserType()==UserTypes.USER_TYPE_MEMBER_INT)){
			throw new UnAuthorizedUserException("");
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
