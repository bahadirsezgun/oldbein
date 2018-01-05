package tr.com.abasus.ptboss.mobile.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import tr.com.abasus.ptboss.exceptions.UnAuthorizedUserException;
import tr.com.abasus.ptboss.program.entity.ProgramClass;
import tr.com.abasus.ptboss.program.entity.ProgramFactory;
import tr.com.abasus.ptboss.program.entity.ProgramInterface;
import tr.com.abasus.ptboss.program.entity.ProgramMembership;
import tr.com.abasus.ptboss.program.entity.ProgramPersonal;
import tr.com.abasus.ptboss.program.service.ProgramService;
import tr.com.abasus.ptboss.ptuser.entity.User;
import tr.com.abasus.ptboss.ptuser.service.ProcessUserService;
import tr.com.abasus.util.ProgramTypes;
import tr.com.abasus.util.SessionUtil;
import tr.com.abasus.util.UserTypes;

@Controller
@RequestMapping(value="/mobile/program")
public class MobileProgramController {

	@Autowired 
	ProcessUserService processUserService;
	
	@Autowired
	ProgramService programService;
	
	@Autowired
	ProgramPersonal programPersonal;
	
	@Autowired
	ProgramClass programClass;
	
	@Autowired
	ProgramMembership programMembership;
	
	
	@RequestMapping(value="/findAllPrograms/{progType}/{userName}/{password}", method = RequestMethod.POST) 
	public @ResponseBody List<ProgramFactory> findAllPrograms(@PathVariable("progType") int progType,@PathVariable String userName,@PathVariable String password , HttpServletRequest request  ) throws UnAuthorizedUserException{
		
		User user=processUserService.loginUserControl(userName, password);
		if(user==null){
			throw new UnAuthorizedUserException("");
    	}else if((user.getUserType()==UserTypes.USER_TYPE_MEMBER_INT)){
			throw new UnAuthorizedUserException("");
		}
		
		ProgramInterface programInterface=null;
		if(progType==ProgramTypes.PROGRAM_PERSONAL){
			programInterface=programPersonal;
		}else if(progType==ProgramTypes.PROGRAM_CLASS){
			programInterface=programClass;
		}else if(progType==ProgramTypes.PROGRAM_MEMBERSHIP){
			programInterface=programMembership;
		}
		
		return programInterface.findAllPrograms(user.getFirmId());
	}
}
