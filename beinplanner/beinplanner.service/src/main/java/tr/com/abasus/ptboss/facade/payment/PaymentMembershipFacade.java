package tr.com.abasus.ptboss.facade.payment;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import tr.com.abasus.ptboss.facade.dao.PaymentFacadeDao;
import tr.com.abasus.ptboss.packetpayment.entity.PacketPaymentFactory;
import tr.com.abasus.ptboss.packetpayment.entity.PacketPaymentMembership;
import tr.com.abasus.ptboss.packetpayment.entity.PacketPaymentMembershipDetail;
@Service(value="paymentMembershipFacade")
public class PaymentMembershipFacade implements PaymentFacadeService {

	@Autowired
	PaymentFacadeDao paymentFacadeDao;
	
	@Autowired
	@Qualifier(value="paymentPersonalFacade")
	PaymentFacadeService paymentFacadeService;
	
	
	@Override
	public boolean canPaymentDelete(PacketPaymentFactory packetPaymentFactory) {
		return paymentFacadeDao.isMembershipPaymentCanDelete((PacketPaymentMembership)packetPaymentFactory);
	}

	@Override
	public boolean canPaymentChange(PacketPaymentFactory packetPaymentFactory) {
		return paymentFacadeDao.isMembershipPaymentCanChange((PacketPaymentMembership)packetPaymentFactory);
	}

	@Override
	public boolean canPaymentDetailDelete(long payDetId, long payId) {
		PacketPaymentMembership packetPaymentMembership=new PacketPaymentMembership();
		packetPaymentMembership.setPayId(payId);
		
		if(!canPaymentDelete(packetPaymentMembership)){
			PacketPaymentMembershipDetail packetPaymentMembershipDetail=new PacketPaymentMembershipDetail();
			packetPaymentMembershipDetail.setPayDetId(payDetId);
			return paymentFacadeDao.isMembershipPaymentDetailCanDelete(packetPaymentMembershipDetail);
		}
		return true;
	}

	@Override
	public boolean canUserDelete(long userId) {
		if(paymentFacadeService.canUserDelete(userId)){
			return paymentFacadeDao.isMembershipUserCanDelete(userId);
		}else{
			return false;
		}
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
		List<PacketPaymentFactory> packetPaymentFactories= paymentFacadeDao.getMembershipPaymentToUserId(userId);
		packetPaymentFactoriesInChain.addAll(packetPaymentFactories);
				
		return packetPaymentFactoriesInChain;
	}

	

}
