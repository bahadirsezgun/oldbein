package tr.com.abasus.ptboss.mobile.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import tr.com.abasus.ptboss.exceptions.UnAuthorizedUserException;
import tr.com.abasus.ptboss.mail.facade.MailService;
import tr.com.abasus.ptboss.packetsale.decorator.IPacketSale;
import tr.com.abasus.ptboss.packetsale.entity.PacketSaleClass;
import tr.com.abasus.ptboss.packetsale.entity.PacketSaleFactory;
import tr.com.abasus.ptboss.packetsale.entity.PacketSaleInterface;
import tr.com.abasus.ptboss.packetsale.entity.PacketSaleMembership;
import tr.com.abasus.ptboss.packetsale.entity.PacketSalePersonal;
import tr.com.abasus.ptboss.ptuser.entity.User;
import tr.com.abasus.ptboss.ptuser.service.ProcessUserService;
import tr.com.abasus.ptboss.result.entity.HmiResultObj;
import tr.com.abasus.util.DateTimeUtil;
import tr.com.abasus.util.GlobalUtil;
import tr.com.abasus.util.OhbeUtil;
import tr.com.abasus.util.ProgramTypes;
import tr.com.abasus.util.SessionUtil;
import tr.com.abasus.util.UserTypes;

@Controller
@RequestMapping(value="/mobile/packetsale")
public class MobilePacketSaleController {

	@Autowired
	PacketSaleClass packetSaleClass;
	
	@Autowired
	PacketSalePersonal packetSalePersonal;
	
	@Autowired
	PacketSaleMembership packetSaleMembership;
	
	@Autowired
    IPacketSale iPacketSale;
	
	
	
	
	@Autowired
	MailService mailService;
	
	@Autowired 
	ProcessUserService processUserService;
	
	
	@RequestMapping(value="/searchSaledPackets/{userName}/{password}", method = RequestMethod.POST) 
	public @ResponseBody List<PacketSaleFactory> searchSaledPackets(@PathVariable String userName,@PathVariable String password,@RequestBody PacketSaleFactory packetSaleQuery) throws UnAuthorizedUserException{
		
		
		User user=processUserService.loginUserControl(userName, password);
		if(user==null){
			throw new UnAuthorizedUserException("");
    	}else if((user.getUserType()==UserTypes.USER_TYPE_MEMBER_INT)){
			throw new UnAuthorizedUserException("");
		}
		
		
		
		PacketSaleInterface packetSaleInterface=null;
		if(packetSaleQuery instanceof PacketSalePersonal){
			packetSaleInterface=packetSalePersonal;
		}else if(packetSaleQuery instanceof PacketSaleClass){
			packetSaleInterface=packetSaleClass;
		}else if(packetSaleQuery instanceof PacketSaleMembership){
			packetSaleInterface=packetSaleMembership;
		}
		
		
		return packetSaleInterface.searchSaledPackets(packetSaleQuery);
		
	}
	
	@RequestMapping(value="/saleNewPacket/{userName}/{password}", method = RequestMethod.POST) 
	public @ResponseBody HmiResultObj saleNewPacket(@PathVariable String userName,@PathVariable String password,@RequestBody PacketSaleFactory packetSaleFactory, HttpServletRequest request) throws UnAuthorizedUserException{
		////System.out.println("CALL SALE NEW PACKET ..");
		////System.out.println(OhbeUtil.getDateStrByFormat(packetSaleFactory.getSalesDate(), "dd/MM/yyyy HH:ss"));
		
		User user=processUserService.loginUserControl(userName, password);
		if(user==null){
			throw new UnAuthorizedUserException("");
    	}else if((user.getUserType()==UserTypes.USER_TYPE_MEMBER_INT)){
			throw new UnAuthorizedUserException("");
		}
		PacketSaleInterface packetSaleInterface=null;
		if(packetSaleFactory instanceof PacketSalePersonal){
			packetSaleInterface=packetSalePersonal;
		}else if(packetSaleFactory instanceof PacketSaleClass){
			packetSaleInterface=packetSaleClass;
		}else if(packetSaleFactory instanceof PacketSaleMembership){
			packetSaleInterface=packetSaleMembership;
		}
		
		packetSaleFactory.setFirmId(user.getFirmId());
		/*
		User userInSale=new User();
		userInSale.setUserId(packetSaleFactory.getUserId());
		List<User> saledMembers=new ArrayList<User>();
		saledMembers.add(userInSale);
		*/		
		
		String salesDateStr=DateTimeUtil.convertMobileDateToSystemDate(packetSaleFactory.getSalesDateStr());// OhbeUtil.getDateStrByFormat(salesDate, GlobalUtil.global.getPtScrDateFormat());
		Date salesDate=OhbeUtil.getThatDayFormatNotNull(salesDateStr, GlobalUtil.global.getPtScrDateFormat());//OhbeUtil.getThatDayFormatNotNull(salesDateStr, "dd/MM/yyyy");
		packetSaleFactory.setSalesDate(salesDate);
		
		//packetSaleFactory.setSaledMembers(saledMembers);
		
		try {
			mailService.sendMailForPacketSale(packetSaleFactory,user.getFirmId());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		return packetSaleInterface.saleNewPacket(packetSaleFactory).get(0);
	}
	
	
	@RequestMapping(value="/saleUpdatePacket/{userName}/{password}", method = RequestMethod.POST) 
	public @ResponseBody HmiResultObj saleUpdatePacket(@PathVariable String userName,@PathVariable String password,@RequestBody PacketSaleFactory packetSaleFactory, HttpServletRequest request) throws UnAuthorizedUserException{
		User user=processUserService.loginUserControl(userName, password);
		if(user==null){
			throw new UnAuthorizedUserException("");
    	}else if((user.getUserType()==UserTypes.USER_TYPE_MEMBER_INT)){
			throw new UnAuthorizedUserException("");
		}
		PacketSaleInterface packetSaleInterface=null;
		if(packetSaleFactory instanceof PacketSalePersonal){
			packetSaleInterface=packetSalePersonal;
		}else if(packetSaleFactory instanceof PacketSaleClass){
			packetSaleInterface=packetSaleClass;
		}else if(packetSaleFactory instanceof PacketSaleMembership){
			packetSaleInterface=packetSaleMembership;
		}
		
		String salesDateStr=DateTimeUtil.convertMobileDateToSystemDate(packetSaleFactory.getSalesDateStr());// OhbeUtil.getDateStrByFormat(salesDate, GlobalUtil.global.getPtScrDateFormat());
		Date salesDate=OhbeUtil.getThatDayFormatNotNull(salesDateStr, GlobalUtil.global.getPtScrDateFormat());//OhbeUtil.getThatDayFormatNotNull(salesDateStr, "dd/MM/yyyy");
		packetSaleFactory.setSalesDate(salesDate);
		
		
		packetSaleFactory.setFirmId(user.getFirmId());
		return packetSaleInterface.saleUpdatePacket(packetSaleFactory);
	}
	
	
	@RequestMapping(value="/deleteSalePacket/{userName}/{password}", method = RequestMethod.POST) 
	public @ResponseBody HmiResultObj deleteSalePacket(@PathVariable String userName,@PathVariable String password,@RequestBody PacketSaleFactory packetSaleFactory) throws UnAuthorizedUserException{
		
		User user=processUserService.loginUserControl(userName, password);
		if(user==null){
			throw new UnAuthorizedUserException("");
    	}else if((user.getUserType()==UserTypes.USER_TYPE_MEMBER_INT)){
			throw new UnAuthorizedUserException("");
		}
		
		PacketSaleInterface packetSaleInterface=null;
		if(packetSaleFactory instanceof PacketSalePersonal){
			packetSaleInterface=packetSalePersonal;
		}else if(packetSaleFactory instanceof PacketSaleClass){
			packetSaleInterface=packetSaleClass;
		}else if(packetSaleFactory instanceof PacketSaleMembership){
			packetSaleInterface=packetSaleMembership;
		}
		return packetSaleInterface.deleteSalePacket(packetSaleFactory);
	}
	
	
	@RequestMapping(value="/findUserBoughtPackets/{userId}/{userName}/{password}", method = RequestMethod.POST) 
	public @ResponseBody List<PacketSaleFactory> findUserBoughtPackets(@PathVariable("userId") long userId,@PathVariable String userName,@PathVariable String password, HttpServletRequest request ) throws UnAuthorizedUserException{
		
		User user=processUserService.loginUserControl(userName, password);
		if(user==null){
			throw new UnAuthorizedUserException("");
    	}else if((user.getUserType()==UserTypes.USER_TYPE_MEMBER_INT)){
			throw new UnAuthorizedUserException("");
		}
		
		iPacketSale=packetSalePersonal;
		
		 List<PacketSaleFactory> packetSaleFactories=iPacketSale.findAllUserBoughtPackets(userId);
		 for (PacketSaleFactory packetSaleFactory : packetSaleFactories) {
			 packetSaleFactory.setPtCurrency(GlobalUtil.global.getPtCurrency());
		}
		
		
		return packetSaleFactories;
	}
	
	
	
	@RequestMapping(value="/findSaledPacketsById/{saleId}/{progType}/{userName}/{password}", method = RequestMethod.POST) 
	public @ResponseBody PacketSaleFactory findSaledPacketsById(@PathVariable String userName,@PathVariable String password,@PathVariable("saleId") long saleId,@PathVariable("progType") int progType, HttpServletRequest request) throws UnAuthorizedUserException{
		User user=processUserService.loginUserControl(userName, password);
		if(user==null){
			throw new UnAuthorizedUserException("");
    	}else if((user.getUserType()==UserTypes.USER_TYPE_MEMBER_INT)){
			throw new UnAuthorizedUserException("");
		}
		PacketSaleInterface packetSaleInterface=null;
		
		
		if(progType==ProgramTypes.PROGRAM_PERSONAL){
			packetSaleInterface=packetSalePersonal;
		}else if(progType==ProgramTypes.PROGRAM_CLASS){
			packetSaleInterface=packetSaleClass;
		}else if(progType==ProgramTypes.PROGRAM_MEMBERSHIP){
			packetSaleInterface=packetSaleMembership;
		}
		
		return packetSaleInterface.findSaledPacketsById(saleId);
	}
	
	
}
