package tr.com.abasus.ptboss.definition.entity;

public class DefCalendarTimes {

	private int calIdx=1;
	private String startTime;
	private String endTime;
	private int duration;
	private int calPeriod;
	
	public int getCalIdx() {
		return calIdx;
	}
	public void setCalIdx(int calIdx) {
		this.calIdx = calIdx;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}
	public int getCalPeriod() {
		return calPeriod;
	}
	public void setCalPeriod(int calPeriod) {
		this.calPeriod = calPeriod;
	}
	
	
}
