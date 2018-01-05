package tr.com.abasus.ptboss.facade.dao;

import java.util.List;

import tr.com.abasus.ptboss.packetpayment.entity.PacketPaymentClass;
import tr.com.abasus.ptboss.packetpayment.entity.PacketPaymentClassDetail;
import tr.com.abasus.ptboss.packetpayment.entity.PacketPaymentFactory;
import tr.com.abasus.ptboss.packetpayment.entity.PacketPaymentMembership;
import tr.com.abasus.ptboss.packetpayment.entity.PacketPaymentMembershipDetail;
import tr.com.abasus.ptboss.packetpayment.entity.PacketPaymentPersonal;
import tr.com.abasus.ptboss.packetpayment.entity.PacketPaymentPersonalDetail;
import tr.com.abasus.ptboss.ptuser.entity.User;

public interface PaymentFacadeDao {

	public boolean isPersonalPaymentCanDelete(PacketPaymentPersonal packetPaymentPersonal);
	
	public boolean isMembershipPaymentCanDelete(PacketPaymentMembership packetPaymentMembership);
	
	public boolean isClassPaymentCanDelete(PacketPaymentClass packetPaymentClass);
	
	public boolean isClassUserCanDelete(long userId);
	
	public boolean isPsersonalUserCanDelete(long userId);
	
	public boolean isMembershipUserCanDelete(long userId);
	
	
	
	
	
	public boolean isPersonalPaymentDetailCanDelete(PacketPaymentPersonalDetail pppd);
	
	public boolean isMembershipPaymentDetailCanDelete(PacketPaymentMembershipDetail ppmd);
	
	public boolean isClassPaymentDetailCanDelete(PacketPaymentClassDetail ppcd);
	
	
	
	
	public boolean isPersonalPaymentCanChange(PacketPaymentPersonal packetPaymentPersonal);
	
	public boolean isMembershipPaymentCanChange(PacketPaymentMembership packetPaymentMembership);
	
	public boolean isClassPaymentCanChange(PacketPaymentClass packetPaymentClass);
	
	
    public boolean havePersonalPaymentForSale(long saleId);
	
	public boolean haveMembershipPaymentForSale(long saleId);
	
	public boolean haveClassPaymentForSale(long saleId);
	
	
	
	public List<PacketPaymentFactory> getPersonalPaymentToUserId(long userId);
	
	public List<PacketPaymentFactory> getMembershipPaymentToUserId(long userId);
	
	public List<PacketPaymentFactory> getClassPaymentToUserId(long userId);
	
	
	
}
