package tr.com.abasus.ptboss.schedule.dao;

import java.util.Date;
import java.util.List;

import tr.com.abasus.ptboss.ptuser.entity.User;
import tr.com.abasus.ptboss.result.entity.HmiResultObj;
import tr.com.abasus.ptboss.schedule.businessEntity.StaffClassPlans;
import tr.com.abasus.ptboss.schedule.entity.ScheduleFactory;
import tr.com.abasus.ptboss.schedule.entity.SchedulePlan;
import tr.com.abasus.ptboss.schedule.entity.ScheduleUsersPersonalPlan;

public interface SchedulePersonalDao {

	
	public List<ScheduleUsersPersonalPlan> usersPersonalPlansBySaleId(long saleId);
	
	public HmiResultObj createScheduleUsersPersonalPlan(ScheduleUsersPersonalPlan scheduleUsersPersonalPlan);
	
	public HmiResultObj deleteScheduleUsersPersonalPlan(ScheduleUsersPersonalPlan scheduleUsersPersonalPlan);
	
	public List<ScheduleUsersPersonalPlan> findScheduleUsersPersonalPlanByPlanId(long schId);
	
	public List<ScheduleUsersPersonalPlan> findScheduleUsersPersonalPlanByUserId(long userId);
	
	public List<ScheduleUsersPersonalPlan> findScheduleUsersPersonalPlanByUserIdAndSaleId(long userId,long saleId);
	
	public List<ScheduleUsersPersonalPlan> findScheduleUsersPersonalPlanByTimePlanId(long schtId);
	
	public List<SchedulePlan> findSchedulePersonalPlansbyUserId(long userId,long saleId);

	public SchedulePlan findSchedulePlanBySaleId(long saleId);

	public List<ScheduleUsersPersonalPlan> findScheduleUsersPersonalPlanByPlanIdAndUserId(long schId,long userId);
	
	public List<User> findUsersInPersonalPlanToGroup(long schId);
	
	public List<StaffClassPlans> findStaffClassPlans(Date startDate,Date endDate,long userId);
	
	
	
	
}
