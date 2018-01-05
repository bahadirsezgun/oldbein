package tr.com.abasus.ptboss.packetpayment.entity;

import java.util.Date;
import java.util.List;

import tr.com.abasus.ptboss.result.entity.HmiResultObj;

public interface PacketPaymentInteface {

	
	public  PacketPaymentFactory	findPacketPaymentBySaleId(long saleId);
	
	public  PacketPaymentFactory	findPacketPaymentByPayId(long payId);
		
	public  HmiResultObj  createPacketPayment(PacketPaymentFactory packetPaymentFactory);
	
	public  HmiResultObj  updatePacketPayment(PacketPaymentFactory packetPaymentFactory);
	
	public  HmiResultObj  updatePacketPaymentDetail(PacketPaymentDetailFactory packetPaymentDetailFactory);
	
	public  HmiResultObj  deletePacketPayment(long payId);
	
	public  HmiResultObj  deletePacketPaymentDetail(long payDetId);
	
	public List<PacketPaymentDetailFactory> findPacketPaymentDetail(long payId);
	
	public  List<PacketPaymentFactory>	findPaymentToConfirm(String userName,String userSurname,int confimed,int unConfirmed);
	
	
	public double findTotalIncomePaymentInMonth(int firmId,int month,int year);
	
	public List<PacketPaymentFactory> findIncomePaymentInMonth(int firmId,int month,int year);
	
	public List<PacketPaymentFactory> findIncomePaymentInDate(int firmId,Date startDate,Date endDate);
	
	
	public List<PacketPaymentFactory> findPaymentInGroupDate(int firmId);
	
}
