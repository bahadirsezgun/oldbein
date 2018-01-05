package tr.com.abasus.ptboss.local.service;

import tr.com.abasus.ptboss.local.dao.GlobalLocalDao;
import tr.com.abasus.ptboss.local.entity.DbHostTbl;
import tr.com.abasus.ptboss.program.dao.ProgramDao;
import tr.com.abasus.ptboss.result.entity.HmiResultObj;
import tr.com.abasus.ptboss.settings.entity.PtRestrictions;

public class GlobalLocalServiceImpl implements GlobalLocalService {

	GlobalLocalDao globalLocalDao;

	
	@Override
	public DbHostTbl findDbHost() {
		return globalLocalDao.findDbHost();
	}

	@Override
	public HmiResultObj createDbHost(DbHostTbl dbHostTbl) {
		return globalLocalDao.createDbHost(dbHostTbl);
	}

	
	@Override
	public PtRestrictions findRestrictionsForCount(String myPass) {
		return globalLocalDao.findRestrictionsForCount(myPass);
	}

	@Override
	public PtRestrictions findRestrictionsForPacket(String myPass) {
		
		PtRestrictions ptRestrictions=globalLocalDao.findRestrictionsForPacket(myPass);
		
		
		
		
		return ptRestrictions;
	}
	
	
	public GlobalLocalDao getGlobalLocalDao() {
		return globalLocalDao;
	}

	public void setGlobalLocalDao(GlobalLocalDao globalLocalDao) {
		this.globalLocalDao = globalLocalDao;
	}

	

	
	
	
}
