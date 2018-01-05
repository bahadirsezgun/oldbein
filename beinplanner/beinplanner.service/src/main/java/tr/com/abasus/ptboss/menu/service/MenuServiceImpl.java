package tr.com.abasus.ptboss.menu.service;

import java.util.List;

import tr.com.abasus.ptboss.menu.dao.MenuDao;
import tr.com.abasus.ptboss.menu.entity.MenuLevel2;
import tr.com.abasus.ptboss.menu.entity.MenuRoleTbl;
import tr.com.abasus.ptboss.menu.entity.MenuTbl;
import tr.com.abasus.ptboss.result.entity.HmiResultObj;
import tr.com.abasus.util.UserTypes;

public class MenuServiceImpl implements MenuService{

	MenuDao menuDao;
	
	@Override
	public List<MenuTbl> findUpperMenuByUserType(int userType) {
		return menuDao.findUpperMenuByUserType(userType);
	}
	
	@Override
	public List<MenuLevel2> findLevel2MenuByUserType(int userType, int upperMenu) {
		return menuDao.findLevel2MenuByUserType(userType, upperMenu);
	}

	@Override
	public List<MenuTbl> findTopMenuByUserType(int userType) {
		return menuDao.findTopMenuByUserType(userType);
	}

	@Override
	public List<MenuTbl> findUserAuthorizedMenus(int authUserType){
		
		
		
		List<MenuTbl> menuAllTbls=menuDao.findAllUpperMenuForRoleMenu();
		for (MenuTbl menuAllTbl : menuAllTbls) {
			List<MenuLevel2> menuLevel2s=menuDao.findLevel2MenuByUserType(UserTypes.getUserTypeInt(UserTypes.USER_TYPE_ADMIN), menuAllTbl.getMenuId());
			menuAllTbl.setMenuLevel2s(menuLevel2s);
		}
		
		List<MenuTbl> menuAuthTbls=menuDao.findUpperMenuByUserType(authUserType);
		
		
		for (MenuTbl menuTbl : menuAllTbls) {
			menuTbl.setAuthority(0);
			for (MenuTbl menuAuthTbl : menuAuthTbls) {
				if(menuAuthTbl.getMenuId()==menuTbl.getMenuId()){
					menuTbl.setAuthority(1);
					break;
				}
			}
			if(menuTbl.getAuthority()==1){
				List<MenuLevel2> menuAuthLevelTbls=menuDao.findLevel2MenuByUserType(authUserType, menuTbl.getMenuId());
				List<MenuLevel2> menuAllLevel2s=menuTbl.getMenuLevel2s();
				for (MenuLevel2 menuAllLevel2 : menuAllLevel2s) {
					menuAllLevel2.setAuthority(0);
					for (MenuLevel2 menuAuthLevel2 : menuAuthLevelTbls) {
						if(menuAllLevel2.getMenuId()==menuAuthLevel2.getMenuId()){
							menuAllLevel2.setAuthority(1);
							break;
						}
					}
				}
			}
			
			
		}
		return menuAllTbls;
	}

	@Override
	public HmiResultObj addRolMenu(MenuRoleTbl menuRoleTbl) {
		return menuDao.addRolMenu(menuRoleTbl);
	}

	@Override
	public HmiResultObj removeRolMenu(MenuRoleTbl menuRoleTbl) {
		return menuDao.removeRolMenu(menuRoleTbl);
	}	
	
	public MenuDao getMenuDao() {
		return menuDao;
	}

	public void setMenuDao(MenuDao menuDao) {
		this.menuDao = menuDao;
	}

	@Override
	public MenuTbl findDashBoardByUserType(int userType) {
		return menuDao.findDashBoardByUserType(userType);
	}

	@Override
	public List<MenuTbl> findAllTopMenu(int userType) {
		List<MenuTbl> menuAllTbls=menuDao.findAllTopMenu();
		
		
		List<MenuTbl> menuAuthTbls=menuDao.findTopMenuByUserType(userType);
		
		
		for (MenuTbl menuTbl : menuAllTbls) {
			menuTbl.setAuthority(0);
			for (MenuTbl menuAuthTbl : menuAuthTbls) {
				if(menuAuthTbl.getMenuId()==menuTbl.getMenuId()){
					menuTbl.setAuthority(1);
					break;
				}
			}
			
		}
		return menuAllTbls;
	}

	@Override
	public MenuRoleTbl findDashBoardRoleMenuByUserType(int userType) {
		return menuDao.findDashBoardRoleMenuByUserType(userType);
	}

	@Override
	public HmiResultObj removeDashboardRolMenu(MenuRoleTbl menuRoleTbl) {
		return menuDao.removeDashboardRolMenu(menuRoleTbl);
	}

	@Override
	public HmiResultObj createMenu(MenuTbl menuTbl) {
		return menuDao.createMenu(menuTbl);
	}

	@Override
	public HmiResultObj createMenuRole(MenuRoleTbl menuRoleTbl) {
		return menuDao.createMenuRole(menuRoleTbl);
	}

	@Override
	public boolean findMenuByName(String menuName) {
		return menuDao.findMenuByName(menuName);
	}


	

	

	
}
