package tr.com.abasus.ptboss.bonus.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import tr.com.abasus.ptboss.bonus.businessEntity.UserBonusObj;
import tr.com.abasus.ptboss.bonus.businessEntity.UserBonusSearchObj;
import tr.com.abasus.ptboss.bonus.businessService.UserBonusCalculateClassService;
import tr.com.abasus.ptboss.bonus.businessService.UserBonusCalculatePersonalService;
import tr.com.abasus.ptboss.bonus.businessService.UserBonusCalculateService;
import tr.com.abasus.ptboss.exceptions.UnAuthorizedUserException;
import tr.com.abasus.ptboss.ptuser.entity.User;
import tr.com.abasus.util.BonusTypes;
import tr.com.abasus.util.DateTimeUtil;
import tr.com.abasus.util.GlobalUtil;
import tr.com.abasus.util.OhbeUtil;
import tr.com.abasus.util.SessionUtil;
import tr.com.abasus.util.UserTypes;

@Controller
@RequestMapping(value="/userBonusCalculator")
public class UserBonusCalculatorController {

	@Autowired
	UserBonusCalculateClassService userBonusCalculateClassService;
	
	@Autowired
	UserBonusCalculatePersonalService userBonusCalculatePersonalService;
	
	
	@RequestMapping(value="/findStaffBonus/{bonType}", method = RequestMethod.POST) 
	public @ResponseBody UserBonusObj findStaffBonus(@RequestBody UserBonusSearchObj userBonusSearchObj, @PathVariable("bonType") int bonType , HttpServletRequest request ) throws UnAuthorizedUserException{
		User user=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if(user==null){
			throw new UnAuthorizedUserException("YETKISIZ ERISIM");
		}else if(user.getUserType()==UserTypes.USER_TYPE_MEMBER_INT){
			throw new UnAuthorizedUserException("YETKISIZ ERISIM");
		}else if(user.getUserType()==UserTypes.USER_TYPE_SCHEDULAR_STAFF_INT){
			throw new UnAuthorizedUserException("YETKISIZ ERISIM");
		}else if(user.getUserType()==UserTypes.USER_TYPE_STAFF_INT){
			throw new UnAuthorizedUserException("YETKISIZ ERISIM");
		}
		
		UserBonusCalculateService userBonusCalculateService;
		if(bonType==BonusTypes.BONUS_TYPE_PERSONAL){
			userBonusCalculateService=userBonusCalculatePersonalService;
		}else{
			userBonusCalculateService=userBonusCalculateClassService;
		}
		
		if(userBonusSearchObj.getQueryType()==1){ // AYLIK SORGULAMA
			
			int year=userBonusSearchObj.getYear();
			int month=userBonusSearchObj.getMonth();
			String monthStr=""+month;
			if(month<10)
				 monthStr="0"+month;
			
			String startDateStr="01/"+monthStr+"/"+year;
			Date startDate=DateTimeUtil.getThatDayFormatNotNull(startDateStr, "dd/MM/yyyy");
			Date endDate=DateTimeUtil.getThatDayFormatNotNull(startDateStr, "dd/MM/yyyy");
			endDate=OhbeUtil.getDateForNextMonth(endDate, 1);
			
			userBonusSearchObj.setStartDate(startDate);
			userBonusSearchObj.setEndDate(endDate);
			
		}else{
			
			Date startDate=DateTimeUtil.getThatDayFormatNotNull(userBonusSearchObj.getStartDateStr() , GlobalUtil.global.getPtScrDateFormat());
			Date endDate=DateTimeUtil.getThatDayFormatNotNull(userBonusSearchObj.getEndDateStr() , GlobalUtil.global.getPtScrDateFormat());
			endDate=OhbeUtil.getDateForNextDate(endDate, 1);
			
			userBonusSearchObj.setStartDate(startDate);
			userBonusSearchObj.setEndDate(endDate);
			
		}
		
		return userBonusCalculateService.findStaffBonusObj(userBonusSearchObj.getSchStaffId(), userBonusSearchObj.getStartDate(), userBonusSearchObj.getEndDate());
	}
	
	
}
