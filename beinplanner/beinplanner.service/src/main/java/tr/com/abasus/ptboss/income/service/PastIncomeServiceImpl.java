package tr.com.abasus.ptboss.income.service;

import java.util.Date;
import java.util.List;

import tr.com.abasus.ptboss.income.dao.PastIncomeDao;
import tr.com.abasus.ptboss.income.entity.PastIncomeMonthTbl;
import tr.com.abasus.ptboss.income.entity.PtExpenses;
import tr.com.abasus.ptboss.result.entity.HmiResultObj;

public class PastIncomeServiceImpl implements PastIncomeService{

	PastIncomeDao pastIncomeDao;
	/*
	@Override
	public List<PastIncomeMonthTbl> findPastForYear(int year, int firmId) {
		return pastIncomeDao.findPastForYear(year, firmId);
	}

	@Override
	public PastIncomeMonthTbl findPastForYearAndMonth(int year, int month, int firmId) {
		return pastIncomeDao.findPastForYearAndMonth(year, month, firmId);
	}

	@Override
	public HmiResultObj createPast(PastIncomeMonthTbl pastIncomeMonthTbl) {
		return pastIncomeDao.createPast(pastIncomeMonthTbl);
	}

	@Override
	public HmiResultObj deletePast(PastIncomeMonthTbl pastIncomeMonthTbl) {
		return pastIncomeDao.deletePast(pastIncomeMonthTbl);
	}
	*/

	public PastIncomeDao getPastIncomeDao() {
		return pastIncomeDao;
	}

	public void setPastIncomeDao(PastIncomeDao pastIncomeDao) {
		this.pastIncomeDao = pastIncomeDao;
	}

	@Override
	public List<PtExpenses> findPtExpensesForMonth(int year, int month, int firmId) {
		return pastIncomeDao.findPtExpensesForMonth(year, month, firmId);
	}

	@Override
	public PtExpenses findPtExpensesById(long peId) {
		return pastIncomeDao.findPtExpensesById(peId);
	}

	@Override
	public HmiResultObj createPtExpenses(PtExpenses ptExpenses) {
		return pastIncomeDao.createPtExpenses(ptExpenses);
	}

	@Override
	public HmiResultObj deletePtExpenses(PtExpenses ptExpenses) {
		return pastIncomeDao.deletePtExpenses(ptExpenses);
	}

	@Override
	public List<PtExpenses> findPtExpensesForDate(Date startDate, Date endDate, int firmId) {
		return pastIncomeDao.findPtExpensesForDate(startDate, endDate, firmId);
	}
	
	

}
