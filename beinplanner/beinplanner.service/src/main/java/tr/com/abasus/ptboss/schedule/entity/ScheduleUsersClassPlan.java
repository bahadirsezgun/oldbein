package tr.com.abasus.ptboss.schedule.entity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonTypeName;

import tr.com.abasus.ptboss.mail.facade.MailService;
import tr.com.abasus.ptboss.packetsale.entity.PacketSaleClass;
import tr.com.abasus.ptboss.packetsale.entity.PacketSaleFactory;
import tr.com.abasus.ptboss.program.entity.ProgramClass;
import tr.com.abasus.ptboss.program.entity.ProgramFactory;
import tr.com.abasus.ptboss.ptuser.entity.User;
import tr.com.abasus.ptboss.result.entity.HmiResultObj;
import tr.com.abasus.ptboss.schedule.businessEntity.ScheduleObj;
import tr.com.abasus.ptboss.schedule.businessService.ScheduleBusinessService;
import tr.com.abasus.ptboss.schedule.service.ScheduleClassService;
import tr.com.abasus.ptboss.schedule.service.ScheduleService;
import tr.com.abasus.util.ActionTypes;
import tr.com.abasus.util.DateTimeUtil;
import tr.com.abasus.util.GlobalUtil;
import tr.com.abasus.util.LangUtil;
import tr.com.abasus.util.ProgramTypes;
import tr.com.abasus.util.ResultStatuObj;
import tr.com.abasus.util.SaleStatus;
import tr.com.abasus.util.StatuTypes;

@Component(value="scheduleUsersClassPlan")
@Scope("prototype")
@JsonTypeName("sucp")
public class ScheduleUsersClassPlan extends ScheduleFactory {

	
	
	@Autowired
	ScheduleBusinessService scheduleBusinessService;
	
	@Autowired
	ScheduleClassService scheduleClassService;
	
	
	@Autowired
	ScheduleService scheduleService;
	
	@Autowired
	ProgramClass programClass;
	
	
	@Autowired
	@Qualifier(value="scheduleUsersPersonalPlan")
	ScheduleFactory scheduleFactory;
	
	@Autowired
	PacketSaleClass packetSaleClass;
	
	@Autowired
	MailService mailService;
	
	
	@Override
	public ScheduleObj createSchedule(ScheduleObj scheduleObj) {
		
		SchedulePlan schedulePlan=scheduleObj.getSchedulePlan();
		
		ProgramFactory programFactory= programClass.findProgramById(schedulePlan.getProgId());
		int progCount=programFactory.getProgCount();
		
		schedulePlan.setMaxMemberCount(programFactory.getMaxMemberCount());
		schedulePlan.setMinMemberCount(programFactory.getMinMemberCount());
		schedulePlan.setProgName(programFactory.getProgName());
		if(schedulePlan.getSchId()!=0){
			List<ScheduleTimePlan> schTP=scheduleService.findScheduleTimePlanByPlanId(schedulePlan.getSchId());
			
			int schtCount=0;
			for (ScheduleTimePlan scheduleTimePlan : schTP) {
				if(scheduleTimePlan.getStatuTp()!=StatuTypes.TIMEPLAN_POSTPONE){
					schtCount++;
				}
			}
			if(schTP!=null){
				progCount=progCount-schtCount;
			}
		}
		
		List<User> users=saleAutomaticPacketToUser(scheduleObj, programFactory);
		scheduleObj.setUsers(users);
		
		
		scheduleObj.setLeftProgCount(progCount);
		return scheduleBusinessService.createUpdateSchedule(scheduleObj, ProgramTypes.PROGRAM_CLASS,ActionTypes.ACTION_CREATE,programFactory);
	}
	
	
	private List<User> saleAutomaticPacketToUser(ScheduleObj scheduleObj,ProgramFactory programFactory){
		
		List<User> users=scheduleObj.getUsers();
		SchedulePlan schedulePlan=scheduleObj.getSchedulePlan();
		for (User user : users) {
			 if(user.getSaleId()==0){
				 PacketSaleFactory packetSaleFactory=new PacketSaleClass();
				 packetSaleFactory.setChangeDate(new Date());
				 packetSaleFactory.setUserId(user.getUserId());
				 packetSaleFactory.setProgId(schedulePlan.getProgId());
				 packetSaleFactory.setSalesComment("");
				 packetSaleFactory.setPacketPrice(programFactory.getProgPrice()*programFactory.getProgCount());
				 packetSaleFactory.setSalesDate(new Date());
				 packetSaleFactory.setStaffId(schedulePlan.getSchStaffId());
				 packetSaleFactory.setProgCount(programFactory.getProgCount());
				 packetSaleFactory.setBonusPayedFlag(0);
				 packetSaleFactory.setSaleStatu(SaleStatus.SALE_HAS_PLANNED);
				 
				 
				 HmiResultObj hmiResultObj= packetSaleClass.saleCreatePacket(packetSaleFactory);
				 if(hmiResultObj.getResultStatu()==ResultStatuObj.RESULT_STATU_FAIL){
					 throw new UnknownError();
				 }
				 
				 user.setSaleId(Long.parseLong(hmiResultObj.getResultMessage()));
				 user.setSaleCount(programFactory.getProgCount());
				 user.setProgId(schedulePlan.getProgId());
				 
				 packetSaleFactory.setUserName(user.getUserName());
				 packetSaleFactory.setUserSurname(user.getUserSurname());
				 packetSaleFactory.setProgName(schedulePlan.getProgName());
				 packetSaleFactory.setMailSubject(LangUtil.LANG_PACKET_SALE_SUBJECT);
				 packetSaleFactory.setMailContent(LangUtil.LANG_PACKET_SALE_AUTOMATIC);
				 packetSaleFactory.setSaledMembers(new ArrayList<User>());
				 packetSaleFactory.getSaledMembers().add(user);
				 packetSaleFactory.setSalesDateStr(DateTimeUtil.getDateStrByFormat(new Date(), GlobalUtil.global.getPtScrDateFormat()));
				 
				 try {
					mailService.sendMailForPacketSale(packetSaleFactory, user.getFirmId());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (MessagingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				 
			 }
		}
		
		return users;
	}
	
	
	@Override
	public ScheduleObj updateSchedule(ScheduleObj scheduleObj) {
		SchedulePlan schedulePlan=scheduleObj.getSchedulePlan();
		ProgramFactory programFactory= programClass.findProgramById(schedulePlan.getProgId());
		schedulePlan.setMaxMemberCount(programFactory.getMaxMemberCount());
		schedulePlan.setMinMemberCount(programFactory.getMinMemberCount());
		schedulePlan.setProgName(programFactory.getProgName());
		
		List<User> users=saleAutomaticPacketToUser(scheduleObj, programFactory);
		scheduleObj.setUsers(users);
		
		
		return scheduleBusinessService.createUpdateSchedule(scheduleObj, ProgramTypes.PROGRAM_CLASS,ActionTypes.ACTION_UPDATE,programFactory);
	}
	
	@Override
	public ScheduleObj changeSchedule(ScheduleObj scheduleObj) {
		SchedulePlan schedulePlan=scheduleObj.getSchedulePlan();
		ProgramFactory programFactory= programClass.findProgramById(schedulePlan.getProgId());
		schedulePlan.setMaxMemberCount(programFactory.getMaxMemberCount());
		schedulePlan.setMinMemberCount(programFactory.getMinMemberCount());
		schedulePlan.setProgName(programFactory.getProgName());
		
		return scheduleBusinessService.createUpdateSchedule(scheduleObj, ProgramTypes.PROGRAM_CLASS,ActionTypes.ACTION_CHANGE,programFactory);
	
	}
	
	@Override
	public ScheduleObj continueSchedule(ScheduleObj scheduleObj) {
		SchedulePlan schedulePlan=scheduleObj.getSchedulePlan();
		ProgramFactory programFactory= programClass.findProgramById(schedulePlan.getProgId());
		int progCount=programFactory.getProgCount();
		
		schedulePlan.setProgName(programFactory.getProgName());
		
		schedulePlan.setMaxMemberCount(programFactory.getMaxMemberCount());
		schedulePlan.setMinMemberCount(programFactory.getMinMemberCount());
		schedulePlan.setProgName(programFactory.getProgName());
	
			List<ScheduleTimePlan> schTP=scheduleService.findScheduleTimePlanByPlanId(schedulePlan.getSchId());
			int schtCount=0;
			for (ScheduleTimePlan scheduleTimePlan : schTP) {
				if(scheduleTimePlan.getStatuTp()!=StatuTypes.TIMEPLAN_POSTPONE){
					schtCount++;
				}
			}
			if(schTP!=null){
				progCount=progCount-schtCount;
			}
		
		scheduleObj.setLeftProgCount(progCount);
		
		List<User> users=saleAutomaticPacketToUser(scheduleObj, programFactory);
		scheduleObj.setUsers(users);
		
		
		return scheduleBusinessService.createUpdateSchedule(scheduleObj, ProgramTypes.PROGRAM_CLASS,ActionTypes.ACTION_ADD,programFactory);
	
	}
	
	
	@Override
	public HmiResultObj createPlan(ScheduleFactory scheduleFactory) {
		return null;
	}

	@Override
	public ScheduleFactory findScheduleFactoryPlanById(long id) {
		return null;
	}

	
	@Override
	public List<SchedulePlan> findSchedulePlansbyUserId(long userId,long saleId) {
		return scheduleClassService.findScheduleClassPlansbyUserId(userId,saleId);
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<ScheduleFactory> findSchedulesBySchId(long schId) {
		List<? extends ScheduleFactory> scheduleFactories=scheduleClassService.findScheduleUsersClassPlanByPlanId(schId);
		return (List<ScheduleFactory>)scheduleFactories;
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<ScheduleFactory> findSchedulesTimesBySchtId(long schtId) {
		List<? extends ScheduleFactory> scheduleFactories=scheduleClassService.findScheduleUsersClassPlanByTimePlanId(schtId);
		return (List<ScheduleFactory>)scheduleFactories;
	}

	@Override
	public SchedulePlan findSchedulePlanBySaleId(long saleId) {
		SchedulePlan schedulePlan=scheduleClassService.findSchedulePlanBySaleId(saleId);
		if(schedulePlan!=null){
			
				ProgramFactory programFactory= programClass.findProgramById(schedulePlan.getProgId());
				schedulePlan.setProgName(programFactory.getProgName());
			
		}
		
		
		return schedulePlan;
	}

	@Override
	public HmiResultObj deleteScheduleUsersPlan(ScheduleFactory scheduleFactory) {
		return scheduleClassService.deleteScheduleUsersClassPlan((ScheduleUsersClassPlan)scheduleFactory);
	}

	@Override
	public String getType() {
		return "sucp";
	}



	
	@SuppressWarnings("unchecked")
	@Override
	public List<ScheduleFactory> findUserSchedulesTimesBySaleId(long saleId) {
		List<? extends ScheduleFactory> scheduleFactories=scheduleClassService.usersClassPlansBySaleId(saleId);
		return  (List<ScheduleFactory>)scheduleFactories;
	}
	


	



	
	
}
