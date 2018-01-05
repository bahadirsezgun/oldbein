package tr.com.abasus.ptboss.settings.entity;

public class PtRestrictions {

	private int resId;
	private String firmCount;
	private String teacherCount;
	
	private int individualRestriction;
	private int groupRestriction;
	private int membershipRestriction;
	
	private int uniquePacket;
	
	private int whichPacket;
	
	private int studioDefinedForBooking;
	
	
	public int getResId() {
		return resId;
	}
	public void setResId(int resId) {
		this.resId = resId;
	}
	public String getFirmCount() {
		return firmCount;
	}
	public void setFirmCount(String firmCount) {
		this.firmCount = firmCount;
	}
	public String getTeacherCount() {
		return teacherCount;
	}
	public void setTeacherCount(String teacherCount) {
		this.teacherCount = teacherCount;
	}
	public int getIndividualRestriction() {
		return individualRestriction;
	}
	public void setIndividualRestriction(int individualRestriction) {
		this.individualRestriction = individualRestriction;
	}
	public int getGroupRestriction() {
		return groupRestriction;
	}
	public void setGroupRestriction(int groupRestriction) {
		this.groupRestriction = groupRestriction;
	}
	public int getMembershipRestriction() {
		return membershipRestriction;
	}
	public void setMembershipRestriction(int membershipRestriction) {
		this.membershipRestriction = membershipRestriction;
	}
	public int getUniquePacket() {
		return uniquePacket;
	}
	public void setUniquePacket(int uniquePacket) {
		this.uniquePacket = uniquePacket;
	}
	public int getWhichPacket() {
		return whichPacket;
	}
	public void setWhichPacket(int whichPacket) {
		this.whichPacket = whichPacket;
	}
	public int getStudioDefinedForBooking() {
		return studioDefinedForBooking;
	}
	public void setStudioDefinedForBooking(int studioDefinedForBooking) {
		this.studioDefinedForBooking = studioDefinedForBooking;
	}
	
	
	
	
	
}
