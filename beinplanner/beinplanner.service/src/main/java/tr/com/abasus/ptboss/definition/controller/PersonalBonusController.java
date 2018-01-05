package tr.com.abasus.ptboss.definition.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import tr.com.abasus.ptboss.definition.entity.DefBonus;
import tr.com.abasus.ptboss.definition.service.PersonalBonusService;
import tr.com.abasus.ptboss.ptuser.entity.User;
import tr.com.abasus.ptboss.result.entity.HmiResultObj;
import tr.com.abasus.util.SessionUtil;
import tr.com.abasus.util.UserTypes;

@Controller
@RequestMapping(value="/pbonus")
public class PersonalBonusController {

	@Autowired
	PersonalBonusService personalBonusService;
	
	@RequestMapping(value="/personal/findPersonalRateBonus/{userId}", method = RequestMethod.POST) 
	public @ResponseBody List<DefBonus> findPersonalRateBonus(@PathVariable("userId") int userId , HttpServletRequest request ){
		User user=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if((user.getUserType()==UserTypes.USER_TYPE_MEMBER_INT)){
			return null;
		}
		return personalBonusService.findPersonalRateBonus(userId);
	}
	
	@RequestMapping(value="/personal/createPersonalRateBonus", method = RequestMethod.POST) 
	public @ResponseBody HmiResultObj createPersonalRateBonus(@RequestBody DefBonus defBonus, HttpServletRequest request ){
		User user=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if((user.getUserType()==UserTypes.USER_TYPE_MEMBER_INT)){
			return null;
		}
		return personalBonusService.createPersonalRateBonus(defBonus);
	}
	
	@RequestMapping(value="/personal/deletePersonalRateBonus/{bonusId}", method = RequestMethod.POST) 
	public @ResponseBody HmiResultObj deletePersonalRateBonus(@PathVariable("bonusId") long bonusId, HttpServletRequest request ){
		User user=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if((user.getUserType()==UserTypes.USER_TYPE_MEMBER_INT)){
			return null;
		}
		return personalBonusService.deletePersonalRateBonus(bonusId);
	}
	
	
	
	@RequestMapping(value="/personal/findPersonalStaticBonus/{userId}", method = RequestMethod.POST) 
	public @ResponseBody List<DefBonus> findPersonalStaticBonus(@PathVariable("userId") int userId , HttpServletRequest request ){
		User user=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if((user.getUserType()==UserTypes.USER_TYPE_MEMBER_INT)){
			return null;
		}
		return personalBonusService.findPersonalStaticBonus(userId);
	}
	
	@RequestMapping(value="/personal/createPersonalStaticBonus", method = RequestMethod.POST) 
	public @ResponseBody HmiResultObj createPersonalStaticBonus(@RequestBody DefBonus defBonus, HttpServletRequest request ){
		User user=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if((user.getUserType()==UserTypes.USER_TYPE_MEMBER_INT)){
			return null;
		}
		return personalBonusService.createPersonalStaticBonus(defBonus);
	}
	
	@RequestMapping(value="/personal/deletePersonalStaticBonus/{bonusId}", method = RequestMethod.POST) 
	public @ResponseBody HmiResultObj deletePersonalStaticBonus(@PathVariable("bonusId") long bonusId, HttpServletRequest request ){
		User user=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if((user.getUserType()==UserTypes.USER_TYPE_MEMBER_INT)){
			return null;
		}
		return personalBonusService.deletePersonalStaticBonus(bonusId);
	}
	
	@RequestMapping(value="/personal/findPersonalStaticRateBonus/{userId}", method = RequestMethod.POST) 
	public @ResponseBody List<DefBonus> findPersonalStaticRateBonus(@PathVariable("userId") int userId , HttpServletRequest request ){
		User user=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if((user.getUserType()==UserTypes.USER_TYPE_MEMBER_INT)){
			return null;
		}
		return personalBonusService.findPersonalStaticRateBonus(userId);
	}
	
	@RequestMapping(value="/personal/createPersonalStaticRateBonus", method = RequestMethod.POST) 
	public @ResponseBody HmiResultObj createPersonalStaticRateBonus(@RequestBody DefBonus defBonus, HttpServletRequest request ){
		User user=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if((user.getUserType()==UserTypes.USER_TYPE_MEMBER_INT)){
			return null;
		}
		return personalBonusService.createPersonalStaticRateBonus(defBonus);
	}
	
	@RequestMapping(value="/personal/deletePersonalStaticRateBonus/{bonusId}", method = RequestMethod.POST) 
	public @ResponseBody HmiResultObj deletePersonalStaticRateBonus(@PathVariable("bonusId") long bonusId, HttpServletRequest request ){
		User user=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if((user.getUserType()==UserTypes.USER_TYPE_MEMBER_INT)){
			return null;
		}
		return personalBonusService.deletePersonalStaticRateBonus(bonusId);
	}
	
	
	
	
	
}
