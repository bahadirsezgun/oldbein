package tr.com.abasus.ptboss.schedule.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import tr.com.abasus.ptboss.program.entity.ProgramFactory;
import tr.com.abasus.ptboss.ptuser.entity.User;
import tr.com.abasus.ptboss.result.entity.HmiResultObj;
import tr.com.abasus.ptboss.schedule.dao.ScheduleMembershipDao;
import tr.com.abasus.ptboss.schedule.entity.ScheduleFactory;
import tr.com.abasus.ptboss.schedule.entity.ScheduleMembershipPlan;
import tr.com.abasus.ptboss.schedule.entity.ScheduleMembershipTimePlan;
import tr.com.abasus.util.DateTimeUtil;
import tr.com.abasus.util.GlobalUtil;
import tr.com.abasus.util.OhbeUtil;
import tr.com.abasus.util.ProgDurationTypes;
import tr.com.abasus.util.StatuTypes;

public class ScheduleMembershipServiceImpl implements  ScheduleMembershipService{

	ScheduleMembershipDao scheduleMembershipDao;

	@Autowired
	@Qualifier(value="programMembership")
	ProgramFactory programFactory;
	
	public ScheduleMembershipDao getScheduleMembershipDao() {
		return scheduleMembershipDao;
	}

	public void setScheduleMembershipDao(ScheduleMembershipDao scheduleMembershipDao) {
		this.scheduleMembershipDao = scheduleMembershipDao;
	}

	@Override
	public synchronized HmiResultObj createPlan(ScheduleFactory scheduleFactory) {
		return scheduleMembershipDao.createSchedulePlan(scheduleFactory);
	}

	@Override
	public  ScheduleFactory findSchedulePlanById(long id) {
		
		ScheduleMembershipPlan scheduleFactory=(ScheduleMembershipPlan)scheduleMembershipDao.findSchedulePlanById(id);
		ProgramFactory pmf=programFactory.findProgramById(scheduleFactory.getProgId());
		
		List<ScheduleMembershipTimePlan> scheduleMembershipTimePlans=scheduleFactory.getScheduleMembershipTimePlans();
		String status=StatuTypes.getStatuTypes(-1);
		/*
		for (ScheduleMembershipTimePlan scheduleMembershipTimePlan : scheduleMembershipTimePlans) {
			scheduleMembershipTimePlan.setSmpStartDayName(OhbeUtil.getDayNamesStaticEn(scheduleMembershipTimePlan.getSmpStartDate()));
			scheduleMembershipTimePlan.setSmpEndDayName(OhbeUtil.getDayNamesStaticEn(scheduleMembershipTimePlan.getSmpEndDate()));
		}
		*/
		
		
		
		
		int i=0;
		
		for (ScheduleMembershipTimePlan scheduleMembershipTimePlan : scheduleMembershipTimePlans) {
			
			
			
			
			if(i<scheduleMembershipTimePlans.size()-1){
				int freezeDuration=pmf.getFreezeDuration(); 
				if(pmf.getFreezeDurationType()==ProgDurationTypes.DURATION_TYPE_MONTHLY){
					
					Date freezeEndDate=ProgDurationTypes.getDateForNextMonth(scheduleMembershipTimePlan.getSmpStartDate(),freezeDuration);
					scheduleMembershipTimePlan.setSmpFreezeEndDateStr(OhbeUtil.getDateStrByFormat(freezeEndDate, GlobalUtil.global.getPtScrDateFormat()));
					scheduleMembershipTimePlan.setSmpFreezeEndDayName(DateTimeUtil.getDayNames(freezeEndDate));
				}else{
					if(pmf.getFreezeDurationType()==ProgDurationTypes.DURATION_TYPE_WEEKLY){
						freezeDuration=freezeDuration*7;
					}
				
					Date freezeEndDate=ProgDurationTypes.getDateForNextDate(scheduleMembershipTimePlan.getSmpStartDate(),freezeDuration);
					scheduleMembershipTimePlan.setSmpFreezeEndDateStr(OhbeUtil.getDateStrByFormat(freezeEndDate, GlobalUtil.global.getPtScrDateFormat()));
					scheduleMembershipTimePlan.setSmpFreezeEndDayName(DateTimeUtil.getDayNames(freezeEndDate));
				}
			}
			scheduleMembershipTimePlan.setSmpStartDayName(DateTimeUtil.getDayNames(scheduleMembershipTimePlan.getSmpStartDate()));
			scheduleMembershipTimePlan.setSmpEndDayName(DateTimeUtil.getDayNames(scheduleMembershipTimePlan.getSmpEndDate()));
			
			scheduleMembershipTimePlan.setSmpStatusStr(StatuTypes.getStatuByDate(scheduleMembershipTimePlan.getSmpStartDate(), scheduleMembershipTimePlan.getSmpEndDate()));
			status=scheduleMembershipTimePlan.getSmpStatusStr();
			i++;
		}
		
		
		
		scheduleFactory.setSmpStatusStr(status);
		
		return scheduleFactory;
	}

	@Override
	public HmiResultObj createScheduleMembershipTimePlan(ScheduleMembershipTimePlan scheduleMembershipTimePlan) {
		return scheduleMembershipDao.createScheduleMembershipTimePlan(scheduleMembershipTimePlan);
	}

	@Override
	public List<ScheduleMembershipTimePlan> findScheduleTimePlanBySmpId(long smpId) {
		return scheduleMembershipDao.findScheduleTimePlanBySmpId(smpId);
	}

	@Override
	public ScheduleMembershipTimePlan findScheduleTimePlanBySmtpId(long smtpId) {
		return scheduleMembershipDao.findScheduleTimePlanBySmtpId(smtpId);
	}

	@Override
	public HmiResultObj deletePlan(ScheduleFactory scheduleFactory) {
		return scheduleMembershipDao.deletePlan(scheduleFactory);
	}

	@Override
	public HmiResultObj deleteScheduleMembershipTimePlan(ScheduleMembershipTimePlan scheduleMembershipTimePlan) {
		return scheduleMembershipDao.deleteScheduleMembershipTimePlan(scheduleMembershipTimePlan);
	}

	
	

	
	
}
