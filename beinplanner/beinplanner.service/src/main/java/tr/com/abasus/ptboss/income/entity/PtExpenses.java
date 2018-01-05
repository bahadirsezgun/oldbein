package tr.com.abasus.ptboss.income.entity;

import java.util.Date;

public class PtExpenses {

	private long 	peId;
	private double 	peAmount;
	private Date 	peDate;
	private String 	peDateStr;
	private String 	peComment;
	private int 	firmId;
	private String 	firmName;
    private int 	peInOut;
	
	private String 	monthName;
	private double 	totalExpense;
	private double 	totalIncome;
	
	public double getPeAmount() {
		return peAmount;
	}
	public void setPeAmount(double peAmount) {
		this.peAmount = peAmount;
	}
	public Date getPeDate() {
		return peDate;
	}
	public void setPeDate(Date peDate) {
		this.peDate = peDate;
	}
	public String getPeComment() {
		return peComment;
	}
	public void setPeComment(String peComment) {
		this.peComment = peComment;
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
	public long getPeId() {
		return peId;
	}
	public void setPeId(long peId) {
		this.peId = peId;
	}
	public String getPeDateStr() {
		return peDateStr;
	}
	public void setPeDateStr(String peDateStr) {
		this.peDateStr = peDateStr;
	}
	public String getMonthName() {
		return monthName;
	}
	public void setMonthName(String monthName) {
		this.monthName = monthName;
	}
	public double getTotalExpense() {
		return totalExpense;
	}
	public void setTotalExpense(double totalExpense) {
		this.totalExpense = totalExpense;
	}
	/**
	 * @comment 1- Income 0- Expense
	 * 
	 * @return int
	 */
	public int getPeInOut() {
		return peInOut;
	}
	public void setPeInOut(int peInOut) {
		this.peInOut = peInOut;
	}
	public double getTotalIncome() {
		return totalIncome;
	}
	public void setTotalIncome(double totalIncome) {
		this.totalIncome = totalIncome;
	}
	
	
	
}
