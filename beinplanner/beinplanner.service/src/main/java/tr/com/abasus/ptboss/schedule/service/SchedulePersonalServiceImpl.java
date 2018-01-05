package tr.com.abasus.ptboss.schedule.service;

import java.util.Date;
import java.util.List;

import tr.com.abasus.ptboss.ptuser.entity.User;
import tr.com.abasus.ptboss.result.entity.HmiResultObj;
import tr.com.abasus.ptboss.schedule.businessEntity.StaffClassPlans;
import tr.com.abasus.ptboss.schedule.dao.SchedulePersonalDao;
import tr.com.abasus.ptboss.schedule.entity.ScheduleFactory;
import tr.com.abasus.ptboss.schedule.entity.SchedulePlan;
import tr.com.abasus.ptboss.schedule.entity.ScheduleUsersPersonalPlan;
import tr.com.abasus.util.DateTimeUtil;

public class SchedulePersonalServiceImpl implements SchedulePersonalService{

	SchedulePersonalDao schedulePersonalDao;

	public SchedulePersonalDao getSchedulePersonalDao() {
		return schedulePersonalDao;
	}

	public void setSchedulePersonalDao(SchedulePersonalDao schedulePersonalDao) {
		this.schedulePersonalDao = schedulePersonalDao;
	}

	@Override
	public synchronized List<ScheduleUsersPersonalPlan> usersPersonalPlansBySaleId(long saleId) {
		return schedulePersonalDao.usersPersonalPlansBySaleId(saleId);
	}

	

	

	@Override
	public synchronized HmiResultObj createScheduleUsersPersonalPlan(ScheduleUsersPersonalPlan scheduleUsersPersonalPlan) {
		return schedulePersonalDao.createScheduleUsersPersonalPlan(scheduleUsersPersonalPlan);
	}

	

	@Override
	public List<ScheduleUsersPersonalPlan> findScheduleUsersPersonalPlanByPlanId(long schId) {
		return schedulePersonalDao.findScheduleUsersPersonalPlanByPlanId(schId);
	}

	@Override
	public List<ScheduleUsersPersonalPlan> findScheduleUsersPersonalPlanByUserId(long userId) {
		return schedulePersonalDao.findScheduleUsersPersonalPlanByUserId(userId);
	}

	@Override
	public List<ScheduleUsersPersonalPlan> findScheduleUsersPersonalPlanByUserIdAndSaleId(long userId,long saleId) {
		return schedulePersonalDao.findScheduleUsersPersonalPlanByUserIdAndSaleId(userId,saleId);
	}

	@Override
	public List<ScheduleUsersPersonalPlan> findScheduleUsersPersonalPlanByTimePlanId(long schtId) {
		return schedulePersonalDao.findScheduleUsersPersonalPlanByTimePlanId(schtId);
	}

	@Override
	public List<SchedulePlan> findSchedulePersonalPlansbyUserId(long userId,long saleId) {
		return schedulePersonalDao.findSchedulePersonalPlansbyUserId(userId,saleId);
	}

	@Override
	public SchedulePlan findSchedulePlanBySaleId(long saleId) {
		return schedulePersonalDao.findSchedulePlanBySaleId(saleId);
	}

	@Override
	public List<ScheduleUsersPersonalPlan> findScheduleUsersPersonalPlanByPlanIdAndUserId(long schId, long userId) {
	    return schedulePersonalDao.findScheduleUsersPersonalPlanByPlanIdAndUserId(schId, userId);
	}

	@Override
	public List<User> findUsersInPersonalPlanToGroup(long schId) {
		return schedulePersonalDao.findUsersInPersonalPlanToGroup(schId);
	}

	@Override
	public HmiResultObj deleteScheduleUsersPersonalPlan(ScheduleUsersPersonalPlan scheduleUsersPersonalPlan) {
		return schedulePersonalDao.deleteScheduleUsersPersonalPlan(scheduleUsersPersonalPlan);
	}

	@Override
	public List<StaffClassPlans> findStaffClassPlans(Date startDate, Date endDate, long userId) {
		
		List<StaffClassPlans> staffClassPlans=schedulePersonalDao.findStaffClassPlans(startDate, endDate, userId);
		
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
