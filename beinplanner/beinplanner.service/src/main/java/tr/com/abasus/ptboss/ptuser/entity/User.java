package tr.com.abasus.ptboss.ptuser.entity;

import java.util.Date;
import java.util.List;

import tr.com.abasus.ptboss.menu.entity.MenuTbl;
import tr.com.abasus.util.GlobalUtil;

public class User {

    private int 		userType;
	
	private long 		userId;
	private String 		userSsn;
	private String 		userName;
	private String 		userSurname;
	private String 		password;
	private String 		oldPassword;
	private Date 		userBirthday;
	private String  	userBirthdayStr;
	private int 		firmId;
	private String  	firmName;
	private int  		stateId;
    private int  		cityId;
    private String 		stateName;
    private String 		cityName;
    private String 		userAddress;
	private String 		userPhone;
	private String 		userGsm;
	private String 		userEmail;
	private String  	profileUrl;
	private int 		urlType;
	private int 		bonusTypeP;
	private int 		bonusTypeC;
	
	
	//private DefState 	defState;
	//private DefCity  	defCity;
	private int 		userGender;
	private String 		userComment;
	private long 		staffId;
	private String 		staffType;
	private String 		staffName;
	private Date 		createTime=GlobalUtil.getCurrentDateByTimeZone();
	private String 		createTimeStr;
	private int 		staffStatu;
	
	private int 		saleStatu;
	private int 		saleStatuStr;
	private long 		saleId;
	
	private long 		sucpId;
	private long 		suppId;
	private long 		schtId;
	private long 		schId;
	private String 		type;
	    
	private int 		saleCount;
	private int         progId;
	private String 		progName;
	private MenuTbl dashBoardMenu;
	
	private List<MenuTbl> menuTbls;
	private List<MenuTbl> menuTopTbls;
	
	/*********PACKET INFORMATION*******************/
	private int packetCount;
	private double leftPayment;
	private double payAmount;
	/*********************************************/
	
	/*************MAIL ATTRIBUTES***************************************************/
	private String mailSubject;
	private String mailContent;
	
	private int groupRestriction;
	private int individualRestriction;
	private int membershipRestriction;
	private int uniquePacket;
	private int whichPacket;
	private String dateFormat;
	private int studioDefined;
	
	
	private String ptCurrency;
	
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
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
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
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
	public int getStateId() {
		return stateId;
	}
	public void setStateId(int stateId) {
		this.stateId = stateId;
	}
	public int getCityId() {
		return cityId;
	}
	public void setCityId(int cityId) {
		this.cityId = cityId;
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
	public int getBonusTypeP() {
		return bonusTypeP;
	}
	public void setBonusTypeP(int bonusTypeP) {
		this.bonusTypeP = bonusTypeP;
	}
	public int getBonusTypeC() {
		return bonusTypeC;
	}
	public void setBonusTypeC(int bonusTypeC) {
		this.bonusTypeC = bonusTypeC;
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
	public long getStaffId() {
		return staffId;
	}
	public void setStaffId(long staffId) {
		this.staffId = staffId;
	}
	public String getStaffType() {
		return staffType;
	}
	public void setStaffType(String staffType) {
		this.staffType = staffType;
	}
	public String getStaffName() {
		return staffName;
	}
	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
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
	public List<MenuTbl> getMenuTbls() {
		return menuTbls;
	}
	public void setMenuTbls(List<MenuTbl> menuTbls) {
		this.menuTbls = menuTbls;
	}
	public List<MenuTbl> getMenuTopTbls() {
		return menuTopTbls;
	}
	public void setMenuTopTbls(List<MenuTbl> menuTopTbls) {
		this.menuTopTbls = menuTopTbls;
	}
	public int getUserType() {
		return userType;
	}
	public void setUserType(int userType) {
		this.userType = userType;
	}
	public int getSaleStatu() {
		return saleStatu;
	}
	public void setSaleStatu(int saleStatu) {
		this.saleStatu = saleStatu;
	}
	public int getSaleStatuStr() {
		return saleStatuStr;
	}
	public void setSaleStatuStr(int saleStatuStr) {
		this.saleStatuStr = saleStatuStr;
	}
	public long getSaleId() {
		return saleId;
	}
	public void setSaleId(long saleId) {
		this.saleId = saleId;
	}
	public long getSucpId() {
		return sucpId;
	}
	public void setSucpId(long sucpId) {
		this.sucpId = sucpId;
	}
	public long getSuppId() {
		return suppId;
	}
	public void setSuppId(long suppId) {
		this.suppId = suppId;
	}
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
	public int getSaleCount() {
		return saleCount;
	}
	public void setSaleCount(int saleCount) {
		this.saleCount = saleCount;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public MenuTbl getDashBoardMenu() {
		return dashBoardMenu;
	}
	public void setDashBoardMenu(MenuTbl dashBoardMenu) {
		this.dashBoardMenu = dashBoardMenu;
	}
	public int getPacketCount() {
		return packetCount;
	}
	public void setPacketCount(int packetCount) {
		this.packetCount = packetCount;
	}
	public double getLeftPayment() {
		return leftPayment;
	}
	public void setLeftPayment(double leftPayment) {
		this.leftPayment = leftPayment;
	}
	public double getPayAmount() {
		return payAmount;
	}
	public void setPayAmount(double payAmount) {
		this.payAmount = payAmount;
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
	public String getOldPassword() {
		return oldPassword;
	}
	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}
	public int getGroupRestriction() {
		return groupRestriction;
	}
	public void setGroupRestriction(int groupRestriction) {
		this.groupRestriction = groupRestriction;
	}
	public int getIndividualRestriction() {
		return individualRestriction;
	}
	public void setIndividualRestriction(int individualRestriction) {
		this.individualRestriction = individualRestriction;
	}
	public int getMembershipRestriction() {
		return membershipRestriction;
	}
	public void setMembershipRestriction(int membershipRestriction) {
		this.membershipRestriction = membershipRestriction;
	}
	public String getPtCurrency() {
		return ptCurrency;
	}
	public void setPtCurrency(String ptCurrency) {
		this.ptCurrency = ptCurrency;
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
	public String getDateFormat() {
		return dateFormat;
	}
	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}
	public int getStudioDefined() {
		return studioDefined;
	}
	public void setStudioDefined(int studioDefined) {
		this.studioDefined = studioDefined;
	}
	

	
	
}
