package tr.com.abasus.ptboss.schedule.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tr.com.abasus.general.dao.AbaxJdbcDaoSupport;
import tr.com.abasus.general.dao.SqlDao;
import tr.com.abasus.general.exception.SqlErrorException;
import tr.com.abasus.ptboss.result.entity.HmiResultObj;
import tr.com.abasus.ptboss.schedule.businessEntity.ScheduleSearchObj;
import tr.com.abasus.ptboss.schedule.entity.SchedulePlan;
import tr.com.abasus.ptboss.schedule.entity.ScheduleStudios;
import tr.com.abasus.ptboss.schedule.entity.ScheduleTimePlan;
import tr.com.abasus.ptboss.schedule.entity.ScheduleUsersClassPlan;
import tr.com.abasus.util.GlobalUtil;
import tr.com.abasus.util.OhbeUtil;
import tr.com.abasus.util.ResultStatuObj;

public class ScheduleDaoMySQL extends AbaxJdbcDaoSupport implements ScheduleDao {

	SqlDao sqlDao;

	public SqlDao getSqlDao() {
		return sqlDao;
	}

	public void setSqlDao(SqlDao sqlDao) {
		this.sqlDao = sqlDao;
	}
	
	
	@Override
	public synchronized HmiResultObj createSchedulePlan(SchedulePlan schedulePlan) {
		HmiResultObj hmiResultObj=null;
		try {
			sqlDao.insertUpdate(schedulePlan);
			if(schedulePlan.getSchId()==0)
				schedulePlan.setSchId(Long.parseLong(getLastSchIdSeq()));
			
			hmiResultObj=new HmiResultObj();
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS);
			hmiResultObj.setResultMessage(""+schedulePlan.getSchId());
		} catch (SqlErrorException e) {
			e.printStackTrace();
			hmiResultObj=new HmiResultObj();
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
			hmiResultObj.setResultMessage(ResultStatuObj.RESULT_STATU_FAIL_STR);
		}
		return hmiResultObj;
	}

	private synchronized String getLastSchIdSeq(){
		String sql=	"SELECT CAST(max(SCH_ID) AS CHAR) SCH_ID" +
				" FROM schedule_plan ";
		String schId="1";
		try {
			schId=""+findEntityForObject(sql, null, Long.class);
			if(schId.equals("null"))
				schId="1";
		} catch (Exception e) {
			schId="1";
			e.printStackTrace();
		}
		return schId;
	}
	
	
	@Override
	public synchronized HmiResultObj createScheduleTimePlan(ScheduleTimePlan scheduleTimePlan) {
		HmiResultObj hmiResultObj=null;
		try {
			sqlDao.insertUpdate(scheduleTimePlan);
			if(scheduleTimePlan.getSchtId()==0)
				scheduleTimePlan.setSchtId(Long.parseLong(getLastSchtIdSeq()));
			
			hmiResultObj=new HmiResultObj();
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS);
			hmiResultObj.setResultMessage(""+scheduleTimePlan.getSchtId());
		} catch (SqlErrorException e) {
			e.printStackTrace();
			hmiResultObj=new HmiResultObj();
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
			hmiResultObj.setResultMessage(ResultStatuObj.RESULT_STATU_FAIL_STR);
		}
		return hmiResultObj;
	}

	private synchronized String getLastSchtIdSeq(){
		String sql=	"SELECT CAST(max(SCHT_ID) AS CHAR) SCHT_ID" +
				" FROM schedule_time_plan ";
		String schtId="1";
		try {
			schtId=""+findEntityForObject(sql, null, Long.class);
			if(schtId.equals("null"))
				schtId="1";
		} catch (Exception e) {
			schtId="1";
			e.printStackTrace();
		}
		return schtId;
	}
	
	@Override
	public HmiResultObj createScheduleStudio(ScheduleStudios scheduleStudios) {
		HmiResultObj hmiResultObj=null;
		try {
			sqlDao.insertUpdate(scheduleStudios);
			if(scheduleStudios.getSchsId()==0)
				scheduleStudios.setSchsId(Long.parseLong(getLastSchsIdSeq()));
			
			hmiResultObj=new HmiResultObj();
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS);
			hmiResultObj.setResultMessage(""+scheduleStudios.getSchsId());
		} catch (SqlErrorException e) {
			e.printStackTrace();
			hmiResultObj=new HmiResultObj();
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
			hmiResultObj.setResultMessage(ResultStatuObj.RESULT_STATU_FAIL_STR);
		}
		return hmiResultObj;
	}

	private synchronized String getLastSchsIdSeq(){
		String sql=	"SELECT CAST(max(SCHS_ID) AS CHAR) SCHS_ID" +
				" FROM schedule_studios ";
		String schsId="1";
		try {
			schsId=""+findEntityForObject(sql, null, Long.class);
			if(schsId.equals("null"))
				schsId="1";
		} catch (Exception e) {
			schsId="1";
			e.printStackTrace();
		}
		return schsId;
	}
	
	
	
	
	
	
	
	@Override
	public List<ScheduleTimePlan> findScheduleTimePlanByPlanId(long schId) {
		String sql="SELECT a.*,b.*,c.*"
				+ ",DATE_FORMAT(a.PLAN_START_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') PLAN_START_DATE_STR"
				+ ",DATE_FORMAT(a.PLAN_END_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') PLAN_END_DATE_STR"
				  + "  FROM  schedule_time_plan a,schedule_plan b,user c "
					+ " WHERE  a.SCH_ID=:schId "
					+ " AND a.SCHT_STAFF_ID=c.USER_ID "
					+ " AND a.SCH_ID=b.SCH_ID "
					+ " ORDER BY a.PLAN_START_DATE";
			 
			
			 Map<String, Object> paramMap=new HashMap<String, Object>();
			 paramMap.put("schId",schId);
			    
			 try{
				 List<ScheduleTimePlan> scheduleTimePlans=findEntityList(sql, paramMap, ScheduleTimePlan.class);
				
				return scheduleTimePlans;
			 }catch (Exception e) {
				 e.printStackTrace();
					return null;
			 }
	}
	
	@Override
	public List<ScheduleTimePlan> findScheduleTimePlanByPlanIdByDates(long schId, Date startDate, Date endDate) {
		String sql="SELECT a.*,b.*,c.*,DATE_FORMAT(a.PLAN_START_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') PLAN_START_DATE_STR"
				+ ",DATE_FORMAT(a.PLAN_END_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') PLAN_END_DATE_STR"
				  + "  FROM  schedule_time_plan a,schedule_plan b,user c "
					+ " WHERE  a.SCH_ID=:schId "
					+ " AND a.SCH_ID=b.SCH_ID "
					+ " AND a.SCHT_STAFF_ID=c.USER_ID "
					+ " AND a.PLAN_START_DATE>:startDate"
					+ "	AND a.PLAN_START_DATE<:endDate"
					+ " ORDER BY a.PLAN_START_DATE";
			 
			
			 Map<String, Object> paramMap=new HashMap<String, Object>();
			 paramMap.put("schId",schId);
			 paramMap.put("startDate",startDate);
			 paramMap.put("endDate",endDate);
			    
			 
			 ////System.out.println(sql);
			 ////System.out.println("schId "+schId);
		     ////System.out.println("startDate "+OhbeUtil.getDateStrByFormat(startDate, GlobalUtil.global.getPtDbDateFormat()));
			 ////System.out.println("endDate "+OhbeUtil.getDateStrByFormat(endDate, GlobalUtil.global.getPtDbDateFormat()));
				
			 try{
				 List<ScheduleTimePlan> scheduleTimePlans=findEntityList(sql, paramMap, ScheduleTimePlan.class);
				
				return scheduleTimePlans;
			 }catch (Exception e) {
				 e.printStackTrace();
					return null;
			 }
	}
	
	@Override
	public HmiResultObj deleteSchedulePlan(SchedulePlan schedulePlan) {
		HmiResultObj hmiResultObj=null;
		try {
			sqlDao.delete(schedulePlan);
			
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
	public SchedulePlan findSchedulePlanByPlanId(long schId) {
		String sql="SELECT a.*,b.* "
				  + "  FROM  schedule_plan a LEFT JOIN user b ON a.SCH_STAFF_ID=b.USER_ID "
					+ " WHERE  a.SCH_ID=:schId";
			 
			 Map<String, Object> paramMap=new HashMap<String, Object>();
			 paramMap.put("schId",schId);
			    
			 try{
				 SchedulePlan schedulePlan=findEntityForObject(sql, paramMap, SchedulePlan.class);
				 return schedulePlan;
			 }catch (Exception e) {
					return null;
			 }
	}

	@Override
	public List<ScheduleStudios> findScheduleStudiosByTimePlanId(long schtId) {
		String sql="SELECT a.*,b.*,c.*"
				+ ",DATE_FORMAT(a.PLAN_START_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') PLAN_START_DATE_STR"
				+ ",DATE_FORMAT(a.PLAN_END_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') PLAN_END_DATE_STR"
				  + "  FROM  schedule_time_plan a,schedule_studios b , def_studio c"
					+ " WHERE a.SCHT_ID=:schtId"
					+ " AND a.SCHT_ID=b.SCHT_ID "
					+ " AND c.STUDIO_ID=b.STUDIO_ID ";
			 
			
			 Map<String, Object> paramMap=new HashMap<String, Object>();
			 paramMap.put("schtId",schtId);
			   
			 try{
				 List<ScheduleStudios> scheduleStudios=findEntityList(sql, paramMap, ScheduleStudios.class);
				
				return scheduleStudios;
			 }catch (Exception e) {
				 e.printStackTrace();
					return null;
			 }
	}

	@Override
	public List<SchedulePlan> searchByQueryForStaff(ScheduleSearchObj scheduleSearchObj) {
		   String sql="SELECT a.*,e.*"
				  + "  FROM  schedule_plan a LEFT JOIN user e ON e.USER_ID=a.SCH_STAFF_ID "
					+ " WHERE  a.SCH_ID IN (SELECT SCH_ID FROM schedule_time_plan b "
					+ " 					  WHERE b.PLAN_START_DATE>:startDate"
					+ "							AND b.PLAN_START_DATE<:endDate"
					+ "    						AND b.SCHT_STAFF_ID=:staffId) ";
			 
			
			 Map<String, Object> paramMap=new HashMap<String, Object>();
			 paramMap.put("startDate",scheduleSearchObj.getStartDate());
			 paramMap.put("endDate",scheduleSearchObj.getEndDate());
			 paramMap.put("staffId",scheduleSearchObj.getStaffId());
			 
			 ////System.out.println(sql);
			 ////System.out.println("staffId "+scheduleSearchObj.getStaffId());
		     ////System.out.println("startDate "+OhbeUtil.getDateStrByFormat(scheduleSearchObj.getStartDate(), GlobalUtil.global.getPtDbDateFormat()));
			 ////System.out.println("endDate "+OhbeUtil.getDateStrByFormat(scheduleSearchObj.getEndDate(), GlobalUtil.global.getPtDbDateFormat()));
			 ////System.out.println("*********************END OF SEARCH PLAN***********************************");
			 
			 
			 
			 try{
				 List<SchedulePlan> schedulePlans=findEntityList(sql, paramMap, SchedulePlan.class);
				 return schedulePlans;
			 }catch (Exception e) {
				 e.printStackTrace();
					return null;
			 }
	}

	@Override
	public List<SchedulePlan> searchByQueryForStudiosForOtherStaffs(ScheduleSearchObj scheduleSearchObj) {
		String studioIds="";
		if(scheduleSearchObj.getStudios()==null){
			return new ArrayList<SchedulePlan>();
		}
		List<ScheduleStudios> scheduleStudios=scheduleSearchObj.getStudios();
		if(scheduleStudios.size()==0){
			return new ArrayList<SchedulePlan>();
		}
		int i=0;
		for (ScheduleStudios scheduleStudio : scheduleStudios) {
			if(i==0){
				studioIds+=scheduleStudio.getStudioId();
			}else{
				studioIds+=","+scheduleStudio.getStudioId();
			}
		}
		
		////System.out.println("studioIds :"+studioIds);
		String sql="SELECT a.*,e.* "
				  + "  FROM  schedule_plan a LEFT JOIN user e ON e.USER_ID=a.SCH_STAFF_ID "
					+ " WHERE a.SCH_ID IN ( SELECT SCH_ID FROM schedule_time_plan b "
					+ "						  WHERE b.PLAN_START_DATE>:startDate"
					+ "							AND b.PLAN_START_DATE<:endDate"
					+ "                         AND b.SCHT_STAFF_ID <> :staffId"
					+ "                         AND b.SCHT_ID IN (SELECT SCHT_ID FROM schedule_studios c "
					+ "											 WHERE c.STUDIO_ID IN ("+studioIds+")))";
			 
			
			 Map<String, Object> paramMap=new HashMap<String, Object>();
			 paramMap.put("startDate",scheduleSearchObj.getStartDate());
			 paramMap.put("endDate",scheduleSearchObj.getEndDate());
			 paramMap.put("staffId",scheduleSearchObj.getStaffId());
			    
			 
			 ////System.out.println(sql);
			 ////System.out.println("studioIds "+studioIds);
			 ////System.out.println("staffId "+scheduleSearchObj.getStaffId());
		     ////System.out.println("startDate "+OhbeUtil.getDateStrByFormat(scheduleSearchObj.getStartDate(), GlobalUtil.global.getPtDbDateFormat()));
			 ////System.out.println("endDate "+OhbeUtil.getDateStrByFormat(scheduleSearchObj.getEndDate(), GlobalUtil.global.getPtDbDateFormat()));
			 ////System.out.println("*********************END OF SEARCH PLAN OTHER INSTRUCTOR***********************************");
			 
			 
			 
			 try{
				 List<SchedulePlan> schedulePlans=findEntityList(sql, paramMap, SchedulePlan.class);
				 return schedulePlans;
			 }catch (Exception e) {
				 e.printStackTrace();
					return null;
			 }
		
	}

	@Override
	public HmiResultObj deleteScheduleTimePlan(ScheduleTimePlan scheduleTimePlan) {
		HmiResultObj hmiResultObj=null;
		try {
			sqlDao.delete(scheduleTimePlan);
			
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
	public List<ScheduleTimePlan> findScheduleTimePlanByDates(Date startDate, Date endDate) {
		String sql="SELECT a.*,b.*,c.*,DATE_FORMAT(a.PLAN_START_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') PLAN_START_DATE_STR"
				+ ",DATE_FORMAT(a.PLAN_END_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') PLAN_END_DATE_STR"
				  + "  FROM  schedule_time_plan a,user b,schedule_plan c "
					+ " WHERE a.PLAN_START_DATE>:startDate"
					+ "	AND a.PLAN_START_DATE<:endDate"
					+ " AND a.SCHT_STAFF_ID=b.USER_ID "
					+ " AND c.SCH_ID=a.SCH_ID "
					+ " ORDER BY a.PLAN_START_DATE ";
			 
			
			 Map<String, Object> paramMap=new HashMap<String, Object>();
			 paramMap.put("startDate",startDate);
			 paramMap.put("endDate",endDate);
			    
			 
			 	
			 try{
				 List<ScheduleTimePlan> scheduleTimePlans=findEntityList(sql, paramMap, ScheduleTimePlan.class);
				 return scheduleTimePlans;
			 }catch (Exception e) {
				 e.printStackTrace();
					return null;
			 }
	}

	@Override
	public List<ScheduleTimePlan> findScheduleTimePlanByStaffAndDate(Date startDate, Date endDate, long staffId) {
		String sql="SELECT a.*,b.*,c.*,DATE_FORMAT(a.PLAN_START_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') PLAN_START_DATE_STR"
				+ ",DATE_FORMAT(a.PLAN_END_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') PLAN_END_DATE_STR"
				  + "  FROM  schedule_time_plan a,user b,schedule_plan c "
					+ " WHERE a.PLAN_START_DATE>:startDate"
					+ "	AND a.PLAN_START_DATE<:endDate";
		
		if(staffId!=0){
			sql+=" AND a.SCHT_STAFF_ID=:staffId ";
		}
					
					
					sql+= " AND a.SCHT_STAFF_ID=b.USER_ID "
					+ " AND c.SCH_ID=a.SCH_ID "
					+ " ORDER BY a.PLAN_START_DATE ";
			 
			
			 Map<String, Object> paramMap=new HashMap<String, Object>();
			 paramMap.put("startDate",startDate);
			 paramMap.put("endDate",endDate);
			 paramMap.put("staffId",staffId);
			    
			 
			 ////System.out.println(sql);
			 ////System.out.println("startDate "+OhbeUtil.getDateStrByFormat(startDate, GlobalUtil.global.getPtDbDateFormat()));
			 ////System.out.println("endDate "+OhbeUtil.getDateStrByFormat(endDate, GlobalUtil.global.getPtDbDateFormat()));
				
			 try{
				 List<ScheduleTimePlan> scheduleTimePlans=findEntityList(sql, paramMap, ScheduleTimePlan.class);
				 return scheduleTimePlans;
			 }catch (Exception e) {
				 e.printStackTrace();
					return null;
			 }
	}

	@Override
	public ScheduleTimePlan findScheduleTimePlanById(long schtId) {
		String sql="SELECT a.*"
				+ " FROM  schedule_time_plan a "
					+ " WHERE  a.SCHT_ID=:schtId ";
			 
			
			 Map<String, Object> paramMap=new HashMap<String, Object>();
			 paramMap.put("schtId",schtId);
			    
			 try{
				 ScheduleTimePlan scheduleTimePlan=findEntityForObject(sql, paramMap, ScheduleTimePlan.class);
				
				return scheduleTimePlan;
			 }catch (Exception e) {
					return null;
			 }
	}

	

	
	
	
	
}
