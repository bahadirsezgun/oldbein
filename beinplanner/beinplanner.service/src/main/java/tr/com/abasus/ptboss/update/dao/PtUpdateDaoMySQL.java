package tr.com.abasus.ptboss.update.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tr.com.abasus.general.dao.AbaxJdbcDaoSupport;
import tr.com.abasus.general.dao.SqlDao;
import tr.com.abasus.general.exception.SqlErrorException;
import tr.com.abasus.ptboss.result.entity.HmiResultObj;
import tr.com.abasus.ptboss.update.entity.PtbossUpdate;
import tr.com.abasus.util.GlobalUtil;
import tr.com.abasus.util.ResultStatuObj;

public class PtUpdateDaoMySQL extends AbaxJdbcDaoSupport implements PtUpdateDao  {

	SqlDao sqlDao;
	
	public SqlDao getSqlDao() {
		return sqlDao;
	}

	public void setSqlDao(SqlDao sqlDao) {
		this.sqlDao = sqlDao;
	}

	@Override
	public PtbossUpdate findPtBossUpdateByVersion(String version) {
		String sql="SELECT * FROM ptboss_update "
				+ " WHERE UPD_VER=:version "
				+ " LIMIT 1";
		 Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("version",version);
		 
		 try{
			 List<PtbossUpdate> ptbossUpdates=findEntityList(sql, paramMap, PtbossUpdate.class);
			 if(ptbossUpdates.size()>0){
				 return ptbossUpdates.get(0);
			 }else{
				 PtbossUpdate ptbossUpdate= new PtbossUpdate();
				 ptbossUpdate.setUpdDate(new Date());
				 ptbossUpdate.setUpdIdx(0);
				 ptbossUpdate.setUpdVer("0");
				 return ptbossUpdate; 
			 }
			 
			 
				
		 }catch (Exception e) {
			e.printStackTrace();
			 return null;
		 }
	}

	@Override
	public HmiResultObj createPtBossUpdate(PtbossUpdate ptbossUpdate) {
		HmiResultObj hmiResultObj=null;
		try {
			sqlDao.insertUpdate(ptbossUpdate);
			
			hmiResultObj=new HmiResultObj();
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
	public void createIndex(String sql) {
		
		try {
			update(sql, null,null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void createTable(String sql, String table) {
		if(!findTable(table)){
		  try {
				update(sql, null,null);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	  	}
	}

	@Override
	public void createTableOfColumn(String sql, String table, String col)  {
		if(!findColumnOfTable(table,col)){
    		
			try {
				update(sql, null,null);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 }
		
	}
	
	@Override
	public void updateTableOfColumn(String sql, String table, String col) {
		if(!findAutoIncrementColumnOfTable(table,col)){
    		
			try {
				update(sql, null,null);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 }
		
	}
	
	public boolean findTable(String tableName) {
		String sql="SELECT TABLE_NAME "+
                " FROM information_schema.TABLES "+
                " WHERE table_name = :tableName "+
                " AND table_schema = 'ptboss' ";
		
		Map<String, Object> paramMap=new HashMap<String, Object>();
		paramMap.put("tableName",tableName);
		
		try {
			String tblName= findEntityForObject(sql, paramMap, String.class);
	        if(tblName.equals(null)){
	        	return false;
	        }else{
	        	
	        	return true;
	        }
	
			
		} catch (Exception e) {
			//e.printStackTrace();
			return false;
	}
    }
	
	public boolean findColumnOfTable(String tableName,String columnName){
		
		String sql="SELECT COLUMN_NAME "+
                    " FROM information_schema.COLUMNS"+
                    " WHERE table_name = :tableName"+
                    " AND table_schema = 'ptboss'"+
                    " AND column_name =:columnName";
		Map<String, Object> paramMap=new HashMap<String, Object>();
		paramMap.put("tableName",tableName);
		paramMap.put("columnName",columnName);
		
		try {
			String colName= findEntityForObject(sql, paramMap, String.class);
            if(colName.equals(null)){
            	return false;
            }else{
            	return true;
            }

			
		} catch (Exception e) {
			//e.printStackTrace();
			return false;
		}
		
		
	}
	
  public boolean findAutoIncrementColumnOfTable(String tableName,String columnName){
		
		String sql="SELECT COLUMN_NAME "+
                    " FROM information_schema.COLUMNS"+
                    " WHERE table_name = :tableName"+
                    " AND table_schema = 'ptboss'"+
                    " AND column_name =:columnName"
                    + "AND EXTRA like '%auto_increment%'";
		Map<String, Object> paramMap=new HashMap<String, Object>();
		paramMap.put("tableName",tableName);
		paramMap.put("columnName",columnName);
		
		try {
			String colName= findEntityForObject(sql, paramMap, String.class);
            if(colName.equals(null)){
            	return false;
            }else{
            	return true;
            }

			
		} catch (Exception e) {
			//e.printStackTrace();
			return false;
		}
		
		
	}

	@Override
	public PtbossUpdate findPtBossUpdateByLast() {
		String sql="SELECT *"
				+ ",DATE_FORMAT(UPD_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') UPD_DATE_STR  "
				+ " FROM ptboss_update "
				+ " ORDER BY  UPD_IDX DESC "
				+ " LIMIT 1";
		
		 try{
			  return findEntityForObject(sql, null, PtbossUpdate.class);
		  }catch (Exception e) {
			 return new PtbossUpdate();
		  }
	}


	
	
	

}
