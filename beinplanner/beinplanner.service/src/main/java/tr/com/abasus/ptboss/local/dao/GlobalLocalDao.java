package tr.com.abasus.ptboss.local.dao;

import tr.com.abasus.ptboss.local.entity.DbHostTbl;
import tr.com.abasus.ptboss.result.entity.HmiResultObj;
import tr.com.abasus.ptboss.settings.entity.PtRestrictions;

public interface GlobalLocalDao {

	public DbHostTbl findDbHost();
	public HmiResultObj createDbHost(DbHostTbl dbHostTbl);
	
	
	public PtRestrictions findRestrictionsForCount(String myPass);
	public PtRestrictions findRestrictionsForPacket(String myPass);
	
	public HmiResultObj updateRestrictionsForCountCampain(int studioCount,int teacherCount);
	public HmiResultObj updateRestrictionsForPacketCampain(String packetType);
	
}
