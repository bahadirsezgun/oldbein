package tr.com.abasus.ptboss.schedule.businessService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import tr.com.abasus.ptboss.definition.entity.DefCalendarTimes;
import tr.com.abasus.util.GlobalUtil;
@Service
@Scope("prototype")
public class ScheduleBusinessCalendarTimesService {

	
	
	
	public List<String> getTimesForMorning(){
		
		List<String> morningTimes=new ArrayList<String>();
		
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
		
		
		int difTime=endTimeCal-startTimeCal;
		int morningTime=Math.round(difTime/3);
		
		int morningEndTime=startTimeCal+morningTime;// 1200;
		
		System.out.println("*****************startTimeCal :"+startTimeCal+"  morningEndTime in times "+morningEndTime);
		
		Calendar startTimeCalendar=Calendar.getInstance();
		startTimeCalendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(defCalendarTimes.getStartTime().split(":")[0]));
		startTimeCalendar.set(Calendar.MINUTE, Integer.parseInt(defCalendarTimes.getStartTime().split(":")[1]));
		startTimeCalendar.set(Calendar.SECOND, 0);
		startTimeCalendar.set(Calendar.MILLISECOND, 0);
		
		
		for (int i = 0; i < loopCount; i++) {
			int startTime=startTimeCal+i*duration;
			int timeMinute=startTime-startTimeCal;
			
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
			////System.out.println("generatedStartTime MORNING:: : "+generatedStartTime);
			////System.out.println("/////////////////////////////////////////");
			
			if(generatedStartTime<1000){
				saatG="0"+(""+generatedStartTime).substring(0,1);
				dakikaG=(""+generatedStartTime).substring(1,3);
			}else{
				saatG=(""+generatedStartTime).substring(0,2);
				dakikaG=(""+generatedStartTime).substring(2,4);
			}
			generatedTime=saatG+":"+dakikaG;
			if(generatedStartTime>=startTimeCal && generatedStartTime<morningEndTime)
				morningTimes.add(generatedTime);
		
		}
		
		
		return morningTimes;
	}
	
	public List<String> getTimesForAfternoon(){
		
		List<String> afternoonTimes=new ArrayList<String>();
		
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
		/*
		int  afternoonStartTime=1200;
		if(sequence!=1){
			afternoonStartTime=1100;
		}
		*/
		Calendar startTimeCalendar=Calendar.getInstance();
		startTimeCalendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(defCalendarTimes.getStartTime().split(":")[0]));
		startTimeCalendar.set(Calendar.MINUTE, Integer.parseInt(defCalendarTimes.getStartTime().split(":")[1]));
		startTimeCalendar.set(Calendar.SECOND, 0);
		startTimeCalendar.set(Calendar.MILLISECOND, 0);
		
		
		int difTime=endTimeCal-startTimeCal;
		int morningTime=Math.round(difTime/3);
		
		int afternoonStartTime=startTimeCal+morningTime;// 1200;
		int afternoonEndTime=afternoonStartTime+morningTime;
		
		
		System.out.println("*****************afternoonStartTime :"+afternoonStartTime+"  afternoonEndTime in times "+afternoonEndTime);
		
		
		for (int i = 0; i < loopCount; i++) {
			int startTime=startTimeCal+i*duration;
			int timeMinute=startTime-startTimeCal;
			
			Calendar sequenceTimeCalendar=Calendar.getInstance();
			sequenceTimeCalendar.set(Calendar.HOUR_OF_DAY, startTimeCalendar.get(Calendar.HOUR_OF_DAY));
			sequenceTimeCalendar.set(Calendar.MINUTE, (startTimeCalendar.get(Calendar.MINUTE)+timeMinute));
			sequenceTimeCalendar.set(Calendar.SECOND, 0);
			sequenceTimeCalendar.set(Calendar.MILLISECOND, 0);
			
			String generatedMinute=sequenceTimeCalendar.get(Calendar.MINUTE)<10?"0"+sequenceTimeCalendar.get(Calendar.MINUTE):""+sequenceTimeCalendar.get(Calendar.MINUTE);
			
			String generatedHour=sequenceTimeCalendar.get(Calendar.HOUR_OF_DAY)==0?"24":""+sequenceTimeCalendar.get(Calendar.HOUR_OF_DAY);
			
			
			int generatedStartTime=Integer.parseInt(generatedHour+""+generatedMinute);
			
			////System.out.println("generatedStartTime AFTERRNOONN:: : "+generatedStartTime);
			////System.out.println("/////////////////////////////////////////");
			
			
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
			if(generatedStartTime>=afternoonStartTime && generatedStartTime<afternoonEndTime)
				afternoonTimes.add(generatedTime);
			
		}
		
		
		return afternoonTimes;
	}
	
	
	public List<String> getTimesForNight(){
		
		List<String> nightTimes=new ArrayList<String>();
		
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
		/*
		int  nightStartTime=1900;
		if(sequence!=1){
			nightStartTime=1800;
		}
		*/
		int difTime=endTimeCal-startTimeCal;
		int morningTime=Math.round(difTime/3);
		
		int nightStartTime=startTimeCal+2*morningTime;// 1200;
		
		
		Calendar startTimeCalendar=Calendar.getInstance();
		startTimeCalendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(defCalendarTimes.getStartTime().split(":")[0]));
		startTimeCalendar.set(Calendar.MINUTE, Integer.parseInt(defCalendarTimes.getStartTime().split(":")[1]));
		startTimeCalendar.set(Calendar.SECOND, 0);
		startTimeCalendar.set(Calendar.MILLISECOND, 0);
		
		
		for (int i = 0; i < loopCount; i++) {
			int startTime=startTimeCal+i*duration;
			int timeMinute=startTime-startTimeCal;
			
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
			////System.out.println("generatedStartTime NIGHT:: : "+generatedStartTime);
			////System.out.println("/////////////////////////////////////////");
			
			
			if(generatedStartTime<1000){
				saatG="0"+(""+generatedStartTime).substring(0,1);
				dakikaG=(""+generatedStartTime).substring(1,3);
			}else{
				saatG=(""+generatedStartTime).substring(0,2);
				dakikaG=(""+generatedStartTime).substring(2,4);
			}
			//System.out.println("1. NIGHT TIMES  generatedStartTime "+generatedStartTime+"   <= endTimeCal :"+endTimeCal);
			generatedTime=saatG+":"+dakikaG;
			if(generatedStartTime>=nightStartTime && generatedStartTime<=endTimeCal){
				nightTimes.add(generatedTime);
				//System.out.println("2. NIGHT TIMES  generatedStartTime "+generatedStartTime+"   <= endTimeCal :"+endTimeCal);
				
			}
			
		}
		
		
		return nightTimes;
	}
	
	public List<String> getTimesForAllDay(){
		
		List<String> allDayTimes=new ArrayList<String>();
		
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
		
		
		for (int i = 0; i < loopCount; i++) {
			int startTime=startTimeCal+i*duration;
			int timeMinute=startTime-startTimeCal;
			
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
			if(generatedStartTime>=startTimeCal && generatedStartTime<=endTimeCal)
				allDayTimes.add(generatedTime);
			
		}
		
		
		return allDayTimes;
	}
	
}
