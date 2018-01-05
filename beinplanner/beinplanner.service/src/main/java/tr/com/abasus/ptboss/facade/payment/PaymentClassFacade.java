package tr.com.abasus.ptboss.facade.payment;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import tr.com.abasus.ptboss.facade.dao.PaymentFacadeDao;
import tr.com.abasus.ptboss.packetpayment.entity.PacketPaymentClass;
import tr.com.abasus.ptboss.packetpayment.entity.PacketPaymentClassDetail;
import tr.com.abasus.ptboss.packetpayment.entity.PacketPaymentFactory;

@Service(value="paymentClassFacade")
public class PaymentClassFacade implements PaymentFacadeService {

	@Autowired
	PaymentFacadeDao paymentFacadeDao;
	
	@Autowired
	@Qualifier(value="paymentMembershipFacade")
	PaymentFacadeService paymentFacadeService;
	
	
	@Override
	public boolean canPaymentDelete(PacketPaymentFactory packetPaymentFactory) {
		return paymentFacadeDao.isClassPaymentCanDelete((PacketPaymentClass)packetPaymentFactory);
	}

	@Override
	public boolean canPaymentChange(PacketPaymentFactory packetPaymentFactory) {
		return paymentFacadeDao.isClassPaymentCanChange((PacketPaymentClass)packetPaymentFactory);
	}

	@Override
	public boolean canPaymentDetailDelete(long payDetId, long payId) {
		PacketPaymentClass packetPaymentClass=new PacketPaymentClass();
		packetPaymentClass.setPayId(payId);
		
		if(!canPaymentDelete(packetPaymentClass)){
			PacketPaymentClassDetail packetPaymentClassDetail=new PacketPaymentClassDetail();
			packetPaymentClassDetail.setPayDetId(payDetId);
			return paymentFacadeDao.isClassPaymentDetailCanDelete(packetPaymentClassDetail);
		}
		return true;
	}

	@Override
	public boolean canUserDelete(long userId) {
		
		if(paymentFacadeService.canUserDelete(userId)){
			return paymentFacadeDao.isClassUserCanDelete(userId);
		}else
		return false;
	}

	public PaymentFacadeDao getPaymentFacadeDao() {
		return paymentFacadeDao;
	}

	public void setPaymentFacadeDao(PaymentFacadeDao paymentFacadeDao) {
		this.paymentFacadeDao = paymentFacadeDao;
	}

	public PaymentFacadeService getPaymentFacadeService() {
		return paymentFacadeService;
	}

	public void setPaymentFacadeService(PaymentFacadeService paymentFacadeService) {
		this.paymentFacadeService = paymentFacadeService;
	}

	@Override
	public List<PacketPaymentFactory> getPaymentsToUserId(long userId) {
		List<PacketPaymentFactory> packetPaymentFactoriesInChain= paymentFacadeService.getPaymentsToUserId(userId);
		List<PacketPaymentFactory> packetPaymentFactories= paymentFacadeDao.getClassPaymentToUserId(userId);
		packetPaymentFactoriesInChain.addAll(packetPaymentFactories);
				
		return packetPaymentFactoriesInChain;
	}

	
	
}
