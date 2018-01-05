package tr.com.abasus.ptboss.ptuser.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import tr.com.abasus.ptboss.exceptions.UnAuthorizedUserException;
import tr.com.abasus.ptboss.facade.payment.PaymentClassFacade;
import tr.com.abasus.ptboss.facade.sale.SaleFacadeService;
import tr.com.abasus.ptboss.mail.MailObj;
import tr.com.abasus.ptboss.mail.MailSenderThread;
import tr.com.abasus.ptboss.mail.facade.MailFacade;
import tr.com.abasus.ptboss.mail.facade.MailService;
import tr.com.abasus.ptboss.packetpayment.entity.PacketPaymentFactory;
import tr.com.abasus.ptboss.packetsale.entity.PacketSaleFactory;
import tr.com.abasus.ptboss.ptuser.entity.User;
import tr.com.abasus.ptboss.ptuser.iuser.ProcessAdmin;
import tr.com.abasus.ptboss.ptuser.iuser.ProcessChain;
import tr.com.abasus.ptboss.ptuser.iuser.ProcessManager;
import tr.com.abasus.ptboss.ptuser.iuser.ProcessMember;
import tr.com.abasus.ptboss.ptuser.iuser.ProcessSchedulerStaff;
import tr.com.abasus.ptboss.ptuser.iuser.ProcessStaff;
import tr.com.abasus.ptboss.ptuser.iuser.ProcessSuperManager;
import tr.com.abasus.ptboss.ptuser.service.ProcessUserService;
import tr.com.abasus.ptboss.result.entity.HmiResultObj;
import tr.com.abasus.ptboss.settings.entity.DbMailTbl;
import tr.com.abasus.ptboss.settings.service.SettingService;
import tr.com.abasus.util.DateTimeUtil;
import tr.com.abasus.util.GlobalUtil;
import tr.com.abasus.util.OhbeUtil;
import tr.com.abasus.util.ProgramTypes;
import tr.com.abasus.util.ResultStatuObj;
import tr.com.abasus.util.RuleUtil;
import tr.com.abasus.util.SessionUtil;
import tr.com.abasus.util.UserTypes;

@Controller
@RequestMapping(value="/ptusers")
public class PtUserController {

	@Autowired
	SettingService settingService;
	
	@Autowired 
	ProcessChain processChain;
	
	
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
	PaymentClassFacade paymentClassFacade;
	
	@Autowired
	@Qualifier(value="saleClassFacade")
	SaleFacadeService saleFacadeService;
	
	
	@Autowired
	MailService mailService;
	
	@RequestMapping(value="/getSessionUser", method = RequestMethod.POST)
	public @ResponseBody User getSessionUser(HttpServletRequest request) throws UnAuthorizedUserException{
		User sessionUser=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if(sessionUser==null){
			throw new UnAuthorizedUserException("User not in session");
		}
		
		return sessionUser;
	}
	
	@RequestMapping(value="/changePassword", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody HmiResultObj changePassword(@RequestBody User user,HttpServletRequest request) throws UnAuthorizedUserException {
		User sessionUser=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if(sessionUser==null){
			throw new UnAuthorizedUserException("User not in session");
		}
		HmiResultObj hmiResultObj=new HmiResultObj();
		hmiResultObj.setResultMessage("undefinedUser");
		hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
		
		User userOld=processUserService.loginUserControl(user.getUserEmail(), user.getOldPassword());
		if(userOld!=null){
			
			userOld.setPassword(user.getPassword());
			processUserService.updateUser(userOld);
			hmiResultObj.setResultMessage("passwordChanged");
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS);
			informByMail(userOld);
		}
		
		return hmiResultObj;
	}
	
	@RequestMapping(value="/resetPassword/{userName}", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody HmiResultObj resetPassword(@PathVariable String userName,HttpServletRequest request) throws UnAuthorizedUserException {
		
		HmiResultObj hmiResultObj=new HmiResultObj();
		hmiResultObj.setResultMessage("resetRequestReceieved");
		hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS);
		
		User user=processUserService.findUserByEMail(userName);
		
		if(user!=null){
		
			if(user.getUserType()==UserTypes.USER_TYPE_ADMIN_INT)
				user.setPassword(getAMIId());
			else{
				int randomPIN = (int)(Math.random()*9000)+1000;
				String password = ""+randomPIN;
				user.setPassword(password);
			}
			
			
			processUserService.updateUser(user);
			hmiResultObj.setResultMessage("resetRequestReceieved");
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS);
			DbMailTbl dbMailTbl= GlobalUtil.mailSettings;
			if(dbMailTbl!=null){
				if(!user.getUserEmail().contains("@")){
					user.setUserEmail(dbMailTbl.getMailUsername());
				}
			informByMail(user);
			}
		}	
			
			
			
		return hmiResultObj;
	}
	
	
	private void informByMail(User user){
		if(user.getUserEmail().contains("@") && OhbeUtil.PLACE==OhbeUtil.AMAZON){
			MailObj mailObj=new MailObj();
			mailObj.setContent(user.getUserName()+" "+user.getUserSurname()+" <br\\>"+" Your Password is "+user.getPassword());
			mailObj.setSubject("PASSWORD RECOVERY");
			mailObj.setToWho(new String[]{user.getUserEmail()});
			mailObj.setToWhom(GlobalUtil.mailSettings.getFromName());
			
			
			MailSenderThread mailSenderThread=new MailSenderThread(mailObj);
			Thread thr=new Thread(mailSenderThread);
			thr.start();
		}
	}
	
	
	
	
	private String getAMIId(){
		Process pr;
		String amiId="";
		String password="";
		if(OhbeUtil.PLACE==OhbeUtil.AMAZON){
			try {
				pr = Runtime.getRuntime().exec("curl http://169.254.169.254/latest/meta-data/instance-id");
				BufferedReader in = new BufferedReader(new InputStreamReader(pr.getInputStream()));
			    
			    while ((amiId = in.readLine()) != null) {
			        password=amiId;
			    }
			    pr.waitFor();
			   // ////System.out.println("ok!");
	
	 
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return password;
}
	
	@RequestMapping(value="/create", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody HmiResultObj create(@RequestBody User user) {
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
			String password = ""+randomPIN;
			user.setPassword(password);
		
		
			if(user.getUserType()== UserTypes.USER_TYPE_ADMIN_INT){
				hmiResultObj=processAdmin.createUser(user);
			}else if(user.getUserType()==UserTypes.USER_TYPE_MANAGER_INT){
				user.setPassword("0000");
				hmiResultObj=processManager.createUser(user);
			}else if(user.getUserType()==UserTypes.USER_TYPE_MEMBER_INT){
				hmiResultObj=processMember.createUser(user);
			}else if(user.getUserType()==UserTypes.USER_TYPE_SCHEDULAR_STAFF_INT){
				user.setPassword("0000");
				hmiResultObj=processSchedulerStaff.createUser(user);
			}else if(user.getUserType()==UserTypes.USER_TYPE_STAFF_INT){
				user.setPassword("0000");
				hmiResultObj=processStaff.createUser(user);
			}else if(user.getUserType()==UserTypes.USER_TYPE_SUPER_MANAGER_INT){
				user.setPassword("0000");
				hmiResultObj=processSuperManager.createUser(user);
			}
		}
		
		return hmiResultObj;
	}
	

	@RequestMapping(value="/update", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE) 
	public @ResponseBody HmiResultObj update(@RequestBody User user){
		return processUserService.updateUser(user);
	}
	
	
	
	@RequestMapping(value="/forgotPassword", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE) 
	public @ResponseBody HmiResultObj forgotPassword(@RequestBody MailObj mailObj) throws IOException, MessagingException{
	
		HmiResultObj hmiResultObj=new HmiResultObj();
		User user= processUserService.findUserByEMail(mailObj.getToPerson());
		if(user==null){
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
			hmiResultObj.setResultMessage("noUserFound");
		}else{
			
			String content=mailObj.getContent();
			content=content.replaceAll("xxxxx", user.getUserName()+" "+user.getUserSurname());
			
			String htmlContent="<strong>"+user.getPassword()+"</strong>";
			
			mailObj.setContent(content);
			mailObj.setHtmlContent(htmlContent);
			
			String[] toWho=new String[]{mailObj.getToPerson()};
			
			
			mailObj.setToWho(toWho);
			mailObj.setToWhom(GlobalUtil.mailSettings.getFromName());
			
			
			
			hmiResultObj=mailService.sendMailForPasswordReminder(mailObj);
			
		}
		
		return hmiResultObj;
	}
	
	@RequestMapping(value="/updateProfile", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE) 
	public @ResponseBody HmiResultObj updateProfile(@RequestBody User user){
		
		User userInDb= processUserService.findById(user.getUserId());
		
		userInDb.setCityId(user.getCityId());
		userInDb.setFirmId(user.getFirmId());
		userInDb.setUserName(user.getUserName());
		userInDb.setUserSurname(user.getUserSurname());
		userInDb.setUserBirthday(DateTimeUtil.getThatDayFormatNotNull(user.getUserBirthdayStr(), GlobalUtil.global.getPtScrDateFormat()));
		userInDb.setStateId(user.getStateId());
		userInDb.setUserAddress(user.getUserAddress());
		userInDb.setUserPhone(user.getUserPhone());
		userInDb.setUserGsm(user.getUserGsm());
		userInDb.setUserEmail(user.getUserEmail());
		userInDb.setUserGender(user.getUserGender());
		userInDb.setUserComment(user.getUserComment());
		userInDb.setStaffId(user.getStaffId());
		
	
		
		return processUserService.updateUser(userInDb);
	}
	
	
	
	
	
	
	@RequestMapping(value="/delete", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE) 
	public @ResponseBody HmiResultObj delete(@RequestBody User user){
		HmiResultObj hmiResultObj=new HmiResultObj();
		
		if(user.getUserType()== UserTypes.USER_TYPE_ADMIN_INT){
			hmiResultObj.setResultMessage("canNotDeleteAdmin");
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
		}else if(user.getUserType()==UserTypes.USER_TYPE_MANAGER_INT){
			hmiResultObj= processManager.deleteUser(user);
  		}else if(user.getUserType()==UserTypes.USER_TYPE_MEMBER_INT){
  			hmiResultObj= processMember.deleteUser(user);
   		}else if(user.getUserType()==UserTypes.USER_TYPE_SCHEDULAR_STAFF_INT){
   			hmiResultObj= processSchedulerStaff.deleteUser(user);
   		}else if(user.getUserType()==UserTypes.USER_TYPE_STAFF_INT){
   			hmiResultObj= processStaff.deleteUser(user);
   		}else if(user.getUserType()==UserTypes.USER_TYPE_SUPER_MANAGER_INT){
   			hmiResultObj= processSuperManager.deleteUser(user);
   		}
		
		return hmiResultObj;
	}
	

	
	@RequestMapping(value="/findByUserNameForSales/{progType}", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE) 
	public @ResponseBody List<User> findByUserNameForSales(@RequestBody User user,@PathVariable int progType,HttpServletRequest request){
	
		User sessionUser=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if(sessionUser==null){
			throw new UsernameNotFoundException("");
		}
		
		List<User> users=null;
		List<User> usersHasNoSale=null;
		List<User> usersNew=new ArrayList<User>();
		
		int firmId=sessionUser.getFirmId();
		if(user.getUserType()==UserTypes.USER_TYPE_MEMBER_INT){
  			users= processMember.findByUserNameForSales(user.getUserName()+"%", user.getUserSurname()+"%",progType,firmId);
  			
  			////System.out.println("GlobalUtil.rules.getRuleNoSaleToPlanning() "+GlobalUtil.rules.getRuleNoSaleToPlanning());
  			
  			if(GlobalUtil.rules.getRuleNoSaleToPlanning()==RuleUtil.RULE_OK){
  			usersHasNoSale=processMember.findByNameAndSurname(user.getUserName()+"%", user.getUserSurname()+"%", firmId);
			
  			
  			for (User userL : usersHasNoSale) {
				boolean found=false;
				if(progType==ProgramTypes.PROGRAM_CLASS){
					userL.setType("sucp");
				}else if(progType==ProgramTypes.PROGRAM_PERSONAL){
					userL.setType("supp");
				}
				for (User userHNS : users) {
						if(userHNS.getUserId()==userL.getUserId()){
							found=true;
							break;
						}
				}
				
				if(!found){
					usersNew.add(userL);
				}
				
			}
  			
  			users.addAll(usersNew);
  			}
   		}
		return users;
	}
	
	
	@RequestMapping(value="/findByUserNameAndSaleProgram/{progType}/{progId}/{schId}", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE) 
	public @ResponseBody List<User> findByUserNameAndSaleProgram(@RequestBody User user,@PathVariable int progType,@PathVariable long progId,@PathVariable long schId,HttpServletRequest request){
		User sessionUser=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if(sessionUser==null){
			throw new UsernameNotFoundException("");
		}
		
		List<User> users=null;
		List<User> usersHasNoSale=null;
		List<User> usersNew=new ArrayList<User>();
		
		int firmId=sessionUser.getFirmId();
		if(user.getUserType()==UserTypes.USER_TYPE_MEMBER_INT){
  			users= processMember.findByNameAndSaleProgramWithNoPlan(user.getUserName()+"%", user.getUserSurname()+"%", progId, progType,firmId,schId);
  			
  			
  			if(GlobalUtil.rules.getRuleNoSaleToPlanning()==RuleUtil.RULE_OK){
	  			usersHasNoSale=processMember.findByNameAndSurname(user.getUserName()+"%", user.getUserSurname()+"%", firmId);
				
	  			for (User userL : usersHasNoSale) {
					boolean found=false;
					if(progType==ProgramTypes.PROGRAM_CLASS){
						userL.setType("sucp");
					}else if(progType==ProgramTypes.PROGRAM_PERSONAL){
						userL.setType("supp");
					}
					
					
					for (User userHNS : users) {
							if(userHNS.getUserId()==userL.getUserId()){
								found=true;
								break;
							}
					}
					if(!found){
						usersNew.add(userL);
					}
				}
	  			users.addAll(usersNew);
  			}
   		}
		return users;
	}
	
	
	@RequestMapping(value="/findByUserNameAndSaleProgramWithPlan/{progType}/{progId}/{schId}", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE) 
	public @ResponseBody List<User> findByUserNameAndSaleProgramWithPlan(@RequestBody User user,@PathVariable int progType,@PathVariable long progId,@PathVariable long schId,HttpServletRequest request){
		User sessionUser=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if(sessionUser==null){
			throw new UsernameNotFoundException("");
		}
		
		List<User> users=null;
		List<User> usersHasNoSale=null;
		List<User> usersNew=new ArrayList<User>();
		
		
		int firmId=sessionUser.getFirmId();
		if(user.getUserType()==UserTypes.USER_TYPE_MEMBER_INT){
  			users= processMember.findByNameAndSaleProgramWithPlan(user.getUserName()+"%", user.getUserSurname()+"%", progId, progType,firmId,schId);
  			
  			if(GlobalUtil.rules.getRuleNoSaleToPlanning()==RuleUtil.RULE_OK){
	  			usersHasNoSale=processMember.findByNameAndSurname(user.getUserName()+"%", user.getUserSurname()+"%", firmId);
				
	  			for (User userL : usersHasNoSale) {
					boolean found=false;
					if(progType==ProgramTypes.PROGRAM_CLASS){
						userL.setType("sucp");
					}else if(progType==ProgramTypes.PROGRAM_PERSONAL){
						userL.setType("supp");
					}
					for (User userHNS : users) {
							if(userHNS.getUserId()==userL.getUserId()){
								found=true;
								break;
							}
					}
					if(!found){
						usersNew.add(userL);
					}
				}
	  			users.addAll(usersNew);
  			}
  			
   		}
		return users;
	}
	
	
	@RequestMapping(value="/findUsersInSameSchedulePlanBySaleId/{progType}/{saleId}", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE) 
	public @ResponseBody List<User> findUsersInSameSchedulePlanBySaleId(@PathVariable int progType,@PathVariable long saleId,HttpServletRequest request){
		User sessionUser=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if(sessionUser==null){
			throw new UsernameNotFoundException("");
		}
		
		List<User> users=null;
		users= processMember.findUsersInSameSchedulePlanBySaleId(saleId, progType);
  		return users;
	}
	

	@RequestMapping(value="/specialDates", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE) 
	public @ResponseBody List<User> specialDates(HttpServletRequest request){
		User sessionUser=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if(sessionUser==null){
			throw new UsernameNotFoundException("");
		}
		
			int firmId=sessionUser.getFirmId();
			List<User>  users= processUserService.findSpecialDates(firmId);
  			
   		
		return users;
	}
	
	/**
	 * @author Bahadir
	 * @param member (userName,userSurname attribute'ları ile araştırma yapılır.
	 * @return List<Member>
	 */
	@RequestMapping(value="/findByUserNameAndSurname", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE) 
	public @ResponseBody List<User> findByUserNameAndSurname(@RequestBody User user,HttpServletRequest request){
		User sessionUser=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		
		if(sessionUser==null){
			throw new UsernameNotFoundException("");
		}
		int firmId=sessionUser.getFirmId();
		if(sessionUser.getUserType()==UserTypes.USER_TYPE_ADMIN_INT){
			firmId=0;
		}
		List<User> users=null;
		
		if(user.getUserType()==UserTypes.USER_TYPE_MANAGER_INT){
			users= processManager.findByNameAndSurname(user.getUserName()+"%", user.getUserSurname()+"%",firmId);
  		}else if(user.getUserType()==UserTypes.USER_TYPE_MEMBER_INT){
  			users= processMember.findByNameAndSurname(user.getUserName()+"%", user.getUserSurname()+"%",firmId);
   		}else if(user.getUserType()==UserTypes.USER_TYPE_SCHEDULAR_STAFF_INT){
   			users= processSchedulerStaff.findByNameAndSurname(user.getUserName()+"%", user.getUserSurname()+"%",firmId);
   		}else if(user.getUserType()==UserTypes.USER_TYPE_STAFF_INT){
   			users= processStaff.findByNameAndSurname(user.getUserName()+"%", user.getUserSurname()+"%",firmId);
   		}else if(user.getUserType()==UserTypes.USER_TYPE_SUPER_MANAGER_INT){
   			users= processSuperManager.findByNameAndSurname(user.getUserName()+"%", user.getUserSurname()+"%",firmId);
   		}else{
   			users=processChain.findByUserNameAndSurnameInChain(user.getUserName()+"%", user.getUserSurname()+"%",firmId);
   		}
		
		
		return users;
	}
	
	/**
	 * @author Bahadir
	 * @param member (userId attribute ile member bulunur.
	 * @return List<Member>
	 */
	@RequestMapping(value="/findById/{userId}", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE) 
	public @ResponseBody User findById(@PathVariable("userId") long userId){
		User user=processUserService.findById(userId);
		user.setPassword(null);
		return user;
	}
	
	@RequestMapping(value="/findAllWithPassive/{firmId}/{userType}", method = RequestMethod.POST) 
	public @ResponseBody List<User> findAllWithPassive(@PathVariable("firmId") int firmId,@PathVariable("userType") int userType,HttpServletRequest request){
		User sessionUser=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		
		if(sessionUser==null){
			throw new UsernameNotFoundException("");
		}
		
		if(firmId==0){
			if(sessionUser.getUserType()==UserTypes.USER_TYPE_ADMIN_INT){
				firmId=0;
			}else{
				firmId=sessionUser.getFirmId();
			}
		}
		
		List<User> users=null;
		
		if(userType==UserTypes.USER_TYPE_MANAGER_INT){
			users= processManager.findAll(firmId);
  		}else if(userType==UserTypes.USER_TYPE_MEMBER_INT){
  			users= processMember.findAll(firmId);
   		}else if(userType==UserTypes.USER_TYPE_SCHEDULAR_STAFF_INT){
   			users= processSchedulerStaff.findAll(firmId);
   		}else if(userType==UserTypes.USER_TYPE_STAFF_INT){
   			users= processStaff.findAll(firmId);
   		}else if(userType==UserTypes.USER_TYPE_SUPER_MANAGER_INT){
   			users= processSuperManager.findAll(firmId);
   		}else{
   			users=processChain.findAllInChain(firmId);
   		}
		
		return users;
	}
	
	@RequestMapping(value="/findAll/{firmId}/{userType}", method = RequestMethod.POST) 
	public @ResponseBody List<User> findAll(@PathVariable("firmId") int firmId,@PathVariable("userType") int userType,HttpServletRequest request){
		User sessionUser=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		
		if(sessionUser==null){
			throw new UsernameNotFoundException("");
		}
		
		if(firmId==0){
			if(sessionUser.getUserType()==UserTypes.USER_TYPE_ADMIN_INT){
				firmId=0;
			}else{
				firmId=sessionUser.getFirmId();
			}
		}
		
		List<User> users=null;
		
		if(userType==UserTypes.USER_TYPE_MANAGER_INT){
			users= processManager.findAll(firmId);
  		}else if(userType==UserTypes.USER_TYPE_MEMBER_INT){
  			users= processMember.findAll(firmId);
   		}else if(userType==UserTypes.USER_TYPE_SCHEDULAR_STAFF_INT){
   			users= processSchedulerStaff.findAllToSchedulerStaffForCalendar(firmId);
   		}else if(userType==UserTypes.USER_TYPE_STAFF_INT){
   			users= processStaff.findAll(firmId);
   		}else if(userType==UserTypes.USER_TYPE_SUPER_MANAGER_INT){
   			users= processSuperManager.findAll(firmId);
   		}else{
   			users=processChain.findAllInChain(firmId);
   		}
		
		return users;
	}
	
	
	
	@RequestMapping(value="/findAllToMemberWithNoLimit/{firmId}", method = RequestMethod.POST) 
	public @ResponseBody  List<User> findAllToMemberWithNoLimit(@PathVariable("firmId") int firmId,HttpServletRequest request){
          User sessionUser=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		
          
		if(sessionUser==null){
			throw new UsernameNotFoundException("");
		}
		
		if(firmId==0){
			if(sessionUser.getUserType()==UserTypes.USER_TYPE_ADMIN_INT){
				firmId=0;
			}else{
				firmId=sessionUser.getFirmId();
			}
		}
		
		List<User> users=processUserService.findAllToMemberWithNoLimit(firmId);
		 for (User user : users) {
				user.setPassword("");
			
				
				List<PacketSaleFactory> packetSaleFactories=saleFacadeService.getPacketSalesToUserId(user.getUserId());
				user.setPacketCount(packetSaleFactories.size());
				double packetAmount=0;
				for (PacketSaleFactory packetSaleFactory : packetSaleFactories) {
					packetAmount+=packetSaleFactory.getPacketPrice();
				}
				
				
				if(user.getPacketCount()>0){
					List<PacketPaymentFactory> packetPaymentFactories= paymentClassFacade.getPaymentsToUserId(user.getUserId());
					double leftPayment=0;
					double payAmount=0;
					for (PacketPaymentFactory packetPaymentFactory : packetPaymentFactories) {
						payAmount+=packetPaymentFactory.getPayAmount();
					}
					leftPayment=packetAmount-payAmount;
					user.setLeftPayment(leftPayment);
					user.setPayAmount(payAmount);
					
				}else{
					user.setLeftPayment(0);
					user.setPayAmount(0);
				}
			}
			
		 
		 
		return users;
	}
	
	
	@RequestMapping(value="/setStaffToPassiveMode/{userId}", method = RequestMethod.POST) 
	public @ResponseBody HmiResultObj setStaffToPassiveMode(@PathVariable("userId") long userId){
		User user=processUserService.findById(userId);
		user.setStaffStatu(1);
		return processUserService.updateUser(user);
	}
	
	@RequestMapping(value="/setStaffToActiveMode/{userId}", method = RequestMethod.POST) 
	public @ResponseBody HmiResultObj setStaffToActiveMode(@PathVariable("userId") long userId){
		User user=processUserService.findById(userId);
		user.setStaffStatu(0);
		return processUserService.updateUser(user);
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
	
	public SettingService getSettingService() {
		return settingService;
	}


	public void setSettingService(SettingService settingService) {
		this.settingService = settingService;
	}


	public ProcessChain getProcessChain() {
		return processChain;
	}


	public void setProcessChain(ProcessChain processChain) {
		this.processChain = processChain;
	}


	public ProcessUserService getProcessUserService() {
		return processUserService;
	}


	public void setProcessUserService(ProcessUserService processUserService) {
		this.processUserService = processUserService;
	}


	public ProcessAdmin getProcessAdmin() {
		return processAdmin;
	}


	public void setProcessAdmin(ProcessAdmin processAdmin) {
		this.processAdmin = processAdmin;
	}


	public ProcessManager getProcessManager() {
		return processManager;
	}


	public void setProcessManager(ProcessManager processManager) {
		this.processManager = processManager;
	}


	public ProcessMember getProcessMember() {
		return processMember;
	}


	public void setProcessMember(ProcessMember processMember) {
		this.processMember = processMember;
	}


	public ProcessSchedulerStaff getProcessSchedulerStaff() {
		return processSchedulerStaff;
	}


	public void setProcessSchedulerStaff(ProcessSchedulerStaff processSchedulerStaff) {
		this.processSchedulerStaff = processSchedulerStaff;
	}


	public ProcessStaff getProcessStaff() {
		return processStaff;
	}


	public void setProcessStaff(ProcessStaff processStaff) {
		this.processStaff = processStaff;
	}


	public ProcessSuperManager getProcessSuperManager() {
		return processSuperManager;
	}


	public void setProcessSuperManager(ProcessSuperManager processSuperManager) {
		this.processSuperManager = processSuperManager;
	}
	
	
	
}
