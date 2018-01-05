package tr.com.abasus.ptboss.income.entity;

import java.io.Serializable;

public class PastIncomeMonthTbl implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int 	pimMonth;
	private String 	pimMonthName;
	private int 	pimYear;
	private double 	incomeGeneral;
	private double 	incomeFromMembers;
	private double 	totalIncome;
	private double 	expenseGeneral;
	private double 	expenseToBonus;
	private double 	totalExpense;
	private double 	totalEarn;
	
	private int 	firmId;
	private String 	firmName;
	public int getPimMonth() {
		return pimMonth;
	}
	public void setPimMonth(int pimMonth) {
		this.pimMonth = pimMonth;
	}
	public String getPimMonthName() {
		return pimMonthName;
	}
	public void setPimMonthName(String pimMonthName) {
		this.pimMonthName = pimMonthName;
	}
	public int getPimYear() {
		return pimYear;
	}
	public void setPimYear(int pimYear) {
		this.pimYear = pimYear;
	}
	public double getIncomeGeneral() {
		return incomeGeneral;
	}
	public void setIncomeGeneral(double incomeGeneral) {
		this.incomeGeneral = incomeGeneral;
	}
	public double getIncomeFromMembers() {
		return incomeFromMembers;
	}
	public void setIncomeFromMembers(double incomeFromMembers) {
		this.incomeFromMembers = incomeFromMembers;
	}
	public double getTotalIncome() {
		return totalIncome;
	}
	public void setTotalIncome(double totalIncome) {
		this.totalIncome = totalIncome;
	}
	public double getExpenseGeneral() {
		return expenseGeneral;
	}
	public void setExpenseGeneral(double expenseGeneral) {
		this.expenseGeneral = expenseGeneral;
	}
	public double getExpenseToBonus() {
		return expenseToBonus;
	}
	public void setExpenseToBonus(double expenseToBonus) {
		this.expenseToBonus = expenseToBonus;
	}
	public double getTotalExpense() {
		return totalExpense;
	}
	public void setTotalExpense(double totalExpense) {
		this.totalExpense = totalExpense;
	}
	public double getTotalEarn() {
		return totalEarn;
	}
	public void setTotalEarn(double totalEarn) {
		this.totalEarn = totalEarn;
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
	
	

	
	
}
