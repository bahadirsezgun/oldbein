package tr.com.abasus.ptboss.schedule.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import tr.com.abasus.ptboss.exceptions.UnAuthorizedUserException;
import tr.com.abasus.ptboss.schedule.businessEntity.ScheduleSearchObj;
import tr.com.abasus.ptboss.schedule.businessEntity.ScheduleTimeObj;
import tr.com.abasus.ptboss.schedule.businessEntity.StaffClassPlans;
import tr.com.abasus.ptboss.schedule.service.ScheduleClassService;
import tr.com.abasus.ptboss.schedule.service.SchedulePersonalService;
import tr.com.abasus.util.OhbeUtil;
import tr.com.abasus.util.ProgramTypes;

@Controller
@RequestMapping(value="/scheduleStaff")
public class ScheduleStaffController {

	@Autowired
	ScheduleClassService scheduleClassService;
	
	@Autowired
	SchedulePersonalService schedulePersonalService;
	
	
	
	
	@RequestMapping(value="/getSchStaffPlan", method = RequestMethod.POST) 
	public @ResponseBody List<StaffClassPlans> getSchStaffPlanByMonth(@RequestBody ScheduleSearchObj scheduleSearchObj, HttpServletRequest request) throws UnAuthorizedUserException {
	
		String monthStr=""+scheduleSearchObj.getMonth();
		if(scheduleSearchObj.getMonth()<10)
			 monthStr="0"+scheduleSearchObj.getMonth();
		
		String payDateStr="01/"+monthStr+"/"+scheduleSearchObj.getYear()+" 00:00";
		
		Date startDate=OhbeUtil.getThatDayFormatNotNull(payDateStr, "dd/MM/yyyy HH:mm");
		Date endDate=OhbeUtil.getDateForNextMonth(startDate, 1);
		
		List<StaffClassPlans> staffClassPlans=new ArrayList<StaffClassPlans>();
		
		if(scheduleSearchObj.getTypeOfSchedule()==ProgramTypes.PROGRAM_PERSONAL){
			staffClassPlans=schedulePersonalService.findStaffClassPlans(startDate, endDate, scheduleSearchObj.getStaffId());
		}else if(scheduleSearchObj.getTypeOfSchedule()==ProgramTypes.PROGRAM_CLASS){
			staffClassPlans=scheduleClassService.findStaffClassPlans(startDate, endDate, scheduleSearchObj.getStaffId());
		}
		
		return staffClassPlans;
		
	}
	
	@RequestMapping(value="/getSchStaffPlanByDate", method = RequestMethod.POST) 
	public @ResponseBody List<StaffClassPlans> getSchStaffPlanByDate(@RequestBody ScheduleSearchObj scheduleSearchObj, HttpServletRequest request) throws UnAuthorizedUserException {
	
		
		Date startDate=OhbeUtil.getThatDayFormatNotNull(scheduleSearchObj.getStartDateStr(), "dd/MM/yyyy HH:mm");
		Date endDate=OhbeUtil.getThatDayFormatNotNull(scheduleSearchObj.getEndDateStr(), "dd/MM/yyyy HH:mm");
		
		List<StaffClassPlans> staffClassPlans=new ArrayList<StaffClassPlans>();
		
		if(scheduleSearchObj.getTypeOfSchedule()==ProgramTypes.PROGRAM_PERSONAL){
			staffClassPlans=schedulePersonalService.findStaffClassPlans(startDate, endDate, scheduleSearchObj.getStaffId());
		}else if(scheduleSearchObj.getTypeOfSchedule()==ProgramTypes.PROGRAM_CLASS){
			staffClassPlans=scheduleClassService.findStaffClassPlans(startDate, endDate, scheduleSearchObj.getStaffId());
		}
		
		return staffClassPlans;
		
	}
	
	
}
