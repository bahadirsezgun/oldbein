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
import tr.com.abasus.ptboss.definition.service.ClassBonusService;
import tr.com.abasus.ptboss.ptuser.entity.User;
import tr.com.abasus.ptboss.result.entity.HmiResultObj;
import tr.com.abasus.util.SessionUtil;
import tr.com.abasus.util.UserTypes;

@Controller
@RequestMapping(value="/cbonus")
public class ClassBonusController {

	@Autowired
	ClassBonusService classBonusService;
	
	@RequestMapping(value="/class/findClassRateBonus/{userId}", method = RequestMethod.POST) 
	public @ResponseBody List<DefBonus> findClassRateBonus(@PathVariable("userId") int userId , HttpServletRequest request ){
		User user=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if(user==null){
			return null;
		}else if((user.getUserType()==UserTypes.USER_TYPE_MEMBER_INT)){
			return null;
		}
		return classBonusService.findClassRateBonus(userId);
	}
	
	@RequestMapping(value="/class/createClassRateBonus", method = RequestMethod.POST) 
	public @ResponseBody HmiResultObj createClassRateBonus(@RequestBody DefBonus defBonus, HttpServletRequest request ){
		User user=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if(user==null){
			return null;
		}else if((user.getUserType()==UserTypes.USER_TYPE_MEMBER_INT)){
			return null;
		}
		return classBonusService.createClassRateBonus(defBonus);
	}
	
	@RequestMapping(value="/class/deleteClassRateBonus/{bonusId}", method = RequestMethod.POST) 
	public @ResponseBody HmiResultObj deleteClassRateBonus(@PathVariable("bonusId") long bonusId, HttpServletRequest request ){
		User user=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if(user==null){
			return null;
		}else if((user.getUserType()==UserTypes.USER_TYPE_MEMBER_INT)){
			return null;
		}
		return classBonusService.deleteClassRateBonus(bonusId);
	}
	
	
	
	@RequestMapping(value="/class/findClassStaticBonus/{userId}", method = RequestMethod.POST) 
	public @ResponseBody List<DefBonus> findClassStaticBonus(@PathVariable("userId") int userId , HttpServletRequest request ){
		User user=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if(user==null){
			return null;
		}else if((user.getUserType()==UserTypes.USER_TYPE_MEMBER_INT)){
			return null;
		}
		return classBonusService.findClassStaticBonus(userId);
	}
	
	@RequestMapping(value="/class/createClassStaticBonus", method = RequestMethod.POST) 
	public @ResponseBody HmiResultObj createClassStaticBonus(@RequestBody DefBonus defBonus, HttpServletRequest request ){
		User user=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if(user==null){
			return null;
		}else if((user.getUserType()==UserTypes.USER_TYPE_MEMBER_INT)){
			return null;
		}
		return classBonusService.createClassStaticBonus(defBonus);
	}
	
	@RequestMapping(value="/class/deleteClassStaticBonus/{bonusId}", method = RequestMethod.POST) 
	public @ResponseBody HmiResultObj deleteClassStaticBonus(@PathVariable("bonusId") long bonusId, HttpServletRequest request ){
		User user=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if(user==null){
			return null;
		}else if((user.getUserType()==UserTypes.USER_TYPE_MEMBER_INT)){
			return null;
		}
		return classBonusService.deleteClassStaticBonus(bonusId);
	}
	
	@RequestMapping(value="/class/findClassStaticRateBonus/{userId}", method = RequestMethod.POST) 
	public @ResponseBody List<DefBonus> findClassStaticRateBonus(@PathVariable("userId") int userId , HttpServletRequest request ){
		User user=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if(user==null){
			return null;
		}else if((user.getUserType()==UserTypes.USER_TYPE_MEMBER_INT)){
			return null;
		}
		return classBonusService.findClassStaticRateBonus(userId);
	}
	
	@RequestMapping(value="/class/createClassStaticRateBonus", method = RequestMethod.POST) 
	public @ResponseBody HmiResultObj createClassStaticRateBonus(@RequestBody DefBonus defBonus, HttpServletRequest request ){
		User user=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if(user==null){
			return null;
		}else if((user.getUserType()==UserTypes.USER_TYPE_MEMBER_INT)){
			return null;
		}
		return classBonusService.createClassStaticRateBonus(defBonus);
	}
	
	@RequestMapping(value="/class/deleteClassStaticRateBonus/{bonusId}", method = RequestMethod.POST) 
	public @ResponseBody HmiResultObj deleteClassStaticRateBonus(@PathVariable("bonusId") long bonusId, HttpServletRequest request ){
		User user=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if(user==null){
			return null;
		}else if((user.getUserType()==UserTypes.USER_TYPE_MEMBER_INT)){
			return null;
		}
		return classBonusService.deleteClassStaticRateBonus(bonusId);
	}
	
}
