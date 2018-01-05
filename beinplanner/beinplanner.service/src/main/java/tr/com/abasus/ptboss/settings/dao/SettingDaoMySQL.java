package tr.com.abasus.ptboss.settings.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tr.com.abasus.general.dao.AbaxJdbcDaoSupport;
import tr.com.abasus.general.dao.SqlDao;
import tr.com.abasus.general.exception.SqlErrorException;
import tr.com.abasus.ptboss.result.entity.HmiResultObj;
import tr.com.abasus.ptboss.settings.entity.DbHostTbl;
import tr.com.abasus.ptboss.settings.entity.DbMailTbl;
import tr.com.abasus.ptboss.settings.entity.PtAction;
import tr.com.abasus.ptboss.settings.entity.PtGlobal;
import tr.com.abasus.ptboss.settings.entity.PtRestrictions;
import tr.com.abasus.ptboss.settings.entity.PtRules;
import tr.com.abasus.util.ActionTypes;
import tr.com.abasus.util.GlobalUtil;
import tr.com.abasus.util.ResultStatuObj;

public class SettingDaoMySQL extends AbaxJdbcDaoSupport implements SettingDao {

	SqlDao sqlDao;
	
	
	
	private void createRestiricitons(){
		/*INSERT INTO pt_restrictions (res_id,firm_count,teacher_count)
		VALUES(1
		,AES_ENCRYPT('1','canan3367sagali7611'),
		AES_ENCRYPT('0','canan3367sagali7611'));*/
		
		
		/*INSERT INTO pt_restrictions (res_id,firm_count,teacher_count)
		VALUES(2
		,AES_ENCRYPT('123','canan3367sagali7611'),
		AES_ENCRYPT('0','canan3367sagali7611'));*/
		
		
	}

	public SqlDao getSqlDao() {
		return sqlDao;
	}

	public void setSqlDao(SqlDao sqlDao) {
		this.sqlDao = sqlDao;
	}

	@Override
	public HmiResultObj createPtAction(PtAction ptAction) {
		HmiResultObj hmiResultObj=null;
		try {
			sqlDao.insertUpdate(ptAction);
			hmiResultObj=new HmiResultObj();
			//hmiResultObj.setResultObj(member);
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS);
			hmiResultObj.setResultMessage(ptAction.getPtaSubject());
		} catch (SqlErrorException e) {
			e.printStackTrace();
			hmiResultObj=new HmiResultObj();
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
		}
		return hmiResultObj;
	}

	@Override
	public HmiResultObj deletePtAction(PtAction ptAction) {
		HmiResultObj hmiResultObj=null;
		try {
			sqlDao.delete(ptAction);
			hmiResultObj=new HmiResultObj();
			//hmiResultObj.setResultObj(member);
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS);
			
		} catch (SqlErrorException e) {
			hmiResultObj=new HmiResultObj();
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
		}
		return hmiResultObj;
	}

	@Override
	public List<PtAction> findPtActionForMe(long ptaTo, int limit, int pointer) {
		String sql="SELECT * FROM pt_action "
				+ " WHERE PTA_TO in (:ptaTo)"
				+ " ORDER BY CREATE_TIME DESC "
				+ " LIMIT :limit,:pointer";
		
		Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("ptaTo",ptaTo);
		 paramMap.put("limit",limit);
		 paramMap.put("pointer",pointer);
		 try{
			 List<PtAction> ptActions=findEntityList(sql, paramMap, PtAction.class);
			 return ptActions;
		 }catch (Exception e) {
				return null;
		 }
	}

	@Override
	public List<PtAction> findPtActionForAll(int limit, int pointer) {
		String sql="SELECT * FROM pt_action "
				+ " WHERE PTA_TO in (:ptaPublic)"
				+ " ORDER BY CREATE_TIME DESC "
				+ " LIMIT :limit,:pointer";
		
		Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("ptaPublic",ActionTypes.PTA_PUBLIC);
		 paramMap.put("limit",limit);
		 paramMap.put("pointer",pointer);
		 try{
			 List<PtAction> ptActions=findEntityList(sql, paramMap, PtAction.class);
			 return ptActions;
		 }catch (Exception e) {
				return null;
		 }
	}

	@Override
	public List<PtAction> findPtActionForAllAndMe(long ptaTo, int limit, int pointer) {
		String sql="SELECT * FROM pt_action "
				+ " WHERE PTA_TO in (:ptaTo,:ptaPublic)"
				+ " ORDER BY CREATE_TIME DESC "
				+ " LIMIT :limit,:pointer";
		
		Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("ptaTo",ptaTo);
		 paramMap.put("ptaPublic",ActionTypes.PTA_PUBLIC);
		 paramMap.put("limit",limit);
		 paramMap.put("pointer",pointer);
		 try{
			 List<PtAction> ptActions=findEntityList(sql, paramMap, PtAction.class);
			 return ptActions;
		 }catch (Exception e) {
				return null;
		 }
	}

	@Override
	public List<PtAction> findPtActionForAllAndUid(long ptaUid, int limit, int pointer) {
		String sql="SELECT * FROM pt_action "
				+ " WHERE PTA_UID=:ptaUid "
				+ " ORDER BY CREATE_TIME DESC "
				+ " LIMIT :limit,:pointer ";
		
		Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("ptaUid",ptaUid);
		 paramMap.put("limit",limit);
		 paramMap.put("pointer",pointer);
		 try{
			 List<PtAction> ptActions=findEntityList(sql, paramMap, PtAction.class);
			 return ptActions;
		 }catch (Exception e) {
				return null;
		 }
	}

	@Override
	public List<PtRules> findPtRules() {
		String sql="SELECT * FROM pt_rules";
		 try{
			 List<PtRules> ptRules=findEntityList(sql, null, PtRules.class);
			 return ptRules;
		 }catch (Exception e) {
				return null;
		 }
	}

	@Override
	public PtRules findPtRuleById(int ruleId) {
		String sql="SELECT * FROM pt_rules"
				+ " WHERE RULE_ID=:ruleId";
		
		Map<String, Object> paramMap=new HashMap<String, Object>();
		paramMap.put("ruleId",ruleId);
		 
		
		try{
			 PtRules ptRules=findEntityForObject(sql, paramMap, PtRules.class);
			 return ptRules;
		 }catch (Exception e) {
				return null;
		 }
	}

	@Override
	public synchronized HmiResultObj createUpdateRule(PtRules ptRules) {
		HmiResultObj hmiResultObj=null;
		try {
			sqlDao.insertUpdate(ptRules);
			hmiResultObj=new HmiResultObj();
			//hmiResultObj.setResultObj(member);
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS);
			hmiResultObj.setResultMessage(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
		} catch (SqlErrorException e) {
			e.printStackTrace();
			hmiResultObj=new HmiResultObj();
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
			hmiResultObj.setResultMessage(ResultStatuObj.RESULT_STATU_FAIL_STR);
		}
		return hmiResultObj;
	}

	@Override
	public PtGlobal findPtGlobal() {
		String sql="SELECT * FROM pt_global LIMIT 1";
		
		 
		
		try{
			PtGlobal ptGlobal=findEntityForObject(sql, null, PtGlobal.class);
			 return ptGlobal;
		 }catch (Exception e) {
			 e.printStackTrace();
				return null;
		 }
	}

	@Override
	public synchronized HmiResultObj createUpdatePtGlobal(PtGlobal ptGlobal) {
		HmiResultObj hmiResultObj=null;
		try {
			sqlDao.insertUpdate(ptGlobal);
			hmiResultObj=new HmiResultObj();
			//hmiResultObj.setResultObj(member);
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS);
			hmiResultObj.setResultMessage(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
		} catch (SqlErrorException e) {
			e.printStackTrace();
			hmiResultObj=new HmiResultObj();
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
			hmiResultObj.setResultMessage(ResultStatuObj.RESULT_STATU_FAIL_STR);
		}
		return hmiResultObj;
	}

	/*
	@Override
	public HmiResultObj createDbHostTbl(DbHostTbl dbHostTbl) {
		HmiResultObj hmiResultObj=null;
		try {
			
			DbHostTbl dbmt=findDbHostTbl();
			sqlDao.delete(dbmt);
			
			
			sqlDao.insertUpdate(dbHostTbl);
			hmiResultObj=new HmiResultObj();
			//hmiResultObj.setResultObj(member);
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS);
			hmiResultObj.setResultMessage(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
		} catch (SqlErrorException e) {
			e.printStackTrace();
			hmiResultObj=new HmiResultObj();
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
			hmiResultObj.setResultMessage(ResultStatuObj.RESULT_STATU_FAIL_STR);
		}
		return hmiResultObj;
	}*/

	
	
	
	@Override
	public HmiResultObj createDbMailTbl(DbMailTbl dbMailTbl) {
		HmiResultObj hmiResultObj=null;
		try {
			
			DbMailTbl dbmt=findDbMailTbl();
			if(dbmt!=null)
			    sqlDao.delete(dbmt);
			
			
			
			sqlDao.insertUpdate(dbMailTbl);
			
			GlobalUtil.mailSettings=dbMailTbl;
			
			hmiResultObj=new HmiResultObj();
			//hmiResultObj.setResultObj(member);
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS);
			hmiResultObj.setResultMessage(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
		} catch (SqlErrorException e) {
			e.printStackTrace();
			hmiResultObj=new HmiResultObj();
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
			hmiResultObj.setResultMessage(ResultStatuObj.RESULT_STATU_FAIL_STR);
		}
		return hmiResultObj;
	}

	@Override
	public DbHostTbl findDbHostTbl() {
		String sql="SELECT * FROM db_host_tbl LIMIT 1";
		try{
			DbHostTbl dbHostTbl=findEntityForObject(sql, null, DbHostTbl.class);
			 return dbHostTbl;
		 }catch (Exception e) {
			 	return null;
		 }
	}

	@Override
	public DbMailTbl findDbMailTbl() {
		String sql="SELECT * FROM db_mail_tbl LIMIT 1";
		try{
			DbMailTbl dbMailTbl=findEntityForObject(sql, null, DbMailTbl.class);
			 return dbMailTbl;
		 }catch (Exception e) {
				return null;
		 }
	}

	
	
}
