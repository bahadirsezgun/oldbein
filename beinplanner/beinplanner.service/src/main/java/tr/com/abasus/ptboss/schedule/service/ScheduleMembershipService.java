package tr.com.abasus.ptboss.schedule.service;

import java.util.List;

import tr.com.abasus.ptboss.ptuser.entity.User;
import tr.com.abasus.ptboss.result.entity.HmiResultObj;
import tr.com.abasus.ptboss.schedule.entity.ScheduleFactory;
import tr.com.abasus.ptboss.schedule.entity.ScheduleMembershipPlan;
import tr.com.abasus.ptboss.schedule.entity.ScheduleMembershipTimePlan;

public interface ScheduleMembershipService {

	public HmiResultObj createPlan(ScheduleFactory scheduleFactory);
	
	public HmiResultObj deletePlan(ScheduleFactory scheduleFactory);
	
	public ScheduleFactory findSchedulePlanById(long id);
	
	public HmiResultObj createScheduleMembershipTimePlan(ScheduleMembershipTimePlan scheduleMembershipTimePlan);
	
	public List<ScheduleMembershipTimePlan> findScheduleTimePlanBySmpId(long smpId);
	
	public ScheduleMembershipTimePlan findScheduleTimePlanBySmtpId(long smtpId);
	
	public HmiResultObj deleteScheduleMembershipTimePlan(ScheduleMembershipTimePlan scheduleMembershipTimePlan);
	
	
}
