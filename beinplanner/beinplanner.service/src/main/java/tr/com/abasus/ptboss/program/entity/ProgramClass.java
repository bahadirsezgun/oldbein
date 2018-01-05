package tr.com.abasus.ptboss.program.entity;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonTypeName;

import tr.com.abasus.ptboss.program.service.ProgramService;
import tr.com.abasus.ptboss.result.entity.HmiResultObj;
import tr.com.abasus.util.GlobalUtil;
import tr.com.abasus.util.ResultStatuObj;

@Component(value="programClass")
@Scope("prototype")
@JsonTypeName("pc")
public class ProgramClass extends ProgramFactory implements ProgramInterface {

	@Autowired
	ProgramService programService;
	
	public ProgramService getProgramService() {
		return programService;
	}
	public void setProgramService(ProgramService programService) {
		this.programService = programService;
	}
	
	
	@Override
	public ProgramFactory findProgramById(int progId) {
		ProgramFactory programFactory=programService.findProgramClass(progId);
		return programFactory;
	}
	
	@Override
	public HmiResultObj createProgram(ProgramFactory programFactory) {
		ProgramClass programClass=(ProgramClass)programFactory;
		programClass.setCreateTime(GlobalUtil.getCurrentDateByTimeZone());
		
		HmiResultObj hmiResultObj= programService.createProgramClass(programClass);
		
		return hmiResultObj;
	}
	
	
	@Override
	public HmiResultObj deleteProgram(ProgramFactory programFactory) {
		return programService.deleteProgramClass((ProgramClass)programFactory);
	}
	
	@Override
	public List<ProgramFactory> findAllPrograms(int firmId) {
		return programService.findAllProgramClass(firmId);
	}
	@Override
	public List<ProgramFactory> findAllProgramsForDefinition(int firmId) {
		return programService.findAllProgramClass(firmId);
	}
	@Override
	public List<ProgramFactory> findAllProgramsForStaff(int firmId) {
		return programService.findAllProgramClass(firmId);
	}
	
	
	
	
	
	
	
}
