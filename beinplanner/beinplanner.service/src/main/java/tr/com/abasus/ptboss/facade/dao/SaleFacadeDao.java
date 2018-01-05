package tr.com.abasus.ptboss.facade.dao;

import java.util.List;

import tr.com.abasus.ptboss.packetsale.entity.PacketSaleFactory;

public interface SaleFacadeDao {

	
	public boolean canPersonalSaleDelete(PacketSaleFactory packetSaleFactory);
	public boolean canMembershipSaleDelete(PacketSaleFactory packetSaleFactory);
	public boolean canClassSaleDelete(PacketSaleFactory packetSaleFactory);
	

	public boolean canPersonalStaffDelete(long userId);
	public boolean canMembershipStaffDelete(long userId);
	public boolean canClassStaffDelete(long userId);
	
	
	public List<PacketSaleFactory> getPersonalPacketSalesToUserId(long userId);
	public List<PacketSaleFactory> getClassPacketSalesToUserId(long userId);
	public List<PacketSaleFactory> getMembershipPacketSalesToUserId(long userId);
	
	
	public boolean userHaveSaleInPersonal(long userId);
	public boolean userHaveSaleInClass(long userId);
	public boolean userHaveSaleInMembership(long userId);
	
	
}
