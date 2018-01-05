package tr.com.abasus.ptboss.ptuser.entity;

import java.util.Date;

public class StaffTracking {

	private long ptIdx;
	private long userId;
	private String staffName;
	private Date stDate;
	private String stDateStr;
	private String stTimeStr;
	private int inOut;
	private String inOutStr;
	private String userName;
	private String userSurname;
	
	
	
	private String startDateStr;
	private String endDateStr;
	
	private int firmId;
	private String firmName;

	public long getPtIdx() {
		return ptIdx;
	}

	public void setPtIdx(long ptIdx) {
		this.ptIdx = ptIdx;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getStaffName() {
		return staffName;
	}

	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}

	public Date getStDate() {
		return stDate;
	}

	public void setStDate(Date stDate) {
		this.stDate = stDate;
	}

	public String getStDateStr() {
		return stDateStr;
	}

	public void setStDateStr(String stDateStr) {
		this.stDateStr = stDateStr;
	}

	public String getStTimeStr() {
		return stTimeStr;
	}

	public void setStTimeStr(String stTimeStr) {
		this.stTimeStr = stTimeStr;
	}

	public int getInOut() {
		return inOut;
	}

	public void setInOut(int inOut) {
		this.inOut = inOut;
	}

	public String getInOutStr() {
		return inOutStr;
	}

	public void setInOutStr(String inOutStr) {
		this.inOutStr = inOutStr;
	}

	public String getFirmName() {
		return firmName;
	}

	public void setFirmName(String firmName) {
		this.firmName = firmName;
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

	public int getFirmId() {
		return firmId;
	}

	public void setFirmId(int firmId) {
		this.firmId = firmId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserSurname() {
		return userSurname;
	}

	public void setUserSurname(String userSurname) {
		this.userSurname = userSurname;
	}

	
	
}
