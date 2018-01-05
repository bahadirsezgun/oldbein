package tr.com.abasus.ptboss.schedule.entity;

import java.util.List;

import tr.com.abasus.ptboss.result.entity.HmiResultObj;
import tr.com.abasus.ptboss.schedule.businessEntity.ScheduleObj;
import tr.com.abasus.ptboss.schedule.businessEntity.ScheduleSearchObj;

public interface ScheduleInterface {

	public HmiResultObj createPlan(ScheduleFactory scheduleFactory);
	
	public ScheduleFactory findScheduleFactoryPlanById(long id);
	
	public List<SchedulePlan> findSchedulePlansbyUserId(long userId,long saleId);
	
	public ScheduleObj createSchedule(ScheduleObj scheduleObj);
	
	public ScheduleObj continueSchedule(ScheduleObj scheduleObj);
	
	public ScheduleObj updateSchedule(ScheduleObj scheduleObj);
	
	public ScheduleObj changeSchedule(ScheduleObj scheduleObj);
	
	public List<ScheduleFactory> findSchedulesBySchId(long schId);
	
	public List<ScheduleFactory> findSchedulesTimesBySchtId(long schtId);
	
	public List<ScheduleFactory> findUserSchedulesTimesBySaleId(long saleId);
	
	
	public SchedulePlan findSchedulePlanBySaleId(long saleId);
	
	public HmiResultObj deleteScheduleUsersPlan(ScheduleFactory scheduleFactory);
	
	
}
