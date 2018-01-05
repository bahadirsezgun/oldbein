package tr.com.abasus.ptboss.local;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tr.com.abasus.ptboss.definition.entity.DefCalendarTimes;
import tr.com.abasus.ptboss.definition.entity.DefFirm;
import tr.com.abasus.ptboss.definition.service.DefinitionService;
import tr.com.abasus.ptboss.local.dao.GlobalLocalDao;
import tr.com.abasus.ptboss.local.entity.DbHostTbl;
import tr.com.abasus.ptboss.local.service.GlobalLocalService;
import tr.com.abasus.ptboss.menu.entity.MenuRoleTbl;
import tr.com.abasus.ptboss.menu.entity.MenuTbl;
import tr.com.abasus.ptboss.menu.service.MenuService;
import tr.com.abasus.ptboss.ptuser.entity.User;
import tr.com.abasus.ptboss.ptuser.service.ProcessUserService;
import tr.com.abasus.ptboss.result.entity.HmiResultObj;
import tr.com.abasus.ptboss.settings.entity.DbMailTbl;
import tr.com.abasus.ptboss.settings.entity.PtRestrictions;
import tr.com.abasus.ptboss.settings.entity.PtRules;
import tr.com.abasus.ptboss.settings.entity.Rules;
import tr.com.abasus.ptboss.settings.service.SettingService;
import tr.com.abasus.util.BonusLockUtil;
import tr.com.abasus.util.GlobalUtil;
import tr.com.abasus.util.LangUtil;
import tr.com.abasus.util.OhbeUtil;
import tr.com.abasus.util.RuleUtil;

@Service
public class StartUp  {

	@Autowired
	BasicDataSource mysql;
	
	@Autowired
	SettingService settingService;
	
	@Autowired
	DefinitionService definitionService;
	
	
	@Autowired
	GlobalLocalDao globalLocalDao;

	@Autowired
	GlobalLocalService globalLocalService;

	
	@Autowired
	ProcessUserService processUserService;
	
	@Autowired
	MenuService menuService;
	
	
	public GlobalLocalDao getGlobalLocalDao() {
		return globalLocalDao;
	}

	public void setGlobalLocalDao(GlobalLocalDao globalLocalDao) {
		this.globalLocalDao = globalLocalDao;
	}
	
	@PostConstruct
	public void startUp() {
   
        DbHostTbl dbHostTbl=globalLocalDao.findDbHost();
        
        //////System.out.println(dbHostTbl.getDbUrl()+" "+dbHostTbl.getDbPassword());
        String url="jdbc:mysql://"+dbHostTbl.getDbUrl()+":"+dbHostTbl.getDbPort()+"/ptboss?useOldAliasMetadataBehavior=true&useUnicode=true&characterEncoding=utf8";
		
        
        mysql.setUrl(url);
        mysql.setUsername(dbHostTbl.getDbUsername());
        mysql.setPassword(dbHostTbl.getDbPassword());
        
		try {
			Connection con= mysql.getConnection();
			if(con!=null){
				
				
				DbMailTbl dbMailTbl=settingService.findDbMailTbl();
				if(dbMailTbl!=null){
					GlobalUtil.mailSettings=dbMailTbl;
				}
				
				createGlobalUtils();
				createDefaults();
				setDefaultPasswordForAdmin();
			}
			
			
			
			
		} catch (SQLException e) {
			
			DbHostTbl dbHostTblLocal=new DbHostTbl();
			if(OhbeUtil.PLACE==OhbeUtil.AMAZON)
				dbHostTblLocal.setDbPassword("sagali7611");
			else 
				dbHostTblLocal.setDbPassword("system");
			
			dbHostTblLocal.setDbPort(3306);
			dbHostTblLocal.setDbUrl("localhost");
			dbHostTblLocal.setDbUsername("root");
			
			globalLocalDao.createDbHost(dbHostTblLocal);
			createGlobalUtils();
			createDefaults();
			setDefaultPasswordForAdmin();
			////System.out.println("VERITABANI BAGLANTISI KURULAMADI. ORGINALE TEKRAR DONDURULDU");
			
		}
    }

	
	private void setDefaultPasswordForAdmin(){
		User user=processUserService.findAdmin();
		if(user!=null){
			String defaultPassword=getAMIId();
			user.setPassword(defaultPassword);
			processUserService.updateUser(user);
		}
		
		
	}
	
	 private String getAMIId(){
		 String password="";
		 if(OhbeUtil.PLACE==OhbeUtil.AMAZON){
		    Process pr;
			String amiId="";
			
			try {
				pr = Runtime.getRuntime().exec("curl http://169.254.169.254/latest/meta-data/instance-id");
				BufferedReader in = new BufferedReader(new InputStreamReader(pr.getInputStream()));
			    
			    while ((amiId = in.readLine()) != null) {
			        ////System.out.println("LINUX AMI ID IS "+amiId);
			        password=amiId;
			    }
			    pr.waitFor();
			   // ////System.out.println("ok!");

	 
			} catch (Exception e) {
				e.printStackTrace();
			}
		 }else{
			 password="1"; 
		 }
			return password;
	}
	
	
	public void createGlobalUtils(){
		if(GlobalUtil.global==null){
		
			DefCalendarTimes defCalendarTimes=definitionService.findCalendarTimes();
			if(defCalendarTimes==null){
				defCalendarTimes=new DefCalendarTimes();
				defCalendarTimes.setDuration(60);
				defCalendarTimes.setCalPeriod(30);
				defCalendarTimes.setStartTime("06:00");
				defCalendarTimes.setEndTime("24:00");
			}
			
			GlobalUtil.defCalendarTimes=defCalendarTimes;
			
			
		////System.out.println("GLOBAL UTILS CREATED");
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
		
		
		List<PtRules> ruleList= settingService.findPtRules();
		Rules rules=new Rules();
		for (PtRules ptRules : ruleList) {
			if(ptRules.getRuleId()==RuleUtil.ruleLocation){
				rules.setRuleLocation(ptRules.getRuleValue());
			}else if(ptRules.getRuleId()==RuleUtil.ruleNoChangeAfterBonusPayment){
				rules.setRuleNoChangeAfterBonusPayment(ptRules.getRuleValue());
				
				if(ptRules.getRuleValue()==RuleUtil.RULE_OK){
					BonusLockUtil.BONUS_LOCK_FLAG=true;
				}else{
					BonusLockUtil.BONUS_LOCK_FLAG=false;
				}
				
				
			}else if(ptRules.getRuleId()==RuleUtil.ruleNoClassBeforePayment){
				rules.setRuleNoClassBeforePayment(ptRules.getRuleValue());
			}else if(ptRules.getRuleId()==RuleUtil.rulePayBonusForConfirmedPayment){
				rules.setRulePayBonusForConfirmedPayment(ptRules.getRuleValue());
			//}else if(ptRules.getRuleId()==RuleUtil.ruleTaxRule){
				//rules.setRuleTaxRule(ptRules.getRuleValue());
			}else if(ptRules.getRuleId()==RuleUtil.ruleCreditCardCommission){
				rules.setCreditCardCommission(ptRules.getRuleValue());
			}else if(ptRules.getRuleId()==RuleUtil.ruleCreditCardCommissionRate){
				rules.setCreditCardCommissionRate(ptRules.getRuleValue());
			}else if(ptRules.getRuleId()==RuleUtil.ruleNoSaleToPlanning){
				rules.setRuleNoSaleToPlanning(ptRules.getRuleValue());
			}else if(ptRules.getRuleId()==RuleUtil.ruleNotice){
				rules.setRuleNotice(ptRules.getRuleValue());
			}
		}
		GlobalUtil.rules=rules;
		
		
		PtRestrictions ptRestrictionsForCount=globalLocalService.findRestrictionsForCount(OhbeUtil.PTBOSS_MYPASS);
		GlobalUtil.ptRestrictionsForCount=ptRestrictionsForCount;
		
		PtRestrictions ptRestrictionsForPacket=globalLocalService.findRestrictionsForPacket(OhbeUtil.PTBOSS_MYPASS);
		GlobalUtil.ptRestrictionsForPacket=ptRestrictionsForPacket;
		
		GlobalUtil.setRestrictions();
		
		
		List<DefFirm> defFirms=definitionService.findFirms();
		if(GlobalUtil.defFirms==null){
			GlobalUtil.defFirms=new HashMap<Integer, DefFirm>();
		}
		for (DefFirm defFirm : defFirms) {
			GlobalUtil.defFirms.put(defFirm.getFirmId(), defFirm);
		}
		
		
		
		}
	}
	
	
	private boolean createDefaults(){
	
		System.out.println("CREATE DEFAULTS WORKS");
		if(!menuService.findMenuByName("calDefTimes%")){
			System.out.println("MENU NOT FOUND AND START TO CREATE");
		MenuTbl menuTbl=new MenuTbl();
		menuTbl.setMenuId(405);
		menuTbl.setMenuName("calDefTimes");
		menuTbl.setUpperMenu(400);
		menuTbl.setMenuLink("#/calDefTimes");
		menuTbl.setMenuComment("Takvim Saatleri");
		menuTbl.setMenuSide(0);
		menuTbl.setMenuClass("");
		
		HmiResultObj hmiResultObj= menuService.createMenu(menuTbl);
		
		////System.out.println("CREATE DEFAULTS WORKS SET MENU TBL ");
		
		MenuRoleTbl menuRoleTbl=new MenuRoleTbl();
		
		menuRoleTbl.setMenuId(405);
		menuRoleTbl.setRoleId(6);
		menuRoleTbl.setRoleName("ADMIN");
		
		menuService.createMenuRole(menuRoleTbl);
		}
		
		
		return true;
	}
	
	
	public BasicDataSource getMysql() {
		return mysql;
	}

	public void setMysql(BasicDataSource mysql) {
		this.mysql = mysql;
	}
	
}
