package tr.com.abasus.ptboss.definition.entity;

import java.util.Date;

import tr.com.abasus.util.GlobalUtil;

public class DefFirm {

	private int firmId;
	private String firmName;
	private String firmPhone;
	private String firmAddress;
	private String firmEmail;

	
	private int firmCityId;
	private int firmStateId;
	private String cityName;
	private String stateName;
	private Date createTime=GlobalUtil.getCurrentDateByTimeZone();
	private String createTimeStr;
	private boolean selected;
	
	
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
	public String getFirmPhone() {
		return firmPhone;
	}
	public void setFirmPhone(String firmPhone) {
		this.firmPhone = firmPhone;
	}
	public String getFirmAddress() {
		return firmAddress;
	}
	public void setFirmAddress(String firmAddress) {
		this.firmAddress = firmAddress;
	}
	public String getFirmEmail() {
		return firmEmail;
	}
	public void setFirmEmail(String firmEmail) {
		this.firmEmail = firmEmail;
	}
	
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public String getStateName() {
		return stateName;
	}
	public void setStateName(String stateName) {
		this.stateName = stateName;
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
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	public int getFirmCityId() {
		return firmCityId;
	}
	public void setFirmCityId(int firmCityId) {
		this.firmCityId = firmCityId;
	}
	public int getFirmStateId() {
		return firmStateId;
	}
	public void setFirmStateId(int firmStateId) {
		this.firmStateId = firmStateId;
	}
	
	
}
