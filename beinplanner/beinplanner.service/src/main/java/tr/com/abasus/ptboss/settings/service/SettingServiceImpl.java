package tr.com.abasus.ptboss.settings.service;

import java.util.List;

import tr.com.abasus.ptboss.result.entity.HmiResultObj;
import tr.com.abasus.ptboss.settings.dao.SettingDao;
import tr.com.abasus.ptboss.settings.entity.DbHostTbl;
import tr.com.abasus.ptboss.settings.entity.DbMailTbl;
import tr.com.abasus.ptboss.settings.entity.PtAction;
import tr.com.abasus.ptboss.settings.entity.PtGlobal;
import tr.com.abasus.ptboss.settings.entity.PtRestrictions;
import tr.com.abasus.ptboss.settings.entity.PtRules;
import tr.com.abasus.util.GlobalUtil;

public class SettingServiceImpl implements SettingService{

	
	SettingDao settingDao;
	
	
	
	


	@Override
	public HmiResultObj createPtAction(PtAction ptAction) {
		return settingDao.createPtAction(ptAction);
	}


	@Override
	public HmiResultObj deletePtAction(PtAction ptAction) {
		return settingDao.deletePtAction(ptAction);
	}


	@Override
	public List<PtAction> findPtActionForMe(long ptaTo, int limit, int pointer) {
		return settingDao.findPtActionForMe(ptaTo, limit, pointer);
	}


	@Override
	public List<PtAction> findPtActionForAll(int limit, int pointer) {
		return settingDao.findPtActionForAll(limit, pointer);
	}


	@Override
	public List<PtAction> findPtActionForAllAndMe(long ptaTo, int limit, int pointer) {
		return settingDao.findPtActionForAllAndMe(ptaTo, limit, pointer);
	}


	@Override
	public List<PtAction> findPtActionForAllAndUid(long ptaUid, int limit, int pointer) {
		return settingDao.findPtActionForAllAndUid(ptaUid, limit, pointer);
	}


	@Override
	public List<PtRules> findPtRules() {
		return settingDao.findPtRules();
	}

	@Override
	public PtRules findPtRuleById(int ruleId) {
		return settingDao.findPtRuleById(ruleId);
	}

	@Override
	public HmiResultObj createUpdateRule(PtRules ptRules) {
		return settingDao.createUpdateRule(ptRules);
	}

	@Override
	public PtGlobal findPtGlobal() {
		
		PtGlobal ptGlobal=settingDao.findPtGlobal();
		if(ptGlobal.getPtDateFormat().equals("%d/%m/%y")){
			ptGlobal.setPtDbDateFormat("dd/MM/yy");
			ptGlobal.setPtScrDateFormat("dd/mm/yy");
		}else if(ptGlobal.getPtDateFormat().equals("%d.%m.%y")){
			ptGlobal.setPtDbDateFormat("MM.dd.yy");
			ptGlobal.setPtScrDateFormat("mm.dd.yy");
		}else if(ptGlobal.getPtDateFormat().equals("%m/%d/%y")){
			ptGlobal.setPtDbDateFormat("MM/dd/yy");
			ptGlobal.setPtScrDateFormat("mm/dd/yy");
		}else if(ptGlobal.getPtDateFormat().equals("%m.%d.%y")){
			ptGlobal.setPtDbDateFormat("MM.dd.yy");
			ptGlobal.setPtScrDateFormat("mm.dd.yy");
		}else if(ptGlobal.getPtDateFormat().equals("%d/%m/%Y")){
			ptGlobal.setPtDbDateFormat("dd/MM/yyyy");
			ptGlobal.setPtScrDateFormat("dd/mm/yyyy");
		}else if(ptGlobal.getPtDateFormat().equals("%d.%m.%Y")){
			ptGlobal.setPtDbDateFormat("dd.MM.yyyy");
			ptGlobal.setPtScrDateFormat("dd.mm.yyyy");
		}else if(ptGlobal.getPtDateFormat().equals("%m/%d/%Y")){
			ptGlobal.setPtDbDateFormat("MM/dd/yyyy");
			ptGlobal.setPtScrDateFormat("mm/dd/yyyy");
		}else if(ptGlobal.getPtDateFormat().equals("%m.%d.%Y")){
			ptGlobal.setPtDbDateFormat("MM.dd.yyyy");
			ptGlobal.setPtScrDateFormat("mm.dd.yyyy");
		}
		
		
		return ptGlobal;
	}

	@Override
	public HmiResultObj createUpdatePtGlobal(PtGlobal ptGlobal) {
		return settingDao.createUpdatePtGlobal(ptGlobal);
	}

	public SettingDao getSettingDao() {
		return settingDao;
	}

	public void setSettingDao(SettingDao settingDao) {
		this.settingDao = settingDao;
	}




/*
	@Override
	public HmiResultObj createDbHostTbl(DbHostTbl dbHostTbl) {
		return settingDao.createDbHostTbl(dbHostTbl);
	}
*/
	@Override
	public HmiResultObj createDbMailTbl(DbMailTbl dbMailTbl) {
		return settingDao.createDbMailTbl(dbMailTbl);
	}

	@Override
	public DbHostTbl findDbHostTbl() {
		return settingDao.findDbHostTbl();
	}

	@Override
	public DbMailTbl findDbMailTbl() {
		return settingDao.findDbMailTbl();
	}

	
}
