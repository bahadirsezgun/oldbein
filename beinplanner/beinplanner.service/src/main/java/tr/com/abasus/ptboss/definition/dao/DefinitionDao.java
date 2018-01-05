package tr.com.abasus.ptboss.definition.dao;

import java.util.List;

import tr.com.abasus.ptboss.definition.entity.DefCalendarTimes;
import tr.com.abasus.ptboss.definition.entity.DefCity;
import tr.com.abasus.ptboss.definition.entity.DefFirm;
import tr.com.abasus.ptboss.definition.entity.DefLevelInfo;
import tr.com.abasus.ptboss.definition.entity.DefState;
import tr.com.abasus.ptboss.definition.entity.DefStudio;
import tr.com.abasus.ptboss.result.entity.HmiResultObj;

public interface DefinitionDao  {

	public List<DefFirm> findFirms();
	
	public DefFirm findFirmsById(int firmId);
	
	public HmiResultObj createFirms(DefFirm defFirm);
	
	public HmiResultObj updateFirms(DefFirm defFirm);
	
	public HmiResultObj deleteFirms(DefFirm defFirm);
	
	
	/*************************************************************************/
	
	public List<DefStudio> findAllStudios(int firmId);
	
	public List<DefStudio> findAllStudiosForDefinition();
	
	public DefStudio findStudioById(int studioId);
	
	public List<DefStudio> findAllStudiosByStatus(int firmId,int status);
	
	public HmiResultObj createStudio(DefStudio defStudio);
	
	public HmiResultObj updateStudio(DefStudio defStudio);
	
	public HmiResultObj deleteStudio(DefStudio defStudio);
	
	
	/*************************************************************************/
	public List<DefCity> findCities(int stateId);
	
	public HmiResultObj createCity(DefCity defCity);
	
	public HmiResultObj updateCity(DefCity defCity);
	
	public HmiResultObj deleteCity(DefCity defCity);
	
	/*************************************************************************/
	public List<DefState> findStates();
	
	public HmiResultObj createState(DefState defState);
	
	public HmiResultObj updateState(DefState defState);
	
	public HmiResultObj deleteState(DefState defState);
	

	/*************************************************************************/
	public List<DefLevelInfo> findLevelInfo();
	
	public DefLevelInfo findLevelInfoById(int levelId);
	
	public HmiResultObj createLevelInfo(DefLevelInfo defLevelInfo);
	
	public HmiResultObj updateLevelInfo(DefLevelInfo defLevelInfo);
	
	public HmiResultObj deleteLevelInfo(DefLevelInfo defLevelInfo);	
	
	
/*************************************************************************/
	
	public DefCalendarTimes findCalendarTimes();
	
	public HmiResultObj createDefCalendarTimes(DefCalendarTimes defCalendarTimes);
	
	
}
