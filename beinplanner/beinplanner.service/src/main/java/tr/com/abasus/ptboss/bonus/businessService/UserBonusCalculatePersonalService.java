package tr.com.abasus.ptboss.bonus.businessService;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import tr.com.abasus.ptboss.bonus.businessEntity.UserBonusObj;
import tr.com.abasus.ptboss.bonus.businessService.calculation.CalculateService;
import tr.com.abasus.ptboss.bonus.businessService.calculation.personal.CalculatePersonalBonusToRate;
import tr.com.abasus.ptboss.bonus.businessService.calculation.personal.CalculatePersonalBonusToStatic;
import tr.com.abasus.ptboss.bonus.businessService.calculation.personal.CalculatePersonalBonusToStaticRate;
import tr.com.abasus.ptboss.bonus.entity.UserBonusPaymentFactory;
import tr.com.abasus.ptboss.bonus.entity.UserBonusPaymentPersonal;
import tr.com.abasus.ptboss.bonus.service.BonusPersonalService;
import tr.com.abasus.ptboss.ptuser.entity.User;
import tr.com.abasus.ptboss.schedule.entity.ScheduleTimePlan;
import tr.com.abasus.util.BonusTypes;

@Service
@Scope("prototype")
public class UserBonusCalculatePersonalService implements  UserBonusCalculateService{

	@Autowired
	BonusPersonalService bonusPersonalService;
	
	@Autowired
	CalculatePersonalBonusToRate calculatePersonalBonusToRate;
	
	@Autowired
	CalculatePersonalBonusToStatic calculatePersonalBonusToStatic;
	
	@Autowired
	CalculatePersonalBonusToStaticRate calculatePersonalBonusToStaticRate;
	
	
	@SuppressWarnings("unchecked")
	@Override
	public UserBonusObj findStaffBonusObj(long schStaffId, Date startDate, Date endDate) {
		
		List<ScheduleTimePlan> scheduleTimePlans=bonusPersonalService.findScheduleTimePlansByDatesForStaff(schStaffId, startDate, endDate);
		
		CalculateService calculateService=null;
		User  staff=bonusPersonalService.findStaffBonusType(schStaffId);
	
		if(staff.getBonusTypeP()==BonusTypes.BONUS_IS_TYPE_RATE){
			calculateService=calculatePersonalBonusToRate;
		}else if(staff.getBonusTypeP()==BonusTypes.BONUS_IS_TYPE_STATIC){
			calculateService=calculatePersonalBonusToStatic;
		}else if(staff.getBonusTypeP()==BonusTypes.BONUS_IS_TYPE_STATIC_RATE){
			calculateService=calculatePersonalBonusToStaticRate;
		}
		
		
		
		List<? extends UserBonusPaymentFactory> bonusPaymentPersonals=bonusPersonalService.findUserBonusPaymentByDate(schStaffId, startDate, endDate);
		List<UserBonusPaymentFactory> bonusPaymentFactories=(List<UserBonusPaymentFactory>)bonusPaymentPersonals;
		
		double payedAmount=0;
		if(bonusPaymentFactories!=null){
			for (UserBonusPaymentFactory userBonusPaymentFactory : bonusPaymentFactories) {
				payedAmount+=userBonusPaymentFactory.getBonAmount();
			}
		}
		UserBonusObj userBonusObj=calculateService.calculateIt(scheduleTimePlans,schStaffId);
		userBonusObj.setUserBonusPaymentFactories(bonusPaymentFactories);
		userBonusObj.setPayedAmount(payedAmount);
		userBonusObj.setBonusType(staff.getBonusTypeP());
		
		
		return userBonusObj;
	}

	
	
	
}
