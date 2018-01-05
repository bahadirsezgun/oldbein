package tr.com.abasus.ptboss.packetsale.decorator;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import tr.com.abasus.ptboss.packetsale.entity.PacketSaleFactory;

@Component
@Primary
public class PacketSaleDecorator implements IPacketSale {

	@Autowired
	@Qualifier("packetSalePersonal")
	IPacketSale iPacketSale;
	
	
	@Override
	public List<PacketSaleFactory> findAllUserBoughtPackets(long userId) {
		return iPacketSale.findAllUserBoughtPackets(userId);
	}


	@Override
	public List<PacketSaleFactory> findLast10UserPacketSale(int firmId) {
		return iPacketSale.findLast10UserPacketSale(firmId);
	}

	
	
	
}
