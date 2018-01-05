package tr.com.abasus.ptboss.schedule.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tr.com.abasus.general.dao.AbaxJdbcDaoSupport;
import tr.com.abasus.general.dao.SqlDao;
import tr.com.abasus.general.exception.SqlErrorException;
import tr.com.abasus.ptboss.ptuser.entity.User;
import tr.com.abasus.ptboss.result.entity.HmiResultObj;
import tr.com.abasus.ptboss.schedule.entity.ScheduleFactory;
import tr.com.abasus.ptboss.schedule.entity.ScheduleMembershipPlan;
import tr.com.abasus.ptboss.schedule.entity.ScheduleMembershipTimePlan;
import tr.com.abasus.util.DateTimeUtil;
import tr.com.abasus.util.GlobalUtil;
import tr.com.abasus.util.OhbeUtil;
import tr.com.abasus.util.ResultStatuObj;
import tr.com.abasus.util.StatuTypes;

public class ScheduleMembershipDaoMySQL extends AbaxJdbcDaoSupport implements ScheduleMembershipDao {

	SqlDao sqlDao;

	public SqlDao getSqlDao() {
		return sqlDao;
	}

	public void setSqlDao(SqlDao sqlDao) {
		this.sqlDao = sqlDao;
	}

	
	
	@Override
	public synchronized HmiResultObj createSchedulePlan(ScheduleFactory scheduleFactory) {
		HmiResultObj hmiResultObj=null;
		try {
			sqlDao.insertUpdate(scheduleFactory);
			if(scheduleFactory.getSmpId()==0)
				scheduleFactory.setSmpId(Long.parseLong(getLastSMPIdSeq()));
			
			hmiResultObj=new HmiResultObj();
			//hmiResultObj.setResultObj(member);
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS);
			hmiResultObj.setResultMessage(""+scheduleFactory.getSmpId());
		} catch (SqlErrorException e) {
			e.printStackTrace();
			hmiResultObj=new HmiResultObj();
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
			hmiResultObj.setResultMessage(ResultStatuObj.RESULT_STATU_FAIL_STR);
		}
		return hmiResultObj;
		
	}
	
	
	
	
	
	private synchronized String getLastSMPIdSeq(){
		String sql=	"SELECT CAST(max(SMP_ID) AS CHAR) SMP_ID" +
				" FROM schedule_membership_plan ";
		String userId="1";
		try {
			userId=""+findEntityForObject(sql, null, Long.class);
			if(userId.equals("null"))
				userId="1";
		} catch (Exception e) {
			userId="1";
			e.printStackTrace();
		}
		return userId;

	
	}
	
	
	
	@Override
	public synchronized HmiResultObj createScheduleMembershipTimePlan(ScheduleMembershipTimePlan scheduleMembershipTimePlan) {
		HmiResultObj hmiResultObj=null;
		try {
			sqlDao.insertUpdate(scheduleMembershipTimePlan);
			if(scheduleMembershipTimePlan.getSmtpId()==0)
				scheduleMembershipTimePlan.setSmtpId(Long.parseLong(getLastSMTPIdSeq()));
			
			hmiResultObj=new HmiResultObj();
			//hmiResultObj.setResultObj(member);
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS);
			hmiResultObj.setResultMessage(""+scheduleMembershipTimePlan.getSmtpId());
		} catch (SqlErrorException e) {
			e.printStackTrace();
			hmiResultObj=new HmiResultObj();
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
			hmiResultObj.setResultMessage(ResultStatuObj.RESULT_STATU_FAIL_STR);
		}
		return hmiResultObj;
		
	}
	
	private synchronized String getLastSMTPIdSeq(){
		String sql=	"SELECT CAST(max(SMTP_ID) AS CHAR) SMTP_ID" +
				" FROM schedule_membership_time_plan ";
		String userId="1";
		try {
			userId=""+findEntityForObject(sql, null, Long.class);
			if(userId.equals("null"))
				userId="1";
		} catch (Exception e) {
			userId="1";
			e.printStackTrace();
		}
		return userId;

	
	}
	

	@Override
	public ScheduleFactory findSchedulePlanById(long id) {
		String sql="SELECT a.*,b.*"
				+ " ,DATE_FORMAT(b.USER_BIRTHDAY,'"+GlobalUtil.global.getPtDateFormat()+"') USER_BIRTHDAY_STR "
		  		+ ",DATE_FORMAT(a.SMP_START_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') SMP_START_DATE_STR"
	  			+ ",DATE_FORMAT(a.SMP_END_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') SMP_END_DATE_STR "
	  			+ ",DATE_FORMAT(a.SMP_START_DATE,'%H:%s:%i') SMP_START_DAY_TIME"
	  			+ ",DATE_FORMAT(a.SMP_END_DATE,'%H:%s:%i') SMP_END_DAY_TIME"
				+ "  FROM schedule_membership_plan a,user b "
					+ " WHERE a.USER_ID=b.USER_ID "
					+ "   AND a.SMP_ID=:id ";
			 
		
		////System.out.println(sql);
		
		    Map<String, Object> paramMap=new HashMap<String, Object>();
			 paramMap.put("id",id);
			    
			 try{
				 ScheduleMembershipPlan scheduleMembershipPlan=findEntityForObject(sql, paramMap, ScheduleMembershipPlan.class);
				 scheduleMembershipPlan.setScheduleMembershipTimePlans(findScheduleTimePlanBySmpId(id));
				 scheduleMembershipPlan.setSmpStartDayName(DateTimeUtil.getDayNames(scheduleMembershipPlan.getSmpStartDate()));
				 scheduleMembershipPlan.setSmpEndDayName(DateTimeUtil.getDayNames(scheduleMembershipPlan.getSmpEndDate()));
				 
				  
				 return scheduleMembershipPlan;
			 }catch (Exception e) {
				  return null;
			 }
		
	}
	
	@Override
	public ScheduleFactory findSchedulePlanBySaleId(long saleId) {
		String sql="SELECT a.*,b.*"
				+ " ,DATE_FORMAT(b.USER_BIRTHDAY,'"+GlobalUtil.global.getPtDateFormat()+"') USER_BIRTHDAY_STR "
		  		+ ",DATE_FORMAT(a.SMP_START_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') SMP_START_DATE_STR"
	  			+ ",DATE_FORMAT(a.SMP_END_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') SMP_END_DATE_STR "
	  			+ ",DATE_FORMAT(a.SMP_START_DATE,'%H:%s:%i') SMP_START_DAY_TIME"
	  			+ ",DATE_FORMAT(a.SMP_END_DATE,'%H:%s:%i') SMP_END_DAY_TIME"
				+ "  FROM schedule_membership_plan a,user b "
					+ " WHERE a.USER_ID=b.USER_ID "
					+ "   AND a.SALE_ID=:saleId "
					+ " LIMIT 1";
			 
		
		////System.out.println(sql);
		
		    Map<String, Object> paramMap=new HashMap<String, Object>();
			 paramMap.put("saleId",saleId);
			    
			 try{
				 ScheduleMembershipPlan scheduleMembershipPlan=findEntityForObject(sql, paramMap, ScheduleMembershipPlan.class);
				 scheduleMembershipPlan.setScheduleMembershipTimePlans(findScheduleTimePlanBySmpId(scheduleMembershipPlan.getSmpId()));
				 scheduleMembershipPlan.setSmpStartDayName(DateTimeUtil.getDayNames(scheduleMembershipPlan.getSmpStartDate()));
				 scheduleMembershipPlan.setSmpEndDayName(DateTimeUtil.getDayNames(scheduleMembershipPlan.getSmpEndDate()));
				 
				  
				 return scheduleMembershipPlan;
			 }catch (Exception e) {
				  return null;
			 }
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ScheduleFactory> findSchedulesbyUserId(long userId) {
		
		String sql="SELECT a.*"
				+ "  FROM schedule_membership_plan a "
					+ " WHERE a.USER_ID=:userId  ";
			 
		    Map<String, Object> paramMap=new HashMap<String, Object>();
			 paramMap.put("userId",userId);
			    
			 try{
				 List<? extends ScheduleFactory> scheduleUsersClassPlans=findEntityList(sql, paramMap, ScheduleMembershipPlan.class);
				 return (List<ScheduleFactory>)scheduleUsersClassPlans;
				 
			 }catch (Exception e) {
				  return null;
			 }
		
		
	}

	@Override
	public List<ScheduleMembershipTimePlan> findScheduleTimePlanBySmpId(long smpId) {
		String sql="SELECT a.*"
				+ ",DATE_FORMAT(a.SMP_START_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') SMP_START_DATE_STR"
	  			+ ",DATE_FORMAT(a.SMP_END_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') SMP_END_DATE_STR "
	  			+ ",DATE_FORMAT(a.SMP_START_DATE,'%H:%s:%i') SMP_START_DAY_TIME"
	  			+ ",DATE_FORMAT(a.SMP_END_DATE,'%H:%s:%i') SMP_END_DAY_TIME"
				+ "  FROM schedule_membership_time_plan a "
					+ " WHERE a.SMP_ID=:smpId "
					+ " ORDER BY SMP_END_DATE DESC ";
			 
		    Map<String, Object> paramMap=new HashMap<String, Object>();
			 paramMap.put("smpId",smpId);
			    
			 try{
				 List<ScheduleMembershipTimePlan> scheduleMembershipTimePlans=findEntityList(sql, paramMap, ScheduleMembershipTimePlan.class);
				 if(scheduleMembershipTimePlans.size()>1)
				     scheduleMembershipTimePlans.get(0).setFirst(true);
				 
				 return scheduleMembershipTimePlans;
				 
			 }catch (Exception e) {
				  return null;
			 }
	}

	@Override
	public ScheduleMembershipTimePlan findScheduleTimePlanBySmtpId(long smtpId) {
		String sql="SELECT a.*"
				+ ",DATE_FORMAT(a.SMP_START_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') SMP_START_DATE_STR"
	  			+ ",DATE_FORMAT(a.SMP_END_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') SMP_END_DATE_STR "
	  			+ ",DATE_FORMAT(a.SMP_START_DATE,'%H:%s:%i') SMP_START_DAY_TIME"
	  			+ ",DATE_FORMAT(a.SMP_END_DATE,'%H:%s:%i') SMP_END_DAY_TIME"
				+ "  FROM schedule_membership_time_plan a "
					+ " WHERE a.SMTP_ID=:smtpId "
					+ " ORDER BY SMP_END_DATE DESC";
			 
		    Map<String, Object> paramMap=new HashMap<String, Object>();
			 paramMap.put("smtpId",smtpId);
			    
			 try{
				 ScheduleMembershipTimePlan scheduleMembershipTimePlan=findEntityForObject(sql, paramMap, ScheduleMembershipTimePlan.class);
				 return scheduleMembershipTimePlan;
				 
			 }catch (Exception e) {
				  return null;
			 }
	}

	

	@Override
	public HmiResultObj deletePlan(ScheduleFactory scheduleFactory) {
		HmiResultObj hmiResultObj=null;
		try {
			sqlDao.delete(scheduleFactory);
			
			hmiResultObj=new HmiResultObj();
			//hmiResultObj.setResultObj(member);
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
	public HmiResultObj deleteScheduleMembershipTimePlan(ScheduleMembershipTimePlan scheduleMembershipTimePlan) {
		HmiResultObj hmiResultObj=null;
		try {
			sqlDao.delete(scheduleMembershipTimePlan);
			
			hmiResultObj=new HmiResultObj();
			//hmiResultObj.setResultObj(member);
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

	
	
	

}
