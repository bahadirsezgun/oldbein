package tr.com.abasus.ptboss.facade.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import tr.com.abasus.general.dao.AbaxJdbcDaoSupport;
import tr.com.abasus.general.dao.SqlDao;
import tr.com.abasus.ptboss.packetsale.entity.PacketSaleClass;
import tr.com.abasus.ptboss.packetsale.entity.PacketSaleFactory;
import tr.com.abasus.ptboss.packetsale.entity.PacketSaleMembership;
import tr.com.abasus.ptboss.packetsale.entity.PacketSalePersonal;

public class SaleFacadeDaoMySQL extends AbaxJdbcDaoSupport  implements SaleFacadeDao {

	SqlDao sqlDao;
	
	PaymentFacadeDao paymentFacadeDao;
	
	SchedulerFacadeDao schedulerFacadeDao;

	public SqlDao getSqlDao() {
		return sqlDao;
	}

	public void setSqlDao(SqlDao sqlDao) {
		this.sqlDao = sqlDao;
	}

	@Override
	public boolean canPersonalSaleDelete(PacketSaleFactory packetSaleFactory) {
	
		if(!paymentFacadeDao.havePersonalPaymentForSale(packetSaleFactory.getSaleId())){
			if(!schedulerFacadeDao.haveMemberGotPersonalSchedulerForSale(packetSaleFactory.getSaleId())){
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
		
	}

	@Override
	public boolean canMembershipSaleDelete(PacketSaleFactory packetSaleFactory) {
		if(!paymentFacadeDao.haveMembershipPaymentForSale(packetSaleFactory.getSaleId())){
				return true;
		}else{
			return false;
		}
	}

	@Override
	public boolean canClassSaleDelete(PacketSaleFactory packetSaleFactory) {
		if(!paymentFacadeDao.haveClassPaymentForSale(packetSaleFactory.getSaleId())){
			if(!schedulerFacadeDao.haveMemberGotClassSchedulerForSale(packetSaleFactory.getSaleId())){
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
	}

	@Override
	public boolean canPersonalStaffDelete(long userId) {
		boolean result=false;
		String sql="SELECT * " +
				 " FROM packet_sale_personal"
				+" WHERE STAFF_ID=:staffId"
				+ " LIMIT 1 ";
		
		 Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("staffId",userId);
		 	
		 
		try {
			PacketSalePersonal packetSale=findEntityForObject(sql, paramMap, PacketSalePersonal.class);
			if(packetSale!=null)
				result=true;
		} catch (Exception e) {
			result=false;
		}
		return result;
	}

	@Override
	public boolean canMembershipStaffDelete(long userId) {
		boolean result=false;
		String sql="SELECT * " +
				 " FROM packet_sale_membership"
				+" WHERE STAFF_ID=:staffId"
				+ " LIMIT 1 ";
		
		 Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("staffId",userId);
		 	
		 
		try {
			PacketSalePersonal packetSale=findEntityForObject(sql, paramMap, PacketSalePersonal.class);
			if(packetSale!=null)
				result=true;
		} catch (Exception e) {
			result=false;
		}
		return result;
	}

	@Override
	public boolean canClassStaffDelete(long userId) {
		boolean result=false;
		String sql="SELECT * " +
				 " FROM packet_sale_class"
				+" WHERE STAFF_ID=:staffId"
				+ " LIMIT 1 ";
		
		 Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("staffId",userId);
		 	
		 
		try {
			PacketSalePersonal packetSale=findEntityForObject(sql, paramMap, PacketSalePersonal.class);
			if(packetSale!=null)
				result=true;
		} catch (Exception e) {
			result=false;
		}
		return result;
	}

	public PaymentFacadeDao getPaymentFacadeDao() {
		return paymentFacadeDao;
	}

	public void setPaymentFacadeDao(PaymentFacadeDao paymentFacadeDao) {
		this.paymentFacadeDao = paymentFacadeDao;
	}

	public SchedulerFacadeDao getSchedulerFacadeDao() {
		return schedulerFacadeDao;
	}

	public void setSchedulerFacadeDao(SchedulerFacadeDao schedulerFacadeDao) {
		this.schedulerFacadeDao = schedulerFacadeDao;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PacketSaleFactory> getPersonalPacketSalesToUserId(long userId) {
		String sql="SELECT * " +
				 " FROM packet_sale_personal"
				+" WHERE USER_ID=:userId ";
		
		 Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("userId",userId);
		 	
		 
		try {
			List<? extends PacketSaleFactory> packetSales=findEntityList(sql, paramMap, PacketSalePersonal.class);
			return (List<PacketSaleFactory>) packetSales;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public List<PacketSaleFactory> getClassPacketSalesToUserId(long userId) {
		String sql="SELECT * " +
				 " FROM packet_sale_class"
				+" WHERE USER_ID=:userId ";
		
		 Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("userId",userId);
		 	
		 
		try {
			List<? extends PacketSaleFactory> packetSales=findEntityList(sql, paramMap, PacketSaleClass.class);
			return (List<PacketSaleFactory>) packetSales;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public List<PacketSaleFactory> getMembershipPacketSalesToUserId(long userId) {
		String sql="SELECT * " +
				 " FROM packet_sale_membership"
				+" WHERE USER_ID=:userId ";
		
		 Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("userId",userId);
		 	
		 
		try {
			List<? extends PacketSaleFactory> packetSales=findEntityList(sql, paramMap, PacketSaleMembership.class);
			return (List<PacketSaleFactory>) packetSales;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public boolean userHaveSaleInPersonal(long userId) {
		String sql="SELECT * " +
				 " FROM packet_sale_personal"
				+" WHERE USER_ID=:userId ";
		
		 Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("userId",userId);
		 	
		 
		try {
			List<PacketSalePersonal> packetSales=findEntityList(sql, paramMap, PacketSalePersonal.class);
			
			if(packetSales.size()>0)
				return false;
			else
				return true;
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	@Override
	public boolean userHaveSaleInClass(long userId) {
		String sql="SELECT * " +
				 " FROM packet_sale_class"
				+" WHERE USER_ID=:userId ";
		
		 Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("userId",userId);
		 	
		 
		try {
			List<PacketSaleClass> packetSales=findEntityList(sql, paramMap, PacketSaleClass.class);
			
			if(packetSales.size()>0)
				return false;
			else
				return true;
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	@Override
	public boolean userHaveSaleInMembership(long userId) {
		String sql="SELECT * " +
				 " FROM packet_sale_membership"
				+" WHERE USER_ID=:userId ";
		
		 Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("userId",userId);
		 	
		 
		try {
			List<PacketSaleMembership> packetSales=findEntityList(sql, paramMap, PacketSaleMembership.class);
			
			if(packetSales.size()>0)
				return false;
			else
				return true;
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	
	
}
