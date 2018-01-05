package tr.com.abasus.ptboss.ptuser.service;

import java.util.Date;
import java.util.List;

import tr.com.abasus.ptboss.ptuser.dao.ProcessUserDao;
import tr.com.abasus.ptboss.ptuser.entity.StaffTracking;
import tr.com.abasus.ptboss.ptuser.entity.User;
import tr.com.abasus.ptboss.ptuser.entity.UserPotential;
import tr.com.abasus.ptboss.result.entity.HmiResultObj;

public class ProcessUserServiceImpl implements ProcessUserService {

	ProcessUserDao processUserDao;
	
	public ProcessUserDao getProcessUserDao() {
		return processUserDao;
	}

	public void setProcessUserDao(ProcessUserDao processUserDao) {
		this.processUserDao = processUserDao;
	}
	@Override
	public User loginUserControl(String userName, String password) {
		return processUserDao.loginUserControl(userName, password);
	}

	

	@Override
	public synchronized HmiResultObj updateUser(User user) {
		return processUserDao.updateUser(user);
	}

	

	@Override
	public User findById(long userId) {
		return processUserDao.findById(userId);
	}

	@Override
	public User findUserByEMail(String email) {
		return processUserDao.findUserByEMail(email);
	}

	@Override
	public List<User> findAllToSchedulerStaff(int firmId) {
		return processUserDao.findAllToSchedulerStaff(firmId);
	}
	
	@Override
	public List<User> findAllToSchedulerStaffForCalendar(int firmId) {
		return processUserDao.findAllToSchedulerStaffForCalendar(firmId);
	}

	@Override
	public HmiResultObj createUserPotential(UserPotential userPotential) {
		return processUserDao.createUserPotential(userPotential);
	}

	@Override
	public HmiResultObj deleteUserPotential(UserPotential userPotential) {
		return processUserDao.deleteUserPotential(userPotential);
	}

	@Override
	public List<UserPotential> findUserPotentials(int firmId) {
		return processUserDao.findUserPotentials(firmId);
	}

	@Override
	public UserPotential findUserPotentialById(long userId) {
		return processUserDao.findUserPotentialById(userId);
	}

	@Override
	public List<UserPotential> findUserPotentialsByStatu(int[] status) {
		return processUserDao.findUserPotentialsByStatu(status);
	}

	@Override
	public List<UserPotential> findAllUserPotentials() {
		return processUserDao.findAllUserPotentials();
	}

	@Override
	public List<UserPotential> findUserPotentialsByName(String name, String surname, int firmId) {
		return processUserDao.findUserPotentialsByName(name, surname, firmId);
	}

	@Override
	public List<User> findSpecialDates(int firmId) {
		return processUserDao.findSpecialDates(firmId);
	}

	@Override
	public User findAdmin() {
		return processUserDao.findAdmin();
	}

	@Override
	public User findAdminForReset() {
		return processUserDao.findAdminForReset();
	}

	@Override
	public int findTotalMemberInSystem(int firmId) {
		return processUserDao.findTotalMemberInSystem(firmId);
	}

	@Override
	public List<User> findAllToMemberWithNoLimit(int firmId) {
		return processUserDao.findAllToMemberWithNoLimit(firmId);
	}

	@Override
	public HmiResultObj createStaffTracking(StaffTracking staffTracking) {
		return processUserDao.createStaffTracking(staffTracking);
	}

	@Override
	public HmiResultObj deleteStaffTracking(StaffTracking staffTracking) {
		return processUserDao.deleteStaffTracking(staffTracking);
	}

	
	
	@Override
	public List<StaffTracking> findStaffTracking(long userId, Date startDate, Date endDate,int firmId) {
		
		
		
		return processUserDao.findStaffTracking(userId, startDate, endDate,firmId);
	}

	

	

	
	

	
}
