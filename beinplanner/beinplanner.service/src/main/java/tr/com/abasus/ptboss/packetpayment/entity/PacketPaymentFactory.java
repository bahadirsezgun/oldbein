package tr.com.abasus.ptboss.packetpayment.entity;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;

import tr.com.abasus.ptboss.packetsale.entity.PacketSaleClass;
import tr.com.abasus.ptboss.packetsale.entity.PacketSaleMembership;
import tr.com.abasus.ptboss.packetsale.entity.PacketSalePersonal;

@JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include=As.PROPERTY, property="type")
@JsonSubTypes({@JsonSubTypes.Type(value = PacketPaymentPersonal.class, name = "ppp"),
			   @JsonSubTypes.Type(value = PacketPaymentClass.class, name = "ppc"),
			   @JsonSubTypes.Type(value = PacketPaymentMembership.class, name = "ppm")})
public abstract class PacketPaymentFactory implements PacketPaymentInteface{

	
	private String type;
	
	private long payId;
	
	private long saleId;
	
	private double payAmount;
	
	private Date payDate;
	
	private String payDateStr;
	
	private int payConfirm;
	
	private Date changeDate;
	
	private String changeDateStr;
	
	private int payType;
	
	private String payComment;
	
	private double packetPrice;
	
	private String salesDateStr;
	
	
	
	private List<PacketPaymentDetailFactory> packetPaymentDetailFactories;
	
	
	/***************************************************************/
	/**********************USER ***********************************/
	/**************************************************************/
	
	private int 		userId;
	private String 		userName;
	private String 		userSurname;
	private String  	firmName;
	private String 		stateName;
    private String 		cityName;
    private String 		userAddress;
	private String 		userPhone;
	private String 		userGsm;
	private String 		userEmail;
	private String  	profileUrl;
	private int 		urlType;
	private int 		userGender;
	private String 		progName;

	/*************MAIL ATTRIBUTES***************************************************/
	private String mailSubject;
	private String mailContent;
	
	private int progCount;
	
	public long getPayId() {
		return payId;
	}

	public void setPayId(long payId) {
		this.payId = payId;
	}

	public long getSaleId() {
		return saleId;
	}

	public void setSaleId(long saleId) {
		this.saleId = saleId;
	}

	public double getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(double payAmount) {
		this.payAmount = payAmount;
	}

	public Date getPayDate() {
		return payDate;
	}

	public void setPayDate(Date payDate) {
		this.payDate = payDate;
	}

	public int getPayConfirm() {
		return payConfirm;
	}

	public void setPayConfirm(int payConfirm) {
		this.payConfirm = payConfirm;
	}

	public Date getChangeDate() {
		return changeDate;
	}

	public void setChangeDate(Date changeDate) {
		this.changeDate = changeDate;
	}

	

	

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPayDateStr() {
		return payDateStr;
	}

	public void setPayDateStr(String payDateStr) {
		this.payDateStr = payDateStr;
	}

	public String getChangeDateStr() {
		return changeDateStr;
	}

	public void setChangeDateStr(String changeDateStr) {
		this.changeDateStr = changeDateStr;
	}

	public int getPayType() {
		return payType;
	}

	public void setPayType(int payType) {
		this.payType = payType;
	}

	public List<PacketPaymentDetailFactory> getPacketPaymentDetailFactories() {
		return packetPaymentDetailFactories;
	}

	public void setPacketPaymentDetailFactories(List<PacketPaymentDetailFactory> packetPaymentDetailFactories) {
		this.packetPaymentDetailFactories = packetPaymentDetailFactories;
	}

	public String getPayComment() {
		return payComment;
	}

	public void setPayComment(String payComment) {
		this.payComment = payComment;
	}

	public double getPacketPrice() {
		return packetPrice;
	}

	public void setPacketPrice(double packetPrice) {
		this.packetPrice = packetPrice;
	}

	public String getSalesDateStr() {
		return salesDateStr;
	}

	public void setSalesDateStr(String salesDateStr) {
		this.salesDateStr = salesDateStr;
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

	public String getFirmName() {
		return firmName;
	}

	public void setFirmName(String firmName) {
		this.firmName = firmName;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
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

	public String getProgName() {
		return progName;
	}

	public void setProgName(String progName) {
		this.progName = progName;
	}

	public int getUserGender() {
		return userGender;
	}

	public void setUserGender(int userGender) {
		this.userGender = userGender;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
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

	public int getProgCount() {
		return progCount;
	}

	public void setProgCount(int progCount) {
		this.progCount = progCount;
	}

	

	
	
	
		
	
	
}
