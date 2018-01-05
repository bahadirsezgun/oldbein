package tr.com.abasus.ptboss.menu.entity;

import java.io.Serializable;

public class MenuLevel2 implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int menuId;
	private String menuName;
	private int upperMenu;
	private String menuLink;
	private String menuComment;
	private int authority;
	
	public int getMenuId() {
		return menuId;
	}
	public void setMenuId(int menuId) {
		this.menuId = menuId;
	}
	public String getMenuName() {
		return menuName;
	}
	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}
	public int getUpperMenu() {
		return upperMenu;
	}
	public void setUpperMenu(int upperMenu) {
		this.upperMenu = upperMenu;
	}
	public String getMenuLink() {
		return menuLink;
	}
	public void setMenuLink(String menuLink) {
		this.menuLink = menuLink;
	}
	public String getMenuComment() {
		return menuComment;
	}
	public void setMenuComment(String menuComment) {
		this.menuComment = menuComment;
	}
	public int getAuthority() {
		return authority;
	}
	public void setAuthority(int authority) {
		this.authority = authority;
	}
	
	
	
}
