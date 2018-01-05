package tr.com.abasus.ptboss.schedule.service;

import java.util.Date;
import java.util.List;

import tr.com.abasus.ptboss.packetsale.entity.PacketSaleFactory;
import tr.com.abasus.ptboss.ptuser.entity.User;
import tr.com.abasus.ptboss.result.entity.HmiResultObj;
import tr.com.abasus.ptboss.schedule.businessEntity.ScheduleSearchObj;
import tr.com.abasus.ptboss.schedule.dao.ScheduleDao;
import tr.com.abasus.ptboss.schedule.entity.SchedulePlan;
import tr.com.abasus.ptboss.schedule.entity.ScheduleStudios;
import tr.com.abasus.ptboss.schedule.entity.ScheduleTimePlan;
import tr.com.abasus.util.SaleStatus;

public class ScheduleServiceImpl implements ScheduleService {

	ScheduleDao scheduleDao;

	public ScheduleDao getScheduleDao() {
		return scheduleDao;
	}

	public void setScheduleDao(ScheduleDao scheduleDao) {
		this.scheduleDao = scheduleDao;
	}
	
	
	@Override
	public HmiResultObj createSchedulePlan(SchedulePlan schedulePlan) {
		return scheduleDao.createSchedulePlan(schedulePlan);
	}

	@Override
	public HmiResultObj createScheduleTimePlan(ScheduleTimePlan scheduleTimePlan) {
		return scheduleDao.createScheduleTimePlan(scheduleTimePlan);
	}

	@Override
	public HmiResultObj createScheduleStudio(ScheduleStudios scheduleStudios) {
		return scheduleDao.createScheduleStudio(scheduleStudios);
	}
	@Override
	public List<ScheduleTimePlan> findScheduleTimePlanByPlanId(long schId) {
		return scheduleDao.findScheduleTimePlanByPlanId(schId);
	}
	
	@Override
	public synchronized HmiResultObj deleteSchedulePlan(SchedulePlan schedulePlan) {
		return scheduleDao.deleteSchedulePlan(schedulePlan);
	}

	@Override
	public SchedulePlan findSchedulePlanByPlanId(long schId) {
		return scheduleDao.findSchedulePlanByPlanId(schId);
	}

	@Override
	public List<ScheduleStudios> findScheduleStudiosByTimePlanId(long schtId) {
		return scheduleDao.findScheduleStudiosByTimePlanId(schtId);
	}

	@Override
	public List<SchedulePlan> searchByQuery(ScheduleSearchObj scheduleSearchObj) {
		List<SchedulePlan> schedulePlans=scheduleDao.searchByQueryForStaff(scheduleSearchObj);
		List<SchedulePlan> schedulePlansForStudio=scheduleDao.searchByQueryForStudiosForOtherStaffs(scheduleSearchObj);
		schedulePlans.addAll(schedulePlansForStudio);
		return schedulePlans;
	}

	@Override
	public List<ScheduleTimePlan> findScheduleTimePlanByPlanIdByDates(long schId, Date startDate, Date endDate) {
		return scheduleDao.findScheduleTimePlanByPlanIdByDates(schId, startDate, endDate);
	}

	@Override
	public synchronized HmiResultObj deleteScheduleTimePlan(ScheduleTimePlan scheduleTimePlan) {
		HmiResultObj hmiResultObj=scheduleDao.deleteScheduleTimePlan(scheduleTimePlan);
		
		
		return hmiResultObj;
	}

	@Override
	public List<ScheduleTimePlan> findScheduleTimePlanByDates(Date startDate, Date endDate) {
		return scheduleDao.findScheduleTimePlanByDates(startDate, endDate);
	}

	@Override
	public int isScheduleTimeLast(SchedulePlan schedulePlan, ScheduleTimePlan scheduleTimePlan,List<ScheduleTimePlan> scheduleTimePlans) {
		int lastOfPlan=0;
		
		if(schedulePlan==null)
			schedulePlan=scheduleDao.findSchedulePlanByPlanId(scheduleTimePlan.getSchId());
		
		if(scheduleTimePlans==null)
			scheduleTimePlans=scheduleDao.findScheduleTimePlanByPlanId(scheduleTimePlan.getSchId());
		
		if(schedulePlan.getSchCount()==scheduleTimePlans.size()){
			ScheduleTimePlan lastTimePlan=scheduleTimePlans.get(scheduleTimePlans.size()-1);
			if(lastTimePlan.getSchtId()==scheduleTimePlan.getSchtId())
				lastOfPlan=1;
		}
		return lastOfPlan;
	}

	@Override
	public List<ScheduleTimePlan> findScheduleTimePlanByStaffAndDate(Date startDate, Date endDate, long staffId) {
		return scheduleDao.findScheduleTimePlanByStaffAndDate(startDate, endDate, staffId);
	}

	@Override
	public ScheduleTimePlan findScheduleTimePlanById(long schtId) {
		return scheduleDao.findScheduleTimePlanById(schtId);
	}

	@Override
	public HmiResultObj deleteJustScheduleTimePlan(ScheduleTimePlan scheduleTimePlan) {
		return scheduleDao.deleteScheduleTimePlan(scheduleTimePlan);
	}


	
	
}
