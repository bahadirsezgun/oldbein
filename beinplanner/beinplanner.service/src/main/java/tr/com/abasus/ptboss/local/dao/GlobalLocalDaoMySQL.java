package tr.com.abasus.ptboss.local.dao;

import java.util.HashMap;
import java.util.Map;

import tr.com.abasus.general.dao.AbaxJdbcDaoSupport;
import tr.com.abasus.general.dao.SqlDao;
import tr.com.abasus.general.exception.SqlErrorException;
import tr.com.abasus.ptboss.local.entity.DbHostTbl;
import tr.com.abasus.ptboss.result.entity.HmiResultObj;
import tr.com.abasus.ptboss.settings.entity.PtRestrictions;
import tr.com.abasus.util.ResultStatuObj;

public class GlobalLocalDaoMySQL extends AbaxJdbcDaoSupport implements GlobalLocalDao {

	SqlDao sqlDaoLocal;

	public SqlDao getSqlDaoLocal() {
		return sqlDaoLocal;
	}


	public void setSqlDaoLocal(SqlDao sqlDaoLocal) {
		this.sqlDaoLocal = sqlDaoLocal;
	}
	
	@Override
	public DbHostTbl findDbHost() {
		String sql="SELECT * FROM db_host_tbl LIMIT 1";
		
		 
		
		try{
			DbHostTbl dbHostTbl=findEntityForObject(sql, null, DbHostTbl.class);
			 return dbHostTbl;
		 }catch (Exception e) {
			 e.printStackTrace();
				return null;
		 }
	}




	@Override
	public HmiResultObj createDbHost(DbHostTbl dbHostTbl) {
		HmiResultObj hmiResultObj=null;
		try {
			
			DbHostTbl dbmt=findDbHost();
			sqlDaoLocal.delete(dbmt);
			
			
			sqlDaoLocal.insertUpdate(dbHostTbl);
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
	public PtRestrictions findRestrictionsForCount(String myPass) {
		
		String sql=" SELECT RES_ID,   " +
					" CAST(AES_DECRYPT(FIRM_COUNT,:myPas1) AS CHAR) firmCount ," +
					" CAST(AES_DECRYPT(TEACHER_COUNT,:myPas2) AS CHAR) teacherCount " +
					" FROM pt_restrictions WHERE RES_ID=1 LIMIT 1";
		
		Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("myPas1",myPass);
		 paramMap.put("myPas2",myPass);
		
		 
		try{
			 PtRestrictions ptRestrictions=findEntityForObject(sql, paramMap, PtRestrictions.class);
			 return ptRestrictions;
		 }catch (Exception e) {
			 e.printStackTrace();
				return null;
		 }
	}
	
	@Override
	public PtRestrictions findRestrictionsForPacket(String myPass) {
		
		String sql=" SELECT RES_ID,   " +
					" CAST(AES_DECRYPT(FIRM_COUNT,:myPas1) AS CHAR) firmCount ," +
					" CAST(AES_DECRYPT(TEACHER_COUNT,:myPas2) AS CHAR) teacherCount " +
					" FROM pt_restrictions WHERE RES_ID=2 LIMIT 1";
		
		Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("myPas1",myPass);
		 paramMap.put("myPas2",myPass);
		
		 
		try{
			 PtRestrictions ptRestrictions=findEntityForObject(sql, paramMap, PtRestrictions.class);
			 return ptRestrictions;
		 }catch (Exception e) {
			 return null;
		 }
	}


	@Override
	public HmiResultObj updateRestrictionsForCountCampain(int studioCount,int teacherCount) {
		
		HmiResultObj hmiResultObj=new HmiResultObj();
		String sql=" UPDATE pt_restrictions "+
					" SET firm_count=AES_ENCRYPT(:studioCount,'canan3367sagali7611'), "+
					" teacher_count=AES_ENCRYPT(:teacherCount,'canan3367sagali7611') "+
					" WHERE RES_ID=1";
	
	
		Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("studioCount",studioCount);
		 paramMap.put("teacherCount",teacherCount);
		
	try{
	     update(sql, paramMap);
	     hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS);
	 }catch (Exception e) {
		 e.printStackTrace();
		 hmiResultObj.setResultMessage(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
	 }
	
	
	//hmiResultObj.setResultObj(member);
	
	return hmiResultObj;
	}


	@Override
	public HmiResultObj updateRestrictionsForPacketCampain(String packetType) {
		HmiResultObj hmiResultObj=new HmiResultObj();
		String sql=" UPDATE pt_restrictions "+
					" SET firm_count=AES_ENCRYPT(:packetType,'canan3367sagali7611'), "+
					" teacher_count=AES_ENCRYPT('0','canan3367sagali7611') "+
					" WHERE RES_ID=2";
	
	
		Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("packetType",packetType);
		 
	try{
	     update(sql, paramMap);
	     hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS);
	 }catch (Exception e) {
		 e.printStackTrace();
		 hmiResultObj.setResultMessage(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
	 }
	
	
	//hmiResultObj.setResultObj(member);
	
	return hmiResultObj;
	}
	
	/*
	 * 
UPDATE pt_restrictions
SET firm_count=AES_ENCRYPT('2','canan3367sagali7611'),
teacher_count=AES_ENCRYPT('0','canan3367sagali7611')
WHERE RES_ID=1

UPDATE pt_restrictions
SET firm_count=AES_ENCRYPT('1-1-1','canan3367sagali7611'),
teacher_count=AES_ENCRYPT('0','canan3367sagali7611')
WHERE RES_ID=2

SELECT RES_ID,   
CAST(AES_DECRYPT(FIRM_COUNT,'canan3367sagali7611') AS CHAR) firmCount ,
CAST(AES_DECRYPT(TEACHER_COUNT,'canan3367sagali7611') AS CHAR) teacherCount 
FROM pt_restrictions

	 */
}
