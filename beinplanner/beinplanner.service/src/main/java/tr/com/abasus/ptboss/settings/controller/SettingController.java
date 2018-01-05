package tr.com.abasus.ptboss.settings.controller;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import tr.com.abasus.ptboss.definition.entity.DefStudio;
import tr.com.abasus.ptboss.definition.service.DefinitionService;
import tr.com.abasus.ptboss.exceptions.UnAuthorizedUserException;
import tr.com.abasus.ptboss.local.entity.DbHostTbl;
import tr.com.abasus.ptboss.local.service.GlobalLocalService;
import tr.com.abasus.ptboss.mail.MailObj;
import tr.com.abasus.ptboss.mail.MailSenderThread;
import tr.com.abasus.ptboss.program.entity.ProgramFactory;
import tr.com.abasus.ptboss.program.service.ProgramService;
import tr.com.abasus.ptboss.ptuser.entity.User;
import tr.com.abasus.ptboss.result.entity.HmiResultObj;
import tr.com.abasus.ptboss.settings.entity.DbMailTbl;
import tr.com.abasus.ptboss.settings.entity.PtGlobal;
import tr.com.abasus.ptboss.settings.entity.PtRestrictions;
import tr.com.abasus.ptboss.settings.entity.PtRules;
import tr.com.abasus.ptboss.settings.entity.Rules;
import tr.com.abasus.ptboss.settings.service.SettingService;
import tr.com.abasus.util.BonusLockUtil;
import tr.com.abasus.util.GlobalUtil;
import tr.com.abasus.util.LangUtil;
import tr.com.abasus.util.OhbeUtil;
import tr.com.abasus.util.ResultStatuObj;
import tr.com.abasus.util.RuleUtil;
import tr.com.abasus.util.SessionUtil;
import tr.com.abasus.util.UserTypes;

@Controller
@RequestMapping(value="/setting")
public class SettingController {


	@Autowired
	BasicDataSource mysql;
	
	
	@Autowired
	SettingService settingService;
	
	
	@Autowired
	GlobalLocalService globalLocalService;
	
	@Autowired
	ProgramService programService;
	
	@Autowired
	DefinitionService definitionService;
	
	
	@RequestMapping(value="/findRules", method = RequestMethod.POST) 
	public @ResponseBody List<PtRules> findRules(HttpServletRequest request ){
		User user=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if(!(user.getUserType()==UserTypes.USER_TYPE_ADMIN_INT)){
			return null;
		}
		return settingService.findPtRules();
	}
	
	@RequestMapping(value="/findRestrictionForPacket", method = RequestMethod.POST) 
	public @ResponseBody PtRestrictions findRestrictionForPacket(HttpServletRequest request ){
		if(GlobalUtil.ptRestrictionsForPacket==null){
			GlobalUtil.ptRestrictionsForPacket=globalLocalService.findRestrictionsForPacket(OhbeUtil.PTBOSS_MYPASS);
			GlobalUtil.setRestrictions();
		
		}
		
		User user=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if(user==null){
			return null;
		}
		
		
         if((GlobalUtil.ptRestrictionsForPacket.getGroupRestriction()==1 
        		 && GlobalUtil.ptRestrictionsForPacket.getIndividualRestriction()==0 
        		 && GlobalUtil.ptRestrictionsForPacket.getMembershipRestriction()==0)
            || 	(GlobalUtil.ptRestrictionsForPacket.getGroupRestriction()==0 
           		 && GlobalUtil.ptRestrictionsForPacket.getIndividualRestriction()==1 
           		 && GlobalUtil.ptRestrictionsForPacket.getMembershipRestriction()==0)
            || 	(GlobalUtil.ptRestrictionsForPacket.getGroupRestriction()==0 
      		 	&& GlobalUtil.ptRestrictionsForPacket.getIndividualRestriction()==0 
      		 	&& GlobalUtil.ptRestrictionsForPacket.getMembershipRestriction()==1)
        		 ){
        	 GlobalUtil.ptRestrictionsForPacket.setUniquePacket(1);
         }else{
    	   
    	   
    	   List<ProgramFactory> classprog=programService.findAllProgramClass(user.getFirmId());
    	   
    	   List<ProgramFactory> personalprog=programService.findAllProgramPersonal(user.getFirmId());
    	   
    	   List<ProgramFactory> membershipprog=programService.findAllProgramMembership(user.getFirmId());
    	   
    	   int uniqueCount=0;
    	   
    	   
    	   List<DefStudio> studios= definitionService.findAllStudios(user.getFirmId());
    	   
    	   if(studios.size()>0){
    		   GlobalUtil.ptRestrictionsForPacket.setStudioDefinedForBooking(1);
    	   }else{
    		   GlobalUtil.ptRestrictionsForPacket.setStudioDefinedForBooking(0);
    	   }
    	   
    	   
    	   if(personalprog.size()>0){
    		   uniqueCount++; 
    		   GlobalUtil.ptRestrictionsForPacket.setWhichPacket(1);
    	   }
    		   
    	   if(classprog.size()>0){   
    		   uniqueCount++; 
    		   GlobalUtil.ptRestrictionsForPacket.setWhichPacket(2);
    	   }
    	   
    	   if(membershipprog.size()>0){
    		   uniqueCount++; 
    		   GlobalUtil.ptRestrictionsForPacket.setWhichPacket(3);
    	   }
    	   
    	   if(uniqueCount>1){
    		   GlobalUtil.ptRestrictionsForPacket.setUniquePacket(2);
    	   }else{
    		   GlobalUtil.ptRestrictionsForPacket.setUniquePacket(1);
    	   }
         }
		
		return GlobalUtil.ptRestrictionsForPacket;
	}
	
	
	
	@RequestMapping(value="/findRule/{ruleId}", method = RequestMethod.POST) 
	public @ResponseBody PtRules findRule(@PathVariable("ruleId") int ruleId,HttpServletRequest request ){
		User user=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if(!(user.getUserType()==UserTypes.USER_TYPE_ADMIN_INT)){
			return null;
		}
		return settingService.findPtRuleById(ruleId);
	}
	
	@RequestMapping(value="/createRule", method = RequestMethod.POST) 
	public @ResponseBody HmiResultObj createRule(@RequestBody PtRules ptRules ,HttpServletRequest request ){
		User user=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if(!(user.getUserType()==UserTypes.USER_TYPE_ADMIN_INT)){
			return null;
		}
		
		HmiResultObj hmiResultObj=settingService.createUpdateRule(ptRules);
		
		List<PtRules> ruleList= settingService.findPtRules();
		Rules rules=new Rules();
		for (PtRules ptRulesInDB : ruleList) {
			if(ptRulesInDB.getRuleId()==RuleUtil.ruleLocation){
				rules.setRuleLocation(ptRulesInDB.getRuleValue());
			}else if(ptRulesInDB.getRuleId()==RuleUtil.ruleNoChangeAfterBonusPayment){
				rules.setRuleNoChangeAfterBonusPayment(ptRulesInDB.getRuleValue());
				
				if(ptRulesInDB.getRuleValue()==RuleUtil.RULE_OK){
					BonusLockUtil.BONUS_LOCK_FLAG=true;
				}else{
					BonusLockUtil.BONUS_LOCK_FLAG=false;
				}
				
				
				
			}else if(ptRulesInDB.getRuleId()==RuleUtil.ruleNoClassBeforePayment){
				rules.setRuleNoClassBeforePayment(ptRulesInDB.getRuleValue());
			}else if(ptRulesInDB.getRuleId()==RuleUtil.rulePayBonusForConfirmedPayment){
				rules.setRulePayBonusForConfirmedPayment(ptRulesInDB.getRuleValue());
			//}else if(ptRulesInDB.getRuleId()==RuleUtil.ruleTaxRule){
				//rules.setRuleTaxRule(ptRulesInDB.getRuleValue());
			}else if(ptRulesInDB.getRuleId()==RuleUtil.ruleCreditCardCommission){
				rules.setCreditCardCommission(ptRulesInDB.getRuleValue());
			}else if(ptRulesInDB.getRuleId()==RuleUtil.ruleCreditCardCommissionRate){
				rules.setCreditCardCommissionRate(ptRulesInDB.getRuleValue());
			}else if(ptRulesInDB.getRuleId()==RuleUtil.ruleNoSaleToPlanning){
				rules.setRuleNoSaleToPlanning(ptRulesInDB.getRuleValue());
			}else if(ptRulesInDB.getRuleId()==RuleUtil.ruleNotice){
				rules.setRuleNotice(ptRulesInDB.getRuleValue());
			}
		}
		GlobalUtil.rules=rules;
		return hmiResultObj;
	}
	
	@RequestMapping(value="/findPtGlobal", method = RequestMethod.POST) 
	public @ResponseBody PtGlobal findPtGlobal(HttpServletRequest request ) throws UnAuthorizedUserException {
		
		return settingService.findPtGlobal();
	}
	
	
	
	@RequestMapping(value="/createPtGlobal", method = RequestMethod.POST) 
	public @ResponseBody HmiResultObj createPtGlobal(@RequestBody PtGlobal ptGlobal ,HttpServletRequest request ){
		User user=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if(!(user.getUserType()==UserTypes.USER_TYPE_ADMIN_INT)){
			return null;
		}
		HmiResultObj hmiResultObj=settingService.createUpdatePtGlobal(ptGlobal);
		createGlobalUtils();
		
		return hmiResultObj;
	}
	
	
	
	@RequestMapping(value="/createDbHost", method = RequestMethod.POST) 
	public @ResponseBody HmiResultObj createDbHost(@RequestBody DbHostTbl dbHostTbl ,HttpServletRequest request ) throws UnAuthorizedUserException {
		User user=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if((user.getUserType()==UserTypes.USER_TYPE_MEMBER_INT)){
			throw new UnAuthorizedUserException("User not in session");
		}
		/*
		
		if(controlDatabaseAttributes(dbHostTbl)){
			return globalLocalService.createDbHost(dbHostTbl);
		}else{
			
			DbHostTbl dbTbl=globalLocalService.findDbHost();
			controlDatabaseAttributes(dbTbl);
			
			HmiResultObj hmiResultObj=new HmiResultObj();
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
			hmiResultObj.setResultMessage("noDatabaseFound");
			
			return hmiResultObj;
		}*/
		
		
		return globalLocalService.createDbHost(dbHostTbl);
	}
	
	
	private boolean controlDatabaseAttributes(DbHostTbl dbHostTbl){
		
		boolean controlOk=false;
        String url="jdbc:mysql://"+dbHostTbl.getDbUrl()+":"+dbHostTbl.getDbPort()+"/ptboss?useOldAliasMetadataBehavior=true&useUnicode=true&characterEncoding=utf8";
		
        
        mysql.setUrl(url);
        mysql.setUsername(dbHostTbl.getDbUsername());
        mysql.setPassword(dbHostTbl.getDbPassword());
        
		try {
			Connection con= mysql.getConnection();
			if(con!=null){
				controlOk=true;
			}else{
				controlOk=false;
			}
		} catch (SQLException e) {
			controlOk=false;
		}
		return controlOk;
		
	}
	
	
	@RequestMapping(value="/createDbMail", method = RequestMethod.POST) 
	public @ResponseBody HmiResultObj createDbMail(@RequestBody DbMailTbl dbMailTbl ,HttpServletRequest request ) throws UnAuthorizedUserException {
		User user=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if((user.getUserType()==UserTypes.USER_TYPE_MEMBER_INT)){
			throw new UnAuthorizedUserException("User not in session");
		}
		return settingService.createDbMailTbl(dbMailTbl);
	}
	
	@RequestMapping(value="/findDbHost", method = RequestMethod.POST) 
	public @ResponseBody DbHostTbl findDbHost(HttpServletRequest request ) throws UnAuthorizedUserException {
		User user=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if((user.getUserType()==UserTypes.USER_TYPE_MEMBER_INT)){
			throw new UnAuthorizedUserException("User not in session");
		}
		return globalLocalService.findDbHost();
	}
	
	@RequestMapping(value="/findDbMail", method = RequestMethod.POST) 
	public @ResponseBody DbMailTbl findDbMail(HttpServletRequest request ) throws UnAuthorizedUserException {
		User user=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if((user.getUserType()==UserTypes.USER_TYPE_MEMBER_INT)){
			throw new UnAuthorizedUserException("User not in session");
		}
		return settingService.findDbMailTbl();
	}
	
	
	@RequestMapping(value="/sendTestMail", method = RequestMethod.POST) 
	public @ResponseBody HmiResultObj sendTestMail(@RequestBody MailObj mailObj ,HttpServletRequest request ) throws UnAuthorizedUserException {
		User user=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if((user.getUserType()==UserTypes.USER_TYPE_MEMBER_INT)){
			throw new UnAuthorizedUserException("User not in session");
		}
		
		HmiResultObj hmiResultObj=new HmiResultObj();
		
		if(GlobalUtil.mailSettings!=null){
			
			
			MailSenderThread mailSenderThread=new MailSenderThread(mailObj);
			Thread mt=new Thread(mailSenderThread);
			mt.start();
			
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS);
			hmiResultObj.setResultMessage("mailSendedTo");
			
			
		}else{
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
			hmiResultObj.setResultMessage("noMailSettingFound");
		}
		
		
		
		
		
		return hmiResultObj;
	}
	
	
	
	
	public void createGlobalUtils(){
		
		GlobalUtil.global=settingService.findPtGlobal();
		
		if(GlobalUtil.global.getPtLang().trim().equals("en_EN")){
			LangUtil.setTranslateEn();
		}else if(GlobalUtil.global.getPtLang().trim().equals("tr_TR")){
			LangUtil.setTranslateTr();
		}
		
		if(GlobalUtil.global.getPtDateFormat().equals("%d/%m/%y")){
			GlobalUtil.global.setPtDbDateFormat("dd/MM/yy");
			GlobalUtil.global.setPtScrDateFormat("dd/MM/yy");
		}else if(GlobalUtil.global.getPtDateFormat().equals("%d.%m.%y")){
			GlobalUtil.global.setPtDbDateFormat("MM.dd.yy");
			GlobalUtil.global.setPtScrDateFormat("MM.dd.yy");
		}else if(GlobalUtil.global.getPtDateFormat().equals("%m/%d/%y")){
			GlobalUtil.global.setPtDbDateFormat("MM/dd/yy");
			GlobalUtil.global.setPtScrDateFormat("MM/dd/yy");
		}else if(GlobalUtil.global.getPtDateFormat().equals("%m.%d.%y")){
			GlobalUtil.global.setPtDbDateFormat("MM.dd.yy");
			GlobalUtil.global.setPtScrDateFormat("MM.dd.yy");
		}else if(GlobalUtil.global.getPtDateFormat().equals("%d/%m/%Y")){
			GlobalUtil.global.setPtDbDateFormat("dd/MM/yyyy");
			GlobalUtil.global.setPtScrDateFormat("dd/MM/yyyy");
		}else if(GlobalUtil.global.getPtDateFormat().equals("%d.%m.%Y")){
			GlobalUtil.global.setPtDbDateFormat("dd.MM.yyyy");
			GlobalUtil.global.setPtScrDateFormat("dd.MM.yyyy");
		}else if(GlobalUtil.global.getPtDateFormat().equals("%m/%d/%Y")){
			GlobalUtil.global.setPtDbDateFormat("MM/dd/yyyy");
			GlobalUtil.global.setPtScrDateFormat("MM/dd/yyyy");
		}else if(GlobalUtil.global.getPtDateFormat().equals("%m.%d.%Y")){
			GlobalUtil.global.setPtDbDateFormat("MM.dd.yyyy");
			GlobalUtil.global.setPtScrDateFormat("MM.dd.yyyy");
		}
		
		
		
	}
	
}
