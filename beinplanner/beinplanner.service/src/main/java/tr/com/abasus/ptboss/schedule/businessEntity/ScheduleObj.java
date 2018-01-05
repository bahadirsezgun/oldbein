package tr.com.abasus.ptboss.schedule.businessEntity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import tr.com.abasus.ptboss.program.entity.ProgramFactory;
import tr.com.abasus.ptboss.ptuser.entity.User;
import tr.com.abasus.ptboss.result.entity.HmiResultObj;
import tr.com.abasus.ptboss.schedule.entity.SchedulePlan;
import tr.com.abasus.ptboss.schedule.entity.ScheduleStudios;
import tr.com.abasus.ptboss.schedule.entity.ScheduleTimePlan;
import tr.com.abasus.util.DateTimeUtil;
import tr.com.abasus.util.GlobalUtil;
import tr.com.abasus.util.OhbeUtil;
import tr.com.abasus.util.TimeTypes;

public class ScheduleObj {

	
	private int 		firmId; //GET FROM SESSION USER
	private int 		leftProgCount; // CALCULATE AND SET BEFORE GENERATETIMEPLANS
	private String 		staffName;
	
	private int 		timeType;
	private int 		periodCount;
	private Date 		planStartDate;
	private String 		planStartDateStr;
	private String 		planStartDateTime;
	
	private SchedulePlan 			schedulePlan;
	private List<ScheduleStudios> 	scheduleStudios;
	private List<User> 				users;
	private List<ScheduleTimeObj> 	scheduleTimeObjs;
	
	private List<ScheduleTimePlan> 	scheduleTimePlans; // GENERATE FROM scheduleTimeObjs
	private List<ScheduleTimePlan> 	allScheduleTimePlans; // GENERATE FROM scheduleTimeObjs
	
	private List<HmiResultObj> 		hmiResultObjs;
	
	private ScheduleTimePlan 		scheduleTimePlanForUpdate;
	
	private String tpComment;
	
	public void generateTimePlans(ProgramFactory programFactory){
		scheduleTimePlans=new ArrayList<ScheduleTimePlan>();
		
		int progDuration=GlobalUtil.defCalendarTimes.getDuration();
		
		if(timeType==TimeTypes.TIME_TYPE_NO_PERIOD){
			for (ScheduleTimeObj scheduleTimeObj : scheduleTimeObjs) {
				
				ScheduleTimePlan scheduleTimePlan=new ScheduleTimePlan();
				scheduleTimePlan.setPlanStartDate(OhbeUtil.getThatDayFormatNotNull(planStartDateStr+" "+scheduleTimeObj.getProgStartTime(), GlobalUtil.global.getPtDbDateFormat()+" "+"HH:mm"));
				Date endDate=OhbeUtil.getThatDayFormatNotNull(planStartDateStr+" "+scheduleTimeObj.getProgStartTime(), GlobalUtil.global.getPtDbDateFormat()+" "+"HH:mm");
				endDate=OhbeUtil.getDateForNextMinute(endDate, progDuration);
				scheduleTimePlan.setPlanEndDate(endDate);
				
				
				scheduleTimePlan.setSchtStaffId(schedulePlan.getSchStaffId());
				scheduleTimePlans.add(scheduleTimePlan);
			}
		}else{
			
			if(leftProgCount<periodCount)
				periodCount=leftProgCount;
			Date startDate=OhbeUtil.getThatDayFormatNotNull(planStartDateStr, GlobalUtil.global.getPtDbDateFormat());
			List<Date> dates=getStudioPlanDayArrays(startDate, scheduleTimeObjs, periodCount);
			for (Date date : dates) {
				ScheduleTimePlan scheduleTimePlan=new ScheduleTimePlan();
				scheduleTimePlan.setPlanStartDate(date);
				Date endDate=(Date)date.clone();
				endDate=OhbeUtil.getDateForNextMinute(endDate, progDuration);
				scheduleTimePlan.setPlanEndDate(endDate);
				
				scheduleTimePlan.setSchtStaffId(schedulePlan.getSchStaffId());
				scheduleTimePlans.add(scheduleTimePlan);
			}
		}
		
		
		
	}
	
	
	
	
	private List<Date> getStudioPlanDayArrays(Date startDate,List<ScheduleTimeObj> scheduleTimeObjs,int dpAdet){
		Calendar startPoint=Calendar.getInstance();
		startPoint.setTime((Date)startDate.clone());
		
		//String[] gunArr=gunler.split("#");
		
		boolean monday=false;
		boolean tuesday=false;
		boolean wednesday=false;
		boolean thursday=false;
		boolean friday=false;
		boolean saturday=false;
		boolean sunday=false;
		
		String mondayTime="";
		String tuesdayTime="";
		String wednesdayTime="";
		String thursdayTime="";
		String fridayTime="";
		String saturdayTime="";
		String sundayTime="";
	
		
		for (ScheduleTimeObj scheduleTimeObj : scheduleTimeObjs) {
	        String gun=""+scheduleTimeObj.getProgDay();
	        String times=scheduleTimeObj.getProgStartTime();
	        if(gun.equals("1")){
	        	monday=true;
	        	mondayTime=times;
	        }else if(gun.equals("2")){
	        	tuesday=true;
	        	tuesdayTime=times;
	        }else if(gun.equals("3")){
	        	wednesday=true;
	        	wednesdayTime=times;
	        }else if(gun.equals("4")){
	        	thursday=true;
	        	thursdayTime=times;
	        }else if(gun.equals("5")){
	        	friday=true;
	        	fridayTime=times;
	        }else if(gun.equals("6")){
	        	saturday=true;
	        	saturdayTime=times;
	        }else if(gun.equals("7")){
	        	sunday=true;
	        	sundayTime=times;
	        }
	        
	        
	        
        }
		
		
		List<Date> gunlerList=new ArrayList<Date>();
		int dersSayisi=dpAdet;
		int dateCounter=0;
		for (int i = 0; i < dpAdet; i++) {
	        
			   while(true){
				   if(dateCounter>0)
					   startPoint.add(Calendar.DATE, 1);
				   
				   String day=TimeTypes.getDateStrByFormatEEEByTurkish(new Date(startPoint.getTimeInMillis()));
				   if(day.equals(TimeTypes.TIME_Pzt) && monday){
					   Date gunDate=new Date(startPoint.getTimeInMillis());
					   if(!mondayTime.equals("0")){
						   gunDate= OhbeUtil.changeHourForDate(gunDate, mondayTime);
					   }
             		   gunlerList.add(gunDate);
             		   dateCounter=dateCounter+1;
             		   break;  
				   }else if(day.equals(TimeTypes.TIME_Sal) && tuesday){
					   Date gunDate=new Date(startPoint.getTimeInMillis());
             		  
             		  if(!tuesdayTime.equals("0")){
						   gunDate= OhbeUtil.changeHourForDate(gunDate,tuesdayTime);
					   }
             		 gunlerList.add(gunDate);
             		   dateCounter=dateCounter+1;
             		   break;  
				   }else if(day.equals(TimeTypes.TIME_Car) && wednesday){
					   Date gunDate=new Date(startPoint.getTimeInMillis());
             		  
             		  if(!wednesdayTime.equals("0")){
						   gunDate= OhbeUtil.changeHourForDate(gunDate,wednesdayTime);
					   }
             		 gunlerList.add(gunDate);
             		   dateCounter=dateCounter+1;
             		   break;  
				   }else if(day.equals(TimeTypes.TIME_Per) && thursday){
					   Date gunDate=new Date(startPoint.getTimeInMillis());
             		  
             		  if(!thursdayTime.equals("0")){
						   gunDate= OhbeUtil.changeHourForDate(gunDate,thursdayTime);
					   }
             		 gunlerList.add(gunDate);
             		   dateCounter=dateCounter+1;
             		   break;  
				   }else if(day.equals(TimeTypes.TIME_Cum) && friday){
					   Date gunDate=new Date(startPoint.getTimeInMillis());
             		  
             		  if(!fridayTime.equals("0")){
						   gunDate= OhbeUtil.changeHourForDate(gunDate,fridayTime);
					   }
             		 gunlerList.add(gunDate);
             		   dateCounter=dateCounter+1;
             		   break;  
				   }else if(day.equals(TimeTypes.TIME_Cmt) && saturday){
					   Date gunDate=new Date(startPoint.getTimeInMillis());
             		   
             		  if(!saturdayTime.equals("0")){
						   gunDate= OhbeUtil.changeHourForDate(gunDate,saturdayTime);
					   }
             		 gunlerList.add(gunDate);
             		   dateCounter=dateCounter+1;
             		   break;  
				   }else if(day.equals(TimeTypes.TIME_Paz) && sunday){
					   Date gunDate=new Date(startPoint.getTimeInMillis());
             		  
             		  if(!sundayTime.equals("0")){
						   gunDate= OhbeUtil.changeHourForDate(gunDate,sundayTime);
					   }
             		 gunlerList.add(gunDate);
             		   dateCounter=dateCounter+1;
             		   break;  
				   }
				   
				   dateCounter=dateCounter+1;
				   
			   }
			   dersSayisi--;
      		   if(dersSayisi==0)
      			  break;
		}
		return gunlerList;
	}
	
	
	
	
	public SchedulePlan getSchedulePlan() {
		return schedulePlan;
	}
	public void setSchedulePlan(SchedulePlan schedulePlan) {
		this.schedulePlan = schedulePlan;
	}
	public List<ScheduleTimePlan> getScheduleTimePlans() {
		return scheduleTimePlans;
	}
	public void setScheduleTimePlans(List<ScheduleTimePlan> scheduleTimePlans) {
		this.scheduleTimePlans = scheduleTimePlans;
	}
	public List<ScheduleStudios> getScheduleStudios() {
		return scheduleStudios;
	}
	public void setScheduleStudios(List<ScheduleStudios> scheduleStudios) {
		this.scheduleStudios = scheduleStudios;
	}
	public int getFirmId() {
		return firmId;
	}
	public void setFirmId(int firmId) {
		this.firmId = firmId;
	}
	public List<ScheduleTimeObj> getScheduleTimeObjs() {
		return scheduleTimeObjs;
	}
	public void setScheduleTimeObjs(List<ScheduleTimeObj> scheduleTimeObjs) {
		this.scheduleTimeObjs = scheduleTimeObjs;
	}

	public int getTimeType() {
		return timeType;
	}

	public void setTimeType(int timeType) {
		this.timeType = timeType;
	}

	public int getPeriodCount() {
		return periodCount;
	}

	public void setPeriodCount(int periodCount) {
		this.periodCount = periodCount;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public String getPlanStartDateStr() {
		return planStartDateStr;
	}

	public void setPlanStartDateStr(String planStartDateStr) {
		this.planStartDateStr = planStartDateStr;
	}

	public int getLeftProgCount() {
		return leftProgCount;
	}

	public void setLeftProgCount(int leftProgCount) {
		this.leftProgCount = leftProgCount;
	}




	public String getStaffName() {
		return staffName;
	}




	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}




	public List<HmiResultObj> getHmiResultObjs() {
		return hmiResultObjs;
	}




	public void setHmiResultObjs(List<HmiResultObj> hmiResultObjs) {
		this.hmiResultObjs = hmiResultObjs;
	}




	public String getPlanStartDateTime() {
		return planStartDateTime;
	}




	public void setPlanStartDateTime(String planStartDateTime) {
		this.planStartDateTime = planStartDateTime;
	}




	public List<ScheduleTimePlan> getAllScheduleTimePlans() {
		return allScheduleTimePlans;
	}




	public void setAllScheduleTimePlans(List<ScheduleTimePlan> allScheduleTimePlans) {
		this.allScheduleTimePlans = allScheduleTimePlans;
	}




	public ScheduleTimePlan getScheduleTimePlanForUpdate() {
		return scheduleTimePlanForUpdate;
	}




	public void setScheduleTimePlanForUpdate(ScheduleTimePlan scheduleTimePlanForUpdate) {
		this.scheduleTimePlanForUpdate = scheduleTimePlanForUpdate;
	}




	public Date getPlanStartDate() {
		return planStartDate;
	}




	public void setPlanStartDate(Date planStartDate) {
		this.planStartDate = planStartDate;
	}




	public String getTpComment() {
		return tpComment;
	}




	public void setTpComment(String tpComment) {
		this.tpComment = tpComment;
	}




	







/*
	@Override
	public String toString() {
		
		
		
		////System.out.println(schedulePlan);
		
		
		////System.out.println("firmId : "+firmId);
		////System.out.println("timeType : "+timeType);
		////System.out.println("periodCount : "+periodCount);
		////System.out.println("leftProgCount : "+leftProgCount);
		////System.out.println("planStartDateStr : "+planStartDateStr);
		
		for (ScheduleStudios scheduleStudios2 : scheduleStudios) {
			////System.out.println(scheduleStudios2);
		}
		for (User user : users) {
			////System.out.println("userId : "+user.getUserId());
			////System.out.println("saleId : "+user.getSaleId());
		}
		
		for (ScheduleTimeObj scheduleTimeObj2 : scheduleTimeObjs) {
			////System.out.println(scheduleTimeObj2);
		}
		
		
		if(scheduleTimePlans!=null){
		
			for (ScheduleTimePlan scheduleTimePlan2  : scheduleTimePlans) {
				////System.out.println(scheduleTimePlan2);
			}
		}
		
		
		return "OK";
	}
*/
	
	
	
	
}
