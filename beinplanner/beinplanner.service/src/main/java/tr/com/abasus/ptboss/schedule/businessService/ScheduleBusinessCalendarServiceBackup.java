package tr.com.abasus.ptboss.schedule.businessService;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

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

public class ScheduleBusinessCalendarServiceBackup {

public String createPlanTblSabah(List<ScheduleTimePlan> scheduleTimePlans,String teacherName,long teacherId){
		
		String result="";
		
		
		DefCalendarTimes defCalendarTimes= GlobalUtil.defCalendarTimes;
		int startTimeCal=Integer.parseInt(defCalendarTimes.getStartTime().replace(":", ""));
		int endTimeCal=Integer.parseInt(defCalendarTimes.getEndTime().replace(":", ""));
		int duration=defCalendarTimes.getDuration();		
		
		
		Calendar startTimeCalendar=Calendar.getInstance();
		startTimeCalendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(defCalendarTimes.getStartTime().split(":")[0]));
		startTimeCalendar.set(Calendar.MINUTE, Integer.parseInt(defCalendarTimes.getStartTime().split(":")[1]));
		startTimeCalendar.set(Calendar.SECOND, 0);
		startTimeCalendar.set(Calendar.MILLISECOND, 0);
		
		//int startTime=530;
		result +="<tr name='"+teacherId+"' >";
		result  +="<td height='150' style='min-height:150px;border-width:2px'><h4>"+teacherName+"</h4><button type='button' class='btn btn-flat btn-sm btn-blue no-radius'><span name='week'></span><span style='display:none'>"+teacherId+"</span> </button> </td>";
		for (int i = 0; i < 13; i++) {
	      
			boolean foundTime=false;
			
			int startTime=startTimeCal+i*duration;
			////System.out.println("START TIME "+startTime);
			
			int timeMinute=startTime-startTimeCal;
			////System.out.println("TIME MINUTE "+timeMinute);
			
			Calendar sequenceTimeCalendar=Calendar.getInstance();
			sequenceTimeCalendar.set(Calendar.HOUR_OF_DAY, startTimeCalendar.get(Calendar.HOUR_OF_DAY));
			sequenceTimeCalendar.set(Calendar.MINUTE, (startTimeCalendar.get(Calendar.MINUTE)+timeMinute));
			sequenceTimeCalendar.set(Calendar.SECOND, 0);
			sequenceTimeCalendar.set(Calendar.MILLISECOND, 0);
			
			String saat=""+sequenceTimeCalendar.get(Calendar.HOUR_OF_DAY);
			String dakika=""+sequenceTimeCalendar.get(Calendar.MINUTE);
			
			
			
			
			if(startTime<1000){
				saat="0"+(""+startTime).substring(0,1);
				dakika=(""+startTime).substring(1,3);
			}else{
				saat=(""+startTime).substring(0,2);
				dakika=(""+startTime).substring(2,4);
			}
			
			////System.out.println("saat:dakika "+saat+":"+dakika);
			
			
			String generatedMinute=sequenceTimeCalendar.get(Calendar.MINUTE)<10?"0"+sequenceTimeCalendar.get(Calendar.MINUTE):""+sequenceTimeCalendar.get(Calendar.MINUTE);
			
			
			int generatedStartTime=Integer.parseInt(sequenceTimeCalendar.get(Calendar.HOUR_OF_DAY)+""+generatedMinute);
			
			////System.out.println("generatedStartTime "+generatedStartTime);
			
			
			////System.out.println("///////////////////////////////////////////////////////////////////");
			
			if(sequenceTimeCalendar.get(Calendar.HOUR_OF_DAY) <12){
				
			
			for (ScheduleTimePlan scheduleTimePlan : scheduleTimePlans) {
				
				int time=Integer.parseInt(DateTimeUtil.getHourMinuteToNumber(scheduleTimePlan.getPlanStartDate()));
				if(time==generatedStartTime){
					result +="<td colspan='2' style='height:110px;border-width:2px' >" +
							"<div name='tokenTime' style='padding:0px;margin:0px' class='hpanel "+(scheduleTimePlan.getLastPlan()==0?"hgreen":"hbgred")+" text-center'>" +
			                    "<div class='panel-body hmc-150' style='cursor: pointer'>" +
			                        
			                        "<div class='row'>"+
				                        "<div class='col-sm-4'>"+
				                          "<small class='text-success'>"+scheduleTimePlan.getSequence()+"</small>" +
				                        "</div>"+
				                        "<div class='col-sm-6 pull-right'>"+
				                          "<small class='label-success' title='"+scheduleTimePlan.getProgName()+"'>"+scheduleTimePlan.getProgShortName()+"</small>" +
				                        "</div>"+
				                     "</div>"+
				                     "<div class='row'>"+
					                    "<div class='col-sm-12 text-center'>"+
					                        "<div class='btn-group'>"+
					                        	"<button type='button' class='btn btn-sm btn-info' name='infoBtn'> <i class='pe-7s-info' ></i></button>"+
					                        	"<button type='button' class='btn btn-sm btn-primary' name='trashBtn'> <i class='pe-7s-trash' ></i></button>"+
					                        "</div>"+ 
					                        "<span style='display:none'>"+scheduleTimePlan.getSchId()+"</span>" +
					                        "<span style='display:none'>"+scheduleTimePlan.getSchtId()+"</span>" +
					                        "<span style='display:none'>"+saat+":"+dakika+"</span>" +
					                        "<span style='display:none'>"+teacherName+"</span>" +
					                        "<span style='display:none'>"+scheduleTimePlan.getProgType()+"</span>" +
					                        "<span style='display:none'>"+scheduleTimePlan.getProgId()+"</span>" +
							            "</div>"+
			                       "</div>"+
			                       "<div class='row' style='margin-top:5px'>"+
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
												result+="<img alt='logo'  title='"+scfu.getUserName()+" "+scfu.getUserSurname()+"' class='img-circle m-b' src='"+url+"'>";
											
												if(u==0){
											    	users+=scfu.getUserName();
											    }else{
											    	users+=","+scfu.getUserName();
											    }
												u++;
					                        
					                        }
				 result +="<br/><small>"+users+"</small>";   
			      result+= 					"</div>"
			       		+ "				</div>"
			       		+ "        </div>"+
			                        
			                        
				                  "</div>" +
			                     
			                "</div>" +
			             "</div>" +
					  "</td>";
					i++;
				    if (i % 2 == 0) {
					   startTime+=70;
					} else {
						startTime+=30;
					}
				foundTime=true;
			}
			}
			
			if(!foundTime){
				result +="<td style='height:60px;border-width:2px' name='freeTime'>"
							+"<div name='newTime'  class='hm-100' style='padding:0px;margin:0px;cursor:hand'>"
							+ "<span style='display:none'>"+teacherId+"</span>   "
							+ "<span style='display:none'>"+saat+":"+dakika+"</span>   "
							+ "<span style='display:none'>"+teacherName+"</span>" 
			               +"</div>"
						 +"</td>";
			}
			
			}else{
				
				break;
				/*
				result +="<td style='height:60px;border-width:2px' name='freeTime'>"
						+"<div name='newTime'  class='hm-100' style='padding:0px;margin:0px;cursor:hand'>"
						+ "<span style='display:none'>"+teacherId+"</span>   "
						+ "<span style='display:none'>"+saat+":"+dakika+"</span>   "
						+ "<span style='display:none'>"+teacherName+"</span>" 
		               +"</div>"
					 +"</td>";*/
			}
			
        }
		result +="</tr>";
		
		
		return result;
	}
	
	
	
	/*
	 * public String createPlanTblSabah(List<ScheduleTimePlan> scheduleTimePlans,String teacherName,long teacherId){
		
		String result="";
		
		
		DefCalendarTimes defCalendarTimes= GlobalUtil.defCalendarTimes;
		int startTimeCal=Integer.parseInt(defCalendarTimes.getStartTime().replace(":", ""));
		int endTimeCal=Integer.parseInt(defCalendarTimes.getEndTime().replace(":", ""));
		int duration=defCalendarTimes.getDuration();		
		
		int startTime=530;
		result +="<tr name='"+teacherId+"' >";
		result  +="<td height='150' style='min-height:150px;border-width:2px'><h4>"+teacherName+"</h4><button type='button' class='btn btn-flat btn-sm btn-blue no-radius'><span name='week'></span><span style='display:none'>"+teacherId+"</span> </button> </td>";
		for (int i = 0; i < 13; i++) {
	      
			boolean foundTime=false;
			
			if (i % 2 == 0) {
				   startTime+=70;
				} else {
					startTime+=30;
				}
			
			
			String saat="";
			String dakika="";
			
			if(startTime<1000){
				saat="0"+(""+startTime).substring(0,1);
				dakika=(""+startTime).substring(1,3);
			}else{
				saat=(""+startTime).substring(0,2);
				dakika=(""+startTime).substring(2,4);
			}
			
			if(startTime<1200){
				
			
			for (ScheduleTimePlan scheduleTimePlan : scheduleTimePlans) {
				
				int time=Integer.parseInt(DateTimeUtil.getHourMinuteToNumber(scheduleTimePlan.getPlanStartDate()));
				if(time==startTime){
					result +="<td colspan='2' style='height:110px;border-width:2px' >" +
							"<div name='tokenTime' style='padding:0px;margin:0px' class='hpanel "+(scheduleTimePlan.getLastPlan()==0?"hgreen":"hbgred")+" text-center'>" +
			                    "<div class='panel-body hmc-150' style='cursor: pointer'>" +
			                        
			                        "<div class='row'>"+
				                        "<div class='col-sm-4'>"+
				                          "<small class='text-success'>"+scheduleTimePlan.getSequence()+"</small>" +
				                        "</div>"+
				                        "<div class='col-sm-6 pull-right'>"+
				                          "<small class='label-success' title='"+scheduleTimePlan.getProgName()+"'>"+scheduleTimePlan.getProgShortName()+"</small>" +
				                        "</div>"+
				                     "</div>"+
				                     "<div class='row'>"+
					                    "<div class='col-sm-12 text-center'>"+
					                        "<div class='btn-group'>"+
					                        	"<button type='button' class='btn btn-sm btn-info' name='infoBtn'> <i class='pe-7s-info' ></i></button>"+
					                        	"<button type='button' class='btn btn-sm btn-primary' name='trashBtn'> <i class='pe-7s-trash' ></i></button>"+
					                        "</div>"+ 
					                        "<span style='display:none'>"+scheduleTimePlan.getSchId()+"</span>" +
					                        "<span style='display:none'>"+scheduleTimePlan.getSchtId()+"</span>" +
					                        "<span style='display:none'>"+saat+":"+dakika+"</span>" +
					                        "<span style='display:none'>"+teacherName+"</span>" +
					                        "<span style='display:none'>"+scheduleTimePlan.getProgType()+"</span>" +
					                        "<span style='display:none'>"+scheduleTimePlan.getProgId()+"</span>" +
							            "</div>"+
			                       "</div>"+
			                       "<div class='row' style='margin-top:5px'>"+
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
												result+="<img alt='logo'  title='"+scfu.getUserName()+" "+scfu.getUserSurname()+"' class='img-circle m-b' src='"+url+"'>";
											
												if(u==0){
											    	users+=scfu.getUserName();
											    }else{
											    	users+=","+scfu.getUserName();
											    }
												u++;
					                        
					                        }
				 result +="<br/><small>"+users+"</small>";   
			      result+= 					"</div>"
			       		+ "				</div>"
			       		+ "        </div>"+
			                        
			                        
				                  "</div>" +
			                     
			                "</div>" +
			             "</div>" +
					  "</td>";
					i++;
				    if (i % 2 == 0) {
					   startTime+=70;
					} else {
						startTime+=30;
					}
				foundTime=true;
			}
			}
			
			if(!foundTime){
				result +="<td style='height:60px;border-width:2px' name='freeTime'>"
							+"<div name='newTime'  class='hm-100' style='padding:0px;margin:0px;cursor:hand'>"
							+ "<span style='display:none'>"+teacherId+"</span>   "
							+ "<span style='display:none'>"+saat+":"+dakika+"</span>   "
							+ "<span style='display:none'>"+teacherName+"</span>" 
			               +"</div>"
						 +"</td>";
			}
			
			}else{
				result +="<td style='height:60px;border-width:2px' name='freeTime'>"
						+"<div name='newTime'  class='hm-100' style='padding:0px;margin:0px;cursor:hand'>"
						+ "<span style='display:none'>"+teacherId+"</span>   "
						+ "<span style='display:none'>"+saat+":"+dakika+"</span>   "
						+ "<span style='display:none'>"+teacherName+"</span>" 
		               +"</div>"
					 +"</td>";
			}
			
        }
		result +="</tr>";
		
		
		return result;
	}
	 */
	
		
	public String createPlanTblAfternoon(List<ScheduleTimePlan> scheduleTimePlans,String teacherName,long teacherId){
		
		String result="";
		
		
		
		int startTime=1130;
		result +="<tr name='"+teacherId+"' >";
		result  +="<td height='150' style='min-height:150px;border-width:2px'><h4>"+teacherName+"</h4><button type='button' class='btn btn-flat btn-sm btn-blue no-radius'><span name='week'></span><span style='display:none'>"+teacherId+"</span> </button> </td>";
		for (int i = 0; i < 13; i++) {
	      
			boolean foundTime=false;
			
			if (i % 2 == 0) {
				   startTime+=70;
				} else {
					startTime+=30;
				}
			
			
			String saat="";
			String dakika="";
			
			if(startTime<1000){
				saat="0"+(""+startTime).substring(0,1);
				dakika=(""+startTime).substring(1,3);
			}else{
				saat=(""+startTime).substring(0,2);
				dakika=(""+startTime).substring(2,4);
			}
			
			if(startTime<1800){
			
			for (ScheduleTimePlan scheduleTimePlan : scheduleTimePlans) {
				int time=Integer.parseInt(DateTimeUtil.getHourMinuteToNumber(scheduleTimePlan.getPlanStartDate()));
				if(time==startTime){
					result +="<td colspan='2' style='height:110px;border-width:2px' >" +
								"<div name='tokenTime' style='padding:0px;margin:0px' class='hpanel "+(scheduleTimePlan.getLastPlan()==0?"hgreen":"hbgred")+" text-center'>" +
				                    "<div class='panel-body hmc-150' style='cursor: pointer'>" +
				                        
										"<div class='row'>"+
										"<div class='col-sm-4'>"+
										  "<small class='text-success'>"+scheduleTimePlan.getSequence()+"</small>" +
										"</div>"+
										"<div class='col-sm-6 pull-right'>"+
										  "<small class='label-success' title='"+scheduleTimePlan.getProgName()+"'>"+scheduleTimePlan.getProgShortName()+"</small>" +
										"</div>"+
										"</div>"+
										"<div class='row'>"+
										"<div class='col-sm-12 text-center'>"+
										    "<div class='btn-group'>"+
										    	"<button type='button' class='btn btn-sm btn-info' name='infoBtn'> <i class='pe-7s-info' ></i></button>"+
										    	"<button type='button' class='btn btn-sm btn-primary' name='trashBtn'> <i class='pe-7s-trash' ></i></button>"+
										    "</div>"+ 
										    "<span style='display:none'>"+scheduleTimePlan.getSchId()+"</span>" +
					                        "<span style='display:none'>"+scheduleTimePlan.getSchtId()+"</span>" +
					                        "<span style='display:none'>"+saat+":"+dakika+"</span>" +
					                        "<span style='display:none'>"+teacherName+"</span>" +
					                        "<span style='display:none'>"+scheduleTimePlan.getProgType()+"</span>" +
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
													result+="<img alt='logo'  title='"+scfu.getUserName()+" "+scfu.getUserSurname()+"' class='img-circle m-b' src='"+url+"'>";
												
													if(u==0){
												    	users+=scfu.getUserName();
												    }else{
												    	users+=","+scfu.getUserName();
												    }
													u++;
						                        
						                        }
					 result +="<br/><small>"+users+"</small>";
						   result+= 					"</div>"
				       		+ "				</div>"
				       		+ "        </div>"+
				                        
				                        
					                   "</div>" +
				                     
				                "</div>" +
				             "</div>" +
						  "</td>";
						i++;
					    if (i % 2 == 0) {
						   startTime+=70;
						} else {
							startTime+=30;
						}
					foundTime=true;
				}
			}
			
			if(!foundTime){
				result +="<td style='height:60px;border-width:2px' name='freeTime'>"
							+"<div name='newTime'  class='hm-100' style='padding:0px;margin:0px;cursor:hand'>"
							+ "<span style='display:none'>"+teacherId+"</span>   "
							+ "<span style='display:none'>"+saat+":"+dakika+"</span>   "
							+ "<span style='display:none'>"+teacherName+"</span>" 
			               +"</div>"
						 +"</td>";
			}
			
			}else{
				result +="<td style='height:60px;border-width:2px' name='freeTime'>"
						+"<div name='newTime'  class='hm-100' style='padding:0px;margin:0px;cursor:hand'>"
						+ "<span style='display:none'>"+teacherId+"</span>   "
						+ "<span style='display:none'>"+saat+":"+dakika+"</span>   "
						+ "<span style='display:none'>"+teacherName+"</span>" 
		               +"</div>"
					 +"</td>";
			}
			
        }
		result +="</tr>";
		
		
		return result;
	}
  
  
	public String createPlanTblNight(List<ScheduleTimePlan> scheduleTimePlans,String teacherName,long teacherId){
		
		String result="";
		
		
		
		int startTime=1730;
		result +="<tr name='"+teacherId+"' >";
		result  +="<td height='150' style='min-height:150px;border-width:2px'><h4>"+teacherName+"</h4><button type='button' class='btn btn-flat btn-sm btn-blue no-radius'><span name='week'></span><span style='display:none'>"+teacherId+"</span> </button> </td>";
		for (int i = 0; i < 13; i++) {
	      
			boolean foundTime=false;
			
			if (i % 2 == 0) {
				   startTime+=70;
				} else {
					startTime+=30;
				}
			
			
			String saat="";
			String dakika="";
			
			if(startTime<1000){
				saat="0"+(""+startTime).substring(0,1);
				dakika=(""+startTime).substring(1,3);
			}else{
				saat=(""+startTime).substring(0,2);
				dakika=(""+startTime).substring(2,4);
			}
			
			
			for (ScheduleTimePlan scheduleTimePlan : scheduleTimePlans) {
				
				int time=Integer.parseInt(DateTimeUtil.getHourMinuteToNumber(scheduleTimePlan.getPlanStartDate()));
				if(time==startTime){
					result +="<td colspan='2' style='height:110px;border-width:2px' >" +
							"<div name='tokenTime' style='padding:0px;margin:0px' class='hpanel "+(scheduleTimePlan.getLastPlan()==0?"hgreen":"hbgred")+" text-center'>" +
			                    "<div class='panel-body hmc-150' style='cursor: pointer'>" +
			                        
									"<div class='row'>"+
									"<div class='col-sm-4'>"+
									  "<small class='text-success'>"+scheduleTimePlan.getSequence()+"</small>" +
									"</div>"+
									"<div class='col-sm-6 pull-right'>"+
									  "<small class='label-success' title='"+scheduleTimePlan.getProgName()+"'>"+scheduleTimePlan.getProgShortName()+"</small>" +
									"</div>"+
									"</div>"+
									"<div class='row'>"+
									"<div class='col-sm-12 text-center'>"+
									    "<div class='btn-group'>"+
									    	"<button type='button' class='btn btn-sm btn-info' name='infoBtn'> <i class='pe-7s-info' ></i></button>"+
									    	"<button type='button' class='btn btn-sm btn-primary' name='trashBtn'> <i class='pe-7s-trash' ></i></button>"+
									    "</div>"+ 
									    "<span style='display:none'>"+scheduleTimePlan.getSchId()+"</span>" +
				                        "<span style='display:none'>"+scheduleTimePlan.getSchtId()+"</span>" +
				                        "<span style='display:none'>"+saat+":"+dakika+"</span>" +
				                        "<span style='display:none'>"+teacherName+"</span>" +
				                        "<span style='display:none'>"+scheduleTimePlan.getProgType()+"</span>" +
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
												result+="<img alt='logo'  title='"+scfu.getUserName()+" "+scfu.getUserSurname()+"' class='img-circle m-b' src='"+url+"'>";
												if(u==0){
											    	users+=scfu.getUserName();
											    }else{
											    	users+=","+scfu.getUserName();
											    }
												u++;
					                        
					                        }
				 result +="<br/><small>"+users+"</small>";
			         result+= 					"</div>"
			       		+ "				</div>"
			       		+ "        </div>"+
			                        
			                        
				               "</div>" +
			                     
			                "</div>" +
			             "</div>" +
					  "</td>";
					i++;
				    if (i % 2 == 0) {
					   startTime+=70;
					} else {
						startTime+=30;
					}
				foundTime=true;
			}
			}
			
			if(!foundTime){
				result +="<td style='height:60px;border-width:2px' name='freeTime'>"
							+"<div name='newTime'  class='hm-100' style='padding:0px;margin:0px;cursor:hand'>"
							+ "<span style='display:none'>"+teacherId+"</span>   "
							+ "<span style='display:none'>"+saat+":"+dakika+"</span>   "
							+ "<span style='display:none'>"+teacherName+"</span>" 
			               +"</div>"
						 +"</td>";
			}
			
			
			
      }
		result +="</tr>";
		
		
		return result;
	}
	
	public String createPlanTblWeekHeader(int dayDuration,Date strtDate,int screenSize){
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
			
			calendarForWeekHeader+="<td style='max-width:"+bodySize+"px;min-width:"+bodySize+"px '><h5>"+startDateStr+"</h5><p name='dayName'>"+startDateName+"</p></td>";
			
		}
		calendarForWeekHeader+="</tr>";
		
		return calendarForWeekHeader;
		
	}
  
	public String createPlanTblWeek(List<ScheduleTimePlan> scheduleTimePlans,long teacherId,String teacherName,int dayDuration,Date strtDate,int screenSize){
		
		List<ScheduleDayObj> dayLists=  DateTimeUtil.getDayList(strtDate, dayDuration);
		
		String dateFormat=GlobalUtil.global.getPtScrDateFormat();
		HashMap<String, WeekTurnHelper>  weekTurnHelpers=new HashMap<String, WeekTurnHelper>();
		
		String result="";
	
		 int titleSize=50;
		 int bodySize=(screenSize-titleSize)/dayDuration;
		
		
			int startTime=530;
			int prevTime=500;
			int i=0;
			while(startTime<2400){
				
				result+="<tr>";
				
				    if (i % 2 == 0) {
					   startTime+=70;
					   prevTime+=30;
					} else {
						startTime+=30;
						prevTime+=70;
					}
				    
				    
				    String saat="";
					String dakika="";
					
					if(startTime<1000){
						saat="0"+(""+startTime).substring(0,1);
						dakika=(""+startTime).substring(1,3);
					}else{
						saat=(""+startTime).substring(0,2);
						dakika=(""+startTime).substring(2,4);
					}
					
				
					
					result +="<td style='min-height:25px;max-width:"+titleSize+"px;min-width:"+titleSize+"px ;border-width:2px'>"+saat+":"+dakika+"</td>" ;
					
			    
				for (ScheduleDayObj scheduleDayObj : dayLists) { 
					boolean isFound=false;
					String startDateStr=OhbeUtil.getDateStrByFormat(scheduleDayObj.getDayDate(), dateFormat).trim();  
					
					
					for (ScheduleTimePlan scheduleTimePlan : scheduleTimePlans) {
						
						String stpStartDateStr=OhbeUtil.getDateStrByFormat(scheduleTimePlan.getPlanStartDate(), dateFormat).trim(); 
						if(stpStartDateStr.equals(startDateStr)){
	                    	
	                    	int time=Integer.parseInt(DateTimeUtil.getHourMinuteToNumber(scheduleTimePlan.getPlanStartDate()));
	        				if(time==startTime){
	        					
	        					WeekTurnHelper weekTurnHelper=new WeekTurnHelper();
	                    		weekTurnHelper.setStartDate(startDateStr);
	                    		weekTurnHelper.setStartTime(startTime);
	                    		weekTurnHelpers.put(startTime+"-"+startDateStr,weekTurnHelper);
	                    		
	        					
	                    		result +="<td rowspan='2'   style='max-width:"+bodySize+"px;min-width:"+bodySize+"px;max-height:25px;overflow:visible;border-width:2px ' >" +
	    								"<div name='tokenTimeWeek' style='padding:0px;margin:0px;z-index:2000' class='hpanel "+(scheduleTimePlan.getLastPlan()==0?"hgreen":"hbgred")+" text-center'>" +
	    				                    "<div class='panel-body hmc-150' style='cursor: pointer'>" +
	    				                    
												"<div class='row'>"+
												"<div class='col-sm-4'>"+
												  "<small class='text-success'>"+scheduleTimePlan.getSequence()+"</small>" +
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
												    "</div>"+ 
												    "<span style='display:none'>"+scheduleTimePlan.getSchId()+"</span>" +
		    				                        "<span style='display:none'>"+scheduleTimePlan.getSchtId()+"</span>" +
		    				                        "<span style='display:none'>"+saat+":"+dakika+"</span>" +
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
						
						if(weekTurnHelper==null){
						result +="<td style='max-width:"+bodySize+"px;min-width:"+bodySize+"px;max-height:25px;overflow:visible ;cursor:hand;border-width:2px'  name='freeTimeWeek'>"
									+"<div name='newTimeWeek'   class='hm-50' style='padding:0px;margin:0px'>"
									+ "<span style='display:none'>"+teacherId+"</span>   "
									+ "<span style='display:none'>"+saat+":"+dakika+"</span>   "
									+ "<span style='display:none'>"+teacherName+"</span>" 
									+ "<span style='display:none'>"+startDateStr+"</span>" 
						            +"</div>"
								 +"</td>";
						}
					}
					
					
				   
			    }
				result+="</tr>";
				
				i++;
				
			}
		 return result;
	}
	
	
	
	
	
	
	 
	
	
}
