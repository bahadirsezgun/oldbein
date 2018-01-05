package tr.com.abasus.ptboss.bonus.businessService;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import tr.com.abasus.ptboss.bonus.businessEntity.UserBonusObj;
import tr.com.abasus.ptboss.bonus.businessService.calculation.CalculateService;
import tr.com.abasus.ptboss.bonus.businessService.calculation.classes.CalculateClassBonusToRate;
import tr.com.abasus.ptboss.bonus.businessService.calculation.classes.CalculateClassBonusToStatic;
import tr.com.abasus.ptboss.bonus.businessService.calculation.classes.CalculateClassBonusToStaticRate;
import tr.com.abasus.ptboss.bonus.entity.UserBonusPaymentFactory;
import tr.com.abasus.ptboss.bonus.service.BonusClassService;
import tr.com.abasus.ptboss.ptuser.entity.User;
import tr.com.abasus.ptboss.schedule.entity.ScheduleTimePlan;
import tr.com.abasus.util.BonusTypes;

@Service
@Scope("prototype")
public class UserBonusCalculateClassService  implements  UserBonusCalculateService {

	@Autowired
	BonusClassService bonusClassService;
	
	@Autowired
	CalculateClassBonusToRate calculateClassBonusToRate;
	
	@Autowired
	CalculateClassBonusToStatic calculateClassBonusToStatic;
	
	@Autowired
	CalculateClassBonusToStaticRate calculateClassBonusToStaticRate;
	
	
	
	@SuppressWarnings("unchecked")
	@Override
	public UserBonusObj findStaffBonusObj(long schStaffId, Date startDate, Date endDate) {
		
		List<ScheduleTimePlan> scheduleTimePlans=bonusClassService.findScheduleTimePlansByDatesForStaff(schStaffId, startDate, endDate);
		
		CalculateService calculateService=null;
		User  staff=bonusClassService.findStaffBonusType(schStaffId);
	
		if(staff.getBonusTypeP()==BonusTypes.BONUS_IS_TYPE_RATE){
			calculateService=calculateClassBonusToRate;
		}else if(staff.getBonusTypeP()==BonusTypes.BONUS_IS_TYPE_STATIC){
			calculateService=calculateClassBonusToStatic;
		}else if(staff.getBonusTypeP()==BonusTypes.BONUS_IS_TYPE_STATIC_RATE){
			calculateService=calculateClassBonusToStaticRate;
		}
		
		
		
		List<? extends UserBonusPaymentFactory> bonusPaymentClasss=bonusClassService.findUserBonusPaymentByDate(schStaffId, startDate, endDate);
		List<UserBonusPaymentFactory> bonusPaymentFactories=(List<UserBonusPaymentFactory>)bonusPaymentClasss;
		
		double payedAmount=0;
		if(bonusPaymentFactories!=null){
			for (UserBonusPaymentFactory userBonusPaymentFactory : bonusPaymentFactories) {
				payedAmount+=userBonusPaymentFactory.getBonAmount();
			}
		}
		UserBonusObj userBonusObj=calculateService.calculateIt(scheduleTimePlans,schStaffId);
		userBonusObj.setUserBonusPaymentFactories(bonusPaymentFactories);
		userBonusObj.setPayedAmount(payedAmount);
		userBonusObj.setBonusType(staff.getBonusTypeC());
		
		
		return userBonusObj;
		
	}

	
	
}
