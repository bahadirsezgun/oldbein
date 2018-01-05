package tr.com.abasus.ptboss.packetsale.service;

import java.util.Date;
import java.util.List;

import tr.com.abasus.ptboss.packetsale.entity.PacketSaleClass;
import tr.com.abasus.ptboss.packetsale.entity.PacketSaleFactory;
import tr.com.abasus.ptboss.packetsale.entity.PacketSaleMembership;
import tr.com.abasus.ptboss.packetsale.entity.PacketSalePersonal;
import tr.com.abasus.ptboss.ptuser.entity.User;
import tr.com.abasus.ptboss.result.entity.HmiResultObj;

public interface PacketSaleService {

	
	/***************************************************************************************
						PERSONAL PACKET SALE
	***************************************************************************************/
	
	public HmiResultObj buyPersonalPacket(PacketSalePersonal packetSalePersonal);
	
	public HmiResultObj  deletePersonalPacket(PacketSalePersonal packetSalePersonal);
	
	public List<PacketSaleFactory> findPersonalUserBoughtPackets(long userId);
	
	public PacketSaleFactory findPersonalPacketSaleById(long saleId);
	
	public List<PacketSaleFactory> findPersonalPacketSaleByNameAndDate(String userName,String userSurname,Date salesDate,Date salesDateNext);
	
	public List<PacketSaleFactory> findPersonalPacketSaleByName(String userName,String userSurname);
	
	public List<User> findByUserNameAndSaleProgramForPersonalWithNoPlan(String userName,String userSurname,long progId,int firmId);
	
	public List<User> findByUserNameAndSaleProgramForPersonalWithPlan(String userName,String userSurname,long progId,int firmId);
	
	public List<User> findByUserNameForSalesForPersonal(String userName,String userSurname,int firmId);
	
	public List<User> findByUsersAndSaledProgramForPersonal(long schId);
	
	public User findUserBySaleIdForPersonal(long saleId);
	
	public List<PacketSaleFactory> findUserPacketSaleBySchIdForPersonal(long schId);
	
	public HmiResultObj updateSalePacketForPersonal(PacketSaleFactory packetSaleFactory);
	
	public List<PacketSaleFactory> findLast10UserPacketSaleForPersonal(int firmId);
	
	/***************************************************************************************
	CLASS PACKET SALE
	***************************************************************************************/

	public HmiResultObj buyClassPacket(PacketSaleClass packetSaleClass);
	
	public HmiResultObj  deleteClassPacket(PacketSaleClass packetSaleClass);
	
	public List<PacketSaleFactory> findClassUserBoughtPackets(long userId);
	
	public PacketSaleFactory findClassPacketSaleById(long saleId);
	
	public List<PacketSaleFactory> findClassPacketSaleByNameAndDate(String userName,String userSurname,Date salesDate,Date salesDateNext);
	
	public List<PacketSaleFactory> findClassPacketSaleByName(String userName,String userSurname);
	
	public List<User> findByUserNameAndSaleProgramForClassWithNoPlan(String userName,String userSurname,long progId,int firmId);
	
	public List<User> findByUserNameAndSaleProgramForClassWithPlan(String userName,String userSurname,long progId,int firmId);
	
	public List<User> findByUserNameForSalesForClass(String userName,String userSurname,int firmId);
	
	public List<User> findByUsersAndSaledProgramForClass(long schId);
	
	public User findUserBySaleIdForClass(long saleId);
	
	public List<PacketSaleFactory> findUserPacketSaleBySchIdForClass(long schId);
	
	
	public HmiResultObj updateSalePacketForClass(PacketSaleFactory packetSaleFactory);
	
	public List<PacketSaleFactory> findLast10UserPacketSaleForClass(int firmId);
	
	/***************************************************************************************
	MEMBERSHIP PACKET SALE
	***************************************************************************************/

	public HmiResultObj buyMembershipPacket(PacketSaleMembership packetSaleMembership);
	
	public HmiResultObj  deleteMembershipPacket(PacketSaleMembership packetSaleMembership);
	
	public List<PacketSaleFactory> findMembershipUserBoughtPackets(long userId);
	
	public PacketSaleFactory findMembershipPacketSaleById(long saleId);
	
	public List<PacketSaleFactory> findMembershipPacketSaleByNameAndDate(String userName,String userSurname,Date salesDate,Date salesDateNext);
	
	public List<PacketSaleFactory> findMembershipPacketSaleByName(String userName,String userSurname);
	
	public User findUserBySaleIdForMembership(long saleId);
	
	public List<PacketSaleFactory> findLast10UserPacketSaleForMembership(int firmId);
	
	
}
