package tr.com.abasus.ptboss.schedule.businessService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tr.com.abasus.ptboss.facade.schedule.ScheduleClassFacade;
import tr.com.abasus.ptboss.packetsale.entity.PacketSaleClass;
import tr.com.abasus.ptboss.packetsale.service.PacketSaleService;
import tr.com.abasus.ptboss.program.entity.ProgramClass;
import tr.com.abasus.ptboss.program.entity.ProgramFactory;
import tr.com.abasus.ptboss.ptuser.entity.User;
import tr.com.abasus.ptboss.ptuser.service.ProcessUserService;
import tr.com.abasus.ptboss.result.entity.HmiResultObj;
import tr.com.abasus.ptboss.schedule.businessEntity.ScheduleObj;
import tr.com.abasus.ptboss.schedule.entity.ScheduleFactory;
import tr.com.abasus.ptboss.schedule.entity.SchedulePlan;
import tr.com.abasus.ptboss.schedule.entity.ScheduleStudios;
import tr.com.abasus.ptboss.schedule.entity.ScheduleTimePlan;
import tr.com.abasus.ptboss.schedule.entity.ScheduleUsersClassPlan;
import tr.com.abasus.ptboss.schedule.entity.ScheduleUsersPersonalPlan;
import tr.com.abasus.ptboss.schedule.service.ScheduleClassService;
import tr.com.abasus.ptboss.schedule.service.ScheduleService;
import tr.com.abasus.util.DateTimeUtil;
import tr.com.abasus.util.GlobalUtil;
import tr.com.abasus.util.OhbeUtil;
import tr.com.abasus.util.ProgramTypes;
import tr.com.abasus.util.ResultStatuObj;
import tr.com.abasus.util.SaleStatus;
import tr.com.abasus.util.StatuTypes;

@Service
public class ScheduleBusinessClassService {

	
	@Autowired
	ScheduleService scheduleService;
	
	
	@Autowired
	ScheduleClassService scheduleClassService;
	
	@Autowired
	ScheduleClassFacade scheduleClassFacade;
	
	
	@Autowired
	ProgramClass programClass;
	
	@Autowired 
	PacketSaleService packetSaleService;
	
	@Autowired 
	PacketSaleClass packetSaleClass;
	
	

	
	@Autowired 
	ProcessUserService processUserService;
	
	
	public ScheduleObj createScheduleClass(ScheduleObj scheduleObj,ProgramFactory programFactory){
		scheduleObj.generateTimePlans(programFactory);
		
		SchedulePlan schedulePlan=scheduleObj.getSchedulePlan();
		schedulePlan.setFirmId(scheduleObj.getFirmId());
		User mainStaff=processUserService.findById(schedulePlan.getSchStaffId());
		mainStaff.setPassword(null);
		schedulePlan.setUserName(mainStaff.getUserName());
		schedulePlan.setUserSurname(mainStaff.getUserSurname());
		schedulePlan.setProfileUrl(mainStaff.getProfileUrl());
		schedulePlan.setUrlType(mainStaff.getUrlType());
		
		HmiResultObj hmiResultObj=scheduleService.createSchedulePlan(schedulePlan);
		
		if(hmiResultObj.getResultStatu()==ResultStatuObj.RESULT_STATU_FAIL){
			return null;
		}
		
		long schId=Long.parseLong(hmiResultObj.getResultMessage());
		schedulePlan.setSchId(schId);
		
		
		List<ScheduleTimePlan> scheduleTimePlans=scheduleObj.getScheduleTimePlans();
		List<ScheduleStudios> scheduleStudios=scheduleObj.getScheduleStudios();
		int maxPlanCount=scheduleObj.getLeftProgCount();
		
		int planCount=1;	
		int i=0;
		for (ScheduleTimePlan scheduleTimePlan : scheduleTimePlans) {
			User staff=processUserService.findById(scheduleTimePlan.getSchtStaffId());
			scheduleTimePlan.setTpComment(scheduleObj.getTpComment());
			
			scheduleTimePlan.setStaff(staff);
			scheduleTimePlan.setSchtStaffId(staff.getUserId());
			scheduleTimePlan.setStaffName(staff.getUserName()+" "+staff.getUserSurname());
			scheduleTimePlan.setUserName(staff.getUserName());
			scheduleTimePlan.setUserSurname(staff.getUserSurname());
			scheduleTimePlan.setPlanDayName(DateTimeUtil.getDayNames(scheduleTimePlan.getPlanStartDate()));
			scheduleTimePlan.setPlanDayTime(DateTimeUtil.getHourMinute(scheduleTimePlan.getPlanStartDate()));
			scheduleTimePlan.setPlanStartDateStr(OhbeUtil.getDateStrByFormat(scheduleTimePlan.getPlanStartDate(), GlobalUtil.global.getPtScrDateFormat()));
			scheduleTimePlan.setPlanEndDateStr(OhbeUtil.getDateStrByFormat(scheduleTimePlan.getPlanEndDate(), GlobalUtil.global.getPtScrDateFormat()));
			scheduleTimePlan.setPlanEndDayTime(DateTimeUtil.getHourMinute(scheduleTimePlan.getPlanEndDate()));
			scheduleTimePlan.setProgType(ProgramTypes.PROGRAM_CLASS);
			
			hmiResultObj= scheduleClassFacade.canScheduleCreate(scheduleTimePlan.getPlanStartDate(), scheduleTimePlan.getSchtStaffId());
			
			
			generateScheduleClassUsers(scheduleObj, scheduleTimePlan,scheduleStudios);
			
			if(hmiResultObj.getResultStatu()==ResultStatuObj.RESULT_STATU_FAIL){
				
				 scheduleTimePlan.setPlanStatus(ResultStatuObj.RESULT_STATU_FAIL); 
		    	 scheduleTimePlan.setPlanStatusComment(hmiResultObj.getResultMessage());
				
			}else{
				
				hmiResultObj=scheduleClassFacade.canSchedulePlanProperToCreate(scheduleTimePlan, programFactory,schId);
				
				if(hmiResultObj.getResultStatu()==ResultStatuObj.RESULT_STATU_FAIL){
					
					 scheduleTimePlan.setPlanStatus(ResultStatuObj.RESULT_STATU_FAIL); 
			    	 scheduleTimePlan.setPlanStatusComment(hmiResultObj.getResultMessage());
					
					
				}else{
				
				
				
				scheduleTimePlan.setSchId(schId);
				scheduleTimePlan.setPlanCount(planCount);
				if(planCount>maxPlanCount){
					break;
				}
				planCount++;
				/**
				  * Tum uygunluklar kontrol edilir. 
				  */
				
				 
			     HmiResultObj hmiResultTimeObj=	scheduleClassFacade.isTimeProperToCreate(scheduleTimePlan,programFactory);
			     if(hmiResultTimeObj.getResultStatu()==ResultStatuObj.RESULT_STATU_FAIL){
			    	 scheduleTimePlan.setPlanStatus(ResultStatuObj.RESULT_STATU_FAIL); 
			    	 scheduleTimePlan.setPlanStatusComment(hmiResultTimeObj.getResultMessage());
			     }else{
			    	 scheduleTimePlan.setSchId(schedulePlan.getSchId());
			    	 HmiResultObj hmiResultCreateTimeObj=scheduleService.createScheduleTimePlan(scheduleTimePlan);
			    	 if(hmiResultCreateTimeObj.getResultStatu()==ResultStatuObj.RESULT_STATU_SUCCESS){
			    		 
			    		 long schtId=Long.parseLong(hmiResultCreateTimeObj.getResultMessage());
			    		 scheduleTimePlan.setSchtId(schtId);	
				    	 scheduleTimePlan.setPlanStatus(hmiResultCreateTimeObj.getResultStatu()); 
				    	 scheduleTimePlan.setPlanStatusComment(hmiResultCreateTimeObj.getResultMessage());
				    	 
				    	 
				    	 List<ScheduleFactory> scheduleUsersClassPlans=scheduleTimePlan.getUsers();
				    	 for (ScheduleFactory user : scheduleUsersClassPlans) {
				    		 user.setSchtId(schtId);
				    		 user.setType("sucp");
				    		 
				    		 if(user.getSaleCount()>i)
				    		   scheduleClassService.createScheduleUsersClassPlan((ScheduleUsersClassPlan)user);
				    	 }
			    	 
				    	 for (ScheduleStudios schStudio : scheduleTimePlan.getScheduleStudios()) {
				    		 schStudio.setSchtId(schtId);
				    		 scheduleService.createScheduleStudio(schStudio);
				    	 }
				    	 
				    	
				    	scheduleTimePlan.setPlanStatus(hmiResultCreateTimeObj.getResultStatu()); 
				    	scheduleTimePlan.setPlanStatusComment("timePlanCreated");
				    	  
			    	 }else{
			    		 scheduleTimePlan.setPlanStatus(hmiResultCreateTimeObj.getResultStatu()); 
				    	 scheduleTimePlan.setPlanStatusComment(hmiResultCreateTimeObj.getResultMessage());
				    }
			     }
			}
			     i++;
		
		  	
		}
		}
		
		if(commitScheduleClassControl(schedulePlan)){
			schedulePlan.setScheduleTimePlans(scheduleTimePlans);
			setPacketSaleClassStatu(schedulePlan);			
			return scheduleObj;
		}else{
			scheduleService.deleteSchedulePlan(schedulePlan);
			return null;
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public ScheduleObj addScheduleClass(ScheduleObj scheduleObj,ProgramFactory programFactory){
		scheduleObj.generateTimePlans(programFactory);
		
		SchedulePlan schedulePlan=scheduleObj.getSchedulePlan();
		List<ScheduleTimePlan> scheduleTimePlans=scheduleObj.getScheduleTimePlans();
		List<ScheduleStudios> scheduleStudios=scheduleObj.getScheduleStudios();
	 	
		List<ScheduleTimePlan> oldScheduleTimePlans=scheduleService.findScheduleTimePlanByPlanId(schedulePlan.getSchId());
		for (ScheduleTimePlan scheduleTimePlan : oldScheduleTimePlans) {
			List<? extends ScheduleFactory> scheduleFactories= scheduleClassService.findScheduleUsersClassPlanByTimePlanId(scheduleTimePlan.getSchtId());
			scheduleTimePlan.setUsers((List<ScheduleFactory>)scheduleFactories);
		}
		
		
		int alreadyPlannedCount=0;
		
		for (ScheduleTimePlan scheduleTimePlan : oldScheduleTimePlans) {
			if(scheduleTimePlan.getStatuTp()!=StatuTypes.TIMEPLAN_POSTPONE){
				alreadyPlannedCount++;
			}
		}
		
		
		int maxPlanCount=schedulePlan.getSchCount()-alreadyPlannedCount;
		
		int planCount=1;	
		int i=0;
		for (ScheduleTimePlan scheduleTimePlan : scheduleTimePlans) {
			User staff=processUserService.findById(scheduleTimePlan.getSchtStaffId());
			staff.setPassword(null);
			scheduleTimePlan.setTpComment(scheduleObj.getTpComment());
			
			scheduleTimePlan.setStaff(staff);
			scheduleTimePlan.setSchtStaffId(staff.getUserId());
			scheduleTimePlan.setStaffName(staff.getUserName()+" "+staff.getUserSurname());
			scheduleTimePlan.setUserName(staff.getUserName());
			scheduleTimePlan.setUserSurname(staff.getUserSurname());
			scheduleTimePlan.setPlanDayName(DateTimeUtil.getDayNames(scheduleTimePlan.getPlanStartDate()));
			scheduleTimePlan.setPlanDayTime(DateTimeUtil.getHourMinute(scheduleTimePlan.getPlanStartDate()));
			scheduleTimePlan.setPlanStartDateStr(OhbeUtil.getDateStrByFormat(scheduleTimePlan.getPlanStartDate(), GlobalUtil.global.getPtScrDateFormat()));
			scheduleTimePlan.setPlanEndDateStr(OhbeUtil.getDateStrByFormat(scheduleTimePlan.getPlanEndDate(), GlobalUtil.global.getPtScrDateFormat()));
			scheduleTimePlan.setPlanEndDayTime(DateTimeUtil.getHourMinute(scheduleTimePlan.getPlanEndDate()));
			
			scheduleTimePlan.setPlanCount(planCount);
			if(planCount>maxPlanCount){
				break;
			}
			planCount++;
			
			generateScheduleClassUsers(scheduleObj, scheduleTimePlan,scheduleStudios);
			
			/**
			  * Tum uygunluklar kontrol edilir. 
			  */
			 HmiResultObj hmiResultObj= scheduleClassFacade.canScheduleCreate(scheduleTimePlan.getPlanStartDate(), scheduleTimePlan.getSchtStaffId());
				
			 if(hmiResultObj.getResultStatu()==ResultStatuObj.RESULT_STATU_FAIL){
					
				 scheduleTimePlan.setPlanStatus(ResultStatuObj.RESULT_STATU_FAIL); 
		    	 scheduleTimePlan.setPlanStatusComment(hmiResultObj.getResultMessage());
		    	 scheduleObj.getSchedulePlan().getScheduleTimePlans().add(scheduleTimePlan);
			}else{
			
				hmiResultObj=scheduleClassFacade.canSchedulePlanProperToCreate(scheduleTimePlan, programFactory,schedulePlan.getSchId());
				
				if(hmiResultObj.getResultStatu()==ResultStatuObj.RESULT_STATU_FAIL){
					
					 scheduleTimePlan.setPlanStatus(ResultStatuObj.RESULT_STATU_FAIL); 
			    	 scheduleTimePlan.setPlanStatusComment(hmiResultObj.getResultMessage());
			    	 scheduleObj.getSchedulePlan().getScheduleTimePlans().add(scheduleTimePlan);
					
				}else{
				 
			     HmiResultObj hmiResultTimeObj=	scheduleClassFacade.isTimeProperToCreate(scheduleTimePlan,programFactory);
			     
			     if(hmiResultTimeObj.getResultStatu()==ResultStatuObj.RESULT_STATU_FAIL){
			    	 scheduleTimePlan.setPlanStatus(ResultStatuObj.RESULT_STATU_FAIL); 
			    	 scheduleTimePlan.setPlanStatusComment(hmiResultTimeObj.getResultMessage());
			     }else{
			    	 
			    	  
			    	 scheduleTimePlan.setSchId(schedulePlan.getSchId());
			    	 HmiResultObj hmiResultCreateTimeObj=scheduleService.createScheduleTimePlan(scheduleTimePlan);
			    	 if(hmiResultCreateTimeObj.getResultStatu()==ResultStatuObj.RESULT_STATU_SUCCESS){
			    		 
			    		 long schtId=Long.parseLong(hmiResultCreateTimeObj.getResultMessage());
			    		 scheduleTimePlan.setSchtId(schtId);	
				    	 scheduleTimePlan.setPlanStatus(hmiResultCreateTimeObj.getResultStatu()); 
				    	 scheduleTimePlan.setPlanStatusComment(hmiResultCreateTimeObj.getResultMessage());
				    	 
				    	 
				    	 List<ScheduleFactory> scheduleUsersClassPlans=scheduleTimePlan.getUsers();
				    	 for (ScheduleFactory user : scheduleUsersClassPlans) {
				    		 user.setSchtId(schtId);
				    		 user.setType("sucp");
				    		 int userCount=0;
				    		 
				    		 for (ScheduleTimePlan oldSTP : oldScheduleTimePlans) {
				    			 if(oldSTP.getStatuTp()!=StatuTypes.TIMEPLAN_POSTPONE){
					    			List<ScheduleFactory> oldSFS=oldSTP.getUsers();
									for (ScheduleFactory oldUser : oldSFS) {
					    			   if(oldUser.getUserId()==user.getUserId()){
											userCount++;
										}
							        }
				    			 }
				    		 }
				    		
				    		 
				    		 int saleCount=user.getSaleCount()-userCount;
				    		 if(saleCount>i)
				    		   scheduleClassService.createScheduleUsersClassPlan((ScheduleUsersClassPlan)user);
				    	 }
			    	 
				    	 for (ScheduleStudios schStudio : scheduleTimePlan.getScheduleStudios()) {
				    		 schStudio.setSchtId(schtId);
				    		 scheduleService.createScheduleStudio(schStudio);
				    	 }
				    	 
				    	
				    	scheduleTimePlan.setPlanStatus(hmiResultCreateTimeObj.getResultStatu()); 
				    	scheduleTimePlan.setPlanStatusComment("timePlanCreated");
				    	  
			    	 }else{
			    		 scheduleTimePlan.setPlanStatus(hmiResultCreateTimeObj.getResultStatu()); 
				    	 scheduleTimePlan.setPlanStatusComment(hmiResultCreateTimeObj.getResultMessage());
				    }
			     }
			     scheduleObj.getSchedulePlan().getScheduleTimePlans().add(scheduleTimePlan);
			     
			     i++;
			}
		   }
		}

		    
		
		
			setPacketSaleClassStatu(schedulePlan);			
			return scheduleObj;


	}
	
	
	
	
	
	
	
	
	
	public boolean setPacketSaleClassStatu(SchedulePlan schedulePlan){
		List<User> usersInPlan=scheduleClassService.findUsersInClassPlanToGroup(schedulePlan.getSchId());
		for (User user : usersInPlan) {
			List<ScheduleUsersClassPlan> scheduleUsersClassPlans=scheduleClassService.findScheduleUsersClassPlanByPlanIdAndUserId(schedulePlan.getSchId(), user.getUserId());
			
			int userInCount=0;
			
			for (ScheduleUsersClassPlan scheduleUsersClassPlan : scheduleUsersClassPlans) {
				if(scheduleUsersClassPlan.getStatuTp()<StatuTypes.TIMEPLAN_POSTPONE){
					userInCount++;
				}
			}
			
			
			PacketSaleClass psc=(PacketSaleClass)packetSaleClass.findSaledPacketsById(user.getSaleId());
			
			if(psc.getProgCount()>userInCount){
				psc.setSaleStatu(SaleStatus.SALE_HAS_PLANNED);
			}else{
				psc.setSaleStatu(SaleStatus.SALE_FINISHED_PLANNED);
			}
			packetSaleService.buyClassPacket(psc);
		}
		return true;
	}
	
	public void generateScheduleClassUsers(ScheduleObj scheduleObj,ScheduleTimePlan scheduleTimePlan,List<ScheduleStudios> scheduleStudios){
		 List<User> users=scheduleObj.getUsers();
	   	 //if(scheduleTimePlan.getUsers()==null)
	   	
		 scheduleTimePlan.setUsers(new ArrayList<ScheduleFactory>());
	   	 
		 for (User user : users) {
	   		 ScheduleUsersClassPlan scheduleUsersClassPlan=new ScheduleUsersClassPlan();
	   		 scheduleUsersClassPlan.setSaleId(user.getSaleId());
	   		 scheduleUsersClassPlan.setUserId(user.getUserId());
	   		 scheduleUsersClassPlan.setUserName(user.getUserName());
	   		 scheduleUsersClassPlan.setUserSurname(user.getUserSurname());
	   		 scheduleUsersClassPlan.setSchtId(scheduleTimePlan.getSchtId());
	   		 scheduleUsersClassPlan.setProfileUrl(user.getProfileUrl());
	   		 scheduleUsersClassPlan.setUserGender(user.getUserGender());
	   		 
	   		 scheduleUsersClassPlan.setUrlType(user.getUrlType());
	   		 scheduleUsersClassPlan.setSaleCount(user.getSaleCount());
	   		 scheduleUsersClassPlan.setType("sucp");
	   		 scheduleTimePlan.getUsers().add(scheduleUsersClassPlan);
	   		 
	   	 }
	   	 
	   	 
		 if(scheduleTimePlan.getScheduleStudios()==null)
		   		scheduleTimePlan.setScheduleStudios(new ArrayList<ScheduleStudios>());
		   	 
		if(scheduleStudios!=null){ 
		   	for (ScheduleStudios schStudio : scheduleStudios) {
	    		ScheduleStudios scStudios= new ScheduleStudios();
	    		scStudios.setChangeDate(new Date());
	    		scStudios.setSchtId(scheduleTimePlan.getSchtId());
	    		scStudios.setStudioId(schStudio.getStudioId());
	    		scStudios.setStudioName(schStudio.getStudioName());
	    		scStudios.setStudioShortName(schStudio.getStudioShortName());
	    		scheduleTimePlan.getScheduleStudios().add(scStudios);
			}
	   	}
	   	 
	}
	
	public boolean commitScheduleClassControl(SchedulePlan schedulePlan){
		 List<ScheduleTimePlan> scheduleTimePlans= scheduleService.findScheduleTimePlanByPlanId(schedulePlan.getSchId());
		 if(scheduleTimePlans.size()>0){
			 List<ScheduleUsersClassPlan> scheduleUsersClassPlans=scheduleClassService.findScheduleUsersClassPlanByPlanId(schedulePlan.getSchId());
			 if(scheduleUsersClassPlans.size()>0){
				 return true;
			 }else{
				 return false;
			 }
		 }else{
			 return false;
		 }
	}
	
	
	public ScheduleObj updateScheduleClass(ScheduleObj scheduleObj,ProgramFactory programFactory){
		
		 int progDuration=GlobalUtil.defCalendarTimes.getDuration();
			
		scheduleObj.setHmiResultObjs(new ArrayList<HmiResultObj>());
		List<ScheduleTimePlan> scheduleTimePlans=scheduleObj.getSchedulePlan().getScheduleTimePlans();
		
		//for (ScheduleTimePlan scheduleTimePlan : scheduleTimePlans) {
		    ScheduleTimePlan scheduleTimePlan=scheduleObj.getScheduleTimePlanForUpdate();
		    scheduleTimePlan.setTpComment(scheduleObj.getTpComment());
			
		    ScheduleTimePlan sctpInDb=null;
			if(scheduleTimePlan.getSchtId()!=0){
				for (ScheduleTimePlan inSchTP : scheduleTimePlans) {
					if(inSchTP.getSchtId()==scheduleTimePlan.getSchtId()){
						sctpInDb=inSchTP;
						break;
					}
		    	}
			}
			
			if(scheduleTimePlan.getSchId()==0){
				SchedulePlan schedulePlan=scheduleObj.getSchedulePlan();
				scheduleTimePlan.setSchId(schedulePlan.getSchId());
			}
			

			 if(scheduleTimePlan.getScheduleStudios()==null)
			   		scheduleTimePlan.setScheduleStudios(new ArrayList<ScheduleStudios>());
			   	 
			String changeDateTo=scheduleObj.getPlanStartDateStr();
			String changeTimeTo=scheduleObj.getPlanStartDateTime();
			changeDateTo=changeDateTo+" "+changeTimeTo;
			Date changeDate=OhbeUtil.getThatDayFormatNotNull(changeDateTo, GlobalUtil.global.getPtDbDateFormat()+" HH:mm");
			scheduleTimePlan.setPlanStartDate(changeDate);
			scheduleTimePlan.setPlanDayTime(DateTimeUtil.getHourMinute(scheduleTimePlan.getPlanStartDate()));
			scheduleTimePlan.setPlanStartDateStr(scheduleObj.getPlanStartDateStr());
			
			
			
			Date endDate=(Date)changeDate.clone();
			scheduleTimePlan.setPlanEndDate(OhbeUtil.getDateForNextMinute(endDate, progDuration));
			
			scheduleTimePlan.setPlanEndDayTime(DateTimeUtil.getHourMinute(scheduleTimePlan.getPlanEndDate()));
			
			HmiResultObj hmiResultObj= scheduleClassFacade.canScheduleChange(scheduleTimePlan.getSchtId());
			
			
			
			if(hmiResultObj.getResultStatu()==ResultStatuObj.RESULT_STATU_FAIL){
				 
				
				 scheduleObj.getHmiResultObjs().add(hmiResultObj);
				 scheduleTimePlan.setPlanStatus(ResultStatuObj.RESULT_STATU_FAIL); 
		    	 scheduleTimePlan.setPlanStatusComment(hmiResultObj.getResultMessage());
		    	 
				 if(sctpInDb!=null){
					 scheduleTimePlan.setPlanDayName(DateTimeUtil.getDayNames(sctpInDb.getPlanStartDate()));
					 scheduleTimePlan.setPlanDayTime(DateTimeUtil.getHourMinute(sctpInDb.getPlanStartDate()));
					 scheduleTimePlan.setPlanEndDayTime(DateTimeUtil.getHourMinute(sctpInDb.getPlanEndDate()));
					 scheduleTimePlan.setSchtStaffId(sctpInDb.getSchtStaffId());
					 User staff=processUserService.findById(scheduleTimePlan.getSchtStaffId());
					 staff.setPassword(null);
					 scheduleTimePlan.setStaff(staff);
					 scheduleTimePlan.setUsers(sctpInDb.getUsers());
					 scheduleTimePlan.setScheduleStudios(sctpInDb.getScheduleStudios());
					 
				 }else{
					 scheduleTimePlan.setPlanDayName(DateTimeUtil.getDayNames(scheduleTimePlan.getPlanStartDate()));
					 scheduleTimePlan.setPlanDayTime(DateTimeUtil.getHourMinute(scheduleTimePlan.getPlanStartDate()));
					 scheduleTimePlan.setPlanEndDayTime(DateTimeUtil.getHourMinute(scheduleTimePlan.getPlanEndDate()));
				 }
		    	 
			}else{
				
				
				HmiResultObj hmiResultTimeObj=	scheduleClassFacade.isTimeProperToCreate(scheduleTimePlan,programFactory);
			    
				 if(hmiResultTimeObj.getResultStatu()==ResultStatuObj.RESULT_STATU_FAIL){
					 if(sctpInDb!=null){
						 scheduleTimePlan.setPlanDayName(DateTimeUtil.getDayNames(sctpInDb.getPlanStartDate()));
						 scheduleTimePlan.setPlanDayTime(DateTimeUtil.getHourMinute(sctpInDb.getPlanStartDate()));
						 scheduleTimePlan.setPlanEndDayTime(DateTimeUtil.getHourMinute(sctpInDb.getPlanEndDate()));
						 scheduleTimePlan.setSchtStaffId(sctpInDb.getSchtStaffId());
						 
						 User staff=processUserService.findById(scheduleTimePlan.getSchtStaffId());
						 staff.setPassword(null);
						 scheduleTimePlan.setStaff(staff);
						 
						 scheduleTimePlan.setUsers(sctpInDb.getUsers());
						 scheduleTimePlan.setScheduleStudios(sctpInDb.getScheduleStudios());
						 
					 }else{
						 scheduleTimePlan.setPlanDayName(DateTimeUtil.getDayNames(scheduleTimePlan.getPlanStartDate()));
						 scheduleTimePlan.setPlanDayTime(DateTimeUtil.getHourMinute(scheduleTimePlan.getPlanStartDate()));
						 scheduleTimePlan.setPlanEndDayTime(DateTimeUtil.getHourMinute(scheduleTimePlan.getPlanEndDate()));
					 }
					 
					 scheduleTimePlan.setPlanStatus(ResultStatuObj.RESULT_STATU_FAIL); 
			    	 scheduleTimePlan.setPlanStatusComment(hmiResultTimeObj.getResultMessage());
			    	
			     }else{
			    	 
			    	hmiResultObj=scheduleClassFacade.canSchedulePlanProperToCreate(scheduleTimePlan, programFactory,scheduleTimePlan.getSchId());
						
					if(hmiResultObj.getResultStatu()==ResultStatuObj.RESULT_STATU_FAIL){
						 
						
						if(sctpInDb!=null){
							 scheduleTimePlan.setPlanDayName(DateTimeUtil.getDayNames(sctpInDb.getPlanStartDate()));
							 scheduleTimePlan.setPlanDayTime(DateTimeUtil.getHourMinute(sctpInDb.getPlanStartDate()));
							 scheduleTimePlan.setPlanEndDayTime(DateTimeUtil.getHourMinute(sctpInDb.getPlanEndDate()));
							 scheduleTimePlan.setSchtStaffId(sctpInDb.getSchtStaffId());
							 
							 User staff=processUserService.findById(scheduleTimePlan.getSchtStaffId());
							 staff.setPassword(null);
							 scheduleTimePlan.setStaff(staff);
							 
							 scheduleTimePlan.setUsers(sctpInDb.getUsers());
							 scheduleTimePlan.setScheduleStudios(sctpInDb.getScheduleStudios());
							 
						 }else{
							 scheduleTimePlan.setPlanDayName(DateTimeUtil.getDayNames(scheduleTimePlan.getPlanStartDate()));
							 scheduleTimePlan.setPlanDayTime(DateTimeUtil.getHourMinute(scheduleTimePlan.getPlanStartDate()));
							 scheduleTimePlan.setPlanEndDayTime(DateTimeUtil.getHourMinute(scheduleTimePlan.getPlanEndDate()));
						 }
						
						
						 scheduleTimePlan.setPlanStatus(ResultStatuObj.RESULT_STATU_FAIL); 
				    	 scheduleTimePlan.setPlanStatusComment(hmiResultObj.getResultMessage());
					}else{	
							long oldSchtId=scheduleTimePlan.getSchtId();
					    	 if(scheduleTimePlan.getSchtId()!=0){
					    		 ////System.out.println("ScheduleTimePlan Deleted "+oldSchtId);
					    		 
								scheduleService.deleteJustScheduleTimePlan(scheduleTimePlan);
					    	 }
								
					    	    scheduleTimePlan.setSchtId(0);
					    	    
					    	    User staff=processUserService.findById(scheduleTimePlan.getStaff().getUserId());
					    	    
					    	    scheduleTimePlan.setStaff(staff);
					    	    scheduleTimePlan.setSchtStaffId(staff.getUserId());
					    	    scheduleTimePlan.setUserName(staff.getUserName());
					    	    scheduleTimePlan.setUserSurname(staff.getUserSurname());
					    	    
					    	    scheduleTimePlan.setPlanDayName(DateTimeUtil.getDayNames(scheduleTimePlan.getPlanStartDate()));
								scheduleTimePlan.setPlanDayTime(DateTimeUtil.getHourMinute(scheduleTimePlan.getPlanStartDate()));
								scheduleTimePlan.setPlanEndDayTime(DateTimeUtil.getHourMinute(scheduleTimePlan.getPlanEndDate()));
					    	    
					    	    
								hmiResultObj= scheduleService.createScheduleTimePlan(scheduleTimePlan);
								scheduleTimePlan.setSchtId(Long.parseLong(hmiResultObj.getResultMessage()));
								scheduleObj.getHmiResultObjs().add(hmiResultObj);
								List<ScheduleStudios> scheduleStudios=scheduleTimePlan.getScheduleStudios();
								for (ScheduleStudios scheduleStudio : scheduleStudios) {
									scheduleStudio.setSchtId(scheduleTimePlan.getSchtId());
									scheduleStudio.setSchsId(0);
									HmiResultObj hmiRU=scheduleService.createScheduleStudio(scheduleStudio);
									scheduleStudio.setSchsId(Long.parseLong(hmiRU.getResultMessage()));
								}
								
								//KONTROL EDILMELI SAKIN KAFA ILE
								generateScheduleClassUsers(scheduleObj, scheduleTimePlan, scheduleStudios);
							
								
								
								List<ScheduleFactory> scheduleFactories=scheduleTimePlan.getUsers();
								for (ScheduleFactory scheduleFactory : scheduleFactories) {
									
									
									scheduleFactory.setSuppId(0);
									scheduleFactory.setSchtId(scheduleTimePlan.getSchtId());
									scheduleFactory.setType("supp");
									HmiResultObj hmiRU= scheduleClassService.createScheduleUsersClassPlan((ScheduleUsersClassPlan)scheduleFactory);
									scheduleFactory.setSuppId(Long.parseLong(hmiRU.getResultMessage()));
								}
								/*
								scheduleTimePlan.setScheduleStudios(scheduleStudios);
								scheduleTimePlan.setUsers(scheduleFactories);
								*/
								scheduleTimePlan.setPlanStatus(hmiResultObj.getResultStatu()); 
						    	scheduleTimePlan.setPlanStatusComment("planUpdated");
						    	
						    	
		
						    	int i=0;
						    	for (ScheduleTimePlan inSchTP : scheduleTimePlans) {
									if(inSchTP.getSchtId()==oldSchtId){
										//inSchTP=scheduleTimePlan;
										scheduleTimePlans.set(i, scheduleTimePlan);
										break;
									}
									i++;
						    	}
						    	
			     }  	
			     }
			}
		//}
		
			
			for (ScheduleTimePlan scheduleTimePlan2 : scheduleTimePlans) {
				
				////System.out.println("SCHID . "+scheduleTimePlan2.getSchId()+"SCHTID . "+scheduleTimePlan2.getSchtId()+"Time : "+scheduleTimePlan2.getPlanDayTime()+"  "+"Time : "+scheduleTimePlan2.getPlanStartDateStr());
			}
			
			
			scheduleObj.setScheduleTimePlanForUpdate(scheduleTimePlan);
			scheduleObj.getSchedulePlan().setScheduleTimePlans(scheduleTimePlans);
			scheduleObj.setScheduleTimePlans(scheduleTimePlans);
		    
		//scheduleObj.setAllScheduleTimePlans(getAllScheduleTimePlansInDBForPlan(scheduleObj.getSchedulePlan()));
		/*
		ScheduleTimePlan uStp=scheduleTimePlans.get(0);
		if(uStp.getPlanStatus()==ResultStatuObj.RESULT_STATU_FAIL){
			int i=0;
			for (ScheduleTimePlan tp : scheduleObj.getAllScheduleTimePlans()) {
				if(tp.getSchtId()==uStp.getSchtId()){
					scheduleObj.getAllScheduleTimePlans().set(i, uStp);
					break;
				}
				i++;
			}
		}
		*/
		
		
		setPacketSaleClassStatu(scheduleObj.getSchedulePlan());
		
		return scheduleObj;
	}
	
	
	
	 public ScheduleObj changeScheduleClass(ScheduleObj scheduleObj,ProgramFactory programFactory){
			
		 int progDuration=GlobalUtil.defCalendarTimes.getDuration();
			
			scheduleObj.setHmiResultObjs(new ArrayList<HmiResultObj>());
			
			//for (ScheduleTimePlan scheduleTimePlan : scheduleTimePlans) {
			    ScheduleTimePlan scheduleTimePlan=scheduleObj.getScheduleTimePlanForUpdate();
			    scheduleTimePlan.setTpComment(scheduleObj.getTpComment());
				
			    ScheduleTimePlan sctpInDb=scheduleService.findScheduleTimePlanById(scheduleTimePlan.getSchtId());
			    List<? extends ScheduleFactory> scheduleUsersClassPlans= scheduleClassService.findScheduleUsersClassPlanByTimePlanId(sctpInDb.getSchtId());
			    @SuppressWarnings("unchecked")
				List<ScheduleFactory> scheduleFactoriesInDb=(List<ScheduleFactory>)scheduleUsersClassPlans;
			    sctpInDb.setUsers(scheduleFactoriesInDb);
			    List<ScheduleStudios> scheduleStudiosInDb= scheduleService.findScheduleStudiosByTimePlanId(sctpInDb.getSchtId());
			    sctpInDb.setScheduleStudios(scheduleStudiosInDb);
			    
			   
				String changeDateTo=scheduleObj.getPlanStartDateStr();
				String changeTimeTo=scheduleObj.getPlanStartDateTime();
				
				changeDateTo=changeDateTo+" "+changeTimeTo;
				Date changeDate=OhbeUtil.getThatDayFormatNotNull(changeDateTo, GlobalUtil.global.getPtDbDateFormat()+" HH:mm");
				scheduleTimePlan.setPlanStartDate(changeDate);
				scheduleTimePlan.setPlanDayTime(DateTimeUtil.getHourMinute(scheduleTimePlan.getPlanStartDate()));
				
				Date endDate=(Date)changeDate.clone();
				scheduleTimePlan.setPlanEndDate(OhbeUtil.getDateForNextMinute(endDate,progDuration));
				scheduleTimePlan.setPlanEndDayTime(DateTimeUtil.getHourMinute(scheduleTimePlan.getPlanEndDate()));
				
				HmiResultObj hmiResultObj= scheduleClassFacade.canScheduleChange(scheduleTimePlan.getSchtId());
				
				
				
				if(hmiResultObj.getResultStatu()==ResultStatuObj.RESULT_STATU_FAIL){
					 scheduleObj.getHmiResultObjs().add(hmiResultObj);
					 scheduleTimePlan.setPlanStatus(ResultStatuObj.RESULT_STATU_FAIL); 
			    	 scheduleTimePlan.setPlanStatusComment(hmiResultObj.getResultMessage());
			    }else{
					
			    	HmiResultObj hmiResultTimeObj=	scheduleClassFacade.isTimeProperToCreate(scheduleTimePlan,programFactory);
				    
					 if(hmiResultTimeObj.getResultStatu()==ResultStatuObj.RESULT_STATU_FAIL){	
						 scheduleTimePlan.setPlanStatus(ResultStatuObj.RESULT_STATU_FAIL); 
				    	 scheduleTimePlan.setPlanStatusComment(hmiResultTimeObj.getResultMessage());
				    	
				     }else{
				    	 
				    	 hmiResultObj=scheduleClassFacade.canSchedulePlanProperToCreate(scheduleTimePlan, programFactory,scheduleTimePlan.getSchId());
							
							if(hmiResultObj.getResultStatu()==ResultStatuObj.RESULT_STATU_FAIL){
								 
									 scheduleTimePlan.setPlanDayName(DateTimeUtil.getDayNames(sctpInDb.getPlanStartDate()));
									 scheduleTimePlan.setPlanDayTime(DateTimeUtil.getHourMinute(sctpInDb.getPlanStartDate()));
									 scheduleTimePlan.setPlanEndDayTime(DateTimeUtil.getHourMinute(sctpInDb.getPlanEndDate()));
									 scheduleTimePlan.setSchtStaffId(sctpInDb.getSchtStaffId());
									 
									 User staff=processUserService.findById(scheduleTimePlan.getSchtStaffId());
									 staff.setPassword(null);
									 scheduleTimePlan.setStaff(staff);
									 
									 scheduleTimePlan.setUsers(sctpInDb.getUsers());
									 scheduleTimePlan.setScheduleStudios(sctpInDb.getScheduleStudios());
									 scheduleTimePlan.setPlanStatus(ResultStatuObj.RESULT_STATU_FAIL); 
									 scheduleTimePlan.setPlanStatusComment(hmiResultObj.getResultMessage());
							}else{
				    	 
				    	 
				    	 
				    	 
				    	 
				    	 
				    	 
							scheduleService.deleteJustScheduleTimePlan(scheduleTimePlan);
							scheduleTimePlan.setSchtId(0);
				    	    
							hmiResultObj= scheduleService.createScheduleTimePlan(scheduleTimePlan);
							scheduleTimePlan.setSchtId(Long.parseLong(hmiResultObj.getResultMessage()));
							scheduleObj.getHmiResultObjs().add(hmiResultObj);
							
							List<ScheduleStudios> scheduleStudios=scheduleTimePlan.getScheduleStudios();
							for (ScheduleStudios scheduleStudio : scheduleStudios) {
								scheduleStudio.setSchtId(scheduleTimePlan.getSchtId());
								scheduleStudio.setSchsId(0);
								HmiResultObj hmiRU=scheduleService.createScheduleStudio(scheduleStudio);
								scheduleStudio.setSchsId(Long.parseLong(hmiRU.getResultMessage()));
							}
							List<ScheduleFactory> scheduleFactories=scheduleTimePlan.getUsers();
							for (ScheduleFactory scheduleFactory : scheduleFactories) {
								
								
								scheduleFactory.setSuppId(0);
								scheduleFactory.setSchtId(scheduleTimePlan.getSchtId());
								scheduleFactory.setType("supp");
								HmiResultObj hmiRU= scheduleClassService.createScheduleUsersClassPlan((ScheduleUsersClassPlan)scheduleFactory);
								scheduleFactory.setSuppId(Long.parseLong(hmiRU.getResultMessage()));
							}
							
							scheduleTimePlan.setPlanStatus(hmiResultObj.getResultStatu()); 
					    	scheduleTimePlan.setPlanStatusComment("planUpdated");
						}
				     }
				}
		
			
			return scheduleObj;
		}
	
	
	
	/*
	@SuppressWarnings("unchecked")
	private List<ScheduleTimePlan> getAllScheduleTimePlansInDBForPlan(SchedulePlan schedulePlan){
		List<ScheduleTimePlan> allScheduleTimePlans=scheduleService.findScheduleTimePlanByPlanId(schedulePlan.getSchId());
		int planCount=0;
		for (ScheduleTimePlan scheduleTimePlan : allScheduleTimePlans) {
			scheduleTimePlan.setPlanCount(planCount++);
			scheduleTimePlan.setPlanDayName(DateTimeUtil.getDayNames(scheduleTimePlan.getPlanStartDate()));
			scheduleTimePlan.setPlanDayTime(DateTimeUtil.getHourMinute(scheduleTimePlan.getPlanStartDate()));
			scheduleTimePlan.setPlanEndDayTime(DateTimeUtil.getHourMinute(scheduleTimePlan.getPlanEndDate()));
			User staff=processUserService.findById(scheduleTimePlan.getSchtStaffId());
    	     scheduleTimePlan.setStaff(staff);
    	    scheduleTimePlan.setProgName(schedulePlan.getProgName());
    	    
    	    List<ScheduleStudios> scheduleStudios=scheduleService.findScheduleStudiosByTimePlanId(scheduleTimePlan.getSchtId());
			List<? extends ScheduleFactory> scheduleFactories=scheduleClassService.findScheduleUsersClassPlanByTimePlanId(scheduleTimePlan.getSchtId());
			
			scheduleTimePlan.setScheduleStudios(scheduleStudios);
			scheduleTimePlan.setUsers((List<ScheduleFactory>)scheduleFactories);
			scheduleTimePlan.setPlanStatusComment("timePlanCreated");
		}
		return allScheduleTimePlans;
	}
	*/
}
