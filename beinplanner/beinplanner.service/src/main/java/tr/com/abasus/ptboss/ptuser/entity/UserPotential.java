package tr.com.abasus.ptboss.ptuser.entity;

import java.util.Date;

public class UserPotential {

	
	private long 		userId;
	private String 		userName;
	private String 		userSurname;
	private int 		firmId;
	private String 		userGsm;
	private String 		userEmail;
	private int 		userGender;
	private String 		pComment;
	private int 		pStatu;
	private String 		pStatuStr;
	
	private Date createTime;
	private Date updateTime;
	
	private String createTimeStr;
	private String updateTimeStr;
	
	private String mailContent;
	private String mailSubject;
	
	private long staffId;
	private String staffName;
	
	private String firmName;
	
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
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
	public int getFirmId() {
		return firmId;
	}
	public void setFirmId(int firmId) {
		this.firmId = firmId;
	}
	public String getUserGsm() {
		return userGsm;
	}
	public void setUserGsm(String userGsm) {
		this.userGsm = userGsm;
	}
	public String getUserEmail() {
		return userEmail;
	}
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	public int getUserGender() {
		return userGender;
	}
	public void setUserGender(int userGender) {
		this.userGender = userGender;
	}
	public String getpComment() {
		return pComment;
	}
	public void setpComment(String pComment) {
		this.pComment = pComment;
	}
	public int getpStatu() {
		return pStatu;
	}
	public void setpStatu(int pStatu) {
		this.pStatu = pStatu;
	}
	public String getFirmName() {
		return firmName;
	}
	public void setFirmName(String firmName) {
		this.firmName = firmName;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public String getMailContent() {
		return mailContent;
	}
	public void setMailContent(String mailContent) {
		this.mailContent = mailContent;
	}
	public String getMailSubject() {
		return mailSubject;
	}
	public void setMailSubject(String mailSubject) {
		this.mailSubject = mailSubject;
	}
	public long getStaffId() {
		return staffId;
	}
	public void setStaffId(long staffId) {
		this.staffId = staffId;
	}
	public String getStaffName() {
		return staffName;
	}
	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}
	public String getpStatuStr() {
		return pStatuStr;
	}
	public void setpStatuStr(String pStatuStr) {
		this.pStatuStr = pStatuStr;
	}
	public String getCreateTimeStr() {
		return createTimeStr;
	}
	public void setCreateTimeStr(String createTimeStr) {
		this.createTimeStr = createTimeStr;
	}
	public String getUpdateTimeStr() {
		return updateTimeStr;
	}
	public void setUpdateTimeStr(String updateTimeStr) {
		this.updateTimeStr = updateTimeStr;
	}

	
	
}
