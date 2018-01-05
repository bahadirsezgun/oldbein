package tr.com.abasus.ptboss.local.service;

import tr.com.abasus.ptboss.local.entity.DbHostTbl;
import tr.com.abasus.ptboss.result.entity.HmiResultObj;
import tr.com.abasus.ptboss.settings.entity.PtRestrictions;

public interface GlobalLocalService {

	public DbHostTbl findDbHost();
	public HmiResultObj createDbHost(DbHostTbl dbHostTbl);
	
	
	public PtRestrictions findRestrictionsForCount(String myPass);
	public PtRestrictions findRestrictionsForPacket(String myPass);
	
	
	
	
}
