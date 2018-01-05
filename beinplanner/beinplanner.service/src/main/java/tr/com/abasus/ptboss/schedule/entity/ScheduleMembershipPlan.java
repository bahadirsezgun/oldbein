package tr.com.abasus.ptboss.schedule.entity;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonTypeName;

import tr.com.abasus.ptboss.facade.schedule.ScheduleMembershipFacade;
import tr.com.abasus.ptboss.program.entity.ProgramFactory;
import tr.com.abasus.ptboss.program.entity.ProgramMembership;
import tr.com.abasus.ptboss.result.entity.HmiResultObj;
import tr.com.abasus.ptboss.schedule.businessEntity.ScheduleObj;
import tr.com.abasus.ptboss.schedule.businessEntity.ScheduleSearchObj;
import tr.com.abasus.ptboss.schedule.service.ScheduleMembershipService;
import tr.com.abasus.util.GlobalUtil;
import tr.com.abasus.util.OhbeUtil;
import tr.com.abasus.util.ProgDurationTypes;
import tr.com.abasus.util.ResultStatuObj;

@Component(value="scheduleMembershipPlan")
@Scope("prototype")
@JsonTypeName("smp")
public class ScheduleMembershipPlan extends ScheduleFactory {

	
	private String smpStartDayName;
	private String smpEndDayName;
	private String smpStartDayTime;
	private String smpEndDayTime;
	
	private String smpStatusStr;
	
	
	
	private List<ScheduleMembershipTimePlan> scheduleMembershipTimePlans;
	
	@Autowired
	ScheduleMembershipService scheduleMembershipService;
	
	@Autowired
	@Qualifier(value="programMembership")
	ProgramFactory programFactory;
	
	@Autowired
	ScheduleMembershipFacade scheduleMembershipFacade;
	
	@Override
	public HmiResultObj createPlan(ScheduleFactory scheduleFactory) {
		
		ProgramFactory programMembership=programFactory.findProgramById(scheduleFactory.getProgId());
		
		Date smpEndDate=OhbeUtil.getThatDayFormatNotNull(scheduleFactory.getSmpStartDateStr(), GlobalUtil.global.getPtDbDateFormat());
		if(programMembership.getProgDurationType()==ProgDurationTypes.DURATION_TYPE_DAILY){
			smpEndDate=ProgDurationTypes.getDateForNextDate(smpEndDate, programMembership.getProgDuration());
		}else if(programMembership.getProgDurationType()==ProgDurationTypes.DURATION_TYPE_WEEKLY){
			smpEndDate=ProgDurationTypes.getDateForNextDate(smpEndDate, programMembership.getProgDuration()*7);
		}else if(programMembership.getProgDurationType()==ProgDurationTypes.DURATION_TYPE_MONTHLY){
			smpEndDate=ProgDurationTypes.getDateForNextMonth(smpEndDate, programMembership.getProgDuration());
		}
		scheduleFactory.setSmpEndDate(smpEndDate);
		
		HmiResultObj hmiResultObj= scheduleMembershipFacade.canScheduleCreate(scheduleFactory);
		
		if(hmiResultObj.getResultStatu()==ResultStatuObj.RESULT_STATU_FAIL){
			return hmiResultObj;
		}
		
		hmiResultObj=scheduleMembershipService.createPlan(scheduleFactory);
		if(hmiResultObj.getResultStatu()==ResultStatuObj.RESULT_STATU_SUCCESS){
			long smpId=Long.parseLong(hmiResultObj.getResultMessage());
			ScheduleMembershipTimePlan scheduleMembershipTimePlan=new ScheduleMembershipTimePlan();
			scheduleMembershipTimePlan.setSmpId(smpId);
			scheduleMembershipTimePlan.setSmpStartDate(scheduleFactory.getSmpStartDate());
			scheduleMembershipTimePlan.setSmpEndDate(scheduleFactory.getSmpEndDate());
			scheduleMembershipTimePlan.setSmpComment(scheduleFactory.getSmpComment());
			scheduleMembershipService.createScheduleMembershipTimePlan(scheduleMembershipTimePlan);
		}
		
		return hmiResultObj;
	}


	@Override
	public ScheduleFactory findScheduleFactoryPlanById(long id) {
		return scheduleMembershipService.findSchedulePlanById(id);
	}


	@Override
	public List<SchedulePlan> findSchedulePlansbyUserId(long userId,long saleId) {
		// TODO Auto-generated method stub
		return null;
	}


	public String getSmpStartDayName() {
		return smpStartDayName;
	}


	public void setSmpStartDayName(String smpStartDayName) {
		this.smpStartDayName = smpStartDayName;
	}


	public String getSmpEndDayName() {
		return smpEndDayName;
	}


	public void setSmpEndDayName(String smpEndDayName) {
		this.smpEndDayName = smpEndDayName;
	}


	public String getSmpStartDayTime() {
		return smpStartDayTime;
	}


	public void setSmpStartDayTime(String smpStartDayTime) {
		this.smpStartDayTime = smpStartDayTime;
	}


	public String getSmpEndDayTime() {
		return smpEndDayTime;
	}


	public void setSmpEndDayTime(String smpEndDayTime) {
		this.smpEndDayTime = smpEndDayTime;
	}


	public String getSmpStatusStr() {
		return smpStatusStr;
	}


	public void setSmpStatusStr(String smpStatusStr) {
		this.smpStatusStr = smpStatusStr;
	}


	public List<ScheduleMembershipTimePlan> getScheduleMembershipTimePlans() {
		return scheduleMembershipTimePlans;
	}


	public void setScheduleMembershipTimePlans(List<ScheduleMembershipTimePlan> scheduleMembershipTimePlans) {
		this.scheduleMembershipTimePlans = scheduleMembershipTimePlans;
	}


	@Override
	public ScheduleObj createSchedule(ScheduleObj scheduleObj) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<ScheduleFactory> findSchedulesBySchId(long schId) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<ScheduleFactory> findSchedulesTimesBySchtId(long schtId) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public ScheduleObj updateSchedule(ScheduleObj scheduleObj) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public SchedulePlan findSchedulePlanBySaleId(long saleId) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public HmiResultObj deleteScheduleUsersPlan(ScheduleFactory scheduleFactory) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public ScheduleObj continueSchedule(ScheduleObj scheduleObj) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public ScheduleObj changeSchedule(ScheduleObj scheduleObj) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<ScheduleFactory> findUserSchedulesTimesBySaleId(long saleId) {
		// TODO Auto-generated method stub
		return null;
	}





	
}
