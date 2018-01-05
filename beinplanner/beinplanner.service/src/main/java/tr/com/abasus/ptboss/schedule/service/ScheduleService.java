package tr.com.abasus.ptboss.schedule.service;

import java.util.Date;
import java.util.List;

import tr.com.abasus.ptboss.result.entity.HmiResultObj;
import tr.com.abasus.ptboss.schedule.businessEntity.ScheduleSearchObj;
import tr.com.abasus.ptboss.schedule.entity.SchedulePlan;
import tr.com.abasus.ptboss.schedule.entity.ScheduleStudios;
import tr.com.abasus.ptboss.schedule.entity.ScheduleTimePlan;

public interface ScheduleService {

	    public HmiResultObj createSchedulePlan(SchedulePlan schedulePlan);
		
		public HmiResultObj createScheduleTimePlan(ScheduleTimePlan scheduleTimePlan);
		
		public HmiResultObj createScheduleStudio(ScheduleStudios scheduleStudios);
		
		public HmiResultObj deleteSchedulePlan(SchedulePlan schedulePlan);
		
		public HmiResultObj deleteScheduleTimePlan(ScheduleTimePlan scheduleTimePlan);
		
		public HmiResultObj deleteJustScheduleTimePlan(ScheduleTimePlan scheduleTimePlan);
		
		
		public List<SchedulePlan> searchByQuery(ScheduleSearchObj scheduleSearchObj); 
		
		public SchedulePlan findSchedulePlanByPlanId(long schId);
		
		public ScheduleTimePlan findScheduleTimePlanById(long schtId);
			
		public List<ScheduleTimePlan> findScheduleTimePlanByPlanId(long schId);
		
	    public List<ScheduleStudios> findScheduleStudiosByTimePlanId(long schtId);
		
	    public List<ScheduleTimePlan> findScheduleTimePlanByPlanIdByDates(long schId,Date startDate,Date endDate);
		
	    public List<ScheduleTimePlan> findScheduleTimePlanByDates(Date startDate,Date endDate);
		
	    
	    public List<ScheduleTimePlan> findScheduleTimePlanByStaffAndDate(Date startDate,Date endDate,long staffId);
		
	    public int isScheduleTimeLast(SchedulePlan schedulePlan,ScheduleTimePlan scheduleTimePlan,List<ScheduleTimePlan> scheduleTimePlans);
   
       
	   
}
