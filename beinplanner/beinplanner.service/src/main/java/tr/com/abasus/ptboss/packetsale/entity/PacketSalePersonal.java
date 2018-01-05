package tr.com.abasus.ptboss.packetsale.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonTypeName;

import tr.com.abasus.ptboss.facade.sale.SalePersonalFacade;
import tr.com.abasus.ptboss.packetpayment.entity.PacketPaymentFactory;
import tr.com.abasus.ptboss.packetpayment.entity.PacketPaymentPersonal;
import tr.com.abasus.ptboss.packetpayment.service.PacketPaymentService;
import tr.com.abasus.ptboss.packetsale.decorator.IPacketSale;
import tr.com.abasus.ptboss.packetsale.service.PacketSaleService;
import tr.com.abasus.ptboss.ptuser.entity.User;
import tr.com.abasus.ptboss.result.entity.HmiResultObj;
import tr.com.abasus.ptboss.schedule.service.SchedulePersonalService;
import tr.com.abasus.util.GlobalUtil;
import tr.com.abasus.util.OhbeUtil;
import tr.com.abasus.util.ResultStatuObj;
import tr.com.abasus.util.SaleStatus;

@Component(value="packetSalePersonal")
@Scope("prototype")
@JsonTypeName("psp")
public class PacketSalePersonal extends PacketSaleFactory {

	@Autowired
	PacketSaleService packetSaleService;
	
	@Autowired
	PacketPaymentService packetPaymentService;
	
	@Autowired
	SchedulePersonalService schedulePersonalService;
	
	@Autowired
	SalePersonalFacade salePersonalFacade;
	
	
	@Autowired
	@Qualifier("packetSaleClass")
	IPacketSale iPacketSale;
	
	
	@Override
	public List<PacketSaleFactory> findUserBoughtPackets(long userId) {
		return packetSaleService.findPersonalUserBoughtPackets(userId);
	}


	@Override
	public List<PacketSaleFactory> searchSaledPackets(PacketSaleFactory packetSaleQuery) {
		List<PacketSaleFactory> packetSales=null;
		
		String userName=packetSaleQuery.getUserName()+"%";
		String userSurname=packetSaleQuery.getUserSurname()+"%";
		if(!packetSaleQuery.getSalesDateStr().equals("")){
			Date salesDate=OhbeUtil.getThatDayFormatNotNull(packetSaleQuery.getSalesDateStr(), GlobalUtil.global.getPtDbDateFormat());
			Date salesDateNext=OhbeUtil.getDateForNextDate(OhbeUtil.getThatDayFormatNotNull(packetSaleQuery.getSalesDateStr(), GlobalUtil.global.getPtDbDateFormat()),1);
			packetSales=packetSaleService.findPersonalPacketSaleByNameAndDate(userName, userSurname, salesDate, salesDateNext);
		}else{
			packetSales=packetSaleService.findPersonalPacketSaleByName(userName, userSurname);
		}
		
		
		return packetSales;
	}


	@Override
	public PacketSaleFactory findSaledPacketsById(long saleId) {
		return packetSaleService.findPersonalPacketSaleById(saleId);
	}


	@Override
	public List<HmiResultObj> saleNewPacket(PacketSaleFactory packetSaleFactory) {
		List<HmiResultObj> hmiResultObjs=new ArrayList<HmiResultObj>();
		List<User> saledMembers=packetSaleFactory.getSaledMembers();
		for (User member : saledMembers) {
			packetSaleFactory.setSalesDate(OhbeUtil.getThatDayFormatNotNull(packetSaleFactory.getSalesDateStr(), GlobalUtil.global.getPtDbDateFormat()));
			packetSaleFactory.setUserId(member.getUserId());
			packetSaleFactory.setSaleId(0);
			packetSaleFactory.setSaleStatu(SaleStatus.SALE_NO_PLANNED);
			
			packetSaleFactory.setChangeDate(GlobalUtil.getCurrentDateByTimeZone());
			
			HmiResultObj hmiResultObj=packetSaleService.buyPersonalPacket((PacketSalePersonal)packetSaleFactory);
			hmiResultObjs.add(hmiResultObj);
		}
		
		return hmiResultObjs;
	}
	
	@Override
	public HmiResultObj saleUpdatePacket(PacketSaleFactory packetSaleFactory) {
		HmiResultObj hmiResultObj=salePersonalFacade.canSaleChange(packetSaleFactory);
		if(hmiResultObj.getResultStatu()==ResultStatuObj.RESULT_STATU_FAIL){
			return hmiResultObj;
			
		}
		
			packetSaleFactory.setSalesDate(OhbeUtil.getThatDayFormatNotNull(packetSaleFactory.getSalesDateStr(), GlobalUtil.global.getPtDbDateFormat()));
			packetSaleFactory.setChangeDate(GlobalUtil.getCurrentDateByTimeZone());
			
			hmiResultObj=packetSaleService.buyPersonalPacket((PacketSalePersonal)packetSaleFactory);
		
		return hmiResultObj;
	}


	@Override
	public HmiResultObj deleteSalePacket(PacketSaleFactory packetSaleFactory) {
		
		HmiResultObj hmiResultObj=new HmiResultObj();
		
		if(!salePersonalFacade.canSaleDelete(packetSaleFactory)){
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
			hmiResultObj.setResultMessage("saledPacketHavePaymentOrBooking");
			return hmiResultObj;
		}
		return packetSaleService.deletePersonalPacket((PacketSalePersonal)packetSaleFactory);
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<PacketSaleFactory> findPacketsBySaleIds(List<PacketSaleFactory> packetSaleFactories) {
		List<PacketSaleFactory> packetSaleFact=new ArrayList<PacketSaleFactory>();
		List<? extends PacketSaleFactory> packetSalePersonals=packetSaleFactories;
		for (PacketSalePersonal packetSalePersonal : (List<PacketSalePersonal>)packetSalePersonals) {
			PacketSaleFactory packetSaleFactory=packetSaleService.findPersonalPacketSaleById(packetSalePersonal.getSaleId());
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


	public PacketPaymentService getPacketPaymentService() {
		return packetPaymentService;
	}


	public void setPacketPaymentService(PacketPaymentService packetPaymentService) {
		this.packetPaymentService = packetPaymentService;
	}


	public SchedulePersonalService getSchedulePersonalService() {
		return schedulePersonalService;
	}


	public void setSchedulePersonalService(SchedulePersonalService schedulePersonalService) {
		this.schedulePersonalService = schedulePersonalService;
	}

	@Override
	public String getProgType() {
		return "psp";
	}


	@Override
	public List<PacketSaleFactory> findAllUserBoughtPackets(long userId) {
		List<PacketSaleFactory> packetSaleClassesAndMembership= iPacketSale.findAllUserBoughtPackets(userId);
		List<PacketSaleFactory> packetSalePersonals=packetSaleService.findPersonalUserBoughtPackets(userId);
		
		for (PacketSaleFactory packetSalePersonal : packetSalePersonals) {
			PacketPaymentFactory packetPaymentFactory=packetPaymentService.findPacketPaymentPersonal(packetSalePersonal.getSaleId());
			packetSalePersonal.setPacketPaymentFactory(packetPaymentFactory);
 		}
		
		packetSalePersonals.addAll(packetSaleClassesAndMembership);
		return packetSalePersonals;
	}


	@Override
	public List<User> findByUserNameAndSaleProgramWithNoPlan(String userName, String userSurname, long progId,int firmId) {
		return packetSaleService.findByUserNameAndSaleProgramForPersonalWithNoPlan(userName, userSurname, progId,firmId);
	}
	
	@Override
	public List<User> findByUserNameForSales(String userName, String userSurname,int firmId) {
		return packetSaleService.findByUserNameForSalesForPersonal(userName, userSurname,firmId);
	}
	
	
	
	
	@Override
	public List<User> findByUserNameAndSaleProgramWithPlan(String userName, String userSurname, long progId,int firmId) {
		return packetSaleService.findByUserNameAndSaleProgramForPersonalWithPlan(userName, userSurname, progId,firmId);
	}


	@Override
	public List<User> findByUsersAndSaledProgram(long schId) {
		return packetSaleService.findByUsersAndSaledProgramForPersonal(schId);
	}


	@Override
	public User findUserBySaleId(long saleId) {
		return packetSaleService.findUserBySaleIdForPersonal(saleId);
	}


	@Override
	public List<PacketSaleFactory> findUserPacketSaleBySchId(long schId) {
		return packetSaleService.findUserPacketSaleBySchIdForPersonal(schId);
	}


	@Override
	public HmiResultObj updateSalePacket(PacketSaleFactory packetSaleFactory) {
		return packetSaleService.updateSalePacketForPersonal(packetSaleFactory);
	}


	@Override
	public List<PacketSaleFactory> findLast10UserPacketSale(int firmId) {
		List<PacketSaleFactory> packetSaleClassesAndMembership= iPacketSale.findLast10UserPacketSale(firmId);
		List<PacketSaleFactory> packetSalePersonals=packetSaleService.findLast10UserPacketSaleForPersonal(firmId);
		
		for (PacketSaleFactory packetSalePersonal : packetSalePersonals) {
			if(packetSalePersonal.getPayId()>0){
				packetSalePersonal.setComplete(1);
			}else{
				packetSalePersonal.setComplete(0);
			}
 		}
		
		packetSalePersonals.addAll(packetSaleClassesAndMembership);
		return packetSalePersonals;
	}


	@Override
	public HmiResultObj saleCreatePacket(PacketSaleFactory packetSaleFactory) {
		return packetSaleService.buyPersonalPacket((PacketSalePersonal)packetSaleFactory);
	}
	
	
	
}
