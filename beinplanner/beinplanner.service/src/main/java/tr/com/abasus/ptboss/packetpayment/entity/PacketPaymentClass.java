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

@Component(value="packetPaymentClass")
@Scope("prototype")
@JsonTypeName("ppc")
public class PacketPaymentClass extends PacketPaymentFactory {

	@Autowired
	PacketPaymentService packetPaymentService;
	
	@Autowired
	PacketSaleService packetSaleService;

	@Autowired
	@Qualifier(value="packetPaymentMembership")
	PacketPaymentFactory packetPaymentFactory;
	
	
	
	@Override
	public String getType() {
		return "ppc";
	}

	@Override
	public PacketPaymentFactory findPacketPaymentByPayId(long payId) {
		return packetPaymentService.findPacketPaymentClassByPayId(payId);
	}
	
	
	@Override
	public PacketPaymentFactory findPacketPaymentBySaleId(long saleId) {
		PacketPaymentFactory packetPaymentFactory=packetPaymentService.findPacketPaymentClass(saleId);
		return packetPaymentFactory;
	}

	@Override
	public HmiResultObj createPacketPayment(PacketPaymentFactory packetPaymentFactory) {
		PacketPaymentClass packetPaymentClass=(PacketPaymentClass)packetPaymentFactory;
		packetPaymentClass.setPayDate(OhbeUtil.getThatDayFormatNotNull(packetPaymentClass.getPayDateStr(), GlobalUtil.global.getPtDbDateFormat()));
		packetPaymentClass.setChangeDate(GlobalUtil.getCurrentDateByTimeZone());
		HmiResultObj hmiResultObj=new HmiResultObj();
		if(packetPaymentClass.getPayId()==0){
			hmiResultObj=packetPaymentService.createPacketPaymentClass(packetPaymentClass);
		
			PacketPaymentClassDetail packetPaymentClassDetail=new PacketPaymentClassDetail();
			packetPaymentClassDetail.setChangeDate(GlobalUtil.getCurrentDateByTimeZone());
			packetPaymentClassDetail.setPayAmount(packetPaymentClass.getPayAmount());
			packetPaymentClassDetail.setPayDate(packetPaymentClass.getPayDate());
			packetPaymentClassDetail.setPayId(packetPaymentClass.getPayId());
			packetPaymentClassDetail.setPayComment(packetPaymentClass.getPayComment());
			packetPaymentClassDetail.setPayType(packetPaymentClass.getPayType());
			
			packetPaymentService.createPacketPaymentClassDetail(packetPaymentClassDetail);
		}else{
		
			List<PacketPaymentClassDetail> packetPaymentClassDetails=packetPaymentService.findPacketPaymentClassDetail(packetPaymentClass.getPayId());
			double totalAmount=packetPaymentClass.getPayAmount();
		    for (PacketPaymentClassDetail packetPaymentClassDetail : packetPaymentClassDetails) {
				 totalAmount+=packetPaymentClassDetail.getPayAmount();
			}
		
		    
		    PacketPaymentClassDetail packetPaymentClassDetail=new PacketPaymentClassDetail();
			packetPaymentClassDetail.setChangeDate(GlobalUtil.getCurrentDateByTimeZone());
			packetPaymentClassDetail.setPayAmount(packetPaymentClass.getPayAmount());
			packetPaymentClassDetail.setPayDate(packetPaymentClass.getPayDate());
			packetPaymentClassDetail.setPayId(packetPaymentClass.getPayId());
			packetPaymentClassDetail.setPayComment(packetPaymentClass.getPayComment());
			packetPaymentClassDetail.setPayType(packetPaymentClass.getPayType());
			
			packetPaymentService.createPacketPaymentClassDetail(packetPaymentClassDetail);
		    
			packetPaymentClass.setPayAmount(totalAmount);
		    hmiResultObj=packetPaymentService.createPacketPaymentClass(packetPaymentClass);
		    
		}
		
		return hmiResultObj;
	}
	
	@Override
	public synchronized HmiResultObj updatePacketPayment(PacketPaymentFactory packetPaymentFactory) {
		return packetPaymentService.createPacketPaymentClass((PacketPaymentClass)packetPaymentFactory);
	}

	@Override
	public synchronized HmiResultObj deletePacketPayment(long payId) {
		PacketPaymentClass packetPaymentClass=new PacketPaymentClass();
		packetPaymentClass.setPayId(payId);
		return packetPaymentService.deletePacketPaymentClass(packetPaymentClass);
	}

	@Override
	public synchronized HmiResultObj deletePacketPaymentDetail(long payDetId) {
		return packetPaymentService.deletePacketPaymentClassDetail(payDetId);
	}

	
	@SuppressWarnings("unchecked")
	@Override
	public synchronized List<PacketPaymentDetailFactory> findPacketPaymentDetail(long payId) {
		List<? extends PacketPaymentDetailFactory> packetPaymentDetailFactories=packetPaymentService.findPacketPaymentClassDetail(payId);
		return (List<PacketPaymentDetailFactory>)packetPaymentDetailFactories;
	}

	@Override
	public List<PacketPaymentFactory> findPaymentToConfirm(String userName, String userSurname, int confimed,
			int unConfirmed) {
		List<PacketPaymentFactory> packetPaymentFactories=packetPaymentFactory.findPaymentToConfirm(userName, userSurname, confimed, unConfirmed);
		
		List<PacketPaymentFactory> ppcs= packetPaymentService.findPaymentClassToConfirm(userName, userSurname, confimed, unConfirmed);
		ppcs.addAll(packetPaymentFactories);
				
		
		return ppcs;
	}

	@Override
	public double findTotalIncomePaymentInMonth(int firmId, int month,int year) {
		double incomePaymentOfMembership=packetPaymentFactory.findTotalIncomePaymentInMonth(firmId, month,year);
		double incomePaymentOfClass= packetPaymentService.findTotalIncomePaymentInMonthClass(firmId, month,year);
		
		double totalPayment=incomePaymentOfMembership+incomePaymentOfClass;
		return totalPayment;
	}

	@Override
	public HmiResultObj updatePacketPaymentDetail(PacketPaymentDetailFactory packetPaymentDetailFactory) {
		return packetPaymentService.createPacketPaymentClassDetail((PacketPaymentClassDetail)packetPaymentDetailFactory);
	}

	@Override
	public List<PacketPaymentFactory> findIncomePaymentInMonth(int firmId, int month, int year) {
		List<PacketPaymentFactory> packetPaymentFactories=packetPaymentFactory.findIncomePaymentInMonth(firmId, month, year);
		
		List<PacketPaymentFactory> paymentFactories=packetPaymentService.findIncomePaymentInMonthForClass(firmId, month, year);
		
		paymentFactories.addAll(packetPaymentFactories);
		return paymentFactories;
	}

	@Override
	public List<PacketPaymentFactory> findIncomePaymentInDate(int firmId, Date startDate, Date endDate) {
		List<PacketPaymentFactory> incomePaymentOfMembership=packetPaymentFactory.findIncomePaymentInDate(firmId, startDate,endDate);
		List<PacketPaymentFactory> incomePaymentOfClass= packetPaymentService.findIncomePaymentInDateForClass(firmId, startDate, endDate);
		
		incomePaymentOfClass.addAll(incomePaymentOfMembership);
		
		return incomePaymentOfClass;
	}

	@Override
	public List<PacketPaymentFactory> findPaymentInGroupDate(int firmId) {
		List<PacketPaymentFactory> incomePaymentOfMembership=packetPaymentFactory.findPaymentInGroupDate(firmId);
		List<PacketPaymentFactory> incomePaymentOfClass= packetPaymentService.findPaymentInGroupDateForClass(firmId);
		
		incomePaymentOfClass.addAll(incomePaymentOfMembership);
		
		return incomePaymentOfClass;
	}

	
	


	
}
