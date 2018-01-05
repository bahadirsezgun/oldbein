package tr.com.abasus.ptboss.facade.schedule;

import java.util.Date;

import tr.com.abasus.ptboss.program.entity.ProgramFactory;
import tr.com.abasus.ptboss.result.entity.HmiResultObj;
import tr.com.abasus.ptboss.schedule.entity.ScheduleFactory;
import tr.com.abasus.ptboss.schedule.entity.SchedulePlan;
import tr.com.abasus.ptboss.schedule.entity.ScheduleTimePlan;


public interface ScheduleFacadeService {

	public abstract HmiResultObj canScheduleCreate(Date timePlanDate,long schtStaffId);

	
	
	public abstract HmiResultObj canScheduleDelete(long schtId);

	public abstract HmiResultObj canScheduleChange(long schtId) ;

	public abstract HmiResultObj canUserDelete(long userId);

	public abstract HmiResultObj canUserRemoveFromTimePlan(ScheduleFactory scheduleFactory);

	
	public abstract HmiResultObj isTimeProperToCreate(ScheduleTimePlan scheduleTimePlan,ProgramFactory programFactory );

	public abstract HmiResultObj canSchedulePlanProperToCreate(ScheduleTimePlan scheduleTimePlan,ProgramFactory programFactory,long schId);

	
}
