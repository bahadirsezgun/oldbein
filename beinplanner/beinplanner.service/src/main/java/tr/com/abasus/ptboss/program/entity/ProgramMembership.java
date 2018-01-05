package tr.com.abasus.ptboss.program.entity;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonTypeName;

import tr.com.abasus.ptboss.program.service.ProgramService;
import tr.com.abasus.ptboss.result.entity.HmiResultObj;
import tr.com.abasus.util.GlobalUtil;
import tr.com.abasus.util.ProgramTypes;
import tr.com.abasus.util.ResultStatuObj;

@Component(value="programMembership")
@Scope("prototype")
@JsonTypeName("pm")
public class ProgramMembership extends ProgramFactory implements ProgramInterface {

	
	
	@Autowired
	ProgramService programService;
	
	

	@Override
	public ProgramFactory findProgramById(int progId) {
		ProgramFactory programFactory=programService.findProgramMembership(progId);
		if(programFactory.getProgRestriction()==ProgramTypes.PROGRAM_RESTRICTED){
			List<ProgramMembershipDetail> programMembershipDetails=programService.findAllProgramMembershipDetail(progId);
			programFactory.setProgramMembershipDetails(programMembershipDetails);
		}
		return programFactory;
	}

	@Override
	public HmiResultObj createProgram(ProgramFactory programFactory) {
		ProgramMembership programMembership=(ProgramMembership)programFactory;
		programMembership.setCreateTime(GlobalUtil.getCurrentDateByTimeZone());
		
		HmiResultObj hmiResultObj= programService.createProgramMembership(programMembership);
		if(hmiResultObj.getResultStatu()==ResultStatuObj.RESULT_STATU_SUCCESS){
		
			int progId=Integer.parseInt(hmiResultObj.getResultMessage());
			
			programService.deleteProgramMembershipDetailByProgId(progId);
			
			for (ProgramMembershipDetail programMembershipDetail : programFactory.getProgramMembershipDetails()) {
				programService.createProgramMembershipDetail(programMembershipDetail);
			}
		}
		return hmiResultObj;
	}

	@Override
	public List<ProgramFactory> findAllPrograms(int firmId) {
		return programService.findAllProgramMembership(firmId);
	}

	@Override
	public List<ProgramFactory> findAllProgramsForDefinition(int firmId) {
		return programService.findAllProgramMembership(firmId);
	}

	@Override
	public HmiResultObj deleteProgram(ProgramFactory programFactory) {
		return programService.deleteProgramMembership((ProgramMembership)programFactory);
	}

	public ProgramService getProgramService() {
		return programService;
	}

	public void setProgramService(ProgramService programService) {
		this.programService = programService;
	}

	@Override
	public List<ProgramFactory> findAllProgramsForStaff(int firmId) {
		return programService.findAllProgramMembership(firmId);
	}
}
