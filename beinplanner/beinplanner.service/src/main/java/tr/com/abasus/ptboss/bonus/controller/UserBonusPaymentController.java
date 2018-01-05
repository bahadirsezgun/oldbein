package tr.com.abasus.ptboss.bonus.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import tr.com.abasus.ptboss.bonus.businessEntity.UserBonusSearchObj;
import tr.com.abasus.ptboss.bonus.businessEntity.UserPaymentObj;
import tr.com.abasus.ptboss.bonus.entity.UserBonusPaymentClass;
import tr.com.abasus.ptboss.bonus.entity.UserBonusPaymentFactory;
import tr.com.abasus.ptboss.bonus.entity.UserBonusPaymentInterface;
import tr.com.abasus.ptboss.bonus.entity.UserBonusPaymentPersonal;
import tr.com.abasus.ptboss.exceptions.UnAuthorizedUserException;
import tr.com.abasus.ptboss.ptuser.entity.User;
import tr.com.abasus.ptboss.result.entity.HmiResultObj;
import tr.com.abasus.util.BonusTypes;
import tr.com.abasus.util.DateTimeUtil;
import tr.com.abasus.util.GlobalUtil;
import tr.com.abasus.util.OhbeUtil;
import tr.com.abasus.util.SessionUtil;
import tr.com.abasus.util.UserTypes;

@Controller
@RequestMapping(value="/userBonusPayment")
public class UserBonusPaymentController {

	
	@Autowired
	UserBonusPaymentClass userBonusPaymentClass;
	
	@Autowired
	UserBonusPaymentPersonal userBonusPaymentPersonal;
	
	
	
	
	@RequestMapping(value="/findUserPaymentDetail/{bonType}/{schtId}", method = RequestMethod.POST) 
	public @ResponseBody UserPaymentObj findUserPaymentDetail(@PathVariable("bonType") int bonType,@PathVariable("schtId") long schtId , HttpServletRequest request ) throws UnAuthorizedUserException{
		User user=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if(user==null){
			return null;
		}else if((user.getUserType()==UserTypes.USER_TYPE_MEMBER_INT)){
			throw new UnAuthorizedUserException("YETKISIZ ERISIM");
		}
		
		UserBonusPaymentInterface userBonusPaymentInterface;
		if(bonType==BonusTypes.BONUS_TYPE_PERSONAL){
			userBonusPaymentInterface=userBonusPaymentPersonal;
		}else{
			userBonusPaymentInterface=userBonusPaymentClass;
		}
		
		return userBonusPaymentInterface.findUserPayment(schtId);
	}
	
	
	@RequestMapping(value="/saveBonusPayment/{bonType}", method = RequestMethod.POST) 
	public @ResponseBody HmiResultObj saveBonusPayment(@RequestBody UserBonusPaymentFactory userBonusPaymentFactory, @PathVariable("bonType") int bonType , HttpServletRequest request ) throws UnAuthorizedUserException{
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
		
		
		UserBonusPaymentInterface userBonusPaymentInterface;
		if(bonType==BonusTypes.BONUS_TYPE_PERSONAL){
			userBonusPaymentInterface=userBonusPaymentPersonal;
		}else{
			userBonusPaymentInterface=userBonusPaymentClass;
		}
		
      if(userBonusPaymentFactory.getBonQueryType()==1){ // AYLIK SORGULAMA
			
			int year=userBonusPaymentFactory.getBonYear();
			int month=userBonusPaymentFactory.getBonMonth();
			String monthStr=""+month;
			if(month<10)
				 monthStr="0"+month;
			
			String startDateStr="01/"+monthStr+"/"+year;
			Date startDate=DateTimeUtil.getThatDayFormatNotNull(startDateStr, "dd/MM/yyyy");
			Date endDate=DateTimeUtil.getThatDayFormatNotNull(startDateStr, "dd/MM/yyyy");
			endDate=OhbeUtil.getDateForNextMonth(endDate, 1);
			
			userBonusPaymentFactory.setBonStartDate(startDate);
			userBonusPaymentFactory.setBonEndDate(endDate);
			
		}else{
			
			Date startDate=DateTimeUtil.getThatDayFormatNotNull(userBonusPaymentFactory.getBonStartDateStr() , GlobalUtil.global.getPtScrDateFormat());
			Date endDate=DateTimeUtil.getThatDayFormatNotNull(userBonusPaymentFactory.getBonEndDateStr() , GlobalUtil.global.getPtScrDateFormat());
			
			userBonusPaymentFactory.setBonStartDate(startDate);
			userBonusPaymentFactory.setBonEndDate(endDate);
			
			int month=DateTimeUtil.getMonthOfDate(startDate);
			int year=DateTimeUtil.getYearOfDate(startDate);
			
			userBonusPaymentFactory.setBonMonth(month);
			userBonusPaymentFactory.setBonYear(year);
			
			
		}
		
        userBonusPaymentFactory.setBonPaymentDate(DateTimeUtil.getThatDayFormatNotNull(userBonusPaymentFactory.getBonPaymentDateStr() , GlobalUtil.global.getPtScrDateFormat()));
		
		return userBonusPaymentInterface.saveBonusPayment(userBonusPaymentFactory);
		
		
	}
	
	@RequestMapping(value="/deleteBonusPayment/{bonType}", method = RequestMethod.POST) 
	public @ResponseBody HmiResultObj deleteBonusPayment(@RequestBody UserBonusPaymentFactory userBonusPaymentFactory, @PathVariable("bonType") int bonType , HttpServletRequest request ) throws UnAuthorizedUserException{
	
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
		
		UserBonusPaymentInterface userBonusPaymentInterface;
		if(bonType==BonusTypes.BONUS_TYPE_PERSONAL){
			userBonusPaymentInterface=userBonusPaymentPersonal;
		}else{
			userBonusPaymentInterface=userBonusPaymentClass;
		}
		
		return userBonusPaymentInterface.deleteBonusPayment(userBonusPaymentFactory);
		
	}
	
	@RequestMapping(value="/findStaffBonusPayment/{bonType}", method = RequestMethod.POST) 
	public @ResponseBody List<UserBonusPaymentFactory> findStaffBonusPayment(@RequestBody UserBonusSearchObj userBonusSearchObj, @PathVariable("bonType") int bonType , HttpServletRequest request ) throws UnAuthorizedUserException{
		User user=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if(user==null){
			return null;
		}else if((user.getUserType()==UserTypes.USER_TYPE_MEMBER_INT)){
			throw new UnAuthorizedUserException("YETKISIZ ERISIM");
		}
		
		UserBonusPaymentInterface userBonusPaymentInterface;
		if(bonType==BonusTypes.BONUS_TYPE_PERSONAL){
			userBonusPaymentInterface=userBonusPaymentPersonal;
		}else{
			userBonusPaymentInterface=userBonusPaymentClass;
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
		
		return userBonusPaymentInterface.findBonusPayment(userBonusSearchObj);
		
	}
	
}
