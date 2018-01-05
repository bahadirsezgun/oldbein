package tr.com.abasus.ptboss.ptuser.iuser;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import tr.com.abasus.ptboss.facade.rulefilter.RuleFilterService;
import tr.com.abasus.ptboss.facade.sale.SaleFacadeService;
import tr.com.abasus.ptboss.menu.entity.MenuLevel2;
import tr.com.abasus.ptboss.menu.entity.MenuTbl;
import tr.com.abasus.ptboss.menu.service.MenuService;
import tr.com.abasus.ptboss.ptuser.dao.ProcessUserDao;
import tr.com.abasus.ptboss.ptuser.entity.User;
import tr.com.abasus.ptboss.result.entity.HmiResultObj;
import tr.com.abasus.ptboss.settings.entity.PtRestrictions;
import tr.com.abasus.ptboss.settings.service.SettingService;
import tr.com.abasus.util.ResultStatuObj;
import tr.com.abasus.util.StatuTypes;
import tr.com.abasus.util.UserTypes;

@Component(value="processSchedulerStaff")
@Scope("prototype")
public class ProcessSchedulerStaff  implements ProcessInterface {

	@Autowired
	ProcessUserDao processUserDao;
	
	
	@Autowired
	SettingService settingService;
	
	
	@Autowired
	@Qualifier(value="processStaff")
	ProcessInterface processInterface;
	
	@Autowired
	MenuService menuService;


	@Autowired
	@Qualifier(value="saleClassFacade")
	SaleFacadeService saleFacadeService;
	
	@Autowired
	@Qualifier(value="newTeacherCreateFilter")
	RuleFilterService ruleFilterService;
	
	@Override
	public int getUserCompletePercent(User user) {
		// TODO Auto-generated method stub
		return 0;
	}

	
	
	@Override
	public List<User> findAll(int firmId) {
		return processUserDao.findAllToSchedulerStaff(firmId);
	}
	
	public List<User> findAllToSchedulerStaffForCalendar(int firmId) {
		return processUserDao.findAllToSchedulerStaffForCalendar(firmId);
	}
	
	@Override
	public List<User> findAllInChain(int firmId) {
		List<User> schedulerStaff=processUserDao.findAllToSchedulerStaff(firmId);
		List<User> chainUsers=processInterface.findAllInChain(firmId);
		if(schedulerStaff.size()>0)
			chainUsers.addAll(schedulerStaff);
		return chainUsers;
	}
	
	@Override
	public List<User> findByUserNameAndSurnameInChain(String userName, String userSurname, int firmId) {
		List<User> schedulerStaffs=processUserDao.findByNameAndSurnameToSchedulerStaff(userName, userSurname, firmId);
		List<User> chainUsers=processInterface.findByUserNameAndSurnameInChain(userName, userSurname, firmId);
		if(schedulerStaffs.size()>0)
			chainUsers.addAll(schedulerStaffs);
		return chainUsers;
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
		HmiResultObj hmiResultObj=new HmiResultObj();
		if(ruleFilterService.canDo(user.getFirmId())){
			hmiResultObj=processUserDao.createUser(user);
		}else{
			hmiResultObj.setResultMessage("unProperLicenceType");
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
		}
		
		return hmiResultObj;
	}

	
	@Override
	public MenuTbl getMenuDashBoard() {
		return menuService.findDashBoardByUserType(UserTypes.USER_TYPE_SCHEDULAR_STAFF_INT);
	}
	
	@Override
	public List<MenuTbl> getMenuSide() {
		List<MenuTbl> menuTbls=menuService.findUpperMenuByUserType(UserTypes.USER_TYPE_SCHEDULAR_STAFF_INT);
		for (MenuTbl menuTbl : menuTbls) {
			List<MenuLevel2> menuLevel2s=menuService.findLevel2MenuByUserType(UserTypes.USER_TYPE_SCHEDULAR_STAFF_INT, menuTbl.getMenuId());
			menuTbl.setMenuLevel2s(menuLevel2s);
		}
		
		return menuTbls;
		
	}

	@Override
	public List<MenuTbl> getMenuTop() {
		List<MenuTbl> menuTopTbls=menuService.findTopMenuByUserType(UserTypes.USER_TYPE_SCHEDULAR_STAFF_INT);
		return menuTopTbls;
	}

	@Override
	public List<User> findByNameAndSurname(String userName, String userSurname,int firmId) {
		return processUserDao.findByNameAndSurnameToSchedulerStaff(userName, userSurname,firmId);
	}

	public ProcessUserDao getProcessUserDao() {
		return processUserDao;
	}

	public void setProcessUserDao(ProcessUserDao processUserDao) {
		this.processUserDao = processUserDao;
	}

	public ProcessInterface getProcessInterface() {
		return processInterface;
	}

	public void setProcessInterface(ProcessInterface processInterface) {
		this.processInterface = processInterface;
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
