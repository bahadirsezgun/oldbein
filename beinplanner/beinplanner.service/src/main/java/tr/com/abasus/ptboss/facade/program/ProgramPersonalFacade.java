package tr.com.abasus.ptboss.facade.program;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tr.com.abasus.ptboss.facade.dao.ProgramFacadeDao;
import tr.com.abasus.ptboss.program.entity.ProgramFactory;
import tr.com.abasus.ptboss.program.entity.ProgramPersonal;

@Service
public class ProgramPersonalFacade implements ProgramFacadeService {

	
	@Autowired
	ProgramFacadeDao programFacadeDao;
	
	@Override
	public boolean canProgramDelete(ProgramFactory programFactory) {
		return programFacadeDao.isPersonalProgramCanDelete((ProgramPersonal)programFactory);
	}

}
