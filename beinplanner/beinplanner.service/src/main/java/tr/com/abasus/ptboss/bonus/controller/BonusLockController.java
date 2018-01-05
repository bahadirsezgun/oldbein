package tr.com.abasus.ptboss.bonus.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import tr.com.abasus.ptboss.exceptions.UnAuthorizedUserException;
import tr.com.abasus.ptboss.ptuser.entity.User;
import tr.com.abasus.ptboss.result.entity.HmiResultObj;
import tr.com.abasus.util.BonusLockUtil;
import tr.com.abasus.util.ResultStatuObj;
import tr.com.abasus.util.SessionUtil;
import tr.com.abasus.util.UserTypes;

@Controller
@RequestMapping(value="/bonusLock")
public class BonusLockController {

	
	@RequestMapping(value="/lock", method = RequestMethod.POST) 
	public @ResponseBody HmiResultObj bonusLock(HttpServletRequest request ) throws UnAuthorizedUserException{
	
		User user=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if(user.getUserType()!=UserTypes.USER_TYPE_ADMIN_INT 
				&& user.getUserType()!=UserTypes.USER_TYPE_MANAGER_INT
				&& user.getUserType()!=UserTypes.USER_TYPE_SUPER_MANAGER_INT){
			throw new UnAuthorizedUserException("YETKISIZ ERISIM");
		}
		
		HmiResultObj hmiResultObj=new HmiResultObj();
		if(BonusLockUtil.BONUS_LOCK_FLAG){
			BonusLockUtil.BONUS_LOCK_FLAG=false;
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS);
		}else{
			BonusLockUtil.BONUS_LOCK_FLAG=true;
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
		}
		
		
		return hmiResultObj;
	}
	
	@RequestMapping(value="/finBonusLock", method = RequestMethod.POST) 
	public @ResponseBody HmiResultObj finBonusLock(HttpServletRequest request ) throws UnAuthorizedUserException{
	
		User user=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if(user.getUserType()!=UserTypes.USER_TYPE_ADMIN_INT 
				&& user.getUserType()!=UserTypes.USER_TYPE_MANAGER_INT
				&& user.getUserType()!=UserTypes.USER_TYPE_SUPER_MANAGER_INT){
			throw new UnAuthorizedUserException("YETKISIZ ERISIM");
		}
		
		HmiResultObj hmiResultObj=new HmiResultObj();
		if(BonusLockUtil.BONUS_LOCK_FLAG){
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
		}else{
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS);
		}
		
		
		
		return hmiResultObj;
	}
	
	
}
