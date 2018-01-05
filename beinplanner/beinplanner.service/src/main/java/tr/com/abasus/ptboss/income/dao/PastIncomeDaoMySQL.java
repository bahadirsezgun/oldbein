package tr.com.abasus.ptboss.income.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tr.com.abasus.general.dao.AbaxJdbcDaoSupport;
import tr.com.abasus.general.dao.SqlDao;
import tr.com.abasus.general.exception.SqlErrorException;
import tr.com.abasus.ptboss.income.entity.PastIncomeMonthTbl;
import tr.com.abasus.ptboss.income.entity.PtExpenses;
import tr.com.abasus.ptboss.result.entity.HmiResultObj;
import tr.com.abasus.util.DateTimeUtil;
import tr.com.abasus.util.GlobalUtil;
import tr.com.abasus.util.OhbeUtil;
import tr.com.abasus.util.ResultStatuObj;
/**
 * 
 * @author bahadir
 *
 */
public class PastIncomeDaoMySQL extends AbaxJdbcDaoSupport implements PastIncomeDao {

	SqlDao sqlDao;
	public SqlDao getSqlDao() {
		return sqlDao;
	}

	public void setSqlDao(SqlDao sqlDao) {
		this.sqlDao = sqlDao;
	}
	
	/**
	 * TODO PastIncomeMonthTbl islemleri silinecektir.
	 */
/*
	@Override
	public List<PastIncomeMonthTbl> findPastForYear(int year, int firmId) {
		String sql="SELECT a.*,b.* "
				+ "  FROM past_income_month_tbl a,def_firm b "
				+ "  WHERE a.PIM_YEAR=:year "
				+ "    AND b.FIRM_ID=a.FIRM_ID"
				+ "  ORDER BY a.PIM_MONTH ";
		
		 Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("year",year);
		 ////System.out.println(sql+" "+year);
		  
	 	try {
			 List<PastIncomeMonthTbl> pastIncomeMonthTbls=findEntityList(sql, paramMap, PastIncomeMonthTbl.class);
			 
			 for (PastIncomeMonthTbl pastIncomeMonthTbl : pastIncomeMonthTbls) {
				 pastIncomeMonthTbl.setPimMonthName(DateTimeUtil.getMonthNamesBySequence(pastIncomeMonthTbl.getPimMonth()));
			 }
			 
			 
			 return pastIncomeMonthTbls;
		}catch (Exception e) {
			e.printStackTrace();
			 return null;
		}
	}

	@Override
	public PastIncomeMonthTbl findPastForYearAndMonth(int year, int month, int firmId) {
		String sql="SELECT a.*,b.* "
				+ "  FROM past_income_month_tbl a,def_firm b "
				+ "  WHERE a.PIM_YEAR=:year "
				+ "  AND a.PIM_MONTH=:month"
				+ "  AND a.FIRM_ID=:firmId   "
				+ " AND b.FIRM_ID=a.FIRM_ID"
				+ "  ORDER BY a.PIM_MONTH ";
		
		 Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("year",year);
		 paramMap.put("firmId",firmId);
		 paramMap.put("month",month);
		 
		  
	 	try {
			 PastIncomeMonthTbl pastIncomeMonthTbl=findEntityForObject(sql, paramMap, PastIncomeMonthTbl.class);
			 
			 	 pastIncomeMonthTbl.setPimMonthName(DateTimeUtil.getMonthNamesBySequence(pastIncomeMonthTbl.getPimMonth()));
			 
			 
			 
			 return pastIncomeMonthTbl;
		}catch (Exception e) {
			 return null;
		}
	}

	@Override
	public HmiResultObj createPast(PastIncomeMonthTbl pastIncomeMonthTbl) {
		HmiResultObj hmiResultObj=null;
		try {
			sqlDao.insertUpdate(pastIncomeMonthTbl);
			hmiResultObj=new HmiResultObj();
			hmiResultObj.setResultMessage(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS);
		} catch (SqlErrorException e) {
			e.printStackTrace();
			hmiResultObj=new HmiResultObj();
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
		}
		return hmiResultObj;
	}

	@Override
	public HmiResultObj deletePast(PastIncomeMonthTbl pastIncomeMonthTbl) {
		HmiResultObj hmiResultObj=null;
		try {
			sqlDao.delete(pastIncomeMonthTbl);
			hmiResultObj=new HmiResultObj();
			hmiResultObj.setResultMessage(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS);
		} catch (SqlErrorException e) {
			e.printStackTrace();
			hmiResultObj=new HmiResultObj();
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
		}
		return hmiResultObj;
	}
*/
	@Override
	public List<PtExpenses> findPtExpensesForMonth(int year, int month, int firmId) {
		
		String monthStr=""+month;
		if(month<10)
			monthStr="0"+month;
		
		
		String startDateStr="01/"+monthStr+"/"+year;
		Date startDate=OhbeUtil.getThatDateForNight(startDateStr,"dd/MM/yyyy"); 
		Date endDate=OhbeUtil.getDateForNextMonth(startDate, 1);
		
		String sql=" SELECT a.*,b.*"
				+ ",DATE_FORMAT(a.PE_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') PE_DATE_STR "
				+ "  FROM pt_expenses a,def_firm b "
				+ "  WHERE a.PE_DATE>=:startDate "
				+ "  AND a.PE_DATE<:endDate"
				+ "  AND b.FIRM_ID=a.FIRM_ID"
				+ "  ORDER BY a.PE_DATE ";
		
		 Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("startDate",startDate);
		 paramMap.put("endDate",endDate);
			
		  
	 	try {
	 		List<PtExpenses> ptExpenses=findEntityList(sql, paramMap, PtExpenses.class);
	 		return ptExpenses;
		}catch (Exception e) {
			 return null;
		}
	}

	@Override
	public PtExpenses findPtExpensesById(long peId) {
		String sql=" SELECT a.*,b.* "
				+ ",DATE_FORMAT(a.PE_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') PE_DATE_STR "
				+ "  FROM pt_expenses a,def_firm b "
				+ "  WHERE a.PE_ID=:peId"
				+ "  AND b.FIRM_ID=a.FIRM_ID"
				+ "  ORDER BY a.PE_DATE ";
		
		 Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("peId",peId);
		
		  
	 	try {
	 		PtExpenses ptExpenses=findEntityForObject(sql, paramMap, PtExpenses.class);
	 		return ptExpenses;
		}catch (Exception e) {
			 return null;
		}
	}

	@Override
	public synchronized HmiResultObj createPtExpenses(PtExpenses ptExpenses) {
		HmiResultObj hmiResultObj=null;
		try {
			sqlDao.insertUpdate(ptExpenses);
			
			if(ptExpenses.getPeId()==0)
				ptExpenses.setPeId(Long.parseLong(getLastPeIdSeq()));
			
			hmiResultObj=new HmiResultObj();
			hmiResultObj.setResultMessage(""+ptExpenses.getPeId());
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS);
		} catch (SqlErrorException e) {
			e.printStackTrace();
			hmiResultObj=new HmiResultObj();
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
		}
		return hmiResultObj;
	}
	
	private String getLastPeIdSeq(){
		String sql=	"SELECT CAST(max(PE_ID) AS CHAR) PE_ID" +
				" FROM pt_expenses ";
		String peId="1";
		try {
			peId=""+findEntityForObject(sql, null, Long.class);
			if(peId.equals("null"))
				peId="1";
		} catch (Exception e) {
			peId="1";
			e.printStackTrace();
		}
		return peId;

	}

	@Override
	public HmiResultObj deletePtExpenses(PtExpenses ptExpenses) {
		HmiResultObj hmiResultObj=null;
		try {
			sqlDao.delete(ptExpenses);
			hmiResultObj=new HmiResultObj();
			hmiResultObj.setResultMessage(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS);
		} catch (SqlErrorException e) {
			e.printStackTrace();
			hmiResultObj=new HmiResultObj();
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
		}
		return hmiResultObj;
	}

	@Override
	public List<PtExpenses> findPtExpensesForDate(Date startDate, Date endDate, int firmId) {
		
		String sql=" SELECT a.*,b.*"
				+ ",DATE_FORMAT(a.PE_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') PE_DATE_STR "
				+ "  FROM pt_expenses a,def_firm b "
				+ "  WHERE a.PE_DATE>=:startDate "
				+ "  AND a.PE_DATE<:endDate"
				+ "  AND b.FIRM_ID=a.FIRM_ID"
				+ "  AND b.FIRM_ID=:firmId"
				+ "  ORDER BY a.PE_DATE ";
		
		 Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("startDate",startDate);
		 paramMap.put("endDate",endDate);
		 paramMap.put("firmId",firmId);
				
		  
	 	try {
	 		List<PtExpenses> ptExpenses=findEntityList(sql, paramMap, PtExpenses.class);
	 		return ptExpenses;
		}catch (Exception e) {
			 return null;
		}
	}
	
	

}
