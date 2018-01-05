package tr.com.abasus.ptboss.facade.payment;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tr.com.abasus.ptboss.facade.dao.PaymentFacadeDao;
import tr.com.abasus.ptboss.packetpayment.entity.PacketPaymentFactory;
import tr.com.abasus.ptboss.packetpayment.entity.PacketPaymentPersonal;
import tr.com.abasus.ptboss.packetpayment.entity.PacketPaymentPersonalDetail;
@Service(value="paymentPersonalFacade")
public class PaymentPersonalFacade implements PaymentFacadeService {

	
	@Autowired(required=true)
	PaymentFacadeDao paymentFacadeDao;
	
	
	@Override
	public boolean canPaymentDelete(PacketPaymentFactory packetPaymentFactory) {
		return paymentFacadeDao.isPersonalPaymentCanDelete((PacketPaymentPersonal)packetPaymentFactory);
	}

	@Override
	public boolean canPaymentChange(PacketPaymentFactory packetPaymentFactory) {
		return paymentFacadeDao.isPersonalPaymentCanChange((PacketPaymentPersonal)packetPaymentFactory);
	}

	@Override
	public boolean canPaymentDetailDelete(long payDetId, long payId) {
		PacketPaymentPersonal packetPaymentPersonal=new PacketPaymentPersonal();
		packetPaymentPersonal.setPayId(payId);
		
		if(!canPaymentDelete(packetPaymentPersonal)){
			PacketPaymentPersonalDetail packetPaymentPersonalDetail=new PacketPaymentPersonalDetail();
			packetPaymentPersonalDetail.setPayDetId(payDetId);
			return paymentFacadeDao.isPersonalPaymentDetailCanDelete(packetPaymentPersonalDetail);
		}
		return true;
	}

	@Override
	public boolean canUserDelete(long userId) {
		return paymentFacadeDao.isPsersonalUserCanDelete(userId);
	}

	public PaymentFacadeDao getPaymentFacadeDao() {
		return paymentFacadeDao;
	}

	public void setPaymentFacadeDao(PaymentFacadeDao paymentFacadeDao) {
		this.paymentFacadeDao = paymentFacadeDao;
	}

	@Override
	public List<PacketPaymentFactory> getPaymentsToUserId(long userId) {
		List<PacketPaymentFactory> packetPaymentFactories= paymentFacadeDao.getPersonalPaymentToUserId(userId);
		return packetPaymentFactories;
	}

	
	
}
