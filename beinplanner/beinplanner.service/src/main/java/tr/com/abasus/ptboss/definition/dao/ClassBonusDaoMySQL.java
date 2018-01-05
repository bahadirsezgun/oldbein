package tr.com.abasus.ptboss.definition.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tr.com.abasus.general.dao.AbaxJdbcDaoSupport;
import tr.com.abasus.general.dao.SqlDao;
import tr.com.abasus.general.exception.SqlErrorException;
import tr.com.abasus.ptboss.definition.entity.DefBonus;
import tr.com.abasus.ptboss.result.entity.HmiResultObj;
import tr.com.abasus.util.BonusTypes;
import tr.com.abasus.util.ResultStatuObj;

public class ClassBonusDaoMySQL extends AbaxJdbcDaoSupport implements ClassBonusDao {

	SqlDao sqlDao;
	public SqlDao getSqlDao() {
		return sqlDao;
	}

	public void setSqlDao(SqlDao sqlDao) {
		this.sqlDao = sqlDao;
	}
	@Override
	public List<DefBonus> findClassRateBonus(long userId) {
		String sql="SELECT a.* "
				+ "  FROM def_bonus a "
				+ "  WHERE a.USER_ID=:userId "
				+ "    AND BONUS_TYPE="+BonusTypes.BONUS_TYPE_CLASS
				+ "    AND a.BONUS_IS_TYPE="+BonusTypes.BONUS_IS_TYPE_RATE
				+ "  ORDER BY BONUS_VALUE ";
		
		 Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("userId",userId);
		 
		  
	 	try {
			 List<DefBonus> defBonuses=findEntityList(sql, paramMap, DefBonus.class);
			 return defBonuses;
		}catch (Exception e) {
			 return null;
		}
	}

	@Override
	public HmiResultObj createClassRateBonus(DefBonus defBonus) {
		HmiResultObj hmiResultObj=null;
		try {
			sqlDao.insertUpdate(defBonus);
			hmiResultObj=new HmiResultObj();
			//hmiResultObj.setResultObj(member);
			hmiResultObj.setResultMessage("");
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS);
		} catch (SqlErrorException e) {
			e.printStackTrace();
			hmiResultObj=new HmiResultObj();
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
		}
		return hmiResultObj;
	}

	@Override
	public HmiResultObj deleteClassRateBonus(long bonusId) {
		HmiResultObj hmiResultObj=null;
		try {
			DefBonus defBonus=new DefBonus();
			defBonus.setBonusId(bonusId);
			
			sqlDao.delete(defBonus);
			hmiResultObj=new HmiResultObj();
			//hmiResultObj.setResultObj(member);
			hmiResultObj.setResultMessage("");
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS);
		} catch (SqlErrorException e) {
			e.printStackTrace();
			hmiResultObj=new HmiResultObj();
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
		}
		return hmiResultObj;
	}

	@Override
	public List<DefBonus> findClassStaticBonus(long userId) {
		String sql="SELECT a.*,b.* "
				+ "  FROM def_bonus a,program_class b "
				+ "  WHERE a.USER_ID=:userId "
				+ "    AND a.BONUS_TYPE="+BonusTypes.BONUS_TYPE_CLASS
				+ "    AND b.PROG_ID=a.BONUS_PROG_ID "
				+ "    AND a.BONUS_IS_TYPE="+BonusTypes.BONUS_IS_TYPE_STATIC
				+ "  ORDER BY BONUS_ID ";
		
		 Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("userId",userId);
		 
		  
	 	try {
			 List<DefBonus> defBonuses=findEntityList(sql, paramMap, DefBonus.class);
			 return defBonuses;
		}catch (Exception e) {
			 return null;
		}
	}

	@Override
	public HmiResultObj createClassStaticBonus(DefBonus defBonus) {
		HmiResultObj hmiResultObj=null;
		try {
			sqlDao.insertUpdate(defBonus);
			hmiResultObj=new HmiResultObj();
			//hmiResultObj.setResultObj(member);
			hmiResultObj.setResultMessage("");
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS);
		} catch (SqlErrorException e) {
			e.printStackTrace();
			hmiResultObj=new HmiResultObj();
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
		}
		return hmiResultObj;
	}

	@Override
	public HmiResultObj deleteClassStaticBonus(long bonusId) {
		HmiResultObj hmiResultObj=null;
		try {
			DefBonus defBonus=new DefBonus();
			defBonus.setBonusId(bonusId);
			
			sqlDao.delete(defBonus);
			hmiResultObj=new HmiResultObj();
			//hmiResultObj.setResultObj(member);
			hmiResultObj.setResultMessage("");
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS);
		} catch (SqlErrorException e) {
			e.printStackTrace();
			hmiResultObj=new HmiResultObj();
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
		}
		return hmiResultObj;
	}

	@Override
	public List<DefBonus> findClassStaticRateBonus(long userId) {
		String sql="SELECT a.*,b.* "
				+ "  FROM def_bonus a,program_class b "
				+ "  WHERE a.USER_ID=:userId "
				+ "    AND a.BONUS_TYPE="+BonusTypes.BONUS_TYPE_CLASS
				+ "    AND b.PROG_ID=a.BONUS_PROG_ID "
				+ "    AND a.BONUS_IS_TYPE="+BonusTypes.BONUS_IS_TYPE_STATIC_RATE
				+ "  ORDER BY BONUS_ID ";
		
		 Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("userId",userId);
		 
		  
	 	try {
			 List<DefBonus> defBonuses=findEntityList(sql, paramMap, DefBonus.class);
			 return defBonuses;
		}catch (Exception e) {
			 return null;
		}
	}

	@Override
	public HmiResultObj createClassStaticRateBonus(DefBonus defBonus) {
		HmiResultObj hmiResultObj=null;
		try {
			sqlDao.insertUpdate(defBonus);
			hmiResultObj=new HmiResultObj();
			//hmiResultObj.setResultObj(member);
			hmiResultObj.setResultMessage("");
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS);
		} catch (SqlErrorException e) {
			e.printStackTrace();
			hmiResultObj=new HmiResultObj();
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
		}
		return hmiResultObj;
	}

	@Override
	public HmiResultObj deleteClassStaticRateBonus(long bonusId) {
		HmiResultObj hmiResultObj=null;
		try {
			DefBonus defBonus=new DefBonus();
			defBonus.setBonusId(bonusId);
			
			sqlDao.delete(defBonus);
			hmiResultObj=new HmiResultObj();
			//hmiResultObj.setResultObj(member);
			hmiResultObj.setResultMessage("");
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS);
		} catch (SqlErrorException e) {
			e.printStackTrace();
			hmiResultObj=new HmiResultObj();
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
		}
		return hmiResultObj;
	}

}
