package tr.com.abasus.ptboss.packetsale.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonTypeName;

import tr.com.abasus.ptboss.facade.sale.SaleFacadeService;
import tr.com.abasus.ptboss.facade.schedule.ScheduleMembershipFacade;
import tr.com.abasus.ptboss.packetpayment.entity.PacketPaymentFactory;
import tr.com.abasus.ptboss.packetpayment.service.PacketPaymentService;
import tr.com.abasus.ptboss.packetsale.service.PacketSaleService;
import tr.com.abasus.ptboss.program.entity.ProgramMembership;
import tr.com.abasus.ptboss.ptuser.entity.User;
import tr.com.abasus.ptboss.result.entity.HmiResultObj;
import tr.com.abasus.ptboss.schedule.entity.ScheduleFactory;
import tr.com.abasus.ptboss.schedule.entity.ScheduleMembershipPlan;
import tr.com.abasus.util.GlobalUtil;
import tr.com.abasus.util.OhbeUtil;
import tr.com.abasus.util.ResultStatuObj;
import tr.com.abasus.util.StatuTypes;

@Component(value="packetSaleMembership")
@Scope("prototype")
@JsonTypeName("psm")
public class PacketSaleMembership extends PacketSaleFactory {

	@Autowired
	PacketSaleService packetSaleService;
	
	@Autowired
	PacketPaymentService packetPaymentService;
	
	@Autowired
	ScheduleMembershipPlan scheduleMembershipPlan;
	
	@Autowired
	ProgramMembership programMembership;
	
	@Autowired
	@Qualifier(value="saleMembershipFacade")
	SaleFacadeService saleFacadeService;
	
	@Autowired
	ScheduleMembershipFacade scheduleMembershipFacade;
	
	@Override
	public List<PacketSaleFactory> findUserBoughtPackets(long userId) {
		return packetSaleService.findMembershipUserBoughtPackets(userId);
	}



	@Override
	public PacketSaleFactory findSaledPacketsById(long saleId) {
		return packetSaleService.findMembershipPacketSaleById(saleId);
	}

	@Override
	public List<HmiResultObj> saleNewPacket(PacketSaleFactory packetSaleFactory) {
		List<HmiResultObj> hmiResultObjs=new ArrayList<HmiResultObj>();
		List<User> saledMembers=packetSaleFactory.getSaledMembers();
		
		
		for (User member : saledMembers) {
			
			Date smpStartDate=OhbeUtil.getThatDayFormatNotNull(packetSaleFactory.getSmpStartDateStr(), GlobalUtil.global.getPtDbDateFormat());
			HmiResultObj canHmiResult= saleFacadeService.canSale(member.getUserId(), smpStartDate);
				
			if(canHmiResult.getResultStatu()==ResultStatuObj.RESULT_STATU_SUCCESS){
				packetSaleFactory.setSalesDate(OhbeUtil.getThatDayFormatNotNull(packetSaleFactory.getSalesDateStr(), GlobalUtil.global.getPtDbDateFormat()));
				packetSaleFactory.setUserId(member.getUserId());
				packetSaleFactory.setSaleId(0);
				packetSaleFactory.setChangeDate(GlobalUtil.getCurrentDateByTimeZone());
				HmiResultObj hmiResultObj=packetSaleService.buyMembershipPacket((PacketSaleMembership)packetSaleFactory);
				
				if(hmiResultObj.getResultStatu()!=ResultStatuObj.RESULT_STATU_FAIL){
					
					ScheduleFactory scheduleFactory=new ScheduleMembershipPlan();
					scheduleFactory.setSmpComment(packetSaleFactory.getSalesComment());
					scheduleFactory.setProgId(packetSaleFactory.getProgId());
					scheduleFactory.setSaleId(packetSaleFactory.getSaleId());
					scheduleFactory.setUserId(member.getUserId());
					scheduleFactory.setSmpStartDate(smpStartDate);
					scheduleFactory.setSmpFreezeCount(0);
					scheduleFactory.setSmpPrice(packetSaleFactory.getPacketPrice());
					scheduleFactory.setSmpStatus(StatuTypes.ACTIVE);
					scheduleFactory.setSmpStartDateStr(packetSaleFactory.getSmpStartDateStr());
					
					scheduleFactory.setFirmId(packetSaleFactory.getFirmId());
					
					scheduleMembershipPlan.createPlan(scheduleFactory);
				}
				
				hmiResultObjs.add(hmiResultObj);
			
			
			}else{
				hmiResultObjs.add(canHmiResult);	
			}
			
			
			
		}
		
		return hmiResultObjs;
	}

	
	
	@Override
	public HmiResultObj deleteSalePacket(PacketSaleFactory packetSaleFactory) {
		HmiResultObj hmiResultObj= scheduleMembershipFacade.canScheduleDelete(packetSaleFactory);
		
		if(hmiResultObj.getResultStatu()==ResultStatuObj.RESULT_STATU_FAIL)
			return hmiResultObj;
		
		return packetSaleService.deleteMembershipPacket((PacketSaleMembership)packetSaleFactory);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PacketSaleFactory> findPacketsBySaleIds(List<PacketSaleFactory> packetSaleFactories) {
		List<PacketSaleFactory> packetSaleFact=new ArrayList<PacketSaleFactory>();
		List<? extends PacketSaleFactory> packetSaleMemberships=packetSaleFactories;
		for (PacketSaleMembership packetSaleMembership : (List<PacketSaleMembership>)packetSaleMemberships) {
			PacketSaleFactory packetSaleFactory=packetSaleService.findMembershipPacketSaleById(packetSaleMembership.getSaleId());
			packetSaleFact.add(packetSaleFactory);
		}
		return packetSaleFact;
	}

	public PacketSaleService getPacketSaleService() {
		return packetSaleService;
	}

	public void setPacketSaleService(PacketSaleService packetSaleService) {
		this.packetSaleService = packetSaleService;
	}

	@Override
	public String getProgType() {
		return "psm";
	}

	@Override
	public List<PacketSaleFactory> findAllUserBoughtPackets(long userId) {
		List<PacketSaleFactory> packetSaleMemberships=packetSaleService.findMembershipUserBoughtPackets(userId);
		
		for (PacketSaleFactory packetSaleFactory : packetSaleMemberships) {
			PacketPaymentFactory packetPaymentFactory=packetPaymentService.findPacketPaymentMembership(packetSaleFactory.getSaleId());
			packetSaleFactory.setPacketPaymentFactory(packetPaymentFactory);
 		}
		
		return packetSaleMemberships;
	}

	
	@Override
	public List<PacketSaleFactory> searchSaledPackets(PacketSaleFactory packetSaleQuery) {
		List<PacketSaleFactory> packetSales=null;
		
		String userName=packetSaleQuery.getUserName()+"%";
		String userSurname=packetSaleQuery.getUserSurname()+"%";
		if(!packetSaleQuery.getSalesDateStr().equals("")){
			Date salesDate=OhbeUtil.getThatDayFormatNotNull(packetSaleQuery.getSalesDateStr(), GlobalUtil.global.getPtDbDateFormat());
			Date salesDateNext=OhbeUtil.getDateForNextDate(OhbeUtil.getThatDayFormatNotNull(packetSaleQuery.getSalesDateStr(), GlobalUtil.global.getPtDbDateFormat()),1);
			packetSales=packetSaleService.findMembershipPacketSaleByNameAndDate(userName, userSurname, salesDate, salesDateNext);
		}else{
			packetSales=packetSaleService.findMembershipPacketSaleByName(userName, userSurname);
		}
		
		
		
		
		return packetSales;
	}
	
	@Override
	public List<User> findByUserNameAndSaleProgramWithNoPlan(String userName, String userSurname, long progId,int firmId) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public List<User> findByUserNameAndSaleProgramWithPlan(String userName, String userSurname, long progId,int firmId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<User> findByUsersAndSaledProgram(long schId) {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public User findUserBySaleId(long saleId) {
		return packetSaleService.findUserBySaleIdForMembership(saleId);
	}



	@Override
	public List<PacketSaleFactory> findUserPacketSaleBySchId(long schId) {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public HmiResultObj updateSalePacket(PacketSaleFactory packetSaleFactory) {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public List<User> findByUserNameForSales(String userName, String userSurname, int firmId) {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public List<PacketSaleFactory> findLast10UserPacketSale(int firmId) {
		List<PacketSaleFactory> packetSaleMemberships=packetSaleService.findLast10UserPacketSaleForMembership(firmId);
		
		for (PacketSaleFactory packetSaleMembership : packetSaleMemberships) {
			if(packetSaleMembership.getPayId()>0){
				packetSaleMembership.setComplete(1);
			}else{
				packetSaleMembership.setComplete(0);
			}
 		}
		
		return packetSaleMemberships;
	}



	@Override
	public HmiResultObj saleUpdatePacket(PacketSaleFactory packetSaleFactory) {
		// TODO Auto-generated method stub
		return null;
	}

	
	@Override
	public HmiResultObj saleCreatePacket(PacketSaleFactory packetSaleFactory) {
		return null;
	}
	
	
}
