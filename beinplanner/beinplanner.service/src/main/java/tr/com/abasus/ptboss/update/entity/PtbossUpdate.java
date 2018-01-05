package tr.com.abasus.ptboss.update.entity;

import java.io.Serializable;
import java.util.Date;

public class PtbossUpdate implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int updIdx;
	private Date updDate;
	private String updDateStr;
	private String updVer;
	private int updStatu;
	
	
	public int getUpdIdx() {
		return updIdx;
	}
	public void setUpdIdx(int updIdx) {
		this.updIdx = updIdx;
	}
	public Date getUpdDate() {
		return updDate;
	}
	public void setUpdDate(Date updDate) {
		this.updDate = updDate;
	}
	public String getUpdDateStr() {
		return updDateStr;
	}
	public void setUpdDateStr(String updDateStr) {
		this.updDateStr = updDateStr;
	}
	public String getUpdVer() {
		return updVer;
	}
	public void setUpdVer(String updVer) {
		this.updVer = updVer;
	}
	public int getUpdStatu() {
		return updStatu;
	}
	public void setUpdStatu(int updStatu) {
		this.updStatu = updStatu;
	}
	
	
	
}
