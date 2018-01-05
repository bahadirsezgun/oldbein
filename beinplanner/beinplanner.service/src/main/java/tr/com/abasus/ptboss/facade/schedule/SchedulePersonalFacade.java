package tr.com.abasus.ptboss.facade.schedule;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tr.com.abasus.ptboss.bonus.facadeService.UserBonusPaymentPersonalFacadeService;
import tr.com.abasus.ptboss.facade.dao.SchedulerFacadeDao;
import tr.com.abasus.ptboss.program.entity.ProgramFactory;
import tr.com.abasus.ptboss.result.entity.HmiResultObj;
import tr.com.abasus.ptboss.schedule.entity.ScheduleFactory;
import tr.com.abasus.ptboss.schedule.entity.SchedulePlan;
import tr.com.abasus.ptboss.schedule.entity.ScheduleStudios;
import tr.com.abasus.ptboss.schedule.entity.ScheduleTimePlan;
import tr.com.abasus.ptboss.schedule.entity.ScheduleUsersPersonalPlan;
import tr.com.abasus.ptboss.schedule.service.SchedulePersonalService;
import tr.com.abasus.util.GlobalUtil;
import tr.com.abasus.util.OhbeUtil;
import tr.com.abasus.util.ProgDurationTypes;
import tr.com.abasus.util.RestFlagUtil;
import tr.com.abasus.util.ResultStatuObj;

@Service
public class SchedulePersonalFacade implements ScheduleFacadeService{

	
	@Autowired
	SchedulerFacadeDao schedulerFacadeDao;
	
	@Autowired
	SchedulePersonalService schedulePersonalService;
	
	@Autowired
	UserBonusPaymentPersonalFacadeService userBonusPaymentPersonalFacadeService;
	
	
	
	@Override
	//public HmiResultObj isTimeProperToCreate(Date planTime, List<ScheduleStudios> studios, long schedulerStaffId,ProgramFactory programFactory) {
		public HmiResultObj isTimeProperToCreate(ScheduleTimePlan scheduleTimePlan,ProgramFactory programFactory) {
				/**
		 * 1. Eğitmenin o saatte dersi varmı?
		 * 2. Studyo o saatte rezerve edilmiş mi ?
		 */
		HmiResultObj hmiResultObj=new HmiResultObj();
		
		//int classDuration= GlobalUtil.defCalendarTimes.getDuration();
		/*
		int progBeforeDuration=(programFactory.getProgBeforeDuration()-1);
		if(progBeforeDuration<0)
			progBeforeDuration=1;
		
		int progAfterDuration=programFactory.getProgAfterDuration()-1;
		if(progAfterDuration<0)
			progAfterDuration=-1;
		*/
		
		int progDuration=GlobalUtil.defCalendarTimes.getDuration()-1;
		
		Date planStartDate=(Date)OhbeUtil.getDateForNextMinute(scheduleTimePlan.getPlanStartDate(), 1).clone();
		Date planEndDate=(Date)OhbeUtil.getDateForNextMinute(scheduleTimePlan.getPlanStartDate(), progDuration).clone();
		
		hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS);
		hmiResultObj.setResultMessage(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
		
		
		
		
		if(!schedulerFacadeDao.haveInstructorGotClassesInThisTime(scheduleTimePlan.getSchtStaffId(), planStartDate, planEndDate,scheduleTimePlan.getSchtId())){
			for (ScheduleStudios scheduleStudios : scheduleTimePlan.getScheduleStudios()) {
				
				if(schedulerFacadeDao.isStudioReservedInThisTime(scheduleStudios.getStudioId(), planStartDate, planEndDate,scheduleTimePlan.getSchtId())){
					hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
					hmiResultObj.setResultMessage("studioisReservedInThisTime");
					break;
				}
			}
		}else{
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
			hmiResultObj.setResultMessage("instructorhaveGotClassesInThisTime");
		}
		return hmiResultObj;
	}
	
	@Override
	public HmiResultObj canScheduleCreate(Date timePlanDate,long schtStaffId) {
		HmiResultObj hmiResultObj=new HmiResultObj();
		hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS);
		hmiResultObj.setResultMessage(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
		if(userBonusPaymentPersonalFacadeService.isLockBonusPayedToDates(timePlanDate,schtStaffId)){
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
			hmiResultObj.setResultMessage("bonusPayedForTimePlan");
			
		}
		return hmiResultObj;
	}

	@Override
	public HmiResultObj canScheduleDelete(long schtId) {
		HmiResultObj hmiResultObj=new HmiResultObj();
		hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS);
		hmiResultObj.setResultMessage(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
		if(userBonusPaymentPersonalFacadeService.isLockBonusPayedClasses(schtId)){
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
			hmiResultObj.setResultMessage("bonusPayedForTimePlan");
			
		}
		return hmiResultObj;
	}

	@Override
	public HmiResultObj canScheduleChange(long schtId) {
		HmiResultObj hmiResultObj=new HmiResultObj();
		hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS);
		hmiResultObj.setResultMessage(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
		if(userBonusPaymentPersonalFacadeService.isLockBonusPayedClasses(schtId)){
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
			hmiResultObj.setResultMessage("bonusPayedForTimePlan");
			
		}
		return hmiResultObj;
	}

	/**
	 * in chain design pattern. Next filter in ScheduleClassFacade
	 */
	
	@Override
	public HmiResultObj canUserDelete(long userId) {
		HmiResultObj hmiResultObj=new HmiResultObj();
		hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS);
		hmiResultObj.setResultMessage(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
		
		List<ScheduleUsersPersonalPlan> scheduleUsersPersonalPlans= schedulePersonalService.findScheduleUsersPersonalPlanByUserId(userId);
		if(scheduleUsersPersonalPlans.size()>0){
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
			hmiResultObj.setResultMessage("memberHavePlanning");
			return hmiResultObj;
		}
		
		return hmiResultObj;
	}

	@Override
	public HmiResultObj canUserRemoveFromTimePlan(ScheduleFactory scheduleFactory) {
		HmiResultObj hmiResultObj=new HmiResultObj();
		hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS);
		hmiResultObj.setResultMessage(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
		long schtId=scheduleFactory.getSchtId();
		
		List<ScheduleUsersPersonalPlan> scheduleUsersPersonalPlans= schedulePersonalService.findScheduleUsersPersonalPlanByTimePlanId(schtId);
		if(scheduleUsersPersonalPlans.size()==1){
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
			hmiResultObj.setResultMessage("oneMemberIncluded");
			return hmiResultObj;
		}
		
		if(userBonusPaymentPersonalFacadeService.isLockBonusPayedClasses(scheduleFactory.getSuppId())){
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
			hmiResultObj.setResultMessage("bonusPayedForTimePlan");
			return hmiResultObj;
			
		}
		return hmiResultObj;
	}

	@Override
	public HmiResultObj canSchedulePlanProperToCreate(ScheduleTimePlan scheduleTimePlan,
			ProgramFactory programFactory,long schId) {
		
		HmiResultObj hmiResultObj=new HmiResultObj();
		hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS);
		hmiResultObj.setResultMessage(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
		int restDuration=programFactory.getRestDuration();
		
		if(programFactory.getRestFlag()==RestFlagUtil.RESTIRICTION_FLAG_YES){
		  
		   ScheduleTimePlan schFirstPlan=schedulerFacadeDao.findFirstClassOfPlan(schId);
		   if(schFirstPlan!=null){
		   Date planStartDate=schFirstPlan.getPlanStartDate();
		   
		    Date planEndDate=new Date();
			if(programFactory.getRestType()==ProgDurationTypes.DURATION_TYPE_MONTHLY){
				planEndDate=OhbeUtil.getDateForNextMonth(planStartDate, restDuration);
				
			}else{
				if(programFactory.getRestType()==ProgDurationTypes.DURATION_TYPE_WEEKLY){
					restDuration=restDuration*7;
				}
				planEndDate=OhbeUtil.getDateForNextDate(planStartDate, restDuration);
			}
		
		    if(planEndDate.before(scheduleTimePlan.getPlanStartDate())){
		    	hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
				hmiResultObj.setResultMessage("planHaveTimeOutRestriction");
		    }
		   }
		}
		return hmiResultObj;
	}

	

	
}
