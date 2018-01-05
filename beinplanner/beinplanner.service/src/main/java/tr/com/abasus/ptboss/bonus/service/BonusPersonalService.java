package tr.com.abasus.ptboss.bonus.service;

import java.util.Date;
import java.util.List;

import tr.com.abasus.ptboss.bonus.entity.UserBonusPaymentFactory;
import tr.com.abasus.ptboss.bonus.entity.UserBonusPaymentPersonal;
import tr.com.abasus.ptboss.definition.entity.DefBonus;
import tr.com.abasus.ptboss.ptuser.entity.User;
import tr.com.abasus.ptboss.result.entity.HmiResultObj;
import tr.com.abasus.ptboss.schedule.entity.ScheduleTimePlan;

public interface BonusPersonalService {

	
	
	public List<UserBonusPaymentPersonal> findUserOfMonth(long userId,int month,int year);
	
	public List<UserBonusPaymentPersonal> findUserBonusPaymentByDate(long userId,Date startDate,Date endDate);
	
	public List<UserBonusPaymentFactory> findUserBonusPaymentFactoryByMonth(int month,int year,int firmId );
	
	
	public List<UserBonusPaymentPersonal> findUserOfYear(long userId,int year);
	
	public HmiResultObj createBonusPaymentForUser(UserBonusPaymentPersonal userBonusPayment);
	
	public HmiResultObj deleteBonusPaymentForUser(UserBonusPaymentPersonal userBonusPayment);
	
	/**************************************************************************************/
	
	
	public List<ScheduleTimePlan> findScheduleTimePlansByDatesForStaff(long schStaffId,Date startDate,Date endDate);
	
	public User findStaffBonusType(long staffId);
	
	
	public List<UserBonusPaymentFactory> findBonusPaymentForToday(int firmId);
	
	
}
