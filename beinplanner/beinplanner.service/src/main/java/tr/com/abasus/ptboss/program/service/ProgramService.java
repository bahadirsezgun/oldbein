package tr.com.abasus.ptboss.program.service;

import java.util.List;

import tr.com.abasus.ptboss.program.entity.ProgramClass;
import tr.com.abasus.ptboss.program.entity.ProgramFactory;
import tr.com.abasus.ptboss.program.entity.ProgramFamily;
import tr.com.abasus.ptboss.program.entity.ProgramMembership;
import tr.com.abasus.ptboss.program.entity.ProgramMembershipDetail;
import tr.com.abasus.ptboss.program.entity.ProgramPersonal;
import tr.com.abasus.ptboss.result.entity.HmiResultObj;

public interface ProgramService {

	
	
	public List<ProgramFactory> findAllProgramPersonal(int firmId);
	
	public ProgramPersonal findProgramPersonal(int progId);
	
	public List<ProgramFactory> findAllProgramsForPersonal();
	
	public HmiResultObj createProgramPersonal(ProgramPersonal programPersonal);
	
	public HmiResultObj deleteProgramPersonal(ProgramPersonal programPersonal);
	
	/***********************************************************************************/
	
	public List<ProgramFactory> findAllProgramFamily(int firmId);
	
	public ProgramFamily findProgramFamily(int progId);
	
	public List<ProgramFactory> findAllProgramsForFamily();
	
	public HmiResultObj createProgramFamily(ProgramFamily programFamily);
	
	public HmiResultObj deleteProgramFamily(ProgramFamily programFamily);
	
	/***********************************************************************************/
	
	
	public List<ProgramFactory> findAllProgramsForClass();
	
	public List<ProgramFactory> findAllProgramClass(int firmId);
	
	public ProgramClass findProgramClass(int progId);
	
	public HmiResultObj createProgramClass(ProgramClass programClass);
	
	public HmiResultObj deleteProgramClass(ProgramClass programClass);
	
	
	
	/***********************************************************************************/
	
	public List<ProgramFactory> findAllProgramMembership(int firmId);
	
	public ProgramMembership findProgramMembership(int progId);
	
	public HmiResultObj createProgramMembership(ProgramMembership programMembership);
	
	public HmiResultObj deleteProgramMembership(ProgramMembership programMembership);
	
	public List<ProgramMembershipDetail> findAllProgramMembershipDetail(int progId);
	
	public ProgramMembershipDetail findProgramMembershipDetail(int progDetId);
	
	public HmiResultObj createProgramMembershipDetail(ProgramMembershipDetail programMembershipDetail);
	
	public HmiResultObj deleteProgramMembershipDetail(ProgramMembershipDetail programMembershipDetail);
	
	public HmiResultObj deleteProgramMembershipDetailByProgId(int progId);
	
	
	
	
}
