package tr.com.abasus.ptboss.update.dao;

import tr.com.abasus.ptboss.result.entity.HmiResultObj;
import tr.com.abasus.ptboss.update.entity.PtbossUpdate;

public interface PtUpdateDao {

	public PtbossUpdate findPtBossUpdateByVersion(String version);
	public HmiResultObj createPtBossUpdate(PtbossUpdate ptbossUpdate);
	public PtbossUpdate findPtBossUpdateByLast();
	
	public void createIndex(String sql);
	public void createTable(String sql,String table);
	public void createTableOfColumn(String sql,String table,String col);
	public void updateTableOfColumn(String sql,String table,String col);
	
}
