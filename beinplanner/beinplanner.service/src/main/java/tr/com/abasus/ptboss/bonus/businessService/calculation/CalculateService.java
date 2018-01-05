package tr.com.abasus.ptboss.bonus.businessService.calculation;

import java.util.List;

import tr.com.abasus.ptboss.bonus.businessEntity.UserBonusObj;
import tr.com.abasus.ptboss.schedule.entity.ScheduleTimePlan;

public interface CalculateService {

	public UserBonusObj calculateIt(List<ScheduleTimePlan> scheduleTimePlans,long staffId);
	
}
