package tr.com.abasus.ptboss.settings.entity;

import java.util.Date;

import tr.com.abasus.util.GlobalUtil;

public class PtAction {

	private long ptaId;
	private long ptaTo;
	private long ptaFrom;
	private String ptaDesc;
	private String ptaSubject;
	private Date createTime=GlobalUtil.getCurrentDateByTimeZone();
	private int ptaGrpId;
	private String ptaUid;
	public long getPtaId() {
		return ptaId;
	}
	public void setPtaId(long ptaId) {
		this.ptaId = ptaId;
	}
	public long getPtaTo() {
		return ptaTo;
	}
	public void setPtaTo(long ptaTo) {
		this.ptaTo = ptaTo;
	}
	public long getPtaFrom() {
		return ptaFrom;
	}
	public void setPtaFrom(long ptaFrom) {
		this.ptaFrom = ptaFrom;
	}
	public String getPtaDesc() {
		return ptaDesc;
	}
	public void setPtaDesc(String ptaDesc) {
		this.ptaDesc = ptaDesc;
	}
	public String getPtaSubject() {
		return ptaSubject;
	}
	public void setPtaSubject(String ptaSubject) {
		this.ptaSubject = ptaSubject;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public int getPtaGrpId() {
		return ptaGrpId;
	}
	public void setPtaGrpId(int ptaGrpId) {
		this.ptaGrpId = ptaGrpId;
	}
	public String getPtaUid() {
		return ptaUid;
	}
	public void setPtaUid(String ptaUid) {
		this.ptaUid = ptaUid;
	}
	
	
	
	
}
