package tr.com.abasus.ptboss.menu.entity;

import java.io.Serializable;

public class MenuRoleTbl implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int roleId;
	private int menuId;
	private String roleName;
	private String menuName;
	
	public int getRoleId() {
		return roleId;
	}
	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}
	public int getMenuId() {
		return menuId;
	}
	public void setMenuId(int menuId) {
		this.menuId = menuId;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public String getMenuName() {
		return menuName;
	}
	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}
	
	
}
