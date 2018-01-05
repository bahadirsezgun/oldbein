package tr.com.abasus.ptboss.bonus.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tr.com.abasus.general.dao.AbaxJdbcDaoSupport;
import tr.com.abasus.general.dao.SqlDao;
import tr.com.abasus.general.exception.SqlErrorException;
import tr.com.abasus.ptboss.bonus.entity.UserBonusPaymentClass;
import tr.com.abasus.ptboss.bonus.entity.UserBonusPaymentFactory;
import tr.com.abasus.ptboss.bonus.entity.UserBonusPaymentPersonal;
import tr.com.abasus.ptboss.ptuser.entity.User;
import tr.com.abasus.ptboss.result.entity.HmiResultObj;
import tr.com.abasus.ptboss.schedule.entity.ScheduleTimePlan;
import tr.com.abasus.util.DateTimeUtil;
import tr.com.abasus.util.GlobalUtil;
import tr.com.abasus.util.OhbeUtil;
import tr.com.abasus.util.ResultStatuObj;

public class BonusClassDaoMySQL extends AbaxJdbcDaoSupport implements BonusClassDao {

	SqlDao sqlDao;
	public SqlDao getSqlDao() {
		return sqlDao;
	}

	public void setSqlDao(SqlDao sqlDao) {
		this.sqlDao = sqlDao;
	}

	@Override
	public List<ScheduleTimePlan> findScheduleTimePlansByDatesForStaff(long schStaffId, Date startDate, Date endDate) {
	
		String sql="SELECT a.SCHT_ID,d.PROG_ID,d.PROG_NAME,c.SCH_COUNT,a.STATUTP"
				+ " ,DATE_FORMAT(a.PLAN_START_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') PLAN_START_DATE_STR"
				+ "  FROM 	schedule_time_plan a, "
				+ "			schedule_users_class_plan b,"
				+ "         schedule_plan c,"
				+ "         program_class d "
					+ " WHERE a.SCHT_STAFF_ID=:schStaffId"
					+ "   AND a.SCHT_ID=b.SCHT_ID"
					+ "   AND a.PLAN_START_DATE>=:startDate"
					+ "   AND a.PLAN_START_DATE<:endDate"
					+ "   AND c.SCH_ID=a.SCH_ID "
					+ "   AND c.PROG_ID=d.PROG_ID "
					+ " GROUP BY a.SCHT_ID,d.PROG_ID,d.PROG_NAME,DATE_FORMAT(a.PLAN_START_DATE,'"+GlobalUtil.global.getPtDateFormat()+"'),c.SCH_COUNT,a.STATUTP "
					+ " ORDER BY a.PLAN_START_DATE ";
		
		 	 Map<String, Object> paramMap=new HashMap<String, Object>();
			 paramMap.put("startDate",startDate);
			 paramMap.put("endDate",endDate);
			 paramMap.put("schStaffId",schStaffId);
			 try{
				List<ScheduleTimePlan> scheduleTimePlans=findEntityList(sql, paramMap, ScheduleTimePlan.class);
				return scheduleTimePlans;
			 }catch (Exception e) {
				 e.printStackTrace();
				return null;
			 }
	}

	@Override
	public List<UserBonusPaymentClass> findUserOfMonth(long userId, int month,int year) {
		String sql="SELECT a.*,b.*,c.* "
				+ " ,DATE_FORMAT(a.BON_PAYMENT_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') BON_PAYMENT_DATE_STR"
				+ " ,DATE_FORMAT(a.BON_START_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') BON_START_DATE_STR"
				+ " ,DATE_FORMAT(a.BON_END_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') BON_END_DATE_STR"
				+ "  FROM user_bonus_payment_class a,user b,def_firm c "
				+ "  WHERE a.BON_MONTH=:month "
				+ "    AND a.BON_YEAR=:year"
				+ "    AND b.FIRM_ID=c.FIRM_ID"
				+ "    AND b.USER_ID=a.USER_ID"
				+ "    AND a.USER_ID=:userId "
				+ "  ORDER BY a.BON_START_DATE ";
		
		 Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("month",month);
		 paramMap.put("year",year);
		 paramMap.put("userId",userId);
		 
		  
	 	try {
	 		List<UserBonusPaymentClass> userBonusPayments=findEntityList(sql, paramMap, UserBonusPaymentClass.class);
			 for (UserBonusPaymentClass userBonusPayment : userBonusPayments) {
				 userBonusPayment.setBonMonthName(DateTimeUtil.getMonthNamesBySequence(userBonusPayment.getBonMonth()));
			 }
			 return userBonusPayments;
		}catch (Exception e) {
			e.printStackTrace();
			 return null;
		}
	}

	@Override
	public List<UserBonusPaymentClass> findUserOfYear(long userId, int year) {
		String sql="SELECT a.*,b.*,c.* "
				+ " ,DATE_FORMAT(a.BON_PAYMENT_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') BON_PAYMENT_DATE_STR"
				+ " ,DATE_FORMAT(a.BON_START_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') BON_START_DATE_STR"
				+ " ,DATE_FORMAT(a.BON_END_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') BON_END_DATE_STR"
				+ "  FROM user_bonus_payment_class a,user b,def_firm c "
				+ "  WHERE a.BON_YEAR=:year "
				+ "    AND b.FIRM_ID=c.FIRM_ID"
				+ "    AND b.USER_ID=a.USER_ID"
				+ "    AND a.USER_ID=:userId "
				+ "  ORDER BY a.BON_START_DATE ";
		
		 Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("year",year);
		 
		  
	 	try {
	 		List<UserBonusPaymentClass> userBonusPayments=findEntityList(sql, paramMap, UserBonusPaymentClass.class);
			 for (UserBonusPaymentClass userBonusPayment : userBonusPayments) {
				 userBonusPayment.setBonMonthName(DateTimeUtil.getMonthNamesBySequence(userBonusPayment.getBonMonth()));
			 }
			 return userBonusPayments;
		}catch (Exception e) {
			 return null;
		}
	}

	@Override
	public synchronized HmiResultObj createBonusPaymentForUser(UserBonusPaymentClass userBonusPaymentClass) {
		HmiResultObj hmiResultObj=null;
		try {
			sqlDao.insertUpdate(userBonusPaymentClass);
			hmiResultObj=new HmiResultObj();
			
			if(userBonusPaymentClass.getBonId()==0){
				hmiResultObj.setResultMessage(getLastBonIdSeq());
			}else{
				hmiResultObj.setResultMessage(""+userBonusPaymentClass.getBonId());
			}
			
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS);
		} catch (SqlErrorException e) {
			e.printStackTrace();
			hmiResultObj=new HmiResultObj();
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
		}
		return hmiResultObj;
	}
	
	private synchronized String getLastBonIdSeq(){
		String sql=	"SELECT CAST(max(BON_ID) AS CHAR) BON_ID" +
				" FROM user_bonus_payment_class ";
		String bonId="1";
		try {
			bonId=""+findEntityForObject(sql, null, Long.class);
			if(bonId.equals("null"))
				bonId="1";
		} catch (Exception e) {
			bonId="1";
			e.printStackTrace();
		}
		return bonId;

	
	}

	@Override
	public HmiResultObj deleteBonusPaymentForUser(UserBonusPaymentClass userBonusPaymentClass) {
		HmiResultObj hmiResultObj=null;
		try {
			sqlDao.delete(userBonusPaymentClass);
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
	public List<UserBonusPaymentClass> findUserBonusPaymentByDate(long userId, Date startDate, Date endDate) {
		String sql="SELECT a.* "
				+ " ,DATE_FORMAT(a.BON_PAYMENT_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') BON_PAYMENT_DATE_STR"
				+ " ,DATE_FORMAT(a.BON_START_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') BON_START_DATE_STR"
				+ " ,DATE_FORMAT(a.BON_END_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') BON_END_DATE_STR"
				+ "  FROM user_bonus_payment_class a"
				+ "  WHERE a.BON_START_DATE>=:startDate "
				+ "    AND a.BON_START_DATE<:endDate"
				+ "    AND a.USER_ID=:userId "
				+ "  ORDER BY a.BON_START_DATE ";
		
		 Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("startDate",startDate);
		 paramMap.put("endDate",endDate);
		 paramMap.put("userId",userId);
		 
		  
	 	try {
	 		 List<UserBonusPaymentClass> userBonusPayments=findEntityList(sql, paramMap, UserBonusPaymentClass.class);
			 for (UserBonusPaymentClass userBonusPayment : userBonusPayments) {
				 userBonusPayment.setBonMonthName(DateTimeUtil.getMonthNamesBySequence(userBonusPayment.getBonMonth()));
			 }
			 return userBonusPayments;
		}catch (Exception e) {
			e.printStackTrace();
			 return null;
		}
	}

	@Override
	public User findStaffBonusType(long staffId) {
		String sql="SELECT a.* "
				+ "  FROM 	user a "
					+ " WHERE a.USER_ID=:staffId  ";
				
		 	 Map<String, Object> paramMap=new HashMap<String, Object>();
			 paramMap.put("staffId",staffId);
			
			 try{
				 User user=findEntityForObject(sql, paramMap, User.class);
				return user;
			 }catch (Exception e) {
				 e.printStackTrace();
				return null;
			 }
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<UserBonusPaymentFactory> findUserBonusPaymentFactoryByMonth(int month, int year, int firmId) {
		String sql="SELECT a.*,b.*,c.* "
				+ " ,DATE_FORMAT(a.BON_PAYMENT_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') BON_PAYMENT_DATE_STR"
				+ " ,DATE_FORMAT(a.BON_START_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') BON_START_DATE_STR"
				+ " ,DATE_FORMAT(a.BON_END_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') BON_END_DATE_STR"
				+ "  FROM user_bonus_payment_class a,user b,def_firm c "
				+ "  WHERE a.BON_MONTH=:month "
				+ "    AND a.BON_YEAR=:year"
				+ "    AND b.FIRM_ID=:firmId"
				+ "    AND b.FIRM_ID=c.FIRM_ID"
				+ "    AND b.USER_ID=a.USER_ID"
				+ "  ORDER BY a.USER_ID ";
		
		 Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("month",month);
		 paramMap.put("year",year);
		 paramMap.put("firmId",firmId);
		 
		  
	 	try {
	 		List<UserBonusPaymentClass> userBonusPayments=findEntityList(sql, paramMap, UserBonusPaymentClass.class);
			 for (UserBonusPaymentClass userBonusPayment : userBonusPayments) {
				 userBonusPayment.setBonMonthName(DateTimeUtil.getMonthNamesBySequence(userBonusPayment.getBonMonth()));
			 }
			 
			 List<? extends UserBonusPaymentFactory> userBonusPaymentFactories=userBonusPayments;
			 
			 
			 return (List<UserBonusPaymentFactory>)userBonusPaymentFactories;
		}catch (Exception e) {
			e.printStackTrace();
			 return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<UserBonusPaymentFactory> findBonusPaymentForToday(int firmId) {
		Date startDate=OhbeUtil.getTodayDate();
		Date endDate=OhbeUtil.getDateForNextDate(startDate, 1);
		
		
		String sql="SELECT a.*,b.*,c.* "
				+ " ,DATE_FORMAT(a.BON_PAYMENT_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') BON_PAYMENT_DATE_STR"
				+ " ,DATE_FORMAT(a.BON_START_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') BON_START_DATE_STR"
				+ " ,DATE_FORMAT(a.BON_END_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') BON_END_DATE_STR"
				+ "  FROM user_bonus_payment_class a,user b,def_firm c "
				+ "  WHERE a.BON_PAYMENT_DATE>=:startDate "
				+ "    AND a.BON_PAYMENT_DATE<=:endDate"
				+ "    AND b.FIRM_ID=:firmId"
				+ "    AND b.FIRM_ID=c.FIRM_ID"
				+ "    AND b.USER_ID=a.USER_ID"
				+ "  ORDER BY a.USER_ID ";
		
		 Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("startDate",startDate);
		 paramMap.put("endDate",endDate);
		 paramMap.put("firmId",firmId);
		 
		 
		 try {
		 		List<UserBonusPaymentClass> userBonusPayments=findEntityList(sql, paramMap, UserBonusPaymentClass.class);
				 for (UserBonusPaymentClass userBonusPayment : userBonusPayments) {
					 userBonusPayment.setBonMonthName(DateTimeUtil.getMonthNamesBySequence(userBonusPayment.getBonMonth()));
				 }
				 
				 List<? extends UserBonusPaymentFactory> userBonusPaymentFactories=userBonusPayments;
				 
				 
				 return (List<UserBonusPaymentFactory>)userBonusPaymentFactories;
			}catch (Exception e) {
				e.printStackTrace();
				 return null;
			}
	}
	
	
	
	
}
