package tr.com.abasus.ptboss.definition.entity;

import java.util.Date;

import tr.com.abasus.util.GlobalUtil;

public class DefStudio {

	private int studioId;
	private String studioName;
	private String studioShortName;
	private int firmId;
	private String firmName;
	private int studioStatus;
	private Date createTime=GlobalUtil.getCurrentDateByTimeZone();
	private String createTimeStr;
	private boolean selected=false;
	
	public int getStudioId() {
		return studioId;
	}
	public void setStudioId(int studioId) {
		this.studioId = studioId;
	}
	public String getStudioName() {
		return studioName;
	}
	public void setStudioName(String studioName) {
		this.studioName = studioName;
	}
	public String getStudioShortName() {
		return studioShortName;
	}
	public void setStudioShortName(String studioShortName) {
		this.studioShortName = studioShortName;
	}
	public int getFirmId() {
		return firmId;
	}
	public void setFirmId(int firmId) {
		this.firmId = firmId;
	}
	public int getStudioStatus() {
		return studioStatus;
	}
	public void setStudioStatus(int studioStatus) {
		this.studioStatus = studioStatus;
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
	public String getFirmName() {
		return firmName;
	}
	public void setFirmName(String firmName) {
		this.firmName = firmName;
	}
	
	
	
	
}
