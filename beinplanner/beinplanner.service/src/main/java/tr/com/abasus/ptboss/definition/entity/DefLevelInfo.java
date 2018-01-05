package tr.com.abasus.ptboss.definition.entity;

import java.util.Date;

import tr.com.abasus.util.GlobalUtil;

public class DefLevelInfo {

	
	private int levelId;
	private String levelName;
	private Date createTime=GlobalUtil.getCurrentDateByTimeZone();
	private String createTimeStr;
	
	public int getLevelId() {
		return levelId;
	}
	public void setLevelId(int levelId) {
		this.levelId = levelId;
	}
	public String getLevelName() {
		return levelName;
	}
	public void setLevelName(String levelName) {
		this.levelName = levelName;
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
	
	
}
