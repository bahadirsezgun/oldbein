package tr.com.abasus.ptboss.schedule.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import tr.com.abasus.ptboss.exceptions.UnAuthorizedUserException;
import tr.com.abasus.ptboss.facade.schedule.ScheduleMembershipFacade;
import tr.com.abasus.ptboss.program.entity.ProgramFactory;
import tr.com.abasus.ptboss.ptuser.entity.User;
import tr.com.abasus.ptboss.result.entity.HmiResultObj;
import tr.com.abasus.ptboss.schedule.dao.ScheduleMembershipDao;
import tr.com.abasus.ptboss.schedule.entity.ScheduleFactory;
import tr.com.abasus.ptboss.schedule.entity.ScheduleMembershipPlan;
import tr.com.abasus.ptboss.schedule.entity.ScheduleMembershipTimePlan;
import tr.com.abasus.ptboss.schedule.service.ScheduleMembershipService;
import tr.com.abasus.util.GlobalUtil;
import tr.com.abasus.util.OhbeUtil;
import tr.com.abasus.util.ProgDurationTypes;
import tr.com.abasus.util.ResultStatuObj;
import tr.com.abasus.util.SessionUtil;

@Controller
@RequestMapping(value="/scheduleMembership")
public class ScheduleMembershipController {

	@Autowired
	@Qualifier(value="scheduleMembershipPlan")
	ScheduleFactory scheduleFactory;
	
	
	@Autowired
	@Qualifier(value="programMembership")
	ProgramFactory programFactory;
	
	
	@Autowired
	ScheduleMembershipService scheduleMembershipService;
	
	@Autowired
	ScheduleMembershipFacade scheduleMembershipFacade;
	
	@RequestMapping(value="/freezeSchedule", method = RequestMethod.POST) 
	public @ResponseBody HmiResultObj freezeSchedule(@RequestBody ScheduleFactory smp, HttpServletRequest request) throws UnAuthorizedUserException {
		
		User user=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if((user==null)){
			throw new UnAuthorizedUserException("");
		}
		
		HmiResultObj hmiResultObj=new HmiResultObj();
		ScheduleMembershipPlan smpInDb=(ScheduleMembershipPlan)scheduleFactory.findScheduleFactoryPlanById(smp.getSmpId());
		//List<ScheduleMembershipTimePlan> scheduleMembershipTimePlans=smp.getScheduleMembershipTimePlans();
		
		ProgramFactory pmf=programFactory.findProgramById(smpInDb.getProgId());
		
		if(smpInDb.getSmpFreezeCount()>=pmf.getMaxFreezeCount()){
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
			hmiResultObj.setResultMessage("maxFreeezeCountExceeded");
			return hmiResultObj;
		}
		
		hmiResultObj=scheduleMembershipFacade.canScheduleFreeze(smpInDb, smpInDb, pmf);
		
		
		if(hmiResultObj.getResultStatu()==ResultStatuObj.RESULT_STATU_FAIL){
			return hmiResultObj;
		}else{
		
			int freezeDuration=pmf.getFreezeDuration();
			Date freezeEndDate=new Date();
			Date freezeStartDate=OhbeUtil.getThatDayFormatNotNull(smp.getSmpStartDateStr(), GlobalUtil.global.getPtDateFormat());
			if(pmf.getFreezeDurationType()==ProgDurationTypes.DURATION_TYPE_MONTHLY){
				freezeEndDate=OhbeUtil.getDateForNextMonth(smpInDb.getSmpEndDate(), freezeDuration);
				
			}else{
				if(pmf.getFreezeDurationType()==ProgDurationTypes.DURATION_TYPE_WEEKLY){
					freezeDuration=freezeDuration*7;
				}
				freezeEndDate=OhbeUtil.getDateForNextDate(smpInDb.getSmpEndDate(), freezeDuration);
			}
			
			smpInDb.setSmpEndDate(freezeEndDate);
			smpInDb.setSmpFreezeCount(smpInDb.getSmpFreezeCount()+1);
			scheduleMembershipService.createPlan(smpInDb);
			
			ScheduleMembershipTimePlan scheduleMembershipTimePlan=new ScheduleMembershipTimePlan();
			scheduleMembershipTimePlan.setSmpEndDate(freezeEndDate);
			scheduleMembershipTimePlan.setSmpStartDate(freezeStartDate);
			scheduleMembershipTimePlan.setSmpId(smp.getSmpId());
			scheduleMembershipTimePlan.setSmpComment(smp.getSmpComment());
			
			hmiResultObj=scheduleMembershipService.createScheduleMembershipTimePlan(scheduleMembershipTimePlan);
		}
		return hmiResultObj;
	}
	
	
	@RequestMapping(value="/unFreezeSchedule/{smtpId}/{smpId}", method = RequestMethod.POST) 
	public @ResponseBody HmiResultObj unFreezeSchedule(@PathVariable("smtpId") long smtpId,@PathVariable("smpId") long smpId, HttpServletRequest request) throws UnAuthorizedUserException {
		
		User user=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if((user==null)){
			throw new UnAuthorizedUserException("");
		}
		HmiResultObj hmiResultObj=new HmiResultObj();
		
		
		
		
		
		
		
		
		ScheduleMembershipPlan smp=(ScheduleMembershipPlan)scheduleFactory.findScheduleFactoryPlanById(smpId);
		ScheduleMembershipTimePlan scheduleMembershipTimePlan=scheduleMembershipService.findScheduleTimePlanBySmtpId(smtpId);
		ProgramFactory pmf=programFactory.findProgramById(smp.getProgId());
		
		
		hmiResultObj=scheduleMembershipFacade.canScheduleUnFreeze(smp, scheduleMembershipTimePlan, pmf);
		
		if(hmiResultObj.getResultStatu()==ResultStatuObj.RESULT_STATU_FAIL){
			return hmiResultObj;
		}else{
		
				int freezeDuration=pmf.getFreezeDuration();
				
				if(pmf.getFreezeDurationType()==ProgDurationTypes.DURATION_TYPE_MONTHLY){
					smp.setSmpEndDate(OhbeUtil.getDateForNextMonth(smp.getSmpEndDate(), freezeDuration*-1));
				}else{
					if(pmf.getFreezeDurationType()==ProgDurationTypes.DURATION_TYPE_WEEKLY){
						freezeDuration=freezeDuration*7;
					}
					smp.setSmpEndDate(OhbeUtil.getDateForNextDate(smp.getSmpEndDate(), freezeDuration*-1));
				}
				
				smp.setSmpFreezeCount(smp.getSmpFreezeCount()-1);
				scheduleMembershipService.createPlan(smp);
				
				hmiResultObj=scheduleMembershipService.deleteScheduleMembershipTimePlan(scheduleMembershipTimePlan);
		}
		return hmiResultObj;
	}
	
}
