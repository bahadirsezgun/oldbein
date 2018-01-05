package tr.com.abasus.ptboss.menu.entity;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
public class MenuTbl implements Serializable {

	private int menuId;
	private String menuName;
	private int upperMenu;
	private String menuLink;
	private String menuComment;
	private int menuSide;
	private String menuClass;
	
	
	private int authority;
	
	private List<MenuLevel2> menuLevel2s;
	
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
	public List<MenuLevel2> getMenuLevel2s() {
		return menuLevel2s;
	}
	public void setMenuLevel2s(List<MenuLevel2> menuLevel2s) {
		this.menuLevel2s = menuLevel2s;
	}
	public int getMenuSide() {
		return menuSide;
	}
	public void setMenuSide(int menuSide) {
		this.menuSide = menuSide;
	}
	public String getMenuClass() {
		return menuClass;
	}
	public void setMenuClass(String menuClass) {
		this.menuClass = menuClass;
	}
	public int getAuthority() {
		return authority;
	}
	public void setAuthority(int authority) {
		this.authority = authority;
	}
	
	
	
	
	
}
