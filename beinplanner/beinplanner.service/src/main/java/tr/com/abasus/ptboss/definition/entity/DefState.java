package tr.com.abasus.ptboss.definition.entity;

import java.io.Serializable;
import java.util.Date;

import tr.com.abasus.util.GlobalUtil;

@SuppressWarnings("serial")
public class DefState implements Serializable {

	private int stateId;
	private String stateName;
	private Date createTime=GlobalUtil.getCurrentDateByTimeZone();
	private String createTimeStr;
	private boolean selected;
	
	public int getStateId() {
		return stateId;
	}
	public void setStateId(int stateId) {
		this.stateId = stateId;
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
	
}
