package tr.com.abasus.ptboss.schedule.entity;

import java.util.List;

import tr.com.abasus.ptboss.ptuser.entity.User;

public class SchedulePlan {

	private long 	schId;
	private int 	progId;
	private int 	progType;
	private String 	progTypeStr;
	private int 	schCount;
	private long 	schStaffId;
	private int 	firmId;
	
	
	/*****************************/
	/*MAIN INSTRUCTOR INFORMATION*/
	/*****************************/
	private String 	userName;
	private String 	userSurname;
	private String 	profileUrl;
	private int 	urlType;
	private int 	userGender;
	
	
	/*****************************/
	/*PLANNING EXTRA INFORMATION*/
	/*****************************/
	private String 	progName;
	private int 	minMemberCount;
	private int 	maxMemberCount;
	
	private int finishedPlan;
	
	private User staff;
	
	private List<ScheduleTimePlan> scheduleTimePlans;
	
	
	int programCount;
	int plannedCount;
	int willPlanCount;
	
	
	public long getSchId() {
		return schId;
	}
	public void setSchId(long schId) {
		this.schId = schId;
	}
	public int getProgId() {
		return progId;
	}
	public void setProgId(int progId) {
		this.progId = progId;
	}
	public int getProgType() {
		return progType;
	}
	public void setProgType(int progType) {
		this.progType = progType;
	}
	public int getSchCount() {
		return schCount;
	}
	public void setSchCount(int schCount) {
		this.schCount = schCount;
	}
	public long getSchStaffId() {
		return schStaffId;
	}
	public void setSchStaffId(long schStaffId) {
		this.schStaffId = schStaffId;
	}
	public int getFirmId() {
		return firmId;
	}
	public void setFirmId(int firmId) {
		this.firmId = firmId;
	}
	@Override
	public String toString() {
		////System.out.println("------------------------------------------");
		////System.out.println("-------------SCHEDULE PLAN----------------");
		////System.out.println("------------------------------------------");
		////System.out.println("schId:"+schId);
		////System.out.println("progId:"+progId);
		////System.out.println("progType:"+progType);
		////System.out.println("schCount:"+schCount);
		////System.out.println("schStaffId:"+schStaffId);
		////System.out.println("firmId:"+firmId);
		////System.out.println("------------------------------------------");
		////System.out.println("---------END-SCHEDULE PLAN----------------");
		////System.out.println("------------------------------------------");
		
		return super.toString();
	}
	public List<ScheduleTimePlan> getScheduleTimePlans() {
		return scheduleTimePlans;
	}
	public void setScheduleTimePlans(List<ScheduleTimePlan> scheduleTimePlans) {
		this.scheduleTimePlans = scheduleTimePlans;
	}
	public String getProgTypeStr() {
		return progTypeStr;
	}
	public void setProgTypeStr(String progTypeStr) {
		this.progTypeStr = progTypeStr;
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
	public String getProfileUrl() {
		return profileUrl;
	}
	public void setProfileUrl(String profileUrl) {
		this.profileUrl = profileUrl;
	}
	public int getUrlType() {
		return urlType;
	}
	public void setUrlType(int urlType) {
		this.urlType = urlType;
	}
	public String getProgName() {
		return progName;
	}
	public void setProgName(String progName) {
		this.progName = progName;
	}
	public int getMinMemberCount() {
		return minMemberCount;
	}
	public void setMinMemberCount(int minMemberCount) {
		this.minMemberCount = minMemberCount;
	}
	public int getMaxMemberCount() {
		return maxMemberCount;
	}
	public void setMaxMemberCount(int maxMemberCount) {
		this.maxMemberCount = maxMemberCount;
	}
	public int getFinishedPlan() {
		return finishedPlan;
	}
	public void setFinishedPlan(int finishedPlan) {
		this.finishedPlan = finishedPlan;
	}
	public User getStaff() {
		return staff;
	}
	public void setStaff(User staff) {
		this.staff = staff;
	}
	public int getUserGender() {
		return userGender;
	}
	public void setUserGender(int userGender) {
		this.userGender = userGender;
	}
	public int getProgramCount() {
		return programCount;
	}
	public void setProgramCount(int programCount) {
		this.programCount = programCount;
	}
	public int getPlannedCount() {
		return plannedCount;
	}
	public void setPlannedCount(int plannedCount) {
		this.plannedCount = plannedCount;
	}
	public int getWillPlanCount() {
		return willPlanCount;
	}
	public void setWillPlanCount(int willPlanCount) {
		this.willPlanCount = willPlanCount;
	}
	
	
	
	
	
}
