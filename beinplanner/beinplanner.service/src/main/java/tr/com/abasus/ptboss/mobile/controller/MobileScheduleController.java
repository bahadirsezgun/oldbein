package tr.com.abasus.ptboss.mobile.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import tr.com.abasus.ptboss.exceptions.UnAuthorizedUserException;
import tr.com.abasus.ptboss.facade.schedule.ScheduleClassFacade;
import tr.com.abasus.ptboss.facade.schedule.ScheduleFacadeService;
import tr.com.abasus.ptboss.facade.schedule.SchedulePersonalFacade;
import tr.com.abasus.ptboss.facade.scheduleplan.SchedulePlanFacadeService;
import tr.com.abasus.ptboss.packetsale.entity.PacketSaleClass;
import tr.com.abasus.ptboss.packetsale.entity.PacketSalePersonal;
import tr.com.abasus.ptboss.program.entity.ProgramClass;
import tr.com.abasus.ptboss.program.entity.ProgramInterface;
import tr.com.abasus.ptboss.program.entity.ProgramPersonal;
import tr.com.abasus.ptboss.ptuser.entity.User;
import tr.com.abasus.ptboss.ptuser.service.ProcessUserService;
import tr.com.abasus.ptboss.schedule.businessEntity.ScheduleTimeObj;
import tr.com.abasus.ptboss.schedule.businessService.ScheduleBusinessCalendarService;
import tr.com.abasus.ptboss.schedule.entity.ScheduleInterface;
import tr.com.abasus.ptboss.schedule.entity.ScheduleMembershipPlan;
import tr.com.abasus.ptboss.schedule.entity.ScheduleUsersClassPlan;
import tr.com.abasus.ptboss.schedule.entity.ScheduleUsersPersonalPlan;
import tr.com.abasus.ptboss.schedule.service.ScheduleService;
import tr.com.abasus.util.DateTimeUtil;
import tr.com.abasus.util.GlobalUtil;
import tr.com.abasus.util.SessionUtil;
import tr.com.abasus.util.UserTypes;

@Controller
@RequestMapping(value="/mobile/schedule")
public class MobileScheduleController {

	@Autowired
	ScheduleService scheduleService;
	
	@Autowired 
	ProcessUserService processUserService;
	
	@Autowired
	ScheduleMembershipPlan scheduleMembershipPlan;
	
	@Autowired
	ScheduleUsersClassPlan scheduleUsersClassPlan;
	
	@Autowired
	ScheduleUsersPersonalPlan scheduleUsersPersonalPlan;
	
	ScheduleInterface scheduleInterface;
	
	ScheduleFacadeService scheduleFacadeService;
	
	@Autowired
	ScheduleClassFacade scheduleClassFacade;
	
	@Autowired
	SchedulePersonalFacade schedulePersonalFacade;
	
	
	@Autowired
	SchedulePlanFacadeService schedulePlanFacadeService;
	
	@Autowired
	ProgramClass programClass;
	
	@Autowired
	ProgramPersonal programPersonal;
	
	ProgramInterface programInterface;
	
	
	@Autowired
	PacketSalePersonal packetSalePersonal;
	
	@Autowired
	PacketSaleClass packetSaleClass;
	
	
	@Autowired
	ScheduleBusinessCalendarService scheduleBusinessCalendarService;
	
	
	@RequestMapping(value="/getCurrentTime/{userName}/{password}", method = RequestMethod.POST) 
	public @ResponseBody ScheduleTimeObj getCurrentTime(@PathVariable String userName,@PathVariable String password,HttpServletRequest request) throws UnAuthorizedUserException {
	
		User user=processUserService.loginUserControl(userName, password);
		if(user==null){
			throw new UnAuthorizedUserException("");
    	}else if((user.getUserType()==UserTypes.USER_TYPE_MEMBER_INT)){
			throw new UnAuthorizedUserException("");
		}
		
		ScheduleTimeObj scheduleTimeObj=new ScheduleTimeObj();
		
		Date date=new Date();
		String progDayName=DateTimeUtil.getDayNames(date);
		String currentDate=DateTimeUtil.getDateStrByFormat(date, GlobalUtil.global.getPtScrDateFormat());
		
		scheduleTimeObj.setProgDayName(progDayName);
		scheduleTimeObj.setProgStartTime(currentDate);
		
		return scheduleTimeObj;
	}
	
}
