package tr.com.abasus.ptboss.schedule.businessEntity;

import java.util.List;

import tr.com.abasus.ptboss.ptuser.entity.User;
import tr.com.abasus.ptboss.schedule.entity.ScheduleTimePlan;

public class ScheduleCalendarObj {

	private String calendarDate;
	private String calendarDateName;
	private User staff;
	
	private long staffId;
	private int day;
	private int dayDuration;
	private int actualSize;
	
	private String calendarForMorning;
	private String calendarForAfternoon;
	private String calendarForNight;
	
	private String calendarForWeekMorning;
	private String calendarForWeekNight;
	
	
	private List<ScheduleTimePlan> scheduleTimePlans;
	
	
	public String getCalendarDate() {
		return calendarDate;
	}
	public void setCalendarDate(String calendarDate) {
		this.calendarDate = calendarDate;
	}
	public String getCalendarDateName() {
		return calendarDateName;
	}
	public void setCalendarDateName(String calendarDateName) {
		this.calendarDateName = calendarDateName;
	}
	public List<ScheduleTimePlan> getScheduleTimePlans() {
		return scheduleTimePlans;
	}
	public void setScheduleTimePlans(List<ScheduleTimePlan> scheduleTimePlans) {
		this.scheduleTimePlans = scheduleTimePlans;
	}
	public int getDay() {
		return day;
	}
	public void setDay(int day) {
		this.day = day;
	}
	public long getStaffId() {
		return staffId;
	}
	public void setStaffId(long staffId) {
		this.staffId = staffId;
	}
	public User getStaff() {
		return staff;
	}
	public void setStaff(User staff) {
		this.staff = staff;
	}
	public String getCalendarForMorning() {
		return calendarForMorning;
	}
	public void setCalendarForMorning(String calendarForMorning) {
		this.calendarForMorning = calendarForMorning;
	}
	public String getCalendarForAfternoon() {
		return calendarForAfternoon;
	}
	public void setCalendarForAfternoon(String calendarForAfternoon) {
		this.calendarForAfternoon = calendarForAfternoon;
	}
	public String getCalendarForNight() {
		return calendarForNight;
	}
	public void setCalendarForNight(String calendarForNight) {
		this.calendarForNight = calendarForNight;
	}
	public int getDayDuration() {
		return dayDuration;
	}
	public void setDayDuration(int dayDuration) {
		this.dayDuration = dayDuration;
	}
	
	public int getActualSize() {
		return actualSize;
	}
	public void setActualSize(int actualSize) {
		this.actualSize = actualSize;
	}
	public String getCalendarForWeekMorning() {
		return calendarForWeekMorning;
	}
	public void setCalendarForWeekMorning(String calendarForWeekMorning) {
		this.calendarForWeekMorning = calendarForWeekMorning;
	}
	public String getCalendarForWeekNight() {
		return calendarForWeekNight;
	}
	public void setCalendarForWeekNight(String calendarForWeekNight) {
		this.calendarForWeekNight = calendarForWeekNight;
	}

	
	
	
	
}
