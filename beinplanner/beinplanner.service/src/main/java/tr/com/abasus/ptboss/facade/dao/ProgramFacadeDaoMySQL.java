package tr.com.abasus.ptboss.facade.dao;

import java.util.HashMap;
import java.util.Map;

import tr.com.abasus.general.dao.AbaxJdbcDaoSupport;
import tr.com.abasus.general.dao.SqlDao;
import tr.com.abasus.ptboss.packetsale.entity.PacketSaleMembership;
import tr.com.abasus.ptboss.packetsale.entity.PacketSalePersonal;
import tr.com.abasus.ptboss.program.entity.ProgramClass;
import tr.com.abasus.ptboss.program.entity.ProgramMembership;
import tr.com.abasus.ptboss.program.entity.ProgramPersonal;

public class ProgramFacadeDaoMySQL extends AbaxJdbcDaoSupport implements ProgramFacadeDao {

	SqlDao sqlDao;
	
	

	public SqlDao getSqlDao() {
		return sqlDao;
	}

	public void setSqlDao(SqlDao sqlDao) {
		this.sqlDao = sqlDao;
	}

	@Override
	public boolean isPersonalProgramCanDelete(ProgramPersonal programPersonal) {
		boolean result=false;
		String sql="SELECT * " +
				 " FROM packet_sale_personal"
				+" WHERE PROG_ID=:progId"
				+ " LIMIT 1 ";
		
		 Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("progId",programPersonal.getProgId());
		 	
		 
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
	public boolean isMembershipProgramCanDelete(ProgramMembership programMembership) {
		boolean result=false;
		String sql="SELECT * " +
				 " FROM packet_sale_membership "
				+" WHERE PROG_ID=:progId"
				+ " LIMIT 1 ";
		
		 Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("progId",programMembership.getProgId());
		 	
		 
		try {
			PacketSaleMembership packetSale=findEntityForObject(sql, paramMap, PacketSaleMembership.class);
			if(packetSale!=null)
				result=true;
		} catch (Exception e) {
			result=false;
		}
		return result;
	}

	@Override
	public boolean isClassProgramCanDelete(ProgramClass programClass) {
		boolean result=false;
		String sql="SELECT * " +
				 " FROM packet_sale_class "
				+" WHERE PROG_ID=:progId"
				+ " LIMIT 1 ";
		
		 Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("progId",programClass.getProgId());
		 	
		 
		try {
			PacketSalePersonal packetSale=findEntityForObject(sql, paramMap, PacketSalePersonal.class);
			if(packetSale!=null)
				result=true;
		} catch (Exception e) {
			result=false;
		}
		return result;
	}
	
	

}
