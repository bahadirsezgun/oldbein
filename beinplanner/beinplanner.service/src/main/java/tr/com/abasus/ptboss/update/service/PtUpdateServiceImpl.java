package tr.com.abasus.ptboss.update.service;

import tr.com.abasus.ptboss.result.entity.HmiResultObj;
import tr.com.abasus.ptboss.update.dao.PtUpdateDao;
import tr.com.abasus.ptboss.update.entity.PtbossUpdate;

public class PtUpdateServiceImpl implements  PtUpdateService{

	PtUpdateDao ptUpdateDao;
	
	@Override
	public PtbossUpdate findPtBossUpdateByVersion(String version) {
		return ptUpdateDao.findPtBossUpdateByVersion(version);
	}

	@Override
	public HmiResultObj createPtBossUpdate(PtbossUpdate ptbossUpdate) {
		return ptUpdateDao.createPtBossUpdate(ptbossUpdate);
	}

	public PtUpdateDao getPtUpdateDao() {
		return ptUpdateDao;
	}

	public void setPtUpdateDao(PtUpdateDao ptUpdateDao) {
		this.ptUpdateDao = ptUpdateDao;
	}

	@Override
	public void createIndex(String sql) {
		ptUpdateDao.createIndex(sql);
		
	}

	@Override
	public void createTable(String sql, String table) {
		ptUpdateDao.createTable(sql, table);
	}

	@Override
	public void createTableOfColumn(String sql, String table, String col) {
		ptUpdateDao.createTableOfColumn(sql, table, col);
	}

	@Override
	public PtbossUpdate findPtBossUpdateByLast() {
		return ptUpdateDao.findPtBossUpdateByLast();
	}

	@Override
	public void updateTableOfColumn(String sql, String table, String col) {
		 ptUpdateDao.updateTableOfColumn(sql, table, col);
		
	}

}
