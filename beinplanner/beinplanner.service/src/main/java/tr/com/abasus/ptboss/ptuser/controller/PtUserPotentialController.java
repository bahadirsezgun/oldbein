package tr.com.abasus.ptboss.ptuser.controller;

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

import tr.com.abasus.ptboss.exceptions.UnAuthorizedUserException;
import tr.com.abasus.ptboss.mail.MailObj;
import tr.com.abasus.ptboss.mail.MailSenderThread;
import tr.com.abasus.ptboss.mail.facade.MailFacade;
import tr.com.abasus.ptboss.ptuser.entity.User;
import tr.com.abasus.ptboss.ptuser.entity.UserPotential;
import tr.com.abasus.ptboss.ptuser.iuser.ProcessSuperManager;
import tr.com.abasus.ptboss.ptuser.service.ProcessUserService;
import tr.com.abasus.ptboss.result.entity.HmiResultObj;
import tr.com.abasus.util.GlobalUtil;
import tr.com.abasus.util.PotentialStatus;
import tr.com.abasus.util.SessionUtil;
import tr.com.abasus.util.UserTypes;

@Controller
@RequestMapping(value="/potential")
public class PtUserPotentialController {

	
	@Autowired
	ProcessUserService processUserService;
	
	@Autowired
	MailFacade mailFacade;
	
	
	@RequestMapping(value="/create", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody HmiResultObj createUserPotential(@RequestBody UserPotential userPotential,HttpServletRequest request) throws UnAuthorizedUserException{
		User sessionUser=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if(sessionUser==null){
			throw new UnAuthorizedUserException("User not in session");
		}
		if(userPotential.getUserId()!=0){
			UserPotential up=processUserService.findUserPotentialById(userPotential.getUserId());
			userPotential.setCreateTime(up.getCreateTime());
		}else{
			userPotential.setCreateTime(new Date());
		}
		
		userPotential.setUpdateTime(new Date());
		
		mailFacade.sendMailForPotential(userPotential);
		
		return processUserService.createUserPotential(userPotential);
		
    }
	
	@RequestMapping(value="/update", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody HmiResultObj updateUserPotential(@RequestBody UserPotential userPotential,HttpServletRequest request) throws UnAuthorizedUserException{
		User sessionUser=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if(sessionUser==null){
			throw new UnAuthorizedUserException("User not in session");
		}
		
		
		userPotential.setUpdateTime(new Date());
		return processUserService.createUserPotential(userPotential);
		
    }
	
	@RequestMapping(value="/delete", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody HmiResultObj deleteUserPotential(@RequestBody UserPotential userPotential,HttpServletRequest request) throws UnAuthorizedUserException{
		User sessionUser=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if(sessionUser==null){
			throw new UnAuthorizedUserException("User not in session");
		}
		
		return processUserService.createUserPotential(userPotential);
	}
	
	@RequestMapping(value="/findByFirmId/{firmId}", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody List<UserPotential> findUserPotentials(@PathVariable int firmId,HttpServletRequest request) throws UnAuthorizedUserException{
		User sessionUser=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if(sessionUser==null){
			throw new UnAuthorizedUserException("User not in session");
		}
		
		return processUserService.findUserPotentials(firmId);
	}

	@RequestMapping(value="/findByStatu/{statu}", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody List<UserPotential> findUserPotentialsByStatu(@PathVariable int statu,HttpServletRequest request) throws UnAuthorizedUserException{
		User sessionUser=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if(sessionUser==null){
			throw new UnAuthorizedUserException("User not in session");
		}
		
		int[] status=new int[]{statu};
		
		return processUserService.findUserPotentialsByStatu(status);
	}

	@RequestMapping(value="/findAll", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody List<UserPotential> findAllUserPotentials(HttpServletRequest request) throws UnAuthorizedUserException{
		User sessionUser=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if(sessionUser==null){
			throw new UnAuthorizedUserException("User not in session");
		}
		
		
		return processUserService.findAllUserPotentials();
		
	}
	
	@RequestMapping(value="/findByName", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody List<UserPotential> findByName(@RequestBody UserPotential userPotential ,HttpServletRequest request) throws UnAuthorizedUserException{
		User sessionUser=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if(sessionUser==null){
			throw new UnAuthorizedUserException("User not in session");
		}
		
		int firmId=sessionUser.getFirmId();
		if(sessionUser.getUserType()==UserTypes.USER_TYPE_ADMIN_INT)
			firmId=0;
		
		List<UserPotential> userPotentials=processUserService.findUserPotentialsByName(userPotential.getUserName()+"%", userPotential.getUserSurname()+"%", firmId);
		for (UserPotential up : userPotentials) {
			up.setpStatuStr(PotentialStatus.POTENTIAL_STATU(up.getpStatu()));
			
		}
		
		return userPotentials;
		
	}
	
	
	@RequestMapping(value="/findById/{userId}", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody UserPotential findUserPotentialById(@RequestBody long userId,HttpServletRequest request) throws UnAuthorizedUserException{
		User sessionUser=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if(sessionUser==null){
			throw new UnAuthorizedUserException("User not in session");
		}
		return processUserService.findUserPotentialById(userId);
	}
	
	
	
	
}
