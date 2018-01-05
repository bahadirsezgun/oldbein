package tr.com.abasus.ptboss.facade.scheduleplan;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tr.com.abasus.ptboss.schedule.entity.SchedulePlan;
import tr.com.abasus.ptboss.schedule.entity.ScheduleTimePlan;
import tr.com.abasus.ptboss.schedule.service.ScheduleService;

@Service
public class SchedulePlanFacadeService {

	
	@Autowired
	ScheduleService scheduleService;
	
	
	public int schedulePlanCompleted(SchedulePlan schedulePlan,int countOfTokenPlans){
		/*
		List<ScheduleTimePlan> scheduleTimePlans=scheduleService.findScheduleTimePlanByPlanId(schedulePlan.getSchId());
		int countOfTokenPlans=scheduleTimePlans.size();
		*/
		int programOfPlanCount=schedulePlan.getSchCount();
		
		if(programOfPlanCount<=countOfTokenPlans)
			return 1;
		
		return 0;
	}
	
	public ScheduleTimePlan getSequenceOfScheduleTimePlan(ScheduleTimePlan scheduleTimePlan){
		
		String sequence="";
		int lastPlan=0;
		int firstPlan=0;
		ScheduleTimePlan schT=new ScheduleTimePlan();
		
		List<ScheduleTimePlan> scheduleTimePlans=scheduleService.findScheduleTimePlanByPlanId(scheduleTimePlan.getSchId());
		int i=1;
		for (ScheduleTimePlan schTP : scheduleTimePlans) {
			if(schTP.getSchtId()==scheduleTimePlan.getSchtId()){
				sequence=i+"/"+scheduleTimePlan.getPlanCount();
				
				if(i==scheduleTimePlan.getPlanCount()){
					lastPlan=1;
				}else if(i==1){
					firstPlan=1;
				}
				
				break;
			}
			
		i++;	
		}
		
		schT.setSequence(sequence);
		schT.setLastPlan(lastPlan);
		schT.setFirstPlan(firstPlan);
		
		return schT;
	}
	
	
}
