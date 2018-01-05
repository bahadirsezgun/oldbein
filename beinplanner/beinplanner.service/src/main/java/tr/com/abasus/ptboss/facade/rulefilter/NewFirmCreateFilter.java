package tr.com.abasus.ptboss.facade.rulefilter;

import org.springframework.stereotype.Service;

@Service
public class NewFirmCreateFilter implements RuleFilterService {

	
	public boolean canDo(int firmId) {
		/*
		List<User> schedulerStaffs=processSchedulerStaff.findAll(firmId);
		int currentTeacherCount=schedulerStaffs.size();
		
		
		PtRestrictions ptRestrictions=GlobalUtil.ptRestrictions;
		
		int teacherCount=Integer.parseInt(ptRestrictions.getTeacherCount());
		
		if(teacherCount>currentTeacherCount){
			return true;
		}else{
			return false;
		}*/
		return false;
	}
	
}
