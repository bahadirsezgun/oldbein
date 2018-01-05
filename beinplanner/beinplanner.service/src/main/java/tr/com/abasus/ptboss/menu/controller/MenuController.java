package tr.com.abasus.ptboss.menu.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import tr.com.abasus.ptboss.menu.entity.MenuTbl;
import tr.com.abasus.ptboss.ptuser.entity.User;
import tr.com.abasus.util.SessionUtil;

@Controller
@RequestMapping(value="/menu")
public class MenuController {

	@RequestMapping(value="/getmenu", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE) 
	public @ResponseBody List<MenuTbl> getMenu(HttpServletRequest http){
		User user=(User)http.getSession().getAttribute(SessionUtil.SESSION_USER);
		if(user==null){
			return null;
		}
		return user.getMenuTbls();
	}
	
	 
	@RequestMapping(value="/gettopmenu", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE) 
	public @ResponseBody List<MenuTbl> getTopMenu(HttpServletRequest http){
		User user=(User)http.getSession().getAttribute(SessionUtil.SESSION_USER);
		if(user==null){
			return null;
		}
		return user.getMenuTopTbls();
	}
	
	
    
}
