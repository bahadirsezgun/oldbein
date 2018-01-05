package tr.com.abasus.ptboss.schedule.service;

import java.util.Date;
import java.util.List;

import tr.com.abasus.ptboss.ptuser.entity.User;
import tr.com.abasus.ptboss.result.entity.HmiResultObj;
import tr.com.abasus.ptboss.schedule.businessEntity.StaffClassPlans;
import tr.com.abasus.ptboss.schedule.entity.ScheduleFactory;
import tr.com.abasus.ptboss.schedule.entity.SchedulePlan;
import tr.com.abasus.ptboss.schedule.entity.ScheduleUsersClassPlan;

public interface ScheduleClassService {

	
	
	public List<ScheduleUsersClassPlan> usersClassPlansBySaleId(long saleId);
	
	public HmiResultObj createScheduleUsersClassPlan(ScheduleUsersClassPlan scheduleUsersClassPlan);
	
	public HmiResultObj deleteScheduleUsersClassPlan(ScheduleUsersClassPlan scheduleUsersClassPlan);
	
	public List<ScheduleUsersClassPlan> findScheduleUsersClassPlanByPlanId(long schId);
	
	public List<ScheduleUsersClassPlan> findScheduleUsersClassPlanByPlanIdAndUserId(long schId,long userId);
	
	public List<ScheduleUsersClassPlan> findScheduleUsersClassPlanByUserId(long userId);
	
	public List<ScheduleUsersClassPlan> findScheduleUsersClassPlanByUserIdAndSaleId(long userId,long saleId);
	
	public List<ScheduleUsersClassPlan> findScheduleUsersClassPlanByTimePlanId(long schtId);
	
	public List<SchedulePlan> findScheduleClassPlansbyUserId(long userId,long saleId);
	
	public SchedulePlan findSchedulePlanBySaleId(long saleId);
	
	public List<User> findUsersInClassPlanToGroup(long schId);
	
	public List<StaffClassPlans> findStaffClassPlans(Date startDate,Date endDate,long userId);
	
	
}
