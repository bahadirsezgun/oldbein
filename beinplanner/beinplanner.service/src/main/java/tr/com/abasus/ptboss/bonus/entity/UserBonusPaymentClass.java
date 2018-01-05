package tr.com.abasus.ptboss.bonus.entity;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonTypeName;

import tr.com.abasus.ptboss.bonus.businessEntity.UserBonusSearchObj;
import tr.com.abasus.ptboss.bonus.businessEntity.UserPaymentObj;
import tr.com.abasus.ptboss.bonus.facadeService.UserBonusPaymentClassFacadeService;
import tr.com.abasus.ptboss.bonus.service.BonusClassService;
import tr.com.abasus.ptboss.packetpayment.entity.PacketPaymentDetailFactory;
import tr.com.abasus.ptboss.packetpayment.entity.PacketPaymentFactory;
import tr.com.abasus.ptboss.packetpayment.service.PacketPaymentService;
import tr.com.abasus.ptboss.result.entity.HmiResultObj;
import tr.com.abasus.ptboss.schedule.entity.ScheduleFactory;
import tr.com.abasus.ptboss.schedule.service.ScheduleClassService;


@Component(value="userBonusPaymentClass")
@Scope("prototype")
@JsonTypeName("bpc")
public class UserBonusPaymentClass extends UserBonusPaymentFactory {

	@Autowired
	PacketPaymentService packetPaymentService;
	
	@Autowired
	ScheduleClassService scheduleClassService;
	
	@Autowired
	BonusClassService bonusClassService;
	
	@Autowired
	UserBonusPaymentClassFacadeService userBonusPaymentClassFacadeService;
	
	
	
	@Override
	public String getBonType() {
		return "bpc";
	}

	@Override
	public HmiResultObj saveBonusPayment(UserBonusPaymentFactory userBonusPaymentFactory) {
		HmiResultObj hmiResultObj=bonusClassService.createBonusPaymentForUser((UserBonusPaymentClass)userBonusPaymentFactory);
		
		 return hmiResultObj;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<UserBonusPaymentFactory> findBonusPayment(UserBonusSearchObj userBonusSearchObj) {
		if(userBonusSearchObj.getQueryType()==1){
			List<? extends UserBonusPaymentFactory> userBonusPaymentFactories=bonusClassService.findUserOfMonth(userBonusSearchObj.getSchStaffId(), userBonusSearchObj.getMonth(), userBonusSearchObj.getYear());
			return (List<UserBonusPaymentFactory>)userBonusPaymentFactories;
		}else{
			List<? extends UserBonusPaymentFactory> userBonusPaymentFactories=bonusClassService.findUserBonusPaymentByDate(userBonusSearchObj.getSchStaffId(), userBonusSearchObj.getStartDate(), userBonusSearchObj.getEndDate());
			return (List<UserBonusPaymentFactory>)userBonusPaymentFactories;
		}
	}

	@Override
	public HmiResultObj deleteBonusPayment(UserBonusPaymentFactory userBonusPaymentFactory) {
		return bonusClassService.deleteBonusPaymentForUser((UserBonusPaymentClass)userBonusPaymentFactory);
	}

	@SuppressWarnings("unchecked")
	@Override
	public UserPaymentObj findUserPayment(long schtId) {
		
		UserPaymentObj userPaymentObj=new UserPaymentObj();
		
	    List<? extends ScheduleFactory> scheduleFactoriesExtends=scheduleClassService.findScheduleUsersClassPlanByTimePlanId(schtId);
		List<ScheduleFactory> scheduleFactories =(  List<ScheduleFactory> )scheduleFactoriesExtends;
		
	    
	    for (ScheduleFactory scheduleFactory : scheduleFactories) {
			
	    	PacketPaymentFactory packetPaymentFactory=packetPaymentService.findPacketPaymentClass(scheduleFactory.getSaleId());
	    	if(packetPaymentFactory!=null){
	    		List<? extends PacketPaymentDetailFactory> packetPaymentDetailFactoriesExtends=packetPaymentService.findPacketPaymentClassDetail(packetPaymentFactory.getPayId());
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
		return bonusClassService.findUserBonusPaymentFactoryByMonth(month, year, firmId);
	}

	@Override
	public double findTotalOfMonth(int firmId, int month, int year) {
		double totalPayment=0;
	    List<UserBonusPaymentFactory>  userBonusPaymentFactories= bonusClassService.findUserBonusPaymentFactoryByMonth(month, year, firmId);
	    for (UserBonusPaymentFactory userBonusPayment : userBonusPaymentFactories) {
			totalPayment+=userBonusPayment.getBonAmount();
		}
	    return totalPayment;
	}

	@Override
	public List<UserBonusPaymentFactory> findBonusPaymentForToday(int firmId) {
		return bonusClassService.findBonusPaymentForToday(firmId);
	}
	
	
	
	
}
