package tr.com.abasus.ptboss.bonus.entity;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonTypeName;

import tr.com.abasus.ptboss.bonus.businessEntity.UserBonusSearchObj;
import tr.com.abasus.ptboss.bonus.businessEntity.UserPaymentObj;
import tr.com.abasus.ptboss.bonus.facadeService.UserBonusPaymentFacadeService;
import tr.com.abasus.ptboss.bonus.facadeService.UserBonusPaymentPersonalFacadeService;
import tr.com.abasus.ptboss.bonus.service.BonusPersonalService;
import tr.com.abasus.ptboss.packetpayment.entity.PacketPaymentDetailFactory;
import tr.com.abasus.ptboss.packetpayment.entity.PacketPaymentFactory;
import tr.com.abasus.ptboss.packetpayment.entity.PacketPaymentPersonal;
import tr.com.abasus.ptboss.packetpayment.service.PacketPaymentService;
import tr.com.abasus.ptboss.result.entity.HmiResultObj;
import tr.com.abasus.ptboss.schedule.entity.ScheduleFactory;
import tr.com.abasus.ptboss.schedule.entity.ScheduleUsersPersonalPlan;
import tr.com.abasus.ptboss.schedule.service.SchedulePersonalService;
import tr.com.abasus.util.ResultStatuObj;

@Component(value="userBonusPaymentPersonal")
@Scope("prototype")
@JsonTypeName("bpp")
public class UserBonusPaymentPersonal extends UserBonusPaymentFactory {

	@Autowired
	BonusPersonalService bonusPersonalService;
	
	@Autowired
	UserBonusPaymentPersonalFacadeService userBonusPaymentPersonalFacadeService;
	
	@Autowired
	UserBonusPaymentClass userBonusPaymentClass;
	
	@Autowired
	PacketPaymentService packetPaymentService;
	
	@Autowired
	SchedulePersonalService schedulePersonalService;
	
	@Override
	public String getBonType() {
		return "bpp";
	}
	

	@Override
	public HmiResultObj saveBonusPayment(UserBonusPaymentFactory userBonusPaymentFactory) {
		
	 HmiResultObj hmiResultObj=bonusPersonalService.createBonusPaymentForUser((UserBonusPaymentPersonal)userBonusPaymentFactory);
	
	 return hmiResultObj;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<UserBonusPaymentFactory> findBonusPayment(UserBonusSearchObj userBonusSearchObj) {
		
		if(userBonusSearchObj.getQueryType()==1){
			List<? extends UserBonusPaymentFactory> userBonusPaymentFactories=bonusPersonalService.findUserOfMonth(userBonusSearchObj.getSchStaffId(), userBonusSearchObj.getMonth(), userBonusSearchObj.getYear());
			return (List<UserBonusPaymentFactory>)userBonusPaymentFactories;
		}else{
			List<? extends UserBonusPaymentFactory> userBonusPaymentFactories=bonusPersonalService.findUserBonusPaymentByDate(userBonusSearchObj.getSchStaffId(), userBonusSearchObj.getStartDate(), userBonusSearchObj.getEndDate());
			return (List<UserBonusPaymentFactory>)userBonusPaymentFactories;
		}
	}

	@Override
	public HmiResultObj deleteBonusPayment(UserBonusPaymentFactory userBonusPaymentFactory) {
		return bonusPersonalService.deleteBonusPaymentForUser((UserBonusPaymentPersonal)userBonusPaymentFactory);
	}

	@SuppressWarnings("unchecked")
	@Override
	public UserPaymentObj findUserPayment(long schtId) {
		
		UserPaymentObj userPaymentObj=new UserPaymentObj();
		
	    List<? extends ScheduleFactory> scheduleFactoriesExtends=schedulePersonalService.findScheduleUsersPersonalPlanByTimePlanId(schtId);
		List<ScheduleFactory> scheduleFactories =(  List<ScheduleFactory> )scheduleFactoriesExtends;
		
	    
	    for (ScheduleFactory scheduleFactory : scheduleFactories) {
			
	    	PacketPaymentFactory packetPaymentFactory=packetPaymentService.findPacketPaymentPersonal(scheduleFactory.getSaleId());
	    	if(packetPaymentFactory!=null){
	    		List<? extends PacketPaymentDetailFactory> packetPaymentDetailFactoriesExtends=packetPaymentService.findPacketPaymentPersonalDetail(packetPaymentFactory.getPayId());
	    	    List<PacketPaymentDetailFactory> packetPaymentDetailFactories=(List<PacketPaymentDetailFactory>)packetPaymentDetailFactoriesExtends;
	    	    packetPaymentFactory.setPacketPaymentDetailFactories(packetPaymentDetailFactories);
	    	    scheduleFactory.setPacketPaymentFactory(packetPaymentFactory);
	    	}
	    	
	    	
	    	
		}
	    
	    userPaymentObj.setScheduleFactories(scheduleFactories);
		
		return userPaymentObj;
	}


	@Override
	public List<UserBonusPaymentFactory> findBonusPaymentForStaffsByMonth(int month, int year, int firmId) {
		List<UserBonusPaymentFactory>  userBonusPaymentFactories= userBonusPaymentClass.findBonusPaymentForStaffsByMonth(month, year, firmId);
		
		List<UserBonusPaymentFactory> bonusPaymentFactories= bonusPersonalService.findUserBonusPaymentFactoryByMonth(month, year, firmId);
		bonusPaymentFactories.addAll(userBonusPaymentFactories);
		
		return bonusPaymentFactories;
	}

	
	@Override
	public double findTotalOfMonth(int firmId, int month,int year) {
		
		    double totalPayment=userBonusPaymentClass.findTotalOfMonth(firmId, month, year);
		  
		    
			List<UserBonusPaymentFactory> userBonusPayments=bonusPersonalService.findUserBonusPaymentFactoryByMonth(month, year, firmId);
			
			for (UserBonusPaymentFactory userBonusPayment : userBonusPayments) {
				totalPayment+=userBonusPayment.getBonAmount();
			}
		return totalPayment;
	}


	@Override
	public List<UserBonusPaymentFactory> findBonusPaymentForToday(int firmId) {
         List<UserBonusPaymentFactory>  userBonusPaymentFactories= userBonusPaymentClass.findBonusPaymentForToday(firmId);
		
		List<UserBonusPaymentFactory> bonusPaymentFactories= bonusPersonalService.findBonusPaymentForToday(firmId);
		bonusPaymentFactories.addAll(userBonusPaymentFactories);
		
		return bonusPaymentFactories;
	}
	
	
	
}
