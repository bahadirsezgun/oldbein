package tr.com.abasus.ptboss.schedule.businessService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tr.com.abasus.ptboss.program.entity.ProgramFactory;
import tr.com.abasus.ptboss.schedule.businessEntity.ScheduleObj;
import tr.com.abasus.util.ActionTypes;
import tr.com.abasus.util.ProgramTypes;

@Service
public class ScheduleBusinessService {

	
	@Autowired
	ScheduleBusinessClassService scheduleBusinessClassService;
	


	@Autowired 
	ScheduleBusinessPersonalService scheduleBusinessPersonalService;
	
	
	
	
	
	public synchronized ScheduleObj createUpdateSchedule(ScheduleObj scheduleObj,int programType,int actionType,ProgramFactory programFactory){
		
		
		
		if(programType==ProgramTypes.PROGRAM_CLASS){
			if(actionType==ActionTypes.ACTION_CREATE)
				return scheduleBusinessClassService.createScheduleClass(scheduleObj,programFactory);
			else if(actionType==ActionTypes.ACTION_UPDATE)
				return scheduleBusinessClassService.updateScheduleClass(scheduleObj,programFactory);
			else if(actionType==ActionTypes.ACTION_ADD)
				return scheduleBusinessClassService.addScheduleClass(scheduleObj,programFactory);
			else if(actionType==ActionTypes.ACTION_CHANGE)
				return scheduleBusinessClassService.changeScheduleClass(scheduleObj,programFactory);
			
			
			
		}else if(programType==ProgramTypes.PROGRAM_PERSONAL){
			if(actionType==ActionTypes.ACTION_CREATE)
				return scheduleBusinessPersonalService.createSchedulePersonal(scheduleObj,programFactory);
			else if(actionType==ActionTypes.ACTION_UPDATE)
				return scheduleBusinessPersonalService.updateSchedulePersonal(scheduleObj,programFactory);
			else if(actionType==ActionTypes.ACTION_ADD)
				return scheduleBusinessPersonalService.addSchedulePersonal(scheduleObj,programFactory);
			else if(actionType==ActionTypes.ACTION_CHANGE)
				return scheduleBusinessPersonalService.changeSchedulePersonal(scheduleObj,programFactory);
		}
		
		
		return null;
	}
	
	
		
	
	
	
	
		
	
		
}
