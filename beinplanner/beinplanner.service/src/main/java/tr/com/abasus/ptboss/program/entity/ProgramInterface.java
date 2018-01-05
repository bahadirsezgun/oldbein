package tr.com.abasus.ptboss.program.entity;

import java.util.List;

import tr.com.abasus.ptboss.result.entity.HmiResultObj;

public interface ProgramInterface {

	public ProgramFactory findProgramById(int progId);
	
	public HmiResultObj createProgram(ProgramFactory programFactory);
	
	public HmiResultObj deleteProgram(ProgramFactory programFactory);
	
	public List<ProgramFactory> findAllPrograms(int firmId);
	
	public List<ProgramFactory> findAllProgramsForDefinition(int firmId);
	
	public List<ProgramFactory> findAllProgramsForStaff(int firmId);
	
}
