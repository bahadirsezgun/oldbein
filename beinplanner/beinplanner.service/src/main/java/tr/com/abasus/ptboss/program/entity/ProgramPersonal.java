package tr.com.abasus.ptboss.program.entity;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonTypeName;

import tr.com.abasus.ptboss.program.service.ProgramService;
import tr.com.abasus.ptboss.result.entity.HmiResultObj;
import tr.com.abasus.util.GlobalUtil;

@Component(value="programPersonal")
@Scope("prototype")
@JsonTypeName("pp")
public class ProgramPersonal extends ProgramFactory implements ProgramInterface  {

	@Autowired
	ProgramService programService;
	public ProgramService getProgramService() {
		return programService;
	}

	public void setProgramService(ProgramService programService) {
		this.programService = programService;
	}
	
	
	
	public ProgramPersonal() {
		super();
		
	}

	@Override
	public ProgramFactory findProgramById(int progId) {
		return programService.findProgramPersonal(progId);
	}

	

	@Override
	public HmiResultObj createProgram(ProgramFactory programFactory) {
		
		ProgramPersonal programPersonal=(ProgramPersonal)programFactory;
		programPersonal.setCreateTime(GlobalUtil.getCurrentDateByTimeZone());
		
		return programService.createProgramPersonal(programPersonal);
	}

	@Override
	public List<ProgramFactory> findAllPrograms(int firmId) {
		return programService.findAllProgramPersonal(firmId);
	}

	@Override
	public List<ProgramFactory> findAllProgramsForDefinition(int firmId) {
		return programService.findAllProgramPersonal(firmId);
	}

	@Override
	public HmiResultObj deleteProgram(ProgramFactory programFactory) {
		return programService.deleteProgramPersonal((ProgramPersonal)programFactory);
	}

	@Override
	public List<ProgramFactory> findAllProgramsForStaff(int firmId) {
		return programService.findAllProgramPersonal(firmId);
	}
	
	
	
	
	
	

}
