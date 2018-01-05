package tr.com.abasus.ptboss.bonus.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;

@JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include=As.PROPERTY, property="bonType")
@JsonSubTypes({@JsonSubTypes.Type(value = UserBonusPaymentClass.class, name = "bpc"),
			   @JsonSubTypes.Type(value = UserBonusPaymentPersonal.class, name = "bpp")})
public abstract class UserBonusPaymentFactory implements UserBonusPaymentInterface{
	private long bonId;
    private String bonType;
	private long userId;
	private Date bonPaymentDate;
	private String bonPaymentDateStr;
	private double bonAmount;
	private String bonComment;
	private int bonMonth;
	private String bonMonthName;
	private int bonYear;
	private Date bonStartDate;
	private Date bonEndDate;
	private String bonStartDateStr;
	private String bonEndDateStr;
	private int bonQueryType;
	
	/********************************************************/
	/**************USER ATTRIBUTES***************************/
	/********************************************************/
	private int 		userType;
	private String 		userSsn;
	private String 		userName;
	private String 		userSurname;
	private String  	userBirthdayStr;
	private int 		firmId;
	private String  	firmName;
	private String 		userAddress;
	private String 		userPhone;
	private String 		userGsm;
	private String 		userEmail;
	private String  	profileUrl;
	private int 		urlType;
	private int 		userGender;
	private String 		userComment;
	private String 		createTimeStr;
	private int 		staffStatu;
	
	
	
	public long getBonId() {
		return bonId;
	}
	public void setBonId(long bonId) {
		this.bonId = bonId;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public Date getBonPaymentDate() {
		return bonPaymentDate;
	}
	public void setBonPaymentDate(Date bonPaymentDate) {
		this.bonPaymentDate = bonPaymentDate;
	}
	public String getBonPaymentDateStr() {
		return bonPaymentDateStr;
	}
	public void setBonPaymentDateStr(String bonPaymentDateStr) {
		this.bonPaymentDateStr = bonPaymentDateStr;
	}
	public double getBonAmount() {
		return bonAmount;
	}
	public void setBonAmount(double bonAmount) {
		this.bonAmount = bonAmount;
	}
	public String getBonComment() {
		return bonComment;
	}
	public void setBonComment(String bonComment) {
		this.bonComment = bonComment;
	}
	public int getBonMonth() {
		return bonMonth;
	}
	public void setBonMonth(int bonMonth) {
		this.bonMonth = bonMonth;
	}
	public int getBonYear() {
		return bonYear;
	}
	public void setBonYear(int bonYear) {
		this.bonYear = bonYear;
	}
	public Date getBonStartDate() {
		return bonStartDate;
	}
	public void setBonStartDate(Date bonStartDate) {
		this.bonStartDate = bonStartDate;
	}
	public Date getBonEndDate() {
		return bonEndDate;
	}
	public void setBonEndDate(Date bonEndDate) {
		this.bonEndDate = bonEndDate;
	}
	public String getBonStartDateStr() {
		return bonStartDateStr;
	}
	public void setBonStartDateStr(String bonStartDateStr) {
		this.bonStartDateStr = bonStartDateStr;
	}
	public String getBonEndDateStr() {
		return bonEndDateStr;
	}
	public void setBonEndDateStr(String bonEndDateStr) {
		this.bonEndDateStr = bonEndDateStr;
	}
	public int getUserType() {
		return userType;
	}
	public void setUserType(int userType) {
		this.userType = userType;
	}
	public String getUserSsn() {
		return userSsn;
	}
	public void setUserSsn(String userSsn) {
		this.userSsn = userSsn;
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
	public int getFirmId() {
		return firmId;
	}
	public void setFirmId(int firmId) {
		this.firmId = firmId;
	}
	public String getFirmName() {
		return firmName;
	}
	public void setFirmName(String firmName) {
		this.firmName = firmName;
	}
	public String getUserAddress() {
		return userAddress;
	}
	public void setUserAddress(String userAddress) {
		this.userAddress = userAddress;
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
	public int getUserGender() {
		return userGender;
	}
	public void setUserGender(int userGender) {
		this.userGender = userGender;
	}
	public String getUserComment() {
		return userComment;
	}
	public void setUserComment(String userComment) {
		this.userComment = userComment;
	}
	public String getCreateTimeStr() {
		return createTimeStr;
	}
	public void setCreateTimeStr(String createTimeStr) {
		this.createTimeStr = createTimeStr;
	}
	public int getStaffStatu() {
		return staffStatu;
	}
	public void setStaffStatu(int staffStatu) {
		this.staffStatu = staffStatu;
	}
	public String getBonMonthName() {
		return bonMonthName;
	}
	public void setBonMonthName(String bonMonthName) {
		this.bonMonthName = bonMonthName;
	}
	public String getBonType() {
		return bonType;
	}
	public void setBonType(String bonType) {
		this.bonType = bonType;
	}

	public int getBonQueryType() {
		return bonQueryType;
	}
	public void setBonQueryType(int bonQueryType) {
		this.bonQueryType = bonQueryType;
	}
	
}
