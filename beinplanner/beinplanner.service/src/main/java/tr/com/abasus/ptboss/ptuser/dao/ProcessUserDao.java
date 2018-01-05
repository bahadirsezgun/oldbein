package tr.com.abasus.ptboss.ptuser.dao;

import java.util.Date;
import java.util.List;

import tr.com.abasus.ptboss.ptuser.entity.StaffTracking;
import tr.com.abasus.ptboss.ptuser.entity.User;
import tr.com.abasus.ptboss.ptuser.entity.UserPotential;
import tr.com.abasus.ptboss.result.entity.HmiResultObj;

public interface ProcessUserDao {

	public User loginUserControl(String userName, String password);
	
	public User findAdmin();
	
	public User findAdminForReset();
	
	public User findUserByEMail(String email);
	
	public HmiResultObj createUser(User user);
	
	public HmiResultObj updateUser(User user);
	
	public HmiResultObj deleteUser(User user);

	public User findById(long userId);
	
	public List<User> findSpecialDates(int firmId);

	public List<User> findAllToMemberWithNoLimit(int firmId);
	
	/*******************************************************************************/
	/*****************************ADMIN*********************************************/
	/*******************************************************************************/
	
    public int getUserCompletePercentToAdmin(User user);
	
	public List<User> findAllToAdmin(int firmId);
	
	public List<User> findByNameAndSurnameToAdmin(String userName,String userSurname,int firmId);
	

	
	/*******************************************************************************/
	/*****************************MANAGER*******************************************/
	/*******************************************************************************/
	
    public int getUserCompletePercentToManager(User user);
	
	public List<User> findAllToManager(int firmId);
	
	public List<User> findByNameAndSurnameToManager(String userName,String userSurname,int firmId);
	

	/*******************************************************************************/
	/*****************************MEMBER**************************************/
	/*******************************************************************************/
	
    public int getUserCompletePercentToMember(User user);
	
	public List<User> findAllToMember(int firmId);
	
	public List<User> findByNameAndSurnameToMember(String userName,String userSurname,int firmId);
	

	/*******************************************************************************/
	/*****************************SCHEDULER STAFF*****************************/
	/*******************************************************************************/
	
    public int getUserCompletePercentToSchedulerStaff(User user);
	
	public List<User> findAllToSchedulerStaff(int firmId);
	
	public List<User> findAllToSchedulerStaffForCalendar(int firmId);
	
	
	public List<User> findByNameAndSurnameToSchedulerStaff(String userName,String userSurname,int firmId);
	
	
	
	
	/*******************************************************************************/
	/***************************** STAFF*****************************/
	/*******************************************************************************/
	
    public int getUserCompletePercentToStaff(User user);
	
	public List<User> findAllToStaff(int firmId);
	
	public List<User> findByNameAndSurnameToStaff(String userName,String userSurname,int firmId);
	
	
	/*******************************************************************************/
	/*****************************SUPER MANAGER*************************************/
	/*******************************************************************************/
	
    public int getUserCompletePercentToSuperManager(User user);
	
	public List<User> findAllToSuperManager(int firmId);
	
	public List<User> findByNameAndSurnameToSuperManager(String userName,String userSurname,int firmId);
	
	
	
	/*******************************************************************************/
	/*****************************POTENTIAL USER*************************************/
	/*******************************************************************************/
	
	
	public HmiResultObj createUserPotential(UserPotential userPotential);
	
	public HmiResultObj deleteUserPotential(UserPotential userPotential);
	
	public List<UserPotential> findAllUserPotentials();

	public List<UserPotential> findUserPotentials(int firmId);

	public List<UserPotential> findUserPotentialsByStatu(int[] status);

	public UserPotential findUserPotentialById(long userId);
	
	public List<UserPotential> findUserPotentialsByName(String name,String surname,int firmId);
	
	
	
	public int findTotalMemberInSystem(int firmId);
	
	
	/*******************************************************************************/
	/*****************************TRACKING STAFF *************************************/
	/*******************************************************************************/
	
	
   public HmiResultObj createStaffTracking(StaffTracking staffTracking);
	
   public HmiResultObj deleteStaffTracking(StaffTracking staffTracking);
	
   public List<StaffTracking> findStaffTracking(long userId,Date startDate,Date endDate,int firmId);
	
   
   
}
