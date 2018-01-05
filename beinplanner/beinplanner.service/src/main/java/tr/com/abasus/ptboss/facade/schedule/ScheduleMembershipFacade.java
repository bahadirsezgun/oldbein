package tr.com.abasus.ptboss.facade.schedule;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import tr.com.abasus.ptboss.packetsale.entity.PacketSaleFactory;
import tr.com.abasus.ptboss.program.entity.ProgramFactory;
import tr.com.abasus.ptboss.result.entity.HmiResultObj;
import tr.com.abasus.ptboss.schedule.dao.ScheduleMembershipDao;
import tr.com.abasus.ptboss.schedule.entity.ScheduleFactory;
import tr.com.abasus.ptboss.schedule.entity.ScheduleMembershipPlan;
import tr.com.abasus.ptboss.schedule.entity.ScheduleMembershipTimePlan;
import tr.com.abasus.util.GlobalUtil;
import tr.com.abasus.util.OhbeUtil;
import tr.com.abasus.util.ProgDurationTypes;
import tr.com.abasus.util.ResultStatuObj;
@Service
public class ScheduleMembershipFacade implements ScheduleMembershipFacadeService{

	
	
	@Autowired
	ScheduleMembershipDao scheduleMembershipDao;
	
	@Override
	public HmiResultObj canScheduleCreate(ScheduleFactory scheduleFactory) {
		HmiResultObj hmiResultObj=new HmiResultObj();
		hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS);
		hmiResultObj.setResultMessage(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
		
		List<ScheduleFactory> scheduleFactories=scheduleMembershipDao.findSchedulesbyUserId(scheduleFactory.getUserId());
		
		
		for (ScheduleFactory sf : scheduleFactories) {
			if(sf.getSmpEndDate().after(scheduleFactory.getSmpStartDate())){
				hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
				hmiResultObj.setResultMessage("prevMembershipNotFinished");
			}
		}

		return hmiResultObj;
	}

	@Override
	public HmiResultObj canScheduleFreeze(ScheduleFactory smp,ScheduleFactory smpInDb,ProgramFactory pmf) {
		
		HmiResultObj hmiResultObj=new HmiResultObj();
		hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS);
		
		if(smpInDb.getSmpFreezeCount()>=pmf.getMaxFreezeCount()){
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
			hmiResultObj.setResultMessage("maxFreeezeCountExceeded");
			return hmiResultObj;
		}
		
		
		int freezeDuration=pmf.getFreezeDuration();
		Date freezeEndDate=new Date();
		Date freezeStartDate=OhbeUtil.getThatDayFormatNotNull(smp.getSmpStartDateStr(), GlobalUtil.global.getPtDateFormat());
		if(pmf.getFreezeDurationType()==ProgDurationTypes.DURATION_TYPE_MONTHLY){
			if(freezeStartDate.after(ProgDurationTypes.getDateForNextMonth(smp.getSmpEndDate(),freezeDuration*-1))){
				hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
				hmiResultObj.setResultMessage("membershipAlreadyFinished");
				return hmiResultObj;
			}
		}else{
			if(pmf.getFreezeDurationType()==ProgDurationTypes.DURATION_TYPE_WEEKLY){
				freezeDuration=freezeDuration*7;
			}
			
			////System.out.println("freezeStartDate "+OhbeUtil.getDateStrByFormat(freezeStartDate, "dd/MM/yyyy"));
			////System.out.println("Smp End Date "+OhbeUtil.getDateStrByFormat(ProgDurationTypes.getDateForNextDate(smp.getSmpEndDate(),freezeDuration*-1), "dd/MM/yyyy"));
			
			
			if(freezeStartDate.after(ProgDurationTypes.getDateForNextDate(smp.getSmpEndDate(),freezeDuration*-1))){
				hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
				hmiResultObj.setResultMessage("membershipAlreadyFinished");
				return hmiResultObj;
			}
		}
		
		
		
		
		
		return hmiResultObj;
	}

	@Override
	public HmiResultObj canScheduleUnFreeze(ScheduleFactory smp,ScheduleMembershipTimePlan scheduleMembershipTimePlan,ProgramFactory pmf) {
		HmiResultObj hmiResultObj=new HmiResultObj();
		hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS);
		
		Date todayDate=new Date();
			if(todayDate.after(ProgDurationTypes.getDateForNextDate(smp.getSmpStartDate(),5))){
				hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
				hmiResultObj.setResultMessage("fiveDaysBeforeFreeze");
				return hmiResultObj;
			}
		return hmiResultObj;
	}

	@Override
	public HmiResultObj canScheduleDelete(PacketSaleFactory packetSaleFactory) {
		HmiResultObj hmiResultObj=new HmiResultObj();
		hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS);
		
		
		ScheduleMembershipPlan smp=(ScheduleMembershipPlan)scheduleMembershipDao.findSchedulePlanBySaleId(packetSaleFactory.getSaleId());
		Date todayDate=new Date();
		if(todayDate.after(ProgDurationTypes.getDateForNextDate(smp.getSmpStartDate(),5))){
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
			hmiResultObj.setResultMessage("fiveDaysBeforeDelete");
			return hmiResultObj;
		}
		
		
		
		return null;
	}

	
	

	
}
