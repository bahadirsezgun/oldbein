package tr.com.abasus.ptboss.bonus.facadeService;

import java.util.Date;

public interface UserBonusPaymentFacadeService {

	
	public boolean isLockBonusPayedClasses(long schtId);
	
	public boolean isLockBonusPayedToDates(Date timePlanDate,long schtStaffId) ;
	
	
}
