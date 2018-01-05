package tr.com.abasus.ptboss.mobile.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import tr.com.abasus.ptboss.exceptions.UnAuthorizedUserException;
import tr.com.abasus.ptboss.ptuser.entity.User;
import tr.com.abasus.ptboss.ptuser.iuser.ProcessSchedulerStaff;
import tr.com.abasus.ptboss.ptuser.service.ProcessUserService;
import tr.com.abasus.ptboss.schedule.businessService.ScheduleBusinessCalendarTimesService;
import tr.com.abasus.util.UserTypes;

@Controller
@RequestMapping("/mobile/util")
public class MobileUtilController {

	@Autowired
	ProcessSchedulerStaff processSchedulerStaff;
	
	@Autowired 
	ProcessUserService processUserService;
	
	@Autowired
	ScheduleBusinessCalendarTimesService scheduleBusinessCalendarTimesService;
	
	
	@RequestMapping(value="/findAllSchStaff/{userEmail}/{userPassword}", method = RequestMethod.POST) 
	public @ResponseBody List<User> findAll(@PathVariable String userEmail,@PathVariable String userPassword) throws UnAuthorizedUserException{
		User user=processUserService.loginUserControl(userEmail, userPassword);
		if(user==null){
			throw new UnAuthorizedUserException("");
    	}else if((user.getUserType()==UserTypes.USER_TYPE_MEMBER_INT)){
			throw new UnAuthorizedUserException("");
		}
		
		List<User> users=processUserService.findAllToSchedulerStaffForCalendar(user.getFirmId());
		for (User user2 : users) {
			user2.setStaffName(user2.getUserName()+" "+user2.getUserSurname());
		}
		
		return users;
   	}
	
	@RequestMapping(value="/getAllDayTimes/{userEmail}/{userPassword}", method = RequestMethod.POST) 
	public @ResponseBody List<String> getAllDayTimes(@PathVariable String userEmail,@PathVariable String userPassword) throws UnAuthorizedUserException {
		User user=processUserService.loginUserControl(userEmail, userPassword);
		if(user==null){
			throw new UnAuthorizedUserException("");
    	}else if((user.getUserType()==UserTypes.USER_TYPE_MEMBER_INT)){
			throw new UnAuthorizedUserException("");
		}
		return scheduleBusinessCalendarTimesService.getTimesForAllDay();
	}
	
}
