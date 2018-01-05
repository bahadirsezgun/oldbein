package tr.com.abasus.ptboss.ptuser.iuser;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import tr.com.abasus.ptboss.facade.sale.SaleFacadeService;
import tr.com.abasus.ptboss.menu.entity.MenuLevel2;
import tr.com.abasus.ptboss.menu.entity.MenuTbl;
import tr.com.abasus.ptboss.menu.service.MenuService;
import tr.com.abasus.ptboss.ptuser.dao.ProcessUserDao;
import tr.com.abasus.ptboss.ptuser.entity.User;
import tr.com.abasus.ptboss.result.entity.HmiResultObj;
import tr.com.abasus.util.StatuTypes;
import tr.com.abasus.util.UserTypes;

@Component(value="processSuperManager")
@Scope("prototype")
public class ProcessSuperManager implements ProcessInterface {

	@Autowired
	MenuService menuService;

	@Autowired
	@Qualifier(value="saleClassFacade")
	SaleFacadeService saleFacadeService;
	
	@Autowired
	ProcessUserDao processUserDao;
	

	@Override
	public int getUserCompletePercent(User user) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<User> findAll(int firmId) {
		// TODO Auto-generated method stub
		return processUserDao.findAllToSuperManager(firmId);
	}

	@Override
	public List<User> findAllInChain(int firmId) {
		List<User> superManagers=processUserDao.findAllToSuperManager(firmId);
		return superManagers;
	}
	
	
	@Override
	public List<User> findByUserNameAndSurnameInChain(String userName, String userSurname, int firmId) {
		List<User> superManager=processUserDao.findByNameAndSurnameToSuperManager(userName, userSurname, firmId);
		return superManager;
	}
	
	@Override
	public HmiResultObj deleteUser(User user) {
		HmiResultObj hmiResultObj=new HmiResultObj();
		if(saleFacadeService.canStaffDelete(user.getUserId())){
			hmiResultObj=processUserDao.deleteUser(user);
		}else{
			user=processUserDao.findById(user.getUserId());
			user.setStaffStatu(StatuTypes.PASSIVE);
			hmiResultObj=processUserDao.updateUser(user);
		}
		
		return hmiResultObj;
	}

	
	@Override
	public HmiResultObj createUser(User user) {
		return processUserDao.createUser(user);
	}
	
	@Override
	public MenuTbl getMenuDashBoard() {
		return menuService.findDashBoardByUserType(UserTypes.USER_TYPE_SUPER_MANAGER_INT);
	}
	
	@Override
	public List<MenuTbl> getMenuSide() {
		List<MenuTbl> menuTbls=menuService.findUpperMenuByUserType(UserTypes.USER_TYPE_SUPER_MANAGER_INT);
		for (MenuTbl menuTbl : menuTbls) {
			List<MenuLevel2> menuLevel2s=menuService.findLevel2MenuByUserType(UserTypes.USER_TYPE_SUPER_MANAGER_INT, menuTbl.getMenuId());
			menuTbl.setMenuLevel2s(menuLevel2s);
		}
		
		return menuTbls;
		
	}

	@Override
	public List<MenuTbl> getMenuTop() {
		List<MenuTbl> menuTopTbls=menuService.findTopMenuByUserType(UserTypes.USER_TYPE_SUPER_MANAGER_INT);
		return menuTopTbls;
	}

	@Override
	public List<User> findByNameAndSurname(String userName, String userSurname,int firmId) {
		return processUserDao.findByNameAndSurnameToSuperManager(userName, userSurname,firmId);
	}

	public MenuService getMenuService() {
		return menuService;
	}

	public void setMenuService(MenuService menuService) {
		this.menuService = menuService;
	}

	public SaleFacadeService getSaleFacadeService() {
		return saleFacadeService;
	}

	public void setSaleFacadeService(SaleFacadeService saleFacadeService) {
		this.saleFacadeService = saleFacadeService;
	}

	public ProcessUserDao getProcessUserDao() {
		return processUserDao;
	}

	public void setProcessUserDao(ProcessUserDao processUserDao) {
		this.processUserDao = processUserDao;
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
	
}
