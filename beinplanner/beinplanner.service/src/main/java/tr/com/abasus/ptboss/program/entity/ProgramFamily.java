package tr.com.abasus.ptboss.program.entity;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonTypeName;

import tr.com.abasus.ptboss.program.service.ProgramService;
import tr.com.abasus.ptboss.result.entity.HmiResultObj;

@Component(value="programFamily")
@Scope("prototype")
@JsonTypeName("pf")
public class ProgramFamily extends ProgramFactory implements ProgramInterface  {

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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HmiResultObj createProgram(ProgramFactory programFactory) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HmiResultObj deleteProgram(ProgramFactory programFactory) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ProgramFactory> findAllPrograms(int firmId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ProgramFactory> findAllProgramsForDefinition(int firmId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ProgramFactory> findAllProgramsForStaff(int firmId) {
		// TODO Auto-generated method stub
		return null;
	}
	

}
