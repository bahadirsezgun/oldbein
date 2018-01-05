package tr.com.abasus.ptboss.facade.dao;

import java.util.Date;

import tr.com.abasus.ptboss.schedule.entity.ScheduleTimePlan;

public interface SchedulerFacadeDao {

	public boolean haveMemberGotPersonalSchedulerForSale(long saleId);
	
	public boolean haveMemberGotClassSchedulerForSale(long saleId);
	
	
	public boolean haveInstructorGotClassesInThisTime(long staffId,Date planStartDate,Date planEndDate,long schtId);
	
	public boolean isStudioReservedInThisTime(int studioId,Date planStartDate,Date planEndDate,long schtId);
	
	
	public boolean isStudioUsedBefore(int studioId);
	
	public ScheduleTimePlan findFirstClassOfPlan(long schId);
	
	
	/*
	public boolean isBonusPayedForThisTimePlan(long schtId);
	
	public boolean isBonusPayedForThisScheduledUserClassPlan(long sucpId);
	
	public boolean isBonusPayedForThisScheduledUserPersonalPlan(long suppId);
	*/
}
