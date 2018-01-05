package tr.com.abasus.ptboss.packetpayment.service;

import java.util.Date;
import java.util.List;

import tr.com.abasus.ptboss.packetpayment.dao.PacketPaymentDao;
import tr.com.abasus.ptboss.packetpayment.entity.PacketPaymentClass;
import tr.com.abasus.ptboss.packetpayment.entity.PacketPaymentClassDetail;
import tr.com.abasus.ptboss.packetpayment.entity.PacketPaymentDetailFactory;
import tr.com.abasus.ptboss.packetpayment.entity.PacketPaymentFactory;
import tr.com.abasus.ptboss.packetpayment.entity.PacketPaymentMembership;
import tr.com.abasus.ptboss.packetpayment.entity.PacketPaymentMembershipDetail;
import tr.com.abasus.ptboss.packetpayment.entity.PacketPaymentPersonal;
import tr.com.abasus.ptboss.packetpayment.entity.PacketPaymentPersonalDetail;
import tr.com.abasus.ptboss.result.entity.HmiResultObj;

public class PacketPaymentServiceImpl implements PacketPaymentService {

	PacketPaymentDao packetPaymentDao;

	@SuppressWarnings("unchecked")
	@Override
	public PacketPaymentFactory findPacketPaymentPersonal(long salesId) {
		PacketPaymentFactory packetPaymentFactory=packetPaymentDao.findPacketPaymentPersonal(salesId);
		if(packetPaymentFactory!=null){
			List<PacketPaymentPersonalDetail> packetPaymentPersonalDetails=findPacketPaymentPersonalDetail(packetPaymentFactory.getPayId());
			List<? extends PacketPaymentDetailFactory> packetPaymentDetailFactories=packetPaymentPersonalDetails;
			packetPaymentFactory.setPacketPaymentDetailFactories((List<PacketPaymentDetailFactory>)packetPaymentDetailFactories);;
		}
		return packetPaymentFactory;
	}

	@Override
	public List<PacketPaymentPersonalDetail> findPacketPaymentPersonalDetail(long saleId) {
		
		return packetPaymentDao.findPacketPaymentPersonalDetail(saleId);
	}

	@Override
	public synchronized HmiResultObj createPacketPaymentPersonal(PacketPaymentPersonal packetPaymentPersonal) {
		return packetPaymentDao.createPacketPaymentPersonal(packetPaymentPersonal);
	}

	@SuppressWarnings("unchecked")
	@Override
	public PacketPaymentFactory findPacketPaymentClass(long salesId) {
		PacketPaymentFactory packetPaymentFactory=packetPaymentDao.findPacketPaymentClass(salesId);
		if(packetPaymentFactory!=null){
			List<PacketPaymentClassDetail> packetPaymentClassDetails=findPacketPaymentClassDetail(packetPaymentFactory.getPayId());
			List<? extends PacketPaymentDetailFactory> packetPaymentDetailFactories=packetPaymentClassDetails;
			packetPaymentFactory.setPacketPaymentDetailFactories((List<PacketPaymentDetailFactory>)packetPaymentDetailFactories);
		}
		
		return packetPaymentFactory;
	}

	@Override
	public List<PacketPaymentClassDetail> findPacketPaymentClassDetail(long saleId) {
		
		return packetPaymentDao.findPacketPaymentClassDetail(saleId);
	}

	@Override
	public HmiResultObj createPacketPaymentClass(PacketPaymentClass packetPaymentClass) {
		
		return packetPaymentDao.createPacketPaymentClass(packetPaymentClass);
	}

	@SuppressWarnings("unchecked")
	@Override
	public PacketPaymentFactory findPacketPaymentMembership(long salesId) {
		
		PacketPaymentFactory packetPaymentFactory=packetPaymentDao.findPacketPaymentMembership(salesId);
		if(packetPaymentFactory!=null){
			List<PacketPaymentMembershipDetail> packetPaymentMembershipDetails=findPacketPaymentMembershipDetail(packetPaymentFactory.getPayId());
			List<? extends PacketPaymentDetailFactory> packetPaymentDetailFactories=packetPaymentMembershipDetails;
			packetPaymentFactory.setPacketPaymentDetailFactories((List<PacketPaymentDetailFactory>)packetPaymentDetailFactories);
		}
		
		return packetPaymentFactory;
	}

	@Override
	public List<PacketPaymentMembershipDetail> findPacketPaymentMembershipDetail(long saleId) {
		
		return packetPaymentDao.findPacketPaymentMembershipDetail(saleId);
	}

	@Override
	public HmiResultObj createPacketPaymentMembership(PacketPaymentMembership packetPaymentPersonal) {
		
		return packetPaymentDao.createPacketPaymentMembership(packetPaymentPersonal);
	}

	@Override
	public HmiResultObj createPacketPaymentPersonalDetail(PacketPaymentPersonalDetail packetPaymentPersonalDetail) {
		return packetPaymentDao.createPacketPaymentPersonalDetail(packetPaymentPersonalDetail);
	}

	@Override
	public HmiResultObj deletePacketPaymentPersonal(PacketPaymentPersonal packetPaymentPersonal) {
		return packetPaymentDao.deletePacketPaymentPersonal(packetPaymentPersonal);
	}

	@Override
	public HmiResultObj createPacketPaymentClassDetail(PacketPaymentClassDetail packetPaymentClassDetail) {
		return packetPaymentDao.createPacketPaymentClassDetail(packetPaymentClassDetail);
	}

	@Override
	public HmiResultObj deletePacketPaymentClass(PacketPaymentClass packetPaymentClass) {
		return packetPaymentDao.deletePacketPaymentClass(packetPaymentClass);
	}

	@Override
	public HmiResultObj createPacketPaymentMembershipDetail(
			PacketPaymentMembershipDetail packetPaymentMembershipDetail) {
		return packetPaymentDao.createPacketPaymentMembershipDetail(packetPaymentMembershipDetail);
	}

	@Override
	public HmiResultObj deletePacketPaymentMembership(PacketPaymentMembership packetPaymentMembership) {
		return packetPaymentDao.deletePacketPaymentMembership(packetPaymentMembership);
	}

	@Override
	public HmiResultObj deletePacketPaymentPersonalDetail(long payDetId) {
		return packetPaymentDao.deletePacketPaymentPersonalDetail(payDetId);
	}

	@Override
	public HmiResultObj deletePacketPaymentClassDetail(long payDetId) {
		return packetPaymentDao.deletePacketPaymentClassDetail(payDetId);
	}

	@Override
	public HmiResultObj deletePacketPaymentMembershipDetail(long payDetId) {
		return packetPaymentDao.deletePacketPaymentMembershipDetail(payDetId);
	}

	public PacketPaymentDao getPacketPaymentDao() {
		return packetPaymentDao;
	}

	public void setPacketPaymentDao(PacketPaymentDao packetPaymentDao) {
		this.packetPaymentDao = packetPaymentDao;
	}

	@Override
	public PacketPaymentFactory findPacketPaymentPersonalByPayId(long payId) {
		
		PacketPaymentFactory packetPaymentFactory=packetPaymentDao.findPacketPaymentPersonalByPayId(payId);
		if(packetPaymentFactory!=null){
			List<PacketPaymentPersonalDetail> packetPaymentPersonalDetails=findPacketPaymentPersonalDetail(packetPaymentFactory.getPayId());
			List<? extends PacketPaymentDetailFactory> packetPaymentDetailFactories=packetPaymentPersonalDetails;
			packetPaymentFactory.setPacketPaymentDetailFactories((List<PacketPaymentDetailFactory>)packetPaymentDetailFactories);;
		}
		
		
		return packetPaymentFactory;
	}

	@Override
	public PacketPaymentFactory findPacketPaymentClassByPayId(long payId) {
		
		PacketPaymentFactory packetPaymentFactory=packetPaymentDao.findPacketPaymentClassByPayId(payId);
		if(packetPaymentFactory!=null){
			List<PacketPaymentPersonalDetail> packetPaymentPersonalDetails=findPacketPaymentPersonalDetail(packetPaymentFactory.getPayId());
			List<? extends PacketPaymentDetailFactory> packetPaymentDetailFactories=packetPaymentPersonalDetails;
			packetPaymentFactory.setPacketPaymentDetailFactories((List<PacketPaymentDetailFactory>)packetPaymentDetailFactories);;
		}
		
		
		return packetPaymentFactory;
	}

	@Override
	public PacketPaymentFactory findPacketPaymentMembershipByPayId(long payId) {
		PacketPaymentFactory packetPaymentFactory=packetPaymentDao.findPacketPaymentMembershipByPayId(payId);
		if(packetPaymentFactory!=null){
			List<PacketPaymentPersonalDetail> packetPaymentPersonalDetails=findPacketPaymentPersonalDetail(packetPaymentFactory.getPayId());
			List<? extends PacketPaymentDetailFactory> packetPaymentDetailFactories=packetPaymentPersonalDetails;
			packetPaymentFactory.setPacketPaymentDetailFactories((List<PacketPaymentDetailFactory>)packetPaymentDetailFactories);;
		}
		
		
		return packetPaymentFactory;
	}

	@Override
	public List<PacketPaymentFactory> findPaymentPersonalToConfirm(String userName, String userSurname, int confimed,
			int unConfirmed) {
		return packetPaymentDao.findPaymentPersonalToConfirm(userName, userSurname, confimed, unConfirmed);
	}

	@Override
	public List<PacketPaymentFactory> findPaymentClassToConfirm(String userName, String userSurname, int confimed,
			int unConfirmed) {
		return packetPaymentDao.findPaymentClassToConfirm(userName, userSurname, confimed, unConfirmed);
	}

	@Override
	public List<PacketPaymentFactory> findPaymentMembershipToConfirm(String userName, String userSurname, int confimed,
			int unConfirmed) {
		return packetPaymentDao.findPaymentMembershipToConfirm(userName, userSurname, confimed, unConfirmed);
	}

	@Override
	public double findTotalIncomePaymentInMonthForPersonal(int firmId, int month,int year) {
		return packetPaymentDao.findTotalIncomePaymentInMonthForPersonal(firmId, month,year);
	}

	@Override
	public double findTotalIncomePaymentInMonthClass(int firmId, int month,int year) {
		return packetPaymentDao.findTotalIncomePaymentInMonthForClass(firmId, month,year);
	}

	@Override
	public double findTotalIncomePaymentInMonthForMembership(int firmId, int month,int year) {
		return packetPaymentDao.findTotalIncomePaymentInMonthForMembership(firmId, month,year);
	}

	@Override
	public List<PacketPaymentFactory> findIncomePaymentInMonthForPersonal(int firmId, int month, int year) {
		return packetPaymentDao.findIncomePaymentInMonthForPersonal(firmId, month, year);
	}

	@Override
	public List<PacketPaymentFactory> findIncomePaymentInMonthForClass(int firmId, int month, int year) {
		return packetPaymentDao.findIncomePaymentInMonthForClass(firmId, month, year);
	}

	@Override
	public List<PacketPaymentFactory> findIncomePaymentInMonthForMembership(int firmId, int month, int year) {
		return packetPaymentDao.findIncomePaymentInMonthForMembership(firmId, month, year);
	}

	@Override
	public List<PacketPaymentFactory> findIncomePaymentInDateForPersonal(int firmId,Date startDate,Date endDate) {
		return packetPaymentDao.findIncomePaymentInDateForPersonal(firmId, startDate, endDate);
	}

	@Override
	public List<PacketPaymentFactory> findIncomePaymentInDateForClass(int firmId,Date startDate,Date endDate) {
		return packetPaymentDao.findIncomePaymentInDateForClass(firmId, startDate, endDate);
	}

	@Override
	public List<PacketPaymentFactory> findIncomePaymentInDateForMembership(int firmId,Date startDate,Date endDate) {
		return packetPaymentDao.findIncomePaymentInDateForMembership(firmId, startDate, endDate);
	}

	@Override
	public List<PacketPaymentFactory> findPaymentInGroupDateForPersonal(int firmId) {
		return packetPaymentDao.findPaymentInGroupDateForPersonal(firmId);
	}

	@Override
	public List<PacketPaymentFactory> findPaymentInGroupDateForClass(int firmId) {
		return packetPaymentDao.findPaymentInGroupDateForClass(firmId);
	}

	@Override
	public List<PacketPaymentFactory> findPaymentInGroupDateForMembership(int firmId) {
		return packetPaymentDao.findPaymentInGroupDateForMembership(firmId);
	}
	
	
	
	
	
	
	
	
}
