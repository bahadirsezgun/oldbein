package tr.com.abasus.ptboss.packetpayment.service;

import java.util.Date;
import java.util.List;

import tr.com.abasus.ptboss.packetpayment.entity.PacketPaymentClass;
import tr.com.abasus.ptboss.packetpayment.entity.PacketPaymentClassDetail;
import tr.com.abasus.ptboss.packetpayment.entity.PacketPaymentFactory;
import tr.com.abasus.ptboss.packetpayment.entity.PacketPaymentMembership;
import tr.com.abasus.ptboss.packetpayment.entity.PacketPaymentMembershipDetail;
import tr.com.abasus.ptboss.packetpayment.entity.PacketPaymentPersonal;
import tr.com.abasus.ptboss.packetpayment.entity.PacketPaymentPersonalDetail;
import tr.com.abasus.ptboss.result.entity.HmiResultObj;

public interface PacketPaymentService {

	public PacketPaymentFactory findPacketPaymentPersonal(long salesId);
	
	public PacketPaymentFactory findPacketPaymentPersonalByPayId(long payId);
	
	public List<PacketPaymentPersonalDetail> findPacketPaymentPersonalDetail(long payId);
	
	public HmiResultObj createPacketPaymentPersonal(PacketPaymentPersonal packetPaymentPersonal);
	
	public HmiResultObj createPacketPaymentPersonalDetail(PacketPaymentPersonalDetail packetPaymentPersonalDetail);
	
	public HmiResultObj deletePacketPaymentPersonal(PacketPaymentPersonal packetPaymentPersonal);
	
	public HmiResultObj deletePacketPaymentPersonalDetail(long payDetId);
	
	public  List<PacketPaymentFactory>	findPaymentPersonalToConfirm(String userName,String userSurname,int confimed,int unConfirmed);
	
	public double findTotalIncomePaymentInMonthForPersonal(int firmId,int month,int year);
	
	public  List<PacketPaymentFactory>	findIncomePaymentInMonthForPersonal(int firmId,int month,int year);
	
	public  List<PacketPaymentFactory>	findIncomePaymentInDateForPersonal(int firmId,Date startDate,Date endDate);
	
	public  List<PacketPaymentFactory>	findPaymentInGroupDateForPersonal(int firmId);
	
	
	
	public PacketPaymentFactory findPacketPaymentClass(long salesId);
	
	public PacketPaymentFactory findPacketPaymentClassByPayId(long payId);
	
	public List<PacketPaymentClassDetail> findPacketPaymentClassDetail(long payDetId);
	
	public HmiResultObj createPacketPaymentClass(PacketPaymentClass packetPaymentClass);
	
	public HmiResultObj createPacketPaymentClassDetail(PacketPaymentClassDetail packetPaymentClassDetail);
	
	public HmiResultObj deletePacketPaymentClass(PacketPaymentClass packetPaymentClass);
	
	public HmiResultObj deletePacketPaymentClassDetail(long payDetId);
	
	public  List<PacketPaymentFactory>	findPaymentClassToConfirm(String userName,String userSurname,int confimed,int unConfirmed);
	
	public double findTotalIncomePaymentInMonthClass(int firmId,int month,int year);
	
	public  List<PacketPaymentFactory>	findIncomePaymentInMonthForClass(int firmId,int month,int year);
	
	public  List<PacketPaymentFactory>	findIncomePaymentInDateForClass(int firmId,Date startDate,Date endDate);
	
	public  List<PacketPaymentFactory>	findPaymentInGroupDateForClass(int firmId);
	
	
	
	public PacketPaymentFactory findPacketPaymentMembership(long salesId);
	
	public PacketPaymentFactory findPacketPaymentMembershipByPayId(long payId);
	
	public List<PacketPaymentMembershipDetail> findPacketPaymentMembershipDetail(long payDetId);
	
	public HmiResultObj createPacketPaymentMembership(PacketPaymentMembership packetPaymentPersonal);
	
	public HmiResultObj createPacketPaymentMembershipDetail(PacketPaymentMembershipDetail packetPaymentMembershipDetail);
	
	public HmiResultObj deletePacketPaymentMembership(PacketPaymentMembership packetPaymentMembership);
	
	public HmiResultObj deletePacketPaymentMembershipDetail(long payDetId);
	
	public  List<PacketPaymentFactory>	findPaymentMembershipToConfirm(String userName,String userSurname,int confimed,int unConfirmed);
	
	public double findTotalIncomePaymentInMonthForMembership(int firmId,int month,int year);
	
	public  List<PacketPaymentFactory>	findIncomePaymentInMonthForMembership(int firmId,int month,int year);
	
	public  List<PacketPaymentFactory>	findIncomePaymentInDateForMembership(int firmId,Date startDate,Date endDate);
	
	public  List<PacketPaymentFactory>	findPaymentInGroupDateForMembership(int firmId);
	
}
