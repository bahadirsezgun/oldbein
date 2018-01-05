package tr.com.abasus.ptboss.facade.sale;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import tr.com.abasus.ptboss.facade.dao.SaleFacadeDao;
import tr.com.abasus.ptboss.packetsale.entity.PacketSaleFactory;
import tr.com.abasus.ptboss.result.entity.HmiResultObj;
import tr.com.abasus.ptboss.schedule.entity.ScheduleUsersClassPlan;
import tr.com.abasus.ptboss.schedule.service.ScheduleClassService;
import tr.com.abasus.util.ResultStatuObj;

@Service
public class SaleClassFacade implements SaleFacadeService {

	
	@Autowired
	@Qualifier(value="saleMembershipFacade")
	SaleFacadeService saleFacadeService;
	
	@Autowired
	SaleFacadeDao saleFacadeDao;
	
	@Autowired
	ScheduleClassService scheduleClassService;
	
	
	@Override
	public HmiResultObj canSale(long userId,Date startDate) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public boolean canSaleDelete(PacketSaleFactory packetSaleFactory) {
		return saleFacadeDao.canClassSaleDelete(packetSaleFactory);
	}

	@Override
	public boolean canStaffDelete(long userId) {
		if(saleFacadeDao.canClassStaffDelete(userId)){
			if(saleFacadeService.canStaffDelete(userId)){
				return true;
			}else{
				return false;
			}
			
		}else{
			return false;
		}
	}

	public SaleFacadeService getSaleFacadeService() {
		return saleFacadeService;
	}

	public void setSaleFacadeService(SaleFacadeService saleFacadeService) {
		this.saleFacadeService = saleFacadeService;
	}

	public SaleFacadeDao getSaleFacadeDao() {
		return saleFacadeDao;
	}

	public void setSaleFacadeDao(SaleFacadeDao saleFacadeDao) {
		this.saleFacadeDao = saleFacadeDao;
	}

	@Override
	public List<PacketSaleFactory> getPacketSalesToUserId(long userId) {
		List<PacketSaleFactory> packetSaleFactoriesToChain=  saleFacadeService.getPacketSalesToUserId(userId);
		
		List<PacketSaleFactory> packetSaleFactoriesToClass=saleFacadeDao.getClassPacketSalesToUserId(userId);
		
		packetSaleFactoriesToChain.addAll(packetSaleFactoriesToClass);
		return packetSaleFactoriesToChain;
	}

	@Override
	public boolean canUserDelete(long userId) {
		if(!saleFacadeService.canUserDelete(userId)){
			return false;
		}
		return saleFacadeDao.userHaveSaleInClass(userId);
	}

	@Override
	public HmiResultObj canSaleChange(PacketSaleFactory packetSaleFactory) {
		HmiResultObj hmiResultObj=new HmiResultObj();
		hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS);
		hmiResultObj.setResultMessage(ResultStatuObj.RESULT_STATU_SUCCESS_STR);
		List<ScheduleUsersClassPlan> scheduleUsersClassPlans= scheduleClassService.findScheduleUsersClassPlanByUserIdAndSaleId(packetSaleFactory.getUserId(),packetSaleFactory.getSaleId());
		if(scheduleUsersClassPlans.size()>packetSaleFactory.getProgCount()){
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
			hmiResultObj.setResultMessage("moreThanProgCountHavePlanned");
			
		}
		
		
		return hmiResultObj;
	}

	

	

}
