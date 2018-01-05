package tr.com.abasus.ptboss.mobile.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import tr.com.abasus.ptboss.definition.entity.DefStudio;
import tr.com.abasus.ptboss.definition.service.DefinitionService;
import tr.com.abasus.ptboss.exceptions.UnAuthorizedUserException;
import tr.com.abasus.ptboss.local.dao.GlobalLocalDao;
import tr.com.abasus.ptboss.program.entity.ProgramFactory;
import tr.com.abasus.ptboss.program.service.ProgramService;
import tr.com.abasus.ptboss.ptuser.entity.User;
import tr.com.abasus.ptboss.ptuser.iuser.ProcessMember;
import tr.com.abasus.ptboss.ptuser.service.ProcessUserService;
import tr.com.abasus.ptboss.result.entity.HmiResultObj;
import tr.com.abasus.ptboss.settings.entity.PtRestrictions;
import tr.com.abasus.util.GlobalUtil;
import tr.com.abasus.util.OhbeUtil;
import tr.com.abasus.util.ResultStatuObj;
import tr.com.abasus.util.UserTypes;

@Controller
@RequestMapping(value="/mobile/login")
public class MobileUserController {


	@Autowired 
	ProcessUserService processUserService;
	
	@Autowired
	ProcessMember processMember;
	
	@Autowired
	GlobalLocalDao globalLocalDao;
	
	@Autowired
	ProgramService programService;
	
	@Autowired
	DefinitionService definitionService;
	
	
	@RequestMapping(value="/loginUser/{userEmail}/{userPassword}", method = RequestMethod.POST) 
	public @ResponseBody User loginUser(@PathVariable String userEmail,@PathVariable String userPassword ){
		User user=processUserService.loginUserControl(userEmail, userPassword);
		PtRestrictions ptRestrictionsForPacket=globalLocalDao.findRestrictionsForPacket(OhbeUtil.PTBOSS_MYPASS);
		
		String[] rstrs=ptRestrictionsForPacket.getFirmCount().split("-");
		
		if(rstrs[0].equals("1")){
			ptRestrictionsForPacket.setIndividualRestriction(1);
		}else{
			ptRestrictionsForPacket.setIndividualRestriction(0);
		}
		
		
		if(rstrs[1].equals("1")){
			ptRestrictionsForPacket.setGroupRestriction(1);
		}else{
			ptRestrictionsForPacket.setGroupRestriction(0);
		}
		
		
		if(rstrs[2].equals("1")){
			ptRestrictionsForPacket.setMembershipRestriction(1);
		}else{
			ptRestrictionsForPacket.setMembershipRestriction(0);
		}
		
		
		user.setGroupRestriction(ptRestrictionsForPacket.getGroupRestriction());
		user.setIndividualRestriction(ptRestrictionsForPacket.getIndividualRestriction());
		user.setMembershipRestriction(ptRestrictionsForPacket.getMembershipRestriction());
		
		user.setPtCurrency(GlobalUtil.global.getPtCurrency());
		
		
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
			user.setUniquePacket(1);
        }else{
        	
        	 List<ProgramFactory> classprog=programService.findAllProgramClass(user.getFirmId());
      	   
      	   List<ProgramFactory> personalprog=programService.findAllProgramPersonal(user.getFirmId());
      	   
      	   List<ProgramFactory> membershipprog=programService.findAllProgramMembership(user.getFirmId());
      	   
      	 int uniqueCount=0;
  	   
  	   
	  	   List<DefStudio> studios= definitionService.findAllStudios(user.getFirmId());
	  	   
	  	   if(studios.size()>0){
	  		   user.setStudioDefined(1);
	  	   }else{
	  		   user.setStudioDefined(0);
	  	   }
	  	   
	  	   
	  	   if(personalprog.size()>0){
	  		   uniqueCount++; 
	  		   user.setWhichPacket(1);
	  	   }
	  		   
	  	   if(classprog.size()>0){   
	  		   uniqueCount++; 
	  		 user.setWhichPacket(2);
	  	   }
	  	   
	  	   if(membershipprog.size()>0){
	  		   uniqueCount++; 
	  		   user.setWhichPacket(3);
	  	   }
	  	   
	  	   if(uniqueCount>1){
	  		 user.setUniquePacket(2);
	  	   }else{
	  		 user.setUniquePacket(1);
	  	   }
	  	   
	  	   
	  	 
        	
        }
		
		user.setDateFormat(GlobalUtil.global.getPtScrDateFormat());
		
		
		
		
		
		////System.out.println("REQUEST COME .." +user.getUserName()+"  "+userEmail+"  "+userPassword);
		if(user!=null){
    		user.setPassword("");
    	}
		return user;
	}
	
	@RequestMapping(value="/find/{userName}/{password}", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody List<User> findUserByName(@PathVariable String userName,@PathVariable String password,@RequestBody User userIn) throws UnAuthorizedUserException {

		User user=processUserService.loginUserControl(userName, password);
		if(user==null){
			throw new UnAuthorizedUserException("");
    	}else if((user.getUserType()==UserTypes.USER_TYPE_MEMBER_INT)){
			throw new UnAuthorizedUserException("");
		}
		
		List<User> users=processMember.findByNameAndSurname(userIn.getUserName(), userIn.getUserSurname(), user.getFirmId());
		for (User user2 : users) {
			if(user2.getUserGender()==0){
				user2.setProfileUrl("male.png");
			}else{
				user2.setProfileUrl("female.png");
			}
		}
		
		return users;
	}
	
	@RequestMapping(value="/create/{userName}/{password}", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody HmiResultObj create(@RequestBody User user,@PathVariable String userName,@PathVariable String password) throws UnAuthorizedUserException {
		
		User userL=processUserService.loginUserControl(userName, password);
		if(userL==null){
			throw new UnAuthorizedUserException("");
    	}else if((userL.getUserType()==UserTypes.USER_TYPE_MEMBER_INT)){
			throw new UnAuthorizedUserException("");
		}
		
		HmiResultObj hmiResultObj=new HmiResultObj();
		
		user.setUserBirthday(OhbeUtil.getThatDayFormatNotNull(user.getUserBirthdayStr(), GlobalUtil.global.getPtDbDateFormat()));
		User userIn=null;
		if(user.getUserId()!=0){
			userIn=processUserService.findById(user.getUserId());
		}else{
			
			if(user.getUserEmail().equals("")){
				user.setUserEmail(generateUserName(user));
			}
			
			
			userIn=processUserService.findUserByEMail(user.getUserEmail());
		    if(userIn!=null){
		    	hmiResultObj.setResultMessage("userFoundWithThisEmail");
		    	hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
		    	return hmiResultObj;
		    }
			
		}
		
		if(userIn!=null){
			user.setUserId(userIn.getUserId());
			user.setProfileUrl(userIn.getProfileUrl());
			user.setUrlType(userIn.getUrlType());
			user.setPassword(userIn.getPassword());
			hmiResultObj=processUserService.updateUser(user);
		}else{
			int randomPIN = (int)(Math.random()*9000)+1000;
			String passwordR = ""+randomPIN;
			user.setPassword(passwordR);
		
			user.setFirmId(userL.getFirmId());
		
			hmiResultObj=processMember.createUser(user);
			
			
		}
		
		return hmiResultObj;
	}
	
	private String generateUserName(User user){
		String userName=(user.getUserName().substring(0,1)).toLowerCase()+user.getUserSurname().toLowerCase();
		
		userName=userName.replaceAll("ö", "o");
		userName=userName.replaceAll("ı", "i");
		userName=userName.replaceAll("ü", "u");
		userName=userName.replaceAll("ş", "s");
		userName=userName.replaceAll("ğ", "g");
		userName=userName.replaceAll("ç", "c");
		
		
		userName=userName.replaceAll("Ö", "o");
		userName=userName.replaceAll("İ", "i");
		userName=userName.replaceAll("Ü", "u");
		userName=userName.replaceAll("Ş", "s");
		userName=userName.replaceAll("Ğ", "g");
		userName=userName.replaceAll("Ç", "c");
		
		return userName;
	}
	
}
