package tr.com.abasus.ptboss.facade.payment;

import java.util.List;

import tr.com.abasus.ptboss.packetpayment.entity.PacketPaymentFactory;


public interface PaymentFacadeService  {

	public boolean canPaymentDelete(PacketPaymentFactory packetPaymentFactory);

	public boolean canPaymentChange(PacketPaymentFactory packetPaymentFactory);

	public boolean canPaymentDetailDelete(long payDetId, long payId);

	public boolean canUserDelete(long userId);

	public List<PacketPaymentFactory> getPaymentsToUserId(long userId);
		

}
