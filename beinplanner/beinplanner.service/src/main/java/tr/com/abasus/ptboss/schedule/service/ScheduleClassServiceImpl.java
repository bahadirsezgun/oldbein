package tr.com.abasus.ptboss.schedule.service;

import java.util.Date;
import java.util.List;

import tr.com.abasus.ptboss.ptuser.entity.User;
import tr.com.abasus.ptboss.result.entity.HmiResultObj;
import tr.com.abasus.ptboss.schedule.businessEntity.StaffClassPlans;
import tr.com.abasus.ptboss.schedule.dao.ScheduleClassDao;
import tr.com.abasus.ptboss.schedule.entity.ScheduleFactory;
import tr.com.abasus.ptboss.schedule.entity.SchedulePlan;
import tr.com.abasus.ptboss.schedule.entity.ScheduleUsersClassPlan;
import tr.com.abasus.util.DateTimeUtil;

public class ScheduleClassServiceImpl implements ScheduleClassService {

	
	ScheduleClassDao scheduleClassDao;

	public ScheduleClassDao getScheduleClassDao() {
		return scheduleClassDao;
	}

	public void setScheduleClassDao(ScheduleClassDao scheduleClassDao) {
		this.scheduleClassDao = scheduleClassDao;
	}

	
	@Override
	public List<ScheduleUsersClassPlan> usersClassPlansBySaleId(long saleId) {
		return scheduleClassDao.usersClassPlansBySaleId(saleId);
	}

	

	@Override
	public HmiResultObj createScheduleUsersClassPlan(ScheduleUsersClassPlan scheduleUsersClassPlan) {
		return scheduleClassDao.createScheduleUsersClassPlan(scheduleUsersClassPlan);
	}

	

	@Override
	public List<ScheduleUsersClassPlan> findScheduleUsersClassPlanByPlanId(long schId) {
		return scheduleClassDao.findScheduleUsersClassPlanByPlanId(schId);
	}

	@Override
	public List<ScheduleUsersClassPlan> findScheduleUsersClassPlanByUserId(long userId) {
		return scheduleClassDao.findScheduleUsersClassPlanByUserId(userId);
	}
	
	@Override
	public List<ScheduleUsersClassPlan> findScheduleUsersClassPlanByUserIdAndSaleId(long userId,long saleId) {
		return scheduleClassDao.findScheduleUsersClassPlanByUserIdAndSaleId(userId,saleId);
	}

	@Override
	public List<ScheduleUsersClassPlan> findScheduleUsersClassPlanByTimePlanId(long schtId) {
		return scheduleClassDao.findScheduleUsersClassPlanByTimePlanId(schtId);
	}

	@Override
	public List<SchedulePlan> findScheduleClassPlansbyUserId(long userId,long saleId) {
		return scheduleClassDao.findScheduleClassPlansbyUserId(userId,saleId);
	}

	@Override
	public SchedulePlan findSchedulePlanBySaleId(long saleId) {
		return scheduleClassDao.findSchedulePlanBySaleId(saleId);
	}

	@Override
	public List<ScheduleUsersClassPlan> findScheduleUsersClassPlanByPlanIdAndUserId(long schId, long userId) {
		return scheduleClassDao.findScheduleUsersClassPlanByPlanIdAndUserId(schId, userId);
	}

	@Override
	public List<User> findUsersInClassPlanToGroup(long schId) {
		return scheduleClassDao.findUsersInClassPlanToGroup(schId);
	}

	@Override
	public HmiResultObj deleteScheduleUsersClassPlan(ScheduleUsersClassPlan scheduleUsersClassPlan) {
		return scheduleClassDao.deleteScheduleUsersClassPlan(scheduleUsersClassPlan);
	}

	@Override
	public List<StaffClassPlans> findStaffClassPlans(Date startDate, Date endDate, long userId) {
		
		List<StaffClassPlans> staffClassPlans=scheduleClassDao.findStaffClassPlans(startDate, endDate, userId);
		
		String color1="#62cb31";
		String color11="#ffffff";
		String color2="#fedde4";
		String color22="#000000";
		String currentColor=color1;
		String currentColorFont=color11;
		long prevSchtId=0;
		for (StaffClassPlans staffClassPlan : staffClassPlans) {
			staffClassPlan.setPlanDay(DateTimeUtil.getDayNames(staffClassPlan.getPlanStartDate()));
			
			if(prevSchtId!=0){
				if(prevSchtId==staffClassPlan.getSchtId()){
					staffClassPlan.setColorOfLine(currentColor);
					staffClassPlan.setColorOfLineFont(currentColorFont);
				}else{
					if(currentColor.equals(color1)){
						currentColor=color2;
						currentColorFont=color22;
					}else{
						currentColor=color1;
						currentColorFont=color11;
					}
					staffClassPlan.setColorOfLine(currentColor);
					staffClassPlan.setColorOfLineFont(currentColorFont);
				}
				
				prevSchtId=staffClassPlan.getSchtId();
			}else{
				staffClassPlan.setColorOfLine(currentColor);
				staffClassPlan.setColorOfLineFont(currentColorFont);
				prevSchtId=staffClassPlan.getSchtId();
			}
		}
		return staffClassPlans;
		
		
		
		
	}

	
	
	
	
}
