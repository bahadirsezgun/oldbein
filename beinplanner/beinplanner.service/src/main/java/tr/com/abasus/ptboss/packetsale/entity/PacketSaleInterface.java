package tr.com.abasus.ptboss.packetsale.entity;

import java.util.List;

import tr.com.abasus.ptboss.ptuser.entity.User;
import tr.com.abasus.ptboss.result.entity.HmiResultObj;

public interface PacketSaleInterface {

	public List<PacketSaleFactory> findUserBoughtPackets(long userId);
	
	public List<PacketSaleFactory> searchSaledPackets(PacketSaleFactory packetSaleQuery);
	
	public  PacketSaleFactory findSaledPacketsById(long saleId);
	
	public List<HmiResultObj> saleNewPacket(PacketSaleFactory packetSaleFactory);
	
	public HmiResultObj saleUpdatePacket(PacketSaleFactory packetSaleFactory);
	
	public HmiResultObj saleCreatePacket(PacketSaleFactory packetSaleFactory);
	
	
	public HmiResultObj deleteSalePacket(PacketSaleFactory packetSaleQuery);
	
	public HmiResultObj updateSalePacket(PacketSaleFactory packetSaleFactory);
	
	public List<PacketSaleFactory> findPacketsBySaleIds(List<PacketSaleFactory> packetSaleFactories);
	
	public List<User> findByUserNameAndSaleProgramWithNoPlan(String userName,String userSurname,long progId,int firmId);
	
	public List<User> findByUserNameAndSaleProgramWithPlan(String userName,String userSurname,long progId,int firmId);
	
	public List<User> findByUsersAndSaledProgram(long schId);
	
	public User findUserBySaleId(long saleId);
	
	public List<PacketSaleFactory> findUserPacketSaleBySchId(long schId);
	
	public List<User> findByUserNameForSales(String userName,String userSurname,int firmId);
	
	
}
