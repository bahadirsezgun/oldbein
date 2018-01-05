package tr.com.abasus.ptboss.packetpayment.entity;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonTypeName;

import tr.com.abasus.ptboss.packetpayment.service.PacketPaymentService;
import tr.com.abasus.ptboss.packetsale.entity.PacketSalePersonal;
import tr.com.abasus.ptboss.packetsale.service.PacketSaleService;
import tr.com.abasus.ptboss.result.entity.HmiResultObj;
import tr.com.abasus.util.GlobalUtil;
import tr.com.abasus.util.OhbeUtil;

@Component(value="packetPaymentPersonal")
@Scope("prototype")
@JsonTypeName("ppp")
public class PacketPaymentPersonal extends PacketPaymentFactory {

	
	@Autowired
	PacketPaymentService packetPaymentService;
	
	@Autowired
	PacketSaleService packetSaleService;
	
	
	@Autowired
	@Qualifier(value="packetPaymentClass")
	PacketPaymentFactory packetPaymentFactory;
	
	
	
	
	@Override
	public String getType() {
		return "ppp";
	}
	
	@Override
	public PacketPaymentFactory findPacketPaymentByPayId(long payId) {
		return packetPaymentService.findPacketPaymentPersonalByPayId(payId);
	}
	
	
	@Override
	public PacketPaymentFactory findPacketPaymentBySaleId(long saleId) {
		PacketPaymentFactory packetPaymentFactory=packetPaymentService.findPacketPaymentPersonal(saleId);
		return packetPaymentFactory;
	}


	@Override
	public HmiResultObj createPacketPayment(PacketPaymentFactory packetPaymentFactory) {
		
		PacketPaymentPersonal packetPaymentPersonal=(PacketPaymentPersonal)packetPaymentFactory;
		packetPaymentPersonal.setPayDate(OhbeUtil.getThatDayFormatNotNull(packetPaymentPersonal.getPayDateStr(), GlobalUtil.global.getPtDbDateFormat()));
		packetPaymentPersonal.setChangeDate(GlobalUtil.getCurrentDateByTimeZone());
		HmiResultObj hmiResultObj=new HmiResultObj();
		if(packetPaymentPersonal.getPayId()==0){
			hmiResultObj=packetPaymentService.createPacketPaymentPersonal(packetPaymentPersonal);
		
			PacketPaymentPersonalDetail packetPaymentPersonalDetail=new PacketPaymentPersonalDetail();
			packetPaymentPersonalDetail.setChangeDate(GlobalUtil.getCurrentDateByTimeZone());
			packetPaymentPersonalDetail.setPayAmount(packetPaymentPersonal.getPayAmount());
			packetPaymentPersonalDetail.setPayDate(packetPaymentPersonal.getPayDate());
			packetPaymentPersonalDetail.setPayId(packetPaymentPersonal.getPayId());
			packetPaymentPersonalDetail.setPayComment(packetPaymentPersonal.getPayComment());
			packetPaymentPersonalDetail.setPayType(packetPaymentPersonal.getPayType());
			packetPaymentService.createPacketPaymentPersonalDetail(packetPaymentPersonalDetail);
		}else{
		
			List<PacketPaymentPersonalDetail> packetPaymentPersonalDetails=packetPaymentService.findPacketPaymentPersonalDetail(packetPaymentPersonal.getPayId());
			double totalAmount=packetPaymentPersonal.getPayAmount();
		    for (PacketPaymentPersonalDetail packetPaymentPersonalDetail : packetPaymentPersonalDetails) {
				 totalAmount+=packetPaymentPersonalDetail.getPayAmount();
			}
		
		    
		    PacketPaymentPersonalDetail packetPaymentPersonalDetail=new PacketPaymentPersonalDetail();
			packetPaymentPersonalDetail.setChangeDate(GlobalUtil.getCurrentDateByTimeZone());
			packetPaymentPersonalDetail.setPayAmount(packetPaymentPersonal.getPayAmount());
			packetPaymentPersonalDetail.setPayDate(packetPaymentPersonal.getPayDate());
			packetPaymentPersonalDetail.setPayId(packetPaymentPersonal.getPayId());
			packetPaymentPersonalDetail.setPayComment(packetPaymentPersonal.getPayComment());
			packetPaymentPersonalDetail.setPayType(packetPaymentPersonal.getPayType());
			
			packetPaymentService.createPacketPaymentPersonalDetail(packetPaymentPersonalDetail);
		    
			packetPaymentPersonal.setPayAmount(totalAmount);
		    hmiResultObj=packetPaymentService.createPacketPaymentPersonal(packetPaymentPersonal);
		    
		}
		
		return hmiResultObj;
	}

	
	@Override
	public synchronized HmiResultObj updatePacketPayment(PacketPaymentFactory packetPaymentFactory) {
		return packetPaymentService.createPacketPaymentPersonal((PacketPaymentPersonal)packetPaymentFactory);
	}

	@Override
	public synchronized HmiResultObj deletePacketPayment(long payId) {
		PacketPaymentPersonal packetPaymentPersonal=new PacketPaymentPersonal();
		packetPaymentPersonal.setPayId(payId);
		return packetPaymentService.deletePacketPaymentPersonal(packetPaymentPersonal);
	}


	@Override
	public synchronized HmiResultObj deletePacketPaymentDetail(long payDetId) {
		return packetPaymentService.deletePacketPaymentPersonalDetail(payDetId);
	}

	
	@SuppressWarnings("unchecked")
	@Override
	public synchronized List<PacketPaymentDetailFactory> findPacketPaymentDetail(long payId) {
		List<? extends PacketPaymentDetailFactory> packetPaymentDetailFactories=packetPaymentService.findPacketPaymentPersonalDetail(payId);
		return (List<PacketPaymentDetailFactory>)packetPaymentDetailFactories;
	}

	public PacketPaymentService getPacketPaymentService() {
		return packetPaymentService;
	}


	public void setPacketPaymentService(PacketPaymentService packetPaymentService) {
		this.packetPaymentService = packetPaymentService;
	}


	public PacketSaleService getPacketSaleService() {
		return packetSaleService;
	}


	public void setPacketSaleService(PacketSaleService packetSaleService) {
		this.packetSaleService = packetSaleService;
	}

	@Override
	public List<PacketPaymentFactory> findPaymentToConfirm(String userName, String userSurname, int confimed,
			int unConfirmed) {
		
		List<PacketPaymentFactory> packetPaymentFactories=packetPaymentFactory.findPaymentToConfirm(userName, userSurname, confimed, unConfirmed);
		
		List<PacketPaymentFactory> ppps= packetPaymentService.findPaymentPersonalToConfirm(userName, userSurname, confimed, unConfirmed);
		ppps.addAll(packetPaymentFactories);
		
		return ppps;
	}

	@Override
	public double findTotalIncomePaymentInMonth(int firmId, int month,int year) {
		double incomePaymentOfClassAndMembership=packetPaymentFactory.findTotalIncomePaymentInMonth(firmId, month,year);
		double incomePaymentOfPersonal= packetPaymentService.findTotalIncomePaymentInMonthForPersonal(firmId, month,year);
		
		double totalPayment=incomePaymentOfClassAndMembership+incomePaymentOfPersonal;
		
		return totalPayment;
	}

	

	@Override
	public HmiResultObj updatePacketPaymentDetail(PacketPaymentDetailFactory packetPaymentDetailFactory) {
		return packetPaymentService.createPacketPaymentPersonalDetail((PacketPaymentPersonalDetail)packetPaymentDetailFactory);
	}

	@Override
	public List<PacketPaymentFactory> findIncomePaymentInMonth(int firmId, int month, int year) {
		
		List<PacketPaymentFactory> packetPaymentFactories=packetPaymentFactory.findIncomePaymentInMonth(firmId, month, year);
		
		List<PacketPaymentFactory> paymentFactories=packetPaymentService.findIncomePaymentInMonthForPersonal(firmId, month, year);
		
		paymentFactories.addAll(packetPaymentFactories);
		
		return paymentFactories;
	}

	@Override
	public List<PacketPaymentFactory> findIncomePaymentInDate(int firmId, Date startDate, Date endDate) {
		List<PacketPaymentFactory> incomePaymentOfClassAndMembership=packetPaymentFactory.findIncomePaymentInDate(firmId, startDate,endDate);
		List<PacketPaymentFactory> incomePaymentOfPersonal= packetPaymentService.findIncomePaymentInDateForPersonal(firmId, startDate, endDate);
		
		incomePaymentOfPersonal.addAll(incomePaymentOfClassAndMembership);
		
		return incomePaymentOfPersonal;
	}

	@Override
	public List<PacketPaymentFactory> findPaymentInGroupDate(int firmId) {
		List<PacketPaymentFactory> incomePaymentOfClassAndMembership=packetPaymentFactory.findPaymentInGroupDate(firmId);
		List<PacketPaymentFactory> incomePaymentOfPersonal= packetPaymentService.findPaymentInGroupDateForPersonal(firmId);
		
		incomePaymentOfPersonal.addAll(incomePaymentOfClassAndMembership);
		
		return incomePaymentOfPersonal;
	}
	

	
	
}
