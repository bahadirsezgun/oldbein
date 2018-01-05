package tr.com.abasus.ptboss.menu.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tr.com.abasus.general.dao.AbaxJdbcDaoSupport;
import tr.com.abasus.general.dao.SqlDao;
import tr.com.abasus.general.exception.SqlErrorException;
import tr.com.abasus.ptboss.menu.entity.MenuLevel2;
import tr.com.abasus.ptboss.menu.entity.MenuRoleTbl;
import tr.com.abasus.ptboss.menu.entity.MenuTbl;
import tr.com.abasus.ptboss.result.entity.HmiResultObj;
import tr.com.abasus.util.ResultStatuObj;

public class MenuDaoMySQL extends AbaxJdbcDaoSupport implements MenuDao {

	SqlDao sqlDao;

	
	@Override
	public MenuTbl findDashBoardByUserType(int userType) {
		String sql="SELECT a.* FROM menu_tbl a,menu_role_tbl b"
				+ " WHERE a.MENU_ID=b.MENU_ID"
				+ "   AND b.ROLE_ID=:userType "
				+ "   AND a.UPPER_MENU_ID=4";
		 
		
		
		 Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("userType",userType);
		   
		 try{
			 MenuTbl menuTbl=findEntityForObject(sql, paramMap, MenuTbl.class);
			 return menuTbl;
		 }catch (Exception e) {
				return null;
		 }
	}
	
	
	@Override
	public List<MenuTbl> findUpperMenuByUserType(int userType) {
		String sql="SELECT a.* FROM menu_tbl a,menu_role_tbl b"
				+ " WHERE a.MENU_ID=b.MENU_ID"
				+ "   AND b.ROLE_ID=:userType "
				+ "   AND a.UPPER_MENU_ID=0 "
				+ "   AND a.MENU_SIDE=0 "
				+ "   ORDER BY a.MENU_ID ";
		 
		
		
		 Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("userType",userType);
		   
		 try{
			 List<MenuTbl> menuTbls=findEntityList(sql, paramMap, MenuTbl.class);
			 return menuTbls;
		 }catch (Exception e) {
				return null;
		 }
	}


	@Override
	public List<MenuLevel2> findLevel2MenuByUserType(int userType, int upperMenu) {
		String sql="SELECT a.* FROM menu_tbl a,menu_role_tbl b"
				+ " WHERE a.MENU_ID=b.MENU_ID"
				+ "   AND b.ROLE_ID=:userType "
				+ "   AND a.UPPER_MENU_ID=:upperMenu "
				+ "   AND a.MENU_SIDE=0 "
				+ "   ORDER BY a.MENU_ID ";
		 
		
		
		 Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("userType",userType);
		 paramMap.put("upperMenu",upperMenu);
		 
		 try{
			 List<MenuLevel2> menuLevel2Tbls=findEntityList(sql, paramMap, MenuLevel2.class);
			 return menuLevel2Tbls;
		 }catch (Exception e) {
				return null;
		 }
	}
	
	public SqlDao getSqlDao() {
		return sqlDao;
	}

	public void setSqlDao(SqlDao sqlDao) {
		this.sqlDao = sqlDao;
	}


	@Override
	public List<MenuTbl> findTopMenuByUserType(int userType) {
		String sql="SELECT a.* FROM menu_tbl a,menu_role_tbl b"
				+ " WHERE a.MENU_ID=b.MENU_ID"
				+ "   AND b.ROLE_ID=:userType "
				+ "   AND a.MENU_SIDE=1 "
				+ "   ORDER BY a.MENU_ID ";
		 
		 Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("userType",userType);
		   
		 try{
			 List<MenuTbl> menuTbls=findEntityList(sql, paramMap, MenuTbl.class);
			 return menuTbls;
		 }catch (Exception e) {
				return null;
		 }
	}


	@Override
	public List<MenuTbl> findAllUpperMenuForRoleMenu() {
		String sql="SELECT a.* FROM menu_tbl a "
				+ " WHERE a.MENU_ID <> 300 "
				+ "   AND a.UPPER_MENU_ID=0 "
				+ "   AND a.MENU_SIDE=0 "
				+ "   ORDER BY a.MENU_ID ";
		 
		   
		 try{
			 List<MenuTbl> menuTbls=findEntityList(sql, null, MenuTbl.class);
			 return menuTbls;
		 }catch (Exception e) {
				return null;
		 }
	}


	@Override
	public HmiResultObj addRolMenu(MenuRoleTbl menuRoleTbl) {
		HmiResultObj hmiResultObj=null;
		try {
			sqlDao.insertUpdate(menuRoleTbl);
			
			MenuTbl menuTbl= findMenuTblByMenuName(menuRoleTbl.getMenuName(),menuRoleTbl.getMenuId());
			if(menuTbl!=null){
				MenuRoleTbl menuRoleTbl2=new MenuRoleTbl();
				menuRoleTbl2.setMenuId(menuTbl.getMenuId());
				menuRoleTbl2.setRoleId(menuRoleTbl.getRoleId());
				menuRoleTbl2.setRoleName(menuRoleTbl.getRoleName());
				sqlDao.insertUpdate(menuRoleTbl2);
			}
			hmiResultObj=new HmiResultObj();
			//hmiResultObj.setResultObj(member);
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS);
		} catch (SqlErrorException e) {
			e.printStackTrace();
			hmiResultObj=new HmiResultObj();
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
		}
		return hmiResultObj;
	}

	
	private MenuTbl findMenuTblByMenuName(String menuName,int menuId){
		String sql="SELECT a.* FROM menu_tbl a "
				+ " WHERE a.MENU_NAME=:menuName"
				+ "   AND a.MENU_ID<>:menuId"
				+ " LIMIT 1 ";
		 
		 Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("menuName",menuName);
		 paramMap.put("menuId",menuId);
		  
		 try{
			 MenuTbl menuTbl=findEntityForObject(sql, paramMap, MenuTbl.class);
			 return menuTbl;
		 }catch (Exception e) {
				return null;
		 }
	}
	

	@Override
	public HmiResultObj removeRolMenu(MenuRoleTbl menuRoleTbl) {
		HmiResultObj hmiResultObj=null;
		try {
			sqlDao.delete(menuRoleTbl);
			
			
			MenuTbl menuTbl= findMenuTblByMenuName(menuRoleTbl.getMenuName(),menuRoleTbl.getMenuId());
			if(menuTbl!=null){
				MenuRoleTbl menuRoleTbl2=new MenuRoleTbl();
				menuRoleTbl2.setMenuId(menuTbl.getMenuId());
				menuRoleTbl2.setRoleId(menuRoleTbl.getRoleId());
				sqlDao.delete(menuRoleTbl2);
			}
			
			hmiResultObj=new HmiResultObj();
			//hmiResultObj.setResultObj(member);
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS);
		} catch (SqlErrorException e) {
			e.printStackTrace();
			hmiResultObj=new HmiResultObj();
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
		}
		return hmiResultObj;
	}


	@Override
	public List<MenuTbl> findAllTopMenu() {
		String sql="SELECT a.* FROM menu_tbl a"
				+ " WHERE a.MENU_SIDE=1 "
				+ "   ORDER BY a.MENU_ID ";
		 
		  
		 try{
			 List<MenuTbl> menuTbls=findEntityList(sql, null, MenuTbl.class);
			 return menuTbls;
		 }catch (Exception e) {
				return null;
		 }
	}


	@Override
	public MenuRoleTbl findDashBoardRoleMenuByUserType(int userType) {
		String sql="SELECT b.* FROM menu_role_tbl b"
				+ " WHERE b.ROLE_ID=:userType"
				+ "   AND b.MENU_ID IN (800,801,802) ";
		 
		
		////System.out.println(sql+" "+userType);
		
		 Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("userType",userType);
		   
		 try{
			 MenuRoleTbl menuRoleTbl=findEntityForObject(sql, paramMap, MenuRoleTbl.class);
			 return menuRoleTbl;
		 }catch (Exception e) {
				return null;
		 }
	}


	@Override
	public HmiResultObj removeDashboardRolMenu(MenuRoleTbl menuRoleTbl) {
		HmiResultObj hmiResultObj=null;
		try {
			sqlDao.delete(menuRoleTbl);
			hmiResultObj=new HmiResultObj();
			//hmiResultObj.setResultObj(member);
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS);
		} catch (SqlErrorException e) {
			e.printStackTrace();
			hmiResultObj=new HmiResultObj();
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
		}
		return hmiResultObj;
	}


	@Override
	public HmiResultObj createMenu(MenuTbl menuTbl) {
		HmiResultObj hmiResultObj=null;
		/*
		try {
			sqlDao.insertUpdate(menuTbl);
			
			hmiResultObj=new HmiResultObj();
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS);
			hmiResultObj.setResultMessage(getLastMenuIdSeq());
		} catch (SqlErrorException e) {
			e.printStackTrace();
			hmiResultObj=new HmiResultObj();
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
			hmiResultObj.setResultMessage(ResultStatuObj.RESULT_STATU_FAIL_STR);
		}
		*/
		String sql="INSERT INTO menu_tbl (MENU_ID"
				+ ",MENU_NAME"
				+ ",UPPER_MENU_ID"
				+ ",MENU_LINK"
				+ ",MENU_COMMENT"
				+ ",MENU_SIDE"
				+ ",MENU_CLASS) "
				+ "VALUES ("
				+ ":menuId"
				+ ",:menuName"
				+ ",:upperMenuId"
				+ ",:menuLink"
				+ ",:menuComment"
				+ ",:menuSide"
				+ ",:menuClass)";
		
		 Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("menuId",menuTbl.getMenuId());
		 paramMap.put("menuName",menuTbl.getMenuName());
		 paramMap.put("upperMenuId",menuTbl.getUpperMenu());
		 paramMap.put("menuLink",menuTbl.getMenuLink());
		 paramMap.put("menuComment",menuTbl.getMenuComment());
		 paramMap.put("menuSide",menuTbl.getMenuSide());
		 paramMap.put("menuClass",menuTbl.getMenuClass());
		 	
		 try {
			update(sql, paramMap);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return hmiResultObj;
	}


	@Override
	public HmiResultObj createMenuRole(MenuRoleTbl menuRoleTbl) {
		HmiResultObj hmiResultObj=null;
		try {
			sqlDao.insertUpdate(menuRoleTbl);
			
			hmiResultObj=new HmiResultObj();
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS);
			hmiResultObj.setResultMessage(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
		} catch (SqlErrorException e) {
			e.printStackTrace();
			hmiResultObj=new HmiResultObj();
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
			hmiResultObj.setResultMessage(ResultStatuObj.RESULT_STATU_FAIL_STR);
		}
		
		
		
		
		
		return hmiResultObj;
	}

	private String getLastMenuIdSeq(){
		String sql=	"SELECT CAST(max(MENU_ID) AS CHAR) MENU_ID" +
				" FROM menu_tbl ";
		String menuId="1";
		try {
			menuId=""+findEntityForObject(sql, null, Long.class);
			if(menuId.equals("null"))
				menuId="1";
		} catch (Exception e) {
			menuId="1";
			e.printStackTrace();
		}
		return menuId;

	}


	@Override
	public boolean findMenuByName(String menuName) {
		String sql="SELECT a.* FROM menu_tbl a"
				+ " WHERE a.MENU_NAME LIKE :menuName "
				+ " LIMIT 1 ";
		 
		 Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("menuName",menuName);
		 try{
			 MenuTbl menuTbl=findEntityForObject(sql, paramMap, MenuTbl.class);
			 if(menuTbl!=null)
				 return true;
			 else
				 return false;
			 
		 }catch (Exception e) {
			 return false;
		 }
	}

	


	
}
