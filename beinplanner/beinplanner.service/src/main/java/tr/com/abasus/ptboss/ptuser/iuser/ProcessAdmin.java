package tr.com.abasus.ptboss.ptuser.iuser;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import tr.com.abasus.ptboss.menu.entity.MenuLevel2;
import tr.com.abasus.ptboss.menu.entity.MenuTbl;
import tr.com.abasus.ptboss.menu.service.MenuService;
import tr.com.abasus.ptboss.ptuser.dao.ProcessUserDao;
import tr.com.abasus.ptboss.ptuser.entity.User;
import tr.com.abasus.ptboss.result.entity.HmiResultObj;
import tr.com.abasus.util.UserTypes;

@Component(value="processAdmin")
@Scope("prototype")
public class ProcessAdmin implements ProcessInterface {

	@Autowired
	MenuService menuService;

	@Autowired
	ProcessUserDao processUserDao;

	
	@Override
	public int getUserCompletePercent(User user) {
		return processUserDao.getUserCompletePercentToAdmin(user);
	}

	@Override
	public List<User> findAll(int firmId) {
		return processUserDao.findAllToAdmin(firmId);
	}
	
	@Override
	public List<User> findAllInChain(int firmId) {
		return null;
	}
	
	@Override
	public List<User> findByUserNameAndSurnameInChain(String userName, String userSurname, int firmId) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public HmiResultObj deleteUser(User user) {
		return null;
	}
	
	
	@Override
	public List<MenuTbl> getMenuSide() {
		List<MenuTbl> menuTbls=menuService.findUpperMenuByUserType(UserTypes.USER_TYPE_ADMIN_INT);
		for (MenuTbl menuTbl : menuTbls) {
			List<MenuLevel2> menuLevel2s=menuService.findLevel2MenuByUserType(UserTypes.USER_TYPE_ADMIN_INT, menuTbl.getMenuId());
			menuTbl.setMenuLevel2s(menuLevel2s);
		}
		
		return menuTbls;
		
	}

	@Override
	public List<MenuTbl> getMenuTop() {
		List<MenuTbl> menuTopTbls=menuService.findTopMenuByUserType(UserTypes.USER_TYPE_ADMIN_INT);
		return menuTopTbls;
	}

	@Override
	public List<User> findByNameAndSurname(String userName, String userSurname,int firmId) {
		// TODO Auto-generated method stub
		return null;
	}

	public MenuService getMenuService() {
		return menuService;
	}

	public void setMenuService(MenuService menuService) {
		this.menuService = menuService;
	}

	public ProcessUserDao getProcessUserDao() {
		return processUserDao;
	}

	public void setProcessUserDao(ProcessUserDao processUserDao) {
		this.processUserDao = processUserDao;
	}

	@Override
	public HmiResultObj createUser(User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<User> findByNameAndSaleProgramWithNoPlan(String userName, String userSurname, long progId, int type,int firmId,long schId) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public List<User> findByNameAndSaleProgramWithPlan(String userName, String userSurname, long progId, int type,int firmId,long schId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MenuTbl getMenuDashBoard() {
		return menuService.findDashBoardByUserType(UserTypes.USER_TYPE_ADMIN_INT);
	}

	
	

	

	
}
