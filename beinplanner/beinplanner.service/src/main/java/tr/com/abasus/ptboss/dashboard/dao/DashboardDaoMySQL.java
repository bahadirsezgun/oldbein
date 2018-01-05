package tr.com.abasus.ptboss.dashboard.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tr.com.abasus.general.dao.AbaxJdbcDaoSupport;
import tr.com.abasus.general.dao.SqlDao;
import tr.com.abasus.ptboss.dashboard.entity.ActiveMember;
import tr.com.abasus.ptboss.dashboard.entity.LeftPaymentInfo;
import tr.com.abasus.ptboss.dashboard.entity.PlannedClassInfo;
import tr.com.abasus.ptboss.dashboard.entity.TodayPayment;
import tr.com.abasus.ptboss.packetpayment.entity.PacketPaymentClass;
import tr.com.abasus.ptboss.packetpayment.entity.PacketPaymentMembership;
import tr.com.abasus.ptboss.packetpayment.entity.PacketPaymentPersonal;
import tr.com.abasus.ptboss.packetsale.entity.PacketSaleClass;
import tr.com.abasus.ptboss.packetsale.entity.PacketSaleFactory;
import tr.com.abasus.ptboss.packetsale.entity.PacketSaleMembership;
import tr.com.abasus.ptboss.packetsale.entity.PacketSalePersonal;
import tr.com.abasus.ptboss.ptuser.entity.User;
import tr.com.abasus.ptboss.schedule.entity.ScheduleMembershipPlan;
import tr.com.abasus.ptboss.schedule.entity.ScheduleTimePlan;
import tr.com.abasus.util.DateTimeUtil;
import tr.com.abasus.util.GlobalUtil;
import tr.com.abasus.util.OhbeUtil;

public class DashboardDaoMySQL extends AbaxJdbcDaoSupport implements DashboardDao {

	SqlDao sqlDao;

	@Override
	public ActiveMember getActiveMembersInPersonal(int firmId) {
		String sql="SELECT COUNT(a.USER_ID)"
				+" FROM user a"
				+" WHERE a.USER_ID IN (SELECT b.USER_ID FROM schedule_users_personal_plan b, schedule_time_plan c,user d"
				+"                      WHERE b.SCHT_ID=c.SCHT_ID"
				+ "  					AND d.USER_ID=b.USER_ID"
				+ "						AND d.FIRM_ID=:firmId "
				+"                       AND c.PLAN_START_DATE>(SELECT CURDATE())"
				+"                       GROUP BY b.USER_ID) ";
		
		
		 Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("firmId",firmId);
		 
		
		ActiveMember activeMember=new ActiveMember();
		activeMember.setActiveMemberCount(0);
		try {
			 long countAM=findEntityForObject(sql, paramMap, Long.class);
			 activeMember.setActiveMemberCount(countAM);
				
			 return activeMember;
		}catch (Exception e) {
			 return activeMember;
		}
	 	
	 	
	 	
	}

	@Override
	public ActiveMember getActiveMembersInClass(int firmId) {
		String sql="SELECT COUNT(a.USER_ID)"
				+" FROM user a"
				+" WHERE a.USER_ID IN (SELECT b.USER_ID FROM schedule_users_class_plan b, schedule_time_plan c,user d"
				+"                      WHERE b.SCHT_ID=c.SCHT_ID"
				+ "  					AND d.USER_ID=b.USER_ID"
				+ "						AND d.FIRM_ID=:firmId "
				+"                       AND c.PLAN_START_DATE>(SELECT CURDATE())"
				+"                       GROUP BY b.USER_ID) ";
		
		Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("firmId",firmId);
		 
		
		ActiveMember activeMember=new ActiveMember();
		activeMember.setActiveMemberCount(0);
		try {
			 long countAM=findEntityForObject(sql, paramMap, Long.class);
			 activeMember.setActiveMemberCount(countAM);
				
			 return activeMember;
		}catch (Exception e) {
			 return activeMember;
		}
	}
	
	@Override
	public ActiveMember getActiveMembersInMembership(int firmId) {
		String sql="SELECT COUNT(a.USER_ID)"
				+" FROM user a"
				+" WHERE a.USER_ID IN (SELECT b.USER_ID FROM schedule_membership_plan b,user d "
				+"                      WHERE b.SCHT_ID=c.SCHT_ID"
				+ "  					AND d.USER_ID=b.USER_ID"
				+ "						AND d.FIRM_ID=:firmId "
				+"                       AND c.PLAN_START_DATE>(SELECT CURDATE())"
				+"                       GROUP BY b.USER_ID) ";
		
		
		Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("firmId",firmId);
		 
		 
		ActiveMember activeMember=new ActiveMember();
		activeMember.setActiveMemberCount(0);
		try {
			 long countAM=findEntityForObject(sql, paramMap, Long.class);
			 activeMember.setActiveMemberCount(countAM);
				
			 return activeMember;
		}catch (Exception e) {
			 return activeMember;
		}
	}
	
	
	@Override
	public LeftPaymentInfo getLeftPaymentInfoInPersonal(int firmId) {
		String sql="SELECT b.PACKET_PRICE,IFNULL(a.PAY_AMOUNT,0) PAY_AMOUNT"
				+" FROM packet_sale_personal b,packet_payment_personal a "
				+ " WHERE a.SALE_ID=b.SALE_ID "
				+ " AND b.PACKET_PRICE>a.PAY_AMOUNT "
				+ " AND b.USER_ID IN (SELECT USER_ID FROM user WHERE FIRM_ID=:firmId)";
		
		
		Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("firmId",firmId);
		 
		 
		ActiveMember activeMember=new ActiveMember();
		activeMember.setActiveMemberCount(0);
		try {
			List<PacketPaymentPersonal> packetPaymentPersonals=findEntityList(sql, paramMap, PacketPaymentPersonal.class);
				
			int leftPaymentCount=0;
			double leftPayment=0;
			for (PacketPaymentPersonal packetPaymentPersonal : packetPaymentPersonals) {
				if(packetPaymentPersonal.getPacketPrice()>packetPaymentPersonal.getPayAmount()){
					leftPaymentCount++;
					leftPayment+=packetPaymentPersonal.getPacketPrice()-packetPaymentPersonal.getPayAmount();
				}
				
				
			}
			
			LeftPaymentInfo leftPaymentInfo=new LeftPaymentInfo();
			leftPaymentInfo.setLeftPayment(leftPayment);
			leftPaymentInfo.setLeftPaymentCount(leftPaymentCount);
			 return leftPaymentInfo;
		}catch (Exception e) {
			 return new LeftPaymentInfo();
		}
	}

	@Override
	public LeftPaymentInfo getLeftPaymentInfoInClass(int firmId) {
		String sql="SELECT b.PACKET_PRICE,IFNULL(a.PAY_AMOUNT,0) PAY_AMOUNT"
				+" FROM packet_sale_class b,packet_payment_class a "
				+ " WHERE a.SALE_ID=b.SALE_ID "
				+ " AND b.PACKET_PRICE>a.PAY_AMOUNT "
				+ " AND b.USER_ID IN (SELECT USER_ID FROM user WHERE FIRM_ID=:firmId)";
		
		Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("firmId",firmId);
		 
		 
		ActiveMember activeMember=new ActiveMember();
		activeMember.setActiveMemberCount(0);
		try {
			List<PacketPaymentClass> packetPaymentClasses=findEntityList(sql, paramMap, PacketPaymentClass.class);
				
			int leftPaymentCount=0;
			double leftPayment=0;
			for (PacketPaymentClass packetPaymentClass : packetPaymentClasses) {
				if(packetPaymentClass.getPacketPrice()>packetPaymentClass.getPayAmount()){
					leftPaymentCount++;
					leftPayment+=packetPaymentClass.getPacketPrice()-packetPaymentClass.getPayAmount();
				}
				
				
			}
			
			LeftPaymentInfo leftPaymentInfo=new LeftPaymentInfo();
			leftPaymentInfo.setLeftPayment(leftPayment);
			leftPaymentInfo.setLeftPaymentCount(leftPaymentCount);
			 return leftPaymentInfo;
		}catch (Exception e) {
			 return new LeftPaymentInfo();
		}
	}

	@Override
	public LeftPaymentInfo getLeftPaymentInfoInMembership(int firmId) {
	  String sql="SELECT b.PACKET_PRICE,IFNULL(a.PAY_AMOUNT,0) PAY_AMOUNT"
				+" FROM packet_sale_membership b,packet_payment_membership a "
				+ " WHERE a.SALE_ID=b.SALE_ID "
				+ " AND b.PACKET_PRICE>a.PAY_AMOUNT "
				+ " AND b.USER_ID IN (SELECT USER_ID FROM user WHERE FIRM_ID=:firmId)";
		
		Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("firmId",firmId);
		 
		 
		ActiveMember activeMember=new ActiveMember();
		activeMember.setActiveMemberCount(0);
		try {
			List<PacketPaymentMembership> packetPaymentMemberships=findEntityList(sql, paramMap, PacketPaymentMembership.class);
				
			int leftPaymentCount=0;
			double leftPayment=0;
			for (PacketPaymentMembership packetPaymentMembership : packetPaymentMemberships) {
				if(packetPaymentMembership.getPacketPrice()>packetPaymentMembership.getPayAmount()){
					leftPaymentCount++;
					leftPayment+=packetPaymentMembership.getPacketPrice()-packetPaymentMembership.getPayAmount();
				}
				
				
			}
			
			LeftPaymentInfo leftPaymentInfo=new LeftPaymentInfo();
			leftPaymentInfo.setLeftPayment(leftPayment);
			leftPaymentInfo.setLeftPaymentCount(leftPaymentCount);
			 return leftPaymentInfo;
		}catch (Exception e) {
			 return new LeftPaymentInfo();
		}
	}
	

	@Override
	public TodayPayment geTodayPayment() {
		// TODO Auto-generated method stub
		return null;
	}

	public SqlDao getSqlDao() {
		return sqlDao;
	}

	public void setSqlDao(SqlDao sqlDao) {
		this.sqlDao = sqlDao;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PacketSaleFactory> getLeftPaymentSaleInPersonal(int firmId) {
		String sql="SELECT b.*,a.*,c.*,d.*,'psp' progType"
				+ " ,DATE_FORMAT(b.SALES_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') SALES_DATE_STR"
				+" FROM program_personal c,user d, packet_sale_personal b ,packet_payment_personal a "
				+ "   WHERE a.SALE_ID=b.SALE_ID "
				+ "  AND b.USER_ID IN (SELECT USER_ID FROM user WHERE FIRM_ID=:firmId)"
				+ "   AND c.PROG_ID=b.PROG_ID"
				+ "   AND d.USER_ID=b.USER_ID "
				+ "   AND b.PACKET_PRICE>a.PAY_AMOUNT";
		
		
		Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("firmId",firmId);
		 
		 try {
			List<? extends PacketSaleFactory> packetSalePersonals=findEntityList(sql, paramMap, PacketSalePersonal.class);
			 return (List<PacketSaleFactory>)packetSalePersonals;
		}catch (Exception e) {
			e.printStackTrace();
			 return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PacketSaleFactory> getLeftPaymentSaleInClass(int firmId) {
		
		
		String sql="SELECT b.*,a.*,c.*,d.*,'psc' progType"
				+ " ,DATE_FORMAT(b.SALES_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') SALES_DATE_STR"
				+" FROM program_class c,user d, packet_sale_class b ,packet_payment_class a "
				+ "   WHERE a.SALE_ID=b.SALE_ID "
				+ "  AND b.USER_ID IN (SELECT USER_ID FROM user WHERE FIRM_ID=:firmId)"
				+ "   AND c.PROG_ID=b.PROG_ID"
				+ "   AND d.USER_ID=b.USER_ID "
				+ "   AND b.PACKET_PRICE>a.PAY_AMOUNT";
		
		
		Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("firmId",firmId);
		try {
			List<? extends PacketSaleFactory> packetPaymentClasses=findEntityList(sql, paramMap, PacketSaleClass.class);
			return (List<PacketSaleFactory>)packetPaymentClasses;
		}catch (Exception e) {
			e.printStackTrace();
			 return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PacketSaleFactory> getLeftPaymentSaleInMembership(int firmId) {
		
		
		String sql="SELECT b.*,a.*,c.*,d.*,'psm' progType"
				+ " ,DATE_FORMAT(b.SALES_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') SALES_DATE_STR"
				+" FROM program_membership c,user d, packet_sale_membership b ,packet_payment_membership a "
				+ "   WHERE a.SALE_ID=b.SALE_ID "
				+ "  AND b.USER_ID IN (SELECT USER_ID FROM user WHERE FIRM_ID=:firmId)"
				+ "   AND c.PROG_ID=b.PROG_ID"
				+ "   AND d.USER_ID=b.USER_ID "
				+ "   AND b.PACKET_PRICE>a.PAY_AMOUNT";
		
		
		
		
		Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("firmId",firmId);
		 
		 
		ActiveMember activeMember=new ActiveMember();
		activeMember.setActiveMemberCount(0);
		try {
			List<? extends PacketSaleFactory> packetSaleMemberhips=findEntityList(sql, paramMap, PacketSaleMembership.class);
			 return (List<PacketSaleFactory>)packetSaleMemberhips;
		}catch (Exception e) {
			e.printStackTrace();
			 return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PacketSaleFactory> getNoPaymentSaleInPersonal(int firmId) {
		String sql="SELECT b.*,c.*,d.*,'psp' progType"
				+ " ,DATE_FORMAT(b.SALES_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') SALES_DATE_STR"
				+" FROM program_personal c,user d, packet_sale_personal b "
				+ " WHERE SALE_ID NOT IN (SELECT SALE_ID FROM packet_payment_personal a )"
				+ "   AND b.USER_ID IN (SELECT USER_ID FROM user WHERE FIRM_ID=:firmId)"
				+ "   AND c.PROG_ID=b.PROG_ID"
				+ "   AND d.USER_ID=b.USER_ID ";
		
		
		Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("firmId",firmId);
		 
		 
		ActiveMember activeMember=new ActiveMember();
		activeMember.setActiveMemberCount(0);
		try {
			List<? extends PacketSaleFactory> packetSalePersonals=findEntityList(sql, paramMap, PacketSalePersonal.class);
			 return (List<PacketSaleFactory>)packetSalePersonals;
		}catch (Exception e) {
			e.printStackTrace();
			 return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PacketSaleFactory> getNoPaymentSaleInClass(int firmId) {
		String sql="SELECT b.*,c.*,d.*,'psc' progType"
				+ " ,DATE_FORMAT(b.SALES_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') SALES_DATE_STR"
				+" FROM program_class c,user d, packet_sale_class b "
				+ " WHERE SALE_ID NOT IN (SELECT SALE_ID FROM packet_payment_class a )"
				+ "   AND b.USER_ID IN (SELECT USER_ID FROM user WHERE FIRM_ID=:firmId)"
				+ "   AND c.PROG_ID=b.PROG_ID"
				+ "   AND d.USER_ID=b.USER_ID ";
		
		
		Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("firmId",firmId);
		 
		 
		ActiveMember activeMember=new ActiveMember();
		activeMember.setActiveMemberCount(0);
		try {
			List<? extends PacketSaleFactory> packetSaleClasses=findEntityList(sql, paramMap, PacketSaleClass.class);
			 return (List<PacketSaleFactory>)packetSaleClasses;
		}catch (Exception e) {
			e.printStackTrace();
			 return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PacketSaleFactory> getNoPaymentSaleInMembership(int firmId) {
		String sql="SELECT b.*,c.*,d.*,'psm' progType"
				+ " ,DATE_FORMAT(b.SALES_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') SALES_DATE_STR"
				+" FROM program_membership c,user d, packet_sale_membership b "
				+ " WHERE SALE_ID NOT IN (SELECT SALE_ID FROM packet_payment_membership a )"
				+ "   AND b.USER_ID IN (SELECT USER_ID FROM user WHERE FIRM_ID=:firmId)"
				+ "   AND c.PROG_ID=b.PROG_ID"
				+ "   AND d.USER_ID=b.USER_ID ";
		
		
		Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("firmId",firmId);
		 
		 
		ActiveMember activeMember=new ActiveMember();
		activeMember.setActiveMemberCount(0);
		try {
			List<? extends PacketSaleFactory> packetSaleMemberhips=findEntityList(sql, paramMap, PacketSaleMembership.class);
			 return (List<PacketSaleFactory>)packetSaleMemberhips;
		}catch (Exception e) {
			e.printStackTrace();
			 return null;
		}
	}

	@Override
	public LeftPaymentInfo getNoPaymentInfoInPersonal(int firmId) {
		String sql="SELECT b.PACKET_PRICE,'psp' progType"
				+" FROM packet_sale_personal b  "
				+ " WHERE b.SALE_ID NOT IN (SELECT SALE_ID FROM packet_payment_personal a ) "
				+ " AND b.USER_ID IN (SELECT USER_ID FROM user WHERE FIRM_ID=:firmId)";
		
		Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("firmId",firmId);
		 
		 
		try {
			List<PacketPaymentPersonal> packetPaymentPersonals=findEntityList(sql, paramMap, PacketPaymentPersonal.class);
				
			int noPaymentCount=0;
			double noPayment=0;
			for (PacketPaymentPersonal packetPaymentPersonal : packetPaymentPersonals) {
					noPaymentCount++;
					noPayment+=packetPaymentPersonal.getPacketPrice();
			}
			
			LeftPaymentInfo leftPaymentInfo=new LeftPaymentInfo();
			leftPaymentInfo.setNoPayment(noPayment);
			leftPaymentInfo.setNoPaymentCount(noPaymentCount);
			 return leftPaymentInfo;
		}catch (Exception e) {
			 return new LeftPaymentInfo();
		}
	}

	@Override
	public LeftPaymentInfo getNoPaymentInfoInClass(int firmId) {
		String sql="SELECT b.PACKET_PRICE,'psc' progType"
				+" FROM packet_sale_class b  "
				+ " WHERE b.SALE_ID NOT IN (SELECT SALE_ID FROM packet_payment_class a ) "
				+ " AND b.USER_ID IN (SELECT USER_ID FROM user WHERE FIRM_ID=:firmId)";
		
		Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("firmId",firmId);
		 
		 
		try {
			List<PacketPaymentClass> packetPaymentClasses=findEntityList(sql, paramMap, PacketPaymentClass.class);
				
			int noPaymentCount=0;
			double noPayment=0;
			for (PacketPaymentClass packetPaymentClass : packetPaymentClasses) {
					noPaymentCount++;
					noPayment+=packetPaymentClass.getPacketPrice();
			}
			
			LeftPaymentInfo leftPaymentInfo=new LeftPaymentInfo();
			leftPaymentInfo.setNoPayment(noPayment);
			leftPaymentInfo.setNoPaymentCount(noPaymentCount);
			 return leftPaymentInfo;
		}catch (Exception e) {
			 return new LeftPaymentInfo();
		}
	}

	@Override
	public LeftPaymentInfo getNoPaymentInfoInMembership(int firmId) {
		String sql="SELECT b.PACKET_PRICE,'psm' progType"
				+" FROM packet_sale_membership b  "
				+ " WHERE b.SALE_ID NOT IN (SELECT SALE_ID FROM packet_payment_membership a ) "
				+ " AND b.USER_ID IN (SELECT USER_ID FROM user WHERE FIRM_ID=:firmId)";
		
		Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("firmId",firmId);
		 
		 
		try {
			List<PacketPaymentMembership> packetPaymentMemberships=findEntityList(sql, paramMap, PacketPaymentMembership.class);
				
			int noPaymentCount=0;
			double noPayment=0;
			for (PacketPaymentMembership packetPaymentMembership : packetPaymentMemberships) {
					noPaymentCount++;
					noPayment+=packetPaymentMembership.getPacketPrice();
			}
			
			LeftPaymentInfo leftPaymentInfo=new LeftPaymentInfo();
			leftPaymentInfo.setNoPayment(noPayment);
			leftPaymentInfo.setNoPaymentCount(noPaymentCount);
			 return leftPaymentInfo;
		}catch (Exception e) {
			 return new LeftPaymentInfo();
		}
	}

	@Override
	public PlannedClassInfo getPlannedClassInfoForPersonalAndClass(int firmId, int year, int month) {
		String monthStr=""+month;
		if(month<10)
			 monthStr="0"+month;
		
		String startDateStr="01/"+monthStr+"/"+year+" 00:00";
		
		Date startDate=OhbeUtil.getThatDayFormatNotNull(startDateStr, "dd/MM/yyyy HH:mm");
		Date endDate=OhbeUtil.getDateForNextMonth(startDate, 1);
		
		
		PlannedClassInfo plannedClassInfo=new PlannedClassInfo();
		
		
		String sql="SELECT IFNULL(COUNT(SCHT_ID),0) "
				+" FROM schedule_time_plan a "
				+ " WHERE a.PLAN_START_DATE>=:startDate"
				+ " AND a.PLAN_START_DATE<:endDate";
		
		Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("startDate",startDate);
		 paramMap.put("endDate",endDate);
		 
		try {
			long classCount=findEntityForObject(sql, paramMap, Long.class);
			plannedClassInfo.setClassCount(classCount);
			plannedClassInfo.setMonth(month);
			plannedClassInfo.setMonthName(DateTimeUtil.getMonthNamesBySequence(month));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return plannedClassInfo;
	}

	

	@Override
	public PlannedClassInfo getPlannedClassInfoForMembership(int firmId, int year, int month) {
		String monthStr=""+month;
		if(month<10)
			 monthStr="0"+month;
		
		String startDateStr="01/"+monthStr+"/"+year+" 00:00";
		
		Date startDate=OhbeUtil.getThatDayFormatNotNull(startDateStr, "dd/MM/yyyy HH:mm");
		Date endDate=OhbeUtil.getDateForNextMonth(startDate, 1);
		
		
		PlannedClassInfo plannedClassInfo=new PlannedClassInfo();
		
		
		String sql="SELECT IFNULL(COUNT(SMTP_ID),0) "
				+" FROM schedule_membership_time_plan a "
				+ " WHERE a.SMP_START_DATE>=:startDate"
				+ " AND a.SMP_START_DATE<:endDate";
		
		Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("startDate",startDate);
		 paramMap.put("endDate",endDate);
		 
		try {
			long classCount=findEntityForObject(sql, paramMap, Long.class);
			plannedClassInfo.setClassCount(classCount);
			plannedClassInfo.setMonth(month);
			plannedClassInfo.setMonthName(DateTimeUtil.getMonthNamesBySequence(month));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return plannedClassInfo;
	}

	@Override
	public List<ScheduleTimePlan> findLastClassesOfUsersThisWeekForPersonal(int firmId) {
		
		Date weekStartDate=DateTimeUtil.getWeekStartDate();
		Date weekEndDate=DateTimeUtil.getWeekEndDate();
		 
		 String sql="SELECT a.*,b.*,e.* "
				 + ",DATE_FORMAT(c.PLAN_START_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') PLAN_START_DATE_STR "
						 + " FROM user a,schedule_users_personal_plan b,schedule_time_plan c,schedule_plan d, program_personal e "
						 + " WHERE c.PLAN_START_DATE>=:weekStartDate1"
						 + " and c.PLAN_START_DATE<:weekEndDate1"
						 + " AND a.USER_ID=b.USER_ID"
						 + " AND b.SCHT_ID=c.SCHT_ID"
						 + " AND d.SCH_ID=c.SCH_ID"
						 + " AND e.PROG_ID=d.PROG_ID"
						 + " AND c.SCHT_ID IN (SELECT MAX(SCHT_ID) FROM schedule_time_plan f,schedule_plan g"
																+ " WHERE f.SCH_ID=g.SCH_ID"
																+ " AND  f.PLAN_START_DATE>=:weekStartDate2"
																+ " AND  f.PLAN_START_DATE<:weekEndDate2"
																+ " GROUP BY g.SCH_ID)";
		 
		 
		 
		 Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("weekStartDate1",weekStartDate);
		 paramMap.put("weekEndDate1",weekEndDate);
		
		 paramMap.put("weekStartDate2",weekStartDate);
		 paramMap.put("weekEndDate2",weekEndDate);
		
		
		
		 List<ScheduleTimePlan> stp=new ArrayList<ScheduleTimePlan>();
			
		try {
			List<ScheduleTimePlan> scheduleTimePlans = findEntityList(sql, paramMap, ScheduleTimePlan.class);
			for (ScheduleTimePlan scheduleTimePlan : scheduleTimePlans) {
				if(isLastPlanForPersonalAndClass(scheduleTimePlan)){
					stp.add(scheduleTimePlan);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return stp;
	}
	
	
	private boolean isLastPlanForPersonalAndClass(ScheduleTimePlan scheduleTimePlan){
		String sql="SELECT a.* FROM schedule_time_plan a"
				+ " WHERE a.SCH_ID=:schId"
				+ "   AND a.PLAN_START_DATE>:planStartDate ";
		
		 Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("planStartDate",scheduleTimePlan.getPlanStartDate());
		 paramMap.put("schId",scheduleTimePlan.getSchId());
		
		try {
			List<ScheduleTimePlan> scheduleTimePlans = findEntityList(sql, paramMap, ScheduleTimePlan.class);
			
			if(scheduleTimePlans.size()>0)
				return false;
			else
				return true;
			
		} catch (Exception e) {
		
			e.printStackTrace();
			return false;
		}
		
		
	}

	@Override
	public List<ScheduleTimePlan> findLastClassesOfUsersThisWeekForClass(int firmId) {
		Date weekStartDate=DateTimeUtil.getWeekStartDate();
		Date weekEndDate=DateTimeUtil.getWeekEndDate();
		 
		 String sql="SELECT a.*,b.*,e.*,d.* "
				 + ",DATE_FORMAT(c.PLAN_START_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') PLAN_START_DATE_STR "
						 + "FROM user a,schedule_users_class_plan b,schedule_time_plan c,schedule_plan d, program_class e "
						 + " WHERE c.PLAN_START_DATE>=:weekStartDate1 "
						 + " AND c.PLAN_START_DATE<:weekEndDate1 "
						 + " AND a.USER_ID=b.USER_ID "
						 + " AND b.SCHT_ID=c.SCHT_ID "
						 + " AND d.SCH_ID=c.SCH_ID "
						 + " AND e.PROG_ID=d.PROG_ID "
						 + " AND c.SCHT_ID IN (SELECT MAX(SCHT_ID) FROM schedule_time_plan f,schedule_plan g "
																 + " WHERE f.SCH_ID=g.SCH_ID "
																 + " AND  f.PLAN_START_DATE>=:weekStartDate2 "
																 + " AND  f.PLAN_START_DATE<:weekEndDate2 "
																 + " GROUP BY g.SCH_ID) ";
		 
		 
		 
		 Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("weekStartDate1",weekStartDate);
		 paramMap.put("weekEndDate1",weekEndDate);
		
		 paramMap.put("weekStartDate2",weekStartDate);
		 paramMap.put("weekEndDate2",weekEndDate);
		
		
		
		 List<ScheduleTimePlan> stp=new ArrayList<ScheduleTimePlan>();
			
		try {
			List<ScheduleTimePlan> scheduleTimePlans = findEntityList(sql, paramMap, ScheduleTimePlan.class);
			for (ScheduleTimePlan scheduleTimePlan : scheduleTimePlans) {
				if(isLastPlanForPersonalAndClass(scheduleTimePlan)){
					stp.add(scheduleTimePlan);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return stp;
	}

	@Override
	public List<ScheduleMembershipPlan> findLastClassesOfUsersThisWeekForMembership(int firmId) {
		Date weekStartDate=DateTimeUtil.getWeekStartDate();
		Date weekEndDate=DateTimeUtil.getWeekEndDate();
		 
		 String sql="SELECT a.*,b.*,c.* "
				 + ",DATE_FORMAT(b.SMP_END_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') SMP_END_DATE_STR "
						 + " FROM user a,schedule_membership_plan b,program_membership c "
						 + " WHERE b.SMP_END_DATE>=:weekStartDate1 "
						 + " and b.SMP_END_DATE<:weekEndDate1 "
						 + " AND a.USER_ID=b.USER_ID "
						 + " AND b.PROG_ID=c.PROG_ID ";
		 
		 
		 
		 Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("weekStartDate1",weekStartDate);
		 paramMap.put("weekEndDate1",weekEndDate);
		
		 
		
		
		try {
			List<ScheduleMembershipPlan> scheduleMembershipPlans = findEntityList(sql, paramMap, ScheduleMembershipPlan.class);
			return scheduleMembershipPlans;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<ScheduleTimePlan> findLastClassesOfUsersNextWeekForPersonal(int firmId) {
		Date weekStartDate=DateTimeUtil.getNextWeekStartDate();
		Date weekEndDate=DateTimeUtil.getNextWeekEndDate();
		 
		 String sql="SELECT a.*,b.*,e.*,c.*,d.* "
				 + ",DATE_FORMAT(c.PLAN_START_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') PLAN_START_DATE_STR "
						 + " FROM user a,schedule_users_personal_plan b,schedule_time_plan c,schedule_plan d, program_personal e "
						 + " WHERE c.PLAN_START_DATE>=:weekStartDate1 "
						 + " and c.PLAN_START_DATE<:weekEndDate1 "
						 + " AND a.USER_ID=b.USER_ID "
						 + " AND b.SCHT_ID=c.SCHT_ID "
						 + " AND d.SCH_ID=c.SCH_ID "
						 + " AND e.PROG_ID=d.PROG_ID "
						 + " AND c.SCHT_ID IN (SELECT MAX(SCHT_ID) FROM schedule_time_plan f,schedule_plan g "
																 + " WHERE f.SCH_ID=g.SCH_ID "
																 + " AND  f.PLAN_START_DATE>=:weekStartDate2 "
																 + " AND  f.PLAN_START_DATE<:weekEndDate2 "
																 + " GROUP BY g.SCH_ID) ";
		 
		 
		 
		 Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("weekStartDate1",weekStartDate);
		 paramMap.put("weekEndDate1",weekEndDate);
		
		 paramMap.put("weekStartDate2",weekStartDate);
		 paramMap.put("weekEndDate2",weekEndDate);
		
		
		
		 List<ScheduleTimePlan> stp=new ArrayList<ScheduleTimePlan>();
			
		try {
			List<ScheduleTimePlan> scheduleTimePlans = findEntityList(sql, paramMap, ScheduleTimePlan.class);
			for (ScheduleTimePlan scheduleTimePlan : scheduleTimePlans) {
				if(isLastPlanForPersonalAndClass(scheduleTimePlan)){
					stp.add(scheduleTimePlan);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return stp;
	}

	@Override
	public List<ScheduleTimePlan> findLastClassesOfUsersNextWeekForClass(int firmId) {
		Date weekStartDate=DateTimeUtil.getNextWeekStartDate();
		Date weekEndDate=DateTimeUtil.getNextWeekEndDate();
		 
		 String sql="SELECT a.*,b.*,e.*,c.*,d.* "
				 + ",DATE_FORMAT(c.PLAN_START_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') PLAN_START_DATE_STR "
						 + " FROM user a,schedule_users_class_plan b,schedule_time_plan c,schedule_plan d, program_class e "
						 + " WHERE c.PLAN_START_DATE>=:weekStartDate1 "
						 + " and c.PLAN_START_DATE<:weekEndDate1 "
						 + " AND a.USER_ID=b.USER_ID "
						 + " AND b.SCHT_ID=c.SCHT_ID "
						 + " AND d.SCH_ID=c.SCH_ID "
						 + " AND e.PROG_ID=d.PROG_ID "
						 + " AND c.SCHT_ID IN (SELECT MAX(SCHT_ID) FROM schedule_time_plan f,schedule_plan g "
																 + " WHERE f.SCH_ID=g.SCH_ID "
																 + " AND  f.PLAN_START_DATE>=:weekStartDate2 "
																 + " AND  f.PLAN_START_DATE<:weekEndDate2 "
																 + " GROUP BY g.SCH_ID)";
		 
		 
		 
		 Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("weekStartDate1",weekStartDate);
		 paramMap.put("weekEndDate1",weekEndDate);
		
		 paramMap.put("weekStartDate2",weekStartDate);
		 paramMap.put("weekEndDate2",weekEndDate);
		
		
		
		 List<ScheduleTimePlan> stp=new ArrayList<ScheduleTimePlan>();
			
		try {
			List<ScheduleTimePlan> scheduleTimePlans = findEntityList(sql, paramMap, ScheduleTimePlan.class);
			for (ScheduleTimePlan scheduleTimePlan : scheduleTimePlans) {
				if(isLastPlanForPersonalAndClass(scheduleTimePlan)){
					stp.add(scheduleTimePlan);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return stp;
	}

	@Override
	public List<ScheduleMembershipPlan> findLastClassesOfUsersNextWeekForMembership(int firmId) {
		Date weekStartDate=DateTimeUtil.getNextWeekStartDate();
		Date weekEndDate=DateTimeUtil.getNextWeekEndDate();
		 
		 String sql="SELECT a.*,b.*,c.* "
				 + " ,DATE_FORMAT(b.SMP_END_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') SMP_END_DATE_STR "
						 + " FROM user a,schedule_membership_plan b,program_membership c "
						 + " WHERE b.SMP_END_DATE>=:weekStartDate1 "
						 + " and b.SMP_END_DATE<:weekEndDate1 "
						 + " AND a.USER_ID=b.USER_ID "
						 + " AND b.PROG_ID=c.PROG_ID ";
		 
		 
		 
		 Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("weekStartDate1",weekStartDate);
		 paramMap.put("weekEndDate1",weekEndDate);
		
		 
		
		
		try {
			List<ScheduleMembershipPlan> scheduleMembershipPlans = findEntityList(sql, paramMap, ScheduleMembershipPlan.class);
			return scheduleMembershipPlans;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	
	}

	

	

	
	
	

}
