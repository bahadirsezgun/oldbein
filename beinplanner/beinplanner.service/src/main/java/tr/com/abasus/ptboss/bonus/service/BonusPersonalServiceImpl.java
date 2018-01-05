package tr.com.abasus.ptboss.bonus.service;

import java.util.Date;
import java.util.List;

import tr.com.abasus.ptboss.bonus.dao.BonusPersonalDao;
import tr.com.abasus.ptboss.bonus.entity.UserBonusPaymentFactory;
import tr.com.abasus.ptboss.bonus.entity.UserBonusPaymentPersonal;
import tr.com.abasus.ptboss.ptuser.entity.User;
import tr.com.abasus.ptboss.result.entity.HmiResultObj;
import tr.com.abasus.ptboss.schedule.entity.ScheduleTimePlan;

public class BonusPersonalServiceImpl implements BonusPersonalService {

	BonusPersonalDao bonusPersonalDao;

	
	
	

	

	@Override
	public List<UserBonusPaymentPersonal> findUserOfMonth(long userId, int month,int year) {
		return bonusPersonalDao.findUserOfMonth(userId, month,year);
	}

	@Override
	public List<UserBonusPaymentPersonal> findUserOfYear(long userId, int year) {
		return bonusPersonalDao.findUserOfYear(userId, year);
	}

	@Override
	public HmiResultObj createBonusPaymentForUser(UserBonusPaymentPersonal userBonusPayment) {
		return bonusPersonalDao.createBonusPaymentForUser(userBonusPayment);
	}

	@Override
	public HmiResultObj deleteBonusPaymentForUser(UserBonusPaymentPersonal userBonusPayment) {
		return bonusPersonalDao.deleteBonusPaymentForUser(userBonusPayment);
	}

	public BonusPersonalDao getBonusPersonalDao() {
		return bonusPersonalDao;
	}

	public void setBonusPersonalDao(BonusPersonalDao bonusPersonalDao) {
		this.bonusPersonalDao = bonusPersonalDao;
	}

	@Override
	public List<ScheduleTimePlan> findScheduleTimePlansByDatesForStaff(long schStaffId, Date startDate, Date endDate) {
		return bonusPersonalDao.findScheduleTimePlansByDatesForStaff(schStaffId, startDate, endDate);
	}

	@Override
	public User findStaffBonusType(long staffId) {
		return bonusPersonalDao.findStaffBonusType(staffId);
	}

	@Override
	public List<UserBonusPaymentPersonal> findUserBonusPaymentByDate(long userId, Date startDate, Date endDate) {
		return bonusPersonalDao.findUserBonusPaymentByDate(userId, startDate, endDate);
	}

	@Override
	public List<UserBonusPaymentFactory> findUserBonusPaymentFactoryByMonth(int month, int year, int firmId) {
		return bonusPersonalDao.findUserBonusPaymentFactoryByMonth(month, year, firmId);
	}

	@Override
	public List<UserBonusPaymentFactory> findBonusPaymentForToday(int firmId) {
		return bonusPersonalDao.findBonusPaymentForToday(firmId);
	}
	
	
}
