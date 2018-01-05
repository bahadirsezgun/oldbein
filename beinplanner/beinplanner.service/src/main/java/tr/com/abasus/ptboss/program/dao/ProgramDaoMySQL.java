package tr.com.abasus.ptboss.program.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tr.com.abasus.general.dao.AbaxJdbcDaoSupport;
import tr.com.abasus.general.dao.SqlDao;
import tr.com.abasus.general.exception.SqlErrorException;
import tr.com.abasus.ptboss.program.entity.ProgramClass;
import tr.com.abasus.ptboss.program.entity.ProgramFactory;
import tr.com.abasus.ptboss.program.entity.ProgramFamily;
import tr.com.abasus.ptboss.program.entity.ProgramMembership;
import tr.com.abasus.ptboss.program.entity.ProgramMembershipDetail;
import tr.com.abasus.ptboss.program.entity.ProgramPersonal;
import tr.com.abasus.ptboss.result.entity.HmiResultObj;
import tr.com.abasus.util.GlobalUtil;
import tr.com.abasus.util.ResultStatuObj;

public class ProgramDaoMySQL extends AbaxJdbcDaoSupport implements ProgramDao {

	SqlDao sqlDao;

	
	
	
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ProgramFactory> findAllProgramPersonal(int firmId) {
		String sql="SELECT a.*,DATE_FORMAT(a.CREATE_TIME,'"+GlobalUtil.global.getPtDateFormat()+"') CREATE_TIME_STR,c.* "
				+ "  FROM def_firm c ,program_personal a LEFT JOIN user b "
				+ " ON a.PROG_USER_ID=b.USER_ID "
				+ " WHERE a.FIRM_ID=:firmId "
				+ " AND a.FIRM_ID=c.FIRM_ID"
				+ " ORDER BY a.CREATE_TIME  ";
		 
		
		Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("firmId",firmId);
		   
		 
		 try{
			 List<? extends ProgramFactory> programPersonals=findEntityList(sql, paramMap, ProgramPersonal.class);
			 return ((List<ProgramFactory>) programPersonals);
		 }catch (Exception e) {
			 
				return null;
		 }
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ProgramFactory> findAllProgramsForPersonal() {
		String sql="SELECT a.*,DATE_FORMAT(a.CREATE_TIME,'"+GlobalUtil.global.getPtDateFormat()+"') CREATE_TIME_STR,c.*,b.* "
				+ "  FROM def_firm c , program_personal a LEFT JOIN user b ON a.PROG_USER_ID=b.USER_ID"
				+ " WHERE a.FIRM_ID=c.FIRM_ID "
				+ " ORDER BY a.CREATE_TIME  ";
		 
		  
		 try{
			/*List<ProgramPersonal> programPersonals=findEntityList(sql, null, ProgramPersonal.class);
			return programPersonals;
			*/
			List<? extends ProgramFactory> programPersonals=findEntityList(sql, null, ProgramPersonal.class);
			 return ((List<ProgramFactory>) programPersonals);
			
		 }catch (Exception e) {
			 e.printStackTrace();
				return null;
		 }
	}

	@Override
	public ProgramPersonal findProgramPersonal(int progId) {
		String sql="SELECT a.*,DATE_FORMAT(a.CREATE_TIME,'"+GlobalUtil.global.getPtDateFormat()+"') CREATE_TIME_STR "
				+ "  FROM program_personal a LEFT JOIN user b "
				+ " ON a.PROG_USER_ID=b.USER_ID "
				+ " WHERE PROG_ID=:progId  ";
		 
		
			Map<String, Object> paramMap=new HashMap<String, Object>();
			paramMap.put("progId",progId);
		   
		 try{
			ProgramPersonal programPersonal=findEntityForObject(sql, paramMap, ProgramPersonal.class);
			return programPersonal;
		 }catch (Exception e) {
				return null;
		 }
	}

	
	
	@Override
	public synchronized HmiResultObj createProgramPersonal(ProgramPersonal programPersonal) {
		HmiResultObj hmiResultObj=null;
		try {
			sqlDao.insertUpdate(programPersonal);
			if(programPersonal.getProgId()==0)
			  programPersonal.setProgId(Integer.parseInt(getLastProgPersIdSeq()));
				
			
			hmiResultObj=new HmiResultObj();
			//hmiResultObj.setResultObj(member);
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS);
			hmiResultObj.setResultMessage(""+programPersonal.getProgId());
		} catch (SqlErrorException e) {
			e.printStackTrace();
			hmiResultObj=new HmiResultObj();
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
		}
		return hmiResultObj;
	}
	
	private String getLastProgPersIdSeq(){
		String sql=	"SELECT CAST(max(PROG_ID) AS CHAR) PROG_ID" +
				" FROM program_personal ";
		String progId="1";
		try {
			progId=""+findEntityForObject(sql, null, Long.class);
			if(progId.equals("null"))
				progId="1";
		} catch (Exception e) {
			progId="1";
			e.printStackTrace();
		}
		return progId;

	}

	@Override
	public HmiResultObj deleteProgramPersonal(ProgramPersonal programPersonal) {
		HmiResultObj hmiResultObj=null;
		try {
			sqlDao.delete(programPersonal);
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

	@SuppressWarnings("unchecked")
	@Override
	public List<ProgramFactory> findAllProgramsForClass() {
		String sql="SELECT a.*,DATE_FORMAT(a.CREATE_TIME,'"+GlobalUtil.global.getPtDateFormat()+"') CREATE_TIME_STR,c.*,b.* "
				+ "  FROM def_firm c , program_class a LEFT JOIN user b ON a.PROG_USER_ID=b.USER_ID"
				+ " WHERE a.FIRM_ID=c.FIRM_ID "
				+ " ORDER BY a.CREATE_TIME  ";
		 
		
		  
		 try{
			
			List<? extends ProgramFactory> programClasses=findEntityList(sql, null, ProgramClass.class);
			 return ((List<ProgramFactory>) programClasses);
			
			
		 }catch (Exception e) {
			 e.printStackTrace();
				return null;
		 }
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ProgramFactory> findAllProgramClass(int firmId) {
		String sql="SELECT a.*,DATE_FORMAT(a.CREATE_TIME,'"+GlobalUtil.global.getPtDateFormat()+"') CREATE_TIME_STR,c.*,b.* "
				+ "  FROM def_firm c ,program_class a LEFT JOIN user b "
				+ " ON a.PROG_USER_ID=b.USER_ID "
				+ " WHERE a.FIRM_ID=:firmId "
				+ " AND a.FIRM_ID=c.FIRM_ID"
				+ " ORDER BY a.CREATE_TIME  ";
		 
		
		Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("firmId",firmId);
		   
		 
		 try{
			
			List<? extends ProgramFactory> programClasses=findEntityList(sql, paramMap, ProgramClass.class);
			return ((List<ProgramFactory>) programClasses);
			
		 }catch (Exception e) {
			 e.printStackTrace();
				return null;
		 }
	}

	@Override
	public ProgramClass findProgramClass(int progId) {
		String sql="SELECT a.*,DATE_FORMAT(a.CREATE_TIME,'"+GlobalUtil.global.getPtDateFormat()+"') CREATE_TIME_STR"
				+ " ,c.*,b.*"
				+ "  FROM def_firm c ,program_class a LEFT JOIN user b "
				+ " ON a.PROG_USER_ID=b.USER_ID "
				+ " WHERE a.PROG_ID=:progId "
				+ " AND a.FIRM_ID=c.FIRM_ID ";
		 
		
		Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("progId",progId);
		   
		 try{
			 ProgramClass programClass=findEntityForObject(sql, paramMap, ProgramClass.class);
			return programClass;
		 }catch (Exception e) {
			return null;
		 }
	}

	@Override
	public synchronized HmiResultObj createProgramClass(ProgramClass programClass) {
		HmiResultObj hmiResultObj=null;
		try {
			sqlDao.insertUpdate(programClass);
			
			if(programClass.getProgId()==0)
				programClass.setProgId(Integer.parseInt(getLastProgClassIdSeq()));
			
			hmiResultObj=new HmiResultObj();
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS);
			hmiResultObj.setResultMessage(""+programClass.getProgId());
		} catch (SqlErrorException e) {
			e.printStackTrace();
			hmiResultObj=new HmiResultObj();
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
		}
		return hmiResultObj;
	}

	
	private String getLastProgClassIdSeq(){
		String sql=	"SELECT CAST(max(PROG_ID) AS CHAR) PROG_ID" +
				" FROM program_class ";
		String progId="1";
		try {
			progId=""+findEntityForObject(sql, null, Long.class);
			if(progId.equals("null"))
				progId="1";
		} catch (Exception e) {
			progId="1";
			e.printStackTrace();
		}
		return progId;

	}
	
	@Override
	public HmiResultObj deleteProgramClass(ProgramClass programClass) {
		HmiResultObj hmiResultObj=null;
		try {
			sqlDao.delete(programClass);
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

	
	
	
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ProgramFactory> findAllProgramMembership(int firmId) {
		String sql="SELECT a.*,DATE_FORMAT(a.CREATE_TIME,'"+GlobalUtil.global.getPtDateFormat()+"') CREATE_TIME_STR"
				+ " ,c.*,b.* "
				+ "  FROM def_firm c ,program_membership a LEFT JOIN user b "
				+ " ON a.PROG_USER_ID=b.USER_ID "
				+ " WHERE a.FIRM_ID=:firmId "
				+ " AND a.FIRM_ID=c.FIRM_ID ";
		 
		
		Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("firmId",firmId);
		   
		 try{
			
			List<? extends ProgramFactory> programMemberships=findEntityList(sql, paramMap, ProgramMembership.class);
			return ((List<ProgramFactory>) programMemberships);
			
		 }catch (Exception e) {
			 e.printStackTrace();
				return null;
		 }
	}

	@Override
	public ProgramMembership findProgramMembership(int progId) {
		String sql="SELECT a.*,DATE_FORMAT(a.CREATE_TIME,'"+GlobalUtil.global.getPtDateFormat()+"') CREATE_TIME_STR,c.*,b.* "
				+ "  FROM def_firm c ,program_membership a LEFT JOIN user b "
				+ " ON a.PROG_USER_ID=b.USER_ID "
				+ " WHERE a.PROG_ID=:progId "
				+ " AND a.FIRM_ID=c.FIRM_ID"
				+ " ORDER BY a.CREATE_TIME  ";
		 
		
		Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("progId",progId);
		   
		 
		 try{
			 ProgramMembership programMembership=findEntityForObject(sql, paramMap, ProgramMembership.class);
			return programMembership;
		 }catch (Exception e) {
			 e.printStackTrace();
				return null;
		 }
	}

	@Override
	public synchronized HmiResultObj createProgramMembership(ProgramMembership programMembership) {
		HmiResultObj hmiResultObj=null;
		try {
			sqlDao.insertUpdate(programMembership);
			hmiResultObj=new HmiResultObj();
			//hmiResultObj.setResultObj(member);
			if(programMembership.getProgId()==0)
				programMembership.setProgId(Integer.parseInt(getLastProgMembershipIdSeq()));
			
			
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS);
			hmiResultObj.setResultMessage(""+programMembership.getProgId());
		} catch (SqlErrorException e) {
			e.printStackTrace();
			hmiResultObj=new HmiResultObj();
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
		}
		return hmiResultObj;
	}

	private String getLastProgMembershipIdSeq(){
		String sql=	"SELECT CAST(max(PROG_ID) AS CHAR) PROG_ID" +
				" FROM program_membership ";
		String progId="1";
		try {
			progId=""+findEntityForObject(sql, null, Long.class);
			if(progId.equals("null"))
				progId="1";
		} catch (Exception e) {
			progId="1";
			e.printStackTrace();
		}
		return progId;

	}
	
	@Override
	public HmiResultObj deleteProgramMembership(ProgramMembership programMembership) {
		HmiResultObj hmiResultObj=null;
		try {
			sqlDao.delete(programMembership);
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
	public List<ProgramMembershipDetail> findAllProgramMembershipDetail(int progId) {
		String sql="SELECT a.* "
				+ "  FROM program_membership_detail a"
				+ " WHERE a.PROG_ID=:progId  ";
		 
		
		Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("progId",progId);
		   
		 try{
			 List<ProgramMembershipDetail> programMembershipDetails=findEntityList(sql, paramMap, ProgramMembershipDetail.class);
			return programMembershipDetails;
		 }catch (Exception e) {
				return null;
		 }
	}

	@Override
	public ProgramMembershipDetail findProgramMembershipDetail(int progDetId) {
		String sql="SELECT a.* "
				+ "  FROM program_membership_detail a"
				+ " WHERE a.PROG_DET_ID=:progDetId  ";
		 
		
		Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("progDetId",progDetId);
		   
		 try{
			 ProgramMembershipDetail programMembershipDetail=findEntityForObject(sql, paramMap, ProgramMembershipDetail.class);
			return programMembershipDetail;
		 }catch (Exception e) {
				return null;
		 }
	}

	@Override
	public HmiResultObj createProgramMembershipDetail(ProgramMembershipDetail programMembershipDetail) {
		HmiResultObj hmiResultObj=null;
		try {
			sqlDao.insertUpdate(programMembershipDetail);
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
	public HmiResultObj deleteProgramMembershipDetail(ProgramMembershipDetail programMembershipDetail) {
		HmiResultObj hmiResultObj=null;
		try {
			sqlDao.delete(programMembershipDetail);
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
	public HmiResultObj deleteProgramMembershipDetailByProgId(int progId) {
		String sql=" DELETE FROM program_membership_detail "
				+ " WHERE PROG_ID=:progId  ";
		 
		
		Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("progId",progId);
		   
		 try{
			 update(sql, paramMap);
			return new HmiResultObj();
		 }catch (Exception e) {
			 e.printStackTrace();
			return null;
		 }
	}
	
	
	public SqlDao getSqlDao() {
		return sqlDao;
	}

	public void setSqlDao(SqlDao sqlDao) {
		this.sqlDao = sqlDao;
	}

	

	

	/****************************************************************************/
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ProgramFactory> findAllProgramFamily(int firmId) {
		String sql="SELECT a.*,DATE_FORMAT(a.CREATE_TIME,'"+GlobalUtil.global.getPtDateFormat()+"') CREATE_TIME_STR,c.* "
				+ "  FROM def_firm c ,program_family a LEFT JOIN user b "
				+ " ON a.PROG_USER_ID=b.USER_ID "
				+ " WHERE a.FIRM_ID=:firmId "
				+ " AND a.FIRM_ID=c.FIRM_ID"
				+ " ORDER BY a.CREATE_TIME  ";
		 
		
		Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("firmId",firmId);
		   
		 
		 try{
			 List<? extends ProgramFactory> programFamilys=findEntityList(sql, paramMap, ProgramFamily.class);
			 return ((List<ProgramFactory>) programFamilys);
		 }catch (Exception e) {
			 
				return null;
		 }
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ProgramFactory> findAllProgramsForFamily() {
		String sql="SELECT a.*,DATE_FORMAT(a.CREATE_TIME,'"+GlobalUtil.global.getPtDateFormat()+"') CREATE_TIME_STR,c.*,b.* "
				+ "  FROM def_firm c , program_family a LEFT JOIN user b ON a.PROG_USER_ID=b.USER_ID"
				+ " WHERE a.FIRM_ID=c.FIRM_ID "
				+ " ORDER BY a.CREATE_TIME  ";
		 
		  
		 try{
			/*List<ProgramFamily> programFamilys=findEntityList(sql, null, ProgramFamily.class);
			return programFamilys;
			*/
			List<? extends ProgramFactory> programFamilys=findEntityList(sql, null, ProgramFamily.class);
			 return ((List<ProgramFactory>) programFamilys);
			
		 }catch (Exception e) {
			 e.printStackTrace();
				return null;
		 }
	}

	@Override
	public ProgramFamily findProgramFamily(int progId) {
		String sql="SELECT a.*,DATE_FORMAT(a.CREATE_TIME,'"+GlobalUtil.global.getPtDateFormat()+"') CREATE_TIME_STR "
				+ "  FROM program_family a LEFT JOIN user b "
				+ " ON a.PROG_USER_ID=b.USER_ID "
				+ " WHERE PROG_ID=:progId  ";
		 
		
			Map<String, Object> paramMap=new HashMap<String, Object>();
			paramMap.put("progId",progId);
		   
		 try{
			ProgramFamily programFamily=findEntityForObject(sql, paramMap, ProgramFamily.class);
			return programFamily;
		 }catch (Exception e) {
				return null;
		 }
	}

	
	
	@Override
	public synchronized HmiResultObj createProgramFamily(ProgramFamily programFamily) {
		HmiResultObj hmiResultObj=null;
		try {
			sqlDao.insertUpdate(programFamily);
			if(programFamily.getProgId()==0)
			  programFamily.setProgId(Integer.parseInt(getLastProgPersIdSeq()));
				
			
			hmiResultObj=new HmiResultObj();
			//hmiResultObj.setResultObj(member);
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS);
			hmiResultObj.setResultMessage(""+programFamily.getProgId());
		} catch (SqlErrorException e) {
			e.printStackTrace();
			hmiResultObj=new HmiResultObj();
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
		}
		return hmiResultObj;
	}
	
	private String getLastProgFamilyIdSeq(){
		String sql=	"SELECT CAST(max(PROG_ID) AS CHAR) PROG_ID" +
				" FROM program_family ";
		String progId="1";
		try {
			progId=""+findEntityForObject(sql, null, Long.class);
			if(progId.equals("null"))
				progId="1";
		} catch (Exception e) {
			progId="1";
			e.printStackTrace();
		}
		return progId;

	}

	@Override
	public HmiResultObj deleteProgramFamily(ProgramFamily programFamily) {
		HmiResultObj hmiResultObj=null;
		try {
			sqlDao.delete(programFamily);
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
