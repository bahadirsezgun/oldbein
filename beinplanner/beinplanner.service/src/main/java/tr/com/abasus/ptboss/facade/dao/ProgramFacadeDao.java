package tr.com.abasus.ptboss.facade.dao;

import tr.com.abasus.ptboss.program.entity.ProgramClass;
import tr.com.abasus.ptboss.program.entity.ProgramMembership;
import tr.com.abasus.ptboss.program.entity.ProgramPersonal;

public interface ProgramFacadeDao {

	public boolean isPersonalProgramCanDelete(ProgramPersonal programPersonal);
	
	public boolean isMembershipProgramCanDelete(ProgramMembership programMembership);
	
	public boolean isClassProgramCanDelete(ProgramClass programClass);
	
	
}
