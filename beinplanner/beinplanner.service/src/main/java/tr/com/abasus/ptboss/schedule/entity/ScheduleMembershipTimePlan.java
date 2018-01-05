package tr.com.abasus.ptboss.schedule.entity;

import java.util.Date;

public class ScheduleMembershipTimePlan {

	
	private long 	smtpId;
	private long 	smpId;
	private Date 	smpStartDate;
	private Date 	smpEndDate;
	private String 	smpStartDateStr;
	private String 	smpEndDateStr;
	private String 	smpComment;
	
	private String smpStartDayName;
	private String smpEndDayName;
	private String smpStartDayTime;
	private String smpEndDayTime;
	private boolean first=false;
	
	private String smpStatusStr;
	private String smpFreezeEndDateStr;
	private String smpFreezeEndDayName;
	
	
	public long getSmtpId() {
		return smtpId;
	}
	public void setSmtpId(long smtpId) {
		this.smtpId = smtpId;
	}
	public long getSmpId() {
		return smpId;
	}
	public void setSmpId(long smpId) {
		this.smpId = smpId;
	}
	public Date getSmpStartDate() {
		return smpStartDate;
	}
	public void setSmpStartDate(Date smpStartDate) {
		this.smpStartDate = smpStartDate;
	}
	public Date getSmpEndDate() {
		return smpEndDate;
	}
	public void setSmpEndDate(Date smpEndDate) {
		this.smpEndDate = smpEndDate;
	}
	public String getSmpStartDateStr() {
		return smpStartDateStr;
	}
	public void setSmpStartDateStr(String smpStartDateStr) {
		this.smpStartDateStr = smpStartDateStr;
	}
	public String getSmpEndDateStr() {
		return smpEndDateStr;
	}
	public void setSmpEndDateStr(String smpEndDateStr) {
		this.smpEndDateStr = smpEndDateStr;
	}
	public String getSmpComment() {
		return smpComment;
	}
	public void setSmpComment(String smpComment) {
		this.smpComment = smpComment;
	}
	public String getSmpStartDayName() {
		return smpStartDayName;
	}
	public void setSmpStartDayName(String smpStartDayName) {
		this.smpStartDayName = smpStartDayName;
	}
	public String getSmpEndDayName() {
		return smpEndDayName;
	}
	public void setSmpEndDayName(String smpEndDayName) {
		this.smpEndDayName = smpEndDayName;
	}
	public String getSmpStartDayTime() {
		return smpStartDayTime;
	}
	public void setSmpStartDayTime(String smpStartDayTime) {
		this.smpStartDayTime = smpStartDayTime;
	}
	public String getSmpEndDayTime() {
		return smpEndDayTime;
	}
	public void setSmpEndDayTime(String smpEndDayTime) {
		this.smpEndDayTime = smpEndDayTime;
	}
	public String getSmpStatusStr() {
		return smpStatusStr;
	}
	public void setSmpStatusStr(String smpStatusStr) {
		this.smpStatusStr = smpStatusStr;
	}
	public boolean isFirst() {
		return first;
	}
	public void setFirst(boolean first) {
		this.first = first;
	}
	public String getSmpFreezeEndDateStr() {
		return smpFreezeEndDateStr;
	}
	public void setSmpFreezeEndDateStr(String smpFreezeEndDateStr) {
		this.smpFreezeEndDateStr = smpFreezeEndDateStr;
	}
	public String getSmpFreezeEndDayName() {
		return smpFreezeEndDayName;
	}
	public void setSmpFreezeEndDayName(String smpFreezeEndDayName) {
		this.smpFreezeEndDayName = smpFreezeEndDayName;
	}
	
	
	
}
