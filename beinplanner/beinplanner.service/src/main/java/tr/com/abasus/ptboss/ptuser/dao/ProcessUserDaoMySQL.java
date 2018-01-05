package tr.com.abasus.ptboss.ptuser.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tr.com.abasus.general.dao.AbaxJdbcDaoSupport;
import tr.com.abasus.general.dao.SqlDao;
import tr.com.abasus.general.exception.SqlErrorException;
import tr.com.abasus.ptboss.ptuser.entity.StaffTracking;
import tr.com.abasus.ptboss.ptuser.entity.User;
import tr.com.abasus.ptboss.ptuser.entity.UserPotential;
import tr.com.abasus.ptboss.result.entity.HmiResultObj;
import tr.com.abasus.util.GlobalUtil;
import tr.com.abasus.util.OhbeUtil;
import tr.com.abasus.util.ResultStatuObj;
import tr.com.abasus.util.StatuTypes;
import tr.com.abasus.util.UserTypes;

public class ProcessUserDaoMySQL extends AbaxJdbcDaoSupport implements ProcessUserDao  {

	SqlDao sqlDao;
	
	
	
	
	public SqlDao getSqlDao() {
		return sqlDao;
	}

	public void setSqlDao(SqlDao sqlDao) {
		this.sqlDao = sqlDao;
	}
	
	@Override
	public User loginUserControl(String userName, String password) {
		String sql="SELECT * FROM user "
				+ " WHERE USER_EMAIL=:userEmail "
				+ " AND PASSWORD=:password "
				+ " LIMIT 1";
		 Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("userEmail",userName);
		 paramMap.put("password",password);
		 try{
				User user=findEntityForObject(sql, paramMap, User.class);
				return user;
		 }catch (Exception e) {
				return null;
		 }
	}

	@Override
	public User findUserByEMail(String email) {
		String sql="SELECT a.*,b.*  "
				+ ",DATE_FORMAT(a.USER_BIRTHDAY,'"+GlobalUtil.global.getPtDateFormat()+"') USER_BIRTHDAY_STR "
				+ ",DATE_FORMAT(a.CREATE_TIME,'"+GlobalUtil.global.getPtDateFormat()+"') CREATE_TIME_STR "
				+ " FROM user a,def_firm b  "
				+ " WHERE a.USER_EMAIL=:email"
				+ " AND a.FIRM_ID=b.FIRM_ID "
				+ " ORDER BY a.USER_NAME,a.USER_SURNAME "
				+ " LIMIT 1 ";
		 
		
		Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("email",email);
		
		 try{
			
			 User users=findEntityForObject(sql, paramMap, User.class);
			 
			 return users;
		 }catch (Exception e) {
				return null;
		 }
	}

	@Override
	public synchronized HmiResultObj createUser(User user) {
		HmiResultObj hmiResultObj=null;
		try {
			sqlDao.insertUpdate(user);
			if(user.getUserId()==0)
				user.setUserId(Long.parseLong(getLastUserIdSeq()));
			
			hmiResultObj=new HmiResultObj();
			//hmiResultObj.setResultObj(member);
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS);
			hmiResultObj.setResultMessage(""+user.getUserId());
		} catch (SqlErrorException e) {
			e.printStackTrace();
			hmiResultObj=new HmiResultObj();
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
			hmiResultObj.setResultMessage(ResultStatuObj.RESULT_STATU_FAIL_STR);
		}
		return hmiResultObj;
		
	}
	
	private synchronized String getLastUserIdSeq(){
		String sql=	"SELECT CAST(max(USER_ID) AS CHAR) USER_ID" +
				" FROM user ";
		String userId="1";
		try {
			userId=""+findEntityForObject(sql, null, Long.class);
			if(userId.equals("null"))
				userId="1";
		} catch (Exception e) {
			userId="1";
			e.printStackTrace();
		}
		return userId;

	
	}

	@Override
	public synchronized HmiResultObj updateUser(User user) {
		HmiResultObj hmiResultObj=null;
		try {
			sqlDao.insertUpdate(user);
			
			hmiResultObj=new HmiResultObj();
			//hmiResultObj.setResultObj(member);
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS);
			hmiResultObj.setResultMessage(""+user.getUserId());
		} catch (SqlErrorException e) {
			e.printStackTrace();
			hmiResultObj=new HmiResultObj();
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
			hmiResultObj.setResultMessage(ResultStatuObj.RESULT_STATU_FAIL_STR);
		}
		return hmiResultObj;
	}

	@Override
	public HmiResultObj deleteUser(User user) {
		HmiResultObj hmiResultObj=null;
		try {
			sqlDao.delete(user);
			
			hmiResultObj=new HmiResultObj();
			//hmiResultObj.setResultObj(member);
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS);
		} catch (SqlErrorException e) {
			e.printStackTrace();
			hmiResultObj=new HmiResultObj();
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
			hmiResultObj.setResultMessage(ResultStatuObj.RESULT_STATU_FAIL_STR);
		}
		return hmiResultObj;
	}

	@Override
	public User findById(long userId) {
		String sql="SELECT a.*,b.* "
				+ ",DATE_FORMAT(a.USER_BIRTHDAY,'"+GlobalUtil.global.getPtDateFormat()+"') USER_BIRTHDAY_STR "
				+ ",DATE_FORMAT(a.CREATE_TIME,'"+GlobalUtil.global.getPtDateFormat()+"') CREATE_TIME_STR "
				+ " FROM user a,def_firm b  "
				+ " WHERE a.USER_ID=:userId"
				+ " AND a.FIRM_ID=b.FIRM_ID ";
		 
		////System.out.println(sql+"  "+userId);
		Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("userId",userId);
		   
		 try{
			 User user=findEntityForObject(sql, paramMap, User.class);
			 
			 return user;
		 }catch (Exception e) {
			 e.printStackTrace();
				return null;
		 }
	}

	@Override
	public int getUserCompletePercentToAdmin(User user) {
		int percent=100;
    	if(user.getStateId()>0 
    			&& user.getCityId()>0 
    			&& !user.getUserGsm().equals("")
    			&& !user.getUserAddress().equals("")
    			&& !user.getUserComment().equals("")
    			&& user.getUserGender()>0
    			){
    		percent=100;
    	}else if(!user.getUserGsm().equals("")
    			&& !user.getUserAddress().equals("")
    			&& !user.getUserComment().equals("")
    			&& user.getUserGender()>0
    			){
    		percent=80;
    	}else if(!user.getUserAddress().equals("")
    			&& !user.getUserComment().equals("")
    			&& user.getUserGender()>0
    			){
    		percent=60;
    	}else if(user.getProfileUrl()!=null){
    		percent=50;
    	}else if( !user.getUserComment().equals("")
        			&& user.getUserGender()>0
        		){
        	percent=45;
        }else if(!user.getUserComment().equals("")
    			&& user.getUserGender()>0
    			){
    		percent=40;
    	}else if( user.getUserGender()>0
    			){
    		percent=20;
    	}else{
    		percent=10;
    	}
    	
    	return percent;
	}

	@Override
	public List<User> findAllToAdmin(int firmId) {
		String sql="SELECT a.*,b.*  "
				+ ",DATE_FORMAT(a.USER_BIRTHDAY,'"+GlobalUtil.global.getPtDateFormat()+"') USER_BIRTHDAY_STR "
				+ ",DATE_FORMAT(a.CREATE_TIME,'"+GlobalUtil.global.getPtDateFormat()+"') CREATE_TIME_STR "
				+ " FROM user a,def_firm b  "
				+ " WHERE a.USER_TYPE="+UserTypes.USER_TYPE_ADMIN_INT;
				if(firmId!=0)
					sql+=  " AND a.FIRM_ID=:firmId ";
			
				sql+= " AND a.FIRM_ID=b.FIRM_ID "
				+ " ORDER BY a.USER_NAME,a.USER_SURNAME "
				+ " LIMIT 50 ";
		 
		
		Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("firmId",firmId);
		
		 try{
			
			 List<User> users=findEntityList(sql, paramMap, User.class);
			 return users;
		 }catch (Exception e) {
				return null;
		 }
	}

	@Override
	public List<User> findByNameAndSurnameToAdmin(String userName, String userSurname,int firmId) {
		String sql="SELECT a.* ,b.* "
				+ ",DATE_FORMAT(a.USER_BIRTHDAY,'"+GlobalUtil.global.getPtDateFormat()+"') USER_BIRTHDAY_STR "
				+ ",DATE_FORMAT(a.CREATE_TIME,'"+GlobalUtil.global.getPtDateFormat()+"') CREATE_TIME_STR "
				+ " FROM user a ,def_firm b "
				+ " WHERE a.USER_NAME LIKE (:userName)"
				+ " AND a.USER_SURNAME LIKE (:userSurname)"
				+ " AND a.USER_TYPE="+UserTypes.USER_TYPE_ADMIN_INT;
				if(firmId!=0)
					sql+=  " AND a.FIRM_ID=:firmId ";
			    sql+= " AND a.FIRM_ID=b.FIRM_ID "
				
				+ " ORDER BY a.USER_NAME,a.USER_SURNAME "
				+ " LIMIT 50 ";
		 
		
		Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("userName",userName);
		 paramMap.put("userSurname",userSurname);
		 paramMap.put("firmId",firmId);
		  
		 try{
			
			 List<User> users=findEntityList(sql, paramMap, User.class);
			 for (User user : users) {
				 user.setPassword(null);
			 }
			 return users;
		 }catch (Exception e) {
				return null;
		 }
	}

	@Override
	public int getUserCompletePercentToManager(User user) {
		int percent=100;
    	if(user.getStateId()>0 
    			&& user.getCityId()>0 
    			&& !user.getUserGsm().equals("")
    			&& !user.getUserAddress().equals("")
    			&& !user.getUserComment().equals("")
    			&& user.getUserGender()>0
    			){
    		percent=100;
    	}else if(!user.getUserGsm().equals("")
    			&& !user.getUserAddress().equals("")
    			&& !user.getUserComment().equals("")
    			&& user.getUserGender()>0
    			){
    		percent=80;
    	}else if(!user.getUserAddress().equals("")
    			&& !user.getUserComment().equals("")
    			&& user.getUserGender()>0
    			){
    		percent=60;
    	}else if(user.getProfileUrl()!=null){
    		percent=50;
    	}else if( !user.getUserComment().equals("")
        			&& user.getUserGender()>0
        		){
        	percent=45;
        }else if(!user.getUserComment().equals("")
    			&& user.getUserGender()>0
    			){
    		percent=40;
    	}else if( user.getUserGender()>0
    			){
    		percent=20;
    	}else{
    		percent=10;
    	}
    	
    	return percent;
	}

	@Override
	public List<User> findAllToManager(int firmId) {
		String sql="SELECT a.*,b.*  "
				+ ",DATE_FORMAT(a.USER_BIRTHDAY,'"+GlobalUtil.global.getPtDateFormat()+"') USER_BIRTHDAY_STR "
				+ ",DATE_FORMAT(a.CREATE_TIME,'"+GlobalUtil.global.getPtDateFormat()+"') CREATE_TIME_STR "
				+ " FROM user a,def_firm b  "
				+ " WHERE a.USER_TYPE="+UserTypes.USER_TYPE_MANAGER_INT;
				if(firmId!=0)
					sql+=  " AND a.FIRM_ID=:firmId ";
			
				sql+= " AND a.FIRM_ID=b.FIRM_ID "
				+ " ORDER BY a.USER_NAME,a.USER_SURNAME "
				+ " LIMIT 50 ";
		 
		
		Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("firmId",firmId);
		
		 try{
			
			 List<User> users=findEntityList(sql, paramMap, User.class);
			 return users;
		 }catch (Exception e) {
				return null;
		 }
	}

	@Override
	public List<User> findByNameAndSurnameToManager(String userName, String userSurname,int firmId) {
		String sql="SELECT a.* ,b.* "
				+ ",DATE_FORMAT(a.USER_BIRTHDAY,'"+GlobalUtil.global.getPtDateFormat()+"') USER_BIRTHDAY_STR "
				+ ",DATE_FORMAT(a.CREATE_TIME,'"+GlobalUtil.global.getPtDateFormat()+"') CREATE_TIME_STR "
				+ " FROM user a ,def_firm b "
				+ " WHERE a.USER_NAME LIKE (:userName)"
				+ " AND a.USER_SURNAME LIKE (:userSurname)"
				+ " AND a.USER_TYPE="+UserTypes.USER_TYPE_MANAGER_INT;
				if(firmId!=0)
					sql+=  " AND a.FIRM_ID=:firmId ";
		
			    sql+= " AND a.FIRM_ID=b.FIRM_ID "
				
				+ " ORDER BY a.USER_NAME,a.USER_SURNAME "
				+ " LIMIT 50 ";
		 
		
		Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("userName",userName);
		 paramMap.put("userSurname",userSurname);
		 paramMap.put("firmId",firmId);
		  
		 try{
			
			 List<User> users=findEntityList(sql, paramMap, User.class);
			 for (User user : users) {
				 user.setPassword(null);
			 }
			 return users;
		 }catch (Exception e) {
				return null;
		 }
	}

	
	@Override
	public int getUserCompletePercentToMember(User user) {
		int percent=100;
    	if(user.getStateId()>0 
    			&& user.getCityId()>0 
    			&& !user.getUserGsm().equals("")
    			&& !user.getUserAddress().equals("")
    			&& !user.getUserComment().equals("")
    			&& user.getUserGender()>0
    			){
    		percent=100;
    	}else if(!user.getUserGsm().equals("")
    			&& !user.getUserAddress().equals("")
    			&& !user.getUserComment().equals("")
    			&& user.getUserGender()>0
    			){
    		percent=80;
    	}else if(!user.getUserAddress().equals("")
    			&& !user.getUserComment().equals("")
    			&& user.getUserGender()>0
    			){
    		percent=60;
    	}else if(user.getProfileUrl()!=null){
    		percent=50;
    	}else if( !user.getUserComment().equals("")
        			&& user.getUserGender()>0
        		){
        	percent=45;
        }else if(!user.getUserComment().equals("")
    			&& user.getUserGender()>0
    			){
    		percent=40;
    	}else if( user.getUserGender()>0
    			){
    		percent=20;
    	}else{
    		percent=10;
    	}
    	
    	return percent;
	}

	@Override
	public List<User> findAllToMember(int firmId) {
		String sql="SELECT a.*,b.*  "
				+ ",DATE_FORMAT(a.USER_BIRTHDAY,'"+GlobalUtil.global.getPtDateFormat()+"') USER_BIRTHDAY_STR "
				+ ",DATE_FORMAT(a.CREATE_TIME,'"+GlobalUtil.global.getPtDateFormat()+"') CREATE_TIME_STR "
				+ " FROM user a,def_firm b  "
				+ " WHERE a.USER_TYPE="+UserTypes.USER_TYPE_MEMBER_INT;
				if(firmId!=0)
					sql+=  " AND a.FIRM_ID=:firmId ";
			
				sql+= " AND a.FIRM_ID=b.FIRM_ID "
				+ " ORDER BY a.USER_NAME,a.USER_SURNAME "
				+ " LIMIT 50 ";
		 
		
		Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("firmId",firmId);
		
		 try{
			
			 List<User> users=findEntityList(sql, paramMap, User.class);
			 return users;
		 }catch (Exception e) {
				return null;
		 }
	}
	
	
	
	@Override
	public List<User> findAllToMemberWithNoLimit(int firmId) {
		String sql="SELECT a.*,b.*  "
				+ ",DATE_FORMAT(a.USER_BIRTHDAY,'"+GlobalUtil.global.getPtDateFormat()+"') USER_BIRTHDAY_STR "
				+ ",DATE_FORMAT(a.CREATE_TIME,'"+GlobalUtil.global.getPtDateFormat()+"') CREATE_TIME_STR "
				+ " FROM user a,def_firm b  "
				+ " WHERE a.USER_TYPE="+UserTypes.USER_TYPE_MEMBER_INT;
				if(firmId!=0)
					sql+=  " AND a.FIRM_ID=:firmId ";
			
				sql+= " AND a.FIRM_ID=b.FIRM_ID "
				+ " ORDER BY a.USER_NAME,a.USER_SURNAME  ";
		 
		
		Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("firmId",firmId);
		
		 try{
			
			 List<User> users=findEntityList(sql, paramMap, User.class);
			 
			
			 
			 
			 return users;
		 }catch (Exception e) {
			 e.printStackTrace();
				return null;
		 }
	}
	

	@Override
	public List<User> findByNameAndSurnameToMember(String userName, String userSurname,int firmId) {
		String sql="SELECT a.* ,b.* "
				+ ",DATE_FORMAT(a.USER_BIRTHDAY,'"+GlobalUtil.global.getPtDateFormat()+"') USER_BIRTHDAY_STR "
				+ ",DATE_FORMAT(a.CREATE_TIME,'"+GlobalUtil.global.getPtDateFormat()+"') CREATE_TIME_STR "
				+ " FROM user a ,def_firm b "
				+ " WHERE a.USER_NAME LIKE (:userName)"
				+ " AND a.USER_SURNAME LIKE (:userSurname)"
				+ " AND a.USER_TYPE="+UserTypes.USER_TYPE_MEMBER_INT;
				if(firmId!=0)
					sql+=  " AND a.FIRM_ID=:firmId ";
			    sql+= " AND a.FIRM_ID=b.FIRM_ID "
				+ " ORDER BY a.USER_NAME,a.USER_SURNAME "
				+ " LIMIT 50 ";
		 
		
		Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("userName",userName);
		 paramMap.put("userSurname",userSurname);
		 paramMap.put("firmId",firmId);
			  
		 try{
			
			 List<User> users=findEntityList(sql, paramMap, User.class);
			 for (User user : users) {
				 user.setPassword(null);
			 }
			 return users;
		 }catch (Exception e) {
				return null;
		 }
	}

	
	@Override
	public int getUserCompletePercentToSchedulerStaff(User user) {
		int percent=100;
    	if(user.getStateId()>0 
    			&& user.getCityId()>0 
    			&& !user.getUserGsm().equals("")
    			&& !user.getUserAddress().equals("")
    			&& !user.getUserComment().equals("")
    			&& user.getUserGender()>0
    			&& user.getBonusTypeP()>0
    			&& user.getBonusTypeC()>0
    			){
    		percent=100;
    	}else if(user.getBonusTypeP()>0
    			&& user.getBonusTypeC()>0
    			){
    		percent=90;
    	}else if(!user.getUserGsm().equals("")
    			&& !user.getUserAddress().equals("")
    			&& !user.getUserComment().equals("")
    			&& user.getUserGender()>0
    			){
    		percent=80;
    	}else if(!user.getUserAddress().equals("")
    			&& !user.getUserComment().equals("")
    			&& user.getUserGender()>0
    			){
    		percent=60;
    	}else if(user.getProfileUrl()!=null){
    		percent=50;
    	}else if( !user.getUserComment().equals("")
        			&& user.getUserGender()>0
        		){
        	percent=45;
        }else if(!user.getUserComment().equals("")
    			&& user.getUserGender()>0
    			){
    		percent=40;
    	}else if( user.getUserGender()>0
    			){
    		percent=20;
    	}else{
    		percent=10;
    	}
    	
    	return percent;
	}

	@Override
	public List<User> findAllToSchedulerStaff(int firmId) {
		String sql="SELECT a.*,b.*  "
				+ ",DATE_FORMAT(a.USER_BIRTHDAY,'"+GlobalUtil.global.getPtDateFormat()+"') USER_BIRTHDAY_STR "
				+ ",DATE_FORMAT(a.CREATE_TIME,'"+GlobalUtil.global.getPtDateFormat()+"') CREATE_TIME_STR "
				+ " FROM user a LEFT JOIN def_firm b ON a.FIRM_ID=b.FIRM_ID   "
				+ " WHERE a.USER_TYPE="+UserTypes.USER_TYPE_SCHEDULAR_STAFF_INT;
				if(firmId!=0)
					sql+=  " AND a.FIRM_ID=:firmId OR a.FIRM_ID=0 ";
			
				sql+= " ORDER BY a.USER_NAME,a.USER_SURNAME "
				+ " LIMIT 50 ";
		 
		
		Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("firmId",firmId);
		 paramMap.put("staffStatu",StatuTypes.ACTIVE);
			
		 try{
			
			 List<User> users=findEntityList(sql, paramMap, User.class);
			 for (User user : users) {
				user.setPassword(null);
			}
			 
			 return users;
		 }catch (Exception e) {
				return null;
		 }
	}
	
	@Override
	public List<User> findAllToSchedulerStaffForCalendar(int firmId) {
		String sql="SELECT a.*,b.*  "
				+ ",DATE_FORMAT(a.USER_BIRTHDAY,'"+GlobalUtil.global.getPtDateFormat()+"') USER_BIRTHDAY_STR "
				+ ",DATE_FORMAT(a.CREATE_TIME,'"+GlobalUtil.global.getPtDateFormat()+"') CREATE_TIME_STR "
				+ " FROM user a LEFT JOIN def_firm b ON a.FIRM_ID=b.FIRM_ID   "
				+ " WHERE a.USER_TYPE="+UserTypes.USER_TYPE_SCHEDULAR_STAFF_INT
				+" AND STAFF_STATU=:staffStatu";
				if(firmId!=0)
					sql+=  " AND a.FIRM_ID=:firmId OR a.FIRM_ID=0 ";
			
				sql+= " ORDER BY a.USER_NAME,a.USER_SURNAME "
				+ " LIMIT 50 ";
		 
		
		Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("firmId",firmId);
		 paramMap.put("staffStatu",StatuTypes.ACTIVE);
			
		 try{
			
			 List<User> users=findEntityList(sql, paramMap, User.class);
			 for (User user : users) {
				user.setPassword(null);
			}
			 
			 return users;
		 }catch (Exception e) {
				return null;
		 }
	}

	@Override
	public List<User> findByNameAndSurnameToSchedulerStaff(String userName, String userSurname,int firmId) {
		String sql="SELECT a.* ,b.* "
				+ ",DATE_FORMAT(a.USER_BIRTHDAY,'"+GlobalUtil.global.getPtDateFormat()+"') USER_BIRTHDAY_STR "
				+ ",DATE_FORMAT(a.CREATE_TIME,'"+GlobalUtil.global.getPtDateFormat()+"') CREATE_TIME_STR "
				+ " FROM user a ,def_firm b "
				+ " WHERE a.USER_NAME LIKE (:userName)"
				+ " AND a.USER_SURNAME LIKE (:userSurname)"
				+ " AND a.USER_TYPE="+UserTypes.USER_TYPE_SCHEDULAR_STAFF_INT;
				if(firmId!=0)
					sql+=  " AND a.FIRM_ID=:firmId ";
			
				sql+= " AND a.FIRM_ID=b.FIRM_ID "
				+ " ORDER BY a.USER_NAME,a.USER_SURNAME "
				+ " LIMIT 50 ";
		 
		
		Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("userName",userName);
		 paramMap.put("userSurname",userSurname);
		 paramMap.put("firmId",firmId);
			  
		 try{
			
			 List<User> users=findEntityList(sql, paramMap, User.class);
			 for (User user : users) {
				 user.setPassword(null);
			 }
			 return users;
		 }catch (Exception e) {
				return null;
		 }
	}

	

	@Override
	public int getUserCompletePercentToStaff(User user) {
		int percent=100;
    	if(user.getStateId()>0 
    			&& user.getCityId()>0 
    			&& !user.getUserGsm().equals("")
    			&& !user.getUserAddress().equals("")
    			&& !user.getUserComment().equals("")
    			&& user.getUserGender()>0
    			){
    		percent=100;
    	}else if(!user.getUserGsm().equals("")
    			&& !user.getUserAddress().equals("")
    			&& !user.getUserComment().equals("")
    			&& user.getUserGender()>0
    			){
    		percent=80;
    	}else if(!user.getUserAddress().equals("")
    			&& !user.getUserComment().equals("")
    			&& user.getUserGender()>0
    			){
    		percent=60;
    	}else if(user.getProfileUrl()!=null){
    		percent=50;
    	}else if( !user.getUserComment().equals("")
        			&& user.getUserGender()>0
        		){
        	percent=45;
        }else if(!user.getUserComment().equals("")
    			&& user.getUserGender()>0
    			){
    		percent=40;
    	}else if( user.getUserGender()>0
    			){
    		percent=20;
    	}else{
    		percent=10;
    	}
    	
    	return percent;
	}

	@Override
	public List<User> findAllToStaff(int firmId) {
		String sql="SELECT a.*,b.*  "
				+ ",DATE_FORMAT(a.USER_BIRTHDAY,'"+GlobalUtil.global.getPtDateFormat()+"') USER_BIRTHDAY_STR "
				+ ",DATE_FORMAT(a.CREATE_TIME,'"+GlobalUtil.global.getPtDateFormat()+"') CREATE_TIME_STR "
				+ " FROM user a,def_firm b  "
				+ " WHERE a.USER_TYPE="+UserTypes.USER_TYPE_STAFF_INT;
				if(firmId!=0)
					sql+=  " AND a.FIRM_ID=:firmId ";
			
				sql+= " AND a.FIRM_ID=b.FIRM_ID "
				+ " ORDER BY a.USER_NAME,a.USER_SURNAME "
				+ " LIMIT 50 ";
		 
		
		Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("firmId",firmId);
		
		 try{
			
			 List<User> users=findEntityList(sql, paramMap, User.class);
			 return users;
		 }catch (Exception e) {
				return null;
		 }
	}

	@Override
	public List<User> findByNameAndSurnameToStaff(String userName, String userSurname,int firmId) {
		String sql="SELECT a.* ,b.* "
				+ ",DATE_FORMAT(a.USER_BIRTHDAY,'"+GlobalUtil.global.getPtDateFormat()+"') USER_BIRTHDAY_STR "
				+ ",DATE_FORMAT(a.CREATE_TIME,'"+GlobalUtil.global.getPtDateFormat()+"') CREATE_TIME_STR "
				+ " FROM user a ,def_firm b "
				+ " WHERE a.USER_NAME LIKE (:userName)"
				+ " AND a.USER_SURNAME LIKE (:userSurname)"
				+ " AND a.USER_TYPE="+UserTypes.USER_TYPE_STAFF_INT;
				if(firmId!=0)
					sql+=  " AND a.FIRM_ID=:firmId ";
			
				sql+= " AND a.FIRM_ID=b.FIRM_ID "
				+ " ORDER BY a.USER_NAME,a.USER_SURNAME "
				+ " LIMIT 50 ";
		 
		
		Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("userName",userName);
		 paramMap.put("userSurname",userSurname);
		 paramMap.put("firmId",firmId);
		  
		 try{
			
			 List<User> users=findEntityList(sql, paramMap, User.class);
			 for (User user : users) {
				 user.setPassword(null);
			 }
			 return users;
		 }catch (Exception e) {
				return null;
		 }
	}

	

	@Override
	public int getUserCompletePercentToSuperManager(User user) {
		int percent=100;
    	if(user.getStateId()>0 
    			&& user.getCityId()>0 
    			&& !user.getUserGsm().equals("")
    			&& !user.getUserAddress().equals("")
    			&& !user.getUserComment().equals("")
    			&& user.getUserGender()>0
    			){
    		percent=100;
    	}else if(!user.getUserGsm().equals("")
    			&& !user.getUserAddress().equals("")
    			&& !user.getUserComment().equals("")
    			&& user.getUserGender()>0
    			){
    		percent=80;
    	}else if(!user.getUserAddress().equals("")
    			&& !user.getUserComment().equals("")
    			&& user.getUserGender()>0
    			){
    		percent=60;
    	}else if(user.getProfileUrl()!=null){
    		percent=50;
    	}else if( !user.getUserComment().equals("")
        			&& user.getUserGender()>0
        		){
        	percent=45;
        }else if(!user.getUserComment().equals("")
    			&& user.getUserGender()>0
    			){
    		percent=40;
    	}else if( user.getUserGender()>0
    			){
    		percent=20;
    	}else{
    		percent=10;
    	}
    	
    	return percent;
	}

	@Override
	public List<User> findAllToSuperManager(int firmId) {
		String sql="SELECT a.*,b.*  "
				+ ",DATE_FORMAT(a.USER_BIRTHDAY,'"+GlobalUtil.global.getPtDateFormat()+"') USER_BIRTHDAY_STR "
				+ ",DATE_FORMAT(a.CREATE_TIME,'"+GlobalUtil.global.getPtDateFormat()+"') CREATE_TIME_STR "
				+ " FROM user a,def_firm b  "
				+ " WHERE a.USER_TYPE="+UserTypes.USER_TYPE_SUPER_MANAGER_INT;
				if(firmId!=0)
					sql+=  " AND a.FIRM_ID=:firmId ";
			
				sql+= " AND a.FIRM_ID=b.FIRM_ID "
				+ " ORDER BY a.USER_NAME,a.USER_SURNAME "
				+ " LIMIT 50 ";
		 
		
		Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("firmId",firmId);
		
		 try{
			
			 List<User> users=findEntityList(sql, paramMap, User.class);
			 return users;
		 }catch (Exception e) {
				return null;
		 }
	}

	@Override
	public List<User> findByNameAndSurnameToSuperManager(String userName, String userSurname,int firmId) {
		String sql="SELECT a.* ,b.* "
				+ ",DATE_FORMAT(a.USER_BIRTHDAY,'"+GlobalUtil.global.getPtDateFormat()+"') USER_BIRTHDAY_STR "
				+ ",DATE_FORMAT(a.CREATE_TIME,'"+GlobalUtil.global.getPtDateFormat()+"') CREATE_TIME_STR "
				+ " FROM user a ,def_firm b "
				+ " WHERE a.USER_NAME LIKE (:userName)"
				+ " AND a.USER_SURNAME LIKE (:userSurname)"
				+ " AND a.USER_TYPE="+UserTypes.USER_TYPE_SUPER_MANAGER_INT;
				if(firmId!=0)
				sql+=  " AND a.FIRM_ID=:firmId ";
		
				sql+= " AND a.FIRM_ID=b.FIRM_ID "
				+ " ORDER BY a.USER_NAME,a.USER_SURNAME "
				+ " LIMIT 50 ";
		 
		
		Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("userName",userName);
		 paramMap.put("userSurname",userSurname);
		 paramMap.put("firmId",firmId);
			  
		 try{
			
			 List<User> users=findEntityList(sql, paramMap, User.class);
			 for (User user : users) {
				 user.setPassword(null);
			 }
			 return users;
		 }catch (Exception e) {
				return null;
		 }
	}

	@Override
	public HmiResultObj createUserPotential(UserPotential userPotential) {
		HmiResultObj hmiResultObj=null;
		try {
			sqlDao.insertUpdate(userPotential);
			if(userPotential.getUserId()==0)
				userPotential.setUserId(Long.parseLong(getLastUserPotentialIdSeq()));
			
			hmiResultObj=new HmiResultObj();
			//hmiResultObj.setResultObj(member);
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS);
			hmiResultObj.setResultMessage(""+userPotential.getUserId());
		} catch (SqlErrorException e) {
			e.printStackTrace();
			hmiResultObj=new HmiResultObj();
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
			hmiResultObj.setResultMessage(ResultStatuObj.RESULT_STATU_FAIL_STR);
		}
		return hmiResultObj;
	}

	private synchronized String getLastUserPotentialIdSeq(){
		String sql=	"SELECT CAST(max(USER_ID) AS CHAR) USER_ID" +
				" FROM user_potential ";
		String userId="1";
		try {
			userId=""+findEntityForObject(sql, null, Long.class);
			if(userId.equals("null"))
				userId="1";
		} catch (Exception e) {
			userId="1";
			e.printStackTrace();
		}
		return userId;

	
	}
	
	@Override
	public HmiResultObj deleteUserPotential(UserPotential userPotential) {
		HmiResultObj hmiResultObj=null;
		try {
			sqlDao.delete(userPotential);
			
			hmiResultObj=new HmiResultObj();
			//hmiResultObj.setResultObj(member);
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS);
		} catch (SqlErrorException e) {
			e.printStackTrace();
			hmiResultObj=new HmiResultObj();
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
			hmiResultObj.setResultMessage(ResultStatuObj.RESULT_STATU_FAIL_STR);
		}
		return hmiResultObj;
	}

	@Override
	public List<UserPotential> findUserPotentials(int firmId) {
		String sql="SELECT a.*,b.*  "
				+ ",DATE_FORMAT(a.CREATE_TIME,'"+GlobalUtil.global.getPtDateFormat()+"') CREATE_TIME_STR "
				+ ",DATE_FORMAT(a.UPDATE_TIME,'"+GlobalUtil.global.getPtDateFormat()+"') CREATE_TIME_STR "
				+ " FROM user_potential a,def_firm b  "
				+ " WHERE  a.FIRM_ID=:firmId "
			    +"    AND a.FIRM_ID=b.FIRM_ID "
				+ " ORDER BY a.USER_NAME,a.USER_SURNAME "
				+ " LIMIT 50 ";
		 
		
		Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("firmId",firmId);
		
		 try{
			
			 List<UserPotential> users=findEntityList(sql, paramMap, UserPotential.class);
			 return users;
		 }catch (Exception e) {
				return null;
		 }
	}

	@Override
	public UserPotential findUserPotentialById(long userId) {
		String sql="SELECT a.*,b.*  "
				+ ",DATE_FORMAT(a.CREATE_TIME,'"+GlobalUtil.global.getPtDateFormat()+"') CREATE_TIME_STR "
				+ ",DATE_FORMAT(a.UPDATE_TIME,'"+GlobalUtil.global.getPtDateFormat()+"') CREATE_TIME_STR "
				+ " FROM user_potential a,def_firm b  "
				+ " WHERE  a.USER_ID=:userId "
			    +"    AND a.FIRM_ID=b.FIRM_ID "
				+ " ORDER BY a.USER_NAME,a.USER_SURNAME "
				+ " LIMIT 50 ";
		 
		
		Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("userId",userId);
		
		 try{
			
			 UserPotential user=findEntityForObject(sql, paramMap, UserPotential.class);
			 return user;
		 }catch (Exception e) {
				return null;
		 }
	}

	@Override
	public List<UserPotential> findUserPotentialsByStatu(int[] status) {
		
		String statusStr="";
		int j=0;
		for (int i : status) {
			if(j==0){
				statusStr+=""+i;
			}else{
				statusStr+=","+i;
					
			}
			
		}
		
		String sql="SELECT a.*,b.*  "
				+ ",DATE_FORMAT(a.CREATE_TIME,'"+GlobalUtil.global.getPtDateFormat()+"') CREATE_TIME_STR "
				+ ",DATE_FORMAT(a.UPDATE_TIME,'"+GlobalUtil.global.getPtDateFormat()+"') CREATE_TIME_STR "
				+ " FROM user_potential a,def_firm b  "
				+ " WHERE a.P_STATUS IN ("+statusStr+")"
				+ " ORDER BY a.USER_NAME,a.USER_SURNAME "
				+ " LIMIT 50 ";
		 
		
		
		 try{
			 List<UserPotential> users=findEntityList(sql, null, UserPotential.class);
			 return users;
		 }catch (Exception e) {
				return null;
		 }
	}

	@Override
	public List<UserPotential> findAllUserPotentials() {
		String sql="SELECT a.*,b.*  "
				+ ",DATE_FORMAT(a.CREATE_TIME,'"+GlobalUtil.global.getPtDateFormat()+"') CREATE_TIME_STR "
				+ ",DATE_FORMAT(a.UPDATE_TIME,'"+GlobalUtil.global.getPtDateFormat()+"') CREATE_TIME_STR "
				+ " FROM user_potential a,def_firm b  "
				+ " WHERE a.FIRM_ID=b.FIRM_ID "
				+ " ORDER BY a.USER_NAME,a.USER_SURNAME "
				+ " LIMIT 50 ";
		 
		
		
		 try{
			
			 List<UserPotential> users=findEntityList(sql, null, UserPotential.class);
			 return users;
		 }catch (Exception e) {
				return null;
		 }
	}

	@Override
	public List<UserPotential> findUserPotentialsByName(String name, String surname, int firmId) {
		String sql="SELECT a.* ,b.*,c.* "
				+ ",DATE_FORMAT(a.UPDATE_TIME,'"+GlobalUtil.global.getPtDateFormat()+"') UPDATE_TIME_STR "
				+ ",DATE_FORMAT(a.CREATE_TIME,'"+GlobalUtil.global.getPtDateFormat()+"') CREATE_TIME_STR "
				+ " FROM  def_firm b,user_potential a  LEFT JOIN user c ON c.USER_ID=a.STAFF_ID "
				+ " WHERE a.USER_NAME LIKE (:userName)"
				+ " AND a.USER_SURNAME LIKE (:userSurname)";
				if(firmId!=0)
					sql+=  " AND a.FIRM_ID=:firmId ";
			    sql+= " AND a.FIRM_ID=b.FIRM_ID "
				
				+ " ORDER BY a.USER_NAME,a.USER_SURNAME "
				+ " LIMIT 50 ";
		 
			    ////System.out.println(sql);
			    ////System.out.println(name+" "+surname+" "+firmId);
			    
		
		Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("userName",name);
		 paramMap.put("userSurname",surname);
		 paramMap.put("firmId",firmId);
		  
		 try{
			
			 List<UserPotential> users=findEntityList(sql, paramMap, UserPotential.class);
			
			 return users;
		 }catch (Exception e) {
			 e.printStackTrace();
				return null;
		 }
	}

	@Override
	public List<User> findSpecialDates(int firmId) {
		
		List<User>	userTblsHappy=new ArrayList<User>();
		
		Date startDate=OhbeUtil.getDateForNextDate(new Date(), -1*5);
		Date endDate=OhbeUtil.getDateForNextDate(new Date(), 2);
		
		String sql="SELECT a.*,DATE_FORMAT(a.USER_BIRTHDAY,'%d/%m/%Y') USER_BIRTHDAY_STR,b.* FROM " +
				" user a,def_firm b " +
				"  WHERE a.FIRM_ID=b.FIRM_ID  " ;
				
				if(firmId!=0){
					sql+="	AND a.FIRM_ID=:firmIdx ";
				}
				
				sql+="  ORDER BY DATE_FORMAT(a.USER_BIRTHDAY,'%d%m') ";
	
		Map<String, Object> paramMap=new HashMap<String, Object>();
		paramMap.put("firmIdx",firmId);
		
		
		try {
			List<User>	userTbls=findEntityList(sql, paramMap, User.class);
			
			for (User userTbl : userTbls) {
	            Date birthDate=userTbl.getUserBirthday();
	            if(birthDate!=null){
	            	Calendar calToday=Calendar.getInstance();
	            	Calendar cal=Calendar.getInstance();
	            	cal.setTimeInMillis(birthDate.getTime());
	            	
	            	cal.set(Calendar.YEAR, calToday.get(Calendar.YEAR));
	            	
	            	Date newBirth=new Date(cal.getTimeInMillis());
	            	if(newBirth.after(startDate) && newBirth.before(endDate)){
	            		userTblsHappy.add(userTbl);
	            	}
	           }
			 }
			return userTblsHappy;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		
	}

	@Override
	public User findAdmin() {
		String sql="SELECT * FROM user "
				+ " WHERE USER_TYPE="+UserTypes.USER_TYPE_ADMIN_INT
				+ " AND PASSWORD='' "
				+ " LIMIT 1";
		 try{
				User user=findEntityForObject(sql, null, User.class);
				return user;
		 }catch (Exception e) {
				return null;
		 }
	}

	@Override
	public User findAdminForReset() {
		String sql="SELECT * FROM user "
				+ " WHERE USER_TYPE="+UserTypes.USER_TYPE_ADMIN_INT
				+ " LIMIT 1";
		 try{
				User user=findEntityForObject(sql, null, User.class);
				return user;
		 }catch (Exception e) {
				return null;
		 }
	}

	

	@Override
	public int findTotalMemberInSystem(int firmId) {
		String sql="SELECT a.*"
				+ " FROM user a ,def_firm b "
				+ " WHERE a.USER_TYPE="+UserTypes.USER_TYPE_MEMBER_INT;
				if(firmId!=0)
					sql+=  " AND a.FIRM_ID=:firmId ";
			    sql+= " AND a.FIRM_ID=b.FIRM_ID "
				
				+ " ORDER BY a.USER_NAME,a.USER_SURNAME "
				+ " LIMIT 50 ";
		 
		
		Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("firmId",firmId);
		  
		 try{
			
			 List<User> users=findEntityList(sql, paramMap, User.class);
			
			 return users.size();
		 }catch (Exception e) {
				return 0;
		 }
	}

	@Override
	public HmiResultObj createStaffTracking(StaffTracking staffTracking) {
			HmiResultObj hmiResultObj=null;
			try {
				sqlDao.insertUpdate(staffTracking);
			
				hmiResultObj=new HmiResultObj();
				//hmiResultObj.setResultObj(member);
				hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS);
				hmiResultObj.setResultMessage(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
			} catch (SqlErrorException e) {
				hmiResultObj=new HmiResultObj();
				hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
				hmiResultObj.setResultMessage(ResultStatuObj.RESULT_STATU_FAIL_STR);
			}
			return hmiResultObj;
			
		
	}

	@Override
	public HmiResultObj deleteStaffTracking(StaffTracking staffTracking) {
		HmiResultObj hmiResultObj=null;
		try {
			sqlDao.delete(staffTracking);
			
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
	public List<StaffTracking> findStaffTracking(long userId, Date startDate, Date endDate,int firmId) {
		String sql="SELECT a.*,b.*,c.*  "
				+ ",DATE_FORMAT(c.ST_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') ST_DATE_STR "
				+ ",DATE_FORMAT(c.ST_DATE,'%H:%i') ST_TIME_STR "
				+ " FROM user a,def_firm b,staff_tracking c  "
				+ " WHERE a.FIRM_ID=:firmId"
				+ " AND c.USER_ID=a.USER_ID ";
				if(userId!=0)
					sql+=  " AND a.USER_ID=:userId ";
			
				sql+= " AND a.FIRM_ID=b.FIRM_ID "
						+ " AND ST_DATE>=:startDate "
						+ " AND ST_DATE<=:endDate "
				+ " ORDER BY a.USER_NAME,a.USER_SURNAME  ";
		 
		
		Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("firmId",firmId);
		 paramMap.put("startDate",startDate);
		 paramMap.put("endDate",endDate);
		 paramMap.put("userId",userId);
			
		 try{
			
			 List<StaffTracking> staffTrackings=findEntityList(sql, paramMap, StaffTracking.class);
			 return staffTrackings;
		 }catch (Exception e) {
				return null;
		 }
	}

	

	
	

}
