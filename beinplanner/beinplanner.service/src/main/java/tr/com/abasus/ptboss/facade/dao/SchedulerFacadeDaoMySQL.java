package tr.com.abasus.ptboss.facade.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tr.com.abasus.general.dao.AbaxJdbcDaoSupport;
import tr.com.abasus.general.dao.SqlDao;
import tr.com.abasus.ptboss.packetsale.entity.PacketSaleFactory;
import tr.com.abasus.ptboss.schedule.entity.ScheduleStudios;
import tr.com.abasus.ptboss.schedule.entity.ScheduleTimePlan;
import tr.com.abasus.ptboss.schedule.entity.ScheduleUsersClassPlan;
import tr.com.abasus.ptboss.schedule.entity.ScheduleUsersPersonalPlan;
import tr.com.abasus.util.GlobalUtil;
import tr.com.abasus.util.OhbeUtil;

public class SchedulerFacadeDaoMySQL extends AbaxJdbcDaoSupport  implements SchedulerFacadeDao {

	SqlDao sqlDao;

	
	@Override
	public boolean haveInstructorGotClassesInThisTime(long staffId, Date planStartDate, Date planEndDate,long schtId) {
		String sql=" SELECT a.*"
				+ "  FROM schedule_time_plan a "
				+ "  WHERE a.SCHT_STAFF_ID=:staffId ";
				if(schtId!=0)
					sql+= " AND A.SCHT_ID<>:schtId ";
				
				
				sql+= "    AND ((a.PLAN_START_DATE>:planStartDate1 AND a.PLAN_START_DATE<:planEndDate1)"
				+ "    		 OR "
				+ "			(a.PLAN_START_DATE<:planStartDate2 AND a.PLAN_END_DATE>:planStartDate3))";
		
		
		 Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("staffId",staffId);
		 paramMap.put("planStartDate1",planStartDate);
		 paramMap.put("planEndDate1",planEndDate);
		 paramMap.put("planStartDate2",planStartDate);
		 paramMap.put("planStartDate3",planStartDate);
		 paramMap.put("schtId",schtId);
			
		 
		 ////System.out.println("**************************************************************************** ");
		 ////System.out.println("haveInstructorGotClassesInThisTime ");
		 ////System.out.println(sql);
		 ////System.out.println("staffId "+staffId);
		 ////System.out.println("planStartDate "+OhbeUtil.getDateStrByFormat(planStartDate, GlobalUtil.global.getPtDbDateFormat()+" "+"HH:mm"));
		 ////System.out.println("planEndDate "+OhbeUtil.getDateStrByFormat(planEndDate, GlobalUtil.global.getPtDbDateFormat()+" "+"HH:mm"));
		 ////System.out.println("**************************************************************************** ");
		 
		 try{
			 List<ScheduleTimePlan> scheduleTimePlans=findEntityList(sql, paramMap, ScheduleTimePlan.class);
			 if(scheduleTimePlans!=null){
				 if(scheduleTimePlans.size()>0){
					 return true;
				 }else{
					 return false;
				 }
				 
			 }else{
				 return false;
			 }
			 
		 }catch (Exception e) {
			  return false;
		 }
	}

	@Override
	public boolean isStudioReservedInThisTime(int studioId, Date planStartDate, Date planEndDate,long schtId) {
		String sql=" SELECT a.*"
				+ "  FROM schedule_time_plan a,schedule_studios b "
				+ "  WHERE a.SCHT_ID=b.SCHT_ID ";
				if(schtId!=0)
					sql+= " AND A.SCHT_ID<>:schtId ";
				
				sql+= "    AND a.STUDIO_ID=:studioId "
				+ "    AND a.PLAN_DATE>:planStartDate "
				+ "    AND a.PLAN_DATE<:planEndDate ";
		
		
		 Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("studioId",studioId);
		 paramMap.put("planStartDate",planStartDate);
		 paramMap.put("planEndDate",planEndDate);
		 paramMap.put("schtId",schtId);
			  
		 try{
			 List<ScheduleTimePlan> scheduleTimePlans=findEntityList(sql, paramMap, ScheduleTimePlan.class);
			 if(scheduleTimePlans!=null){
				 if(scheduleTimePlans.size()>0){
					 return true;
				 }else{
					 return false;
				 }
				 
			 }else{
				 return false;
			 }
			 
		 }catch (Exception e) {
			  return false;
		 }
	}
	
	
	@Override
	public boolean haveMemberGotPersonalSchedulerForSale(long saleId) {
		
		String sql="SELECT a.*"
				+ "  FROM schedule_users_personal_plan a "
					+ " WHERE a.SALE_ID=:saleId  ";
			 
		     Map<String, Object> paramMap=new HashMap<String, Object>();
			 paramMap.put("saleId",saleId);
			    
			 try{
				 List<ScheduleUsersClassPlan> scheduleUsersClassPlans=findEntityList(sql, paramMap, ScheduleUsersClassPlan.class);
				 if(scheduleUsersClassPlans!=null){
					 if(scheduleUsersClassPlans.size()>0){
						 return true;
					 }else{
						 return false;
					 }
					 
				 }else{
					 return false;
				 }
				 
			 }catch (Exception e) {
				  return false;
			 }
		
	
	}

	@Override
	public boolean haveMemberGotClassSchedulerForSale(long saleId) {
		String sql="SELECT a.*"
				+ "  FROM schedule_users_class_plan a "
					+ " WHERE a.SALE_ID=:saleId  ";
			 
		    Map<String, Object> paramMap=new HashMap<String, Object>();
			 paramMap.put("saleId",saleId);
			    
			 try{
				 List<ScheduleUsersClassPlan> scheduleUsersClassPlans=findEntityList(sql, paramMap, ScheduleUsersClassPlan.class);
				 if(scheduleUsersClassPlans!=null){
					 if(scheduleUsersClassPlans.size()>0){
						 return true;
					 }else{
						 return false;
					 }
					 
				 }else{
					 return false;
				 }
				 
			 }catch (Exception e) {
				  return false;
			 }
	}

	public SqlDao getSqlDao() {
		return sqlDao;
	}

	public void setSqlDao(SqlDao sqlDao) {
		this.sqlDao = sqlDao;
	}
/*
	@Override
	public boolean isBonusPayedForThisTimePlan(long schtId) {
	String sql=" SELECT a.*"
					+ "  FROM schedule_time_plan a "
					+ "  WHERE a.SCHT_ID=:schtId ";
			
			
			 Map<String, Object> paramMap=new HashMap<String, Object>();
			 paramMap.put("schtId",schtId);
			  
			 try{
				 ScheduleTimePlan scheduleTimePlan=findEntityForObject(sql, paramMap, ScheduleTimePlan.class);
				 if(scheduleTimePlan.getBonusPayed()==0){
					 return false;
				 }else{
					 return true;
				 }
			}catch (Exception e) {
				  return false;
			 }
		
	}

	
	@Override
	public boolean isBonusPayedForThisScheduledUserClassPlan(long sucpId) {
		String sql=" SELECT a.*"
				+ "  FROM schedule_user_class_plan a "
				+ "  WHERE a.SUCP_ID=:sucpId ";
		
		
		 Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("sucpId",sucpId);
		  
		 try{
			 ScheduleUsersClassPlan scheduleUsersClassPlan=findEntityForObject(sql, paramMap, ScheduleUsersClassPlan.class);
			 if(scheduleUsersClassPlan.getBonusPayed()==0){
				 return false;
			 }else{
				 return true;
			 }
		}catch (Exception e) {
			  return false;
		 }
	}

	@Override
	public boolean isBonusPayedForThisScheduledUserPersonalPlan(long suppId) {
		String sql=" SELECT a.*"
				+ "  FROM schedule_user_personal_plan a "
				+ "  WHERE a.SUPP_ID=:suppId ";
		
		
		 Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("suppId",suppId);
		  
		 try{
			 ScheduleUsersPersonalPlan scheduleUsersPersonalPlan=findEntityForObject(sql, paramMap, ScheduleUsersPersonalPlan.class);
			 if(scheduleUsersPersonalPlan.getBonusPayed()==0){
				 return false;
			 }else{
				 return true;
			 }
		}catch (Exception e) {
			  return false;
		 }
	}
*/

	@Override
	public boolean isStudioUsedBefore(int studioId) {
		String sql=" SELECT * "
				+ "  FROM schedule_studios a "
				+ "  WHERE a.STUDIO_ID=:studioId LIMIT 1 ";
		
		
		 Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("studioId",studioId);
		  
		 try{
			 ScheduleStudios scheduleStudio=findEntityForObject(sql, paramMap, ScheduleStudios.class);
			 if(scheduleStudio!=null){
				 return false;
			 }else{
				 return true;
			 }
		}catch (Exception e) {
			  return false;
		 }
	}

	@Override
	public ScheduleTimePlan findFirstClassOfPlan(long schId) {
		
		String sql="SELECT * FROM schedule_time_plan a "
				+ " WHERE a.SCH_ID=:schId"
				+ "  AND a.PLAN_START_DATE = "
				+ " (SELECT min(b.PLAN_START_DATE) FROM schedule_time_plan b WHERE b.SCH_ID=:schId2 )"
				+ " LIMIT 1 ";
		
		Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("schId",schId);
		 paramMap.put("schId2",schId);
		 
		 try{
			 ScheduleTimePlan scheduleTimePlan=findEntityForObject(sql, paramMap, ScheduleTimePlan.class);
			 return scheduleTimePlan;
		}catch (Exception e) {
			  return null;
		 }
	}
	
	
	
}
