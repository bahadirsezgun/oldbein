package tr.com.abasus.ptboss.facade.definitions;

import tr.com.abasus.ptboss.result.entity.HmiResultObj;

public interface DefinitionFacadeService {

	public HmiResultObj canFirmDelete(int firmId);
	
	public boolean canStudioDelete(int studioId);
}
