package tr.com.abasus.ptboss.bonus.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import tr.com.abasus.ptboss.bonus.dao.BonusClassDao;
import tr.com.abasus.ptboss.bonus.entity.UserBonusPaymentClass;
import tr.com.abasus.ptboss.bonus.entity.UserBonusPaymentFactory;
import tr.com.abasus.ptboss.definition.entity.DefBonus;
import tr.com.abasus.ptboss.ptuser.entity.User;
import tr.com.abasus.ptboss.result.entity.HmiResultObj;
import tr.com.abasus.ptboss.schedule.entity.ScheduleTimePlan;

public class BonusClassServiceImpl implements BonusClassService{

	BonusClassDao bonusClassDao;

	public BonusClassDao getBonusClassDao() {
		return bonusClassDao;
	}

	public void setBonusClassDao(BonusClassDao bonusClassDao) {
		this.bonusClassDao = bonusClassDao;
	}

	@Override
	public List<ScheduleTimePlan> findScheduleTimePlansByDatesForStaff(long schStaffId, Date startDate, Date endDate) {
		return bonusClassDao.findScheduleTimePlansByDatesForStaff(schStaffId, startDate, endDate);
	}
	
	@Override
	public User findStaffBonusType(long staffId) {
		return bonusClassDao.findStaffBonusType(staffId);
	}

	@Override
	public List<UserBonusPaymentClass> findUserBonusPaymentByDate(long userId, Date startDate, Date endDate) {
		return bonusClassDao.findUserBonusPaymentByDate(userId, startDate, endDate);
	}

	@Override
	public double findTotalOfMonth(int firmId, int month, int year) {
		List<User> users= new ArrayList<User>();// processSchedulerStaff.findAll(firmId);
		double totalPayment=0;
		for (User user : users) {
			List<UserBonusPaymentClass> userBonusPayments=bonusClassDao.findUserOfMonth(user.getUserId(), month,year);
			
			for (UserBonusPaymentClass userBonusPayment : userBonusPayments) {
				totalPayment+=userBonusPayment.getBonAmount();
			}
			
			
		}
		
		UserBonusPaymentClass userBonusPayment=new UserBonusPaymentClass();
		userBonusPayment.setBonMonth(month);
		userBonusPayment.setBonAmount(totalPayment);
		
		
		return totalPayment;
	}

	@Override
	public List<UserBonusPaymentClass> findUserOfMonth(long userId, int month, int year) {
		return bonusClassDao.findUserOfMonth(userId, month, year);
	}

	@Override
	public List<UserBonusPaymentClass> findUserOfYear(long userId, int year) {
		return bonusClassDao.findUserOfYear(userId, year);
	}

	@Override
	public HmiResultObj createBonusPaymentForUser(UserBonusPaymentClass userBonusPayment) {
		return bonusClassDao.createBonusPaymentForUser(userBonusPayment);
	}

	@Override
	public HmiResultObj deleteBonusPaymentForUser(UserBonusPaymentClass userBonusPayment) {
		return bonusClassDao.deleteBonusPaymentForUser(userBonusPayment);
	}

	@Override
	public List<UserBonusPaymentFactory> findUserBonusPaymentFactoryByMonth(int month, int year, int firmId) {
		return bonusClassDao.findUserBonusPaymentFactoryByMonth(month, year, firmId);
	}

	@Override
	public List<UserBonusPaymentFactory> findBonusPaymentForToday(int firmId) {
		return bonusClassDao.findBonusPaymentForToday(firmId);
	}
	
	
}
