package tr.com.abasus.ptboss.packetsale.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonTypeName;

import tr.com.abasus.ptboss.facade.sale.SaleClassFacade;
import tr.com.abasus.ptboss.packetpayment.entity.PacketPaymentFactory;
import tr.com.abasus.ptboss.packetpayment.service.PacketPaymentService;
import tr.com.abasus.ptboss.packetsale.decorator.IPacketSale;
import tr.com.abasus.ptboss.packetsale.service.PacketSaleService;
import tr.com.abasus.ptboss.ptuser.entity.User;
import tr.com.abasus.ptboss.result.entity.HmiResultObj;
import tr.com.abasus.ptboss.schedule.service.ScheduleClassService;
import tr.com.abasus.util.GlobalUtil;
import tr.com.abasus.util.OhbeUtil;
import tr.com.abasus.util.ResultStatuObj;
import tr.com.abasus.util.SaleStatus;

@Component(value="packetSaleClass")
@Scope("prototype")
@JsonTypeName("psc")
public class PacketSaleClass extends PacketSaleFactory  {

	
	@Autowired
	PacketSaleService packetSaleService;
	
	@Autowired
	PacketPaymentService packetPaymentService;
	
	@Autowired
	ScheduleClassService scheduleClassService;
	
	@Autowired
	@Qualifier("packetSaleMembership")
	IPacketSale iPacketSale;
	
	@Autowired
	SaleClassFacade saleClassFacade;
	
	
	@Override
	public List<PacketSaleFactory> findUserBoughtPackets(long userId) {
		return packetSaleService.findClassUserBoughtPackets(userId);
	}

	@Override
	public List<PacketSaleFactory> searchSaledPackets(PacketSaleFactory packetSaleQuery) {
		List<PacketSaleFactory> packetSales=null;
		
		String userName=packetSaleQuery.getUserName()+"%";
		String userSurname=packetSaleQuery.getUserSurname()+"%";
		if(!packetSaleQuery.getSalesDateStr().equals("")){
			Date salesDate=OhbeUtil.getThatDayFormatNotNull(packetSaleQuery.getSalesDateStr(), GlobalUtil.global.getPtDbDateFormat());
			Date salesDateNext=OhbeUtil.getDateForNextDate(OhbeUtil.getThatDayFormatNotNull(packetSaleQuery.getSalesDateStr(), GlobalUtil.global.getPtDbDateFormat()),1);
			packetSales=packetSaleService.findClassPacketSaleByNameAndDate(userName, userSurname, salesDate, salesDateNext);
		}else{
			packetSales=packetSaleService.findClassPacketSaleByName(userName, userSurname);
		}
		
		
		return packetSales;
	}

	@Override
	public PacketSaleFactory findSaledPacketsById(long saleId) {
		return packetSaleService.findClassPacketSaleById(saleId);
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
			HmiResultObj hmiResultObj=packetSaleService.buyClassPacket((PacketSaleClass)packetSaleFactory);
			hmiResultObjs.add(hmiResultObj);
		}
		
		return hmiResultObjs;
	}

	@Override
	public HmiResultObj saleUpdatePacket(PacketSaleFactory packetSaleFactory) {
	
		HmiResultObj hmiResultObj=saleClassFacade.canSaleChange(packetSaleFactory);
		if(hmiResultObj.getResultStatu()==ResultStatuObj.RESULT_STATU_FAIL){
			return hmiResultObj;
			
		}
		
		
			packetSaleFactory.setSalesDate(OhbeUtil.getThatDayFormatNotNull(packetSaleFactory.getSalesDateStr(), GlobalUtil.global.getPtDbDateFormat()));
			packetSaleFactory.setChangeDate(GlobalUtil.getCurrentDateByTimeZone());
			
			hmiResultObj=packetSaleService.buyClassPacket((PacketSaleClass)packetSaleFactory);
		
		return hmiResultObj;
	}
	
	@Override
	public HmiResultObj deleteSalePacket(PacketSaleFactory packetSaleFactory) {
		
		HmiResultObj hmiResultObj=new HmiResultObj();
		
		if(!saleClassFacade.canSaleDelete(packetSaleFactory)){
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
			hmiResultObj.setResultMessage("saledPacketHavePaymentOrBooking");
			return hmiResultObj;
		}
		return packetSaleService.deleteClassPacket((PacketSaleClass)packetSaleFactory);
	}
	

	@Override
	public List<PacketSaleFactory> findPacketsBySaleIds(List<PacketSaleFactory> packetSaleFactories) {
		List<PacketSaleFactory> packetSaleFact=new ArrayList<PacketSaleFactory>();
		List<? extends PacketSaleFactory> packetSaleClasss=packetSaleFactories;
		for (PacketSaleClass packetSaleClass : (List<PacketSaleClass>)packetSaleClasss) {
			PacketSaleFactory packetSaleFactory=packetSaleService.findClassPacketSaleById(packetSaleClass.getSaleId());
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

	public ScheduleClassService getScheduleClassService() {
		return scheduleClassService;
	}

	public void setScheduleClassService(ScheduleClassService scheduleClassService) {
		this.scheduleClassService = scheduleClassService;
	}

	@Override
	public String getProgType() {
		return "psc";
	}

	
	/**
	 * @author bahadir
	 * @comment findAllUserBoughtPackets chain desing pattern içerisindeki methoddur. 
	 * 
	 */
	@Override
	public List<PacketSaleFactory> findAllUserBoughtPackets(long userId) {
		List<PacketSaleFactory> packetSaleMembership= iPacketSale.findAllUserBoughtPackets(userId);
		List<PacketSaleFactory> packetSaleClasses=packetSaleService.findClassUserBoughtPackets(userId);
		
		for (PacketSaleFactory packetSaleFactory : packetSaleClasses) {
			PacketPaymentFactory packetPaymentFactory=packetPaymentService.findPacketPaymentClass(packetSaleFactory.getSaleId());
			packetSaleFactory.setPacketPaymentFactory(packetPaymentFactory);
 		}
		
		
		packetSaleClasses.addAll(packetSaleMembership);
		return packetSaleClasses;
	}

	@Override
	public List<User> findByUserNameAndSaleProgramWithNoPlan(String userName, String userSurname, long progId,int firmId) {
		return packetSaleService.findByUserNameAndSaleProgramForClassWithNoPlan(userName, userSurname, progId,firmId);
		
	}
	
	@Override
	public List<User> findByUserNameAndSaleProgramWithPlan(String userName, String userSurname, long progId,int firmId) {
		return packetSaleService.findByUserNameAndSaleProgramForClassWithPlan(userName, userSurname, progId,firmId);
		
	}

	@Override
	public List<User> findByUsersAndSaledProgram(long schId) {
		return packetSaleService.findByUsersAndSaledProgramForClass(schId);
	}

	@Override
	public User findUserBySaleId(long saleId) {
		return packetSaleService.findUserBySaleIdForClass(saleId);
	}

	@Override
	public List<PacketSaleFactory> findUserPacketSaleBySchId(long schId) {
		return packetSaleService.findUserPacketSaleBySchIdForClass(schId);
	}

	@Override
	public HmiResultObj updateSalePacket(PacketSaleFactory packetSaleFactory) {
		return packetSaleService.updateSalePacketForClass(packetSaleFactory);
	}

	@Override
	public List<User> findByUserNameForSales(String userName, String userSurname,int firmId) {
		return packetSaleService.findByUserNameForSalesForClass(userName, userSurname,firmId);
	}

	@Override
	public List<PacketSaleFactory> findLast10UserPacketSale(int firmId) {
		List<PacketSaleFactory> packetSaleMembership= iPacketSale.findLast10UserPacketSale(firmId);
		List<PacketSaleFactory> packetSaleClasses=packetSaleService.findLast10UserPacketSaleForClass(firmId);
		
		for (PacketSaleFactory packetSaleClass : packetSaleClasses) {
			if(packetSaleClass.getPayId()>0){
				packetSaleClass.setComplete(1);
			}else{
				packetSaleClass.setComplete(0);
			}
 		}
		
		packetSaleClasses.addAll(packetSaleMembership);
		return packetSaleClasses;
	}

	
	@Override
	public HmiResultObj saleCreatePacket(PacketSaleFactory packetSaleFactory) {
		return packetSaleService.buyClassPacket((PacketSaleClass)packetSaleFactory);
	}
	
}
