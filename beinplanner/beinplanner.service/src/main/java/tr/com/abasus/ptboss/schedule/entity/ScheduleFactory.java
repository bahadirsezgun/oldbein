package tr.com.abasus.ptboss.schedule.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;

import tr.com.abasus.ptboss.packetpayment.entity.PacketPaymentFactory;
import tr.com.abasus.ptboss.packetsale.entity.PacketSaleFactory;

@JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include=As.PROPERTY, property="type")
@JsonSubTypes({@JsonSubTypes.Type(value = ScheduleUsersClassPlan.class, name = "sucp"),
			   @JsonSubTypes.Type(value = ScheduleMembershipPlan.class, name = "smp"),
			   @JsonSubTypes.Type(value = ScheduleUsersPersonalPlan.class, name = "supp")})
public abstract class ScheduleFactory implements ScheduleInterface {

	private String 	type;
	private long 	userId;
	private long 	saleId;
	private int 	firmId;
	private Date 	smpStartDate;
	private Date 	smpEndDate;
	private String 	smpStartDateStr;
	private String 	smpEndDateStr;
	private int 	smpFreezeCount;
	private double 	smpPrice;
	private int 	progId;
	private String 	progName;
	private int 	smpStatus;
	private String 	smpComment;
	private long 	schtId;
	
	private Date 	planStartDate;
	private Date 	planEndDate;
	private String 	planStartDateStr;
	private String 	planEndDateStr;
	
	
	private PacketSaleFactory packetSaleFactory;
	
	private PacketPaymentFactory packetPaymentFactory;
	/*******************SCHEDULE USERS MEMBERSHIP PLAN EXTRA********/
	private long smpId;
	
	
	/*******************SCHEDULE USERS CLASS PLAN EXTRA********/
	private long sucpId;
	
	
	/*******************SCHEDULE USERS PERSONAL PLAN EXTRA********/
	private long suppId;
	
	private String 		userName;
	private String 		userSurname;
	private Date 		userBirthday;
	private String  	userBirthdayStr;
	private String 		userPhone;
	private String 		userGsm;
	private String 		userEmail;
	private String  	profileUrl;
	private int 		urlType;
	private int 		saleCount;
	private int 		userGender;
	
	
	private double unitPrice;
	
	private double packetPrice;
	
	private int statuTp;
	
	public long getSmpId() {
		return smpId;
	}
	public void setSmpId(long smpId) {
		this.smpId = smpId;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public long getSaleId() {
		return saleId;
	}
	public void setSaleId(long saleId) {
		this.saleId = saleId;
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
	public int getSmpFreezeCount() {
		return smpFreezeCount;
	}
	public void setSmpFreezeCount(int smpFreezeCount) {
		this.smpFreezeCount = smpFreezeCount;
	}
	
	public double getSmpPrice() {
		return smpPrice;
	}
	public void setSmpPrice(double smpPrice) {
		this.smpPrice = smpPrice;
	}
	public int getProgId() {
		return progId;
	}
	public void setProgId(int progId) {
		this.progId = progId;
	}
	public String getProgName() {
		return progName;
	}
	public void setProgName(String progName) {
		this.progName = progName;
	}
	public int getSmpStatus() {
		return smpStatus;
	}
	public void setSmpStatus(int smpStatus) {
		this.smpStatus = smpStatus;
	}
	public String getSmpComment() {
		return smpComment;
	}
	public void setSmpComment(String smpComment) {
		this.smpComment = smpComment;
	}
	public long getSucpId() {
		return sucpId;
	}
	public void setSucpId(long sucpId) {
		this.sucpId = sucpId;
	}
	public long getSchtId() {
		return schtId;
	}
	public void setSchtId(long schtId) {
		this.schtId = schtId;
	}
	public long getSuppId() {
		return suppId;
	}
	public void setSuppId(long suppId) {
		this.suppId = suppId;
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Date getUserBirthday() {
		return userBirthday;
	}
	public void setUserBirthday(Date userBirthday) {
		this.userBirthday = userBirthday;
	}
	public String getUserBirthdayStr() {
		return userBirthdayStr;
	}
	public void setUserBirthdayStr(String userBirthdayStr) {
		this.userBirthdayStr = userBirthdayStr;
	}
	public String getUserPhone() {
		return userPhone;
	}
	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
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
	public PacketSaleFactory getPacketSaleFactory() {
		return packetSaleFactory;
	}
	public void setPacketSaleFactory(PacketSaleFactory packetSaleFactory) {
		this.packetSaleFactory = packetSaleFactory;
	}
	public int getFirmId() {
		return firmId;
	}
	public void setFirmId(int firmId) {
		this.firmId = firmId;
	}

	public int getSaleCount() {
		return saleCount;
	}
	public void setSaleCount(int saleCount) {
		this.saleCount = saleCount;
	}
	public int getUserGender() {
		return userGender;
	}
	public void setUserGender(int userGender) {
		this.userGender = userGender;
	}
	public double getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}
	public PacketPaymentFactory getPacketPaymentFactory() {
		return packetPaymentFactory;
	}
	public void setPacketPaymentFactory(PacketPaymentFactory packetPaymentFactory) {
		this.packetPaymentFactory = packetPaymentFactory;
	}
	public double getPacketPrice() {
		return packetPrice;
	}
	public void setPacketPrice(double packetPrice) {
		this.packetPrice = packetPrice;
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
	
	public int getStatuTp() {
		return statuTp;
	}
	public void setStatuTp(int statuTp) {
		this.statuTp = statuTp;
	}
	
	
	
}
