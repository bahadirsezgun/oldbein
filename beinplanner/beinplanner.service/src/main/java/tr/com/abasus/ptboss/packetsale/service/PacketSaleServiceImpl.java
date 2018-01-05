package tr.com.abasus.ptboss.packetsale.service;

import java.util.Date;
import java.util.List;

import tr.com.abasus.ptboss.packetsale.dao.PacketSaleDao;
import tr.com.abasus.ptboss.packetsale.entity.PacketSaleClass;
import tr.com.abasus.ptboss.packetsale.entity.PacketSaleFactory;
import tr.com.abasus.ptboss.packetsale.entity.PacketSaleMembership;
import tr.com.abasus.ptboss.packetsale.entity.PacketSalePersonal;
import tr.com.abasus.ptboss.ptuser.entity.User;
import tr.com.abasus.ptboss.result.entity.HmiResultObj;

public class PacketSaleServiceImpl implements PacketSaleService {

	PacketSaleDao packetSaleDao;
	
	
	
	public PacketSaleDao getPacketSaleDao() {
		return packetSaleDao;
	}

	public void setPacketSaleDao(PacketSaleDao packetSaleDao) {
		this.packetSaleDao = packetSaleDao;
	}

	@Override
	public HmiResultObj buyPersonalPacket(PacketSalePersonal packetSalePersonal) {
		return packetSaleDao.buyPersonalPacket(packetSalePersonal);
	}

	@Override
	public HmiResultObj deletePersonalPacket(PacketSalePersonal packetSalePersonal) {
		return packetSaleDao.deletePersonalPacket(packetSalePersonal);
	}

	@Override
	public List<PacketSaleFactory> findPersonalUserBoughtPackets(long userId) {
		return packetSaleDao.findPersonalUserBoughtPackets(userId);
	}

	@Override
	public PacketSaleFactory findPersonalPacketSaleById(long saleId) {
		return packetSaleDao.findPersonalPacketSaleById(saleId);
	}

	@Override
	public List<PacketSaleFactory> findPersonalPacketSaleByNameAndDate(String userName, String userSurname,
			Date salesDate, Date salesDateNext) {
		return packetSaleDao.findPersonalPacketSaleByNameAndDate(userName, userSurname, salesDate, salesDateNext);
	}

	@Override
	public List<PacketSaleFactory> findPersonalPacketSaleByName(String userName, String userSurname) {
		return packetSaleDao.findPersonalPacketSaleByName(userName, userSurname);
	}

	@Override
	public List<User> findByUserNameAndSaleProgramForPersonalWithNoPlan(String userName, String userSurname, long progId,int firmId) {
		return packetSaleDao.findByUserNameAndSaleProgramForPersonalWithNoPlan(userName, userSurname, progId,firmId);
	}
	
	@Override
	public List<User> findByUserNameAndSaleProgramForPersonalWithPlan(String userName, String userSurname, long progId,int firmId) {
		return packetSaleDao.findByUserNameAndSaleProgramForPersonalWithPlan(userName, userSurname, progId,firmId);
	}

	
	@Override
	public List<User> findByUsersAndSaledProgramForPersonal(long schId) {
		return packetSaleDao.findByUsersAndSaledProgramForPersonal(schId);
	}

	

	
	/***********************************CLASS SALE****************************************************************/
	
	
	@Override
	public HmiResultObj buyClassPacket(PacketSaleClass packetSaleClass) {
		return packetSaleDao.buyClassPacket(packetSaleClass);
	}

	@Override
	public HmiResultObj deleteClassPacket(PacketSaleClass packetSaleClass) {
		return packetSaleDao.deleteClassPacket(packetSaleClass);
	}

	@Override
	public List<PacketSaleFactory> findClassUserBoughtPackets(long userId) {
		return packetSaleDao.findClassUserBoughtPackets(userId);
	}

	@Override
	public PacketSaleFactory findClassPacketSaleById(long saleId) {
		return packetSaleDao.findClassPacketSaleById(saleId);
	}

	@Override
	public List<PacketSaleFactory> findClassPacketSaleByNameAndDate(String userName, String userSurname, Date salesDate,
			Date salesDateNext) {
		return packetSaleDao.findClassPacketSaleByNameAndDate(userName, userSurname, salesDate, salesDateNext);
	}

	@Override
	public List<PacketSaleFactory> findClassPacketSaleByName(String userName, String userSurname) {
		return packetSaleDao.findClassPacketSaleByName(userName, userSurname);
	}

	@Override
	public List<User> findByUserNameAndSaleProgramForClassWithNoPlan(String userName, String userSurname, long progId,int firmId) {
		return packetSaleDao.findByUserNameAndSaleProgramForClassWithNoPlan(userName, userSurname, progId,firmId);
	}

	@Override
	public List<User> findByUserNameAndSaleProgramForClassWithPlan(String userName, String userSurname, long progId,int firmId) {
		return packetSaleDao.findByUserNameAndSaleProgramForClassWithPlan(userName, userSurname, progId,firmId);
	}
	

	@Override
	public List<User> findByUsersAndSaledProgramForClass(long schId) {
		return packetSaleDao.findByUsersAndSaledProgramForClass(schId);
	}
	

	
	/***********************************MEMBERSHIP****************************************************************/
	
	
	
	@Override
	public HmiResultObj buyMembershipPacket(PacketSaleMembership packetSaleMembership) {
		return packetSaleDao.buyMembershipPacket(packetSaleMembership);
	}

	@Override
	public HmiResultObj deleteMembershipPacket(PacketSaleMembership packetSaleMembership) {
		return packetSaleDao.deleteMembershipPacket(packetSaleMembership);
	}

	@Override
	public List<PacketSaleFactory> findMembershipUserBoughtPackets(long userId) {
		return packetSaleDao.findMembershipUserBoughtPackets(userId);
	}

	@Override
	public PacketSaleFactory findMembershipPacketSaleById(long saleId) {
		return packetSaleDao.findMembershipPacketSaleById(saleId);
	}

	@Override
	public List<PacketSaleFactory> findMembershipPacketSaleByNameAndDate(String userName, String userSurname,
			Date salesDate, Date salesDateNext) {
		return packetSaleDao.findMembershipPacketSaleByNameAndDate(userName, userSurname, salesDate, salesDateNext);
	}

	@Override
	public List<PacketSaleFactory> findMembershipPacketSaleByName(String userName, String userSurname) {
		return packetSaleDao.findMembershipPacketSaleByName(userName, userSurname);
	}

	@Override
	public User findUserBySaleIdForPersonal(long saleId) {
		return packetSaleDao.findUserBySaleIdForPersonal(saleId);
	}

	@Override
	public User findUserBySaleIdForClass(long saleId) {
		return packetSaleDao.findUserBySaleIdForClass(saleId);
	}

	@Override
	public User findUserBySaleIdForMembership(long saleId) {
		return packetSaleDao.findUserBySaleIdForMembership(saleId);
	}

	@Override
	public List<PacketSaleFactory> findUserPacketSaleBySchIdForPersonal(long schId) {
		return packetSaleDao.findUserPacketSaleBySchIdForPersonal(schId);
	}

	@Override
	public List<PacketSaleFactory> findUserPacketSaleBySchIdForClass(long schId) {
		return packetSaleDao.findUserPacketSaleBySchIdForClass(schId);
	}

	@Override
	public HmiResultObj updateSalePacketForPersonal(PacketSaleFactory packetSaleFactory) {
		return packetSaleDao.updateSalePacketForPersonal(packetSaleFactory);
	}

	@Override
	public HmiResultObj updateSalePacketForClass(PacketSaleFactory packetSaleFactory) {
		return packetSaleDao.updateSalePacketForClass(packetSaleFactory);
	}

	@Override
	public List<User> findByUserNameForSalesForPersonal(String userName, String userSurname, int firmId) {
		return packetSaleDao.findByUserNameForSalesForPersonal(userName, userSurname, firmId);
	}

	@Override
	public List<User> findByUserNameForSalesForClass(String userName, String userSurname, int firmId) {
		return packetSaleDao.findByUserNameForSalesForClass(userName, userSurname, firmId);
	}

	@Override
	public List<PacketSaleFactory> findLast10UserPacketSaleForPersonal(int firmId) {
		return packetSaleDao.findLast10UserPacketSaleForPersonal(firmId);
	}

	@Override
	public List<PacketSaleFactory> findLast10UserPacketSaleForClass(int firmId) {
		return packetSaleDao.findLast10UserPacketSaleForClass(firmId);
	}

	@Override
	public List<PacketSaleFactory> findLast10UserPacketSaleForMembership(int firmId) {
		return packetSaleDao.findLast10UserPacketSaleForMembership(firmId);
	}



	
	
	/*************************************************************************************************************/
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
