package tr.com.abasus.ptboss.program.entity;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
@JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include=As.PROPERTY, property="type")
@JsonSubTypes({@JsonSubTypes.Type(value = ProgramPersonal.class, name = "pp"),
			   @JsonSubTypes.Type(value = ProgramClass.class, name = "pc"),
			   @JsonSubTypes.Type(value = ProgramMembership.class, name = "pm")})
public abstract class ProgramFactory implements ProgramInterface {

	private String type;
	
	private int 	progId;
	private double 	progPrice;
	private String 	progName;
	private String progIcon;
	private String progShortName;
	
	/**
	 *  Personal ve Class icin saatlik bir tanimlama fakat Membership için
	 *  progDurationType a bagli olarak gunluk,aylık ve haftalik olarak verilir.
	 */
	private int 	progDuration;  
	
	/**
	 * Membership icin gecerlidir. ProgDuration tipi, gunluk,aylık ve haftalik 
	 * oldugunu belirtir.
	 * 0-Gunluk
	 * 1-Haftalik
	 * 2-Aylik
	 * Util olarak ProgDurationTypes icinden alinir.
	 */
	
	
	private Date 	createTime;//=GlobalUtil.getCurrentDateByTimeZone();
	private String 	createTimeStr;
	private int 	progBeforeDuration;
	private int 	progAfterDuration;
	private long 	progUserId;
	private String 	progDescription;
	private String 	progComment;
	private int 	firmId;
	private int 	progStatus;
	private int 	progCount;
	
	private String 	userName;
	private String 	userSurname;
	private String 	firmName;
	
	
	
	
	// Extra Program Class Attributes
	private int 	minMemberCount;
	private int 	maxMemberCount;
	
	private int 	restFlag;  
	private int 	restType;  
	private int 	restDuration;  
	
	
	private String 	dateFormat;
	
	// Extra Program Membership Attributes
	private int 	progDurationType;
	private int 	maxFreezeCount;
	private int 	freezeDuration;
	private int 	freezeDurationType;
	
	
	
	
	
	
	
	// Extra Program Membership Attributes
	private List<ProgramMembershipDetail> programMembershipDetails;

	private int progRestriction;
	
	public int getProgId() {
		return progId;
	}
	public void setProgId(int progId) {
		this.progId = progId;
	}
	public double getProgPrice() {
		return progPrice;
	}
	public void setProgPrice(double progPrice) {
		this.progPrice = progPrice;
	}
	public String getProgName() {
		return progName;
	}
	public void setProgName(String progName) {
		this.progName = progName;
	}
	public int getProgDuration() {
		return progDuration;
	}
	public void setProgDuration(int progDuration) {
		this.progDuration = progDuration;
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
	public int getProgBeforeDuration() {
		return progBeforeDuration;
	}
	public void setProgBeforeDuration(int progBeforeDuration) {
		this.progBeforeDuration = progBeforeDuration;
	}
	public int getProgAfterDuration() {
		return progAfterDuration;
	}
	public void setProgAfterDuration(int progAfterDuration) {
		this.progAfterDuration = progAfterDuration;
	}
	public long getProgUserId() {
		return progUserId;
	}
	public void setProgUserId(long progUserId) {
		this.progUserId = progUserId;
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
	public int getProgCount() {
		return progCount;
	}
	public void setProgCount(int progCount) {
		this.progCount = progCount;
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
	
	public String getDateFormat() {
		return dateFormat;
	}
	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getProgRestriction() {
		return progRestriction;
	}
	public void setProgRestriction(int progRestriction) {
		this.progRestriction = progRestriction;
	}
	
	
	public List<ProgramMembershipDetail> getProgramMembershipDetails() {
		return programMembershipDetails;
	}
	public void setProgramMembershipDetails(List<ProgramMembershipDetail> programMembershipDetails) {
		this.programMembershipDetails = programMembershipDetails;
	}
	public int getProgDurationType() {
		return progDurationType;
	}
	public void setProgDurationType(int progDurationType) {
		this.progDurationType = progDurationType;
	}
	public int getMaxFreezeCount() {
		return maxFreezeCount;
	}
	public void setMaxFreezeCount(int maxFreezeCount) {
		this.maxFreezeCount = maxFreezeCount;
	}
	public int getFreezeDuration() {
		return freezeDuration;
	}
	public void setFreezeDuration(int freezeDuration) {
		this.freezeDuration = freezeDuration;
	}
	public int getFreezeDurationType() {
		return freezeDurationType;
	}
	public void setFreezeDurationType(int freezeDurationType) {
		this.freezeDurationType = freezeDurationType;
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
	public String getProgIcon() {
		return progIcon;
	}
	public void setProgIcon(String progIcon) {
		this.progIcon = progIcon;
	}
	public String getProgShortName() {
		return progShortName;
	}
	public void setProgShortName(String progShortName) {
		this.progShortName = progShortName;
	}
	public int getRestFlag() {
		return restFlag;
	}
	public void setRestFlag(int restFlag) {
		this.restFlag = restFlag;
	}
	public int getRestType() {
		return restType;
	}
	public void setRestType(int restType) {
		this.restType = restType;
	}
	public int getRestDuration() {
		return restDuration;
	}
	public void setRestDuration(int restDuration) {
		this.restDuration = restDuration;
	}
}
