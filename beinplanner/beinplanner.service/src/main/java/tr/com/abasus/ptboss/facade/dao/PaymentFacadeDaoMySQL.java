package tr.com.abasus.ptboss.facade.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tr.com.abasus.general.dao.AbaxJdbcDaoSupport;
import tr.com.abasus.general.dao.SqlDao;
import tr.com.abasus.ptboss.packetpayment.entity.PacketPaymentClass;
import tr.com.abasus.ptboss.packetpayment.entity.PacketPaymentClassDetail;
import tr.com.abasus.ptboss.packetpayment.entity.PacketPaymentFactory;
import tr.com.abasus.ptboss.packetpayment.entity.PacketPaymentMembership;
import tr.com.abasus.ptboss.packetpayment.entity.PacketPaymentMembershipDetail;
import tr.com.abasus.ptboss.packetpayment.entity.PacketPaymentPersonal;
import tr.com.abasus.ptboss.packetpayment.entity.PacketPaymentPersonalDetail;
import tr.com.abasus.ptboss.packetsale.entity.PacketSaleClass;
import tr.com.abasus.ptboss.packetsale.entity.PacketSaleMembership;
import tr.com.abasus.ptboss.packetsale.entity.PacketSalePersonal;
import tr.com.abasus.ptboss.ptuser.entity.User;

public class PaymentFacadeDaoMySQL  extends AbaxJdbcDaoSupport implements PaymentFacadeDao {

	SqlDao sqlDao;
	
	

	public SqlDao getSqlDao() {
		return sqlDao;
	}

	public void setSqlDao(SqlDao sqlDao) {
		this.sqlDao = sqlDao;
	}
	
	@Override
	public synchronized boolean isPersonalPaymentCanDelete(PacketPaymentPersonal ppp) {
		boolean result=true;
		String sql="SELECT * " +
				 " FROM packet_payment_personal"
				+" WHERE PAY_ID=:payId  ";
		
		 Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("payId",ppp.getPayId());
		 	
		 
		try {
			PacketPaymentPersonal packetPaymentPersonalDb=findEntityForObject(sql, paramMap, PacketPaymentPersonal.class);
			if(packetPaymentPersonalDb.getPayConfirm()==1)
				result=false;
		} catch (Exception e) {
			result=false;
		}
		return result;
	}

	@Override
	public synchronized boolean isMembershipPaymentCanDelete(PacketPaymentMembership ppm) {
		boolean result=true;
		String sql="SELECT * " +
				 " FROM packet_payment_membership "
				+" WHERE PAY_ID=:payId  ";
		
		 Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("payId",ppm.getPayId());
		 	
		 
		try {
			PacketPaymentMembership packetPaymentMembership=findEntityForObject(sql, paramMap, PacketPaymentMembership.class);
			if(packetPaymentMembership.getPayConfirm()==1)
				result=false;
		} catch (Exception e) {
			result=false;
		}
		return result;
	}

	@Override
	public synchronized boolean isClassPaymentCanDelete(PacketPaymentClass ppc) {
		boolean result=true;
		String sql="SELECT * " +
				 " FROM packet_payment_class "
				+" WHERE PAY_ID=:payId  ";
		
		 Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("payId",ppc.getPayId());
		 	
		 
		try {
			PacketPaymentClass packetPaymentClass=findEntityForObject(sql, paramMap, PacketPaymentClass.class);
			if(packetPaymentClass.getPayConfirm()==1)
				result=false;
		} catch (Exception e) {
			result=false;
		}
		return result;
	}

	@Override
	public synchronized boolean isPersonalPaymentCanChange(PacketPaymentPersonal ppp) {
		
		boolean result=true;
		String sql="SELECT * " +
				 " FROM packet_sale_personal "
				+" WHERE SALE_ID=:saleId  ";
		
		 Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("saleId",ppp.getSaleId());
		 	
		 
		try {
			PacketSalePersonal packetSalePersonal=findEntityForObject(sql, paramMap, PacketSalePersonal.class);
			if(packetSalePersonal.getBonusPayedFlag()==1)
				result=false;
		} catch (Exception e) {
			result=false;
		}
		return result;
	}

	@Override
	public synchronized boolean isMembershipPaymentCanChange(PacketPaymentMembership ppm) {
		boolean result=true;
		String sql="SELECT * " +
				 " FROM packet_sale_membership "
				+" WHERE SALE_ID=:saleId  ";
		
		 Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("saleId",ppm.getSaleId());
		 	
		 
		try {
			PacketSaleMembership packetSaleMembership=findEntityForObject(sql, paramMap, PacketSaleMembership.class);
			if(packetSaleMembership.getBonusPayedFlag()==1)
				result=false;
		} catch (Exception e) {
			result=false;
		}
		return result;
	}

	@Override
	public synchronized boolean isClassPaymentCanChange(PacketPaymentClass ppc) {
		boolean result=true;
		String sql="SELECT * " +
				 " FROM packet_sale_class "
				+" WHERE SALE_ID=:saleId  ";
		
		 Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("saleId",ppc.getSaleId());
		 	
		 
		try {
			PacketSaleClass packetSaleClass=findEntityForObject(sql, paramMap, PacketSaleClass.class);
			if(packetSaleClass.getBonusPayedFlag()==1)
				result=false;
		} catch (Exception e) {
			result=false;
		}
		return result;
	}

	@Override
	public synchronized boolean isPersonalPaymentDetailCanDelete(PacketPaymentPersonalDetail pppd) {
		 String sql="SELECT * " +
				 " FROM packet_payment_personal_detail"
				+" WHERE PAY_DET_ID=:payDetId  ";
		
		 Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("payDetId",pppd.getPayDetId());
		 try {
				List<PacketPaymentPersonalDetail> packetPaymentPersonalDetails=findEntityList(sql, paramMap, PacketPaymentPersonalDetail.class);
				if(packetPaymentPersonalDetails.size()==1){
					return false;
				}else{
					return true;
				}
		} catch (Exception e) {
			return true;
		}
	}

	@Override
	public synchronized boolean isMembershipPaymentDetailCanDelete(PacketPaymentMembershipDetail ppmd) {
		 String sql="SELECT * " +
				 " FROM packet_payment_membership_detail"
				+" WHERE PAY_DET_ID=:payDetId  ";
		
		 Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("payDetId",ppmd.getPayDetId());
		 try {
				List<PacketPaymentMembershipDetail> paymentMembershipDetails=findEntityList(sql, paramMap, PacketPaymentMembershipDetail.class);
				if(paymentMembershipDetails.size()==1){
					return false;
				}else{
					return true;
				}
		} catch (Exception e) {
			return true;
		}
	}

	@Override
	public synchronized boolean isClassPaymentDetailCanDelete(PacketPaymentClassDetail ppcd) {
		String sql="SELECT * " +
				 " FROM packet_payment_class_detail"
				+" WHERE PAY_DET_ID=:payDetId  ";
		
		 Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("payDetId",ppcd.getPayDetId());
		 	
		 
		try {
			List<PacketPaymentClassDetail> paymentClassDetails=findEntityList(sql, paramMap, PacketPaymentClassDetail.class);
			if(paymentClassDetails.size()==1){
				return false;
			}else{
				return true;
			}
	    } catch (Exception e) {
			return true;
		}
	}

	@Override
	public synchronized boolean isClassUserCanDelete(long userId) {
		String sql="SELECT * " +
				 " FROM packet_payment_class"
				+" WHERE SALE_ID IN (SELECT SALE_ID FROM packet_sale_class WHERE USER_ID=:userId)  ";
		
		 Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("userId",userId);
		 try {
				List<PacketPaymentClass> packetPaymentClasses=findEntityList(sql, paramMap, PacketPaymentClass.class);
				if(packetPaymentClasses.size()>0){
					return false;
				}else{
					return true;
				}
		} catch (Exception e) {
			return true;
		}
	}

	@Override
	public synchronized boolean isPsersonalUserCanDelete(long userId) {
		 String sql="SELECT * " +
				 " FROM packet_payment_personal"
				+" WHERE SALE_ID IN (SELECT SALE_ID FROM packet_sale_personal WHERE USER_ID=:userId)  ";
		
		 Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("userId",userId);
		 try {
				List<PacketPaymentPersonal> packetPaymentPersonals=findEntityList(sql, paramMap, PacketPaymentPersonal.class);
				if(packetPaymentPersonals.size()>0){
					return false;
				}else{
					return true;
				}
		} catch (Exception e) {
			return true;
		}
	}

	@Override
	public synchronized boolean isMembershipUserCanDelete(long userId) {
		String sql="SELECT * " +
				 " FROM packet_payment_membership"
				+" WHERE SALE_ID IN (SELECT SALE_ID FROM packet_sale_membership WHERE USER_ID=:userId)  ";
		
		 Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("userId",userId);
		 try {
				List<PacketPaymentMembership> packetPaymentMemberships=findEntityList(sql, paramMap, PacketPaymentMembership.class);
				if(packetPaymentMemberships.size()>0){
					return false;
				}else{
					return true;
				}
		 } catch (Exception e) {
			return true;
		 }
	}

	@Override
	public boolean havePersonalPaymentForSale(long saleId) {
		String sql="SELECT * " +
				 " FROM packet_payment_personal"
				+" WHERE SALE_ID=:saleId LIMIT 1 ";
		
		 Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("saleId",saleId);
		 try {
				PacketPaymentPersonal packetPaymentPersonal=findEntityForObject(sql, paramMap, PacketPaymentPersonal.class);
				if(packetPaymentPersonal!=null){
					return true;
				}else{
					return false;
				}
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public boolean haveMembershipPaymentForSale(long saleId) {
		String sql="SELECT * " +
				 " FROM packet_payment_membership"
				+" WHERE SALE_ID=:saleId LIMIT 1 ";
		
		 Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("saleId",saleId);
		 try {
				PacketPaymentMembership packetPaymentMembership=findEntityForObject(sql, paramMap, PacketPaymentMembership.class);
				if(packetPaymentMembership!=null){
					return true;
				}else{
					return false;
				}
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public boolean haveClassPaymentForSale(long saleId) {
		String sql="SELECT * " +
				 " FROM packet_payment_class"
				+" WHERE SALE_ID=:saleId LIMIT 1 ";
		
		 Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("saleId",saleId);
		 try {
				PacketPaymentClass packetPaymentClass=findEntityForObject(sql, paramMap, PacketPaymentClass.class);
				if(packetPaymentClass!=null){
					return true;
				}else{
					return false;
				}
		} catch (Exception e) {
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PacketPaymentFactory> getPersonalPaymentToUserId(long userId) {
		String sql="SELECT a.*,b.* " +
				 " FROM packet_payment_personal a,packet_sale_personal b "
				+" WHERE b.USER_ID=:userId "
				+ " AND a.SALE_ID=b.SALE_ID";
		
		 Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("userId",userId);
		 try {
			List<? extends PacketPaymentFactory> packetPaymentPersonals=findEntityList(sql, paramMap, PacketPaymentPersonal.class);
			return (List<PacketPaymentFactory>)packetPaymentPersonals;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<PacketPaymentFactory> getMembershipPaymentToUserId(long userId) {
		String sql="SELECT a.*,b.* " +
				 " FROM packet_payment_membership a,packet_sale_membership b "
				+" WHERE b.USER_ID=:userId "
				+ " AND a.SALE_ID=b.SALE_ID";
		
		 Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("userId",userId);
		 try {
			List<? extends PacketPaymentFactory> packetPaymentMemberships=findEntityList(sql, paramMap, PacketPaymentMembership.class);
			return (List<PacketPaymentFactory>)packetPaymentMemberships;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PacketPaymentFactory> getClassPaymentToUserId(long userId) {
		String sql="SELECT a.*,b.* " +
				 " FROM packet_payment_class a,packet_sale_class b "
				+" WHERE b.USER_ID=:userId "
				+ " AND a.SALE_ID=b.SALE_ID";
		
		 Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("userId",userId);
		 try {
			List<? extends PacketPaymentFactory> packetPaymentClasses=findEntityList(sql, paramMap, PacketPaymentClass.class);
			return (List<PacketPaymentFactory>)packetPaymentClasses;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	
	
	
	

	
}
