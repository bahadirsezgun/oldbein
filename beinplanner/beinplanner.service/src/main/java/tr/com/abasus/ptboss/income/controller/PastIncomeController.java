package tr.com.abasus.ptboss.income.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import tr.com.abasus.ptboss.bonus.entity.UserBonusPaymentFactory;
import tr.com.abasus.ptboss.bonus.entity.UserBonusPaymentPersonal;
import tr.com.abasus.ptboss.bonus.service.BonusPersonalService;
import tr.com.abasus.ptboss.exceptions.UnAuthorizedUserException;
import tr.com.abasus.ptboss.income.entity.PastIncomeMonthTbl;
import tr.com.abasus.ptboss.income.entity.PtExpenses;
import tr.com.abasus.ptboss.income.entity.PtMonthlyInOutObj;
import tr.com.abasus.ptboss.income.service.PastIncomeService;
import tr.com.abasus.ptboss.packetpayment.entity.PacketPaymentFactory;
import tr.com.abasus.ptboss.packetpayment.entity.PacketPaymentPersonal;
import tr.com.abasus.ptboss.ptuser.entity.User;
import tr.com.abasus.ptboss.result.entity.HmiResultObj;
import tr.com.abasus.util.DateTimeUtil;
import tr.com.abasus.util.GlobalUtil;
import tr.com.abasus.util.OhbeUtil;
import tr.com.abasus.util.SessionUtil;

@Controller
@RequestMapping(value="/incomeController")
public class PastIncomeController {

	@Autowired
	PastIncomeService pastIncomeService;
	
	@Autowired
	BonusPersonalService bonusService;
	
	@Autowired
	PacketPaymentPersonal packetPaymentPersonal;
	
	@Autowired
	UserBonusPaymentPersonal userBonusPaymentPersonal;
	
	
	@RequestMapping(value="/findPastForYear/{year}/{firmId}", method = RequestMethod.POST) 
	public @ResponseBody List<PastIncomeMonthTbl> findPastForYear(@PathVariable("year") int year,@PathVariable("firmId") int firmId, HttpServletRequest request ) throws UnAuthorizedUserException{
		User user=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if(user==null){
			throw new UnAuthorizedUserException("");
		}
	
		List<PastIncomeMonthTbl> pastIncomeMonthTbls=new ArrayList<PastIncomeMonthTbl>();
	
		for (int i=1;i<13;i++) {
			
			PastIncomeMonthTbl pastIncomeMonthTbl=new PastIncomeMonthTbl();
			pastIncomeMonthTbl.setPimMonth(i);
			pastIncomeMonthTbl.setPimMonthName(DateTimeUtil.getMonthNamesBySequence(i));
			pastIncomeMonthTbl.setFirmId(firmId);
			pastIncomeMonthTbl.setPimYear(year);
				
			
			
			
			List<PtExpenses> ptExpenses=pastIncomeService.findPtExpensesForMonth(year, i, firmId);
			
			double expenseGeneral=0;
			double incomeGeneral=0;
			
			for (PtExpenses ptExpenses2 : ptExpenses) {
				if(ptExpenses2.getPeInOut()==0)
					expenseGeneral+=ptExpenses2.getPeAmount();
				else
					incomeGeneral+=ptExpenses2.getPeAmount();
				
			}
			
			pastIncomeMonthTbl.setExpenseGeneral(expenseGeneral);
			pastIncomeMonthTbl.setExpenseToBonus(userBonusPaymentPersonal.findTotalOfMonth(firmId, i,year));
			pastIncomeMonthTbl.setTotalExpense(pastIncomeMonthTbl.getExpenseGeneral()+pastIncomeMonthTbl.getExpenseToBonus());
			
			pastIncomeMonthTbl.setIncomeGeneral(incomeGeneral);
			pastIncomeMonthTbl.setIncomeFromMembers(packetPaymentPersonal.findTotalIncomePaymentInMonth(firmId, i,year));
			
			pastIncomeMonthTbl.setTotalIncome(pastIncomeMonthTbl.getIncomeGeneral()+pastIncomeMonthTbl.getIncomeFromMembers());
			
			pastIncomeMonthTbl.setTotalEarn(pastIncomeMonthTbl.getTotalIncome()-pastIncomeMonthTbl.getTotalExpense());
			pastIncomeMonthTbls.add(pastIncomeMonthTbl);
			
		}
		return pastIncomeMonthTbls;
	 }
		
	
	
	@RequestMapping(value="/findPtInOutDetailForMonth/{year}/{month}/{firmId}", method = RequestMethod.POST) 
	public @ResponseBody PtMonthlyInOutObj findPtInOutDetailForMonth(@PathVariable("year") int year,@PathVariable("month") int month,@PathVariable("firmId") int firmId, HttpServletRequest request ) throws UnAuthorizedUserException{
	
		User user=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if(user==null){
			throw new UnAuthorizedUserException("");
		}
		
		PtMonthlyInOutObj ptMonthlyInOutObj=new PtMonthlyInOutObj();
		
		List<PtExpenses> ptExp= pastIncomeService.findPtExpensesForMonth(year, month, firmId);
		ptMonthlyInOutObj.setPtExp(ptExp);
		
		List<PacketPaymentFactory> ppf=packetPaymentPersonal.findIncomePaymentInMonth(firmId, month, year);
		ptMonthlyInOutObj.setPpf(ppf);
		
		List<UserBonusPaymentFactory> userBonusPaymentFactories=userBonusPaymentPersonal.findBonusPaymentForStaffsByMonth(month, year, firmId);
		ptMonthlyInOutObj.setUbpf(userBonusPaymentFactories);
		
		
		return ptMonthlyInOutObj;
		
	}
	
	
	
	@RequestMapping(value="/findPtExpensesForMonth/{year}/{month}/{firmId}", method = RequestMethod.POST) 
	public @ResponseBody List<PtExpenses> findPtExpensesForMonth(@PathVariable("year") int year,@PathVariable("month") int month,@PathVariable("firmId") int firmId, HttpServletRequest request ) throws UnAuthorizedUserException{
		User user=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if(user==null){
			throw new UnAuthorizedUserException("");
		}
		return pastIncomeService.findPtExpensesForMonth(year, month, firmId);
	}
	
	@RequestMapping(value="/findPtExpensesByDate", method = RequestMethod.POST) 
	public @ResponseBody List<PtExpenses> findPtExpensesByDate(@RequestBody PtExpenses ptExpenses, HttpServletRequest request ) throws UnAuthorizedUserException{
		User user=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if(user==null){
			throw new UnAuthorizedUserException("");
		}
		
		Date peDate=OhbeUtil.getThatDayFormatNotNull(ptExpenses.getPeDateStr(), GlobalUtil.global.getPtScrDateFormat());
		
		int year=DateTimeUtil.getYearOfDate(peDate);
		int month=DateTimeUtil.getMonthOfDate(peDate);
		int firmId=ptExpenses.getFirmId();
				
		
		
		return pastIncomeService.findPtExpensesForMonth(year, month, firmId);
	}
	
	
	
	@RequestMapping(value="/findPtExpensesById/{peId}", method = RequestMethod.POST) 
	public @ResponseBody PtExpenses findPtExpensesById(@PathVariable long peId, HttpServletRequest request ) throws UnAuthorizedUserException{
		User user=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if(user==null){
			throw new UnAuthorizedUserException("");
		}

		return pastIncomeService.findPtExpensesById(peId);
	}
	
	@RequestMapping(value="/findPtTotalExpensesByMonth/{year}/{month}/{firmId}", method = RequestMethod.POST) 
	public @ResponseBody PtExpenses findPtTotalExpensesByMonth(@PathVariable("year") int year,@PathVariable("month") int month,@PathVariable("firmId") int firmId, HttpServletRequest request ) throws UnAuthorizedUserException{
		User user=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if(user==null){
			throw new UnAuthorizedUserException("");
		}
		
		List<PtExpenses> ptExpenses=pastIncomeService.findPtExpensesForMonth(year, month, firmId);
		
		double totalExpense=0;
		double totalIncome=0;
		String monthName=DateTimeUtil.getMonthNamesBySequence(month);
		
		for (PtExpenses ptExpenses2 : ptExpenses) {
			if(ptExpenses2.getPeInOut()==0)
			   totalExpense+=ptExpenses2.getPeAmount();
			else
			   totalIncome+=ptExpenses2.getPeAmount();
		}
		
		PtExpenses ptExp=new PtExpenses();
		ptExp.setTotalExpense(totalExpense);
		ptExp.setTotalIncome(totalIncome);
		ptExp.setMonthName(monthName);

		return ptExp;
	}
	
	@RequestMapping(value="/createPtExpenses", method = RequestMethod.POST) 
	public @ResponseBody HmiResultObj createPtExpenses(@RequestBody PtExpenses ptExpenses, HttpServletRequest request ) throws UnAuthorizedUserException{
		User user=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if(user==null){
			throw new UnAuthorizedUserException("");
		}
		Date peDate=OhbeUtil.getThatDayFormatNotNull(ptExpenses.getPeDateStr(), GlobalUtil.global.getPtScrDateFormat());
		ptExpenses.setPeDate(peDate);
		
		return pastIncomeService.createPtExpenses(ptExpenses);
	}
		
	@RequestMapping(value="/deletePtExpenses", method = RequestMethod.POST) 
	public @ResponseBody HmiResultObj deletePtExpenses(@RequestBody PtExpenses ptExpenses, HttpServletRequest request ) throws UnAuthorizedUserException{
		User user=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if(user==null){
			throw new UnAuthorizedUserException("");
		}

		return pastIncomeService.deletePtExpenses(ptExpenses);
	}
	
}
