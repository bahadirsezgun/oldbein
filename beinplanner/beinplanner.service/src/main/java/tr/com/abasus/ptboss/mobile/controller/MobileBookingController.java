package tr.com.abasus.ptboss.mobile.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import tr.com.abasus.ptboss.exceptions.UnAuthorizedUserException;
import tr.com.abasus.ptboss.facade.schedule.ScheduleClassFacade;
import tr.com.abasus.ptboss.facade.schedule.ScheduleFacadeService;
import tr.com.abasus.ptboss.facade.schedule.SchedulePersonalFacade;
import tr.com.abasus.ptboss.facade.scheduleplan.SchedulePlanFacadeService;
import tr.com.abasus.ptboss.packetsale.entity.PacketSaleClass;
import tr.com.abasus.ptboss.packetsale.entity.PacketSaleFactory;
import tr.com.abasus.ptboss.packetsale.entity.PacketSaleInterface;
import tr.com.abasus.ptboss.packetsale.entity.PacketSalePersonal;
import tr.com.abasus.ptboss.program.entity.ProgramClass;
import tr.com.abasus.ptboss.program.entity.ProgramFactory;
import tr.com.abasus.ptboss.program.entity.ProgramPersonal;
import tr.com.abasus.ptboss.ptuser.entity.User;
import tr.com.abasus.ptboss.ptuser.iuser.ProcessMember;
import tr.com.abasus.ptboss.ptuser.service.ProcessUserService;
import tr.com.abasus.ptboss.result.entity.HmiResultObj;
import tr.com.abasus.ptboss.schedule.businessEntity.ScheduleCalendarObj;
import tr.com.abasus.ptboss.schedule.businessEntity.ScheduleObj;
import tr.com.abasus.ptboss.schedule.businessEntity.ScheduleTimeObj;
import tr.com.abasus.ptboss.schedule.entity.ScheduleFactory;
import tr.com.abasus.ptboss.schedule.entity.ScheduleInterface;
import tr.com.abasus.ptboss.schedule.entity.ScheduleMembershipPlan;
import tr.com.abasus.ptboss.schedule.entity.SchedulePlan;
import tr.com.abasus.ptboss.schedule.entity.ScheduleStudios;
import tr.com.abasus.ptboss.schedule.entity.ScheduleTimePlan;
import tr.com.abasus.ptboss.schedule.entity.ScheduleUsersClassPlan;
import tr.com.abasus.ptboss.schedule.entity.ScheduleUsersPersonalPlan;
import tr.com.abasus.ptboss.schedule.service.ScheduleClassService;
import tr.com.abasus.ptboss.schedule.service.SchedulePersonalService;
import tr.com.abasus.ptboss.schedule.service.ScheduleService;
import tr.com.abasus.util.DateTimeUtil;
import tr.com.abasus.util.GlobalUtil;
import tr.com.abasus.util.OhbeUtil;
import tr.com.abasus.util.ProgramTypes;
import tr.com.abasus.util.ResultStatuObj;
import tr.com.abasus.util.RuleUtil;
import tr.com.abasus.util.SaleStatus;
import tr.com.abasus.util.SessionUtil;
import tr.com.abasus.util.StatuTypes;
import tr.com.abasus.util.TimeTypes;
import tr.com.abasus.util.UserTypes;

@Controller
@RequestMapping(value="/mobile/booking")
public class MobileBookingController {

	@Autowired
	ProcessMember processMember;
	
	@Autowired 
	ProcessUserService processUserService;
	
	@Autowired
	ScheduleService scheduleService;
	
	@Autowired
	SchedulePlanFacadeService schedulePlanFacadeService;
	
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
	SchedulePersonalService schedulePersonalService;
	
	@Autowired
	ScheduleClassService scheduleClassService;
	
	@Autowired
	ProgramClass programClass;
	
	@Autowired
	ProgramPersonal programPersonal;
	
	
	@Autowired
	PacketSalePersonal packetSalePersonal;
	
	@Autowired
	PacketSaleClass packetSaleClass;
	
	
	@RequestMapping(value="/findByUsersInSchedulePlan/{schId}/{progType}/{userName}/{password}", method = RequestMethod.POST) 
	public @ResponseBody List<User> findByUsersInSchedulePlan(@PathVariable long schId,@PathVariable int progType,@PathVariable String userName,@PathVariable String password,HttpServletRequest request) throws UnAuthorizedUserException{
	
		User sessionUser=processUserService.loginUserControl(userName, password);
		if(sessionUser==null){
			throw new UnAuthorizedUserException("");
    	}else if((sessionUser.getUserType()==UserTypes.USER_TYPE_MEMBER_INT)){
			throw new UnAuthorizedUserException("");
		}
		
		List<User> users=null;
		
		if(progType==ProgramTypes.PROGRAM_CLASS){
			users=scheduleClassService.findUsersInClassPlanToGroup(schId);
		}else if(progType==ProgramTypes.PROGRAM_PERSONAL){
			users=schedulePersonalService.findUsersInPersonalPlanToGroup(schId);
		}
		
		return users;
		
	}
	
	@RequestMapping(value="/findByUserNameForSales/{progType}/{userName}/{password}", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE) 
	public @ResponseBody List<User> findByUserNameForSales(@RequestBody User user,@PathVariable String userName,@PathVariable String password,@PathVariable int progType,HttpServletRequest request) throws UnAuthorizedUserException{
	
		
		
		User sessionUser=processUserService.loginUserControl(userName, password);
		if(sessionUser==null){
			throw new UnAuthorizedUserException("");
    	}else if((sessionUser.getUserType()==UserTypes.USER_TYPE_MEMBER_INT)){
			throw new UnAuthorizedUserException("");
		}
		
		List<User> users=null;
		List<User> usersHasNoSale=null;
		List<User> usersNew=new ArrayList<User>();
		
		System.out.println("Username :"+user.getUserName()+"%  ProgType "+progType+"%  USER TYPE "+user.getUserType());
		
		if(user.getUserType()==UserTypes.USER_TYPE_MEMBER_INT){
  			users= processMember.findByUserNameForSales(user.getUserName()+"%", user.getUserSurname()+"%",progType,sessionUser.getFirmId());
  			
  			////System.out.println("GlobalUtil.rules.getRuleNoSaleToPlanning() "+GlobalUtil.rules.getRuleNoSaleToPlanning());
  			
  			if(GlobalUtil.rules.getRuleNoSaleToPlanning()==RuleUtil.RULE_OK){
  			usersHasNoSale=processMember.findByNameAndSurname(user.getUserName()+"%", user.getUserSurname()+"%", sessionUser.getFirmId());
			
  			
  			for (User userL : usersHasNoSale) {
				boolean found=false;
				if(progType==ProgramTypes.PROGRAM_CLASS){
					userL.setType("sucp");
				}else if(progType==ProgramTypes.PROGRAM_PERSONAL){
					userL.setType("supp");
				}
				for (User userHNS : users) {
						if(userHNS.getUserId()==userL.getUserId()){
							found=true;
							break;
						}
				}
				
				if(!found){
					usersNew.add(userL);
				}
				
			}
  			
  			users.addAll(usersNew);
  			}
   		}
		System.out.println("USER SIZE "+users.size());
		for (User user2 : users) {
			if(user2.getUserGender()==0){
				user2.setProfileUrl("male.png");
			}else{
				user2.setProfileUrl("female.png");
			}
			if(user2.getSaleStatu()==SaleStatus.SALE_HAS_PLANNED){
				
				
				if(progType==ProgramTypes.PROGRAM_CLASS){
					SchedulePlan schedulePlan=scheduleClassService.findSchedulePlanBySaleId(user2.getSaleId());
					if(schedulePlan!=null){
						user2.setSchId(schedulePlan.getSchId());
					}else{
						user2.setSchId(0);
					}
				}else if(progType==ProgramTypes.PROGRAM_PERSONAL){
					SchedulePlan schedulePlan=schedulePersonalService.findSchedulePlanBySaleId(user2.getSaleId());
					System.out.println("user2.getSaleId() "+user2.getSaleId()+"  user2.getSaleStatu():"+user2.getSaleStatu());
					
					if(schedulePlan!=null){
					user2.setSchId(schedulePlan.getSchId());
					}else{
						user2.setSchId(0);
					}
				}
				
				
			}
			
		}
		
		return users;
	}
	
	
	@RequestMapping(value="/findByUserNameForPlans/{progType}/{userName}/{password}", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE) 
	public @ResponseBody List<User> findByUserNameForPlans(@RequestBody User user,@PathVariable String userName,@PathVariable String password,@PathVariable int progType,HttpServletRequest request) throws UnAuthorizedUserException{
	
		
		
		User sessionUser=processUserService.loginUserControl(userName, password);
		if(sessionUser==null){
			throw new UnAuthorizedUserException("");
    	}else if((sessionUser.getUserType()==UserTypes.USER_TYPE_MEMBER_INT)){
			throw new UnAuthorizedUserException("");
		}
		
		List<User> users=null;
		List<User> usersResult=new ArrayList<User>();
		
		System.out.println("Username :"+user.getUserName()+"%  ProgType "+progType+"%  USER TYPE "+user.getUserType());
		
		if(user.getUserType()==UserTypes.USER_TYPE_MEMBER_INT){
  			users= processMember.findByUserNameForSales(user.getUserName()+"%", user.getUserSurname()+"%",progType,sessionUser.getFirmId());
  			
   		}
		
		if(progType==ProgramTypes.PROGRAM_CLASS){
			scheduleInterface=scheduleUsersClassPlan;
		}else if(progType==ProgramTypes.PROGRAM_PERSONAL){
			scheduleInterface=scheduleUsersPersonalPlan;
		}
		
		
		for (User user2 : users) {
			SchedulePlan schedulePlan= scheduleInterface.findSchedulePlanBySaleId(user2.getSaleId());
			if(schedulePlan!=null){
				user2.setSchId(schedulePlan.getSchId());
			    if(user2.getUserGender()==0){
					user2.setProfileUrl("male.png");
				}else{
					user2.setProfileUrl("female.png");
				}
		
			    usersResult.add(user2);
			}
		}
		
		return usersResult;
	}
	
	@RequestMapping(value="/findByUserNameForSalesForProgram/{progType}/{progId}/{schId}/{userName}/{password}", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE) 
	public @ResponseBody List<User> findByUserNameForSalesForProgram(@RequestBody User user,@PathVariable long schId,@PathVariable String userName,@PathVariable String password,@PathVariable int progType,@PathVariable long progId,HttpServletRequest request) throws UnAuthorizedUserException{
	
		
		
		User sessionUser=processUserService.loginUserControl(userName, password);
		if(sessionUser==null){
			throw new UnAuthorizedUserException("");
    	}else if((sessionUser.getUserType()==UserTypes.USER_TYPE_MEMBER_INT)){
			throw new UnAuthorizedUserException("");
		}
		
		List<User> users=null;
		List<User> usersHasNoSale=null;
		List<User> usersNew=new ArrayList<User>();
		
		System.out.println("Username :"+user.getUserName()+"%  ProgType "+progType+"%  USER TYPE "+user.getUserType());
		
		if(user.getUserType()==UserTypes.USER_TYPE_MEMBER_INT){
  			users= processMember.findByNameAndSaleProgramWithNoPlan(user.getUserName()+"%", user.getUserSurname()+"%", progId, progType, sessionUser.getFirmId(), schId);
  			
  			////System.out.println("GlobalUtil.rules.getRuleNoSaleToPlanning() "+GlobalUtil.rules.getRuleNoSaleToPlanning());
  			
  			if(GlobalUtil.rules.getRuleNoSaleToPlanning()==RuleUtil.RULE_OK){
  			usersHasNoSale=processMember.findByNameAndSurname(user.getUserName()+"%", user.getUserSurname()+"%", sessionUser.getFirmId());
			
  			
  			for (User userL : usersHasNoSale) {
				boolean found=false;
				if(progType==ProgramTypes.PROGRAM_CLASS){
					userL.setType("sucp");
				}else if(progType==ProgramTypes.PROGRAM_PERSONAL){
					userL.setType("supp");
				}
				for (User userHNS : users) {
						if(userHNS.getUserId()==userL.getUserId()){
							found=true;
							break;
						}
				}
				
				if(!found){
					usersNew.add(userL);
				}
				
			}
  			
  			users.addAll(usersNew);
  			}
   		}
		for (User user2 : users) {
			if(user2.getUserGender()==0){
				user2.setProfileUrl("male.png");
			}else{
				user2.setProfileUrl("female.png");
			}
			if(user2.getSaleStatu()==SaleStatus.SALE_HAS_PLANNED){
				
				
				if(progType==ProgramTypes.PROGRAM_CLASS){
					SchedulePlan schedulePlan=scheduleClassService.findSchedulePlanBySaleId(user2.getSaleId());
					user2.setSchId(schedulePlan.getSchId());
					
				}else if(progType==ProgramTypes.PROGRAM_PERSONAL){
					SchedulePlan schedulePlan=schedulePersonalService.findSchedulePlanBySaleId(user2.getSaleId());
					user2.setSchId(schedulePlan.getSchId());
					
				}
				
				
			}
			
		}
		
		return users;
	}
	
	
	
	@RequestMapping(value="/findSchedulePlanById/{schId}/{type}/{userName}/{password}", method = RequestMethod.POST) 
	public @ResponseBody SchedulePlan findSchedulePlanById(@PathVariable("schId") long schId,@PathVariable("type") int type,@PathVariable String userName,@PathVariable String password, HttpServletRequest request) throws UnAuthorizedUserException {
		
		User sessionUser=processUserService.loginUserControl(userName, password);
		if(sessionUser==null){
			throw new UnAuthorizedUserException("");
    	}else if((sessionUser.getUserType()==UserTypes.USER_TYPE_MEMBER_INT)){
			throw new UnAuthorizedUserException("");
		}
		
		if(type==ProgramTypes.PROGRAM_CLASS){
			scheduleInterface=scheduleUsersClassPlan;
		}else if(type==ProgramTypes.PROGRAM_PERSONAL){
			scheduleInterface=scheduleUsersPersonalPlan;
		}
		
		SchedulePlan schedulePlan=scheduleService.findSchedulePlanByPlanId(schId);
		if(schedulePlan!=null){
			User staffMain=processUserService.findById(schedulePlan.getSchStaffId());
			staffMain.setPassword(null);
			schedulePlan.setStaff(staffMain);
			
		List<ScheduleTimePlan> scheduleTimePlans=scheduleService.findScheduleTimePlanByPlanId(schedulePlan.getSchId());
		
		schedulePlan.setFinishedPlan(schedulePlanFacadeService.schedulePlanCompleted(schedulePlan, scheduleTimePlans.size()));
		
		
		
		int i=1;
		for (ScheduleTimePlan scheduleTimePlan : scheduleTimePlans) {
			
			
			/**LAST PLAN FOUND**/
			int lastPlan=scheduleService.isScheduleTimeLast(schedulePlan, scheduleTimePlan, scheduleTimePlans);
			scheduleTimePlan.setLastPlan(lastPlan);
			
			
			List<ScheduleStudios> scheduleStudios=scheduleService.findScheduleStudiosByTimePlanId(scheduleTimePlan.getSchtId());
			List<ScheduleFactory> scheduleFactories=scheduleInterface.findSchedulesTimesBySchtId(scheduleTimePlan.getSchtId());
			User staff=processUserService.findById(scheduleTimePlan.getSchtStaffId());
			staff.setPassword(null);
			
			scheduleTimePlan.setStaff(staff);
			scheduleTimePlan.setPlanCount(i++);
			scheduleTimePlan.setPlanStatus(ResultStatuObj.RESULT_STATU_SUCCESS); 
	    	scheduleTimePlan.setPlanStatusComment(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
	    	
	    	scheduleTimePlan.setPlanDayName(DateTimeUtil.getDayNames(scheduleTimePlan.getPlanStartDate()));
	    	scheduleTimePlan.setPlanDayTime(DateTimeUtil.getHourMinute(scheduleTimePlan.getPlanStartDate()));
	    	scheduleTimePlan.setPlanEndDayTime(DateTimeUtil.getHourMinute(scheduleTimePlan.getPlanEndDate()));
	    	
			scheduleTimePlan.setScheduleStudios(scheduleStudios);
			scheduleTimePlan.setUsers(scheduleFactories);
			String participants="";
			for (ScheduleFactory scheduleFactory : scheduleFactories) {
				participants+=scheduleFactory.getUserName()+" "+scheduleFactory.getUserSurname()+",";
			}
			
			scheduleTimePlan.setParticipants(participants);
			scheduleTimePlan.setStaffName(staff.getUserName()+" "+staff.getUserSurname());
		}
		
		
		schedulePlan.setScheduleTimePlans(scheduleTimePlans);
		}
		
		
		return  getProgramsCounts(schedulePlan);
	}
	
	@RequestMapping(value="/findSchedulePlanBySaleId/{saleId}/{type}/{userName}/{password}", method = RequestMethod.POST) 
	public @ResponseBody SchedulePlan findSchedulePlanBySaleId(@PathVariable("saleId") long saleId,@PathVariable("type") int type,@PathVariable String userName,@PathVariable String password, HttpServletRequest request) throws UnAuthorizedUserException {
		
		User sessionUser=processUserService.loginUserControl(userName, password);
		if(sessionUser==null){
			throw new UnAuthorizedUserException("");
    	}else if((sessionUser.getUserType()==UserTypes.USER_TYPE_MEMBER_INT)){
			throw new UnAuthorizedUserException("");
		}
		
		
		if(type==ProgramTypes.PROGRAM_CLASS){
			scheduleInterface=scheduleUsersClassPlan;
		}else if(type==ProgramTypes.PROGRAM_PERSONAL){
			scheduleInterface=scheduleUsersPersonalPlan;
		}
		
		SchedulePlan schedulePlan=scheduleInterface.findSchedulePlanBySaleId(saleId);
		if(schedulePlan!=null){
			User staffMain=processUserService.findById(schedulePlan.getSchStaffId());
			staffMain.setPassword(null);
			schedulePlan.setStaff(staffMain);
			
		List<ScheduleTimePlan> scheduleTimePlans=scheduleService.findScheduleTimePlanByPlanId(schedulePlan.getSchId());
		
		schedulePlan.setFinishedPlan(schedulePlanFacadeService.schedulePlanCompleted(schedulePlan, scheduleTimePlans.size()));
		
		
		
		int i=1;
		for (ScheduleTimePlan scheduleTimePlan : scheduleTimePlans) {
			
			
			/**LAST PLAN FOUND**/
			int lastPlan=scheduleService.isScheduleTimeLast(schedulePlan, scheduleTimePlan, scheduleTimePlans);
			scheduleTimePlan.setLastPlan(lastPlan);
			
			
			List<ScheduleStudios> scheduleStudios=scheduleService.findScheduleStudiosByTimePlanId(scheduleTimePlan.getSchtId());
			List<ScheduleFactory> scheduleFactories=scheduleInterface.findSchedulesTimesBySchtId(scheduleTimePlan.getSchtId());
			User staff=processUserService.findById(scheduleTimePlan.getSchtStaffId());
			staff.setPassword(null);
			
			scheduleTimePlan.setStaff(staff);
			scheduleTimePlan.setPlanCount(i++);
			scheduleTimePlan.setPlanStatus(ResultStatuObj.RESULT_STATU_SUCCESS); 
			scheduleTimePlan.setPlanStatusComment(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
	    	
	    	scheduleTimePlan.setPlanDayName(DateTimeUtil.getDayNames(scheduleTimePlan.getPlanStartDate()));
	    	scheduleTimePlan.setPlanDayTime(DateTimeUtil.getHourMinute(scheduleTimePlan.getPlanStartDate()));
	    	scheduleTimePlan.setPlanEndDayTime(DateTimeUtil.getHourMinute(scheduleTimePlan.getPlanEndDate()));
	    	
			scheduleTimePlan.setScheduleStudios(scheduleStudios);
			scheduleTimePlan.setUsers(scheduleFactories);
		}
		
		
		schedulePlan.setScheduleTimePlans(scheduleTimePlans);
		}
		
		return getProgramsCounts(schedulePlan);
	}
	
	private SchedulePlan getProgramsCounts(SchedulePlan schedulePlan){
		
		if(schedulePlan!=null){
			List<ScheduleTimePlan> scheduleTimePlans=schedulePlan.getScheduleTimePlans();
			int programCount=schedulePlan.getSchCount();
			int plannedCount=0;
			for (ScheduleTimePlan scheduleTimePlan : scheduleTimePlans) {
				if(scheduleTimePlan.getPlanStartDate().before(new Date())){
					plannedCount++;
				}
			}
			
			int willPlanCount=programCount-plannedCount;
			
			schedulePlan.setProgramCount(programCount);
			schedulePlan.setPlannedCount(plannedCount);
			schedulePlan.setWillPlanCount(willPlanCount);
		}
	
	
	return schedulePlan;
    }
	
	
	
	
	@RequestMapping(value="/searchTimePlans/{userName}/{password}", method = RequestMethod.POST) 
	public @ResponseBody List<ScheduleTimePlan> searchTimePlans(@RequestBody ScheduleCalendarObj scheduleCalendarObj,@PathVariable String userName,@PathVariable String password) throws UnAuthorizedUserException {
		User sessionUser=processUserService.loginUserControl(userName, password);
		if(sessionUser==null){
			throw new UnAuthorizedUserException("");
    	}else if((sessionUser.getUserType()==UserTypes.USER_TYPE_MEMBER_INT)){
			throw new UnAuthorizedUserException("");
		}
		
		Date startDate=OhbeUtil.getThatDateForNight(DateTimeUtil.convertMobileDateToSystemDate(scheduleCalendarObj.getCalendarDate()) , GlobalUtil.global.getPtScrDateFormat());
		startDate=OhbeUtil.getDateForNextDate(startDate, scheduleCalendarObj.getDay());
		
		Date endDate=OhbeUtil.getThatDateForNight(DateTimeUtil.convertMobileDateToSystemDate(scheduleCalendarObj.getCalendarDate()), GlobalUtil.global.getPtScrDateFormat());
		endDate=OhbeUtil.getDateForNextDate(endDate, scheduleCalendarObj.getDay());
		endDate=OhbeUtil.getDateForNextDate(endDate, 1);
		
		scheduleCalendarObj.setCalendarDate(OhbeUtil.getDateStrByFormat(startDate, GlobalUtil.global.getPtDbDateFormat()));
		scheduleCalendarObj.setCalendarDateName(DateTimeUtil.getDayNames(startDate));
		
		List<ScheduleTimePlan> scheduleTimePlans=null;
		
		if(scheduleCalendarObj.getStaffId()==0 ){
			scheduleTimePlans=scheduleService.findScheduleTimePlanByDates(startDate, endDate);
		}else{
			scheduleTimePlans=scheduleService.findScheduleTimePlanByStaffAndDate(startDate, endDate, scheduleCalendarObj.getStaffId());
		}
		
		
		int i=1;
		for (ScheduleTimePlan scheduleTimePlan : scheduleTimePlans) {
			
			/**LAST PLAN FOUND**/
			int lastPlan=scheduleService.isScheduleTimeLast(null, scheduleTimePlan, null);
			scheduleTimePlan.setLastPlan(lastPlan);
			
			
			ProgramFactory programFactory=null;
			if(scheduleTimePlan.getProgType()==ProgramTypes.PROGRAM_CLASS){
		    	scheduleInterface=scheduleUsersClassPlan;
		    	programFactory=programClass;
		    }else  if(scheduleTimePlan.getProgType()==ProgramTypes.PROGRAM_PERSONAL){
		    	scheduleInterface=scheduleUsersPersonalPlan;
		    	programFactory=programPersonal;
		    }
			
			ProgramFactory pf= programFactory.findProgramById(scheduleTimePlan.getProgId());
			scheduleTimePlan.setProgName(pf.getProgName());
			scheduleTimePlan.setProgId(pf.getProgId());
			
			
			
			List<ScheduleStudios> scheduleStudios=scheduleService.findScheduleStudiosByTimePlanId(scheduleTimePlan.getSchtId());
			List<ScheduleFactory> scheduleFactories=scheduleInterface.findSchedulesTimesBySchtId(scheduleTimePlan.getSchtId());
			User staff=processUserService.findById(scheduleTimePlan.getSchtStaffId());
			staff.setPassword(null);
			
			scheduleTimePlan.setStaff(staff);
			scheduleTimePlan.setPlanCount(i++);
			scheduleTimePlan.setPlanStatus(ResultStatuObj.RESULT_STATU_SUCCESS); 
			scheduleTimePlan.setPlanStatusComment(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
	    	
	    	scheduleTimePlan.setPlanDayName(DateTimeUtil.getDayNames(scheduleTimePlan.getPlanStartDate()));
	    	scheduleTimePlan.setPlanDayTime(DateTimeUtil.getHourMinute(scheduleTimePlan.getPlanStartDate()));
	    	
			scheduleTimePlan.setScheduleStudios(scheduleStudios);
			scheduleTimePlan.setUsers(scheduleFactories);
			
			String participants="";
			for (ScheduleFactory scheduleFactory : scheduleFactories) {
				participants+=scheduleFactory.getUserName()+" "+scheduleFactory.getUserSurname()+",";
			}
			scheduleTimePlan.setParticipants(participants);
			scheduleTimePlan.setStaffName(staff.getUserName()+" "+staff.getUserSurname());
			
		}
		
		
		return scheduleTimePlans;
	}
	
	
	@RequestMapping(value="/createSchedule/{type}/{userName}/{password}", method = RequestMethod.POST) 
	public @ResponseBody HmiResultObj createSchedule(@RequestBody SchedulePlan schedulePlan ,@PathVariable("type") String type,@PathVariable String userName,@PathVariable String password, HttpServletRequest request) throws UnAuthorizedUserException {
	
		ScheduleObj scheduleObj=new ScheduleObj();
		
		
		User sessionUser=processUserService.loginUserControl(userName, password);
		if(sessionUser==null){
			throw new UnAuthorizedUserException("");
    	}else if((sessionUser.getUserType()==UserTypes.USER_TYPE_MEMBER_INT)){
			throw new UnAuthorizedUserException("");
		}
		
		if(type.equals(ProgramTypes.SCHEDULE_TYPE_PERSONAL)){
			scheduleInterface=scheduleUsersPersonalPlan;
		}else if(type.equals(ProgramTypes.SCHEDULE_TYPE_CLASS)){
			scheduleInterface=scheduleUsersClassPlan;
		}
		scheduleObj.setFirmId(sessionUser.getFirmId());
		scheduleObj.setTpComment(schedulePlan.getScheduleTimePlans().get(0).getTpComment());
		
		
		
		scheduleObj.setSchedulePlan(schedulePlan);
		
		scheduleObj.setPlanStartDateStr(DateTimeUtil.convertMobileDateToSystemDate(schedulePlan.getScheduleTimePlans().get(0).getPlanStartDateStr()));
		scheduleObj.setTimeType(TimeTypes.TIME_TYPE_NO_PERIOD);
		
		scheduleObj.setScheduleTimeObjs(new ArrayList<ScheduleTimeObj>());
		ScheduleTimeObj scheduleTimeObj=new ScheduleTimeObj();
		scheduleTimeObj.setProgStartTime(schedulePlan.getScheduleTimePlans().get(0).getPlanDayTime());
		scheduleObj.getScheduleTimeObjs().add(scheduleTimeObj);
		scheduleObj.setUsers(new ArrayList<User>());
		for (ScheduleFactory scheduleFactory : schedulePlan.getScheduleTimePlans().get(0).getUsers()) {
			User user=new User();
			user.setSaleId(scheduleFactory.getSaleId());
			user.setUserName(scheduleFactory.getUserName());
			user.setUserSurname(scheduleFactory.getUserSurname());
			user.setFirmId(sessionUser.getFirmId());
			user.setSaleCount(scheduleFactory.getSaleCount());
			user.setUserId(scheduleFactory.getUserId());
			scheduleObj.getUsers().add(user);
		}
		
		
		
		ScheduleObj resultScheduleObj=null;
		if(schedulePlan.getSchId()!=0){
			resultScheduleObj=scheduleInterface.continueSchedule(scheduleObj);
		}else{
			resultScheduleObj=scheduleInterface.createSchedule(scheduleObj);
		}
		
		HmiResultObj hmiResultObj=new HmiResultObj();
		
		if(resultScheduleObj==null){
			hmiResultObj.setResultMessage(ResultStatuObj.RESULT_STATU_FAIL_STR);
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
		}else{
			
			ScheduleTimePlan reScheduleTimePlan=resultScheduleObj.getScheduleTimePlans().get(0);
				hmiResultObj.setResultStatu(reScheduleTimePlan.getPlanStatus());
				if(reScheduleTimePlan.getPlanStatus()==ResultStatuObj.RESULT_STATU_FAIL)
					hmiResultObj.setResultMessage(reScheduleTimePlan.getPlanStatusComment());
				else
					hmiResultObj.setResultMessage(""+resultScheduleObj.getSchedulePlan().getSchId());
					
				
			
			
		}
		
		
		return hmiResultObj;
	}
	
	
	@RequestMapping(value="/updateSchedule/{type}/{userName}/{password}", method = RequestMethod.POST) 
	public @ResponseBody HmiResultObj updateSchedule(@RequestBody SchedulePlan schedulePlan  ,@PathVariable("type") String type,@PathVariable String userName,@PathVariable String password, HttpServletRequest request,HttpServletResponse response) throws UnAuthorizedUserException, IOException, ServletException {
	
		User sessionUser=processUserService.loginUserControl(userName, password);
		if(sessionUser==null){
			throw new UnAuthorizedUserException("");
    	}else if((sessionUser.getUserType()==UserTypes.USER_TYPE_MEMBER_INT)){
			throw new UnAuthorizedUserException("");
		}
		
		if(type.equals(ProgramTypes.SCHEDULE_TYPE_PERSONAL)){
			scheduleInterface=scheduleUsersPersonalPlan;
		}else if(type.equals(ProgramTypes.SCHEDULE_TYPE_CLASS)){
			scheduleInterface=scheduleUsersClassPlan;
		}
		
		ScheduleObj scheduleObj=new ScheduleObj();
		
		scheduleObj.setFirmId(sessionUser.getFirmId());
		scheduleObj.setTpComment(schedulePlan.getScheduleTimePlans().get(0).getTpComment());
		
		
		User staff=processUserService.findById(schedulePlan.getScheduleTimePlans().get(0).getSchtStaffId());
		schedulePlan.getScheduleTimePlans().get(0).setStaff(staff);
		
		scheduleObj.setSchedulePlan(schedulePlan);
		
		scheduleObj.setPlanStartDateStr(DateTimeUtil.convertMobileDateToSystemDate(schedulePlan.getScheduleTimePlans().get(0).getPlanStartDateStr()));
		scheduleObj.setTimeType(TimeTypes.TIME_TYPE_NO_PERIOD);
		
		scheduleObj.setPlanStartDateTime(schedulePlan.getScheduleTimePlans().get(0).getPlanDayTime());
		
		scheduleObj.setScheduleTimeObjs(new ArrayList<ScheduleTimeObj>());
		ScheduleTimeObj scheduleTimeObj=new ScheduleTimeObj();
		scheduleTimeObj.setProgStartTime(schedulePlan.getScheduleTimePlans().get(0).getPlanDayTime());
		scheduleObj.getScheduleTimeObjs().add(scheduleTimeObj);
		scheduleObj.setUsers(new ArrayList<User>());
		for (ScheduleFactory scheduleFactory : schedulePlan.getScheduleTimePlans().get(0).getUsers()) {
			User user=new User();
			user.setSaleId(scheduleFactory.getSaleId());
			user.setUserName(scheduleFactory.getUserName());
			user.setUserSurname(scheduleFactory.getUserSurname());
			user.setFirmId(sessionUser.getFirmId());
			user.setSaleCount(scheduleFactory.getSaleCount());
			user.setUserId(scheduleFactory.getUserId());
			scheduleObj.getUsers().add(user);
		}
		
		scheduleObj.setScheduleTimePlanForUpdate(schedulePlan.getScheduleTimePlans().get(0));
		
		HmiResultObj hmiResultObj=new HmiResultObj();
		
		ScheduleObj resultScheduleObj=scheduleInterface.updateSchedule(scheduleObj);
		
		if(resultScheduleObj==null){
			hmiResultObj.setResultMessage(ResultStatuObj.RESULT_STATU_FAIL_STR);
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
		}else{
			ScheduleTimePlan reScheduleTimePlan=resultScheduleObj.getScheduleTimePlanForUpdate();
			hmiResultObj.setResultStatu(reScheduleTimePlan.getPlanStatus());
			if(reScheduleTimePlan.getPlanStatus()==ResultStatuObj.RESULT_STATU_FAIL)
				hmiResultObj.setResultMessage(reScheduleTimePlan.getPlanStatusComment());
			else
				hmiResultObj.setResultMessage(""+resultScheduleObj.getSchedulePlan().getSchId());
				
			
		}
		
		return  hmiResultObj;
	}
	
	
	@RequestMapping(value="/deleteTimePlan/{type}/{userName}/{password}", method = RequestMethod.POST) 
	public @ResponseBody HmiResultObj deleteTimePlan(@RequestBody ScheduleTimePlan scheduleTimePlan ,@PathVariable("type") int type,@PathVariable String userName,@PathVariable String password, HttpServletRequest request) throws UnAuthorizedUserException {
		
		User sessionUser=processUserService.loginUserControl(userName, password);
		if(sessionUser==null){
			throw new UnAuthorizedUserException("");
    	}else if((sessionUser.getUserType()==UserTypes.USER_TYPE_MEMBER_INT)){
			throw new UnAuthorizedUserException("");
		}
		
		PacketSaleInterface packetSaleInterface=null;
		
		
		if(type==ProgramTypes.PROGRAM_PERSONAL){
			scheduleFacadeService=schedulePersonalFacade;
			packetSaleInterface=packetSalePersonal;
		}else if(type== ProgramTypes.PROGRAM_CLASS){
			scheduleFacadeService=scheduleClassFacade;
			packetSaleInterface=packetSaleClass;
		}
		
		
		HmiResultObj hmiResultObj= scheduleFacadeService.canScheduleDelete(scheduleTimePlan.getSchtId());
		if(hmiResultObj.getResultStatu()==ResultStatuObj.RESULT_STATU_FAIL){
			return hmiResultObj;
		}
		
		
		List<ScheduleTimePlan> scheduleTimePlans=scheduleService.findScheduleTimePlanByPlanId(scheduleTimePlan.getSchId());
		if(scheduleTimePlans.size()==1){
			SchedulePlan schedulePlan=new SchedulePlan();
			schedulePlan.setSchId(scheduleTimePlan.getSchId());
			
			List<PacketSaleFactory> packetSaleFactories=packetSaleInterface.findUserPacketSaleBySchId(schedulePlan.getSchId());
			for (PacketSaleFactory packetSaleFactory : packetSaleFactories) {
				packetSaleFactory.setSaleStatu(SaleStatus.SALE_NO_PLANNED);
				packetSaleInterface.updateSalePacket(packetSaleFactory);
			}
			
			
			scheduleService.deleteSchedulePlan(schedulePlan);
		}else{
			
			
			List<PacketSaleFactory> packetSaleFactories=packetSaleInterface.findUserPacketSaleBySchId(scheduleTimePlan.getSchId());
			for (PacketSaleFactory packetSaleFactory : packetSaleFactories) {
				packetSaleFactory.setSaleStatu(SaleStatus.SALE_HAS_PLANNED);
				packetSaleInterface.updateSalePacket(packetSaleFactory);
			}
			
			hmiResultObj=scheduleService.deleteScheduleTimePlan(scheduleTimePlan);
			
		}
		
		
			
		return hmiResultObj;
		
	}
	
	
	@RequestMapping(value="/postPoneTimePlan/{schtId}/{type}/{userName}/{password}", method = RequestMethod.POST) 
	public @ResponseBody HmiResultObj postPoneTimePlan(@PathVariable long schtId ,@PathVariable int type,@PathVariable String userName,@PathVariable String password ,HttpServletRequest request) throws UnAuthorizedUserException {
		
		User sessionUser=processUserService.loginUserControl(userName, password);
		if(sessionUser==null){
			throw new UnAuthorizedUserException("");
    	}else if((sessionUser.getUserType()==UserTypes.USER_TYPE_MEMBER_INT)){
			throw new UnAuthorizedUserException("");
		}
		
		
		HmiResultObj hmiResultObj=new HmiResultObj();
		hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS);
		
		ScheduleTimePlan scheduleTimePlan=scheduleService.findScheduleTimePlanById(schtId);
		PacketSaleInterface packetSaleInterface=null;
		ScheduleInterface scheduleInterface=null;
		if(type==ProgramTypes.PROGRAM_PERSONAL){
			packetSaleInterface=packetSalePersonal;
			scheduleInterface=scheduleUsersPersonalPlan;
		}else if(type== ProgramTypes.PROGRAM_CLASS){
			packetSaleInterface=packetSaleClass;
			scheduleInterface=scheduleUsersClassPlan;
		}
		if(scheduleTimePlan!=null){
			if(scheduleTimePlan.getStatuTp()!=StatuTypes.TIMEPLAN_POSTPONE ){
			  scheduleTimePlan.setStatuTp(StatuTypes.TIMEPLAN_POSTPONE);
			
			  List<PacketSaleFactory> packetSaleFactories=packetSaleInterface.findUserPacketSaleBySchId(scheduleTimePlan.getSchId());
				for (PacketSaleFactory packetSaleFactory : packetSaleFactories) {
					packetSaleFactory.setSaleStatu(SaleStatus.SALE_HAS_PLANNED);
					packetSaleInterface.updateSalePacket(packetSaleFactory);
				}
			}else{
				scheduleTimePlan.setStatuTp(StatuTypes.TIMEPLAN_NORMAL);
			
				List<PacketSaleFactory> packetSaleFactories=packetSaleInterface.findUserPacketSaleBySchId(scheduleTimePlan.getSchId());
				
				for (PacketSaleFactory packetSaleFactory : packetSaleFactories) {
					List<ScheduleFactory> scheduleFactories=scheduleInterface.findUserSchedulesTimesBySaleId(packetSaleFactory.getSaleId());
					int countOfParticipate=0;
					for (ScheduleFactory scheduleFactory : scheduleFactories) {
						if(scheduleFactory.getStatuTp()<StatuTypes.TIMEPLAN_POSTPONE){
							countOfParticipate++;
						}
						
					}
					
					
					
					if(countOfParticipate==packetSaleFactory.getProgCount()){
						packetSaleFactory.setSaleStatu(SaleStatus.SALE_FINISHED_PLANNED);
						packetSaleInterface.updateSalePacket(packetSaleFactory);
					}
				}
			}
			hmiResultObj=scheduleService.createScheduleTimePlan(scheduleTimePlan);
		}
		
		return hmiResultObj;
	}
	
	
	@RequestMapping(value="/cancelTimePlan/{schtId}/{userName}/{password}", method = RequestMethod.POST) 
	public @ResponseBody HmiResultObj cancelTimePlan(@PathVariable long schtId,@PathVariable String userName,@PathVariable String password ,HttpServletRequest request) throws UnAuthorizedUserException {
		
		User sessionUser=processUserService.loginUserControl(userName, password);
		if(sessionUser==null){
			throw new UnAuthorizedUserException("");
    	}else if((sessionUser.getUserType()==UserTypes.USER_TYPE_MEMBER_INT)){
			throw new UnAuthorizedUserException("");
		}
		
		HmiResultObj hmiResultObj=new HmiResultObj();
		hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS);
		
		ScheduleTimePlan scheduleTimePlan=scheduleService.findScheduleTimePlanById(schtId);
		
		if(scheduleTimePlan!=null){
			if(scheduleTimePlan.getStatuTp()!=StatuTypes.TIMEPLAN_CANCEL )
			  scheduleTimePlan.setStatuTp(StatuTypes.TIMEPLAN_CANCEL);
			else
				scheduleTimePlan.setStatuTp(StatuTypes.TIMEPLAN_NORMAL);
				
			hmiResultObj=scheduleService.createScheduleTimePlan(scheduleTimePlan);
		}
		
		return hmiResultObj;
	}
	
	
	
	
	
	@RequestMapping(value="/removeMember/{type}/{userName}/{password}", method = RequestMethod.POST) 
	public @ResponseBody HmiResultObj removeMember(@RequestBody ScheduleFactory scheduleFactory,@PathVariable("type") int type,@PathVariable String userName,@PathVariable String password, HttpServletRequest request) throws UnAuthorizedUserException {
		
		User sessionUser=processUserService.loginUserControl(userName, password);
		if(sessionUser==null){
			throw new UnAuthorizedUserException("");
    	}else if((sessionUser.getUserType()==UserTypes.USER_TYPE_MEMBER_INT)){
			throw new UnAuthorizedUserException("");
		}
		PacketSaleInterface packetSaleInterface=null;
		
		
		if(type==ProgramTypes.PROGRAM_PERSONAL){
			scheduleFacadeService=schedulePersonalFacade;
			scheduleInterface=scheduleUsersPersonalPlan;
			packetSaleInterface=packetSalePersonal;
		}else if(type== ProgramTypes.PROGRAM_CLASS){
			scheduleFacadeService=scheduleClassFacade;
			scheduleInterface=scheduleUsersClassPlan;
			packetSaleInterface=packetSaleClass;
		}
		
		
		HmiResultObj hmiResultObj= scheduleFacadeService.canUserRemoveFromTimePlan(scheduleFactory);
		if(hmiResultObj.getResultStatu()==ResultStatuObj.RESULT_STATU_FAIL){
			return hmiResultObj;
		}
		
		
		
		
		
		hmiResultObj=scheduleInterface.deleteScheduleUsersPlan(scheduleFactory);
		
		List<SchedulePlan> schedulePlan= scheduleInterface.findSchedulePlansbyUserId(scheduleFactory.getUserId(), scheduleFactory.getSaleId());
		
		if(schedulePlan.size()==0){
			  PacketSaleFactory packetSaleFactories=packetSaleInterface.findSaledPacketsById(scheduleFactory.getSaleId());
			  packetSaleFactories.setSaleStatu(SaleStatus.SALE_NO_PLANNED);
			  packetSaleInterface.updateSalePacket(packetSaleFactories);
		}
		
		
		return hmiResultObj;
	}
	
	
	
	
	

	@RequestMapping(value="/changeSchedule/{userName}/{password}", method = RequestMethod.POST) 
	public @ResponseBody ScheduleObj changeTimePlan(@RequestBody ScheduleTimePlan scheduleTimePlan,@PathVariable String userName,@PathVariable String password, HttpServletRequest request) throws UnAuthorizedUserException, IOException, ServletException {
	
		User sessionUser=processUserService.loginUserControl(userName, password);
		if(sessionUser==null){
			throw new UnAuthorizedUserException("");
    	}else if((sessionUser.getUserType()==UserTypes.USER_TYPE_MEMBER_INT)){
			throw new UnAuthorizedUserException("");
		}
		
		
		
		
		SchedulePlan schedulePlan=scheduleService.findSchedulePlanByPlanId(scheduleTimePlan.getSchId());
		
		

		if(schedulePlan.getProgType()==ProgramTypes.PROGRAM_PERSONAL){
			scheduleInterface=scheduleUsersPersonalPlan;
		}else if(schedulePlan.getProgType()==ProgramTypes.PROGRAM_CLASS){
			scheduleInterface=scheduleUsersClassPlan;
		}
		
		ScheduleTimePlan scTimePlan=scheduleService.findScheduleTimePlanById(scheduleTimePlan.getSchtId());
		scTimePlan.setSchtStaffId(scheduleTimePlan.getSchtStaffId());
		
		
		List<ScheduleStudios> scheduleStudios=scheduleService.findScheduleStudiosByTimePlanId(scheduleTimePlan.getSchtId());
		List<ScheduleFactory> scheduleFactories=scheduleInterface.findSchedulesTimesBySchtId(scheduleTimePlan.getSchtId());
		User staff=processUserService.findById(scheduleTimePlan.getSchtStaffId());
		staff.setPassword(null);
		
		scTimePlan.setStaff(staff);
		scTimePlan.setScheduleStudios(scheduleStudios);
		scTimePlan.setUsers(scheduleFactories);
		
		
		
		String planStartDate=scheduleTimePlan.getPlanStartDateStr()+" "+scheduleTimePlan.getPlanDayTime();
		
		////System.out.println("PLAN START DATE IS "+planStartDate);
		
		scTimePlan.setPlanStartDate(OhbeUtil.getThatDayFormatNotNull(scheduleTimePlan.getPlanStartDateStr(), GlobalUtil.global.getPtScrDateFormat()+" HH:mm"));
		
		
		ScheduleObj scheduleObj=new ScheduleObj();
		scheduleObj.setSchedulePlan(schedulePlan);
		scheduleObj.setPlanStartDateStr(planStartDate);
		scheduleObj.setScheduleTimePlanForUpdate(scTimePlan);
		
		return scheduleInterface.changeSchedule(scheduleObj);
	}
	
	
}
