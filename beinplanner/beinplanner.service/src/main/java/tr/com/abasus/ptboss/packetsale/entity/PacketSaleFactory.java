package tr.com.abasus.ptboss.packetsale.entity;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;

import tr.com.abasus.ptboss.packetpayment.entity.PacketPaymentFactory;
import tr.com.abasus.ptboss.packetsale.decorator.IPacketSale;
import tr.com.abasus.ptboss.ptuser.entity.User;

@JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include=As.PROPERTY, property="progType")
@JsonSubTypes({@JsonSubTypes.Type(value = PacketSalePersonal.class, name = "psp"),
			   @JsonSubTypes.Type(value = PacketSaleClass.class, name = "psc"),
			   @JsonSubTypes.Type(value = PacketSaleMembership.class, name = "psm")})
public abstract class PacketSaleFactory implements PacketSaleInterface,IPacketSale {

	
	private long 	saleId;
	private long 	userId;
	private int 	progId;
	private int 	saleStatu;
	private String 	progName;
	private String 	progType;
	private String 	salesComment;
	private double 	packetPrice;
	private double 	leftPrice;
	
	private Date 	salesDate;
	private String 	salesDateStr;
	private Date 	changeDate;
	private String 	changeDateStr;
	private long 	staffId;
	private int 	progCount;
	private int 	progDurationType;
	private int 	progDuration;
	private String 	userName;
	private String 	userSurname;
	private String  userBirthdayStr;
	private String 	userPhone;
	private String 	userGsm;
	private String 	userEmail;
	private String  profileUrl;
	private int 	urlType;
	private int 	userGender;
	private String 	progDescription;
	private String 	progComment;
	private int 	firmId;
	private int 	progStatus;
	
	
	
	private int 	complete;
	
	
	
	private int bonusPayedFlag;
	
	
	private List<User> saledMembers;
	
	
	private User member;
	
	private PacketPaymentFactory packetPaymentFactory;
	private long 	payId;
	private double 	payAmount;
	
	/***************FOR MEMBERSHIP SCHEDULE***********************/
	private String smpStartDateStr;
	private String smpEndDateStr;
	private int smpStatus;
	private String smpComment;
	private long smpId;
	
	/******************************************/
	
	private String mailSubject;
	private String mailContent;
	
	
	private String ptCurrency;
	
	
	public long getSaleId() {
		return saleId;
	}

	public void setSaleId(long saleId) {
		this.saleId = saleId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public int getProgId() {
		return progId;
	}

	public void setProgId(int progId) {
		this.progId = progId;
	}

	
	public String getSalesComment() {
		return salesComment;
	}

	public void setSalesComment(String salesComment) {
		this.salesComment = salesComment;
	}

	public double getPacketPrice() {
		return packetPrice;
	}

	public void setPacketPrice(double packetPrice) {
		this.packetPrice = packetPrice;
	}

	public Date getSalesDate() {
		return salesDate;
	}

	public void setSalesDate(Date salesDate) {
		this.salesDate = salesDate;
	}

	public Date getChangeDate() {
		return changeDate;
	}

	public void setChangeDate(Date changeDate) {
		this.changeDate = changeDate;
	}

	public long getStaffId() {
		return staffId;
	}

	public void setStaffId(long staffId) {
		this.staffId = staffId;
	}

	public int getProgCount() {
		return progCount;
	}

	public void setProgCount(int progCount) {
		this.progCount = progCount;
	}

	

	

	

	

	public String getSalesDateStr() {
		return salesDateStr;
	}

	public void setSalesDateStr(String salesDateStr) {
		this.salesDateStr = salesDateStr;
	}

	public String getProgName() {
		return progName;
	}

	public void setProgName(String progName) {
		this.progName = progName;
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

	public String getProgDescription() {
		return progDescription;
	}

	public void setProgDescription(String progDescription) {
		this.progDescription = progDescription;
	}

	public String getProgComment() {
		return progComment;
	}

	public void setProgComment(String progComment) {
		this.progComment = progComment;
	}

	public int getFirmId() {
		return firmId;
	}

	public void setFirmId(int firmId) {
		this.firmId = firmId;
	}

	public int getProgStatus() {
		return progStatus;
	}

	public void setProgStatus(int progStatus) {
		this.progStatus = progStatus;
	}

	public int getUrlType() {
		return urlType;
	}

	public void setUrlType(int urlType) {
		this.urlType = urlType;
	}

	public int getComplete() {
		return complete;
	}

	public void setComplete(int complete) {
		this.complete = complete;
	}

	

	public String getProgType() {
		return progType;
	}

	public void setProgType(String progType) {
		this.progType = progType;
	}

	public int getBonusPayedFlag() {
		return bonusPayedFlag;
	}

	public void setBonusPayedFlag(int bonusPayedFlag) {
		this.bonusPayedFlag = bonusPayedFlag;
	}

	public PacketPaymentFactory getPacketPaymentFactory() {
		return packetPaymentFactory;
	}

	public void setPacketPaymentFactory(PacketPaymentFactory packetPaymentFactory) {
		this.packetPaymentFactory = packetPaymentFactory;
	}

	

	public User getMember() {
		return member;
	}

	public void setMember(User member) {
		this.member = member;
	}

	public void setSaledMembers(List<User> saledMembers) {
		this.saledMembers = saledMembers;
	}

	public List<User> getSaledMembers() {
		return saledMembers;
	}

	public double getLeftPrice() {
		return leftPrice;
	}

	public void setLeftPrice(double leftPrice) {
		this.leftPrice = leftPrice;
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

	public long getSmpId() {
		return smpId;
	}

	public void setSmpId(long smpId) {
		this.smpId = smpId;
	}

	public int getProgDurationType() {
		return progDurationType;
	}

	public void setProgDurationType(int progDurationType) {
		this.progDurationType = progDurationType;
	}

	public int getProgDuration() {
		return progDuration;
	}

	public void setProgDuration(int progDuration) {
		this.progDuration = progDuration;
	}

	public int getSaleStatu() {
		return saleStatu;
	}

	public void setSaleStatu(int saleStatu) {
		this.saleStatu = saleStatu;
	}

	public int getUserGender() {
		return userGender;
	}

	public void setUserGender(int userGender) {
		this.userGender = userGender;
	}

	public String getMailSubject() {
		return mailSubject;
	}

	public void setMailSubject(String mailSubject) {
		this.mailSubject = mailSubject;
	}

	public String getMailContent() {
		return mailContent;
	}

	public void setMailContent(String mailContent) {
		this.mailContent = mailContent;
	}

	public long getPayId() {
		return payId;
	}

	public void setPayId(long payId) {
		this.payId = payId;
	}

	public String getChangeDateStr() {
		return changeDateStr;
	}

	public void setChangeDateStr(String changeDateStr) {
		this.changeDateStr = changeDateStr;
	}

	public double getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(double payAmount) {
		this.payAmount = payAmount;
	}

	public String getPtCurrency() {
		return ptCurrency;
	}

	public void setPtCurrency(String ptCurrency) {
		this.ptCurrency = ptCurrency;
	}

	
	
	
}
