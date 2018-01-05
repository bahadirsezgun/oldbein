package tr.com.abasus.ptboss.dashboard.service;

import java.util.ArrayList;
import java.util.List;

import tr.com.abasus.ptboss.dashboard.dao.DashboardDao;
import tr.com.abasus.ptboss.dashboard.entity.ActiveMember;
import tr.com.abasus.ptboss.dashboard.entity.LeftPaymentInfo;
import tr.com.abasus.ptboss.dashboard.entity.PlannedClassInfo;
import tr.com.abasus.ptboss.dashboard.entity.TodayPayment;
import tr.com.abasus.ptboss.packetsale.entity.PacketSaleFactory;
import tr.com.abasus.ptboss.schedule.entity.ScheduleMembershipPlan;
import tr.com.abasus.ptboss.schedule.entity.ScheduleTimePlan;

public class DashboardServiceImpl implements DashboardService {

	DashboardDao dashboardDao;
	
	
	@Override
	public ActiveMember getActiveMembers(int firmId) {
		
		ActiveMember amP=dashboardDao.getActiveMembersInPersonal(firmId);
		ActiveMember amC=dashboardDao.getActiveMembersInClass(firmId);
		ActiveMember amM=dashboardDao.getActiveMembersInMembership(firmId);
		
		ActiveMember activeMember=new ActiveMember();
		activeMember.setActiveMemberCount(amP.getActiveMemberCount()+amC.getActiveMemberCount()+amM.getActiveMemberCount());
		activeMember.setActiveMemberCCount(amC.getActiveMemberCount());
		activeMember.setActiveMemberMCount(amM.getActiveMemberCount());
		activeMember.setActiveMemberPCount(amP.getActiveMemberCount());
		return activeMember;
	}

	

	@Override
	public TodayPayment geTodayPayment() {
		return dashboardDao.geTodayPayment();
	}

	public DashboardDao getDashboardDao() {
		return dashboardDao;
	}

	public void setDashboardDao(DashboardDao dashboardDao) {
		this.dashboardDao = dashboardDao;
	}



	@Override
	public LeftPaymentInfo getLeftPaymentInfo(int firmId) {
		
		LeftPaymentInfo lpiP=dashboardDao.getLeftPaymentInfoInPersonal(firmId);
		LeftPaymentInfo lpiC=dashboardDao.getLeftPaymentInfoInClass(firmId);
		LeftPaymentInfo lpiM=dashboardDao.getLeftPaymentInfoInMembership(firmId);
		
		LeftPaymentInfo lpiPN=dashboardDao.getNoPaymentInfoInPersonal(firmId);
		LeftPaymentInfo lpiCN=dashboardDao.getNoPaymentInfoInClass(firmId);
		LeftPaymentInfo lpiMN=dashboardDao.getNoPaymentInfoInMembership(firmId);
		
		
		
		LeftPaymentInfo lpiT=new LeftPaymentInfo();
	
		lpiT.setLeftPayment(lpiP.getLeftPayment()+lpiC.getLeftPayment()+lpiM.getLeftPayment());
		lpiT.setLeftPaymentCount(lpiP.getLeftPaymentCount()+lpiC.getLeftPaymentCount()+lpiM.getLeftPaymentCount());
		
		lpiT.setNoPayment(lpiPN.getNoPayment()+lpiCN.getNoPayment()+lpiMN.getNoPayment());
		lpiT.setNoPaymentCount(lpiPN.getNoPaymentCount()+lpiCN.getNoPaymentCount()+lpiMN.getNoPaymentCount());
		
		
		return lpiT;
	}



	@Override
	public List<PacketSaleFactory> getLeftPaymentSale(int firmId) {
		
		List<PacketSaleFactory> ppfP=dashboardDao.getLeftPaymentSaleInPersonal(firmId);
		List<PacketSaleFactory> ppfC=dashboardDao.getLeftPaymentSaleInClass(firmId);
		List<PacketSaleFactory> ppfM=dashboardDao.getLeftPaymentSaleInMembership(firmId);
		
		List<PacketSaleFactory> ppfPN=dashboardDao.getNoPaymentSaleInPersonal(firmId);
		List<PacketSaleFactory> ppfCN=dashboardDao.getNoPaymentSaleInClass(firmId);
		List<PacketSaleFactory> ppfMN=dashboardDao.getNoPaymentSaleInMembership(firmId);
		
		
		
		
		List<PacketSaleFactory> ppf=new ArrayList<PacketSaleFactory>();
		ppf.addAll(ppfP);
		ppf.addAll(ppfC);
		ppf.addAll(ppfM);
		
		ppf.addAll(ppfPN);
		ppf.addAll(ppfCN);
		ppf.addAll(ppfMN);
		return ppf;
	}



	@Override
	public List<PlannedClassInfo> getPlannedClassInfo(int firmId, int year) {
		List<PlannedClassInfo> plannedClassInfos=new ArrayList<PlannedClassInfo>();
		
		for(int i=1;i<13;i++){
			PlannedClassInfo plannedClassInfoPC=dashboardDao.getPlannedClassInfoForPersonalAndClass(firmId, year, i);
			PlannedClassInfo plannedClassInfoM=dashboardDao.getPlannedClassInfoForMembership(firmId, year, i);
			
			PlannedClassInfo plannedClassInfo=new PlannedClassInfo();
			plannedClassInfo.setClassCount(plannedClassInfoPC.getClassCount()+plannedClassInfoM.getClassCount());
			plannedClassInfo.setMonth(i);
			plannedClassInfo.setMonthName(plannedClassInfoPC.getMonthName());
			plannedClassInfos.add(plannedClassInfo);
		}
		return plannedClassInfos;
	}



	@Override
	public List<ScheduleTimePlan> findLastClassesOfUsersThisWeek(int firmId) {
		
		List<ScheduleTimePlan> scheduleTimePlans=dashboardDao.findLastClassesOfUsersThisWeekForPersonal(firmId);
		List<ScheduleTimePlan> scheduleTimePlanCs=dashboardDao.findLastClassesOfUsersThisWeekForClass(firmId);
		scheduleTimePlans.addAll(scheduleTimePlanCs);
		return scheduleTimePlans;
	}



	@Override
	public List<ScheduleTimePlan> findLastClassesOfUsersThisNextWeek(int firmId) {
		List<ScheduleTimePlan> scheduleTimePlans=dashboardDao.findLastClassesOfUsersNextWeekForPersonal(firmId);
		List<ScheduleTimePlan> scheduleTimePlanCs=dashboardDao.findLastClassesOfUsersNextWeekForClass(firmId);
		scheduleTimePlans.addAll(scheduleTimePlanCs);
		return scheduleTimePlans;
	}



	@Override
	public List<ScheduleMembershipPlan> findLastMembershipsOfUsersThisWeek(int firmId) {
		return dashboardDao.findLastClassesOfUsersThisWeekForMembership(firmId);
	}



	@Override
	public List<ScheduleMembershipPlan> findLastMembershipsOfUsersThisNextWeek(int firmId) {
		return dashboardDao.findLastClassesOfUsersNextWeekForMembership(firmId);
	}

	
	
}
