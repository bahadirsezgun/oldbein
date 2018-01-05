package tr.com.abasus.ptboss.definition.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tr.com.abasus.general.dao.AbaxJdbcDaoSupport;
import tr.com.abasus.general.dao.SqlDao;
import tr.com.abasus.general.exception.SqlErrorException;
import tr.com.abasus.ptboss.definition.entity.DefCalendarTimes;
import tr.com.abasus.ptboss.definition.entity.DefCity;
import tr.com.abasus.ptboss.definition.entity.DefFirm;
import tr.com.abasus.ptboss.definition.entity.DefLevelInfo;
import tr.com.abasus.ptboss.definition.entity.DefState;
import tr.com.abasus.ptboss.definition.entity.DefStudio;
import tr.com.abasus.ptboss.local.dao.GlobalLocalDao;
import tr.com.abasus.ptboss.result.entity.HmiResultObj;
import tr.com.abasus.ptboss.settings.dao.SettingDao;
import tr.com.abasus.ptboss.settings.entity.PtRestrictions;
import tr.com.abasus.util.GlobalUtil;
import tr.com.abasus.util.OhbeUtil;
import tr.com.abasus.util.ResultStatuObj;
import tr.com.abasus.util.StatuTypes;

public class DefinitionDaoMySQL  extends AbaxJdbcDaoSupport implements DefinitionDao{

	SqlDao sqlDao;
	
	GlobalLocalDao globalLocalDao; 

	
	
	
	@Override
	public List<DefStudio> findAllStudios(int firmId) {
		String sql="SELECT a.*,DATE_FORMAT(a.CREATE_TIME,'"+GlobalUtil.global.getPtDateFormat()+"') CREATE_TIME_STR,b.* "
				+ "  FROM def_studio a,def_firm b"
				+ "  WHERE a.FIRM_ID=:firmId "
				+ "    AND a.FIRM_ID=b.FIRM_ID"
				+ "    ORDER BY a.FIRM_ID";
		
		 Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("firmId",firmId);
		 
		  
	 	try {
			 List<DefStudio> defStudios=findEntityList(sql, paramMap, DefStudio.class);
			 return defStudios;
		}catch (Exception e) {
			 return null;
		}
	}
	
	@Override
	public List<DefStudio> findAllStudiosForDefinition() {
		String sql="SELECT a.*,DATE_FORMAT(a.CREATE_TIME,'"+GlobalUtil.global.getPtDateFormat()+"') CREATE_TIME_STR,b.* "
				+ "  FROM def_studio a,def_firm b"
				+ "  WHERE a.FIRM_ID=b.FIRM_ID"
				+ "    ORDER BY a.FIRM_ID ";
		
		 
		   
	 	try {
			 List<DefStudio> defStudios=findEntityList(sql, null, DefStudio.class);
			 
			 return defStudios;
		}catch (Exception e) {
			e.printStackTrace();
			 return null;
		}
	}

	@Override
	public DefStudio findStudioById(int studioId) {
		String sql="SELECT a.*,DATE_FORMAT(a.CREATE_TIME,'"+GlobalUtil.global.getPtDateFormat()+"') CREATE_TIME_STR,b.* "
				+ "  FROM def_studio a,def_firm b"
				+ "  WHERE a.FIRM_ID=b.FIRM_ID "
				+ "    AND a.STUDIO_ID=:studioId"
				+ "    ORDER BY a.FIRM_ID ";
		
		 Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("studioId",studioId);
		 
		   
	 	try {
			 DefStudio defStudio=findEntityForObject(sql, paramMap, DefStudio.class);
			 return defStudio;
		}catch (Exception e) {
			 return null;
		}
	}
	
	@Override
	public List<DefStudio> findAllStudiosByStatus(int firmId,int status) {
		String sql="SELECT * FROM def_studio"
				+ "  WHERE FIRM_ID=:firmId ";
				
				if(status!=StatuTypes.ALL)
					sql+= "    AND STUDIO_STATU=:status";
		
		 Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("firmId",firmId);
		 paramMap.put("status",status);
			 
		  
		 try {
			 List<DefStudio> defStudios=findEntityList(sql, paramMap, DefStudio.class);
			 return defStudios;
			} catch (Exception e) {
			 return null;
			}
	}

	@Override
	public synchronized HmiResultObj createStudio(DefStudio defStudio) {
		HmiResultObj hmiResultObj=null;
		try {
			sqlDao.insertUpdate(defStudio);
			hmiResultObj=new HmiResultObj();
			//hmiResultObj.setResultObj(member);
			hmiResultObj.setResultMessage(getLastStudioIdSeq());
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS);
		} catch (SqlErrorException e) {
			e.printStackTrace();
			hmiResultObj=new HmiResultObj();
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
		}
		return hmiResultObj;
	}

	
	private String getLastStudioIdSeq(){
		String sql=	"SELECT CAST(max(STUDIO_ID) AS CHAR) STUDIO_ID" +
				" FROM def_studio ";
		String studioId="1";
		try {
			studioId=""+findEntityForObject(sql, null, Long.class);
			if(studioId.equals("null"))
				studioId="1";
		} catch (Exception e) {
			studioId="1";
			e.printStackTrace();
		}
		return studioId;

	}
	
	@Override
	public HmiResultObj updateStudio(DefStudio defStudio) {
		HmiResultObj hmiResultObj=null;
		try {
			sqlDao.insertUpdate(defStudio);
			hmiResultObj=new HmiResultObj();
			//hmiResultObj.setResultObj(member);
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS);
		} catch (SqlErrorException e) {
			e.printStackTrace();
			hmiResultObj=new HmiResultObj();
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
		}
		return hmiResultObj;
	}

	@Override
	public HmiResultObj deleteStudio(DefStudio defStudio) {
		HmiResultObj hmiResultObj=null;
		try {
			sqlDao.delete(defStudio);
			hmiResultObj=new HmiResultObj();
			//hmiResultObj.setResultObj(member);
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS);
		} catch (SqlErrorException e) {
			e.printStackTrace();
			hmiResultObj=new HmiResultObj();
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
		}
		return hmiResultObj;
	}

	

	@Override
	public List<DefCity> findCities(int stateId) {
		String sql="SELECT * FROM def_city "
				+ "  WHERE STATE_ID=:stateId ";
		
		 Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("stateId",stateId);
		 
		 ////System.out.println(sql);
		  
	 	try {
			 List<DefCity> defCites=findEntityList(sql, paramMap, DefCity.class);
			 for (DefCity defCity : defCites) {
				////System.out.println("defCity "+defCity.getCityName());
			}
			 
			 return defCites;
		}catch (Exception e) {
			e.printStackTrace();
			 return null;
		}
	}

	@Override
	public synchronized HmiResultObj createCity(DefCity defCity) {
		HmiResultObj hmiResultObj=null;
		try {
			sqlDao.insertUpdate(defCity);
			hmiResultObj=new HmiResultObj();
			//hmiResultObj.setResultObj(member);
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS);
		} catch (SqlErrorException e) {
			e.printStackTrace();
			hmiResultObj=new HmiResultObj();
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
		}
		return hmiResultObj;
	}

	@Override
	public HmiResultObj updateCity(DefCity defCity) {
		HmiResultObj hmiResultObj=null;
		try {
			sqlDao.insertUpdate(defCity);
			hmiResultObj=new HmiResultObj();
			//hmiResultObj.setResultObj(member);
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS);
		} catch (SqlErrorException e) {
			e.printStackTrace();
			hmiResultObj=new HmiResultObj();
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
		}
		return hmiResultObj;
	}

	@Override
	public HmiResultObj deleteCity(DefCity defCity) {
		HmiResultObj hmiResultObj=null;
		try {
			sqlDao.delete(defCity);
			hmiResultObj=new HmiResultObj();
			//hmiResultObj.setResultObj(member);
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS);
		} catch (SqlErrorException e) {
			e.printStackTrace();
			hmiResultObj=new HmiResultObj();
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
		}
		return hmiResultObj;
	}

	@Override
	public List<DefState> findStates() {
		String sql="SELECT *,DATE_FORMAT(CREATE_TIME,'"+GlobalUtil.global.getPtDateFormat()+"') CREATE_TIME_STR"
				+ "  FROM def_state ";
		
		 
	 	try {
			 List<DefState> defStates=findEntityList(sql, null, DefState.class);
			 return defStates;
		}catch (Exception e) {
			 return null;
		}
	}

	@Override
	public synchronized HmiResultObj createState(DefState defState) {
		HmiResultObj hmiResultObj=null;
		try {
			sqlDao.insertUpdate(defState);
			hmiResultObj=new HmiResultObj();
			//hmiResultObj.setResultObj(member);
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS);
		} catch (SqlErrorException e) {
			e.printStackTrace();
			hmiResultObj=new HmiResultObj();
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
		}
		return hmiResultObj;
	}

	@Override
	public HmiResultObj updateState(DefState defState) {
		HmiResultObj hmiResultObj=null;
		try {
			sqlDao.insertUpdate(defState);
			hmiResultObj=new HmiResultObj();
			//hmiResultObj.setResultObj(member);
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS);
		} catch (SqlErrorException e) {
			e.printStackTrace();
			hmiResultObj=new HmiResultObj();
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
		}
		return hmiResultObj;
	}

	@Override
	public HmiResultObj deleteState(DefState defState) {
		HmiResultObj hmiResultObj=null;
		try {
			sqlDao.delete(defState);
			hmiResultObj=new HmiResultObj();
			//hmiResultObj.setResultObj(member);
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS);
		} catch (SqlErrorException e) {
			e.printStackTrace();
			hmiResultObj=new HmiResultObj();
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
		}
		return hmiResultObj;
	}

	

	@Override
	public List<DefFirm> findFirms() {
		String sql="SELECT *,DATE_FORMAT(CREATE_TIME,'"+GlobalUtil.global.getPtDateFormat()+"') CREATE_TIME_STR FROM def_firm ";
		
		 
	 	try {
			 List<DefFirm> defStates=findEntityList(sql, null, DefFirm.class);
			 return defStates;
		}catch (Exception e) {
			 return null;
		}
	}

	@Override
	public DefFirm findFirmsById(int firmId) {
		String sql="SELECT *,DATE_FORMAT(CREATE_TIME,'"+GlobalUtil.global.getPtDateFormat()+"') CREATE_TIME_STR FROM def_firm"
				+ " WHERE FIRM_ID=:firmId ";
		
		Map<String, Object> paramMap=new HashMap<String, Object>();
		paramMap.put("firmId",firmId);
		 
	 	try {
			 DefFirm defFirm=findEntityForObject(sql, paramMap, DefFirm.class);
			 return defFirm;
		}catch (Exception e) {
			 return null;
		}
	}

	@Override
	public synchronized HmiResultObj  createFirms(DefFirm defFirm) {
		HmiResultObj hmiResultObj=null;
		
		List<DefFirm> firms=findFirms();
		int firmSize=0;
		if(firms!=null)
			firmSize=firms.size();
		
		PtRestrictions ptRestrictions=globalLocalDao.findRestrictionsForCount(OhbeUtil.PTBOSS_MYPASS);
		if(canFirmCreate(ptRestrictions.getFirmCount().trim(), firmSize,defFirm.getFirmName())){
			
			try {
				sqlDao.insertUpdate(defFirm);
				hmiResultObj=new HmiResultObj();
				//hmiResultObj.setResultObj(member);
				hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS);
				DefFirm dFirm=findFirmByName(defFirm.getFirmName());
				hmiResultObj.setResultMessage(""+dFirm.getFirmId());
				
			} catch (SqlErrorException e) {
				e.printStackTrace();
				hmiResultObj=new HmiResultObj();
				hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
			}
		}else{
			hmiResultObj=new HmiResultObj();
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
			hmiResultObj.setResultMessage("canNotCreateNewFirm");
		}
		
		return hmiResultObj;
	}
	
	
	private DefFirm findFirmByName(String firmName){
		String sql="SELECT *,DATE_FORMAT(CREATE_TIME,'"+GlobalUtil.global.getPtDateFormat()+"') CREATE_TIME_STR FROM def_firm "
				+ " WHERE FIRM_NAME=:firmName LIMIT 1 ";
		
		Map<String, Object> paramMap=new HashMap<String, Object>();
		paramMap.put("firmName",firmName);
		 
	 	try {
			 DefFirm defFirm=findEntityForObject(sql, paramMap, DefFirm.class);
			 return defFirm;
		}catch (Exception e) {
			 return null;
		}
	}
	
	private boolean canFirmCreate(String firmCount,int createdCount,String firmName){
		
		DefFirm defFirm=findFirmByName(firmName);
		if(defFirm!=null){
			return false;
		}
			if(createdCount>=Integer.parseInt(firmCount.trim()))
				return false;
		
		
		return true;
	}

	@Override
	public HmiResultObj updateFirms(DefFirm defFirm) {
		HmiResultObj hmiResultObj=null;
		try {
			sqlDao.insertUpdate(defFirm);
			hmiResultObj=new HmiResultObj();
			//hmiResultObj.setResultObj(member);
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS);
		} catch (SqlErrorException e) {
			e.printStackTrace();
			hmiResultObj=new HmiResultObj();
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
		}
		return hmiResultObj;
	}

	@Override
	public HmiResultObj deleteFirms(DefFirm defFirm) {
		HmiResultObj hmiResultObj=null;
		try {
			sqlDao.delete(defFirm);
			hmiResultObj=new HmiResultObj();
			//hmiResultObj.setResultObj(member);
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS);
		} catch (SqlErrorException e) {
			e.printStackTrace();
			hmiResultObj=new HmiResultObj();
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
		}
		return hmiResultObj;
	}
	
	public SqlDao getSqlDao() {
		return sqlDao;
	}

	public void setSqlDao(SqlDao sqlDao) {
		this.sqlDao = sqlDao;
	}


	@Override
	public List<DefLevelInfo> findLevelInfo() {
		String sql="SELECT *,DATE_FORMAT(CREATE_TIME,'"+GlobalUtil.global.getPtDateFormat()+"') CREATE_TIME_STR FROM def_level_info ";
		
		 
	 	try {
	 		List<DefLevelInfo> defLevelInfos=findEntityList(sql, null, DefLevelInfo.class);
			 return defLevelInfos;
		}catch (Exception e) {
			 return null;
		}
	}

	@Override
	public DefLevelInfo findLevelInfoById(int levelId) {
		String sql="SELECT *,DATE_FORMAT(CREATE_TIME,'"+GlobalUtil.global.getPtDateFormat()+"') CREATE_TIME_STR FROM def_level_info"
				+ " WHERE LEVEL_ID=:levelId ";
		
		Map<String, Object> paramMap=new HashMap<String, Object>();
		paramMap.put("levelId",levelId);
		 
	 	try {
	 		DefLevelInfo defLevelInfo=findEntityForObject(sql, paramMap, DefLevelInfo.class);
			 return defLevelInfo;
		}catch (Exception e) {
			 return null;
		}
	}

	@Override
	public HmiResultObj createLevelInfo(DefLevelInfo defLevelInfo) {
		HmiResultObj hmiResultObj=null;
		try {
			sqlDao.insertUpdate(defLevelInfo);
			hmiResultObj=new HmiResultObj();
			//hmiResultObj.setResultObj(member);
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS);
		} catch (SqlErrorException e) {
			e.printStackTrace();
			hmiResultObj=new HmiResultObj();
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
		}
		return hmiResultObj;
	}

	@Override
	public HmiResultObj updateLevelInfo(DefLevelInfo defLevelInfo) {
		HmiResultObj hmiResultObj=null;
		try {
			sqlDao.insertUpdate(defLevelInfo);
			hmiResultObj=new HmiResultObj();
			//hmiResultObj.setResultObj(member);
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS);
		} catch (SqlErrorException e) {
			e.printStackTrace();
			hmiResultObj=new HmiResultObj();
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
		}
		return hmiResultObj;
	}

	@Override
	public HmiResultObj deleteLevelInfo(DefLevelInfo defLevelInfo) {
		HmiResultObj hmiResultObj=null;
		try {
			sqlDao.delete(defLevelInfo);
			hmiResultObj=new HmiResultObj();
			//hmiResultObj.setResultObj(member);
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS);
		} catch (SqlErrorException e) {
			e.printStackTrace();
			hmiResultObj=new HmiResultObj();
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
		}
		return hmiResultObj;
	}

	public GlobalLocalDao getGlobalLocalDao() {
		return globalLocalDao;
	}

	public void setGlobalLocalDao(GlobalLocalDao globalLocalDao) {
		this.globalLocalDao = globalLocalDao;
	}

	@Override
	public DefCalendarTimes findCalendarTimes() {
		String sql="SELECT * FROM def_calendar_times LIMIT 1";
		
		 
	 	try {
	 		DefCalendarTimes defCalendarTimes=findEntityForObject(sql, null, DefCalendarTimes.class);
			 return defCalendarTimes;
		}catch (Exception e) {
			 return null;
		}
	}

	@Override
	public HmiResultObj createDefCalendarTimes(DefCalendarTimes defCalendarTimes) {
		HmiResultObj hmiResultObj=null;
		try {
			sqlDao.insertUpdate(defCalendarTimes);
			hmiResultObj=new HmiResultObj();
			//hmiResultObj.setResultObj(member);
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS);
		} catch (SqlErrorException e) {
			e.printStackTrace();
			hmiResultObj=new HmiResultObj();
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
		}
		return hmiResultObj;
	}

	


	
}
