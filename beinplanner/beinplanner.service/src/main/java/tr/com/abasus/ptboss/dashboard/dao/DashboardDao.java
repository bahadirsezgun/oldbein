package tr.com.abasus.ptboss.dashboard.dao;

import java.util.List;

import tr.com.abasus.ptboss.dashboard.entity.ActiveMember;
import tr.com.abasus.ptboss.dashboard.entity.LeftPaymentInfo;
import tr.com.abasus.ptboss.dashboard.entity.PlannedClassInfo;
import tr.com.abasus.ptboss.dashboard.entity.TodayPayment;
import tr.com.abasus.ptboss.packetsale.entity.PacketSaleFactory;
import tr.com.abasus.ptboss.schedule.entity.ScheduleMembershipPlan;
import tr.com.abasus.ptboss.schedule.entity.ScheduleTimePlan;

public interface DashboardDao {

	
    public ActiveMember getActiveMembersInPersonal(int firmId);
    public ActiveMember getActiveMembersInClass(int firmId);
    public ActiveMember getActiveMembersInMembership(int firmId);
	
	
	public LeftPaymentInfo getLeftPaymentInfoInPersonal(int firmId);
	public LeftPaymentInfo getLeftPaymentInfoInClass(int firmId);
	public LeftPaymentInfo getLeftPaymentInfoInMembership(int firmId);
	
	public LeftPaymentInfo getNoPaymentInfoInPersonal(int firmId);
	public LeftPaymentInfo getNoPaymentInfoInClass(int firmId);
	public LeftPaymentInfo getNoPaymentInfoInMembership(int firmId);
	
	
	
	public List<PacketSaleFactory> getLeftPaymentSaleInPersonal(int firmId);
	public List<PacketSaleFactory> getLeftPaymentSaleInClass(int firmId);
	public List<PacketSaleFactory> getLeftPaymentSaleInMembership(int firmId);
	
	public List<PacketSaleFactory> getNoPaymentSaleInPersonal(int firmId);
	public List<PacketSaleFactory> getNoPaymentSaleInClass(int firmId);
	public List<PacketSaleFactory> getNoPaymentSaleInMembership(int firmId);
	
	
	public TodayPayment geTodayPayment();
	
	
	public PlannedClassInfo getPlannedClassInfoForPersonalAndClass(int firmId,int year,int month);
	public PlannedClassInfo getPlannedClassInfoForMembership(int firmId,int year,int month);
	
	
	public List<ScheduleTimePlan> findLastClassesOfUsersThisWeekForPersonal(int firmId);
	public List<ScheduleTimePlan> findLastClassesOfUsersThisWeekForClass(int firmId);
	public List<ScheduleMembershipPlan> findLastClassesOfUsersThisWeekForMembership(int firmId);
	
	public List<ScheduleTimePlan> findLastClassesOfUsersNextWeekForPersonal(int firmId);
	public List<ScheduleTimePlan> findLastClassesOfUsersNextWeekForClass(int firmId);
	public List<ScheduleMembershipPlan> findLastClassesOfUsersNextWeekForMembership(int firmId);
	
}
