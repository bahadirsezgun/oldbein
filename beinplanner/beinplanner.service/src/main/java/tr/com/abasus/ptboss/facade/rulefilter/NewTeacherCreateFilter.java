package tr.com.abasus.ptboss.facade.rulefilter;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tr.com.abasus.ptboss.ptuser.entity.User;
import tr.com.abasus.ptboss.ptuser.iuser.ProcessSchedulerStaff;
import tr.com.abasus.ptboss.settings.entity.PtRestrictions;
import tr.com.abasus.util.GlobalUtil;

@Service
public class NewTeacherCreateFilter implements RuleFilterService {

	@Autowired
	ProcessSchedulerStaff processSchedulerStaff;
	
	public synchronized boolean canDo(int firmId){
		PtRestrictions ptRestrictions=GlobalUtil.ptRestrictionsForCount;
		if(Integer.parseInt(ptRestrictions.getTeacherCount())==0){
			return true;
		}else{
			List<User> schedulerStaffs=processSchedulerStaff.findAll(firmId);
			int currentTeacherCount=schedulerStaffs.size();
			int teacherCount=Integer.parseInt(ptRestrictions.getTeacherCount());
			if(teacherCount>currentTeacherCount){
				return true;
			}else{
				return false;
			}
		}
		
	}
}
