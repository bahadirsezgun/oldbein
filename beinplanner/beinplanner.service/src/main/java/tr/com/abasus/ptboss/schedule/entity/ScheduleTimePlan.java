package tr.com.abasus.ptboss.schedule.entity;

import java.util.Date;
import java.util.List;

import tr.com.abasus.ptboss.ptuser.entity.User;

public class ScheduleTimePlan {

	private long schtId;
	private long schId;
	private Date planStartDate;
	private Date planEndDate;
	private String planStartDateStr;
	private String planEndDateStr;
	private int statuTp;
	private long schtStaffId;
	private int  schCount;
	private String tpComment;
	
	private String planDayName;
	private String planDayTime;
	private String planEndDayTime;
	private int planCount;
	
	private String planStatusComment;
	private int planStatus;
	
	private String userName;
	private String userSurname;
	private String userGsm;
	
	
	
	private String staffName;
	
	private User staff;
	
	private int progType;
	private int progId;
	private String progName;
	private String progShortName;
	
	private int lastPlan;
	private int firstPlan;
	
	private String sequence ;
	
	private List<ScheduleFactory> users;
	
	private List<ScheduleStudios> scheduleStudios;
	
	private int updateFlag=0;
	
	private String participants;
	
	
	public long getSchtId() {
		return schtId;
	}
	public void setSchtId(long schtId) {
		this.schtId = schtId;
	}
	public long getSchId() {
		return schId;
	}
	public void setSchId(long schId) {
		this.schId = schId;
	}
	
	
	public long getSchtStaffId() {
		return schtStaffId;
	}
	public void setSchtStaffId(long schtStaffId) {
		this.schtStaffId = schtStaffId;
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
	public String getPlanDayName() {
		return planDayName;
	}
	public void setPlanDayName(String planDayName) {
		this.planDayName = planDayName;
	}
	public String getPlanDayTime() {
		return planDayTime;
	}
	public void setPlanDayTime(String planDayTime) {
		this.planDayTime = planDayTime;
	}
	public int getPlanCount() {
		return planCount;
	}
	public void setPlanCount(int planCount) {
		this.planCount = planCount;
	}
	public String getPlanStatusComment() {
		return planStatusComment;
	}
	public void setPlanStatusComment(String planStatusComment) {
		this.planStatusComment = planStatusComment;
	}
	public int getPlanStatus() {
		return planStatus;
	}
	public void setPlanStatus(int planStatus) {
		this.planStatus = planStatus;
	}
	@Override
	public String toString() {
		
		/*
		 * private long schtId;
	private long schId;
	private Date planDate;
	private String planDateStr;
	private int bonusPayed;
	private long schtStaffId;
	
	private String planDayName;
	private String planDayTime;
	private int planCount;
	
	private String planStatusComment;
	private int planStatus;
		 */
		
		////System.out.println("------------------------------------------");
		////System.out.println("-------------SCHEDULE TIME PLAN----------------");
		////System.out.println("------------------------------------------");
		////System.out.println("schtId:"+schtId);
		////System.out.println("planStartDate:"+planStartDate);
		////System.out.println("planStartDateStr:"+planStartDateStr);
		////System.out.println("planEndDate:"+planEndDate);
		////System.out.println("planEndDateStr:"+planEndDateStr);
		////System.out.println("schtStaffId:"+schtStaffId);
		////System.out.println("planDayName:"+planDayName);
		////System.out.println("planDayTime:"+planDayTime);
		////System.out.println("planCount:"+planCount);
		////System.out.println("planStatusComment:"+planStatusComment);
		////System.out.println("planStatus:"+planStatus);
		////System.out.println("------------------------------------------");
		////System.out.println("---------END-SCHEDULE TIME PLAN----------------");
		////System.out.println("------------------------------------------");
		return super.toString();
	}
	
	public List<ScheduleStudios> getScheduleStudios() {
		return scheduleStudios;
	}
	public void setScheduleStudios(List<ScheduleStudios> scheduleStudios) {
		this.scheduleStudios = scheduleStudios;
	}
	public List<ScheduleFactory> getUsers() {
		return users;
	}
	public void setUsers(List<ScheduleFactory> users) {
		this.users = users;
	}
	public String getStaffName() {
		return staffName;
	}
	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}
	public User getStaff() {
		return staff;
	}
	public void setStaff(User staff) {
		this.staff = staff;
	}
	public Date getPlanStartDate() {
		return planStartDate;
	}
	public void setPlanStartDate(Date planStartDate) {
		this.planStartDate = planStartDate;
	}
	public Date getPlanEndDate() {
		return planEndDate;
	}
	public void setPlanEndDate(Date planEndDate) {
		this.planEndDate = planEndDate;
	}
	public String getPlanStartDateStr() {
		return planStartDateStr;
	}
	public void setPlanStartDateStr(String planStartDateStr) {
		this.planStartDateStr = planStartDateStr;
	}
	public String getPlanEndDateStr() {
		return planEndDateStr;
	}
	public void setPlanEndDateStr(String planEndDateStr) {
		this.planEndDateStr = planEndDateStr;
	}
	public int getProgType() {
		return progType;
	}
	public void setProgType(int progType) {
		this.progType = progType;
	}
	public String getProgName() {
		return progName;
	}
	public void setProgName(String progName) {
		this.progName = progName;
	}
	public int getProgId() {
		return progId;
	}
	public void setProgId(int progId) {
		this.progId = progId;
	}
	public int getLastPlan() {
		return lastPlan;
	}
	public void setLastPlan(int lastPlan) {
		this.lastPlan = lastPlan;
	}
	public String getPlanEndDayTime() {
		return planEndDayTime;
	}
	public void setPlanEndDayTime(String planEndDayTime) {
		this.planEndDayTime = planEndDayTime;
	}
	public int getUpdateFlag() {
		return updateFlag;
	}
	public void setUpdateFlag(int updateFlag) {
		this.updateFlag = updateFlag;
	}
	public String getSequence() {
		return sequence;
	}
	public void setSequence(String sequence) {
		this.sequence = sequence;
	}
	public int getSchCount() {
		return schCount;
	}
	public void setSchCount(int schCount) {
		this.schCount = schCount;
	}
	public String getUserGsm() {
		return userGsm;
	}
	public void setUserGsm(String userGsm) {
		this.userGsm = userGsm;
	}
	public String getProgShortName() {
		return progShortName;
	}
	public void setProgShortName(String progShortName) {
		this.progShortName = progShortName;
	}
	public int getStatuTp() {
		return statuTp;
	}
	public void setStatuTp(int statuTp) {
		this.statuTp = statuTp;
	}
	public int getFirstPlan() {
		return firstPlan;
	}
	public void setFirstPlan(int firstPlan) {
		this.firstPlan = firstPlan;
	}
	public String getTpComment() {
		return tpComment;
	}
	public void setTpComment(String tpComment) {
		this.tpComment = tpComment;
	}
	public String getParticipants() {
		return participants;
	}
	public void setParticipants(String participants) {
		this.participants = participants;
	}

	
	
	
	
}
