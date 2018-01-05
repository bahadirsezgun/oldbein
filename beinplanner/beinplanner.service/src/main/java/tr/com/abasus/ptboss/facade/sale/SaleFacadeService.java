package tr.com.abasus.ptboss.facade.sale;

import java.util.Date;
import java.util.List;

import tr.com.abasus.ptboss.packetsale.entity.PacketSaleFactory;
import tr.com.abasus.ptboss.result.entity.HmiResultObj;

public interface SaleFacadeService {

	public boolean canSaleDelete(PacketSaleFactory packetSaleFactory);
	
	public boolean canStaffDelete(long userId);

	public HmiResultObj canSale(long userId,Date startDate);
	
	public List<PacketSaleFactory> getPacketSalesToUserId(long userId);
	
	public boolean canUserDelete(long userId);

	public HmiResultObj canSaleChange(PacketSaleFactory packetSaleFactory);
	
}
