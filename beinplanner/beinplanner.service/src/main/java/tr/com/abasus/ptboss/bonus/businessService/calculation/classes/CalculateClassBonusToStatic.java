package tr.com.abasus.ptboss.bonus.businessService.calculation.classes;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import tr.com.abasus.ptboss.bonus.businessEntity.UserBonusDetailObj;
import tr.com.abasus.ptboss.bonus.businessEntity.UserBonusObj;
import tr.com.abasus.ptboss.bonus.businessService.calculation.CalculateService;
import tr.com.abasus.ptboss.definition.entity.DefBonus;
import tr.com.abasus.ptboss.definition.service.ClassBonusService;
import tr.com.abasus.ptboss.packetpayment.entity.PacketPaymentClass;
import tr.com.abasus.ptboss.packetpayment.entity.PacketPaymentFactory;
import tr.com.abasus.ptboss.schedule.entity.ScheduleFactory;
import tr.com.abasus.ptboss.schedule.entity.ScheduleTimePlan;
import tr.com.abasus.ptboss.schedule.entity.ScheduleUsersClassPlan;
import tr.com.abasus.util.GlobalUtil;
import tr.com.abasus.util.PaymentConfirmUtil;
import tr.com.abasus.util.RuleUtil;
import tr.com.abasus.util.StatuTypes;
@Service
@Scope("prototype")
public class CalculateClassBonusToStatic implements CalculateService {

	@Autowired
	ClassBonusService personalBonusService;
	
	@Autowired
	ScheduleUsersClassPlan scheduleUsersClassPlan;
	
	
	@Autowired
	PacketPaymentClass packetPaymentClass;
	
	
	@Override
	public UserBonusObj calculateIt(List<ScheduleTimePlan> scheduleTimePlans,long staffId) {
		UserBonusObj userBonusObj=new UserBonusObj();
		
		int bonusPaymentRule=GlobalUtil.rules.getRulePayBonusForConfirmedPayment();
		
		userBonusObj.setBonusPaymentRule(bonusPaymentRule);
		userBonusObj.setCreditCardCommissionRate(0);
		userBonusObj.setCreditCardCommissionRule(RuleUtil.RULE_NOK);
		
		
		List<DefBonus> defBonuses= personalBonusService.findClassStaticBonus(staffId);
		int i=1;
		
		double willPayAmount=0;
		
		List<UserBonusDetailObj> userBonusDetailObjs=new ArrayList<UserBonusDetailObj>();
		
		for (ScheduleTimePlan scheduleTimePlan : scheduleTimePlans) {
			if(scheduleTimePlan.getStatuTp()!=StatuTypes.TIMEPLAN_POSTPONE){
			UserBonusDetailObj userBonusDetailObj=new UserBonusDetailObj();
			
			double bonusPrice=0;
			for (DefBonus defBonus : defBonuses) {
				if(defBonus.getBonusProgId()==scheduleTimePlan.getProgId()){
					bonusPrice=defBonus.getBonusValue();
					break;
				}
			}
			
			
			List<ScheduleFactory> usersInTimePlan=scheduleUsersClassPlan.findSchedulesTimesBySchtId(scheduleTimePlan.getSchtId());
			userBonusDetailObj.setScheduleFactories(usersInTimePlan);
			double totalTimePlanPayment=0;
			
			for (ScheduleFactory scheduleFactory : usersInTimePlan) {
				PacketPaymentFactory packetPaymentFactory=packetPaymentClass.findPacketPaymentBySaleId(scheduleFactory.getSaleId());
				
				double unitPrice=0;
				int saleCount=0;
				
				if(packetPaymentFactory!=null){
					if(bonusPaymentRule==RuleUtil.RULE_OK){
						if(packetPaymentFactory.getPayConfirm()==PaymentConfirmUtil.PAYMENT_CONFIRM){
							unitPrice=bonusPrice/usersInTimePlan.size();
							totalTimePlanPayment+=unitPrice;
						}
					}else{
						unitPrice=bonusPrice/usersInTimePlan.size();
						totalTimePlanPayment+=unitPrice;
						saleCount=packetPaymentFactory.getProgCount();
					}
					
				}
				scheduleFactory.setUnitPrice(unitPrice);
				scheduleFactory.setSaleCount(saleCount);
			}
			
			
			
			
			userBonusDetailObj.setSchCount(scheduleTimePlan.getSchCount());
			userBonusDetailObj.setPlanStartDateStr(scheduleTimePlan.getPlanStartDateStr());
			userBonusDetailObj.setClassCount(i);
			userBonusDetailObj.setProgName(scheduleTimePlan.getProgName());
			userBonusDetailObj.setBonusValue(bonusPrice);
			userBonusDetailObj.setPacketUnitPrice(totalTimePlanPayment);
			userBonusDetailObj.setStaffPaymentAmount(totalTimePlanPayment);
			userBonusDetailObj.setSchtId(scheduleTimePlan.getSchtId());
			
			willPayAmount+=userBonusDetailObj.getStaffPaymentAmount();
			
			userBonusDetailObjs.add(userBonusDetailObj);
		   i++;	
		}
		}
		
		userBonusObj.setWillPayAmount(willPayAmount);
		userBonusObj.setSchStaffId(staffId);
		userBonusObj.setUserBonusDetailObjs(userBonusDetailObjs);
		return userBonusObj;
	}
}
