package tr.com.abasus.ptboss.schedule.businessService;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import tr.com.abasus.ptboss.definition.entity.DefCalendarTimes;
import tr.com.abasus.ptboss.ptuser.entity.User;
import tr.com.abasus.ptboss.schedule.businessEntity.ScheduleDayObj;
import tr.com.abasus.ptboss.schedule.businessEntity.WeekTurnHelper;
import tr.com.abasus.ptboss.schedule.entity.ScheduleFactory;
import tr.com.abasus.ptboss.schedule.entity.ScheduleTimePlan;
import tr.com.abasus.util.DateTimeUtil;
import tr.com.abasus.util.GlobalUtil;
import tr.com.abasus.util.OhbeUtil;
import tr.com.abasus.util.StatuTypes;

@Service
@Scope("prototype")
public class ScheduleBusinessCalendarAllService {

	

  
	
   public String createPlanTblWeekMorning(List<ScheduleTimePlan> scheduleTimePlans,List<User> trainers,int dayDuration,Date strtDate,int screenSize){
	
	
	
	List<ScheduleDayObj> dayLists=  DateTimeUtil.getDayList(strtDate, dayDuration);
	
	String dateFormat=GlobalUtil.global.getPtScrDateFormat();
	HashMap<String, WeekTurnHelper>  weekTurnHelpers=new HashMap<String, WeekTurnHelper>();
	
	String result=createPlanTblWeekHeader(dayDuration,trainers, strtDate, screenSize);

	 int titleSize=50;
	 int bodySize=(screenSize-titleSize)/dayDuration;
	
	
	    DefCalendarTimes defCalendarTimes= GlobalUtil.defCalendarTimes;
		int startTimeCal=Integer.parseInt(defCalendarTimes.getStartTime().replace(":", ""));
		int endTimeCal=Integer.parseInt(defCalendarTimes.getEndTime().replace(":", ""));
		
		if(endTimeCal==0){
			endTimeCal=2400;
		}
		
		int morningPeriod=(endTimeCal-startTimeCal)/2;
		int morningEndTime=(startTimeCal+morningPeriod)+100;
		
		
		
		int duration=defCalendarTimes.getCalPeriod();		
		int classDuration=defCalendarTimes.getDuration();		
		int sequence=classDuration/duration;
		int loopCount=24*60/duration;
		
		Calendar startTimeCalendar=Calendar.getInstance();
		startTimeCalendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(defCalendarTimes.getStartTime().split(":")[0]));
		startTimeCalendar.set(Calendar.MINUTE, Integer.parseInt(defCalendarTimes.getStartTime().split(":")[1]));
		startTimeCalendar.set(Calendar.SECOND, 0);
		startTimeCalendar.set(Calendar.MILLISECOND, 0);
		
	 
		String boxClass="hmc-50";
	    if(sequence>1){
	    	boxClass="hmc-100";
	    }
	 
	 
		for (int i = 0; i < loopCount; i++) {
			
			boolean foundTime=false;
			
			int startTime=startTimeCal+i*duration;
			int timeMinute=startTime-startTimeCal;
			int prevTime=(startTimeCal+(i-1)*duration);
			////System.out.println("/////////////////////////////////////////////////////////////");
			////System.out.println("startTime "+startTime+"  prevTime "+prevTime+"  duration "+duration);
			////System.out.println("/////////////////////////////////////////////////////////////");
			
			Calendar sequenceTimeCalendar=Calendar.getInstance();
			sequenceTimeCalendar.set(Calendar.HOUR_OF_DAY, startTimeCalendar.get(Calendar.HOUR_OF_DAY));
			sequenceTimeCalendar.set(Calendar.MINUTE, (startTimeCalendar.get(Calendar.MINUTE)+timeMinute));
			sequenceTimeCalendar.set(Calendar.SECOND, 0);
			sequenceTimeCalendar.set(Calendar.MILLISECOND, 0);
			
			String generatedMinute=sequenceTimeCalendar.get(Calendar.MINUTE)<10?"0"+sequenceTimeCalendar.get(Calendar.MINUTE):""+sequenceTimeCalendar.get(Calendar.MINUTE);
			
			
			String generatedHour=sequenceTimeCalendar.get(Calendar.HOUR_OF_DAY)==0?"24":""+sequenceTimeCalendar.get(Calendar.HOUR_OF_DAY);
			int generatedStartTime=Integer.parseInt(generatedHour+""+generatedMinute);
			
			String generatedTime="";
			String saatG="";
			String dakikaG="";
			
			if(generatedStartTime<1000){
				saatG="0"+(""+generatedStartTime).substring(0,1);
				dakikaG=(""+generatedStartTime).substring(1,3);
			}else{
				saatG=(""+generatedStartTime).substring(0,2);
				dakikaG=(""+generatedStartTime).substring(2,4);
			}
			 generatedTime=saatG+":"+dakikaG;
			
			
			 
			if(generatedStartTime>=startTimeCal && generatedStartTime<=morningEndTime ){
			 
			result+="<tr>";
				
				
			result +="<td style='min-height:25px;max-width:"+titleSize+"px;min-width:"+titleSize+"px ;border-width:2px'>"+generatedTime+"</td>" ;
			
			String bckColor="lavender";
		    
			for (ScheduleDayObj scheduleDayObj : dayLists) { 
				boolean isFound=false;
				String startDateStr=OhbeUtil.getDateStrByFormat(scheduleDayObj.getDayDate(), dateFormat).trim();  
				
				
				
				for (User trainer : trainers) {
					
					isFound=false;
				for (ScheduleTimePlan scheduleTimePlan : scheduleTimePlans) {
					
					if(trainer.getUserId()==scheduleTimePlan.getSchtStaffId()){
					
						
						
						
					String stpStartDateStr=OhbeUtil.getDateStrByFormat(scheduleTimePlan.getPlanStartDate(), dateFormat).trim(); 
					if(stpStartDateStr.equals(startDateStr)){
                    	
                    	int time=Integer.parseInt(DateTimeUtil.getHourMinuteToNumber(scheduleTimePlan.getPlanStartDate()));
        				
                    	
                    	
						
                    	 if(time==generatedStartTime){
        					if(sequence>1){
        						for (int j = 0; j < sequence-1 ; j++) {
        							WeekTurnHelper weekTurnHelper=new WeekTurnHelper();
    	                    		weekTurnHelper.setStartDate(startDateStr);
    	                    		weekTurnHelper.setStartTime(startTime);
    	                    		weekTurnHelpers.put((startTime+j*duration)+"-"+startDateStr+"-"+trainer.getUserId(),weekTurnHelper);
								}
        						
        					
        					}
                    	
        					String userIns="";
        					int j=0;
        					for (ScheduleFactory scfu : scheduleTimePlan.getUsers()) {
        						if(j==0){
        							userIns+=scfu.getUserName();
        					    }else{
        					    	userIns+=","+scfu.getUserName();
        					    }
        						j++;
        					}
        					
                        		
        					
                    		result +="<td rowspan='"+sequence+"'  style='max-width:"+bodySize+"px;min-width:"+bodySize+"px;max-height:25px;overflow:visible;border-width:2px;background-color:"+bckColor+" ' >" +
    								"<div name='tokenTimeWeek' style='padding:0px;margin:0px;z-index:500' class='hpanel "+getColorOfPlan(scheduleTimePlan)+" text-center'>" +
    				                    "<div class='panel-body "+boxClass+"' style='cursor: pointer'>" +
    				                    
											"<div class='row'>"+
											"<div class='col-sm-6'>"+
											  "<small >"+scheduleTimePlan.getSequence()+"</small>" +
											"</div>"+
											"<div class='col-sm-6 pull-right'>"+
											  "<small class='label-success' title='"+scheduleTimePlan.getProgName()+"'>"+scheduleTimePlan.getProgShortName()+"</small>" +
											"</div>"+
											"<div class='col-sm-12'><small>"+userIns+"</small>"+
											"</div>";
											
						                    if(!scheduleTimePlan.getTpComment().equals(""))  {  
						                    	result +="<div class='col-sm-12 text-center'>"+
						                        "<button type='button' class='btn btn-circle btn-xs btn-primary2 text-center'></button>"+
						                        "</div>";
						                    }
						                    result +="</div>"+
											"<div class='customButtons' style='display:none;position:absolute;overflow:visible;table-layout:fixed'>"+
											"<div class='row' style='padding:0px;margin:0px' >"+
											"<div class='col-sm-12 text-center form-inline' style='padding:0px;margin:0px'>"+
											    "<div class='btn-group' style='padding:0px;margin:0px'>"+
											    	"<button type='button' class='btn btn-xs btn-info' name='infoBtnWeek'> <i class='pe-7s-info' ></i></button>"+
											    	"<button type='button' class='btn btn-xs btn-primary' name='trashBtnWeek'> <i class='pe-7s-trash' ></i></button>"+
											    	"<button type='button' class='btn btn-xs btn-primary2' name='cancelBtnWeek'><em style='display:none'>"+scheduleTimePlan.getStatuTp()+"</em>   <i class='"+(scheduleTimePlan.getStatuTp()==StatuTypes.TIMEPLAN_CANCEL?"pe-7s-like2":"pe-7s-attention")+"' ></i></button>"+
											    	"<button type='button' class='btn btn-xs btn-danger2' name='postponeBtnWeek'><em style='display:none'>"+scheduleTimePlan.getStatuTp()+"</em>  <i class='"+(scheduleTimePlan.getStatuTp()==StatuTypes.TIMEPLAN_POSTPONE?"pe-7s-like2":"pe-7s-stopwatch")+"' ></i></button>"+
									             "</div>"+ 
											    "<span style='display:none'>"+scheduleTimePlan.getSchId()+"</span>" +
	    				                        "<span style='display:none'>"+scheduleTimePlan.getSchtId()+"</span>" +
	    				                        "<span style='display:none'>"+generatedTime+"</span>" +
	    				                        "<span style='display:none'>"+trainer.getUserName()+" "+trainer.getUserSurname()+"</span>" +
	    				                        "<span style='display:none'>"+scheduleTimePlan.getProgType()+"</span>" +
	    				                        "<span style='display:none'>"+startDateStr+"</span>" +
	    				                        "<span style='display:none'>"+scheduleTimePlan.getProgId()+"</span>" +
	    				                        "<span style='display:none'>"+trainer.getUserId()+"</span>" +
	    				                        
	    					                   	
											"</div>"+
											"</div>"+
											"</div>"+
											
										"<div class='customInfo' style='display:none;position:relative;z-index:2000;color:white'>"+
										  "<div class=' hpanel' style='background-color:#008080;max-width:250px;min-width:150px;margin:0px'>"+	
											"<div class='row ' style='padding-left:10px;padding-right:10px' >"+
											 "<p>"+trainer.getUserName()+" "+trainer.getUserSurname()+"</p>"+
											 "<p>"+startDateStr+" "+generatedTime+"</p>"+
											 "<p>"+scheduleTimePlan.getProgName()+"</p>"+
											 "<label style='word-wrap: break-word;'>"+scheduleTimePlan.getTpComment()+"</label>"+
											"</div>"+

										    
										    "<div class='row ' >"+

										    	
										    
    				                        
    				                        
    				                        "<div class='projects'>"+
    				                        "<div class='project-people text-center' >";
							                String users="";
							                int u=0;
    				                        for (ScheduleFactory scfu : scheduleTimePlan.getUsers()) {
												String profileUrl=scfu.getProfileUrl();
												int urlType=scfu.getUrlType();
												String url="";
												if(profileUrl==null){
													if(scfu.getUserGender()==1){
														url="/beinplanner/homerlib/images/profile.png";
													}else{
														url="/beinplanner/homerlib/images/profilem.png";
													}
												}else{
													if(urlType==1){
														url="../pt/member/get/profile/"+scfu.getUserId()+"/1";
													}else{
														url="/beinplanner/homerlib/images/"+profileUrl;
													}
												}
												result+="<img alt='logo' title='"+scfu.getUserName()+" "+scfu.getUserSurname()+"' class='img-circle m-b' src='"+url+"'>";
												if(u==0){
											    	users+=scfu.getUserName();
											    }else{
											    	users+=","+scfu.getUserName();
											    }
												u++;
					                        
					                        }
    				        result +="<br/><small>"+users+"</small>";             
    				        result += 		"</div></div></div>"+
    				        		    "</div>" +
										"</div>" +
    				    		   			"</div>" +
    					                "</div>" +
	    				            "</div>" +
    							  "</td>";
                    		
                    		isFound=true;
                    	}
                    }
					}
			    }
			
				if(!isFound){
					
					WeekTurnHelper weekTurnHelper=weekTurnHelpers.get(prevTime+"-"+startDateStr+"-"+trainer.getUserId());
					
					//System.out.println("search weekTurnHelpers key  "+prevTime+"-"+startDateStr);
   				    //System.out.println("*************************************************************************************");
    				
					
					
					if(weekTurnHelper==null){
							//System.out.println("<td> not found "+startDateStr+" "+generatedTime+"  "+trainer.getUserName()+"</td>");
           				
					result +="<td style='max-width:"+bodySize+"px;min-width:"+bodySize+"px;max-height:25px;overflow:visible ;cursor:hand;border-width:2px;background-color:"+bckColor+"'  name='freeTimeWeek'>"
								+"<div name='newTimeWeek'   class='hm-50' style='padding:0px;margin:0px'>"
								+ "<span style='display:none'>"+trainer.getUserId()+"</span>   "
								+ "<span style='display:none'>"+generatedTime+"</span>   "
								+ "<span style='display:none'>"+trainer.getUserName()+" "+trainer.getUserSurname()+"</span>" 
								+ "<span style='display:none'>"+startDateStr+"</span>" 
					            +"</div>"
							 +"</td>";
					}else{
						if(generatedStartTime>1300 && generatedStartTime<1410){	
                    		//System.out.println("<td> prev time found  "+startDateStr+" "+generatedTime+"  "+trainer.getUserName()+"</td>");
           				}
					}
				}
				
				}
				
				if(bckColor.equals("lavender")){
					bckColor="";
				}else{
					bckColor="lavender";
				}
			   
		    }
			result+="</tr>";
			}
			
		
			
		}
	 return result;
}
    
   public String createPlanTblWeekNight(List<ScheduleTimePlan> scheduleTimePlans,List<User> trainers,int dayDuration,Date strtDate,int screenSize){
		
		
		
		List<ScheduleDayObj> dayLists=  DateTimeUtil.getDayList(strtDate, dayDuration);
		
		String dateFormat=GlobalUtil.global.getPtScrDateFormat();
		HashMap<String, WeekTurnHelper>  weekTurnHelpers=new HashMap<String, WeekTurnHelper>();
		
		String result=createPlanTblWeekHeader(dayDuration,trainers, strtDate, screenSize);

		 int titleSize=50;
		 int bodySize=(screenSize-titleSize)/dayDuration;
		
		
		    DefCalendarTimes defCalendarTimes= GlobalUtil.defCalendarTimes;
			int startTimeCal=Integer.parseInt(defCalendarTimes.getStartTime().replace(":", ""));
			int endTimeCal=Integer.parseInt(defCalendarTimes.getEndTime().replace(":", ""));
			
			if(endTimeCal==0){
				endTimeCal=2400;
			}
			
			int morningPeriod=(endTimeCal-startTimeCal)/2;
			int morningEndTime=(startTimeCal+morningPeriod)+100;
			
			
			
			int duration=defCalendarTimes.getCalPeriod();		
			int classDuration=defCalendarTimes.getDuration();		
			int sequence=classDuration/duration;
			int loopCount=24*60/duration;
			
			Calendar startTimeCalendar=Calendar.getInstance();
			startTimeCalendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(defCalendarTimes.getStartTime().split(":")[0]));
			startTimeCalendar.set(Calendar.MINUTE, Integer.parseInt(defCalendarTimes.getStartTime().split(":")[1]));
			startTimeCalendar.set(Calendar.SECOND, 0);
			startTimeCalendar.set(Calendar.MILLISECOND, 0);
			
		 
			 String boxClass="hmc-50";
			    if(sequence>1){
			    	boxClass="hmc-100";
			    }
		 
		 
			for (int i = 0; i < loopCount; i++) {
				
				boolean foundTime=false;
				
				int startTime=startTimeCal+i*duration;
				int timeMinute=startTime-startTimeCal;
				int prevTime=(startTimeCal+(i-1)*duration);
				////System.out.println("/////////////////////////////////////////////////////////////");
				////System.out.println("startTime "+startTime+"  prevTime "+prevTime+"  duration "+duration);
				////System.out.println("/////////////////////////////////////////////////////////////");
				
				Calendar sequenceTimeCalendar=Calendar.getInstance();
				sequenceTimeCalendar.set(Calendar.HOUR_OF_DAY, startTimeCalendar.get(Calendar.HOUR_OF_DAY));
				sequenceTimeCalendar.set(Calendar.MINUTE, (startTimeCalendar.get(Calendar.MINUTE)+timeMinute));
				sequenceTimeCalendar.set(Calendar.SECOND, 0);
				sequenceTimeCalendar.set(Calendar.MILLISECOND, 0);
				
				String generatedMinute=sequenceTimeCalendar.get(Calendar.MINUTE)<10?"0"+sequenceTimeCalendar.get(Calendar.MINUTE):""+sequenceTimeCalendar.get(Calendar.MINUTE);
				
				
				String generatedHour=sequenceTimeCalendar.get(Calendar.HOUR_OF_DAY)==0?"24":""+sequenceTimeCalendar.get(Calendar.HOUR_OF_DAY);
				int generatedStartTime=Integer.parseInt(generatedHour+""+generatedMinute);
				
				String generatedTime="";
				String saatG="";
				String dakikaG="";
				
				if(generatedStartTime<1000){
					saatG="0"+(""+generatedStartTime).substring(0,1);
					dakikaG=(""+generatedStartTime).substring(1,3);
				}else{
					saatG=(""+generatedStartTime).substring(0,2);
					dakikaG=(""+generatedStartTime).substring(2,4);
				}
				 generatedTime=saatG+":"+dakikaG;
				
				
				 
				if(generatedStartTime>=morningEndTime && generatedStartTime<=endTimeCal ){
				 
				result+="<tr>";
					
					
				result +="<td style='min-height:25px;max-width:"+titleSize+"px;min-width:"+titleSize+"px ;border-width:2px'>"+generatedTime+"</td>" ;
				
				String bckColor="lavender";
			    
				for (ScheduleDayObj scheduleDayObj : dayLists) { 
					boolean isFound=false;
					String startDateStr=OhbeUtil.getDateStrByFormat(scheduleDayObj.getDayDate(), dateFormat).trim();  
					
					
					
					for (User trainer : trainers) {
						
						isFound=false;
					for (ScheduleTimePlan scheduleTimePlan : scheduleTimePlans) {
						
						if(trainer.getUserId()==scheduleTimePlan.getSchtStaffId()){
						
							
							
							
						String stpStartDateStr=OhbeUtil.getDateStrByFormat(scheduleTimePlan.getPlanStartDate(), dateFormat).trim(); 
						if(stpStartDateStr.equals(startDateStr)){
	                    	
	                    	int time=Integer.parseInt(DateTimeUtil.getHourMinuteToNumber(scheduleTimePlan.getPlanStartDate()));
	        				
	                    	
	                    	
							
	                    	 if(time==generatedStartTime){
	        					if(sequence>1){
	        						for (int j = 0; j < sequence-1 ; j++) {
	        							WeekTurnHelper weekTurnHelper=new WeekTurnHelper();
	    	                    		weekTurnHelper.setStartDate(startDateStr);
	    	                    		weekTurnHelper.setStartTime(startTime);
	    	                    		weekTurnHelpers.put((startTime+j*duration)+"-"+startDateStr+"-"+trainer.getUserId(),weekTurnHelper);
									}
	        						
	        					
	        					}
	                    	
	        					String userIns="";
	        					int j=0;
	        					for (ScheduleFactory scfu : scheduleTimePlan.getUsers()) {
	        						if(j==0){
	        							userIns+=scfu.getUserName();
	        					    }else{
	        					    	userIns+=","+scfu.getUserName();
	        					    }
	        						j++;
	        					}
	        					
	                        		
	        					
	                    		result +="<td rowspan='"+sequence+"'  style='max-width:"+bodySize+"px;min-width:"+bodySize+"px;max-height:25px;overflow:visible;border-width:2px;background-color:"+bckColor+" ' >" +
	    								"<div name='tokenTimeWeek' style='padding:0px;margin:0px;z-index:500' class='hpanel "+getColorOfPlan(scheduleTimePlan)+" text-center'>" +
	    				                    "<div class='panel-body "+boxClass+"' style='cursor: pointer'>" +
	    				                    
												"<div class='row'>"+
												"<div class='col-sm-6'>"+
												  "<small >"+scheduleTimePlan.getSequence()+"</small>" +
												"</div>"+
												"<div class='col-sm-6 pull-right'>"+
												  "<small class='label-success' title='"+scheduleTimePlan.getProgName()+"'>"+scheduleTimePlan.getProgShortName()+"</small>" +
												"</div>"+
												"<div class='col-sm-12'><small>"+userIns+"</small>"+
												"</div>";
												
							                    if(!scheduleTimePlan.getTpComment().equals(""))  {  
							                    	result +="<div class='col-sm-12 text-center'>"+
							                        "<button type='button' class='btn btn-circle btn-xs btn-primary2 text-center'></button>"+
							                        "</div>";
							                    }
							                    result +="</div>"+
												"<div class='customButtons' style='display:none;position:absolute;overflow:visible;table-layout:fixed'>"+
												"<div class='row' style='padding:0px;margin:0px' >"+
												"<div class='col-sm-12 text-center form-inline' style='padding:0px;margin:0px'>"+
												    "<div class='btn-group' style='padding:0px;margin:0px'>"+
												    	"<button type='button' class='btn btn-xs btn-info' name='infoBtnWeek'> <i class='pe-7s-info' ></i></button>"+
												    	"<button type='button' class='btn btn-xs btn-primary' name='trashBtnWeek'> <i class='pe-7s-trash' ></i></button>"+
												    	"<button type='button' class='btn btn-xs btn-primary2' name='cancelBtnWeek'><em style='display:none'>"+scheduleTimePlan.getStatuTp()+"</em>   <i class='"+(scheduleTimePlan.getStatuTp()==StatuTypes.TIMEPLAN_CANCEL?"pe-7s-like2":"pe-7s-attention")+"' ></i></button>"+
												    	"<button type='button' class='btn btn-xs btn-danger2' name='postponeBtnWeek'><em style='display:none'>"+scheduleTimePlan.getStatuTp()+"</em>  <i class='"+(scheduleTimePlan.getStatuTp()==StatuTypes.TIMEPLAN_POSTPONE?"pe-7s-like2":"pe-7s-stopwatch")+"' ></i></button>"+
										            "</div>"+ 
												    "<span style='display:none'>"+scheduleTimePlan.getSchId()+"</span>" +
		    				                        "<span style='display:none'>"+scheduleTimePlan.getSchtId()+"</span>" +
		    				                        "<span style='display:none'>"+generatedTime+"</span>" +
		    				                        "<span style='display:none'>"+trainer.getUserName()+" "+trainer.getUserSurname()+"</span>" +
		    				                        "<span style='display:none'>"+scheduleTimePlan.getProgType()+"</span>" +
		    				                        "<span style='display:none'>"+startDateStr+"</span>" +
		    				                        "<span style='display:none'>"+scheduleTimePlan.getProgId()+"</span>" +
		    				                        "<span style='display:none'>"+trainer.getUserId()+"</span>" +
		    				                        
		    					                   	
												"</div>"+
												"</div>"+
												"</div>"+
												
											"<div class='customInfo' style='display:none;position:relative;z-index:2000;color:white'>"+
											  "<div class=' hpanel' style='background-color:#008080;max-width:250px;min-width:150px;margin:0px'>"+	
												"<div class='row ' style='padding-left:10px;padding-right:10px' >"+
												 "<p>"+trainer.getUserName()+" "+trainer.getUserSurname()+"</p>"+
												 "<p>"+startDateStr+" "+generatedTime+"</p>"+
												 "<p>"+scheduleTimePlan.getProgName()+"</p>"+
												 "<label style='word-wrap: break-word;'>"+scheduleTimePlan.getTpComment()+"</label>"+
												"</div>"+

											    
											    "<div class='row ' >"+

											    	
											    
	    				                        
	    				                        
	    				                        "<div class='projects'>"+
	    				                        "<div class='project-people text-center' >";
								                String users="";
								                int u=0;
	    				                        for (ScheduleFactory scfu : scheduleTimePlan.getUsers()) {
													String profileUrl=scfu.getProfileUrl();
													int urlType=scfu.getUrlType();
													String url="";
													if(profileUrl==null){
														if(scfu.getUserGender()==1){
															url="/beinplanner/homerlib/images/profile.png";
														}else{
															url="/beinplanner/homerlib/images/profilem.png";
														}
													}else{
														if(urlType==1){
															url="../pt/member/get/profile/"+scfu.getUserId()+"/1";
														}else{
															url="/beinplanner/homerlib/images/"+profileUrl;
														}
													}
													result+="<img alt='logo' title='"+scfu.getUserName()+" "+scfu.getUserSurname()+"' class='img-circle m-b' src='"+url+"'>";
													if(u==0){
												    	users+=scfu.getUserName();
												    }else{
												    	users+=","+scfu.getUserName();
												    }
													u++;
						                        
						                        }
	    				        result +="<br/><small>"+users+"</small>";             
	    				        result += 		"</div></div></div>"+
	    				        		    "</div>" +
											"</div>" +
	    				    		   			"</div>" +
	    					                "</div>" +
		    				            "</div>" +
	    							  "</td>";
	                    		
	                    		isFound=true;
	                    	}
	                    }
						}
				    }
				
					if(!isFound){
						
						WeekTurnHelper weekTurnHelper=weekTurnHelpers.get(prevTime+"-"+startDateStr+"-"+trainer.getUserId());
						
						//System.out.println("search weekTurnHelpers key  "+prevTime+"-"+startDateStr);
	   				    //System.out.println("*************************************************************************************");
	    				
						
						
						if(weekTurnHelper==null){
								//System.out.println("<td> not found "+startDateStr+" "+generatedTime+"  "+trainer.getUserName()+"</td>");
	           				
						result +="<td style='max-width:"+bodySize+"px;min-width:"+bodySize+"px;max-height:25px;overflow:visible ;cursor:hand;border-width:2px;background-color:"+bckColor+"'  name='freeTimeWeek'>"
									+"<div name='newTimeWeek'   class='hm-50' style='padding:0px;margin:0px'>"
									+ "<span style='display:none'>"+trainer.getUserId()+"</span>   "
									+ "<span style='display:none'>"+generatedTime+"</span>   "
									+ "<span style='display:none'>"+trainer.getUserName()+" "+trainer.getUserSurname()+"</span>" 
									+ "<span style='display:none'>"+startDateStr+"</span>" 
						            +"</div>"
								 +"</td>";
						}else{
							if(generatedStartTime>1300 && generatedStartTime<1410){	
	                    		//System.out.println("<td> prev time found  "+startDateStr+" "+generatedTime+"  "+trainer.getUserName()+"</td>");
	           				}
						}
					}
					
					}
					
					if(bckColor.equals("lavender")){
						bckColor="";
					}else{
						bckColor="lavender";
					}
				   
			    }
				result+="</tr>";
				}
				
			
				
			}
		 return result;
	} 
   
   
  
   
  /* 
  public String createPlanTblWeekNight(List<ScheduleTimePlan> scheduleTimePlans,List<User> trainers,int dayDuration,Date strtDate,int screenSize){
		
		
		
		List<ScheduleDayObj> dayLists=  DateTimeUtil.getDayList(strtDate, dayDuration);
		
		String dateFormat=GlobalUtil.global.getPtScrDateFormat();
		HashMap<String, WeekTurnHelper>  weekTurnHelpers=new HashMap<String, WeekTurnHelper>();
		
		String result=createPlanTblWeekHeader(dayDuration,trainers, strtDate, screenSize);
	
		 int titleSize=50;
		 int bodySize=(screenSize-titleSize)/dayDuration;
		
		
		    DefCalendarTimes defCalendarTimes= GlobalUtil.defCalendarTimes;
			int startTimeCal=Integer.parseInt(defCalendarTimes.getStartTime().replace(":", ""));
			int endTimeCal=Integer.parseInt(defCalendarTimes.getEndTime().replace(":", ""));
			
			if(endTimeCal==0){
				endTimeCal=2400;
			}
			
			int duration=defCalendarTimes.getCalPeriod();		
			int classDuration=defCalendarTimes.getDuration();		
			int sequence=classDuration/duration;
			int loopCount=24*60/duration;
			
			Calendar startTimeCalendar=Calendar.getInstance();
			startTimeCalendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(defCalendarTimes.getStartTime().split(":")[0]));
			startTimeCalendar.set(Calendar.MINUTE, Integer.parseInt(defCalendarTimes.getStartTime().split(":")[1]));
			startTimeCalendar.set(Calendar.SECOND, 0);
			startTimeCalendar.set(Calendar.MILLISECOND, 0);
			
		 
			int morningPeriod=(endTimeCal-startTimeCal)/2;
			int morningEndTime=(startTimeCal+morningPeriod)-100;
			
		 
		 
			for (int i = 0; i < loopCount; i++) {
				
				boolean foundTime=false;
				
				int startTime=startTimeCal+i*duration;
				int timeMinute=startTime-startTimeCal;
				int prevTime=(startTimeCal+(i-1)*duration);
				////System.out.println("/////////////////////////////////////////////////////////////");
				////System.out.println("startTime "+startTime+"  prevTime "+prevTime+"  duration "+duration);
				////System.out.println("/////////////////////////////////////////////////////////////");
				
				Calendar sequenceTimeCalendar=Calendar.getInstance();
				sequenceTimeCalendar.set(Calendar.HOUR_OF_DAY, startTimeCalendar.get(Calendar.HOUR_OF_DAY));
				sequenceTimeCalendar.set(Calendar.MINUTE, (startTimeCalendar.get(Calendar.MINUTE)+timeMinute));
				sequenceTimeCalendar.set(Calendar.SECOND, 0);
				sequenceTimeCalendar.set(Calendar.MILLISECOND, 0);
				
				String generatedMinute=sequenceTimeCalendar.get(Calendar.MINUTE)<10?"0"+sequenceTimeCalendar.get(Calendar.MINUTE):""+sequenceTimeCalendar.get(Calendar.MINUTE);
				
				
				String generatedHour=sequenceTimeCalendar.get(Calendar.HOUR_OF_DAY)==0?"24":""+sequenceTimeCalendar.get(Calendar.HOUR_OF_DAY);
				int generatedStartTime=Integer.parseInt(generatedHour+""+generatedMinute);
				
				String generatedTime="";
				String saatG="";
				String dakikaG="";
				
				if(generatedStartTime<1000){
					saatG="0"+(""+generatedStartTime).substring(0,1);
					dakikaG=(""+generatedStartTime).substring(1,3);
				}else{
					saatG=(""+generatedStartTime).substring(0,2);
					dakikaG=(""+generatedStartTime).substring(2,4);
				}
				 generatedTime=saatG+":"+dakikaG;
				
				//System.out.println("generatedStartTime "+generatedStartTime+"   morningEndTime:"+morningEndTime+"  endTimeCal:"+endTimeCal);
				 //System.out.println("****************************************************************************************");
				if(generatedStartTime>=morningEndTime && generatedStartTime<=endTimeCal ){
				 
				result+="<tr>";
					
					
				result +="<td style='min-height:25px;max-width:"+titleSize+"px;min-width:"+titleSize+"px ;border-width:2px'>"+generatedTime+"</td>" ;
					
			    
				for (ScheduleDayObj scheduleDayObj : dayLists) { 
					boolean isFound=false;
					String startDateStr=OhbeUtil.getDateStrByFormat(scheduleDayObj.getDayDate(), dateFormat).trim();  
					
					
					for (ScheduleTimePlan scheduleTimePlan : scheduleTimePlans) {
						
						String stpStartDateStr=OhbeUtil.getDateStrByFormat(scheduleTimePlan.getPlanStartDate(), dateFormat).trim(); 
						if(stpStartDateStr.equals(startDateStr)){
	                    	
	                    	int time=Integer.parseInt(DateTimeUtil.getHourMinuteToNumber(scheduleTimePlan.getPlanStartDate()));
	        				if(time==generatedStartTime){
	        					
	        					
	        					
	        					if(sequence>1){
	        						for (int j = 0; j < sequence-1 ; j++) {
	        							WeekTurnHelper weekTurnHelper=new WeekTurnHelper();
	    	                    		weekTurnHelper.setStartDate(startDateStr);
	    	                    		weekTurnHelper.setStartTime(startTime);
	    	                    		weekTurnHelpers.put((startTime+j*duration)+"-"+startDateStr,weekTurnHelper);
									}
	        						
	        					
	        					}
	                    		
	                    		
	                    		////System.out.println("weekTurnHelpers key  "+startTime+"-"+startDateStr);
	           				    ////System.out.println("*************************************************************************************");
	            				
	                    		
	        					
	                    		result +="<td rowspan='"+sequence+"'  style='max-width:"+bodySize+"px;min-width:"+bodySize+"px;max-height:25px;overflow:visible;border-width:2px ' >" +
	    								"<div name='tokenTimeWeek' style='padding:0px;margin:0px;z-index:2000' class='hpanel "+getColorOfPlan(scheduleTimePlan)+" text-center'>" +
	    				                    "<div class='panel-body hmc-150' style='cursor: pointer'>" +
	    				                    
												"<div class='row'>"+
												"<div class='col-sm-4'>"+
												  "<small >"+scheduleTimePlan.getSequence()+"</small>" +
												"</div>"+
												"<div class='col-sm-6 pull-right'>"+
												  "<small class='label-success' title='"+scheduleTimePlan.getProgName()+"'>"+scheduleTimePlan.getProgShortName()+"</small>" +
												"</div>"+
												"</div>"+
												"<div class='row'>"+
												"<div class='col-sm-12 text-center'>"+
												    "<div class='btn-group'>"+
												    	"<button type='button' class='btn btn-sm btn-info' name='infoBtnWeek'> <i class='pe-7s-info' ></i></button>"+
												    	"<button type='button' class='btn btn-sm btn-primary' name='trashBtnWeek'> <i class='pe-7s-trash' ></i></button>"+
												    	"<button type='button' class='btn btn-sm btn-primary2' name='cancelBtnWeek'><em style='display:none'>"+scheduleTimePlan.getStatuTp()+"</em>   <i class='"+(scheduleTimePlan.getStatuTp()==StatuTypes.TIMEPLAN_CANCEL?"pe-7s-like2":"pe-7s-attention")+"' ></i></button>"+
												    "</div>"+ 
												    "<span style='display:none'>"+scheduleTimePlan.getSchId()+"</span>" +
		    				                        "<span style='display:none'>"+scheduleTimePlan.getSchtId()+"</span>" +
		    				                        "<span style='display:none'>"+generatedTime+"</span>" +
		    				                        "<span style='display:none'>"+teacherName+"</span>" +
		    				                        "<span style='display:none'>"+scheduleTimePlan.getProgType()+"</span>" +
		    				                        "<span style='display:none'>"+startDateStr+"</span>" +
		    				                        "<span style='display:none'>"+scheduleTimePlan.getProgId()+"</span>" +
		    					                   	
												"</div>"+
												"</div>"+
												
												"<div class='row'>"+

	    				                        "<div class='projects'>"+
	    				                        "<div class='project-people text-center' >";
								                String users="";
								                int u=0;
	    				                        for (ScheduleFactory scfu : scheduleTimePlan.getUsers()) {
													String profileUrl=scfu.getProfileUrl();
													int urlType=scfu.getUrlType();
													String url="";
													if(profileUrl==null){
														if(scfu.getUserGender()==1){
															url="/beinplanner/homerlib/images/profile.png";
														}else{
															url="/beinplanner/homerlib/images/profilem.png";
														}
													}else{
														if(urlType==1){
															url="../pt/member/get/profile/"+scfu.getUserId()+"/1";
														}else{
															url="/beinplanner/homerlib/images/"+profileUrl;
														}
													}
													result+="<img alt='logo' title='"+scfu.getUserName()+" "+scfu.getUserSurname()+"' class='img-circle m-b' src='"+url+"'>";
													if(u==0){
												    	users+=scfu.getUserName();
												    }else{
												    	users+=","+scfu.getUserName();
												    }
													u++;
						                        
						                        }
	    				        result +="<br/><small>"+users+"</small>";             
	    				        result += 		"</div></div></div>"+
	    				       
	    				    		   
	    				    		   			"</div>" +
	    					                "</div>" +
		    				            "</div>" +
	    							  "</td>";
	                    		
	                    		isFound=true;
	                    	}
	                    }
				    }
				
					if(!isFound){
						
						WeekTurnHelper weekTurnHelper=weekTurnHelpers.get(prevTime+"-"+startDateStr);
						
						//System.out.println("search weekTurnHelpers key  "+prevTime+"-"+startDateStr);
       				    //System.out.println("*************************************************************************************");
        				
						
						
						if(weekTurnHelper==null){
						result +="<td style='max-width:"+bodySize+"px;min-width:"+bodySize+"px;max-height:25px;overflow:visible ;cursor:hand;border-width:2px'  name='freeTimeWeek'>"
									+"<div name='newTimeWeek'   class='hm-50' style='padding:0px;margin:0px'>"
									+ "<span style='display:none'>"+teacherId+"</span>   "
									+ "<span style='display:none'>"+generatedTime+"</span>   "
									+ "<span style='display:none'>"+teacherName+"</span>" 
									+ "<span style='display:none'>"+startDateStr+"</span>" 
						            +"</div>"
								 +"</td>";
						}
					}
					
					
				   
			    }
				result+="</tr>";
				}
				
			
				
			}
		 return result;
	}
	*/

		public String createPlanTblWeekHeader(int dayDuration,List<User> trainers,Date strtDate,int screenSize){
			List<ScheduleDayObj> dayLists=  DateTimeUtil.getDayList(strtDate, dayDuration);
			int titleSize=50;
			 int bodySize=(screenSize-titleSize)/dayDuration;
			
			String dateFormat=GlobalUtil.global.getPtScrDateFormat();
			
			String result="";
			String calendarForWeekHeader="";
			calendarForWeekHeader+="<tr>";
			calendarForWeekHeader+="<td style='max-width:"+titleSize+"px;min-width:"+titleSize+"px;border-width:2px '></td>";
			for (ScheduleDayObj dayList : dayLists) {
				
				
				String startDateStr=OhbeUtil.getDateStrByFormat(dayList.getDayDate(), dateFormat).trim();  
				String startDateName=DateTimeUtil.getDayNames(dayList.getDayDate());
				
				calendarForWeekHeader+="<td colspan="+trainers.size()+" style='max-width:"+bodySize+"px;min-width:"+bodySize+"px '><h5>"+startDateStr+"</h5><p name='dayName'>"+startDateName+"</p></td>";
				
			}
			calendarForWeekHeader+="</tr>";
			
			calendarForWeekHeader+="<tr>";
			calendarForWeekHeader+="<td style='max-width:"+titleSize+"px;min-width:"+titleSize+"px;border-width:2px '></td>";
			for (ScheduleDayObj dayList : dayLists) {
				for (User trainer : trainers) {
					calendarForWeekHeader+="<td style='max-width:"+bodySize+"px;min-width:"+bodySize+"px;border-width:2px;text-align:center '><p>"+trainer.getUserName()+"</p><p name='dayName'>"+trainer.getUserSurname()+"</p></td>";
				}
			}
			calendarForWeekHeader+="</tr>";
			
			return calendarForWeekHeader;
			
		}
		
		private String getColorOfPlan(ScheduleTimePlan scheduleTimePlan){
			
			if(scheduleTimePlan.getStatuTp()==StatuTypes.TIMEPLAN_CANCEL){
				return "hbgyellow";
			}else if(scheduleTimePlan.getStatuTp()==StatuTypes.TIMEPLAN_POSTPONE){
				return "hbgblue";
			}else if(scheduleTimePlan.getLastPlan()==1){
				return "hbgred";
			}else if(scheduleTimePlan.getFirstPlan()==1){
				return "hbggreen";
			}
			
			return "hgreen";
			
		}
}
