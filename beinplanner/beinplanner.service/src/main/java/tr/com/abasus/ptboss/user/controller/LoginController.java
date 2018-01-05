package tr.com.abasus.ptboss.user.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import tr.com.abasus.ptboss.local.dao.GlobalLocalDao;
import tr.com.abasus.ptboss.menu.entity.MenuTbl;
import tr.com.abasus.ptboss.ptuser.entity.User;
import tr.com.abasus.ptboss.ptuser.iuser.ProcessAdmin;
import tr.com.abasus.ptboss.ptuser.iuser.ProcessManager;
import tr.com.abasus.ptboss.ptuser.iuser.ProcessMember;
import tr.com.abasus.ptboss.ptuser.iuser.ProcessSchedulerStaff;
import tr.com.abasus.ptboss.ptuser.iuser.ProcessStaff;
import tr.com.abasus.ptboss.ptuser.iuser.ProcessSuperManager;
import tr.com.abasus.ptboss.ptuser.service.ProcessUserService;
import tr.com.abasus.ptboss.result.entity.HmiResultObj;
import tr.com.abasus.ptboss.settings.entity.PtRestrictions;
import tr.com.abasus.ptboss.settings.entity.PtRules;
import tr.com.abasus.ptboss.settings.entity.Rules;
import tr.com.abasus.ptboss.settings.service.SettingService;
import tr.com.abasus.util.GlobalUtil;
import tr.com.abasus.util.LangUtil;
import tr.com.abasus.util.OhbeUtil;
import tr.com.abasus.util.ResultStatuObj;
import tr.com.abasus.util.SessionUtil;
import tr.com.abasus.util.UserTypes;

@Controller
@RequestMapping(value="/login")
public class LoginController {

	@Autowired
	SettingService settingService;
	
	
	@Autowired 
	ProcessUserService processUserService;
	
	@Autowired
	ProcessAdmin processAdmin;
	
	@Autowired
	ProcessManager processManager;
	
	@Autowired
	ProcessMember processMember;
	
	@Autowired
	ProcessSchedulerStaff processSchedulerStaff;
	
	@Autowired
	ProcessStaff processStaff;
	
	@Autowired
	ProcessSuperManager processSuperManager;
	
	@Autowired
	GlobalLocalDao globalLocalDao;
 
	
	
	@RequestMapping(value="/campain/{userEmail}/{userPassword}/{campainCode}", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE) 
	public @ResponseBody HmiResultObj campain(@PathVariable String userEmail,@PathVariable String userPassword,@PathVariable String campainCode ,HttpServletRequest http){
		HmiResultObj hmiResultObj=new HmiResultObj();
		hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS);
		hmiResultObj.setResultMessage(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
      User user=processUserService.loginUserControl(userEmail, userPassword);
		
		
	    if(user!=null){
		if(campainCode.equals("TR2162")){
			globalLocalDao.updateRestrictionsForCountCampain(2, 0);
			globalLocalDao.updateRestrictionsForPacketCampain("1-1-1");
			
			
			PtRestrictions ptRestrictionsForCount=globalLocalDao.findRestrictionsForCount(OhbeUtil.PTBOSS_MYPASS);
			GlobalUtil.ptRestrictionsForCount=ptRestrictionsForCount;
			
			PtRestrictions ptRestrictionsForPacket=globalLocalDao.findRestrictionsForPacket(OhbeUtil.PTBOSS_MYPASS);
			GlobalUtil.ptRestrictionsForPacket=ptRestrictionsForPacket;
			
			GlobalUtil.setRestrictions();
			
			
		}else if(campainCode.equals("TR1284") || campainCode.equals("AU1284") || campainCode.equals("US1284") || campainCode.equals("EN1284") || campainCode.equals("ES1284")){
		  
			PtRestrictions ptRestrictionsForCount=globalLocalDao.findRestrictionsForCount(OhbeUtil.PTBOSS_MYPASS);
			
			
			
			if(Integer.parseInt(ptRestrictionsForCount.getTeacherCount())==0){
				hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
				hmiResultObj.setResultMessage("noCampain");
			}else if (Integer.parseInt(ptRestrictionsForCount.getTeacherCount())==6 ||Integer.parseInt(ptRestrictionsForCount.getTeacherCount())==8 || Integer.parseInt(ptRestrictionsForCount.getTeacherCount())==20){
				hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
				hmiResultObj.setResultMessage("noCampain");
			}else{
				ptRestrictionsForCount.setTeacherCount(""+(Integer.parseInt(ptRestrictionsForCount.getTeacherCount())+5));
				globalLocalDao.updateRestrictionsForCountCampain(Integer.parseInt(ptRestrictionsForCount.getFirmCount()), Integer.parseInt(ptRestrictionsForCount.getTeacherCount()));
				GlobalUtil.ptRestrictionsForCount=ptRestrictionsForCount;
			}
			
			
			
			
			
			
		}else if(campainCode.equals("TR100649") || campainCode.equals("AU100649") || campainCode.equals("US100649") || campainCode.equals("EN100649") || campainCode.equals("ES100649")){
			  
				PtRestrictions ptRestrictionsForCount=globalLocalDao.findRestrictionsForCount(OhbeUtil.PTBOSS_MYPASS);
				
				if(Integer.parseInt(ptRestrictionsForCount.getFirmCount())==4 || Integer.parseInt(ptRestrictionsForCount.getFirmCount())==3 ){
					hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
					hmiResultObj.setResultMessage("noCampain");
				}else {
					ptRestrictionsForCount.setFirmCount(""+(Integer.parseInt(ptRestrictionsForCount.getFirmCount())+2));
					globalLocalDao.updateRestrictionsForCountCampain(Integer.parseInt(ptRestrictionsForCount.getFirmCount()), Integer.parseInt(ptRestrictionsForCount.getTeacherCount()));
					GlobalUtil.ptRestrictionsForCount=ptRestrictionsForCount;
				}
		
		}else{
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
			hmiResultObj.setResultMessage("noCampain");
		}
	    }else{
	    	hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
			hmiResultObj.setResultMessage("USER_NOT_FOUND");
	    }
		
		return hmiResultObj;
		
	}
	

	@RequestMapping(value="/loginUser1/{userEmail}/{userPassword}", method = RequestMethod.POST) 
	public @ResponseBody HmiResultObj loginUser1(@PathVariable String userEmail,@PathVariable String userPassword ){
		////System.out.println("LOGIN 1 CALLED");
		HmiResultObj hmiResultObj=new HmiResultObj();
		hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
		hmiResultObj.setResultMessage("USER_NOT_FOUND");
		return hmiResultObj;
	}
	
	@RequestMapping(value="/loginUser/{userEmail}/{userPassword}", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE) 
	public @ResponseBody HmiResultObj loginUser(@PathVariable String userEmail,@PathVariable String userPassword ,HttpServletRequest http){
		HmiResultObj hmiResultObj=new HmiResultObj();
		
		http.getSession().removeAttribute(SessionUtil.SESSION_USER);
		
		User user=processUserService.loginUserControl(userEmail, userPassword);
		
		
	    if(user==null){
    		hmiResultObj.setResultMessage(LangUtil.USER_NOT_FOUND);
    		hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
    	}else{
    		List<MenuTbl> menuTopTbls= null;
    		List<MenuTbl> menuSideTbls=null;
    		MenuTbl menuDashboard=null;
    		
    		if(user.getUserType()== UserTypes.USER_TYPE_ADMIN_INT){
    		   menuTopTbls= processAdmin.getMenuTop();
    		   menuSideTbls=processAdmin.getMenuSide();
    		   menuDashboard=processAdmin.getMenuDashBoard();
    		}else if(user.getUserType()==UserTypes.USER_TYPE_MANAGER_INT){
     		   menuTopTbls= processManager.getMenuTop();
     		   menuSideTbls=processManager.getMenuSide();
     		  menuDashboard=processManager.getMenuDashBoard();
     		}else if(user.getUserType()==UserTypes.USER_TYPE_MEMBER_INT){
      		   menuTopTbls= processMember.getMenuTop();
      		   menuSideTbls=processMember.getMenuSide();
      		 menuDashboard=processMember.getMenuDashBoard();
      		}else if(user.getUserType()==UserTypes.USER_TYPE_SCHEDULAR_STAFF_INT){
      		   menuTopTbls= processSchedulerStaff.getMenuTop();
      		   menuSideTbls=processSchedulerStaff.getMenuSide();
      		 menuDashboard=processSchedulerStaff.getMenuDashBoard();
      		}else if(user.getUserType()==UserTypes.USER_TYPE_STAFF_INT){
      		   menuTopTbls= processStaff.getMenuTop();
      		   menuSideTbls=processStaff.getMenuSide();
      		 menuDashboard=processStaff.getMenuDashBoard();
      		}else if(user.getUserType()==UserTypes.USER_TYPE_SUPER_MANAGER_INT){
      		   menuTopTbls= processSuperManager.getMenuTop();
      		   menuSideTbls=processSuperManager.getMenuSide();
      		 menuDashboard=processSuperManager.getMenuDashBoard();
      		}
    		
    		user.setMenuTbls(menuSideTbls);
    		user.setMenuTopTbls(menuTopTbls);
    		user.setDashBoardMenu(menuDashboard);
    		user.setPassword(null);
    		
    		
    		http.getSession().setAttribute(SessionUtil.SESSION_USER, user);
    		
    		hmiResultObj.setResultMessage(LangUtil.LOGIN_SUCCESS);
    		hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS);
    	}
    
	   return hmiResultObj;
	}

	

	
	

	
}
