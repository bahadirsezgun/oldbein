package tr.com.abasus.ptboss.bonus.businessService;

import java.util.Date;

import tr.com.abasus.ptboss.bonus.businessEntity.UserBonusObj;

public interface UserBonusCalculateService {

	
	public UserBonusObj findStaffBonusObj(long schStaffId,Date startDate,Date endDate);
	
	
}
