package tr.com.abasus.ptboss.dashboard.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import tr.com.abasus.ptboss.bonus.entity.UserBonusPaymentFactory;
import tr.com.abasus.ptboss.bonus.entity.UserBonusPaymentPersonal;
import tr.com.abasus.ptboss.dashboard.entity.ActiveMember;
import tr.com.abasus.ptboss.dashboard.entity.LastClasses;
import tr.com.abasus.ptboss.dashboard.entity.LeftPaymentInfo;
import tr.com.abasus.ptboss.dashboard.entity.PlannedClassInfo;
import tr.com.abasus.ptboss.dashboard.entity.TodayPayment;
import tr.com.abasus.ptboss.dashboard.service.DashboardService;
import tr.com.abasus.ptboss.income.entity.PtExpenses;
import tr.com.abasus.ptboss.income.service.PastIncomeService;
import tr.com.abasus.ptboss.packetpayment.entity.PacketPaymentFactory;
import tr.com.abasus.ptboss.packetsale.decorator.IPacketSale;
import tr.com.abasus.ptboss.packetsale.entity.PacketSaleFactory;
import tr.com.abasus.ptboss.ptuser.entity.User;
import tr.com.abasus.ptboss.ptuser.iuser.ProcessMember;
import tr.com.abasus.ptboss.schedule.entity.ScheduleMembershipPlan;
import tr.com.abasus.ptboss.schedule.entity.ScheduleTimePlan;
import tr.com.abasus.ptboss.update.entity.PtbossUpdate;
import tr.com.abasus.ptboss.update.service.PtUpdateService;
import tr.com.abasus.util.DateTimeUtil;
import tr.com.abasus.util.OhbeUtil;
import tr.com.abasus.util.SessionUtil;
import tr.com.abasus.util.UserTypes;

@Controller
@RequestMapping(value="/dashboard")
public class DashboardController {

	@Autowired
	DashboardService dashboardService;
	
	@Autowired
	PtUpdateService ptUpdateService;
	
	
	@Autowired
	@Qualifier(value="packetPaymentPersonal")
	PacketPaymentFactory packetPaymentFactory;
	
	@Autowired
    IPacketSale iPacketSale;
	
	
	@Autowired
	PastIncomeService pastIncomeService;
	
	@Autowired
	UserBonusPaymentPersonal userBonusPaymentPersonal;
	
	@Autowired
	ProcessMember processMember;
	

	@RequestMapping(value="/findTotalMemberInSystem", method = RequestMethod.POST) 
	public @ResponseBody int findTotalMemberInSystem( HttpServletRequest request ){
		User user=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if(user==null){
			return 0;
		}else if((user.getUserType()==UserTypes.USER_TYPE_MEMBER_INT)){
			return 0;
		}else if((user.getUserType()==UserTypes.USER_TYPE_ADMIN_INT)){
			//user.setFirmId(0);
		}
		
		return processMember.findTotalMemberInSystem(user.getFirmId());
	}
	
		
	
	@RequestMapping(value="/lastOfClasses", method = RequestMethod.POST) 
	public @ResponseBody LastClasses lastOfClasses( HttpServletRequest request ){
		User user=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if(user==null){
			return null;
		}else if((user.getUserType()==UserTypes.USER_TYPE_MEMBER_INT)){
			return null;
		}
		
		
		LastClasses lastClasses=new LastClasses();
		lastClasses.setStpMNW(dashboardService.findLastMembershipsOfUsersThisNextWeek(user.getFirmId()));
		lastClasses.setStpMTW(dashboardService.findLastMembershipsOfUsersThisWeek(user.getFirmId()));
		lastClasses.setStpNW(dashboardService.findLastClassesOfUsersThisNextWeek(user.getFirmId()));
		lastClasses.setStpTW(dashboardService.findLastClassesOfUsersThisWeek(user.getFirmId()));
		
		
		return lastClasses;
		
	}
	
	
	@RequestMapping(value="/packetPayments", method = RequestMethod.POST) 
	public @ResponseBody List<PacketPaymentFactory> packetPayments( HttpServletRequest request ){
		User user=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if(user==null){
			return null;
		}else if((user.getUserType()==UserTypes.USER_TYPE_MEMBER_INT)){
			return null;
		}
		
		List<PacketPaymentFactory> packetPaymentFactories=packetPaymentFactory.findPaymentInGroupDate(user.getFirmId());
		List<PacketPaymentFactory> packetPayments=new ArrayList<PacketPaymentFactory>();
		for (PacketPaymentFactory ppfm : packetPaymentFactories) {
			boolean isAdded=false;
			for (PacketPaymentFactory ppf : packetPayments) {
				if(ppf.getPayDateStr().equals(ppfm.getPayDateStr())){
					ppf.setPayAmount(ppf.getPayAmount()+ppfm.getPayAmount());
					isAdded=true;
					break;
				}
			}
			if(!isAdded){
				packetPayments.add(ppfm);
			}
		}
		
		
		return packetPayments;
	}
	
	
	@RequestMapping(value="/plannedClassInfo/{year}", method = RequestMethod.POST) 
	public @ResponseBody List<PlannedClassInfo> getPlannedClassInfo(@PathVariable int year, HttpServletRequest request ){
		User user=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if(user==null){
			return null;
		}else if((user.getUserType()==UserTypes.USER_TYPE_MEMBER_INT)){
			return null;
		}
		
		return dashboardService.getPlannedClassInfo(user.getFirmId(),year);
	}
	
	@RequestMapping(value="/packetSales", method = RequestMethod.POST) 
	public @ResponseBody List<PacketSaleFactory> packetSales( HttpServletRequest request ){
		User user=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if(user==null){
			return null;
		}else if((user.getUserType()==UserTypes.USER_TYPE_MEMBER_INT)){
			return null;
		}
		
		return iPacketSale.findLast10UserPacketSale(user.getFirmId());
	}
	
	@RequestMapping(value="/todayIncomeExpense", method = RequestMethod.POST) 
	public @ResponseBody TodayPayment todayIncomeExpense( HttpServletRequest request ){
		User user=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if(user==null){
			return null;
		}else if((user.getUserType()==UserTypes.USER_TYPE_MEMBER_INT)){
			return null;
		}
		
		Date startDate=OhbeUtil.getTodayDate();
		Date endDate=OhbeUtil.getDateForNextDate(OhbeUtil.getTodayDate(), 1);
		
		List<PacketPaymentFactory> packetPaymentFactories=packetPaymentFactory.findIncomePaymentInDate(user.getFirmId(), startDate, endDate);
		double income=0;
		for (PacketPaymentFactory packetPaymentFactory : packetPaymentFactories) {
			income+=packetPaymentFactory.getPayAmount();
		}
		
		
		
		double expense=0;
		List<PtExpenses> ptExpenses=pastIncomeService.findPtExpensesForDate(startDate, endDate, user.getFirmId());
		for (PtExpenses ptExpense : ptExpenses) {
			if(ptExpense.getPeInOut()==1){
				income+=ptExpense.getPeAmount();
			}else{
				expense+=ptExpense.getPeAmount();
			}
		}
		
		List<UserBonusPaymentFactory> userBonusPaymentFactories= userBonusPaymentPersonal.findBonusPaymentForToday(user.getFirmId());
		for (UserBonusPaymentFactory userBonusPaymentFactory : userBonusPaymentFactories) {
			expense+=userBonusPaymentFactory.getBonAmount();
		}
		
		
		TodayPayment todayPayment=new TodayPayment();
		todayPayment.setDayName(DateTimeUtil.getDayNames(startDate));
		todayPayment.setExpenseAmount(expense);
		todayPayment.setIncomeAmount(income);
		todayPayment.setMonthName(DateTimeUtil.getMonthNames(startDate));
		return todayPayment;
	}
	
	@RequestMapping(value="/activeMembers", method = RequestMethod.POST) 
	public @ResponseBody ActiveMember findClassRateBonus( HttpServletRequest request ){
		User user=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if(user==null){
			return null;
		}else if((user.getUserType()==UserTypes.USER_TYPE_MEMBER_INT)){
			return null;
		}
		
		return dashboardService.getActiveMembers(user.getFirmId());
	}
	
	@RequestMapping(value="/updateVersion", method = RequestMethod.POST) 
	public @ResponseBody PtbossUpdate findUpdateVersion( HttpServletRequest request ){
		User user=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if(user==null){
			return null;
		}else if((user.getUserType()==UserTypes.USER_TYPE_MEMBER_INT)){
			return null;
		}
		
		return ptUpdateService.findPtBossUpdateByLast();
	}
	
	@RequestMapping(value="/leftPayment", method = RequestMethod.POST) 
	public @ResponseBody LeftPaymentInfo leftPayment( HttpServletRequest request ){
		User user=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if(user==null){
			return null;
		}else if((user.getUserType()==UserTypes.USER_TYPE_MEMBER_INT)){
			return null;
		}
		
		return dashboardService.getLeftPaymentInfo(user.getFirmId());
	}
	
	@RequestMapping(value="/leftPaymentDetail", method = RequestMethod.POST) 
	public @ResponseBody List<PacketSaleFactory> leftPaymentDetail( HttpServletRequest request ){
		User user=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if(user==null){
			return null;
		}else if((user.getUserType()==UserTypes.USER_TYPE_MEMBER_INT)){
			return null;
		}
		
		return dashboardService.getLeftPaymentSale(user.getFirmId());
	}
	
}
