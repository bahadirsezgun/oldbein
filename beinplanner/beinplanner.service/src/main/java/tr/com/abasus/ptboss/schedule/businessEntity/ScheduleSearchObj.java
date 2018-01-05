package tr.com.abasus.ptboss.schedule.businessEntity;

import java.util.Date;
import java.util.List;

import tr.com.abasus.ptboss.schedule.entity.ScheduleStudios;

public class ScheduleSearchObj {

	private Date startDate;
	private Date endDate;
	private String startDateStr;
	private String endDateStr;
	private long staffId;
	private List<ScheduleStudios> studios;
	
	private int month;
	private int year;
	private int typeOfSchedule;
	
	
	
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public String getStartDateStr() {
		return startDateStr;
	}
	public void setStartDateStr(String startDateStr) {
		this.startDateStr = startDateStr;
	}
	public String getEndDateStr() {
		return endDateStr;
	}
	public void setEndDateStr(String endDateStr) {
		this.endDateStr = endDateStr;
	}
	public long getStaffId() {
		return staffId;
	}
	public void setStaffId(long staffId) {
		this.staffId = staffId;
	}
	public List<ScheduleStudios> getStudios() {
		return studios;
	}
	public void setStudios(List<ScheduleStudios> studios) {
		this.studios = studios;
	}
	public int getMonth() {
		return month;
	}
	public void setMonth(int month) {
		this.month = month;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public int getTypeOfSchedule() {
		return typeOfSchedule;
	}
	public void setTypeOfSchedule(int typeOfSchedule) {
		this.typeOfSchedule = typeOfSchedule;
	}
	
	
}
