package tr.com.abasus.ptboss.dashboard.service;

import java.util.List;

import tr.com.abasus.ptboss.dashboard.entity.ActiveMember;
import tr.com.abasus.ptboss.dashboard.entity.LeftPaymentInfo;
import tr.com.abasus.ptboss.dashboard.entity.PlannedClassInfo;
import tr.com.abasus.ptboss.dashboard.entity.TodayPayment;
import tr.com.abasus.ptboss.packetsale.entity.PacketSaleFactory;
import tr.com.abasus.ptboss.ptuser.entity.User;
import tr.com.abasus.ptboss.schedule.entity.ScheduleMembershipPlan;
import tr.com.abasus.ptboss.schedule.entity.ScheduleTimePlan;

public interface DashboardService {

	public ActiveMember getActiveMembers(int firmId);
	
	public LeftPaymentInfo getLeftPaymentInfo(int firmId);
	
	public TodayPayment geTodayPayment();
	
	public List<PacketSaleFactory> getLeftPaymentSale(int firmId);
	
	public List<PlannedClassInfo> getPlannedClassInfo(int firmId,int year);
	
	public List<ScheduleTimePlan> findLastClassesOfUsersThisWeek(int firmId);
	
	public List<ScheduleTimePlan> findLastClassesOfUsersThisNextWeek(int firmId);
	
	public List<ScheduleMembershipPlan> findLastMembershipsOfUsersThisWeek(int firmId);
	
	public List<ScheduleMembershipPlan> findLastMembershipsOfUsersThisNextWeek(int firmId);
	
	
}
