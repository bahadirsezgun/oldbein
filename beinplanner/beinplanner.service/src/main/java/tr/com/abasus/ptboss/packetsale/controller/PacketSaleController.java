package tr.com.abasus.ptboss.packetsale.controller;

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
import tr.com.abasus.ptboss.mail.facade.MailService;
import tr.com.abasus.ptboss.packetsale.decorator.IPacketSale;
import tr.com.abasus.ptboss.packetsale.entity.PacketSaleClass;
import tr.com.abasus.ptboss.packetsale.entity.PacketSaleFactory;
import tr.com.abasus.ptboss.packetsale.entity.PacketSaleInterface;
import tr.com.abasus.ptboss.packetsale.entity.PacketSaleMembership;
import tr.com.abasus.ptboss.packetsale.entity.PacketSalePersonal;
import tr.com.abasus.ptboss.ptuser.entity.User;
import tr.com.abasus.ptboss.result.entity.HmiResultObj;
import tr.com.abasus.util.ProgramTypes;
import tr.com.abasus.util.SessionUtil;
import tr.com.abasus.util.UserTypes;

@Controller
@RequestMapping(value="/packetsale")
public class PacketSaleController {

	
	
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
	
	
	@RequestMapping(value="/findUserBoughtPackets/{userId}/{progType}", method = RequestMethod.POST) 
	public @ResponseBody List<PacketSaleFactory> findUserBoughtPackets(@PathVariable("userId") long userId,@PathVariable("progType") int progType, HttpServletRequest request ){
		
		User user=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if((user.getUserType()==UserTypes.USER_TYPE_MEMBER_INT)){
			return null;
		}
		PacketSaleInterface packetSaleInterface=null;
		
		
		if(progType==ProgramTypes.PROGRAM_PERSONAL){
			packetSaleInterface=packetSalePersonal;
		}else if(progType==ProgramTypes.PROGRAM_CLASS){
			packetSaleInterface=packetSaleClass;
		}else if(progType==ProgramTypes.PROGRAM_MEMBERSHIP){
			packetSaleInterface=packetSaleMembership;
		}
		
		return packetSaleInterface.findUserBoughtPackets(userId);
	}
	
	@RequestMapping(value="/findAllUserBoughtPackets/{userId}", method = RequestMethod.POST) 
	public @ResponseBody List<PacketSaleFactory> findAllUserBoughtPackets(@PathVariable("userId") long userId, HttpServletRequest request ){
		
		User user=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if((user.getUserType()==UserTypes.USER_TYPE_MEMBER_INT)){
			return null;
		}
		
		
		return iPacketSale.findAllUserBoughtPackets(userId);
	}
	
	
	@RequestMapping(value="/searchSaledPackets", method = RequestMethod.POST) 
	public @ResponseBody List<PacketSaleFactory> searchSaledPackets(@RequestBody PacketSaleFactory packetSaleQuery){
		
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
	
	
	@RequestMapping(value="/findSaledPacketsById/{saleId}/{progType}", method = RequestMethod.POST) 
	public @ResponseBody PacketSaleFactory findSaledPacketsById(@PathVariable("saleId") long saleId,@PathVariable("progType") int progType, HttpServletRequest request){
		User user=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if((user.getUserType()==UserTypes.USER_TYPE_MEMBER_INT)){
			return null;
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
	
	
	@RequestMapping(value="/findPacketsBySaleIds/{progType}", method = RequestMethod.POST) 
	public @ResponseBody List<PacketSaleFactory> findSaledPacketsById(@RequestBody List<PacketSaleFactory> packetSaleFactories ,@PathVariable("progType") int progType, HttpServletRequest request){
		User user=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if((user.getUserType()==UserTypes.USER_TYPE_MEMBER_INT)){
			return null;
		}
		PacketSaleInterface packetSaleInterface=null;
		
		
		if(progType==ProgramTypes.PROGRAM_PERSONAL){
			packetSaleInterface=packetSalePersonal;
		}else if(progType==ProgramTypes.PROGRAM_CLASS){
			packetSaleInterface=packetSaleClass;
		}else if(progType==ProgramTypes.PROGRAM_MEMBERSHIP){
			packetSaleInterface=packetSaleMembership;
		}
		
		return packetSaleInterface.findPacketsBySaleIds(packetSaleFactories);
	}
	
	@RequestMapping(value="/findUserBySaleId/{progType}/{saleId}", method = RequestMethod.POST) 
	public @ResponseBody User findUserBySaleId(@PathVariable("progType") int progType,@PathVariable("saleId") long saleId, HttpServletRequest request){
	
		PacketSaleInterface packetSaleInterface=null;
		
		if(progType==ProgramTypes.PROGRAM_PERSONAL){
			packetSaleInterface=packetSalePersonal;
		}else if(progType==ProgramTypes.PROGRAM_CLASS){
			packetSaleInterface=packetSaleClass;
		}else if(progType==ProgramTypes.PROGRAM_MEMBERSHIP){
			packetSaleInterface=packetSaleMembership;
		}
		
		User user=packetSaleInterface.findUserBySaleId(saleId);
		
		return user;
	}
	

	@RequestMapping(value="/saleNewPacket", method = RequestMethod.POST) 
	public @ResponseBody List<HmiResultObj> saleNewPacket(@RequestBody PacketSaleFactory packetSaleFactory, HttpServletRequest request) throws UnAuthorizedUserException{
		User user=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if((user.getUserType()==UserTypes.USER_TYPE_MEMBER_INT)){
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
		
		
		
		try {
			mailService.sendMailForPacketSale(packetSaleFactory,user.getFirmId());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		
		
		
		return packetSaleInterface.saleNewPacket(packetSaleFactory);
	}
	
	
	@RequestMapping(value="/saleUpdatePacket", method = RequestMethod.POST) 
	public @ResponseBody HmiResultObj saleUpdatePacket(@RequestBody PacketSaleFactory packetSaleFactory, HttpServletRequest request) throws UnAuthorizedUserException{
		User user=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if((user.getUserType()==UserTypes.USER_TYPE_MEMBER_INT)){
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
		
		
		
		return packetSaleInterface.saleUpdatePacket(packetSaleFactory);
	}
	
	
	@RequestMapping(value="/deleteSalePacket", method = RequestMethod.POST) 
	public @ResponseBody HmiResultObj deleteSalePacket(@RequestBody PacketSaleFactory packetSaleFactory){
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
	
	
	
}
