package tr.com.abasus.ptboss.program.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import tr.com.abasus.ptboss.exceptions.UnAuthorizedUserException;
import tr.com.abasus.ptboss.facade.program.ProgramClassFacade;
import tr.com.abasus.ptboss.facade.program.ProgramFacadeService;
import tr.com.abasus.ptboss.facade.program.ProgramMembershipFacade;
import tr.com.abasus.ptboss.facade.program.ProgramPersonalFacade;
import tr.com.abasus.ptboss.program.entity.ProgramClass;
import tr.com.abasus.ptboss.program.entity.ProgramFactory;
import tr.com.abasus.ptboss.program.entity.ProgramInterface;
import tr.com.abasus.ptboss.program.entity.ProgramMembership;
import tr.com.abasus.ptboss.program.entity.ProgramPersonal;
import tr.com.abasus.ptboss.program.service.ProgramService;
import tr.com.abasus.ptboss.ptuser.entity.User;
import tr.com.abasus.ptboss.result.entity.HmiResultObj;
import tr.com.abasus.util.GlobalUtil;
import tr.com.abasus.util.ProgramTypes;
import tr.com.abasus.util.ResultStatuObj;
import tr.com.abasus.util.SessionUtil;
import tr.com.abasus.util.UserTypes;

@Controller
@RequestMapping(value="/program")
public class ProgramController {

	@Autowired
	ProgramService programService;
	
	@Autowired
	ProgramPersonal programPersonal;
	
	@Autowired
	ProgramClass programClass;
	
	@Autowired
	ProgramMembership programMembership;
	
	@Autowired
	ProgramPersonalFacade programPersonalFacade;
	
	@Autowired
	ProgramClassFacade programClassFacade;
	
	@Autowired
	ProgramMembershipFacade programMembershipFacade;
	
	
	@RequestMapping(value="/findProgramByProgId/{progId}/{progType}", method = RequestMethod.POST) 
	public @ResponseBody ProgramFactory findProgramByProgId(@PathVariable("progId") int progId,@PathVariable("progType") int progType, HttpServletRequest request ){
		User user=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if((user.getUserType()==UserTypes.USER_TYPE_MEMBER_INT)){
			return null;
		}
		ProgramInterface programInterface=null;
		
		////System.out.println(programPersonal.getFirmName()+"  "+programPersonal.getProgId());
		
		if(progType==ProgramTypes.PROGRAM_PERSONAL){
			programInterface=programPersonal;
		}else if(progType==ProgramTypes.PROGRAM_CLASS){
			programInterface=programClass;
		}else if(progType==ProgramTypes.PROGRAM_MEMBERSHIP){
			programInterface=programMembership;
		}
		
		return (ProgramFactory)programInterface.findProgramById(progId);
	}
	
	
	
	@RequestMapping(value="/createProgram", method = RequestMethod.POST) 
	public @ResponseBody HmiResultObj createProgram(@RequestBody ProgramFactory programFactory){
		
		ProgramInterface programInterface=null;
		if(programFactory instanceof ProgramPersonal){
			if(GlobalUtil.ptRestrictionsForPacket.getIndividualRestriction()==1){
				programInterface=programPersonal;
			}else{
				HmiResultObj hmiResultObj=new HmiResultObj();
				hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
				hmiResultObj.setResultMessage("packetInappropriate");
				
				return hmiResultObj;
			}
		}else if(programFactory instanceof ProgramClass){
			if(GlobalUtil.ptRestrictionsForPacket.getGroupRestriction()==1){
			programInterface=programClass;
			}else{
				HmiResultObj hmiResultObj=new HmiResultObj();
				hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
				hmiResultObj.setResultMessage("packetInappropriate");
				
				return hmiResultObj;
			}
		}else if(programFactory instanceof ProgramMembership){
			if(GlobalUtil.ptRestrictionsForPacket.getMembershipRestriction()==1){
				programInterface=programMembership;
			}else{
				HmiResultObj hmiResultObj=new HmiResultObj();
				hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
				hmiResultObj.setResultMessage("packetInappropriate");
				
				return hmiResultObj;
			}
		}
		
		return programInterface.createProgram(programFactory);
	}
	
	@RequestMapping(value="/deleteProgram", method = RequestMethod.POST) 
	public @ResponseBody HmiResultObj deleteProgram(@RequestBody ProgramFactory programFactory){
		
		ProgramFacadeService programFacadeService=null;
		
		ProgramInterface programInterface=null;
		if(programFactory instanceof ProgramPersonal){
			programInterface=programPersonal;
		    programFacadeService=programPersonalFacade;
		}else if(programFactory instanceof ProgramClass){
			 programInterface=programClass;
			 programFacadeService=programClassFacade;
		}else if(programFactory instanceof ProgramMembership){
			 programInterface=programMembership;
			 programFacadeService=programMembershipFacade;
		}
		
		if(programFacadeService.canProgramDelete(programFactory)){
			HmiResultObj hmiResultObj=new HmiResultObj();
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
			hmiResultObj.setResultMessage("programUsedInSales");
			return hmiResultObj;
		}
		
		
		return  programInterface.deleteProgram(programFactory);
	}
	
	
	
	@RequestMapping(value="/findAllPrograms/{progType}", method = RequestMethod.POST) 
	public @ResponseBody List<ProgramFactory> findAllPrograms(@PathVariable("progType") int progType, HttpServletRequest request  ){
		User user=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if((user.getUserType()==UserTypes.USER_TYPE_MEMBER_INT)){
			return null;
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
	
	@RequestMapping(value="/findAllProgramsForStaff/{firmId}/{progType}", method = RequestMethod.POST) 
	public @ResponseBody List<ProgramFactory> findAllProgramsForStaff(@PathVariable("firmId") int firmId,@PathVariable("progType") int progType, HttpServletRequest request  ) throws UnAuthorizedUserException{
		User user=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if((user.getUserType()==UserTypes.USER_TYPE_MEMBER_INT)){
			return null;
		}
		
		ProgramInterface programInterface=null;
		if(progType==ProgramTypes.PROGRAM_PERSONAL){
			programInterface=programPersonal;
		}else if(progType==ProgramTypes.PROGRAM_CLASS){
			programInterface=programClass;
		}else if(progType==ProgramTypes.PROGRAM_MEMBERSHIP){
			programInterface=programMembership;
		}
		
		List<ProgramFactory> programFactories=programInterface.findAllProgramsForStaff(firmId);
		
		
		return programFactories;
	}
	
	
	@RequestMapping(value="/findAllProgramsForDefinition/{firmId}/{progType}", method = RequestMethod.POST) 
	public @ResponseBody List<ProgramFactory> findAllProgramsForDefinition(@PathVariable("firmId") int firmId,@PathVariable("progType") int progType, HttpServletRequest request  ) throws UnAuthorizedUserException{
		User user=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if((user.getUserType()==UserTypes.USER_TYPE_MEMBER_INT)){
			return null;
		}
		
		ProgramInterface programInterface=null;
		if(progType==ProgramTypes.PROGRAM_PERSONAL){
			programInterface=programPersonal;
		}else if(progType==ProgramTypes.PROGRAM_CLASS){
			programInterface=programClass;
		}else if(progType==ProgramTypes.PROGRAM_MEMBERSHIP){
			programInterface=programMembership;
		}
		List<ProgramFactory> programFactories=programInterface.findAllPrograms(firmId);
		
		
		return programFactories;
		
		
	}
	
	
	
	
	
	
	

	public ProgramService getProgramService() {
		return programService;
	}

	public void setProgramService(ProgramService programService) {
		this.programService = programService;
	}


	public ProgramPersonal getProgramPersonal() {
		return programPersonal;
	}


	public void setProgramPersonal(ProgramPersonal programPersonal) {
		this.programPersonal = programPersonal;
	}



	public ProgramClass getProgramClass() {
		return programClass;
	}



	public void setProgramClass(ProgramClass programClass) {
		this.programClass = programClass;
	}



	public ProgramMembership getProgramMembership() {
		return programMembership;
	}



	public void setProgramMembership(ProgramMembership programMembership) {
		this.programMembership = programMembership;
	}
	
	
	
}
