package tr.com.abasus.ptboss.schedule.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
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
import tr.com.abasus.ptboss.program.entity.ProgramInterface;
import tr.com.abasus.ptboss.program.entity.ProgramPersonal;
import tr.com.abasus.ptboss.ptuser.entity.User;
import tr.com.abasus.ptboss.ptuser.service.ProcessUserService;
import tr.com.abasus.ptboss.result.entity.HmiResultObj;
import tr.com.abasus.ptboss.schedule.businessEntity.ScheduleCalendarObj;
import tr.com.abasus.ptboss.schedule.businessEntity.ScheduleObj;
import tr.com.abasus.ptboss.schedule.businessEntity.ScheduleSearchObj;
import tr.com.abasus.ptboss.schedule.businessEntity.ScheduleTimeObj;
import tr.com.abasus.ptboss.schedule.businessService.ScheduleBusinessCalendarAllService;
import tr.com.abasus.ptboss.schedule.businessService.ScheduleBusinessCalendarService;
import tr.com.abasus.ptboss.schedule.businessService.ScheduleBusinessCalendarTimesService;
import tr.com.abasus.ptboss.schedule.entity.ScheduleFactory;
import tr.com.abasus.ptboss.schedule.entity.ScheduleInterface;
import tr.com.abasus.ptboss.schedule.entity.ScheduleMembershipPlan;
import tr.com.abasus.ptboss.schedule.entity.SchedulePlan;
import tr.com.abasus.ptboss.schedule.entity.ScheduleStudios;
import tr.com.abasus.ptboss.schedule.entity.ScheduleTimePlan;
import tr.com.abasus.ptboss.schedule.entity.ScheduleUsersClassPlan;
import tr.com.abasus.ptboss.schedule.entity.ScheduleUsersPersonalPlan;
import tr.com.abasus.ptboss.schedule.service.ScheduleService;
import tr.com.abasus.util.DateTimeUtil;
import tr.com.abasus.util.GlobalUtil;
import tr.com.abasus.util.OhbeUtil;
import tr.com.abasus.util.ProgramTypes;
import tr.com.abasus.util.ResultStatuObj;
import tr.com.abasus.util.SaleStatus;
import tr.com.abasus.util.SessionUtil;
import tr.com.abasus.util.StatuTypes;

@Controller
@RequestMapping(value="/schedule")
public class ScheduleController {

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
	
	@Autowired
	ScheduleBusinessCalendarAllService scheduleBusinessCalendarAllService;
	
	
	@Autowired
	ScheduleBusinessCalendarTimesService scheduleBusinessCalendarTimesService;
	
	
	@RequestMapping(value="/getCurrentTime", method = RequestMethod.POST) 
	public @ResponseBody ScheduleTimeObj getCurrentTime(HttpServletRequest request) throws UnAuthorizedUserException {
	
		User user=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if((user==null)){
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
	
	@RequestMapping(value="/getMorningTimes", method = RequestMethod.POST) 
	public @ResponseBody List<String> getMorningTimes(HttpServletRequest request) throws UnAuthorizedUserException {
		return scheduleBusinessCalendarTimesService.getTimesForMorning();
	}
	
	@RequestMapping(value="/getAfternoonTimes", method = RequestMethod.POST) 
	public @ResponseBody List<String> getAfternoonTimes(HttpServletRequest request) throws UnAuthorizedUserException {
		return scheduleBusinessCalendarTimesService.getTimesForAfternoon();
	}
	
	@RequestMapping(value="/getNightTimes", method = RequestMethod.POST) 
	public @ResponseBody List<String> getNightTimes(HttpServletRequest request) throws UnAuthorizedUserException {
		return scheduleBusinessCalendarTimesService.getTimesForNight();
	}
	
	@RequestMapping(value="/getAllDayTimes", method = RequestMethod.POST) 
	public @ResponseBody List<String> getAllDayTimes(HttpServletRequest request) throws UnAuthorizedUserException {
		return scheduleBusinessCalendarTimesService.getTimesForAllDay();
	}
	
	@RequestMapping(value="/cancelTimePlan/{schtId}", method = RequestMethod.POST) 
	public @ResponseBody HmiResultObj cancelTimePlan(@PathVariable long schtId ,HttpServletRequest request) throws UnAuthorizedUserException {
		
		User user=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if((user==null)){
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
	
	
	@RequestMapping(value="/postPoneTimePlan/{schtId}/{type}", method = RequestMethod.POST) 
	public @ResponseBody HmiResultObj postPoneTimePlan(@PathVariable long schtId ,@PathVariable int type ,HttpServletRequest request) throws UnAuthorizedUserException {
		
		User user=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if((user==null)){
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
	
	@RequestMapping(value="/findScheduleFactoryPlanById/{id}/{type}", method = RequestMethod.POST) 
	public @ResponseBody ScheduleFactory findScheduleFactoryPlanById(@PathVariable("id") long id,@PathVariable("type") String type, HttpServletRequest request) throws UnAuthorizedUserException {
		
		User user=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if((user==null)){
			throw new UnAuthorizedUserException("");
		}
		
		if(type.equals(ProgramTypes.SCHEDULE_TYPE_PERSONAL)){
			scheduleInterface=scheduleUsersPersonalPlan;
		}else if(type.equals(ProgramTypes.SCHEDULE_TYPE_CLASS)){
			scheduleInterface=scheduleUsersClassPlan;
		}else if(type.equals(ProgramTypes.SCHEDULE_TYPE_MEMBERSHIP)){
			scheduleInterface=scheduleMembershipPlan;
		}
		
		return scheduleInterface.findScheduleFactoryPlanById(id);
	}
	
	
	@RequestMapping(value="/findSchedulePlanById/{schId}/{type}", method = RequestMethod.POST) 
	public @ResponseBody SchedulePlan findSchedulePlanById(@PathVariable("schId") long schId,@PathVariable("type") int type, HttpServletRequest request) throws UnAuthorizedUserException {
		
		User user=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if((user==null)){
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
		}
		
		
		schedulePlan.setScheduleTimePlans(scheduleTimePlans);
		}
		
		
		return  getProgramsCounts(schedulePlan);
	}
	
	@RequestMapping(value="/findSchedulePlanBySaleId/{saleId}/{type}", method = RequestMethod.POST) 
	public @ResponseBody SchedulePlan findSchedulePlanBySaleId(@PathVariable("saleId") long saleId,@PathVariable("type") int type, HttpServletRequest request) throws UnAuthorizedUserException {
		
		User user=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if((user==null)){
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
					if(scheduleTimePlan.getStatuTp()!=StatuTypes.TIMEPLAN_POSTPONE){
						if(scheduleTimePlan.getPlanStartDate().before(new Date())){
							plannedCount++;
						}
					}
				}
				
				int willPlanCount=programCount-plannedCount;
				
				schedulePlan.setProgramCount(programCount);
				schedulePlan.setPlannedCount(plannedCount);
				schedulePlan.setWillPlanCount(willPlanCount);
			}
		
		
		return schedulePlan;
	}
	
	
	
	@RequestMapping(value="/getSchedule/{type}/{schId}", method = RequestMethod.POST) 
	public @ResponseBody SchedulePlan getSchedule(@PathVariable("type") int type,@PathVariable("schId") long schId, HttpServletRequest request) throws UnAuthorizedUserException {
		
		User user=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if((user==null)){
			throw new UnAuthorizedUserException("");
		}
		
		if(type==ProgramTypes.PROGRAM_PERSONAL){
			scheduleInterface=scheduleUsersPersonalPlan;
			programInterface=programPersonal;
		}else if(type==ProgramTypes.PROGRAM_CLASS){
			scheduleInterface=scheduleUsersClassPlan;
			programInterface=programClass;
		}
		
		
		SchedulePlan schedulePlan=scheduleService.findSchedulePlanByPlanId(schId);
		if(schedulePlan!=null){
			ProgramFactory programFactory=programInterface.findProgramById(schedulePlan.getProgId());
			schedulePlan.setProgName(programFactory.getProgName());
			User staffMain=processUserService.findById(schedulePlan.getSchStaffId());
			staffMain.setPassword(null);
			schedulePlan.setStaff(staffMain);
			List<ScheduleTimePlan> scheduleTimePlans=scheduleService.findScheduleTimePlanByPlanId(schId);
			
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
	
	
	@RequestMapping(value="/createSchedule/{type}", method = RequestMethod.POST) 
	public @ResponseBody ScheduleObj createSchedule(@RequestBody ScheduleObj scheduleObj ,@PathVariable("type") String type, HttpServletRequest request) throws UnAuthorizedUserException {
	
		User user=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if((user==null)){
			throw new UnAuthorizedUserException("");
		}
		
		if(type.equals(ProgramTypes.SCHEDULE_TYPE_PERSONAL)){
			scheduleInterface=scheduleUsersPersonalPlan;
		}else if(type.equals(ProgramTypes.SCHEDULE_TYPE_CLASS)){
			scheduleInterface=scheduleUsersClassPlan;
		}
		scheduleObj.setFirmId(user.getFirmId());
		
		String tpComment=scheduleObj.getTpComment();
		if(tpComment.length()>255){
			tpComment=tpComment.substring(0,255);
		}
		
		scheduleObj.setTpComment(tpComment);
		
		SchedulePlan schedulePlan=scheduleObj.getSchedulePlan();
		if(schedulePlan.getSchId()!=0){
			return getProgramsCountsForScheduleObj(scheduleInterface.continueSchedule(scheduleObj));
		}
		
		
		return getProgramsCountsForScheduleObj(scheduleInterface.createSchedule(scheduleObj));
	}
	
	
	private ScheduleObj getProgramsCountsForScheduleObj(ScheduleObj scheduleObj){
		if(scheduleObj!=null){
			SchedulePlan scPlan=scheduleObj.getSchedulePlan();
			if(scPlan!=null){
				List<ScheduleTimePlan> scheduleTimePlans=scPlan.getScheduleTimePlans();
				int programCount=scPlan.getSchCount();
				int plannedCount=0;
				for (ScheduleTimePlan scheduleTimePlan : scheduleTimePlans) {
					if(scheduleTimePlan.getStatuTp()!=StatuTypes.TIMEPLAN_POSTPONE){
						if(scheduleTimePlan.getPlanStartDate().before(new Date()) && scheduleTimePlan.getPlanStatus()!=StatuTypes.TIMEPLAN_POSTPONE){
							plannedCount++;
						}
					}
				}
				
				int willPlanCount=programCount-plannedCount;
				
				scPlan.setProgramCount(programCount);
				scPlan.setPlannedCount(plannedCount);
				scPlan.setWillPlanCount(willPlanCount);
			}
		}
		
		return scheduleObj;
	}
	
	
	@RequestMapping(value="/updateSchedule/{type}", method = RequestMethod.POST) 
	public @ResponseBody ScheduleObj updateSchedule(@RequestBody ScheduleObj scheduleObj ,@PathVariable("type") String type, HttpServletRequest request,HttpServletResponse response) throws UnAuthorizedUserException, IOException, ServletException {
	
		User user=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if((user==null)){
			throw new UnAuthorizedUserException("");
		}
		
		if(type.equals(ProgramTypes.SCHEDULE_TYPE_PERSONAL)){
			scheduleInterface=scheduleUsersPersonalPlan;
		}else if(type.equals(ProgramTypes.SCHEDULE_TYPE_CLASS)){
			scheduleInterface=scheduleUsersClassPlan;
		}
		return  getProgramsCountsForScheduleObj(scheduleInterface.updateSchedule(scheduleObj));
	}
	
	
	@RequestMapping(value="/continueSchedule/{type}", method = RequestMethod.POST) 
	public @ResponseBody ScheduleObj continueSchedule(@RequestBody ScheduleObj scheduleObj ,@PathVariable("type") String type, HttpServletRequest request) throws UnAuthorizedUserException {
	
		User user=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if((user==null)){
			throw new UnAuthorizedUserException("");
		}
		
		if(type.equals(ProgramTypes.SCHEDULE_TYPE_PERSONAL)){
			scheduleInterface=scheduleUsersPersonalPlan;
		}else if(type.equals(ProgramTypes.SCHEDULE_TYPE_CLASS)){
			scheduleInterface=scheduleUsersClassPlan;
		}
		scheduleObj.setFirmId(user.getFirmId());
		
		
		String tpComment=scheduleObj.getTpComment();
		if(tpComment.length()>255){
			tpComment=tpComment.substring(0,255);
		}
		
		scheduleObj.setTpComment(tpComment);
		
		
		return scheduleInterface.continueSchedule(scheduleObj);
	}
	
	
	@RequestMapping(value="/deletePlan/{type}", method = RequestMethod.POST) 
	public @ResponseBody HmiResultObj deletePlan(@RequestBody SchedulePlan schedulePlan ,@PathVariable("type") int type, HttpServletRequest request) throws UnAuthorizedUserException {
		
		User user=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if((user==null)){
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
		
		List<ScheduleTimePlan> scheduleTimePlans=scheduleService.findScheduleTimePlanByPlanId(schedulePlan.getSchId());
		
		for (ScheduleTimePlan stp : scheduleTimePlans) {
			HmiResultObj hmiResultObj= scheduleFacadeService.canScheduleDelete(stp.getSchtId());
			if(hmiResultObj.getResultStatu()==ResultStatuObj.RESULT_STATU_FAIL){
				return hmiResultObj;
			}
		}
		
		List<PacketSaleFactory> packetSaleFactories=packetSaleInterface.findUserPacketSaleBySchId(schedulePlan.getSchId());
		for (PacketSaleFactory packetSaleFactory : packetSaleFactories) {
			packetSaleFactory.setSaleStatu(SaleStatus.SALE_NO_PLANNED);
			packetSaleInterface.updateSalePacket(packetSaleFactory);
		}
		
		
		return scheduleService.deleteSchedulePlan(schedulePlan);
	}
	
	@RequestMapping(value="/deleteTimePlan/{type}", method = RequestMethod.POST) 
	public @ResponseBody HmiResultObj deleteTimePlan(@RequestBody ScheduleTimePlan scheduleTimePlan ,@PathVariable("type") int type, HttpServletRequest request) throws UnAuthorizedUserException {
		
		User user=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if((user==null)){
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
	
	@RequestMapping(value="/removeMember/{type}", method = RequestMethod.POST) 
	public @ResponseBody HmiResultObj removeMember(@RequestBody ScheduleFactory scheduleFactory,@PathVariable("type") int type, HttpServletRequest request) throws UnAuthorizedUserException {
		
		User user=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if((user==null)){
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
	
	
	
	
	

	@RequestMapping(value="/changeSchedule", method = RequestMethod.POST) 
	public @ResponseBody ScheduleObj changeTimePlan(@RequestBody ScheduleTimePlan scheduleTimePlan, HttpServletRequest request) throws UnAuthorizedUserException, IOException, ServletException {
	
		User user=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if((user==null)){
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
		scheduleObj.setTpComment(scTimePlan.getTpComment());
		
		
		return scheduleInterface.changeSchedule(scheduleObj);
	}
	
	
	
	
	
	@RequestMapping(value="/searchDashTimePlansForStaff", method = RequestMethod.POST) 
	public @ResponseBody ScheduleCalendarObj searchDashTimePlansForStaff(@RequestBody ScheduleCalendarObj scheduleCalendarObj, HttpServletRequest request) throws UnAuthorizedUserException {
	
		User user=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if((user==null)){
			throw new UnAuthorizedUserException("");
		}
		List<ScheduleTimePlan> scheduleTimePlans=new ArrayList<ScheduleTimePlan>();
		
		Date startDate=OhbeUtil.getThatDateForNight(scheduleCalendarObj.getCalendarDate(), GlobalUtil.global.getPtScrDateFormat());
		startDate=OhbeUtil.getDateForNextDate(startDate, scheduleCalendarObj.getDay());
		
		Date endDate=OhbeUtil.getThatDateForNight(scheduleCalendarObj.getCalendarDate(), GlobalUtil.global.getPtScrDateFormat());
		endDate=OhbeUtil.getDateForNextDate(endDate, scheduleCalendarObj.getDay());
		endDate=OhbeUtil.getDateForNextDate(endDate, 1);
		
		scheduleCalendarObj.setCalendarDate(OhbeUtil.getDateStrByFormat(startDate, GlobalUtil.global.getPtDbDateFormat()));
		scheduleCalendarObj.setCalendarDateName(DateTimeUtil.getDayNames(startDate));
		ScheduleCalendarObj scObj=new ScheduleCalendarObj();
		
		
			scheduleTimePlans=scheduleService.findScheduleTimePlanByStaffAndDate(startDate, endDate, scheduleCalendarObj.getStaffId());
			for (ScheduleTimePlan scheduleTimePlan : scheduleTimePlans) {
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
					scheduleTimePlan.setPlanCount(pf.getProgCount());
					scheduleTimePlan.setPlanDayTime(DateTimeUtil.getHourMinute(scheduleTimePlan.getPlanStartDate()));
					
					
					List<ScheduleFactory> scheduleFactories=scheduleInterface.findSchedulesTimesBySchtId(scheduleTimePlan.getSchtId());
					scheduleTimePlan.setUsers(scheduleFactories);
					
					ScheduleTimePlan schTP= schedulePlanFacadeService.getSequenceOfScheduleTimePlan(scheduleTimePlan);
					scheduleTimePlan.setSequence(schTP.getSequence());
					scheduleTimePlan.setLastPlan(schTP.getLastPlan());
			}
			
			scObj.setScheduleTimePlans(scheduleTimePlans);
			scObj.setCalendarDate(OhbeUtil.getDateStrByFormat(startDate, GlobalUtil.global.getPtDbDateFormat()));
			scObj.setCalendarDateName(DateTimeUtil.getDayNames(startDate));
		
		
	    return scObj;
		
	};
	
	
	@RequestMapping(value="/searchTimePlansForStaff", method = RequestMethod.POST) 
	public @ResponseBody ScheduleCalendarObj searchTimePlansForStaff(@RequestBody ScheduleCalendarObj scheduleCalendarObj, HttpServletRequest request) throws UnAuthorizedUserException {
	
		User user=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if((user==null)){
			throw new UnAuthorizedUserException("");
		}
		
		Date startDate=OhbeUtil.getThatDateForNight(scheduleCalendarObj.getCalendarDate(), GlobalUtil.global.getPtScrDateFormat());
		startDate=OhbeUtil.getDateForNextDate(startDate, scheduleCalendarObj.getDay());
		
		Date endDate=OhbeUtil.getThatDateForNight(scheduleCalendarObj.getCalendarDate(), GlobalUtil.global.getPtScrDateFormat());
		endDate=OhbeUtil.getDateForNextDate(endDate, scheduleCalendarObj.getDay());
		endDate=OhbeUtil.getDateForNextDate(endDate, 1);
		
		scheduleCalendarObj.setCalendarDate(OhbeUtil.getDateStrByFormat(startDate, GlobalUtil.global.getPtDbDateFormat()));
		scheduleCalendarObj.setCalendarDateName(DateTimeUtil.getDayNames(startDate));
		ScheduleCalendarObj scObj=new ScheduleCalendarObj();
		
		List<User> staffList=processUserService.findAllToSchedulerStaffForCalendar(user.getFirmId());
		
		String calendarForMorning="";
		String calendarForAfternoon="";
		String calendarForNight="";
		
		for (User staff : staffList) {
			ScheduleCalendarObj schCalObj=new ScheduleCalendarObj();
			List<ScheduleTimePlan> scheduleTimePlans=scheduleService.findScheduleTimePlanByStaffAndDate(startDate, endDate, staff.getUserId());
			
			
			for (ScheduleTimePlan scheduleTimePlan : scheduleTimePlans) {
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
					scheduleTimePlan.setProgShortName(pf.getProgShortName());
					scheduleTimePlan.setPlanCount(pf.getProgCount());
					
					scheduleTimePlan.setPlanDayTime(DateTimeUtil.getHourMinute(scheduleTimePlan.getPlanStartDate()));
					
					List<ScheduleFactory> scheduleFactories=scheduleInterface.findSchedulesTimesBySchtId(scheduleTimePlan.getSchtId());
					scheduleTimePlan.setUsers(scheduleFactories);
					
					ScheduleTimePlan schTP= schedulePlanFacadeService.getSequenceOfScheduleTimePlan(scheduleTimePlan);
					scheduleTimePlan.setSequence(schTP.getSequence());
					scheduleTimePlan.setLastPlan(schTP.getLastPlan());
					scheduleTimePlan.setFirstPlan(schTP.getFirstPlan());
			}
			
			schCalObj.setStaff(staff);
			schCalObj.setScheduleTimePlans(scheduleTimePlans);
			
			
			calendarForMorning+=scheduleBusinessCalendarService.createPlanTblSabah(scheduleTimePlans, staff.getUserName()+" "+staff.getUserSurname(), staff.getUserId());
			calendarForAfternoon+=scheduleBusinessCalendarService.createPlanTblAfternoon(scheduleTimePlans, staff.getUserName()+" "+staff.getUserSurname(), staff.getUserId());
			calendarForNight+=scheduleBusinessCalendarService.createPlanTblNight(scheduleTimePlans, staff.getUserName()+" "+staff.getUserSurname(), staff.getUserId());
			
		}
		
		
		scObj.setCalendarDate(OhbeUtil.getDateStrByFormat(startDate, GlobalUtil.global.getPtDbDateFormat()));
		scObj.setCalendarDateName(DateTimeUtil.getDayNames(startDate));
		
		
		scObj.setCalendarForMorning(calendarForMorning);
		scObj.setCalendarForAfternoon(calendarForAfternoon);;
		scObj.setCalendarForNight(calendarForNight);;
		
	    return scObj;
		
	};
	
	
	
	
	@RequestMapping(value="/searchTimePlansForStaffToWeek", method = RequestMethod.POST) 
	public @ResponseBody ScheduleCalendarObj searchTimePlansForStaffToWeek(@RequestBody ScheduleCalendarObj scheduleCalendarObj, HttpServletRequest request) throws UnAuthorizedUserException {
	
		User user=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if((user==null)){
			throw new UnAuthorizedUserException("");
		}
		
		Date startDate=OhbeUtil.getThatDateForNight(scheduleCalendarObj.getCalendarDate(), GlobalUtil.global.getPtScrDateFormat());
		startDate=OhbeUtil.getDateForNextDate(startDate, scheduleCalendarObj.getDay());
		
		Date endDate=OhbeUtil.getThatDateForNight(scheduleCalendarObj.getCalendarDate(), GlobalUtil.global.getPtScrDateFormat());
		endDate=OhbeUtil.getDateForNextDate(endDate, scheduleCalendarObj.getDay());
		endDate=OhbeUtil.getDateForNextDate(endDate, scheduleCalendarObj.getDayDuration());
		
		scheduleCalendarObj.setCalendarDate(OhbeUtil.getDateStrByFormat(startDate, GlobalUtil.global.getPtDbDateFormat()));
		scheduleCalendarObj.setCalendarDateName(DateTimeUtil.getDayNames(startDate));
		ScheduleCalendarObj scObj=new ScheduleCalendarObj();
		
		String calendarForWeekMorning="";
		String calendarForWeekNight="";
		String calendarForWeekHeader="";
		
		User staff =processUserService.findById(scheduleCalendarObj.getStaffId());
		
		int screenSize=scheduleCalendarObj.getActualSize();
				
				
			ScheduleCalendarObj schCalObj=new ScheduleCalendarObj();
			List<ScheduleTimePlan> scheduleTimePlans=scheduleService.findScheduleTimePlanByStaffAndDate(startDate, endDate, staff.getUserId());
			for (ScheduleTimePlan scheduleTimePlan : scheduleTimePlans) {
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
					scheduleTimePlan.setProgShortName(pf.getProgShortName());
					scheduleTimePlan.setPlanCount(pf.getProgCount());
					
					List<ScheduleFactory> scheduleFactories=scheduleInterface.findSchedulesTimesBySchtId(scheduleTimePlan.getSchtId());
					scheduleTimePlan.setUsers(scheduleFactories);
					
					ScheduleTimePlan schTP= schedulePlanFacadeService.getSequenceOfScheduleTimePlan(scheduleTimePlan);
					scheduleTimePlan.setSequence(schTP.getSequence());
					scheduleTimePlan.setLastPlan(schTP.getLastPlan());
					scheduleTimePlan.setFirstPlan(schTP.getFirstPlan());
			
			}
			
			schCalObj.setStaff(staff);
			schCalObj.setScheduleTimePlans(scheduleTimePlans);
			
			calendarForWeekMorning+=scheduleBusinessCalendarService.createPlanTblWeekMorning(scheduleTimePlans, staff.getUserId(), staff.getUserName()+" "+staff.getUserSurname(),scheduleCalendarObj.getDayDuration(),startDate,screenSize);
			calendarForWeekNight+=scheduleBusinessCalendarService.createPlanTblWeekNight(scheduleTimePlans, staff.getUserId(), staff.getUserName()+" "+staff.getUserSurname(),scheduleCalendarObj.getDayDuration(),startDate,screenSize);
			
			scObj.setCalendarForWeekMorning(calendarForWeekMorning);
			scObj.setCalendarForWeekNight(calendarForWeekNight);
			
		    
		    
	    return scObj;
		
	};
	
	
	
	@RequestMapping(value="/searchTimePlansForAllStaffToWeek", method = RequestMethod.POST) 
	public @ResponseBody ScheduleCalendarObj searchTimePlansForAllStaffToWeek(@RequestBody ScheduleCalendarObj scheduleCalendarObj, HttpServletRequest request) throws UnAuthorizedUserException {
	
		User user=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if((user==null)){
			throw new UnAuthorizedUserException("");
		}
		
		Date startDate=OhbeUtil.getThatDateForNight(scheduleCalendarObj.getCalendarDate(), GlobalUtil.global.getPtScrDateFormat());
		startDate=OhbeUtil.getDateForNextDate(startDate, scheduleCalendarObj.getDay());
		
		Date endDate=OhbeUtil.getThatDateForNight(scheduleCalendarObj.getCalendarDate(), GlobalUtil.global.getPtScrDateFormat());
		endDate=OhbeUtil.getDateForNextDate(endDate, scheduleCalendarObj.getDay());
		endDate=OhbeUtil.getDateForNextDate(endDate, scheduleCalendarObj.getDayDuration());
		
		scheduleCalendarObj.setCalendarDate(OhbeUtil.getDateStrByFormat(startDate, GlobalUtil.global.getPtDbDateFormat()));
		scheduleCalendarObj.setCalendarDateName(DateTimeUtil.getDayNames(startDate));
		ScheduleCalendarObj scObj=new ScheduleCalendarObj();
		
		String calendarForWeekMorning="";
		String calendarForWeekNight="";
		String calendarForWeekHeader="";
		
		
		List<User> trainers=processUserService.findAllToSchedulerStaffForCalendar(user.getFirmId());
		
		if(trainers.size()<3){
			scheduleCalendarObj.setDayDuration(6);
		}else if(trainers.size()>2 && trainers.size()<=4){
			scheduleCalendarObj.setDayDuration(5);
		}else{
			scheduleCalendarObj.setDayDuration(4);
		}
		
		
		int screenSize=scheduleCalendarObj.getActualSize();
				
				
			
			
			
			
				//ScheduleCalendarObj schCalObj=new ScheduleCalendarObj();
				
		        List<ScheduleTimePlan> scheduleTimePlans=scheduleService.findScheduleTimePlanByDates(startDate, endDate);
				for (ScheduleTimePlan scheduleTimePlan : scheduleTimePlans) {
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
						scheduleTimePlan.setProgShortName(pf.getProgShortName());
						scheduleTimePlan.setPlanCount(pf.getProgCount());
						
						List<ScheduleFactory> scheduleFactories=scheduleInterface.findSchedulesTimesBySchtId(scheduleTimePlan.getSchtId());
						scheduleTimePlan.setUsers(scheduleFactories);
						
						ScheduleTimePlan schTP= schedulePlanFacadeService.getSequenceOfScheduleTimePlan(scheduleTimePlan);
						scheduleTimePlan.setSequence(schTP.getSequence());
						scheduleTimePlan.setLastPlan(schTP.getLastPlan());
						scheduleTimePlan.setFirstPlan(schTP.getFirstPlan());
				
				}
				
				//schCalObj.setStaff(staff);
				//schCalObj.setScheduleTimePlans(scheduleTimePlans);
				
			
			
			calendarForWeekMorning+=scheduleBusinessCalendarAllService.createPlanTblWeekMorning(scheduleTimePlans,trainers,scheduleCalendarObj.getDayDuration(),startDate,screenSize);
			calendarForWeekNight+=scheduleBusinessCalendarAllService.createPlanTblWeekNight(scheduleTimePlans,trainers,scheduleCalendarObj.getDayDuration(),startDate,screenSize);
		
			
			scObj.setCalendarForWeekMorning(calendarForWeekMorning);
			scObj.setCalendarForWeekNight(calendarForWeekNight);
			
		    
		    
	    return scObj;
		
	};
	
	
	
	@RequestMapping(value="/searchTimePlans", method = RequestMethod.POST) 
	public @ResponseBody ScheduleCalendarObj searchTimePlans(@RequestBody ScheduleCalendarObj scheduleCalendarObj, HttpServletRequest request) throws UnAuthorizedUserException {
		User user=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if((user==null)){
			throw new UnAuthorizedUserException("");
		}
		
		Date startDate=OhbeUtil.getThatDateForNight(scheduleCalendarObj.getCalendarDate(), GlobalUtil.global.getPtScrDateFormat());
		startDate=OhbeUtil.getDateForNextDate(startDate, scheduleCalendarObj.getDay());
		
		Date endDate=OhbeUtil.getThatDateForNight(scheduleCalendarObj.getCalendarDate(), GlobalUtil.global.getPtScrDateFormat());
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
		}
		
		scheduleCalendarObj.setScheduleTimePlans(scheduleTimePlans);
		
		return scheduleCalendarObj;
	}
	
	
	
	
	@RequestMapping(value="/searchPlans", method = RequestMethod.POST) 
	public @ResponseBody List<SchedulePlan> searchPlans(@RequestBody ScheduleSearchObj scheduleSearchObj, HttpServletRequest request) throws UnAuthorizedUserException {
		User user=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if((user==null)){
			throw new UnAuthorizedUserException("");
		}
		
		Date startDate=OhbeUtil.getThatDateForNight(scheduleSearchObj.getStartDateStr(), GlobalUtil.global.getPtScrDateFormat());
		Date endDate=OhbeUtil.getThatDateForNight(scheduleSearchObj.getEndDateStr(), GlobalUtil.global.getPtScrDateFormat());
		endDate=OhbeUtil.getDateForNextDate(endDate, 1);
		
		scheduleSearchObj.setEndDate(endDate);
		scheduleSearchObj.setStartDate(startDate);
		
		List<SchedulePlan> schedulePlans=scheduleService.searchByQuery(scheduleSearchObj);
		
		
		
		
		for (SchedulePlan schedulePlan : schedulePlans) {
			ProgramFactory programFactory=null;
			    if(schedulePlan.getProgType()==ProgramTypes.PROGRAM_CLASS){
			    	schedulePlan.setProgTypeStr("classProgram");
			    	programFactory=programClass.findProgramById(schedulePlan.getProgId());
			    	scheduleInterface=scheduleUsersClassPlan;
			    }else  if(schedulePlan.getProgType()==ProgramTypes.PROGRAM_PERSONAL){
			    	schedulePlan.setProgTypeStr("personalProgram");
			    	programFactory=programPersonal.findProgramById(schedulePlan.getProgId());
			    	scheduleInterface=scheduleUsersPersonalPlan;
			    }
			    
			    if(programFactory!=null){
		    		schedulePlan.setProgName(programFactory.getProgName());
		    	}
			
			    List<ScheduleTimePlan> scheduleTimePlans=scheduleService.findScheduleTimePlanByPlanIdByDates(schedulePlan.getSchId(), startDate, endDate);
			    schedulePlan.setFinishedPlan(schedulePlanFacadeService.schedulePlanCompleted(schedulePlan, scheduleTimePlans.size()));
				
			    
			    int i=1;
				for (ScheduleTimePlan scheduleTimePlan : scheduleTimePlans) {
					
					/**LAST PLAN FOUND**/
					int lastPlan=scheduleService.isScheduleTimeLast(schedulePlan, scheduleTimePlan, null);
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
			    	
					scheduleTimePlan.setScheduleStudios(scheduleStudios);
					scheduleTimePlan.setUsers(scheduleFactories);
				}
				
				schedulePlan.setScheduleTimePlans(scheduleTimePlans);
		}
		
		
		return schedulePlans;
	}
	
}
