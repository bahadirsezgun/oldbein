package tr.com.abasus.ptboss.packetsale.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tr.com.abasus.general.dao.AbaxJdbcDaoSupport;
import tr.com.abasus.general.dao.SqlDao;
import tr.com.abasus.general.exception.SqlErrorException;
import tr.com.abasus.ptboss.packetpayment.entity.PacketPaymentClass;
import tr.com.abasus.ptboss.packetpayment.entity.PacketPaymentMembership;
import tr.com.abasus.ptboss.packetpayment.entity.PacketPaymentPersonal;
import tr.com.abasus.ptboss.packetpayment.service.PacketPaymentService;
import tr.com.abasus.ptboss.packetsale.entity.PacketSaleClass;
import tr.com.abasus.ptboss.packetsale.entity.PacketSaleFactory;
import tr.com.abasus.ptboss.packetsale.entity.PacketSaleMembership;
import tr.com.abasus.ptboss.packetsale.entity.PacketSalePersonal;
import tr.com.abasus.ptboss.ptuser.entity.User;
import tr.com.abasus.ptboss.result.entity.HmiResultObj;
import tr.com.abasus.util.GlobalUtil;
import tr.com.abasus.util.ResultStatuObj;
import tr.com.abasus.util.SaleStatus;
import tr.com.abasus.util.UserTypes;

public class PacketSaleDaoMySQL extends AbaxJdbcDaoSupport implements PacketSaleDao {
	
	SqlDao sqlDao;
	
	PacketPaymentService packetPaymentService;
	
	
	public SqlDao getSqlDao() {
		return sqlDao;
	}

	public void setSqlDao(SqlDao sqlDao) {
		this.sqlDao = sqlDao;
	}

	public PacketPaymentService getPacketPaymentService() {
		return packetPaymentService;
	}

	public void setPacketPaymentService(PacketPaymentService packetPaymentService) {
		this.packetPaymentService = packetPaymentService;
	}

	
	
	
	@Override
	public  synchronized HmiResultObj buyPersonalPacket(PacketSalePersonal packetSale) {
		HmiResultObj hmiResultObj=null;
		try {
			sqlDao.insertUpdate(packetSale);
			if(packetSale.getSaleId()==0)
				packetSale.setSaleId(Integer.parseInt(getLastSalesPersonalIdSeq()));
			
			hmiResultObj=new HmiResultObj();
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS);
			hmiResultObj.setResultMessage(""+packetSale.getSaleId());
		} catch (SqlErrorException e) {
			e.printStackTrace();
			hmiResultObj=new HmiResultObj();
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
		}
		return hmiResultObj;
	}
	
	@Override
	public  synchronized HmiResultObj deletePersonalPacket(PacketSalePersonal packetSale) {
		HmiResultObj hmiResultObj=null;
		try {
			sqlDao.delete(packetSale);
			
			hmiResultObj=new HmiResultObj();
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS);
		} catch (SqlErrorException e) {
			e.printStackTrace();
			hmiResultObj=new HmiResultObj();
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
		}
		return hmiResultObj;
	}
	
	private String getLastSalesPersonalIdSeq(){
		String sql=	"SELECT CAST(max(SALE_ID) AS CHAR) SALE_ID" +
				" FROM packet_sale_personal ";
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
	public List<User> findByUserNameAndSaleProgramForPersonalWithNoPlan(String userName, String userSurname, long progId,int firmId) {
			    String sql="SELECT a.* ,b.*,c.*,c.PROG_COUNT SALE_COUNT,'supp' TYPE,d.* "
						+ ",DATE_FORMAT(a.USER_BIRTHDAY,'"+GlobalUtil.global.getPtDateFormat()+"') USER_BIRTHDAY_STR "
						+ ",DATE_FORMAT(a.CREATE_TIME,'"+GlobalUtil.global.getPtDateFormat()+"') CREATE_TIME_STR "
						+ " FROM user a ,def_firm b,packet_sale_personal c,program_personal d "
						+ " WHERE a.USER_NAME LIKE (:userName)"
						+ " AND a.USER_SURNAME LIKE (:userSurname)"
						+ " AND a.USER_TYPE="+UserTypes.USER_TYPE_MEMBER_INT;
						if(firmId!=0){
							sql+=  " AND a.FIRM_ID=:firmId ";
						}
						
					    sql+= " AND a.FIRM_ID=b.FIRM_ID "
					       +"  AND c.USER_ID=a.USER_ID "
					       + " AND d.PROG_ID=c.PROG_ID "
					       +"  AND c.PROG_ID=:progId "
					       +"  AND c.SALE_STATU ="+SaleStatus.SALE_NO_PLANNED
					       +"  ORDER BY a.USER_NAME,a.USER_SURNAME ";
		
					    
					    
		Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("userName",userName);
		 paramMap.put("userSurname",userSurname);
		 paramMap.put("firmId",firmId);
		 paramMap.put("progId",progId);
				  
		 try{
			
			 List<User> users=findEntityList(sql, paramMap, User.class);
			 return users;
		 }catch (Exception e) {
			 e.printStackTrace();
				return null;
		 }
		
	}
	
	@Override
	public List<User> findByUserNameAndSaleProgramForPersonalWithPlan(String userName, String userSurname, long progId,int firmId) {
			    String sql="SELECT a.* ,b.*,c.*,c.PROG_COUNT SALE_COUNT,'supp' TYPE,d.* "
						+ ",DATE_FORMAT(a.USER_BIRTHDAY,'"+GlobalUtil.global.getPtDateFormat()+"') USER_BIRTHDAY_STR "
						+ ",DATE_FORMAT(a.CREATE_TIME,'"+GlobalUtil.global.getPtDateFormat()+"') CREATE_TIME_STR "
						+ " FROM user a ,def_firm b,packet_sale_personal c,program_personal d "
						+ " WHERE a.USER_NAME LIKE (:userName)"
						+ " AND a.USER_SURNAME LIKE (:userSurname)"
						+ " AND a.USER_TYPE="+UserTypes.USER_TYPE_MEMBER_INT;
						if(firmId!=0){
							sql+=  " AND a.FIRM_ID=:firmId ";
						}
						
					    sql+= " AND a.FIRM_ID=b.FIRM_ID "
					       +"  AND c.USER_ID=a.USER_ID "
					       + " AND d.PROG_ID=c.PROG_ID "
					       +"  AND c.PROG_ID=:progId "
					       +"  AND c.SALE_STATU <"+SaleStatus.SALE_FINISHED_PLANNED
					       +"  ORDER BY a.USER_NAME,a.USER_SURNAME ";
		
					    
					    
		Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("userName",userName);
		 paramMap.put("userSurname",userSurname);
		 paramMap.put("firmId",firmId);
		 paramMap.put("progId",progId);
				  
		 try{
			
			 List<User> users=findEntityList(sql, paramMap, User.class);
			 return users;
		 }catch (Exception e) {
			 e.printStackTrace();
				return null;
		 }
		
	}
	
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<PacketSaleFactory> findPersonalUserBoughtPackets(long userId) {
		String sql="SELECT a.*,DATE_FORMAT(a.SALES_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') SALES_DATE_STR,c.*,b.*"
		  		+ " ,DATE_FORMAT(b.USER_BIRTHDAY,'"+GlobalUtil.global.getPtDateFormat()+"') USER_BIRTHDAY_STR, 'psp' PROG_TYPE "
					+ " FROM program_personal c ,packet_sale_personal a, user b "
					+ " WHERE a.USER_ID=b.USER_ID "
		            + " AND b.USER_ID=:userId "
		            + " AND a.PROG_ID=c.PROG_ID "
		            + " ORDER BY  a.SALES_DATE  ";
			 
			
			 Map<String, Object> paramMap=new HashMap<String, Object>();
			 paramMap.put("userId",userId);
			
			 int complete=0;
			 
			 try{
				 List<PacketSalePersonal> packetSales=findEntityList(sql, paramMap, PacketSalePersonal.class);
				 
				 for (PacketSalePersonal packetSale : packetSales) {
					PacketPaymentPersonal packetPaymentPersonal= (PacketPaymentPersonal)packetPaymentService.findPacketPaymentPersonal(packetSale.getSaleId());
					//packetSale.setPacketPrice(packetSale.getPacketPrice()*packetSale.getProgCount());
					if(packetPaymentPersonal!=null){
						 if(packetPaymentPersonal.getPayAmount()!=packetSale.getPacketPrice()){
							 complete+=20; 
						 }else{
							 complete+=50;
						 }
						 
						 packetSale.setLeftPrice(packetSale.getPacketPrice()-packetPaymentPersonal.getPayAmount());
					 }else{
						 packetSale.setLeftPrice(packetSale.getPacketPrice());
					 }
					 
					 packetSale.setComplete(complete);
					 packetSale.setPacketPaymentFactory(packetPaymentPersonal);
				 }
				 List<? extends PacketSaleFactory> packetSaleFactories=packetSales;
				 return (List<PacketSaleFactory>)packetSaleFactories;
			 }catch (Exception e) {
				 e.printStackTrace();
					return null;
			 }
	}
	
	@Override
	public synchronized PacketSaleFactory findPersonalPacketSaleById(long saleId) {
		
	  String sql="SELECT a.*,DATE_FORMAT(a.SALES_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') SALES_DATE_STR,c.*,b.*"
	  		+ " ,DATE_FORMAT(b.USER_BIRTHDAY,'"+GlobalUtil.global.getPtDateFormat()+"') USER_BIRTHDAY_STR "
				+ "  FROM program_personal c ,packet_sale_personal a, user b "
				+ " WHERE a.USER_ID=b.USER_ID "
				+ " AND a.SALE_ID=:saleId "
				+ " AND a.PROG_ID=c.PROG_ID  ";
		 
		
		 Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("saleId",saleId);
		    int complete=0;
		 
		 try{
			 PacketSaleFactory packetSaleFactory=findEntityForObject(sql, paramMap, PacketSalePersonal.class);
			 if(packetSaleFactory!=null){
				 PacketPaymentPersonal packetPayment= (PacketPaymentPersonal)packetPaymentService.findPacketPaymentPersonal(packetSaleFactory.getSaleId());
				 if(packetPayment!=null){
					 if(packetPayment.getPayAmount()!=packetSaleFactory.getPacketPrice()){
						 complete+=20; 
					 }else{
						 complete+=50;
					 }
					 packetSaleFactory.setLeftPrice(packetSaleFactory.getPacketPrice()-packetPayment.getPayAmount());
				 }else{
					 packetSaleFactory.setLeftPrice(packetSaleFactory.getPacketPrice());
				 }
				 
				 packetSaleFactory.setComplete(complete);
				 packetSaleFactory.setPacketPaymentFactory(packetPayment);
			}
			return packetSaleFactory;
		 }catch (Exception e) {
			 e.printStackTrace();
				return null;
		 }
	}

	
	@SuppressWarnings("unchecked")
	@Override
	public List<PacketSaleFactory> findPersonalPacketSaleByNameAndDate(String userName, String userSurname, Date salesDate,
			Date salesDateNext) {
		
		String sql="SELECT a.*,DATE_FORMAT(a.SALES_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') SALES_DATE_STR,c.*,b.*"
		  		+ " ,DATE_FORMAT(b.USER_BIRTHDAY,'"+GlobalUtil.global.getPtDateFormat()+"') USER_BIRTHDAY_STR "
					+ "  FROM program_personal c ,packet_sale_personal a, user b "
					+ " WHERE a.USER_ID=b.USER_ID "
		            + " AND b.USER_NAME LIKE :userName "
		            + " AND b.USER_SURNAME LIKE :userSurname "
		            + " AND a.SALES_DATE>=:salesDate "
		            + " AND a.SALES_DATE<=:salesDateNext "
					+ " AND a.PROG_ID=c.PROG_ID  "
					+ " ORDER BY  a.SALES_DATE LIMIT 50 ";
		 
			
			 Map<String, Object> paramMap=new HashMap<String, Object>();
			 paramMap.put("userName",userName);
			 paramMap.put("userSurname",userSurname);
			 paramMap.put("salesDate",salesDate);
			 paramMap.put("salesDateNext",salesDateNext);
			
			 int complete=0;
			 
			 try{
				 List<PacketSalePersonal> packetSales=findEntityList(sql, paramMap, PacketSalePersonal.class);
				 
				 for (PacketSalePersonal packetSale : packetSales) {
					 PacketPaymentPersonal packetPayment= (PacketPaymentPersonal)packetPaymentService.findPacketPaymentPersonal(packetSale.getSaleId());
					 if(packetPayment!=null){
						 if(packetPayment.getPayAmount()!=packetSale.getPacketPrice()){
							 complete+=20; 
						 }else{
							 complete+=50;
						 }
						 
						 packetSale.setLeftPrice(packetSale.getPacketPrice()-packetPayment.getPayAmount());
					 }else{
						 packetSale.setLeftPrice(packetSale.getPacketPrice());
					 }
					 
					 packetSale.setComplete(complete);
					 packetSale.setPacketPaymentFactory(packetPayment);
				 }
				 
				List<? extends PacketSaleFactory> packetSaleFactories=packetSales;
				return (List<PacketSaleFactory>)packetSaleFactories;
			 }catch (Exception e) {
				 e.printStackTrace();
					return null;
			 }
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PacketSaleFactory> findPersonalPacketSaleByName(String userName, String userSurname) {
		String sql="SELECT a.*,DATE_FORMAT(a.SALES_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') SALES_DATE_STR,c.*,b.*"
		  		+ " ,DATE_FORMAT(b.USER_BIRTHDAY,'"+GlobalUtil.global.getPtDateFormat()+"') USER_BIRTHDAY_STR "
					+ " FROM program_personal c ,packet_sale_personal a, user b "
					+ " WHERE a.USER_ID=b.USER_ID "
		            + " AND b.USER_NAME LIKE :userName "
		            + " AND b.USER_SURNAME LIKE :userSurname "
		            + " AND a.PROG_ID=c.PROG_ID "
		            + " ORDER BY  a.SALES_DATE LIMIT 50 ";
			 
			////System.out.println(sql);
			 Map<String, Object> paramMap=new HashMap<String, Object>();
			 paramMap.put("userName",userName);
			 paramMap.put("userSurname",userSurname);
			
			 int complete=0;
			 
			 try{
				 List<PacketSalePersonal> packetSales=findEntityList(sql, paramMap, PacketSalePersonal.class);
				 
				 for (PacketSalePersonal packetSale : packetSales) {
					PacketPaymentPersonal packetPayment= (PacketPaymentPersonal)packetPaymentService.findPacketPaymentPersonal(packetSale.getSaleId());
					 if(packetPayment!=null){
						 if(packetPayment.getPayAmount()!=packetSale.getPacketPrice()){
							 complete+=20; 
						 }else{
							 complete+=50;
						 }
						 packetSale.setLeftPrice(packetSale.getPacketPrice()-packetPayment.getPayAmount());
					 }else{
						 packetSale.setLeftPrice(packetSale.getPacketPrice());
					 }
					 
					 packetSale.setComplete(complete);
					 packetSale.setPacketPaymentFactory(packetPayment);
				 }
				 List<? extends PacketSaleFactory> packetSaleFactories=packetSales;
				 return (List<PacketSaleFactory>)packetSaleFactories;
			 }catch (Exception e) {
				 e.printStackTrace();
					return null;
			 }
	}

	
	
	

	
	
	
	
   /*********************************************************************************************************************/
	
	
	
	@Override
	public  synchronized HmiResultObj buyClassPacket(PacketSaleClass packetSale) {
		HmiResultObj hmiResultObj=null;
		try {
			sqlDao.insertUpdate(packetSale);
			if(packetSale.getSaleId()==0)
				packetSale.setSaleId(Integer.parseInt(getLastSalesClassIdSeq()));
			
			hmiResultObj=new HmiResultObj();
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS);
			hmiResultObj.setResultMessage(""+packetSale.getSaleId());
		} catch (SqlErrorException e) {
			e.printStackTrace();
			hmiResultObj=new HmiResultObj();
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
		}
		return hmiResultObj;
	}
	
	@Override
	public  synchronized HmiResultObj deleteClassPacket(PacketSaleClass packetSale) {
		HmiResultObj hmiResultObj=null;
		try {
			sqlDao.delete(packetSale);
			
			hmiResultObj=new HmiResultObj();
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS);
		} catch (SqlErrorException e) {
			e.printStackTrace();
			hmiResultObj=new HmiResultObj();
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
		}
		return hmiResultObj;
	}
	
	private String getLastSalesClassIdSeq(){
		String sql=	"SELECT CAST(max(SALE_ID) AS CHAR) SALE_ID" +
				" FROM packet_sale_class ";
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
	public List<User> findByUserNameAndSaleProgramForClassWithNoPlan(String userName, String userSurname, long progId,int firmId) {
		
		
		String sql="SELECT a.* ,b.*,c.*,c.PROG_COUNT SALE_COUNT,'sucp' TYPE,d.* "
				+ ",DATE_FORMAT(a.USER_BIRTHDAY,'"+GlobalUtil.global.getPtDateFormat()+"') USER_BIRTHDAY_STR "
				+ ",DATE_FORMAT(a.CREATE_TIME,'"+GlobalUtil.global.getPtDateFormat()+"') CREATE_TIME_STR "
				+ " FROM user a ,def_firm b,packet_sale_class c,program_class d "
				+ " WHERE a.USER_NAME LIKE (:userName)"
				+ " AND a.USER_SURNAME LIKE (:userSurname)"
				+ " AND a.USER_TYPE="+UserTypes.USER_TYPE_MEMBER_INT;
				if(firmId!=0){
					sql+=  " AND a.FIRM_ID=:firmId ";
				}
				
			    sql+= " AND a.FIRM_ID=b.FIRM_ID "
			       + " AND c.USER_ID=a.USER_ID "
			       + " AND d.PROG_ID=c.PROG_ID "
			       + " AND c.PROG_ID=:progId "
			       + " AND c.SALE_STATU ="+SaleStatus.SALE_NO_PLANNED
			       +"  ORDER BY a.USER_NAME,a.USER_SURNAME ";
		 
			    
		Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("userName",userName);
		 paramMap.put("userSurname",userSurname);
		 paramMap.put("firmId",firmId);
		 paramMap.put("progId",progId);
				  
		 try{
			
			 List<User> users=findEntityList(sql, paramMap, User.class);
			 return users;
		 }catch (Exception e) {
			 e.printStackTrace();
				return null;
		 }
		
	}
	
	@Override
	public List<User> findByUserNameAndSaleProgramForClassWithPlan(String userName, String userSurname, long progId,int firmId) {
		
		
		String sql="SELECT a.* ,b.*,c.*,c.PROG_COUNT SALE_COUNT,'sucp' TYPE,d.* "
				+ ",DATE_FORMAT(a.USER_BIRTHDAY,'"+GlobalUtil.global.getPtDateFormat()+"') USER_BIRTHDAY_STR "
				+ ",DATE_FORMAT(a.CREATE_TIME,'"+GlobalUtil.global.getPtDateFormat()+"') CREATE_TIME_STR "
				+ " FROM user a ,def_firm b,packet_sale_class c,program_class d "
				+ " WHERE a.USER_NAME LIKE (:userName)"
				+ " AND a.USER_SURNAME LIKE (:userSurname)"
				+ " AND a.USER_TYPE="+UserTypes.USER_TYPE_MEMBER_INT;
				if(firmId!=0){
					sql+=  " AND a.FIRM_ID=:firmId ";
				}
				
			    sql+= " AND a.FIRM_ID=b.FIRM_ID "
			       + " AND c.USER_ID=a.USER_ID "
			       + " AND d.PROG_ID=c.PROG_ID "
			       + " AND c.PROG_ID=:progId "
			       + " AND c.SALE_STATU <"+SaleStatus.SALE_FINISHED_PLANNED
			       +"  ORDER BY a.USER_NAME,a.USER_SURNAME ";
		 
			    
		Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("userName",userName);
		 paramMap.put("userSurname",userSurname);
		 paramMap.put("firmId",firmId);
		 paramMap.put("progId",progId);
				  
		 try{
			
			 List<User> users=findEntityList(sql, paramMap, User.class);
			 return users;
		 }catch (Exception e) {
			 e.printStackTrace();
				return null;
		 }
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PacketSaleFactory> findClassUserBoughtPackets(long userId) {
		String sql="SELECT a.*,DATE_FORMAT(a.SALES_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') SALES_DATE_STR,c.*,b.*"
		  		+ " ,DATE_FORMAT(b.USER_BIRTHDAY,'"+GlobalUtil.global.getPtDateFormat()+"') USER_BIRTHDAY_STR, 'psc' PROG_TYPE "
					+ " FROM program_class c ,packet_sale_class a, user b "
					+ " WHERE a.USER_ID=b.USER_ID "
		            + " AND b.USER_ID=:userId "
		            + " AND a.PROG_ID=c.PROG_ID "
		            + " ORDER BY  a.SALES_DATE  ";
			 
			
			 Map<String, Object> paramMap=new HashMap<String, Object>();
			 paramMap.put("userId",userId);
			
			 int complete=0;
			 
			 try{
				 List<PacketSaleClass> packetSales=findEntityList(sql, paramMap, PacketSaleClass.class);
				 
				 for (PacketSaleClass packetSale : packetSales) {
					// packetSale.setPacketPrice(packetSale.getPacketPrice()*packetSale.getProgCount());
						
					 PacketPaymentClass packetPaymentClass= (PacketPaymentClass)packetPaymentService.findPacketPaymentClass(packetSale.getSaleId());
					 if(packetPaymentClass!=null){
						 if(packetPaymentClass.getPayAmount()!=packetSale.getPacketPrice()){
							 complete+=20; 
						 }else{
							 complete+=50;
						 }
						 packetSale.setLeftPrice(packetSale.getPacketPrice()-packetPaymentClass.getPayAmount());
					 }else{
						 packetSale.setLeftPrice(packetSale.getPacketPrice());
					 }
					 
					 packetSale.setComplete(complete);
					 packetSale.setPacketPaymentFactory(packetPaymentClass);
				 }
				 List<? extends PacketSaleFactory> packetSaleFactories=packetSales;
				 return (List<PacketSaleFactory>)packetSaleFactories;
			 }catch (Exception e) {
				 e.printStackTrace();
					return null;
			 }
	}
	
	@Override
	public synchronized PacketSaleFactory findClassPacketSaleById(long saleId) {
		
	  String sql="SELECT a.*,DATE_FORMAT(a.SALES_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') SALES_DATE_STR,c.*,b.*"
	  		+ " ,DATE_FORMAT(b.USER_BIRTHDAY,'"+GlobalUtil.global.getPtDateFormat()+"') USER_BIRTHDAY_STR "
				+ "  FROM program_class c ,packet_sale_class a, user b "
				+ " WHERE a.USER_ID=b.USER_ID "
				+ " AND a.SALE_ID=:saleId "
				+ " AND a.PROG_ID=c.PROG_ID  ";
		 
		
		 Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("saleId",saleId);
		    int complete=0;
		 
		 try{
			 PacketSaleFactory packetSaleFactory=findEntityForObject(sql, paramMap, PacketSaleClass.class);
			 if(packetSaleFactory!=null){
				 PacketPaymentClass packetPayment= (PacketPaymentClass)packetPaymentService.findPacketPaymentClass(packetSaleFactory.getSaleId());
				 if(packetPayment!=null){
					 if(packetPayment.getPayAmount()!=packetSaleFactory.getPacketPrice()){
						 complete+=20; 
					 }else{
						 complete+=50;
					 }
					 packetSaleFactory.setLeftPrice(packetSaleFactory.getPacketPrice()-packetPayment.getPayAmount());
				 }else{
					 packetSaleFactory.setLeftPrice(packetSaleFactory.getPacketPrice());
				 }
				 
				 packetSaleFactory.setComplete(complete);
				 packetSaleFactory.setPacketPaymentFactory(packetPayment);
			 }
			return packetSaleFactory;
		 }catch (Exception e) {
			 e.printStackTrace();
				return null;
		 }
	}

	
	@SuppressWarnings("unchecked")
	@Override
	public List<PacketSaleFactory> findClassPacketSaleByNameAndDate(String userName, String userSurname, Date salesDate,
			Date salesDateNext) {
		
		String sql="SELECT a.*,DATE_FORMAT(a.SALES_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') SALES_DATE_STR,c.*,b.*"
		  		+ " ,DATE_FORMAT(b.USER_BIRTHDAY,'"+GlobalUtil.global.getPtDateFormat()+"') USER_BIRTHDAY_STR "
					+ "  FROM program_class c ,packet_sale_class a, user b "
					+ " WHERE a.USER_ID=b.USER_ID "
		            + " AND b.USER_NAME LIKE :userName "
		            + " AND b.USER_SURNAME LIKE :userSurname "
		            + " AND a.SALES_DATE>=:salesDate "
		            + " AND a.SALES_DATE<=:salesDateNext "
					+ " AND a.PROG_ID=c.PROG_ID  "
					+ " ORDER BY  a.SALES_DATE LIMIT 50 ";
		 
			
			 Map<String, Object> paramMap=new HashMap<String, Object>();
			 paramMap.put("userName",userName);
			 paramMap.put("userSurname",userSurname);
			 paramMap.put("salesDate",salesDate);
			 paramMap.put("salesDateNext",salesDateNext);
			
			 int complete=0;
			 
			 try{
				 List<PacketSaleClass> packetSales=findEntityList(sql, paramMap, PacketSaleClass.class);
				 
				 for (PacketSaleClass packetSale : packetSales) {
					 PacketPaymentClass packetPayment= (PacketPaymentClass)packetPaymentService.findPacketPaymentClass(packetSale.getSaleId());
					 if(packetPayment!=null){
						 if(packetPayment.getPayAmount()!=packetSale.getPacketPrice()){
							 complete+=20; 
						 }else{
							 complete+=50;
						 }
						 packetSale.setLeftPrice(packetSale.getPacketPrice()-packetPayment.getPayAmount());
					 }else{
						 packetSale.setLeftPrice(packetSale.getPacketPrice());
					 }
					 
					 packetSale.setComplete(complete);
					 packetSale.setPacketPaymentFactory(packetPayment);
				 }
				 
				List<? extends PacketSaleFactory> packetSaleFactories=packetSales;
				return (List<PacketSaleFactory>)packetSaleFactories;
			 }catch (Exception e) {
				 e.printStackTrace();
					return null;
			 }
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PacketSaleFactory> findClassPacketSaleByName(String userName, String userSurname) {
		String sql="SELECT a.*,DATE_FORMAT(a.SALES_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') SALES_DATE_STR,c.*,b.*"
		  		+ " ,DATE_FORMAT(b.USER_BIRTHDAY,'"+GlobalUtil.global.getPtDateFormat()+"') USER_BIRTHDAY_STR "
					+ " FROM program_class c ,packet_sale_class a, user b "
					+ " WHERE a.USER_ID=b.USER_ID "
		            + " AND b.USER_NAME LIKE :userName "
		            + " AND b.USER_SURNAME LIKE :userSurname "
		            + " AND a.PROG_ID=c.PROG_ID "
		            + " ORDER BY  a.SALES_DATE LIMIT 50 ";
			 
			
			 Map<String, Object> paramMap=new HashMap<String, Object>();
			 paramMap.put("userName",userName);
			 paramMap.put("userSurname",userSurname);
			
			 int complete=0;
			 
			 try{
				 List<PacketSaleClass> packetSales=findEntityList(sql, paramMap, PacketSaleClass.class);
				 
				 for (PacketSaleClass packetSale : packetSales) {
					PacketPaymentClass packetPayment= (PacketPaymentClass)packetPaymentService.findPacketPaymentClass(packetSale.getSaleId());
					 if(packetPayment!=null){
						 if(packetPayment.getPayAmount()!=packetSale.getPacketPrice()){
							 complete+=20; 
						 }else{
							 complete+=50;
						 }
						 packetSale.setLeftPrice(packetSale.getPacketPrice()-packetPayment.getPayAmount());
					 }else{
						 packetSale.setLeftPrice(packetSale.getPacketPrice());
					 }
					 
					 packetSale.setComplete(complete);
					 packetSale.setPacketPaymentFactory(packetPayment);
				 }
				 List<? extends PacketSaleFactory> packetSaleFactories=packetSales;
				 return (List<PacketSaleFactory>)packetSaleFactories;
			 }catch (Exception e) {
				 e.printStackTrace();
					return null;
			 }
	}

	
	
	

	
	
	
	
	/******************************************************************************************************/
	
	
	@Override
	public  synchronized HmiResultObj buyMembershipPacket(PacketSaleMembership packetSale) {
		HmiResultObj hmiResultObj=null;
		try {
			sqlDao.insertUpdate(packetSale);
			if(packetSale.getSaleId()==0)
				packetSale.setSaleId(Integer.parseInt(getLastSalesMembershipIdSeq()));
			
			hmiResultObj=new HmiResultObj();
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS);
			hmiResultObj.setResultMessage(""+packetSale.getSaleId());
		} catch (SqlErrorException e) {
			e.printStackTrace();
			hmiResultObj=new HmiResultObj();
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
		}
		return hmiResultObj;
	}
	
	@Override
	public  synchronized HmiResultObj deleteMembershipPacket(PacketSaleMembership packetSale) {
		HmiResultObj hmiResultObj=null;
		try {
			sqlDao.delete(packetSale);
			
			hmiResultObj=new HmiResultObj();
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS);
		} catch (SqlErrorException e) {
			e.printStackTrace();
			hmiResultObj=new HmiResultObj();
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
		}
		return hmiResultObj;
	}
	
	private String getLastSalesMembershipIdSeq(){
		String sql=	"SELECT CAST(max(SALE_ID) AS CHAR) SALE_ID" +
				" FROM packet_sale_membership ";
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

	@SuppressWarnings("unchecked")
	@Override
	public List<PacketSaleFactory> findMembershipUserBoughtPackets(long userId) {
		/*String sql="SELECT a.*,DATE_FORMAT(a.SALES_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') SALES_DATE_STR,c.*,b.*"
		  		+ " ,DATE_FORMAT(b.USER_BIRTHDAY,'"+GlobalUtil.global.getPtDateFormat()+"') USER_BIRTHDAY_STR, 'psm' PROG_TYPE "
					+ " FROM program_membership c ,packet_sale_membership a, user b "
					+ " WHERE a.USER_ID=b.USER_ID "
		            + " AND b.USER_ID=:userId "
		            + " AND a.PROG_ID=c.PROG_ID "
		            + " ORDER BY  a.SALES_DATE  ";
			 */
			
		String sql="SELECT a.*,DATE_FORMAT(a.SALES_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') SALES_DATE_STR,c.*,b.*,d.*"
		  		+ " ,DATE_FORMAT(b.USER_BIRTHDAY,'"+GlobalUtil.global.getPtDateFormat()+"') USER_BIRTHDAY_STR "
		  		+ ",DATE_FORMAT(d.SMP_START_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') SMP_START_DATE_STR"
	  			+ ",DATE_FORMAT(d.SMP_END_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') SMP_END_DATE_STR "
				+ " FROM program_membership c ,packet_sale_membership a, user b,schedule_membership_plan d "
					+ " WHERE a.USER_ID=b.USER_ID "
		            + " AND a.PROG_ID=c.PROG_ID "
		            + " AND b.USER_ID=:userId "
		            + " AND d.SALE_ID=a.SALE_ID";
		
		
		
			 Map<String, Object> paramMap=new HashMap<String, Object>();
			 paramMap.put("userId",userId);
			
			 int complete=0;
			 
			 try{
				 List<PacketSaleMembership> packetSales=findEntityList(sql, paramMap, PacketSaleMembership.class);
				 
				 for (PacketSaleMembership packetSale : packetSales) {
					PacketPaymentMembership packetPaymentMembership= (PacketPaymentMembership)packetPaymentService.findPacketPaymentMembership(packetSale.getSaleId());
					 if(packetPaymentMembership!=null){
						 if(packetPaymentMembership.getPayAmount()!=packetSale.getPacketPrice()){
							 complete+=20; 
						 }else{
							 complete+=50;
						 }
						 packetSale.setLeftPrice(packetSale.getPacketPrice()-packetPaymentMembership.getPayAmount());
					 }else{
						 packetSale.setLeftPrice(packetSale.getPacketPrice());
					 }
					 
					 packetSale.setComplete(complete);
					 packetSale.setPacketPaymentFactory(packetPaymentMembership);
				 }
				 List<? extends PacketSaleFactory> packetSaleFactories=packetSales;
				 return (List<PacketSaleFactory>)packetSaleFactories;
			 }catch (Exception e) {
				 e.printStackTrace();
					return null;
			 }
	}
	
	@Override
	public synchronized PacketSaleFactory findMembershipPacketSaleById(long saleId) {
		
	  String sql="SELECT a.*,DATE_FORMAT(a.SALES_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') SALES_DATE_STR,c.*,b.*,d.*"
	  		+ " ,DATE_FORMAT(b.USER_BIRTHDAY,'"+GlobalUtil.global.getPtDateFormat()+"') USER_BIRTHDAY_STR"
	  			+ ",DATE_FORMAT(d.SMP_START_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') SMP_START_DATE_STR"
	  			+ ",DATE_FORMAT(d.SMP_END_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') SMP_END_DATE_STR "
				+ "  FROM program_membership c ,packet_sale_membership a, user b,schedule_membership_plan d "
				+ " WHERE a.USER_ID=b.USER_ID "
				+ " AND a.SALE_ID=:saleId "
				+ " AND a.PROG_ID=c.PROG_ID "
				+ " AND d.SALE_ID=a.SALE_ID ";
		 
		
		 Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("saleId",saleId);
		    int complete=0;
		 
		 try{
			 PacketSaleFactory packetSaleFactory=findEntityForObject(sql, paramMap, PacketSaleMembership.class);
			 
			 
			 
			 if(packetSaleFactory!=null){
				 PacketPaymentMembership packetPayment= (PacketPaymentMembership)packetPaymentService.findPacketPaymentMembership(packetSaleFactory.getSaleId());
				 if(packetPayment!=null){
					 if(packetPayment.getPayAmount()!=packetSaleFactory.getPacketPrice()){
						 complete+=20; 
					 }else{
						 complete+=50;
					 }
					 packetSaleFactory.setLeftPrice(packetSaleFactory.getPacketPrice()-packetPayment.getPayAmount());
				 }else{
					 packetSaleFactory.setLeftPrice(packetSaleFactory.getPacketPrice());
				 }
				 
				 packetSaleFactory.setComplete(complete);
				 packetSaleFactory.setPacketPaymentFactory(packetPayment);
			 }
			return packetSaleFactory;
		 }catch (Exception e) {
			 e.printStackTrace();
				return null;
		 }
	}

	
	@SuppressWarnings("unchecked")
	@Override
	public List<PacketSaleFactory> findMembershipPacketSaleByNameAndDate(String userName, String userSurname, Date salesDate,
			Date salesDateNext) {
		
		String sql="SELECT a.*,DATE_FORMAT(a.SALES_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') SALES_DATE_STR,c.*,b.*,d.*"
		  		+ " ,DATE_FORMAT(b.USER_BIRTHDAY,'"+GlobalUtil.global.getPtDateFormat()+"') USER_BIRTHDAY_STR "
		  		+ ",DATE_FORMAT(d.SMP_START_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') SMP_START_DATE_STR"
	  			+ ",DATE_FORMAT(d.SMP_END_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') SMP_END_DATE_STR "
					+ "  FROM program_membership c ,packet_sale_membership a, user b,schedule_membership_plan d "
					+ " WHERE a.USER_ID=b.USER_ID "
		            + " AND b.USER_NAME LIKE :userName "
		            + " AND b.USER_SURNAME LIKE :userSurname "
		            + " AND a.SALES_DATE>=:salesDate "
		            + " AND a.SALES_DATE<=:salesDateNext "
					+ " AND a.PROG_ID=c.PROG_ID "
					+ " AND d.SALE_ID=a.SALE_ID "
					+ " ORDER BY  a.SALES_DATE LIMIT 50 ";
		 
			
			 Map<String, Object> paramMap=new HashMap<String, Object>();
			 paramMap.put("userName",userName);
			 paramMap.put("userSurname",userSurname);
			 paramMap.put("salesDate",salesDate);
			 paramMap.put("salesDateNext",salesDateNext);
			
			 int complete=0;
			 
			 try{
				 List<PacketSaleMembership> packetSales=findEntityList(sql, paramMap, PacketSaleMembership.class);
				 
				 for (PacketSaleMembership packetSale : packetSales) {
					 PacketPaymentMembership packetPayment= (PacketPaymentMembership)packetPaymentService.findPacketPaymentMembership(packetSale.getSaleId());
					 if(packetPayment!=null){
						 if(packetPayment.getPayAmount()!=packetSale.getPacketPrice()){
							 complete+=20; 
						 }else{
							 complete+=50;
						 }
						 packetSale.setLeftPrice(packetSale.getPacketPrice()-packetPayment.getPayAmount());
					 }else{
						 packetSale.setLeftPrice(packetSale.getPacketPrice());
					 }
					 
					 packetSale.setComplete(complete);
					 packetSale.setPacketPaymentFactory(packetPayment);
				 }
				 
				List<? extends PacketSaleFactory> packetSaleFactories=packetSales;
				return (List<PacketSaleFactory>)packetSaleFactories;
			 }catch (Exception e) {
				 e.printStackTrace();
					return null;
			 }
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PacketSaleFactory> findMembershipPacketSaleByName(String userName, String userSurname) {
		String sql="SELECT a.*,DATE_FORMAT(a.SALES_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') SALES_DATE_STR,c.*,b.*,d.*"
		  		+ " ,DATE_FORMAT(b.USER_BIRTHDAY,'"+GlobalUtil.global.getPtDateFormat()+"') USER_BIRTHDAY_STR "
		  		+ ",DATE_FORMAT(d.SMP_START_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') SMP_START_DATE_STR"
	  			+ ",DATE_FORMAT(d.SMP_END_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') SMP_END_DATE_STR "
				+ " FROM program_membership c ,packet_sale_membership a, user b,schedule_membership_plan d "
					+ " WHERE a.USER_ID=b.USER_ID "
		            + " AND b.USER_NAME LIKE :userName "
		            + " AND b.USER_SURNAME LIKE :userSurname "
		            + " AND a.PROG_ID=c.PROG_ID "
		            + " AND d.SALE_ID=a.SALE_ID"
		            + " ORDER BY  a.SALES_DATE LIMIT 50 ";
		
		
		
		
		
		
		
			
			 Map<String, Object> paramMap=new HashMap<String, Object>();
			 paramMap.put("userName",userName);
			 paramMap.put("userSurname",userSurname);
			
			 int complete=0;
			 
			 try{
				 List<PacketSaleMembership> packetSales=findEntityList(sql, paramMap, PacketSaleMembership.class);
				 
				 for (PacketSaleMembership packetSale : packetSales) {
					PacketPaymentMembership packetPayment= (PacketPaymentMembership)packetPaymentService.findPacketPaymentMembership(packetSale.getSaleId());
					 if(packetPayment!=null){
						 if(packetPayment.getPayAmount()!=packetSale.getPacketPrice()){
							 complete+=20; 
						 }else{
							 complete+=50;
						 }
						 packetSale.setLeftPrice(packetSale.getPacketPrice()-packetPayment.getPayAmount());
					 }else{
						 packetSale.setLeftPrice(packetSale.getPacketPrice());
					 }
					 
					 packetSale.setComplete(complete);
					 packetSale.setPacketPaymentFactory(packetPayment);
				 }
				 List<? extends PacketSaleFactory> packetSaleFactories=packetSales;
				 return (List<PacketSaleFactory>)packetSaleFactories;
			 }catch (Exception e) {
				 e.printStackTrace();
					return null;
			 }
	}

	@Override
	public List<User> findByUsersAndSaledProgramForPersonal(long schId) {
		String sql="SELECT a.USER_ID,"
				+ "a.USER_NAME,"
				+ "a.USER_SURNAME,"
				+ "a.USER_TYPE,"
				+ "a.PROFILE_URL,"
				+ "a.USER_EMAIL,"
				+ "a.USER_GSM,"
				+ "d.SALE_ID,"
				+ "a.USER_COMMENT, "
				+ "d.PROG_COUNT SALE_COUNT, "
				+ "d.PROG_ID, "
				+ "'supp' TYPE ,"
				+ "a.URL_TYPE,"
				+ "e.PROG_NAME"
				+ " FROM user a ,schedule_users_personal_plan b,schedule_time_plan c,packet_sale_personal d,program_personal e "
				+ " WHERE c.SCH_ID=:schId"
				+ " AND b.SCHT_ID=c.SCHT_ID "
				+ " AND b.SALE_ID=d.SALE_ID "
				+ " AND e.PROG_ID=d.PROG_ID "
			    + " AND b.USER_ID=a.USER_ID "
				+ " AND d.USER_ID=a.USER_ID "
				+ " GROUP BY 	a.USER_NAME,"
							+ "	a.USER_SURNAME,"
							+ "	a.USER_TYPE,"
							+ "	a.PROFILE_URL,"
							+ "	a.USER_EMAIL,"
							+ "	a.USER_GSM,"
							+ "	d.SALE_ID,"
							+ " a.USER_COMMENT,"
							+ " d.PROG_COUNT,"
							+ " d.PROG_ID,"
							+ " 'supp',"
							+ " a.URL_TYPE,"
							+ " e.PROG_NAME";
			   
			    
		 
		
		
		
		Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("schId",schId);
			  
		 try{
			
			 List<User> users=findEntityList(sql, paramMap, User.class);
			 return users;
		 }catch (Exception e) {
			 e.printStackTrace();
				return null;
		 }
	}

	@Override
	public List<User> findByUsersAndSaledProgramForClass(long schId) {
		String sql="SELECT a.USER_ID,"
				+ "a.USER_NAME,"
				+ "a.USER_SURNAME,"
				+ "a.USER_TYPE,"
				+ "a.PROFILE_URL,"
				+ "a.USER_EMAIL,"
				+ "a.USER_GSM,"
				+ "d.SALE_ID, "
				+ "a.USER_COMMENT,"
				+ "d.PROG_COUNT SALE_COUNT,"
				+ "d.PROG_ID, "
				+ "'sucp' TYPE,"
				+ "a.URL_TYPE,"
				+ "e.PROG_NAME "
				+ " FROM user a ,schedule_users_class_plan b,schedule_time_plan c,packet_sale_class d,program_class e "
				+ " WHERE c.SCH_ID=:schId"
				+ " AND b.SCHT_ID=c.SCHT_ID "
				+ " AND b.SALE_ID=d.SALE_ID "
				+ " AND e.PROG_ID=d.PROG_ID "
			    + " AND b.USER_ID=a.USER_ID "
				+ " AND d.USER_ID=a.USER_ID "
				+ " GROUP BY 	a.USER_NAME,"
							+ "	a.USER_SURNAME,"
							+ "	a.USER_TYPE,"
							+ "	a.PROFILE_URL,"
							+ "	a.USER_EMAIL,"
							+ "	a.USER_GSM,"
							+ "	d.SALE_ID,"
							+ " a.USER_COMMENT,"
							+ " d.PROG_COUNT,"
							+ " d.PROG_ID, "
							+ " 'sucp',"
							+ " a.URL_TYPE,"
							+ " e.PROG_NAME";
			   
			    
		 
		Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("schId",schId);
			  
		 try{
			
			 List<User> users=findEntityList(sql, paramMap, User.class);
			 return users;
		 }catch (Exception e) {
			 e.printStackTrace();
				return new ArrayList<User>();
		 }
	}

	@Override
	public User findUserBySaleIdForPersonal(long saleId) {
		 String sql="SELECT a.*,DATE_FORMAT(a.SALES_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') SALES_DATE_STR,c.*,b.*,a.PROG_COUNT SALE_COUNT "
			  		+ " ,DATE_FORMAT(b.USER_BIRTHDAY,'"+GlobalUtil.global.getPtDateFormat()+"') USER_BIRTHDAY_STR ,'supp' TYPE "
						+ "  FROM program_personal c ,packet_sale_personal a, user b "
						+ " WHERE a.USER_ID=b.USER_ID "
						+ " AND a.SALE_ID=:saleId "
						+ " AND a.PROG_ID=c.PROG_ID  ";
				 
				
				 Map<String, Object> paramMap=new HashMap<String, Object>();
				 paramMap.put("saleId",saleId);
				 
				 try{
					 User user=findEntityForObject(sql, paramMap, User.class);
					
					return user;
				 }catch (Exception e) {
					 e.printStackTrace();
						return null;
				 }
	}

	@Override
	public User findUserBySaleIdForClass(long saleId) {
		String sql="SELECT a.*,DATE_FORMAT(a.SALES_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') SALES_DATE_STR,c.*,b.*,a.PROG_COUNT SALE_COUNT "
		  		+ " ,DATE_FORMAT(b.USER_BIRTHDAY,'"+GlobalUtil.global.getPtDateFormat()+"') USER_BIRTHDAY_STR,'sucp' TYPE  "
					+ "  FROM program_class c ,packet_sale_class a, user b "
					+ " WHERE a.USER_ID=b.USER_ID "
					+ " AND a.SALE_ID=:saleId "
					+ " AND a.PROG_ID=c.PROG_ID  ";
			 
			
			 Map<String, Object> paramMap=new HashMap<String, Object>();
			 paramMap.put("saleId",saleId);
			 
			 try{
				 User user=findEntityForObject(sql, paramMap, User.class);
				
				return user;
			 }catch (Exception e) {
				 e.printStackTrace();
					return null;
			 }
	}

	@Override
	public User findUserBySaleIdForMembership(long saleId) {
		String sql="SELECT a.*,DATE_FORMAT(a.SALES_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') SALES_DATE_STR,c.*,b.*"
		  		+ " ,DATE_FORMAT(b.USER_BIRTHDAY,'"+GlobalUtil.global.getPtDateFormat()+"') USER_BIRTHDAY_STR,'smp' TYPE  "
					+ "  FROM program_membership c ,packet_sale_membership a, user b "
					+ " WHERE a.USER_ID=b.USER_ID "
					+ " AND a.SALE_ID=:saleId "
					+ " AND a.PROG_ID=c.PROG_ID  ";
			 
			
			 Map<String, Object> paramMap=new HashMap<String, Object>();
			 paramMap.put("saleId",saleId);
			 
			 try{
				 User user=findEntityForObject(sql, paramMap, User.class);
				
				return user;
			 }catch (Exception e) {
				 e.printStackTrace();
					return null;
			 }
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PacketSaleFactory> findUserPacketSaleBySchIdForPersonal(long schId) {
			String sql="SELECT a.*,'psp' PROG_TYPE "
			  		+ " FROM packet_sale_personal a "
						+ " WHERE a.SALE_ID IN (SELECT b.SALE_ID FROM schedule_users_personal_plan b,schedule_time_plan c "
						+ "                                WHERE b.SCHT_ID=c.SCHT_ID"
						+ "                                    AND c.SCH_ID=:schId "
						+ "                                   GROUP BY b.SALE_ID)  ";
				 
				
				 Map<String, Object> paramMap=new HashMap<String, Object>();
				 paramMap.put("schId",schId);
				
				 
				 try{
					 List<PacketSalePersonal> packetSales=findEntityList(sql, paramMap, PacketSalePersonal.class);
					 List<? extends PacketSaleFactory> packetSaleFactories=packetSales;
					 return (List<PacketSaleFactory>)packetSaleFactories;
				 }catch (Exception e) {
					 e.printStackTrace();
						return null;
				 }
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PacketSaleFactory> findUserPacketSaleBySchIdForClass(long schId) {
		String sql="SELECT a.*,'psc' PROG_TYPE  "
		  		+ " FROM packet_sale_class a "
					+ " WHERE a.SALE_ID IN (SELECT b.SALE_ID FROM schedule_users_class_plan b,schedule_time_plan c "
					+ "                                WHERE b.SCHT_ID=c.SCHT_ID"
					+ "                                    AND c.SCH_ID=:schId "
					+ "                                   GROUP BY b.SALE_ID)  ";
			 
			
			 Map<String, Object> paramMap=new HashMap<String, Object>();
			 paramMap.put("schId",schId);
			
			 
			 try{
				 List<PacketSaleClass> packetSales=findEntityList(sql, paramMap, PacketSaleClass.class);
				 List<? extends PacketSaleFactory> packetSaleFactories=packetSales;
				 return (List<PacketSaleFactory>)packetSaleFactories;
			 }catch (Exception e) {
				 e.printStackTrace();
					return null;
			 }
	}

	@Override
	public HmiResultObj updateSalePacketForPersonal(PacketSaleFactory packetSaleFactory) {
		HmiResultObj hmiResultObj=null;
		try {
			sqlDao.insertUpdate((PacketSalePersonal)packetSaleFactory);
			if(packetSaleFactory.getSaleId()==0)
				packetSaleFactory.setSaleId(Integer.parseInt(getLastSalesPersonalIdSeq()));
			
			hmiResultObj=new HmiResultObj();
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS);
			hmiResultObj.setResultMessage(""+packetSaleFactory.getSaleId());
		} catch (SqlErrorException e) {
			e.printStackTrace();
			hmiResultObj=new HmiResultObj();
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
		}
		return hmiResultObj;
	}

	@Override
	public HmiResultObj updateSalePacketForClass(PacketSaleFactory packetSaleFactory) {
		HmiResultObj hmiResultObj=null;
		try {
			sqlDao.insertUpdate((PacketSaleClass)packetSaleFactory);
			if(packetSaleFactory.getSaleId()==0)
				packetSaleFactory.setSaleId(Integer.parseInt(getLastSalesPersonalIdSeq()));
			
			hmiResultObj=new HmiResultObj();
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS);
			hmiResultObj.setResultMessage(""+packetSaleFactory.getSaleId());
		} catch (SqlErrorException e) {
			e.printStackTrace();
			hmiResultObj=new HmiResultObj();
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
		}
		return hmiResultObj;
	}

	@Override
	public List<User> findByUserNameForSalesForPersonal(String userName, String userSurname, int firmId) {
		String sql="SELECT a.* ,b.*,c.*,c.PROG_COUNT SALE_COUNT,'supp' TYPE,d.* "
				+ ",DATE_FORMAT(a.USER_BIRTHDAY,'"+GlobalUtil.global.getPtDateFormat()+"') USER_BIRTHDAY_STR "
				+ ",DATE_FORMAT(a.CREATE_TIME,'"+GlobalUtil.global.getPtDateFormat()+"') CREATE_TIME_STR "
				+ " FROM user a ,def_firm b,packet_sale_personal c,program_personal d "
				+ " WHERE a.USER_NAME LIKE (:userName)"
				+ " AND a.USER_SURNAME LIKE (:userSurname)"
				+ " AND a.USER_TYPE="+UserTypes.USER_TYPE_MEMBER_INT;
				if(firmId!=0){
					sql+=  " AND a.FIRM_ID=:firmId ";
				}
				
			    sql+= " AND a.FIRM_ID=b.FIRM_ID "
			       + " AND c.USER_ID=a.USER_ID "
			       + " AND d.PROG_ID=c.PROG_ID "
			       + " AND c.SALE_STATU <"+SaleStatus.SALE_FINISHED_PLANNED
			       +"  ORDER BY a.USER_NAME,a.USER_SURNAME ";
		 
			
		 Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("userName",userName);
		 paramMap.put("userSurname",userSurname);
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
	public List<User> findByUserNameForSalesForClass(String userName, String userSurname, int firmId) {
		String sql="SELECT a.* ,b.*,c.*,c.PROG_COUNT SALE_COUNT,'sucp' TYPE,d.* "
				+ ",DATE_FORMAT(a.USER_BIRTHDAY,'"+GlobalUtil.global.getPtDateFormat()+"') USER_BIRTHDAY_STR "
				+ ",DATE_FORMAT(a.CREATE_TIME,'"+GlobalUtil.global.getPtDateFormat()+"') CREATE_TIME_STR "
				+ " FROM user a ,def_firm b,packet_sale_class c,program_class d "
				+ " WHERE a.USER_NAME LIKE (:userName)"
				+ " AND a.USER_SURNAME LIKE (:userSurname)"
				+ " AND a.USER_TYPE="+UserTypes.USER_TYPE_MEMBER_INT;
				if(firmId!=0){
					sql+=  " AND a.FIRM_ID=:firmId ";
				}
				
			    sql+= " AND a.FIRM_ID=b.FIRM_ID "
			       + " AND c.USER_ID=a.USER_ID "
			       + " AND d.PROG_ID=c.PROG_ID "
			       + " AND c.SALE_STATU <"+SaleStatus.SALE_FINISHED_PLANNED
			       +"  ORDER BY a.USER_NAME,a.USER_SURNAME ";
		 
			    
		Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("userName",userName);
		 paramMap.put("userSurname",userSurname);
		 paramMap.put("firmId",firmId);
				  
		 try{
			
			 List<User> users=findEntityList(sql, paramMap, User.class);
			 return users;
		 }catch (Exception e) {
			 e.printStackTrace();
				return null;
		 }
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PacketSaleFactory> findLast10UserPacketSaleForPersonal(int firmId) {
		String sql="SELECT a.* ,b.*,c.*,'psp' PROG_TYPE,d.*,IFNULL(e.PAY_ID,0) PAY_ID "
				+ ",DATE_FORMAT(c.SALES_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') SALES_DATE_STR "
				+ ",DATE_FORMAT(c.CHANGE_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') CHANGE_DATE_STR "
				+ " FROM user a ,def_firm b,program_personal d,packet_sale_personal c left join packet_payment_personal e ON e.SALE_ID=c.SALE_ID "
				+ " WHERE a.FIRM_ID=:firmId "
				+ " AND a.FIRM_ID=b.FIRM_ID "
			       + " AND c.USER_ID=a.USER_ID "
			       + " AND d.PROG_ID=c.PROG_ID "
			       + " ORDER BY c.SALES_DATE DESC LIMIT 5 ";
		 Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("firmId",firmId);
				  
		 try{
			
			 List<? extends PacketSaleFactory> packetSalePersonals=findEntityList(sql, paramMap, PacketSalePersonal.class);
			 return (List<PacketSaleFactory>)packetSalePersonals;
		 }catch (Exception e) {
			 e.printStackTrace();
				return null;
		 }
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PacketSaleFactory> findLast10UserPacketSaleForClass(int firmId) {
		String sql="SELECT a.* ,b.*,c.*,'psc' PROG_TYPE,d.*,IFNULL(e.PAY_ID,0) PAY_ID "
				+ ",DATE_FORMAT(c.SALES_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') SALES_DATE_STR "
				+ ",DATE_FORMAT(c.CHANGE_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') CHANGE_DATE_STR "
				+ " FROM user a ,def_firm b,program_class d,packet_sale_class c left join packet_payment_class e ON e.SALE_ID=c.SALE_ID "
				+ " WHERE a.FIRM_ID=:firmId "
				+ " AND a.FIRM_ID=b.FIRM_ID "
			       + " AND c.USER_ID=a.USER_ID "
			       + " AND d.PROG_ID=c.PROG_ID "
			       + " ORDER BY c.SALES_DATE DESC LIMIT 5 ";
		 Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("firmId",firmId);
				  
		 try{
			
			 List<? extends PacketSaleFactory> packetSaleClasses=findEntityList(sql, paramMap, PacketSaleClass.class);
			 return (List<PacketSaleFactory>)packetSaleClasses;
		 }catch (Exception e) {
			 e.printStackTrace();
				return null;
		 }
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PacketSaleFactory> findLast10UserPacketSaleForMembership(int firmId) {
		String sql="SELECT a.* ,b.*,c.*,'psm' PROG_TYPE,d.*,IFNULL(e.PAY_ID,0) PAY_ID "
				+ ",DATE_FORMAT(c.SALES_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') SALES_DATE_STR "
				+ ",DATE_FORMAT(c.CHANGE_DATE,'"+GlobalUtil.global.getPtDateFormat()+"') CHANGE_DATE_STR "
				+ " FROM user a ,def_firm b,program_membership d,packet_sale_membership c left join packet_payment_membership e ON e.SALE_ID=c.SALE_ID "
				+ " WHERE a.FIRM_ID=:firmId "
				+ " AND a.FIRM_ID=b.FIRM_ID "
			       + " AND c.USER_ID=a.USER_ID "
			       + " AND d.PROG_ID=c.PROG_ID "
			       + " ORDER BY c.SALES_DATE DESC LIMIT 5 ";
		 Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("firmId",firmId);
				  
		 try{
			
			 List<? extends PacketSaleFactory> packetSaleMemberships=findEntityList(sql, paramMap, PacketSaleMembership.class);
			 return (List<PacketSaleFactory>)packetSaleMemberships;
		 }catch (Exception e) {
			 e.printStackTrace();
				return null;
		 }
	}

	

	

	
	
	

	
	

	
	
}
