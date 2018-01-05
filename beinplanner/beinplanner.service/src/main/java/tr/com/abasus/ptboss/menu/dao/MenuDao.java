package tr.com.abasus.ptboss.menu.dao;

import java.util.List;

import tr.com.abasus.ptboss.menu.entity.MenuLevel2;
import tr.com.abasus.ptboss.menu.entity.MenuRoleTbl;
import tr.com.abasus.ptboss.menu.entity.MenuTbl;
import tr.com.abasus.ptboss.result.entity.HmiResultObj;

public interface MenuDao {

	public List<MenuTbl> findUpperMenuByUserType(int userType);
	
	public List<MenuLevel2> findLevel2MenuByUserType(int userType,int upperMenu);
	
	public List<MenuTbl> findAllTopMenu();
	
	public List<MenuTbl> findTopMenuByUserType(int userType);
	
	public List<MenuTbl> findAllUpperMenuForRoleMenu();
	
	public HmiResultObj addRolMenu(MenuRoleTbl menuRoleTbl);
	
	public HmiResultObj removeRolMenu(MenuRoleTbl menuRoleTbl);
	
	public MenuTbl findDashBoardByUserType(int userType);
	
	public MenuRoleTbl findDashBoardRoleMenuByUserType(int userType);

	public HmiResultObj removeDashboardRolMenu(MenuRoleTbl menuRoleTbl);

	
	public HmiResultObj createMenu(MenuTbl menuTbl);
	
	public HmiResultObj createMenuRole(MenuRoleTbl menuRoleTbl);
	
	public boolean findMenuByName(String menuName);
	
}
