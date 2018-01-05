package tr.com.abasus.ptboss.ptuser.service;

import java.util.Date;
import java.util.List;

import tr.com.abasus.ptboss.ptuser.entity.StaffTracking;
import tr.com.abasus.ptboss.ptuser.entity.User;
import tr.com.abasus.ptboss.ptuser.entity.UserPotential;
import tr.com.abasus.ptboss.result.entity.HmiResultObj;

public interface ProcessUserService  {

	public User loginUserControl(String userName, String password);
	
	public User findAdmin();
	
	public User findAdminForReset();
	
	
	public HmiResultObj updateUser(User user);
	
	public User findById(long userId);
	
	public User findUserByEMail(String email);
	
	public List<User> findAllToSchedulerStaffForCalendar(int firmId);
	
	public List<User> findAllToSchedulerStaff(int firmId);

	public List<User> findSpecialDates(int firmId);

	
	public HmiResultObj createUserPotential(UserPotential userPotential);
	
	public HmiResultObj deleteUserPotential(UserPotential userPotential);
	
	public List<UserPotential> findUserPotentials(int firmId);

	public List<UserPotential> findUserPotentialsByStatu(int[] status);

	public List<UserPotential> findAllUserPotentials();
	
	public List<UserPotential> findUserPotentialsByName(String name,String surname,int firmId);
	
	public UserPotential findUserPotentialById(long userId);
	
	
	public int findTotalMemberInSystem(int firmId);
	
	
	public List<User> findAllToMemberWithNoLimit(int firmId);
	
	/*******************************************************************************/
	/*****************************TRACKING STAFF *************************************/
	/*******************************************************************************/
	
	
   public HmiResultObj createStaffTracking(StaffTracking staffTracking);
	
   public HmiResultObj deleteStaffTracking(StaffTracking staffTracking);
	
   public List<StaffTracking> findStaffTracking(long userId,Date startDate,Date endDate,int firmId); 
	
  
}
