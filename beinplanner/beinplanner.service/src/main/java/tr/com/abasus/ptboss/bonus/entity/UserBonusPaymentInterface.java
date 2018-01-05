package tr.com.abasus.ptboss.bonus.entity;

import java.util.List;

import tr.com.abasus.ptboss.bonus.businessEntity.UserBonusSearchObj;
import tr.com.abasus.ptboss.bonus.businessEntity.UserPaymentObj;
import tr.com.abasus.ptboss.result.entity.HmiResultObj;

public interface UserBonusPaymentInterface {

	public HmiResultObj saveBonusPayment(UserBonusPaymentFactory userBonusPaymentFactory);
	
	public List<UserBonusPaymentFactory> findBonusPayment(UserBonusSearchObj userBonusSearchObj);
	
	public HmiResultObj deleteBonusPayment(UserBonusPaymentFactory userBonusPaymentFactory);
	
	public UserPaymentObj findUserPayment(long schtId);
	
	
	public List<UserBonusPaymentFactory> findBonusPaymentForStaffsByMonth(int month,int year,int firmId);
	
	
	public double findTotalOfMonth(int firmId,int month,int year);
	
	public List<UserBonusPaymentFactory> findBonusPaymentForToday(int firmId);
	
}
