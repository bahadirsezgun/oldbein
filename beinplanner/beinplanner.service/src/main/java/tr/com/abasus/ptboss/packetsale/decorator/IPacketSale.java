package tr.com.abasus.ptboss.packetsale.decorator;

import java.util.List;

import tr.com.abasus.ptboss.packetsale.entity.PacketSaleFactory;

public interface IPacketSale {

	public List<PacketSaleFactory> findAllUserBoughtPackets(long userId);
	
	
	public List<PacketSaleFactory> findLast10UserPacketSale(int firmId);
	
}
