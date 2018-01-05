package tr.com.abasus.ptboss.schedule.dao;

import java.util.List;

import tr.com.abasus.ptboss.ptuser.entity.User;
import tr.com.abasus.ptboss.result.entity.HmiResultObj;
import tr.com.abasus.ptboss.schedule.entity.ScheduleFactory;
import tr.com.abasus.ptboss.schedule.entity.ScheduleMembershipTimePlan;

public interface ScheduleMembershipDao {

	public HmiResultObj createSchedulePlan(ScheduleFactory scheduleFactory);
	
	public HmiResultObj deletePlan(ScheduleFactory scheduleFactory);
	
	public HmiResultObj createScheduleMembershipTimePlan(ScheduleMembershipTimePlan scheduleMembershipTimePlan);
	
	public ScheduleFactory findSchedulePlanById(long id);
	
	public ScheduleFactory findSchedulePlanBySaleId(long saleId);
	
	
	public List<ScheduleFactory> findSchedulesbyUserId(long userId); 
	
	public List<ScheduleMembershipTimePlan> findScheduleTimePlanBySmpId(long smpId);
	
	public ScheduleMembershipTimePlan findScheduleTimePlanBySmtpId(long smtpId);
	
	public HmiResultObj deleteScheduleMembershipTimePlan(ScheduleMembershipTimePlan scheduleMembershipTimePlan);
	
	
	
}
