package tr.com.abasus.ptboss.income.dao;

import java.util.Date;
import java.util.List;

import tr.com.abasus.ptboss.income.entity.PastIncomeMonthTbl;
import tr.com.abasus.ptboss.income.entity.PtExpenses;
import tr.com.abasus.ptboss.result.entity.HmiResultObj;

public interface PastIncomeDao {
/*
    public List<PastIncomeMonthTbl> findPastForYear(int year,int firmId);
	
	public PastIncomeMonthTbl findPastForYearAndMonth(int year,int month,int firmId);
	
	public HmiResultObj createPast(PastIncomeMonthTbl pastIncomeMonthTbl);
	
	public HmiResultObj deletePast(PastIncomeMonthTbl pastIncomeMonthTbl);
*/	
    
	
	public List<PtExpenses> findPtExpensesForMonth(int year,int month,int firmId);
	
	public PtExpenses findPtExpensesById(long peId);
	
	public HmiResultObj createPtExpenses(PtExpenses ptExpenses);
	
	public HmiResultObj deletePtExpenses(PtExpenses ptExpenses);
	
	public List<PtExpenses> findPtExpensesForDate(Date startDate,Date endDate,int firmId);
	
}
