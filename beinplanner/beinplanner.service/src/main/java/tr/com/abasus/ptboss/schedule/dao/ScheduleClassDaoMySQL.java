package tr.com.abasus.ptboss.schedule.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tr.com.abasus.general.dao.AbaxJdbcDaoSupport;
import tr.com.abasus.general.dao.SqlDao;
import tr.com.abasus.general.exception.SqlErrorException;
import tr.com.abasus.ptboss.ptuser.entity.User;
import tr.com.abasus.ptboss.result.entity.HmiResultObj;
import tr.com.abasus.ptboss.schedule.businessEntity.StaffClassPlans;
import tr.com.abasus.ptboss.schedule.entity.ScheduleFactory;
import tr.com.abasus.ptboss.schedule.entity.SchedulePlan;
import tr.com.abasus.ptboss.schedule.entity.ScheduleUsersClassPlan;
import tr.com.abasus.util.GlobalUtil;
import tr.com.abasus.util.ResultStatuObj;

public class ScheduleClassDaoMySQL extends AbaxJdbcDaoSupport implements ScheduleClassDao {

	SqlDao sqlDao;

	public SqlDao getSqlDao() {
		return sqlDao;
	}

	public void setSqlDao(SqlDao sqlDao) {
		this.sqlDao = sqlDao;
	}

	
	
	@Override
	public HmiResultObj createScheduleUsersClassPlan(ScheduleUsersClassPlan scheduleUsersClassPlan) {
		HmiResultObj hmiResultObj=null;
		try {
			sqlDao.insertUpdate(scheduleUsersClassPlan);
			if(scheduleUsersClassPlan.getSucpId()==0)
				scheduleUsersClassPlan.setSucpId(Long.parseLong(getLastSucpIdSeq()));
			
			hmiResultObj=new HmiResultObj();
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS);
			hmiResultObj.setResultMessage(""+scheduleUsersClassPlan.getSucpId());
		} catch (SqlErrorException e) {
			e.printStackTrace();
			hmiResultObj=new HmiResultObj();
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
			hmiResultObj.setResultMessage(ResultStatuObj.RESULT_STATU_FAIL_STR);
		}
		return hmiResultObj;
	}
	
	private synchronized String getLastSucpIdSeq(){
		String sql=	"SELECT CAST(max(SUCP_ID) AS CHAR) SUCP_ID" +
				" FROM schedule_users_class_plan ";
		String sucpId="1";
		try {
			sucpId=""+findEntityForObject(sql, null, Long.class);
			if(sucpId.equals("null"))
				sucpId="1";
		} catch (Exception e) {
			sucpId="1";
			e.printStackTrace();
		}
		return sucpId;
	}
	
	

	@Override
	public List<ScheduleUsersClassPlan> usersClassPlansBySaleId(long saleId) {
		String sql="SELECT a.*"
				+ " ,DATE_FORMAT(c.PLAN_START_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') PLAN_START_DATE_STR"
				 + ",DATE_FORMAT(c.PLAN_END_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') PLAN_END_DATE_STR"
					+ " ,c.*,b.*,d.*,e.*,'sucp' TYPE,d.PROG_COUNT SALE_COUNT"
				+ " ,DATE_FORMAT(d.SALES_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') SALES_DATE_STR"
		  		+ " ,DATE_FORMAT(b.USER_BIRTHDAY,'"+GlobalUtil.global.getPtDateFormat()+"') USER_BIRTHDAY_STR "
					+ "  FROM schedule_users_class_plan a, user b,schedule_time_plan c,packet_sale_class d,schedule_plan e "
					+ " WHERE a.USER_ID=b.USER_ID "
					+ " AND a.SALE_ID=:saleId "
					+ " AND a.SCHT_ID=c.SCHT_ID "
					+ " AND a.SALE_ID=d.SALE_ID "
					+ " AND e.SCH_ID=c.SCH_ID ";
			 
			
			 Map<String, Object> paramMap=new HashMap<String, Object>();
			 paramMap.put("saleId",saleId);
			    
			 try{
				 List<ScheduleUsersClassPlan> scheduleUsersClassPlans=findEntityList(sql, paramMap, ScheduleUsersClassPlan.class);
				
				return scheduleUsersClassPlans;
			 }catch (Exception e) {
				 e.printStackTrace();
					return null;
			 }
	}
	
	@Override
	public List<ScheduleUsersClassPlan> findScheduleUsersClassPlanByUserId(long userId) {
		String sql="SELECT a.*"
				+ ",DATE_FORMAT(c.PLAN_START_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') PLAN_START_DATE_STR"
				 + ",DATE_FORMAT(c.PLAN_END_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') PLAN_END_DATE_STR"
				+ " ,c.*,b.*,d.*,e.*,'sucp' TYPE,d.PROG_COUNT SALE_COUNT"
				+ " ,DATE_FORMAT(d.SALES_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') SALES_DATE_STR"
		  		+ " ,DATE_FORMAT(b.USER_BIRTHDAY,'"+GlobalUtil.global.getPtDateFormat()+"') USER_BIRTHDAY_STR "
					+ "  FROM schedule_users_class_plan a, user b,schedule_time_plan c,packet_sale_class d,schedule_plan e "
					+ " WHERE a.USER_ID=b.USER_ID "
					+ " AND a.USER_ID=:userId "
					+ " AND a.SCHT_ID=c.SCHT_ID "
					+ " AND a.SALE_ID=d.SALE_ID "
					+ " AND e.SCH_ID=c.SCH_ID ";
			 
			
			 Map<String, Object> paramMap=new HashMap<String, Object>();
			 paramMap.put("userId",userId);
			    
			 try{
				 List<ScheduleUsersClassPlan> scheduleUsersClassPlans=findEntityList(sql, paramMap, ScheduleUsersClassPlan.class);
				
				return scheduleUsersClassPlans;
			 }catch (Exception e) {
				 e.printStackTrace();
					return null;
			 }
	}

	@Override
	public List<ScheduleUsersClassPlan> findScheduleUsersClassPlanByUserIdAndSaleId(long userId,long saleId) {
		String sql="SELECT a.*"
				+ ",DATE_FORMAT(c.PLAN_START_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') PLAN_START_DATE_STR"
				 + ",DATE_FORMAT(c.PLAN_END_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') PLAN_END_DATE_STR"
				+ " ,c.*,b.*,d.*,e.*,'sucp' TYPE,d.PROG_COUNT SALE_COUNT"
				+ " ,DATE_FORMAT(d.SALES_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') SALES_DATE_STR"
		  		+ " ,DATE_FORMAT(b.USER_BIRTHDAY,'"+GlobalUtil.global.getPtDateFormat()+"') USER_BIRTHDAY_STR "
					+ "  FROM schedule_users_class_plan a, user b,schedule_time_plan c,packet_sale_class d,schedule_plan e "
					+ " WHERE a.USER_ID=b.USER_ID "
					+ " AND a.USER_ID=:userId "
					+ " AND a.SALE_ID=:saleId "
					+ " AND a.SCHT_ID=c.SCHT_ID "
					+ " AND a.SALE_ID=d.SALE_ID "
					+ " AND e.SCH_ID=c.SCH_ID ";
			 
			
			 Map<String, Object> paramMap=new HashMap<String, Object>();
			 paramMap.put("userId",userId);
			 paramMap.put("saleId",saleId);
			    
			 try{
				 List<ScheduleUsersClassPlan> scheduleUsersClassPlans=findEntityList(sql, paramMap, ScheduleUsersClassPlan.class);
				
				return scheduleUsersClassPlans;
			 }catch (Exception e) {
				 e.printStackTrace();
					return null;
			 }
	}


	@Override
	public List<ScheduleUsersClassPlan> findScheduleUsersClassPlanByPlanId(long schId) {
		String sql="SELECT a.*,b.*,c.*,d.*,'sucp' TYPE,e.PROG_COUNT SALE_COUNT,e.* "
				+ " ,DATE_FORMAT(b.PLAN_START_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') PLAN_START_DATE_STR"
				 + ",DATE_FORMAT(b.PLAN_END_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') PLAN_END_DATE_STR"
					+ "  FROM schedule_users_class_plan a, schedule_time_plan b,schedule_plan c,user d,packet_sale_class e "
					+ " WHERE a.SCHT_ID=b.SCHT_ID "
					+ " AND c.SCH_ID=:schId "
					+ " AND b.SCH_ID=c.SCH_ID "
					+ " AND d.USER_ID=a.USER_ID"
					+ " AND e.SALE_ID=a.SALE_ID ";
			 
			
			 Map<String, Object> paramMap=new HashMap<String, Object>();
			 paramMap.put("schId",schId);
			    
			 try{
				 List<ScheduleUsersClassPlan> scheduleUsersClassPlans=findEntityList(sql, paramMap, ScheduleUsersClassPlan.class);
				
				return scheduleUsersClassPlans;
			 }catch (Exception e) {
				 e.printStackTrace();
					return null;
			 }
	}

	@Override
	public List<ScheduleUsersClassPlan> findScheduleUsersClassPlanByTimePlanId(long schtId) {
		String sql="SELECT a.*,b.*,d.*,'sucp' TYPE,e.PROG_COUNT SALE_COUNT,e.*"
				+ " ,DATE_FORMAT(b.PLAN_START_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') PLAN_START_DATE_STR"
				 + ",DATE_FORMAT(b.PLAN_END_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') PLAN_END_DATE_STR"
				+ "  FROM schedule_users_class_plan a, schedule_time_plan b,user d,packet_sale_class e "
				+ " WHERE a.SCHT_ID=b.SCHT_ID "
				+ " AND b.SCHT_ID=:schtId "
				+ " AND d.USER_ID=a.USER_ID"
				+ " AND e.SALE_ID=a.SALE_ID";
		 
		
		 Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("schtId",schtId);
		    
		 try{
			 List<ScheduleUsersClassPlan> scheduleUsersClassPlans=findEntityList(sql, paramMap, ScheduleUsersClassPlan.class);
			
			return scheduleUsersClassPlans;
		 }catch (Exception e) {
			 e.printStackTrace();
				return null;
		 }
	}

	@Override
	public List<SchedulePlan> findScheduleClassPlansbyUserId(long userId,long saleId) {
		String sql="SELECT a.SCH_ID "
				+ "  FROM schedule_plan a,schedule_time_plan b, schedule_users_class_plan c "
					+ " WHERE c.USER_ID=:userId "
					+ " AND b.SCHT_ID=c.SCHT_ID "
					+ " AND a.SCH_ID=b.SCH_ID "
					+ " AND c.SALE_ID=:saleId "
					+ " GROUP BY SCH_ID ";
			
		 	 Map<String, Object> paramMap=new HashMap<String, Object>();
			 paramMap.put("userId",userId);
			 paramMap.put("saleId",saleId);
				try{
				 List<SchedulePlan> schedulePlans=findEntityList(sql, paramMap, SchedulePlan.class);
				 return schedulePlans;
			 }catch (Exception e) {
				 e.printStackTrace();
				return null;
			 }
	}

	@Override
	public SchedulePlan findSchedulePlanBySaleId(long saleId) {
		String sql="SELECT a.SCH_ID,a.PROG_ID,a.PROG_TYPE,a.SCH_COUNT,a.SCH_STAFF_ID,a.FIRM_ID,d.USER_NAME,d.USER_SURNAME,d.PROFILE_URL,d.URL_TYPE "
				+ "  FROM schedule_plan a,schedule_time_plan b, schedule_users_class_plan c,user d "
					+ " WHERE b.SCHT_ID=c.SCHT_ID "
					+ " AND a.SCH_ID=b.SCH_ID "
					+ " AND d.USER_ID=a.SCH_STAFF_ID "
					+ " AND c.SALE_ID=:saleId "
					+ " GROUP BY a.SCH_ID,a.PROG_ID,a.PROG_TYPE,a.SCH_COUNT,a.SCH_STAFF_ID,a.FIRM_ID,d.USER_NAME,d.USER_SURNAME,d.PROFILE_URL,d.URL_TYPE ";
			
		 	 Map<String, Object> paramMap=new HashMap<String, Object>();
			 paramMap.put("saleId",saleId);
				try{
				SchedulePlan schedulePlan=findEntityForObject(sql, paramMap, SchedulePlan.class);
				 return schedulePlan;
			 }catch (Exception e) {
				return null;
			 }
	}

	@Override
	public List<ScheduleUsersClassPlan> findScheduleUsersClassPlanByPlanIdAndUserId(long schId, long userId) {
		String sql="SELECT a.*,b.*,c.PROG_COUNT SALE_COUNT,c.* "
				+ "  FROM schedule_time_plan a, schedule_users_class_plan b,packet_sale_class c "
					+ " WHERE b.USER_ID=:userId "
					+ " AND a.SCH_ID=:schId"
					+ " AND b.SCHT_ID=a.SCHT_ID "
					+ " AND c.SALE_ID=b.SALE_ID";
			
		 	 Map<String, Object> paramMap=new HashMap<String, Object>();
			 paramMap.put("userId",userId);
			 paramMap.put("schId",schId);
				try{
				 List<ScheduleUsersClassPlan> schedulePlans=findEntityList(sql, paramMap, ScheduleUsersClassPlan.class);
				 return schedulePlans;
			 }catch (Exception e) {
				 e.printStackTrace();
				return null;
			 }
	}

	@Override
	public List<User> findUsersInClassPlanToGroup(long schId) {
		String sql="SELECT b.USER_ID,b.SALE_ID,c.USER_NAME,c.USER_SURNAME,'sucp' TYPE,d.PROG_COUNT SALE_COUNT "
				+ "  FROM schedule_time_plan a, schedule_users_class_plan b,user c,packet_sale_class d  "
					+ " WHERE a.SCH_ID=:schId "
					+ " AND b.SCHT_ID=a.SCHT_ID "
					+ " AND c.USER_ID=b.USER_ID"
					+ " AND d.SALE_ID=b.SALE_ID "
					+ " GROUP BY b.USER_ID,b.SALE_ID,c.USER_NAME,c.USER_SURNAME,d.PROG_COUNT ";
			
		 	 Map<String, Object> paramMap=new HashMap<String, Object>();
			 paramMap.put("schId",schId);
				try{
				 List<User> users=findEntityList(sql, paramMap, User.class);
				 return users;
			 }catch (Exception e) {
				 e.printStackTrace();
				return null;
			 }
	}

	@Override
	public HmiResultObj deleteScheduleUsersClassPlan(ScheduleUsersClassPlan scheduleUsersClassPlan) {
		HmiResultObj hmiResultObj=null;
		try {
			sqlDao.delete(scheduleUsersClassPlan);
			
			hmiResultObj=new HmiResultObj();
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS);
			hmiResultObj.setResultMessage(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
		} catch (SqlErrorException e) {
			e.printStackTrace();
			hmiResultObj=new HmiResultObj();
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
			hmiResultObj.setResultMessage(ResultStatuObj.RESULT_STATU_FAIL_STR);
		}
		return hmiResultObj;
	}

	@Override
	public List<StaffClassPlans> findStaffClassPlans(Date startDate, Date endDate, long userId) {
		String sql="SELECT b.SALE_ID "
				+ "			,a.SCH_ID"
				+ "			,f.PROG_NAME"
				+ "			,a.SCHT_ID"
				+ " 		,DATE_FORMAT(a.PLAN_START_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') PLAN_START_DATE_STR"
				+ "			,DATE_FORMAT(a.PLAN_START_DATE,'%H:%i') PLAN_START_TIME_STR"
				+ "			,c.SCH_COUNT"
				+ "			,d.PROG_COUNT"
				+ "			,e.USER_NAME"
				+ "			,e.USER_SURNAME"
				+ "			,a.PLAN_START_DATE "
				+ "         ,a.STATUTP "
				+ "  FROM 	schedule_time_plan a, "
				+ "			schedule_users_class_plan b,"
				+ "			schedule_plan c,"
				+ "			packet_sale_class d,"
				+ "			user e,"
				+ "			program_class f "
					+ " WHERE a.SCH_ID=c.SCH_ID"
					+ "   AND a.SCHT_ID=b.SCHT_ID"
					+ "   AND b.SALE_ID=d.SALE_ID"
					+ "	  AND e.USER_ID=b.USER_ID"
					+ "   AND f.PROG_ID=c.PROG_ID"
					+ "   AND f.PROG_ID=d.PROG_ID"
					+ "   AND a.PLAN_START_DATE>=:startDate"
					+ "   AND a.PLAN_START_DATE<:endDate"
					+ "   AND a.SCHT_STAFF_ID=:userId "
					+ " ORDER BY a.PLAN_START_DATE,a.SCHT_ID ";
			
		 	 Map<String, Object> paramMap=new HashMap<String, Object>();
			 paramMap.put("startDate",startDate);
			 paramMap.put("endDate",endDate);
			 paramMap.put("userId",userId);
					try{
				 List<StaffClassPlans> staffClassPlans=findEntityList(sql, paramMap, StaffClassPlans.class);
				 return staffClassPlans;
			 }catch (Exception e) {
				 e.printStackTrace();
				return null;
			 }
		
	}

	

	

	
	
}
