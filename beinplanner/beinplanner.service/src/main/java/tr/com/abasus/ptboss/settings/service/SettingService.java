package tr.com.abasus.ptboss.settings.service;

import java.util.List;

import tr.com.abasus.ptboss.result.entity.HmiResultObj;
import tr.com.abasus.ptboss.settings.entity.DbHostTbl;
import tr.com.abasus.ptboss.settings.entity.DbMailTbl;
import tr.com.abasus.ptboss.settings.entity.PtAction;
import tr.com.abasus.ptboss.settings.entity.PtGlobal;
import tr.com.abasus.ptboss.settings.entity.PtRestrictions;
import tr.com.abasus.ptboss.settings.entity.PtRules;

public interface SettingService {

	
	public HmiResultObj createPtAction(PtAction ptAction);
	public HmiResultObj deletePtAction(PtAction ptAction);
	public List<PtAction> findPtActionForMe(long ptaTo,int limit,int pointer);
	public List<PtAction> findPtActionForAll(int limit,int pointer);
	public List<PtAction> findPtActionForAllAndMe(long ptaTo,int limit,int pointer);
	public List<PtAction> findPtActionForAllAndUid(long ptaUid,int limit,int pointer);

	public List<PtRules> findPtRules();
	public PtRules findPtRuleById(int ruleId);
	public HmiResultObj createUpdateRule(PtRules ptRules);
	
	public PtGlobal findPtGlobal();
	public HmiResultObj createUpdatePtGlobal(PtGlobal ptGlobal);
	
	
	//public HmiResultObj createDbHostTbl(DbHostTbl dbHostTbl);
	public HmiResultObj createDbMailTbl(DbMailTbl dbMailTbl);
	
	public DbHostTbl findDbHostTbl();
	public DbMailTbl findDbMailTbl();
	
	
	
}
