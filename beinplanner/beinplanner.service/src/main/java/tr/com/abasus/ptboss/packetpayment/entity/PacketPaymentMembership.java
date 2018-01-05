package tr.com.abasus.ptboss.packetpayment.entity;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonTypeName;

import tr.com.abasus.ptboss.packetpayment.service.PacketPaymentService;
import tr.com.abasus.ptboss.packetsale.service.PacketSaleService;
import tr.com.abasus.ptboss.result.entity.HmiResultObj;
import tr.com.abasus.util.GlobalUtil;
import tr.com.abasus.util.OhbeUtil;

@Component(value="packetPaymentMembership")
@Scope("prototype")
@JsonTypeName("ppm")
public class PacketPaymentMembership extends PacketPaymentFactory {

	@Autowired
	PacketPaymentService packetPaymentService;
	
	@Autowired
	PacketSaleService packetSaleService;
	
	
	
	
	
	@Override
	public String getType() {
		return "ppm";
	}
	
	@Override
	public PacketPaymentFactory findPacketPaymentByPayId(long payId) {
		return packetPaymentService.findPacketPaymentMembershipByPayId(payId);
	}
	
	
	@Override
	public PacketPaymentFactory findPacketPaymentBySaleId(long saleId) {
		PacketPaymentFactory packetPaymentFactory=packetPaymentService.findPacketPaymentMembership(saleId);
		return packetPaymentFactory;
	}

	@Override
	public HmiResultObj createPacketPayment(PacketPaymentFactory packetPaymentFactory) {
		PacketPaymentMembership packetPaymentMembership=(PacketPaymentMembership)packetPaymentFactory;
		packetPaymentMembership.setPayDate(OhbeUtil.getThatDayFormatNotNull(packetPaymentMembership.getPayDateStr(), GlobalUtil.global.getPtDbDateFormat()));
		packetPaymentMembership.setChangeDate(GlobalUtil.getCurrentDateByTimeZone());
		HmiResultObj hmiResultObj=new HmiResultObj();
		if(packetPaymentMembership.getPayId()==0){
			hmiResultObj=packetPaymentService.createPacketPaymentMembership(packetPaymentMembership);
		
			PacketPaymentMembershipDetail packetPaymentMembershipDetail=new PacketPaymentMembershipDetail();
			packetPaymentMembershipDetail.setChangeDate(GlobalUtil.getCurrentDateByTimeZone());
			packetPaymentMembershipDetail.setPayAmount(packetPaymentMembership.getPayAmount());
			packetPaymentMembershipDetail.setPayDate(packetPaymentMembership.getPayDate());
			packetPaymentMembershipDetail.setPayId(packetPaymentMembership.getPayId());
			packetPaymentMembershipDetail.setPayComment(packetPaymentMembership.getPayComment());
			packetPaymentMembershipDetail.setPayType(packetPaymentMembership.getPayType());
			
			packetPaymentService.createPacketPaymentMembershipDetail(packetPaymentMembershipDetail);
		}else{
		
			List<PacketPaymentMembershipDetail> packetPaymentMembershipDetails=packetPaymentService.findPacketPaymentMembershipDetail(packetPaymentMembership.getPayId());
			double totalAmount=packetPaymentMembership.getPayAmount();
		    for (PacketPaymentMembershipDetail packetPaymentMembershipDetail : packetPaymentMembershipDetails) {
				 totalAmount+=packetPaymentMembershipDetail.getPayAmount();
			}
		
		    
		    PacketPaymentMembershipDetail packetPaymentMembershipDetail=new PacketPaymentMembershipDetail();
			packetPaymentMembershipDetail.setChangeDate(GlobalUtil.getCurrentDateByTimeZone());
			packetPaymentMembershipDetail.setPayAmount(packetPaymentMembership.getPayAmount());
			packetPaymentMembershipDetail.setPayDate(packetPaymentMembership.getPayDate());
			packetPaymentMembershipDetail.setPayId(packetPaymentMembership.getPayId());
			packetPaymentMembershipDetail.setPayComment(packetPaymentMembership.getPayComment());
			packetPaymentMembershipDetail.setPayType(packetPaymentMembership.getPayType());
			
			packetPaymentService.createPacketPaymentMembershipDetail(packetPaymentMembershipDetail);
		    
			packetPaymentMembership.setPayAmount(totalAmount);
		    hmiResultObj=packetPaymentService.createPacketPaymentMembership(packetPaymentMembership);
		    
		}
		
		return hmiResultObj;
	}

	@Override
	public synchronized HmiResultObj updatePacketPayment(PacketPaymentFactory packetPaymentFactory) {
		return packetPaymentService.createPacketPaymentMembership((PacketPaymentMembership)packetPaymentFactory);
	}
	
	@Override
	public synchronized HmiResultObj deletePacketPayment(long payId) {
		PacketPaymentMembership packetPaymentMembership=new PacketPaymentMembership();
		packetPaymentMembership.setPayId(payId);
		return packetPaymentService.deletePacketPaymentMembership(packetPaymentMembership);
	}

	@Override
	public synchronized HmiResultObj deletePacketPaymentDetail(long payDetId) {
		return packetPaymentService.deletePacketPaymentMembershipDetail(payDetId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public synchronized List<PacketPaymentDetailFactory> findPacketPaymentDetail(long payId) {
		List<? extends PacketPaymentDetailFactory> packetPaymentDetailFactories=packetPaymentService.findPacketPaymentMembershipDetail(payId);
		return (List<PacketPaymentDetailFactory>)packetPaymentDetailFactories;
	}

	@Override
	public List<PacketPaymentFactory> findPaymentToConfirm(String userName, String userSurname, int confimed,
			int unConfirmed) {
		List<PacketPaymentFactory> ppms= packetPaymentService.findPaymentMembershipToConfirm(userName, userSurname, confimed, unConfirmed);
		return ppms;
	}

	@Override
	public double findTotalIncomePaymentInMonth(int firmId, int month,int year) {
		return packetPaymentService.findTotalIncomePaymentInMonthForMembership(firmId, month,year);
	}

	@Override
	public HmiResultObj updatePacketPaymentDetail(PacketPaymentDetailFactory packetPaymentDetailFactory) {
		return packetPaymentService.createPacketPaymentMembershipDetail((PacketPaymentMembershipDetail)packetPaymentDetailFactory);
	}

	@Override
	public List<PacketPaymentFactory> findIncomePaymentInMonth(int firmId, int month, int year) {
		
		return packetPaymentService.findIncomePaymentInMonthForMembership(firmId, month, year);
	}

	@Override
	public List<PacketPaymentFactory> findIncomePaymentInDate(int firmId, Date startDate, Date endDate) {
		return packetPaymentService.findIncomePaymentInDateForMembership(firmId, startDate, endDate);
	}

	@Override
	public List<PacketPaymentFactory> findPaymentInGroupDate(int firmId) {
		return packetPaymentService.findPaymentInGroupDateForMembership(firmId);
	}
	
	
}
