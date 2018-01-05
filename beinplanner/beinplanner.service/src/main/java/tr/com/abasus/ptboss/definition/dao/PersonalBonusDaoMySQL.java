package tr.com.abasus.ptboss.definition.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tr.com.abasus.general.dao.AbaxJdbcDaoSupport;
import tr.com.abasus.general.dao.SqlDao;
import tr.com.abasus.general.exception.SqlErrorException;
import tr.com.abasus.ptboss.definition.entity.DefBonus;
import tr.com.abasus.ptboss.definition.entity.DefStudio;
import tr.com.abasus.ptboss.result.entity.HmiResultObj;
import tr.com.abasus.util.BonusTypes;
import tr.com.abasus.util.GlobalUtil;
import tr.com.abasus.util.ResultStatuObj;

public class PersonalBonusDaoMySQL extends AbaxJdbcDaoSupport implements PersonalBonusDao {

	SqlDao sqlDao;
	public SqlDao getSqlDao() {
		return sqlDao;
	}

	public void setSqlDao(SqlDao sqlDao) {
		this.sqlDao = sqlDao;
	}
	@Override
	public List<DefBonus> findPersonalRateBonus(long userId) {
		String sql="SELECT a.* "
				+ "  FROM def_bonus a "
				+ "  WHERE a.USER_ID=:userId "
				+ "    AND BONUS_TYPE="+BonusTypes.BONUS_TYPE_PERSONAL
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
	public HmiResultObj createPersonalRateBonus(DefBonus defBonus) {
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
	public HmiResultObj deletePersonalRateBonus(long bonusId) {
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
	public List<DefBonus> findPersonalStaticBonus(long userId) {
		String sql="SELECT a.*,b.* "
				+ "  FROM def_bonus a,program_personal b "
				+ "  WHERE a.USER_ID=:userId "
				+ "    AND a.BONUS_TYPE="+BonusTypes.BONUS_TYPE_PERSONAL
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
	public HmiResultObj createPersonalStaticBonus(DefBonus defBonus) {
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
	public HmiResultObj deletePersonalStaticBonus(long bonusId) {
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
	public List<DefBonus> findPersonalStaticRateBonus(long userId) {
		String sql="SELECT a.*,b.* "
				+ "  FROM def_bonus a,program_personal b "
				+ "  WHERE a.USER_ID=:userId "
				+ "    AND a.BONUS_TYPE="+BonusTypes.BONUS_TYPE_PERSONAL
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
	public HmiResultObj createPersonalStaticRateBonus(DefBonus defBonus) {
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
	public HmiResultObj deletePersonalStaticRateBonus(long bonusId) {
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
