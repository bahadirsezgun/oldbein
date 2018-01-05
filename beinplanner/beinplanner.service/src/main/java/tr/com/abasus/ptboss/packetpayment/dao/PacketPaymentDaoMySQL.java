package tr.com.abasus.ptboss.packetpayment.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tr.com.abasus.general.dao.AbaxJdbcDaoSupport;
import tr.com.abasus.general.dao.SqlDao;
import tr.com.abasus.general.exception.SqlErrorException;
import tr.com.abasus.ptboss.packetpayment.entity.PacketPaymentClass;
import tr.com.abasus.ptboss.packetpayment.entity.PacketPaymentClassDetail;
import tr.com.abasus.ptboss.packetpayment.entity.PacketPaymentFactory;
import tr.com.abasus.ptboss.packetpayment.entity.PacketPaymentMembership;
import tr.com.abasus.ptboss.packetpayment.entity.PacketPaymentMembershipDetail;
import tr.com.abasus.ptboss.packetpayment.entity.PacketPaymentPersonal;
import tr.com.abasus.ptboss.packetpayment.entity.PacketPaymentPersonalDetail;
import tr.com.abasus.ptboss.result.entity.HmiResultObj;
import tr.com.abasus.util.GlobalUtil;
import tr.com.abasus.util.OhbeUtil;
import tr.com.abasus.util.ResultStatuObj;

public class PacketPaymentDaoMySQL extends AbaxJdbcDaoSupport implements PacketPaymentDao {

	SqlDao sqlDao;
	
	

	public SqlDao getSqlDao() {
		return sqlDao;
	}

	public void setSqlDao(SqlDao sqlDao) {
		this.sqlDao = sqlDao;
	}

	/**********************************************************************************************/
	/**********************************************************************************************/
	/**********************************PERSONAL PAYMENT DAO ******************************************/
	/**********************************************************************************************/
	/**********************************************************************************************/
	
	@Override
	public PacketPaymentFactory findPacketPaymentPersonalByPayId(long payId) {
		String sql="SELECT a.*,DATE_FORMAT(a.SALES_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') SALES_DATE_STR,c.*,b.*"
		  		+ " ,DATE_FORMAT(b.USER_BIRTHDAY,'"+GlobalUtil.global.getPtDateFormat()+"') USER_BIRTHDAY_STR"
		  		+ " ,DATE_FORMAT(c.PAY_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') PAY_DATE_STR"
		  		+ " ,DATE_FORMAT(c.CHANGE_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') CHANGE_DATE_STR"
					+ " FROM packet_payment_personal c ,packet_sale_personal a, user b "
					+ " WHERE a.USER_ID=b.USER_ID "
		            + " AND a.SALE_ID=c.SALE_ID "
		            + " AND c.PAY_ID=:payId   ";
			 
			
			 Map<String, Object> paramMap=new HashMap<String, Object>();
			 paramMap.put("payId",payId);
			 PacketPaymentFactory packetPaymentFactory=null;
			 try{
				 packetPaymentFactory=findEntityForObject(sql, paramMap, PacketPaymentPersonal.class);
				 return packetPaymentFactory;
			 }catch (Exception e) {
				 return null;
			 }
		
	}
	
	
	@Override
	public PacketPaymentFactory findPacketPaymentPersonal(long salesId) {
		String sql="SELECT a.*,DATE_FORMAT(a.SALES_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') SALES_DATE_STR,c.*,b.*"
		  		+ " ,DATE_FORMAT(b.USER_BIRTHDAY,'"+GlobalUtil.global.getPtDateFormat()+"') USER_BIRTHDAY_STR"
		  		+ " ,DATE_FORMAT(c.PAY_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') PAY_DATE_STR"
		  		+ " ,DATE_FORMAT(c.CHANGE_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') CHANGE_DATE_STR"
					+ " FROM packet_payment_personal c ,packet_sale_personal a, user b "
					+ " WHERE a.USER_ID=b.USER_ID "
		            + " AND a.SALE_ID=c.SALE_ID "
		            + " AND c.SALE_ID=:salesId   ";
			 
			
			 Map<String, Object> paramMap=new HashMap<String, Object>();
			 paramMap.put("salesId",salesId);
			 PacketPaymentFactory packetPaymentFactory=null;
			 try{
				 List<PacketPaymentPersonal>  packetPaymentPersonals=findEntityList(sql, paramMap, PacketPaymentPersonal.class);
				 
				 if(packetPaymentPersonals.size()>1){
					 for (int i = 0; i < packetPaymentPersonals.size()-1; i++) {
						 PacketPaymentPersonal packetPaymentPersonal=packetPaymentPersonals.get(i);
						 deletePacketPaymentPersonal(packetPaymentPersonal);
					}
				 }
				 
				 packetPaymentFactory=findEntityForObject(sql, paramMap,  PacketPaymentPersonal.class);
				 
				 return packetPaymentFactory;
			 }catch (Exception e) {
				 return null;
			 }
		
		
	}

	@Override
	public List<PacketPaymentPersonalDetail> findPacketPaymentPersonalDetail(long payId) {
		String sql="SELECT a.*"
				+ " ,DATE_FORMAT(a.PAY_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') PAY_DATE_STR"
		  		+ " ,DATE_FORMAT(a.CHANGE_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') CHANGE_DATE_STR"
					+ " FROM packet_payment_personal_detail a, packet_payment_personal b"
					+ " WHERE a.PAY_ID=:payId "
		            + " AND a.PAY_ID=b.PAY_ID  ";
			 
			
			 Map<String, Object> paramMap=new HashMap<String, Object>();
			 paramMap.put("payId",payId);
			
			 try{
				 List<PacketPaymentPersonalDetail> packetPaymentPersonalDetails=findEntityList(sql, paramMap, PacketPaymentPersonalDetail.class);
				 return packetPaymentPersonalDetails;
			 }catch (Exception e) {
				 e.printStackTrace();
					return null;
			 }
	}

	@Override
	public HmiResultObj createPacketPaymentPersonal(PacketPaymentPersonal packetPaymentPersonal) {
		HmiResultObj hmiResultObj=null;
		try {
			sqlDao.insertUpdate(packetPaymentPersonal);
			if(packetPaymentPersonal.getPayId()==0)
				packetPaymentPersonal.setPayId(Integer.parseInt(getLastPaymentPersonalIdSeq()));
			
			hmiResultObj=new HmiResultObj();
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS);
			hmiResultObj.setResultMessage(""+packetPaymentPersonal.getPayId());
		} catch (SqlErrorException e) {
			e.printStackTrace();
			hmiResultObj=new HmiResultObj();
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
		}
		return hmiResultObj;
	}

	
	private String getLastPaymentPersonalIdSeq(){
		String sql=	"SELECT CAST(max(PAY_ID) AS CHAR) SALE_ID" +
				" FROM packet_payment_personal ";
		String saleId="1";
		try {
			saleId=""+findEntityForObject(sql, null, Long.class);
			if(saleId.equals("null"))
				saleId="1";
		} catch (Exception e) {
			saleId="1";
			e.printStackTrace();
		}
		return saleId;

	}
	
	
	@Override
	public HmiResultObj createPacketPaymentPersonalDetail(PacketPaymentPersonalDetail packetPaymentPersonalDetail) {
		HmiResultObj hmiResultObj=null;
		try {
			sqlDao.insertUpdate(packetPaymentPersonalDetail);
			
			hmiResultObj=new HmiResultObj();
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS);
			hmiResultObj.setResultMessage("success");
		} catch (SqlErrorException e) {
			e.printStackTrace();
			hmiResultObj=new HmiResultObj();
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
		}
		return hmiResultObj;
	}

	@Override
	public HmiResultObj deletePacketPaymentPersonal(PacketPaymentPersonal packetPaymentPersonal) {
		HmiResultObj hmiResultObj=null;
		try {
			sqlDao.delete(packetPaymentPersonal);
			
			hmiResultObj=new HmiResultObj();
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS);
			hmiResultObj.setResultMessage("success");
		} catch (SqlErrorException e) {
			e.printStackTrace();
			hmiResultObj=new HmiResultObj();
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
		}
		return hmiResultObj;
	}
	
	@Override
	public HmiResultObj deletePacketPaymentPersonalDetail(long payDetId) {
		
		PacketPaymentPersonalDetail packetPaymentPersonalDetail=new PacketPaymentPersonalDetail();
		packetPaymentPersonalDetail.setPayDetId(payDetId);
		HmiResultObj hmiResultObj=null;
		try {
			sqlDao.delete(packetPaymentPersonalDetail);
			
			hmiResultObj=new HmiResultObj();
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS);
			hmiResultObj.setResultMessage("success");
		} catch (SqlErrorException e) {
			e.printStackTrace();
			hmiResultObj=new HmiResultObj();
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
		}
		return hmiResultObj;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<PacketPaymentFactory> findPaymentPersonalToConfirm(String userName, String userSurname, int confimed,
			int unConfirmed) {
		String sql="SELECT a.*,b.*,c.*,d.*,e.* "
				+ " ,DATE_FORMAT(a.PAY_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') PAY_DATE_STR"
				+ " ,DATE_FORMAT(b.SALES_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') SALES_DATE_STR"
		  		+ " ,DATE_FORMAT(a.CHANGE_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') CHANGE_DATE_STR"
		  				+ ",'ppp' TYPE"
					+ " FROM packet_payment_personal a,packet_sale_personal b,user c ,program_personal d,def_firm e "
					+ " WHERE a.SALE_ID = b.SALE_ID "
		            + " AND c.USER_ID=b.USER_ID "
		            + " AND c.USER_NAME LIKE (:userName)"
		            + " AND c.USER_SURNAME LIKE (:userSurname)"
		            + " AND d.PROG_ID=b.PROG_ID "
		            + " AND e.FIRM_ID=c.FIRM_ID";
		
		   if(confimed==0 && unConfirmed==1){
			   sql+= " AND a.PAY_CONFIRM=0 ";
		   }else if(confimed==1 && unConfirmed==0){
			   sql+= " AND a.PAY_CONFIRM=1 ";
		   }
		   
		   ////System.out.println(sql);
		   ////System.out.println(userName);
		   ////System.out.println(userSurname);
			
			 Map<String, Object> paramMap=new HashMap<String, Object>();
			 paramMap.put("userName",userName);
			 paramMap.put("userSurname",userSurname);
				
			 try{
				 List<? extends PacketPaymentFactory> packetPaymentPersonalDetails=findEntityList(sql, paramMap, PacketPaymentPersonal.class);
				 return (List<PacketPaymentFactory>)packetPaymentPersonalDetails ;
			 }catch (Exception e) {
				 e.printStackTrace();
					return null;
			 }
		
	}
	
	/**********************************************************************************************/
	/**********************************************************************************************/
	/**********************************CLASS PAYMENT DAO ******************************************/
	/**********************************************************************************************/
	/**********************************************************************************************/
	
	@Override
	public PacketPaymentFactory findPacketPaymentClassByPayId(long payId) {
		String sql="SELECT a.*,DATE_FORMAT(a.SALES_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') SALES_DATE_STR,c.*,b.*"
		  		+ " ,DATE_FORMAT(b.USER_BIRTHDAY,'"+GlobalUtil.global.getPtDateFormat()+"') USER_BIRTHDAY_STR"
		  		+ " ,DATE_FORMAT(c.PAY_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') PAY_DATE_STR"
		  		+ " ,DATE_FORMAT(c.CHANGE_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') CHANGE_DATE_STR"
					+ " FROM packet_payment_class c ,packet_sale_class a, user b "
					+ " WHERE a.USER_ID=b.USER_ID "
		            + " AND a.SALE_ID=c.SALE_ID "
		            + " AND c.PAY_ID=:payId   ";
			 
			
			 Map<String, Object> paramMap=new HashMap<String, Object>();
			 paramMap.put("payId",payId);
			
			 try{
				 PacketPaymentFactory packetPaymentFactory=findEntityForObject(sql, paramMap, PacketPaymentClass.class);
				 return packetPaymentFactory;
			 }catch (Exception e) {
					return null;
			 }
	}
	
	@Override
	public PacketPaymentFactory findPacketPaymentClass(long salesId) {
		String sql="SELECT a.*,DATE_FORMAT(a.SALES_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') SALES_DATE_STR,c.*,b.*"
		  		+ " ,DATE_FORMAT(b.USER_BIRTHDAY,'"+GlobalUtil.global.getPtDateFormat()+"') USER_BIRTHDAY_STR"
		  		+ " ,DATE_FORMAT(c.PAY_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') PAY_DATE_STR"
		  		+ " ,DATE_FORMAT(c.CHANGE_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') CHANGE_DATE_STR"
					+ " FROM packet_payment_class c ,packet_sale_class a, user b "
					+ " WHERE a.USER_ID=b.USER_ID "
		            + " AND a.SALE_ID=c.SALE_ID "
		            + " AND c.SALE_ID=:salesId   ";
			 
			
			 Map<String, Object> paramMap=new HashMap<String, Object>();
			 paramMap.put("salesId",salesId);
			
			 try{
				 
				 List<PacketPaymentClass>  packetPaymentClasses=findEntityList(sql, paramMap, PacketPaymentClass.class);
				 
				 if(packetPaymentClasses.size()>1){
					 for (int i = 0; i < packetPaymentClasses.size()-1; i++) {
						 PacketPaymentClass packetPaymentClass=packetPaymentClasses.get(i);
						 deletePacketPaymentClass(packetPaymentClass);
					}
				 }
				 
				 
				 PacketPaymentFactory packetPaymentFactory=findEntityForObject(sql, paramMap, PacketPaymentClass.class);
				 return packetPaymentFactory;
			 }catch (Exception e) {
					return null;
			 }
	}

	@Override
	public List<PacketPaymentClassDetail> findPacketPaymentClassDetail(long payId) {
		String sql="SELECT a.*"
				+ " ,DATE_FORMAT(a.PAY_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') PAY_DATE_STR"
		  		+ " ,DATE_FORMAT(a.CHANGE_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') CHANGE_DATE_STR"
					+ " FROM packet_payment_class_detail a, packet_payment_class b"
					+ " WHERE a.PAY_ID=:payId "
		            + " AND a.PAY_ID=b.PAY_ID  ";
			 
			
			 Map<String, Object> paramMap=new HashMap<String, Object>();
			 paramMap.put("payId",payId);
			
			 try{
				 List<PacketPaymentClassDetail> packetPaymentClassDetails=findEntityList(sql, paramMap, PacketPaymentClassDetail.class);
				 return packetPaymentClassDetails;
			 }catch (Exception e) {
				 e.printStackTrace();
					return null;
			 }
	}

	@Override
	public HmiResultObj createPacketPaymentClass(PacketPaymentClass packetPaymentClass) {
		HmiResultObj hmiResultObj=null;
		try {
			sqlDao.insertUpdate(packetPaymentClass);
			if(packetPaymentClass.getPayId()==0)
				packetPaymentClass.setPayId(Integer.parseInt(getLastPaymentClassIdSeq()));
			
			hmiResultObj=new HmiResultObj();
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS);
			hmiResultObj.setResultMessage(""+packetPaymentClass.getPayId());
		} catch (SqlErrorException e) {
			e.printStackTrace();
			hmiResultObj=new HmiResultObj();
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
		}
		return hmiResultObj;
	}

	
	private String getLastPaymentClassIdSeq(){
		String sql=	"SELECT CAST(max(PAY_ID) AS CHAR) SALE_ID" +
				" FROM packet_payment_class ";
		String saleId="1";
		try {
			saleId=""+findEntityForObject(sql, null, Long.class);
			if(saleId.equals("null"))
				saleId="1";
		} catch (Exception e) {
			saleId="1";
			e.printStackTrace();
		}
		return saleId;

	}

	@Override
	public HmiResultObj createPacketPaymentClassDetail(PacketPaymentClassDetail packetPaymentClassDetail) {
		HmiResultObj hmiResultObj=null;
		try {
			sqlDao.insertUpdate(packetPaymentClassDetail);
			
			hmiResultObj=new HmiResultObj();
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS);
			hmiResultObj.setResultMessage("success");
		} catch (SqlErrorException e) {
			e.printStackTrace();
			hmiResultObj=new HmiResultObj();
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
		}
		return hmiResultObj;
	}

	@Override
	public HmiResultObj deletePacketPaymentClass(PacketPaymentClass packetPaymentClass) {
		HmiResultObj hmiResultObj=null;
		try {
			sqlDao.delete(packetPaymentClass);
			
			hmiResultObj=new HmiResultObj();
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS);
			hmiResultObj.setResultMessage("success");
		} catch (SqlErrorException e) {
			e.printStackTrace();
			hmiResultObj=new HmiResultObj();
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
		}
		return hmiResultObj;
	}
	
	
	@Override
	public HmiResultObj deletePacketPaymentClassDetail(long payDetId) {
		PacketPaymentClassDetail packetPaymentClassDetail=new PacketPaymentClassDetail();
		packetPaymentClassDetail.setPayDetId(payDetId);
		HmiResultObj hmiResultObj=null;
		try {
			sqlDao.delete(packetPaymentClassDetail);
			
			hmiResultObj=new HmiResultObj();
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS);
			hmiResultObj.setResultMessage("success");
		} catch (SqlErrorException e) {
			e.printStackTrace();
			hmiResultObj=new HmiResultObj();
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
		}
		return hmiResultObj;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<PacketPaymentFactory> findPaymentClassToConfirm(String userName, String userSurname, int confimed,
			int unConfirmed) {
		String sql="SELECT a.*,b.*,c.*,d.*,e.* "
				+ " ,DATE_FORMAT(a.PAY_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') PAY_DATE_STR"
				+ " ,DATE_FORMAT(b.SALES_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') SALES_DATE_STR"
		  		+ " ,DATE_FORMAT(a.CHANGE_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') CHANGE_DATE_STR"
		  				+ ",'ppc' TYPE"
					+ " FROM packet_payment_class a,packet_sale_class b,user c,program_class d,def_firm e  "
					+ " WHERE a.SALE_ID = b.SALE_ID "
		            + " AND c.USER_ID=b.USER_ID "
		            + " AND c.USER_NAME LIKE (:userName)"
		            + " AND c.USER_SURNAME LIKE (:userSurname)"
		            + " AND d.PROG_ID=b.PROG_ID"
		            + " AND e.FIRM_ID=c.FIRM_ID ";
		
		   if(confimed==0 && unConfirmed==1){
			   sql+= " AND a.PAY_CONFIRM=0 ";
		   }else if(confimed==1 && unConfirmed==0){
			   sql+= " AND a.PAY_CONFIRM=1 ";
		   }
			
			 Map<String, Object> paramMap=new HashMap<String, Object>();
			 paramMap.put("userName",userName);
			 paramMap.put("userSurname",userSurname);
				
			 try{
				 List<? extends PacketPaymentFactory> packetPaymentPersonalDetails=findEntityList(sql, paramMap, PacketPaymentClass.class);
				 return (List<PacketPaymentFactory>)packetPaymentPersonalDetails ;
			 }catch (Exception e) {
				 e.printStackTrace();
					return null;
			 }
	}
	
	/**********************************************************************************************/
	/**********************************************************************************************/
	/**********************************MEMBERHSIP PAYMENT DAO *************************************/
	/**********************************************************************************************/
	/**********************************************************************************************/
	
	@Override
	public PacketPaymentFactory findPacketPaymentMembershipByPayId(long payId) {
		String sql="SELECT a.*,DATE_FORMAT(a.SALES_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') SALES_DATE_STR,c.*,b.*"
		  		+ " ,DATE_FORMAT(b.USER_BIRTHDAY,'"+GlobalUtil.global.getPtDateFormat()+"') USER_BIRTHDAY_STR"
		  		+ " ,DATE_FORMAT(c.PAY_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') PAY_DATE_STR"
		  		+ " ,DATE_FORMAT(c.CHANGE_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') CHANGE_DATE_STR"
					+ " FROM packet_payment_membership c ,packet_sale_membership a, user b "
					+ " WHERE a.USER_ID=b.USER_ID "
		            + " AND a.SALE_ID=c.SALE_ID "
		            + " AND c.PAY_ID=:payId   ";
			 
			
			 Map<String, Object> paramMap=new HashMap<String, Object>();
			 paramMap.put("payId",payId);
			
			 try{
				 PacketPaymentFactory packetPaymentFactory=findEntityForObject(sql, paramMap, PacketPaymentMembership.class);
				 return packetPaymentFactory;
			 }catch (Exception e) {
				 e.printStackTrace();
					return null;
			 }
	}
	
	@Override
	public PacketPaymentFactory findPacketPaymentMembership(long salesId) {
		String sql="SELECT a.*,DATE_FORMAT(a.SALES_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') SALES_DATE_STR,c.*,b.*"
		  		+ " ,DATE_FORMAT(b.USER_BIRTHDAY,'"+GlobalUtil.global.getPtDateFormat()+"') USER_BIRTHDAY_STR"
		  		+ " ,DATE_FORMAT(c.PAY_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') PAY_DATE_STR"
		  		+ " ,DATE_FORMAT(c.CHANGE_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') CHANGE_DATE_STR"
					+ " FROM packet_payment_membership c ,packet_sale_membership a, user b "
					+ " WHERE a.USER_ID=b.USER_ID "
		            + " AND a.SALE_ID=c.SALE_ID "
		            + " AND c.SALE_ID=:salesId   ";
			 
			
			 Map<String, Object> paramMap=new HashMap<String, Object>();
			 paramMap.put("salesId",salesId);
			
			 try{
				 
				 List<PacketPaymentMembership>  packetPaymentMemberships=findEntityList(sql, paramMap, PacketPaymentMembership.class);
				 
				 if(packetPaymentMemberships.size()>1){
					 for (int i = 0; i < packetPaymentMemberships.size()-1; i++) {
						 PacketPaymentMembership packetPaymentMembership=packetPaymentMemberships.get(i);
						 deletePacketPaymentMembership(packetPaymentMembership);
					}
				 }
				 
				 PacketPaymentFactory packetPaymentFactory=findEntityForObject(sql, paramMap, PacketPaymentMembership.class);
				 return packetPaymentFactory;
			 }catch (Exception e) {
				 	return null;
			 }
		
	}

	@Override
	public List<PacketPaymentMembershipDetail> findPacketPaymentMembershipDetail(long payId) {
		String sql="SELECT a.*"
				+ " ,DATE_FORMAT(a.PAY_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') PAY_DATE_STR"
		  		+ " ,DATE_FORMAT(a.CHANGE_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') CHANGE_DATE_STR"
					+ " FROM packet_payment_membership_detail a, packet_payment_membership b"
					+ " WHERE a.PAY_ID=:payId "
		            + " AND a.PAY_ID=b.PAY_ID  ";
			 
			
			 Map<String, Object> paramMap=new HashMap<String, Object>();
			 paramMap.put("payId",payId);
			
			 try{
				 List<PacketPaymentMembershipDetail> packetPaymentMembershipDetails=findEntityList(sql, paramMap, PacketPaymentMembershipDetail.class);
				 return packetPaymentMembershipDetails;
			 }catch (Exception e) {
				 e.printStackTrace();
					return null;
			 }
	}

	@Override
	public HmiResultObj createPacketPaymentMembership(PacketPaymentMembership packetPaymentPersonal) {
		HmiResultObj hmiResultObj=null;
		try {
			sqlDao.insertUpdate(packetPaymentPersonal);
			if(packetPaymentPersonal.getPayId()==0)
				packetPaymentPersonal.setPayId(Integer.parseInt(getLastPaymentMembershipIdSeq()));
			
			hmiResultObj=new HmiResultObj();
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS);
			hmiResultObj.setResultMessage(""+packetPaymentPersonal.getPayId());
		} catch (SqlErrorException e) {
			e.printStackTrace();
			hmiResultObj=new HmiResultObj();
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
		}
		return hmiResultObj;
	}
	
	private String getLastPaymentMembershipIdSeq(){
		String sql=	"SELECT CAST(max(PAY_ID) AS CHAR) SALE_ID" +
				" FROM packet_payment_membership ";
		String saleId="1";
		try {
			saleId=""+findEntityForObject(sql, null, Long.class);
			if(saleId.equals("null"))
				saleId="1";
		} catch (Exception e) {
			saleId="1";
			e.printStackTrace();
		}
		return saleId;

	}
	

	@Override
	public HmiResultObj createPacketPaymentMembershipDetail(
			PacketPaymentMembershipDetail packetPaymentMembershipDetail) {
		HmiResultObj hmiResultObj=null;
		try {
			sqlDao.insertUpdate(packetPaymentMembershipDetail);
			
			hmiResultObj=new HmiResultObj();
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS);
			hmiResultObj.setResultMessage("success");
		} catch (SqlErrorException e) {
			e.printStackTrace();
			hmiResultObj=new HmiResultObj();
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
		}
		return hmiResultObj;
	}

	@Override
	public HmiResultObj deletePacketPaymentMembership(PacketPaymentMembership packetPaymentMembership) {
		HmiResultObj hmiResultObj=null;
		try {
			sqlDao.delete(packetPaymentMembership);
			
			hmiResultObj=new HmiResultObj();
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS);
			hmiResultObj.setResultMessage("success");
		} catch (SqlErrorException e) {
			e.printStackTrace();
			hmiResultObj=new HmiResultObj();
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
		}
		return hmiResultObj;
	}

	

	@Override
	public HmiResultObj deletePacketPaymentMembershipDetail(long payDetId) {
		PacketPaymentMembershipDetail packetPaymentMembershipDetail=new PacketPaymentMembershipDetail();
		packetPaymentMembershipDetail.setPayDetId(payDetId);
		HmiResultObj hmiResultObj=null;
		try {
			sqlDao.delete(packetPaymentMembershipDetail);
			
			hmiResultObj=new HmiResultObj();
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS);
			hmiResultObj.setResultMessage("success");
		} catch (SqlErrorException e) {
			e.printStackTrace();
			hmiResultObj=new HmiResultObj();
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
		}
		return hmiResultObj;
	}

	

	

	@SuppressWarnings("unchecked")
	@Override
	public List<PacketPaymentFactory> findPaymentMembershipToConfirm(String userName, String userSurname, int confimed,
			int unConfirmed) {
		String sql="SELECT a.*,b.*,c.*,d.*,e.* "
				+ " ,DATE_FORMAT(a.PAY_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') PAY_DATE_STR"
				+ " ,DATE_FORMAT(b.SALES_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') SALES_DATE_STR"
		  		+ " ,DATE_FORMAT(a.CHANGE_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') CHANGE_DATE_STR"
		  				+ ",'ppm' TYPE"
					+ " FROM packet_payment_membership a,packet_sale_membership b,user c,program_membership d,def_firm e "
					+ " WHERE a.SALE_ID = b.SALE_ID "
		            + " AND c.USER_ID=b.USER_ID "
		            + " AND c.USER_NAME LIKE (:userName)"
		            + " AND c.USER_SURNAME LIKE (:userSurname)"
		            + " AND d.PROG_ID=b.PROG_ID "
		            + " AND e.FIRM_ID=c.FIRM_ID ";
		
		   if(confimed==0 && unConfirmed==1){
			   sql+= " AND a.PAY_CONFIRM=0 ";
		   }else if(confimed==1 && unConfirmed==0){
			   sql+= " AND a.PAY_CONFIRM=1 ";
		   }
			
			 Map<String, Object> paramMap=new HashMap<String, Object>();
			 paramMap.put("userName",userName);
			 paramMap.put("userSurname",userSurname);
				
			 try{
				 List<? extends PacketPaymentFactory> packetPaymentPersonalDetails=findEntityList(sql, paramMap, PacketPaymentMembership.class);
				 return (List<PacketPaymentFactory>)packetPaymentPersonalDetails ;
			 }catch (Exception e) {
				 e.printStackTrace();
					return null;
			 }
	}

	@Override
	public double findTotalIncomePaymentInMonthForPersonal(int firmId, int month,int year) {
		
		String monthStr=""+month;
		if(month<10)
			 monthStr="0"+month;
		
		String payDateStr="01/"+monthStr+"/"+year+" 00:00";
		
		Date payStartDate=OhbeUtil.getThatDayFormatNotNull(payDateStr, "dd/MM/yyyy HH:mm");
		Date payEndDate=OhbeUtil.getDateForNextMonth(payStartDate, 1);
		
		
		String sql=	"SELECT COALESCE(SUM(PAY_AMOUNT),0)  PAY_AMOUNT" +
				" FROM packet_payment_personal"
				+ " WHERE PAY_DATE>=:payStartDate"
				+ " AND PAY_DATE<:payEndDate ";
		

		 Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("payStartDate",payStartDate);
		 paramMap.put("payEndDate",payEndDate);
		
		 
		double payAmount=0;
		try {
			payAmount=findEntityForObject(sql, paramMap, Double.class);
			
		} catch (Exception e) {
			payAmount=0;
			e.printStackTrace();
		}
		return payAmount;
		
	}

	@Override
	public double findTotalIncomePaymentInMonthForClass(int firmId, int month,int year) {
		String monthStr=""+month;
		if(month<10)
			 monthStr="0"+month;
		
		String payDateStr="01/"+monthStr+"/"+year+" 00:00";
		
		Date payStartDate=OhbeUtil.getThatDayFormatNotNull(payDateStr, "dd/MM/yyyy HH:mm");
		Date payEndDate=OhbeUtil.getDateForNextMonth(payStartDate, 1);
		
		
		String sql=	"SELECT COALESCE(SUM(PAY_AMOUNT),0)  PAY_AMOUNT" +
				" FROM packet_payment_class"
				+ " WHERE PAY_DATE>=:payStartDate"
				+ " AND PAY_DATE<:payEndDate ";
		

		 Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("payStartDate",payStartDate);
		 paramMap.put("payEndDate",payEndDate);
		
		 
		double payAmount=0;
		try {
			payAmount=findEntityForObject(sql, paramMap, Double.class);
			
		} catch (Exception e) {
			payAmount=0;
			e.printStackTrace();
		}
		return payAmount;
	}

	@Override
	public double findTotalIncomePaymentInMonthForMembership(int firmId, int month,int year) {
		String monthStr=""+month;
		if(month<10)
			 monthStr="0"+month;
		
		String payDateStr="01/"+monthStr+"/"+year+" 00:00";
		
		Date payStartDate=OhbeUtil.getThatDayFormatNotNull(payDateStr, "dd/MM/yyyy HH:mm");
		Date payEndDate=OhbeUtil.getDateForNextMonth(payStartDate, 1);
		
		
		String sql=	"SELECT COALESCE(SUM(PAY_AMOUNT),0)  PAY_AMOUNT" +
				" FROM packet_payment_membership"
				+ " WHERE PAY_DATE>=:payStartDate"
				+ " AND PAY_DATE<:payEndDate ";
		

		 Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("payStartDate",payStartDate);
		 paramMap.put("payEndDate",payEndDate);
		
		 
		double payAmount=0;
		try {
			payAmount=findEntityForObject(sql, paramMap, Double.class);
			
		} catch (Exception e) {
			payAmount=0;
			e.printStackTrace();
		}
		return payAmount;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PacketPaymentFactory> findIncomePaymentInMonthForPersonal(int firmId, int month, int year) {
		String monthStr=""+month;
		if(month<10)
			 monthStr="0"+month;
		
		String payDateStr="01/"+monthStr+"/"+year+" 00:00";
		
		Date payStartDate=OhbeUtil.getThatDayFormatNotNull(payDateStr, "dd/MM/yyyy HH:mm");
		Date payEndDate=OhbeUtil.getDateForNextMonth(payStartDate, 1);
		
		
		String sql="SELECT a.*,DATE_FORMAT(a.SALES_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') SALES_DATE_STR,c.*,b.*"
		  		+ " ,DATE_FORMAT(b.USER_BIRTHDAY,'"+GlobalUtil.global.getPtDateFormat()+"') USER_BIRTHDAY_STR"
		  		+ " ,DATE_FORMAT(c.PAY_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') PAY_DATE_STR"
		  		+ " ,DATE_FORMAT(c.CHANGE_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') CHANGE_DATE_STR"
				+ " FROM packet_payment_personal c ,packet_sale_personal a, user b "
				+ " WHERE c.PAY_DATE>=:payStartDate"
				+ " AND c.PAY_DATE<:payEndDate"
				+ " AND  a.SALE_ID = c.SALE_ID "
		        + " AND a.USER_ID=b.USER_ID ";
		

		 Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("payStartDate",payStartDate);
		 paramMap.put("payEndDate",payEndDate);
		
		
		 try {
			 List<PacketPaymentPersonal>  packetPaymentPersonals=findEntityList(sql, paramMap, PacketPaymentPersonal.class);
			 
			 List<? extends PacketPaymentFactory> packetPaymentFactories=packetPaymentPersonals;
			 
			 return ( List<PacketPaymentFactory> )packetPaymentFactories;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PacketPaymentFactory> findIncomePaymentInMonthForClass(int firmId, int month, int year) {
		String monthStr=""+month;
		if(month<10)
			 monthStr="0"+month;
		
		String payDateStr="01/"+monthStr+"/"+year+" 00:00";
		
		Date payStartDate=OhbeUtil.getThatDayFormatNotNull(payDateStr, "dd/MM/yyyy HH:mm");
		Date payEndDate=OhbeUtil.getDateForNextMonth(payStartDate, 1);
		
		
		String sql="SELECT a.*,DATE_FORMAT(a.SALES_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') SALES_DATE_STR,c.*,b.*"
		  		+ " ,DATE_FORMAT(b.USER_BIRTHDAY,'"+GlobalUtil.global.getPtDateFormat()+"') USER_BIRTHDAY_STR"
		  		+ " ,DATE_FORMAT(c.PAY_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') PAY_DATE_STR"
		  		+ " ,DATE_FORMAT(c.CHANGE_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') CHANGE_DATE_STR"
				+ " FROM packet_payment_class c ,packet_sale_class a, user b "
				+ " WHERE c.PAY_DATE>=:payStartDate"
				+ " AND c.PAY_DATE<:payEndDate"
				+ " AND  a.SALE_ID = c.SALE_ID "
		        + " AND a.USER_ID=b.USER_ID ";
		

		 Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("payStartDate",payStartDate);
		 paramMap.put("payEndDate",payEndDate);
		
		
		 try {
			 List<PacketPaymentClass>  packetPaymentClasses=findEntityList(sql, paramMap, PacketPaymentClass.class);
			 List<? extends PacketPaymentFactory> packetPaymentFactories=packetPaymentClasses;
			 return ( List<PacketPaymentFactory> )packetPaymentFactories;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PacketPaymentFactory> findIncomePaymentInMonthForMembership(int firmId, int month, int year) {
		String monthStr=""+month;
		if(month<10)
			 monthStr="0"+month;
		
		String payDateStr="01/"+monthStr+"/"+year+" 00:00";
		
		Date payStartDate=OhbeUtil.getThatDayFormatNotNull(payDateStr, "dd/MM/yyyy HH:mm");
		Date payEndDate=OhbeUtil.getDateForNextMonth(payStartDate, 1);
		
		
		String sql="SELECT a.*,DATE_FORMAT(a.SALES_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') SALES_DATE_STR,c.*,b.*"
		  		+ " ,DATE_FORMAT(b.USER_BIRTHDAY,'"+GlobalUtil.global.getPtDateFormat()+"') USER_BIRTHDAY_STR"
		  		+ " ,DATE_FORMAT(c.PAY_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') PAY_DATE_STR"
		  		+ " ,DATE_FORMAT(c.CHANGE_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') CHANGE_DATE_STR"
				+ " FROM packet_payment_membership c ,packet_sale_membership a, user b "
				+ " WHERE c.PAY_DATE>=:payStartDate"
				+ " AND c.PAY_DATE<:payEndDate"
				+ " AND  a.SALE_ID = c.SALE_ID "
		        + " AND a.USER_ID=b.USER_ID ";
		

		 Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("payStartDate",payStartDate);
		 paramMap.put("payEndDate",payEndDate);
		
		
		 try {
			 
			 List<PacketPaymentMembership>  packetPaymentMemberships=findEntityList(sql, paramMap, PacketPaymentMembership.class);
			 
			 List<? extends PacketPaymentFactory> packetPaymentFactories=packetPaymentMemberships;
			 
			 return ( List<PacketPaymentFactory> )packetPaymentFactories;
			 
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PacketPaymentFactory> findIncomePaymentInDateForPersonal(int firmId, Date payStartDate, Date payEndDate) {
	
		
		String sql="SELECT a.*,DATE_FORMAT(a.SALES_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') SALES_DATE_STR,c.*,b.*"
		  		+ " ,DATE_FORMAT(b.USER_BIRTHDAY,'"+GlobalUtil.global.getPtDateFormat()+"') USER_BIRTHDAY_STR"
		  		+ " ,DATE_FORMAT(c.PAY_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') PAY_DATE_STR"
		  		+ " ,DATE_FORMAT(c.CHANGE_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') CHANGE_DATE_STR"
				+ " FROM packet_payment_personal c ,packet_sale_personal a, user b "
				+ " WHERE c.PAY_DATE>=:payStartDate"
				+ " AND c.PAY_DATE<:payEndDate"
				+ " AND  a.SALE_ID = c.SALE_ID "
		        + " AND a.USER_ID=b.USER_ID ";
		

		 Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("payStartDate",payStartDate);
		 paramMap.put("payEndDate",payEndDate);
		
		
		 try {
			 List<PacketPaymentPersonal>  packetPaymentPersonals=findEntityList(sql, paramMap, PacketPaymentPersonal.class);
			 List<? extends PacketPaymentFactory> packetPaymentFactories=packetPaymentPersonals;
			 return ( List<PacketPaymentFactory> )packetPaymentFactories;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PacketPaymentFactory> findIncomePaymentInDateForClass(int firmId, Date payStartDate, Date payEndDate) {
		String sql="SELECT a.*,DATE_FORMAT(a.SALES_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') SALES_DATE_STR,c.*,b.*"
		  		+ " ,DATE_FORMAT(b.USER_BIRTHDAY,'"+GlobalUtil.global.getPtDateFormat()+"') USER_BIRTHDAY_STR"
		  		+ " ,DATE_FORMAT(c.PAY_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') PAY_DATE_STR"
		  		+ " ,DATE_FORMAT(c.CHANGE_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') CHANGE_DATE_STR"
				+ " FROM packet_payment_class c ,packet_sale_class a, user b "
				+ " WHERE c.PAY_DATE>=:payStartDate"
				+ " AND c.PAY_DATE<:payEndDate"
				+ " AND  a.SALE_ID = c.SALE_ID "
		        + " AND a.USER_ID=b.USER_ID ";
		

		 Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("payStartDate",payStartDate);
		 paramMap.put("payEndDate",payEndDate);
		
		
		 try {
			 List<PacketPaymentClass>  packetPaymentClasses=findEntityList(sql, paramMap, PacketPaymentClass.class);
			 List<? extends PacketPaymentFactory> packetPaymentFactories=packetPaymentClasses;
			 return ( List<PacketPaymentFactory> )packetPaymentFactories;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PacketPaymentFactory> findIncomePaymentInDateForMembership(int firmId, Date payStartDate, Date payEndDate) {
		String sql="SELECT a.*,DATE_FORMAT(a.SALES_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') SALES_DATE_STR,c.*,b.*"
		  		+ " ,DATE_FORMAT(b.USER_BIRTHDAY,'"+GlobalUtil.global.getPtDateFormat()+"') USER_BIRTHDAY_STR"
		  		+ " ,DATE_FORMAT(c.PAY_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') PAY_DATE_STR"
		  		+ " ,DATE_FORMAT(c.CHANGE_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') CHANGE_DATE_STR"
				+ " FROM packet_payment_membership c ,packet_sale_membership a, user b "
				+ " WHERE c.PAY_DATE>=:payStartDate"
				+ " AND c.PAY_DATE<:payEndDate"
				+ " AND  a.SALE_ID = c.SALE_ID "
		        + " AND a.USER_ID=b.USER_ID ";
		

		 Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("payStartDate",payStartDate);
		 paramMap.put("payEndDate",payEndDate);
		
		
		 try {
			 List<PacketPaymentMembership>  packetPaymentMemberships=findEntityList(sql, paramMap, PacketPaymentMembership.class);
			 List<? extends PacketPaymentFactory> packetPaymentFactories=packetPaymentMemberships;
			 return ( List<PacketPaymentFactory> )packetPaymentFactories;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PacketPaymentFactory> findPaymentInGroupDateForPersonal(int firmId) {
		String sql="SELECT SUM(PAY_AMOUNT) PAY_AMOUNT "
				+ " ,DATE_FORMAT(PAY_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') PAY_DATE_STR"
				+ " ,PAY_DATE "
				+ " FROM packet_payment_personal"
				+ " WHERE SALE_ID IN (SELECT SALE_ID FROM packet_sale_personal WHERE USER_ID IN (SELECT USER_ID FROM user WHERE FIRM_ID=:firmId))"
				+ " GROUP BY PAY_DATE DESC"
				+ " LIMIT 10  ";
		  		
		 Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("firmId",firmId);
		 
		try {
			 List<PacketPaymentPersonal>  packetPaymentPersonals=findEntityList(sql, paramMap, PacketPaymentPersonal.class);
			 List<? extends PacketPaymentFactory> packetPaymentFactories=packetPaymentPersonals;
			 return ( List<PacketPaymentFactory> )packetPaymentFactories;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PacketPaymentFactory> findPaymentInGroupDateForClass(int firmId) {
		String sql="SELECT SUM(PAY_AMOUNT) PAY_AMOUNT "
				+ " ,DATE_FORMAT(PAY_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') PAY_DATE_STR"
				+ " ,PAY_DATE "
				+ " FROM packet_payment_class"
				+ " WHERE SALE_ID IN (SELECT SALE_ID FROM packet_sale_class WHERE USER_ID IN (SELECT USER_ID FROM user WHERE FIRM_ID=:firmId))"
				+ " GROUP BY PAY_DATE DESC"
				+ " LIMIT 10  ";
		  	
		 Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("firmId",firmId);
		 
		try {
			 List<PacketPaymentClass>  packetPaymentClasss=findEntityList(sql, paramMap, PacketPaymentClass.class);
			 List<? extends PacketPaymentFactory> packetPaymentFactories=packetPaymentClasss;
			 return ( List<PacketPaymentFactory> )packetPaymentFactories;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PacketPaymentFactory> findPaymentInGroupDateForMembership(int firmId) {
		String sql="SELECT SUM(PAY_AMOUNT) PAY_AMOUNT "
				+ " ,DATE_FORMAT(PAY_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') PAY_DATE_STR"
				+ " ,PAY_DATE "
				+ " FROM packet_payment_membership"
				+ " WHERE SALE_ID IN (SELECT SALE_ID FROM packet_sale_membership WHERE USER_ID IN (SELECT USER_ID FROM user WHERE FIRM_ID=:firmId))"
				+ " GROUP BY PAY_DATE DESC"
				+ " LIMIT 10  ";
		  
		 Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("firmId",firmId);
		 
		try {
			 List<PacketPaymentMembership>  packetPaymentMemberships=findEntityList(sql, paramMap, PacketPaymentMembership.class);
			 List<? extends PacketPaymentFactory> packetPaymentFactories=packetPaymentMemberships;
			 return ( List<PacketPaymentFactory> )packetPaymentFactories;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	

	

	

	
}
