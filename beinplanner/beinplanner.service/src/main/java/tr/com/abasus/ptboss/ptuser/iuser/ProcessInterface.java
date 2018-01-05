package tr.com.abasus.ptboss.ptuser.iuser;

import java.util.List;

import tr.com.abasus.ptboss.menu.entity.MenuTbl;
import tr.com.abasus.ptboss.ptuser.entity.User;
import tr.com.abasus.ptboss.result.entity.HmiResultObj;

public interface ProcessInterface {

	
	public int getUserCompletePercent(User user);
	
	public List<User> findAll(int firmId);
	
	public List<User> findAllInChain(int firmId);
	
	public List<User> findByUserNameAndSurnameInChain(String userName,String userSurname,int firmId);
	
	public List<User> findByNameAndSurname(String userName,String userSurname,int firmId);
	
	public List<User> findByNameAndSaleProgramWithNoPlan(String userName,String userSurname,long progId,int type,int firmId,long schId);
	
	public List<User> findByNameAndSaleProgramWithPlan(String userName,String userSurname,long progId,int type,int firmId,long schId);
	
	public MenuTbl getMenuDashBoard();
		
	public List<MenuTbl> getMenuSide();
	
	public List<MenuTbl> getMenuTop();
	
	public HmiResultObj deleteUser(User user);

	public HmiResultObj createUser(User user);

	
	
}
