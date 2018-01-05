package tr.com.abasus.ptboss.schedule.entity;

import java.util.Date;

import tr.com.abasus.util.GlobalUtil;

public class ScheduleStudios {

	private long schsId;
	private long schtId; //Schedule Time Plan Id
	private int studioId;
	private String studioName;
	private String studioShortName;
	private Date changeDate=GlobalUtil.getCurrentDateByTimeZone();
	private String changeDateStr;
	public long getSchsId() {
		return schsId;
	}
	public void setSchsId(long schsId) {
		this.schsId = schsId;
	}
	public long getSchtId() {
		return schtId;
	}
	public void setSchtId(long schtId) {
		this.schtId = schtId;
	}
	public int getStudioId() {
		return studioId;
	}
	public void setStudioId(int studioId) {
		this.studioId = studioId;
	}
	public String getStudioName() {
		return studioName;
	}
	public void setStudioName(String studioName) {
		this.studioName = studioName;
	}
	public String getStudioShortName() {
		return studioShortName;
	}
	public void setStudioShortName(String studioShortName) {
		this.studioShortName = studioShortName;
	}
	public Date getChangeDate() {
		return changeDate;
	}
	public void setChangeDate(Date changeDate) {
		this.changeDate = changeDate;
	}
	public String getChangeDateStr() {
		return changeDateStr;
	}
	public void setChangeDateStr(String changeDateStr) {
		this.changeDateStr = changeDateStr;
	}
	@Override
	public String toString() {
		/*
		 * private long schsId;
	private long schtId; //Schedule Time Plan Id
	private int studioId;
	private String studioName;
	private String studioShortName;
	private Date changeDate=GlobalUtil.getCurrentDateByTimeZone();
	private String changeDateStr;
		 */
		
		////System.out.println("------------------------------------------");
		////System.out.println("-------------SCHEDULE STUDIOUS------------");
		////System.out.println("------------------------------------------");
		////System.out.println("schsId:"+schsId);
		////System.out.println("schtId:"+schtId);
		////System.out.println("studioId:"+studioId);
		////System.out.println("studioName:"+studioName);
		////System.out.println("studioShortName:"+studioShortName);
		////System.out.println("changeDate:"+changeDate);
		////System.out.println("changeDateStr:"+changeDateStr);
		////System.out.println("------------------------------------------");
		////System.out.println("---------END-SCHEDULE STUDIOUS------------");
		////System.out.println("------------------------------------------");
		return super.toString();
	}
	
	
	
	
}
