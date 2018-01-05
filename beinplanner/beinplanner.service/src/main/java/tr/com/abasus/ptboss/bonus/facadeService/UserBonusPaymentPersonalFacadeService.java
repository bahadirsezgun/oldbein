package tr.com.abasus.ptboss.bonus.facadeService;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import tr.com.abasus.ptboss.bonus.entity.UserBonusPaymentPersonal;
import tr.com.abasus.ptboss.bonus.service.BonusPersonalService;
import tr.com.abasus.ptboss.packetpayment.service.PacketPaymentService;
import tr.com.abasus.ptboss.schedule.entity.ScheduleTimePlan;
import tr.com.abasus.ptboss.schedule.service.ScheduleService;
import tr.com.abasus.util.BonusLockUtil;
import tr.com.abasus.util.DateTimeUtil;
import tr.com.abasus.util.OhbeUtil;

@Service
@Scope("prototype")
public class UserBonusPaymentPersonalFacadeService implements UserBonusPaymentFacadeService {

	@Autowired
	ScheduleService scheduleService;
	
	@Autowired
	BonusPersonalService bonusPersonalService;
	
	
	
	
	@Override
	public boolean isLockBonusPayedClasses(long schtId) {
		boolean isLock=false;
		if(BonusLockUtil.BONUS_LOCK_FLAG){
		ScheduleTimePlan scheduleTimePlan=scheduleService.findScheduleTimePlanById(schtId);
			if(scheduleTimePlan!=null){
				
					Date timePlanDate=scheduleTimePlan.getPlanStartDate();
					
					int month=DateTimeUtil.getMonthOfDate(scheduleTimePlan.getPlanStartDate());
					int year=DateTimeUtil.getYearOfDate(scheduleTimePlan.getPlanStartDate());
					long schtStaffId=scheduleTimePlan.getSchtStaffId();
					
					List<UserBonusPaymentPersonal> userBonusPaymentPersonals=bonusPersonalService.findUserOfMonth(schtStaffId, month, year);
					////System.out.println("BONUS LOCK KONTROL START MONTH:"+month+"  YEAR:"+year+" STAFF ID:" +schtStaffId);
					for (UserBonusPaymentPersonal userBonusPaymentPersonal : userBonusPaymentPersonals) {
						Date bonEndDate=userBonusPaymentPersonal.getBonEndDate();
						////System.out.println("BONUS END DATE "+DateTimeUtil.getDateStrByFormat(bonEndDate, "dd/MM/yyyy"));
						////System.out.println("TIME PLAN DATE "+DateTimeUtil.getDateStrByFormat(timePlanDate, "dd/MM/yyyy"));
						if(timePlanDate.before(bonEndDate)){
							isLock=true;
							break;
						}
					}
			}else{
				isLock=false;
			}
		}else{
			isLock=false;
		}
		return isLock;
	}



	@Override
	public boolean isLockBonusPayedToDates(Date timePlanDate,long schtStaffId) {
		boolean isLock=false;
		if(BonusLockUtil.BONUS_LOCK_FLAG){
					
					int month=DateTimeUtil.getMonthOfDate(timePlanDate);
					int year=DateTimeUtil.getYearOfDate(timePlanDate);
					
					List<UserBonusPaymentPersonal> userBonusPaymentPersonals=bonusPersonalService.findUserOfMonth(schtStaffId, month, year);
					////System.out.println("BONUS LOCK KONTROL START MONTH:"+month+"  YEAR:"+year+" STAFF ID:" +schtStaffId);
					for (UserBonusPaymentPersonal userBonusPaymentPersonal : userBonusPaymentPersonals) {
						Date bonEndDate=userBonusPaymentPersonal.getBonEndDate();
						////System.out.println("BONUS END DATE "+DateTimeUtil.getDateStrByFormat(bonEndDate, "dd/MM/yyyy"));
						////System.out.println("TIME PLAN DATE "+DateTimeUtil.getDateStrByFormat(timePlanDate, "dd/MM/yyyy"));
						if(timePlanDate.before(bonEndDate)){
							isLock=true;
							break;
						}
					}
			
		}else{
			isLock=false;
		}
		return isLock;
	}


	
	

	
	
}
