package tr.com.abasus.ptboss.packetpayment.entity;

import java.util.Date;

public abstract class PacketPaymentDetailFactory {
	private long payDetId;
	
	private long payId;
	
	private double payAmount;
	
	private Date payDate;
	
	private String payDateStr;
	
	private Date changeDate;

	
	private int payType;
	
	private String payComment;
	
	private int payConfirm;
	
	public long getPayDetId() {
		return payDetId;
	}

	public void setPayDetId(long payDetId) {
		this.payDetId = payDetId;
	}

	public long getPayId() {
		return payId;
	}

	public void setPayId(long payId) {
		this.payId = payId;
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

	public Date getChangeDate() {
		return changeDate;
	}

	public void setChangeDate(Date changeDate) {
		this.changeDate = changeDate;
	}

	public int getPayType() {
		return payType;
	}

	public void setPayType(int payType) {
		this.payType = payType;
	}

	public String getPayComment() {
		return payComment;
	}

	public void setPayComment(String payComment) {
		this.payComment = payComment;
	}

	public String getPayDateStr() {
		return payDateStr;
	}

	public void setPayDateStr(String payDateStr) {
		this.payDateStr = payDateStr;
	}

	public int getPayConfirm() {
		return payConfirm;
	}

	public void setPayConfirm(int payConfirm) {
		this.payConfirm = payConfirm;
	}
}
