package tr.com.abasus.ptboss.definition.service;

import java.util.List;

import tr.com.abasus.ptboss.definition.dao.DefinitionDao;
import tr.com.abasus.ptboss.definition.entity.DefCalendarTimes;
import tr.com.abasus.ptboss.definition.entity.DefCity;
import tr.com.abasus.ptboss.definition.entity.DefFirm;
import tr.com.abasus.ptboss.definition.entity.DefLevelInfo;
import tr.com.abasus.ptboss.definition.entity.DefState;
import tr.com.abasus.ptboss.definition.entity.DefStudio;
import tr.com.abasus.ptboss.result.entity.HmiResultObj;

public class DefinitionServiceImpl implements DefinitionService {

	DefinitionDao definitionDao;
	
	@Override
	public List<DefStudio> findAllStudios(int firmId) {
		return definitionDao.findAllStudios(firmId);
	}
	
	@Override
	public List<DefStudio> findAllStudiosForDefinition() {
		return definitionDao.findAllStudiosForDefinition();
	}
	
	@Override
	public DefStudio findStudioById(int studioId) {
		return definitionDao.findStudioById(studioId);
	}

	@Override
	public List<DefStudio> findAllStudiosByStatus(int firmId,int status) {
		return definitionDao.findAllStudiosByStatus(firmId,status);
	}

	@Override
	public synchronized HmiResultObj createStudio(DefStudio defStudio) {
		return definitionDao.createStudio(defStudio);
	}

	@Override
	public synchronized HmiResultObj updateStudio(DefStudio defStudio) {
		return definitionDao.updateStudio(defStudio);
	}

	@Override
	public synchronized HmiResultObj deleteStudio(DefStudio defStudio) {
		return definitionDao.deleteStudio(defStudio);
	}

	

	@Override
	public List<DefCity> findCities(int stateId) {
		return definitionDao.findCities(stateId);
	}

	@Override
	public HmiResultObj createCity(DefCity defCity) {
		return definitionDao.createCity(defCity);
	}

	@Override
	public HmiResultObj updateCity(DefCity defCity) {
		return definitionDao.updateCity(defCity);
	}

	@Override
	public HmiResultObj deleteCity(DefCity defCity) {
		return definitionDao.deleteCity(defCity);
	}

	@Override
	public List<DefState> findStates() {
		return definitionDao.findStates();
	}

	@Override
	public HmiResultObj createState(DefState defState) {
		return definitionDao.createState(defState);
	}

	@Override
	public HmiResultObj updateState(DefState defState) {
		return definitionDao.updateState(defState);
	}

	@Override
	public HmiResultObj deleteState(DefState defState) {
		return definitionDao.deleteState(defState);
	}

	

	@Override
	public List<DefFirm> findFirms() {
		return definitionDao.findFirms();
	}

	@Override
	public DefFirm findFirmsById(int firmId) {
		return definitionDao.findFirmsById(firmId);
	}

	@Override
	public HmiResultObj createFirms(DefFirm defFirm) {
		return definitionDao.createFirms(defFirm);
	}

	@Override
	public HmiResultObj updateFirms(DefFirm defFirm) {
		return definitionDao.updateFirms(defFirm);
	}

	@Override
	public HmiResultObj deleteFirms(DefFirm defFirm) {
		return definitionDao.deleteFirms(defFirm);
	}
	
	
	public DefinitionDao getDefinitionDao() {
		return definitionDao;
	}

	public void setDefinitionDao(DefinitionDao definitionDao) {
		this.definitionDao = definitionDao;
	}

	@Override
	public List<DefLevelInfo> findLevelInfo() {
		return definitionDao.findLevelInfo();
	}

	@Override
	public DefLevelInfo findLevelInfoById(int levelId) {
		return definitionDao.findLevelInfoById(levelId);
	}

	@Override
	public HmiResultObj createLevelInfo(DefLevelInfo defLevelInfo) {
		return definitionDao.createLevelInfo(defLevelInfo);
	}

	@Override
	public HmiResultObj updateLevelInfo(DefLevelInfo defLevelInfo) {
		return definitionDao.updateLevelInfo(defLevelInfo);
	}

	@Override
	public HmiResultObj deleteLevelInfo(DefLevelInfo defLevelInfo) {
		return definitionDao.deleteLevelInfo(defLevelInfo);
	}

	@Override
	public DefCalendarTimes findCalendarTimes() {
		return definitionDao.findCalendarTimes();
	}

	@Override
	public HmiResultObj createDefCalendarTimes(DefCalendarTimes defCalendarTimes) {
		return definitionDao.createDefCalendarTimes(defCalendarTimes);
	}

	

	
}
