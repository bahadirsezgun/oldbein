package tr.com.abasus.ptboss.bonus.service;

import java.util.Date;
import java.util.List;

import tr.com.abasus.ptboss.bonus.entity.UserBonusPaymentClass;
import tr.com.abasus.ptboss.bonus.entity.UserBonusPaymentFactory;
import tr.com.abasus.ptboss.ptuser.entity.User;
import tr.com.abasus.ptboss.result.entity.HmiResultObj;
import tr.com.abasus.ptboss.schedule.entity.ScheduleTimePlan;

public interface BonusClassService {

	
	
    public double findTotalOfMonth(int firmId,int month,int year);
	
	public List<UserBonusPaymentClass> findUserOfMonth(long userId,int month,int year);
	
	public List<UserBonusPaymentClass> findUserBonusPaymentByDate(long userId,Date startDate,Date endDate);
	
	public List<UserBonusPaymentFactory> findUserBonusPaymentFactoryByMonth(int month,int year,int firmId );
	
	
	public List<UserBonusPaymentClass> findUserOfYear(long userId,int year);
	
	public HmiResultObj createBonusPaymentForUser(UserBonusPaymentClass userBonusPayment);
	
	public HmiResultObj deleteBonusPaymentForUser(UserBonusPaymentClass userBonusPayment);
	
	
	
	
	public List<ScheduleTimePlan> findScheduleTimePlansByDatesForStaff(long schStaffId,Date startDate,Date endDate);

	public User findStaffBonusType(long staffId);
	
	public List<UserBonusPaymentFactory> findBonusPaymentForToday(int firmId);
	
}
