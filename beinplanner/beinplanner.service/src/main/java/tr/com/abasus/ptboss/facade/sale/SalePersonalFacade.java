package tr.com.abasus.ptboss.facade.sale;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tr.com.abasus.ptboss.facade.dao.SaleFacadeDao;
import tr.com.abasus.ptboss.packetsale.entity.PacketSaleFactory;
import tr.com.abasus.ptboss.result.entity.HmiResultObj;
import tr.com.abasus.ptboss.schedule.entity.ScheduleUsersPersonalPlan;
import tr.com.abasus.ptboss.schedule.service.SchedulePersonalService;
import tr.com.abasus.util.ResultStatuObj;

@Service
public class SalePersonalFacade  implements SaleFacadeService {

	@Autowired
	SaleFacadeDao saleFacadeDao;
	
	@Autowired
	SchedulePersonalService schedulePersonalService;
	
	@Override
	public HmiResultObj canSale(long userId,Date startDate) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public boolean canSaleDelete(PacketSaleFactory packetSaleFactory) {
		return saleFacadeDao.canPersonalSaleDelete(packetSaleFactory);
	}

	@Override
	public boolean canStaffDelete(long userId) {
		if(saleFacadeDao.canPersonalStaffDelete(userId)){
			return false;
		}else{
			return true;
		}
	}

	public SaleFacadeDao getSaleFacadeDao() {
		return saleFacadeDao;
	}

	public void setSaleFacadeDao(SaleFacadeDao saleFacadeDao) {
		this.saleFacadeDao = saleFacadeDao;
	}

	@Override
	public List<PacketSaleFactory> getPacketSalesToUserId(long userId) {
    	List<PacketSaleFactory> packetSaleFactoriesToPersonal=saleFacadeDao.getPersonalPacketSalesToUserId(userId);
		return packetSaleFactoriesToPersonal;
	}

	@Override
	public boolean canUserDelete(long userId) {
		return saleFacadeDao.userHaveSaleInPersonal(userId);
	}

	@Override
	public HmiResultObj canSaleChange(PacketSaleFactory packetSaleFactory) {
		
		HmiResultObj hmiResultObj=new HmiResultObj();
		hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS);
		hmiResultObj.setResultMessage(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
		List<ScheduleUsersPersonalPlan> scheduleUsersPersonalPlans= schedulePersonalService.findScheduleUsersPersonalPlanByUserIdAndSaleId(packetSaleFactory.getUserId(),packetSaleFactory.getSaleId());
		if(scheduleUsersPersonalPlans.size()>packetSaleFactory.getProgCount()){
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
			hmiResultObj.setResultMessage("moreThanProgCountHavePlanned");
			
		}
		
	
		
		
		return hmiResultObj;
	}

}
