package tr.com.abasus.ptboss.facade.sale;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import tr.com.abasus.ptboss.facade.dao.SaleFacadeDao;
import tr.com.abasus.ptboss.packetsale.dao.PacketSaleDao;
import tr.com.abasus.ptboss.packetsale.entity.PacketSaleFactory;
import tr.com.abasus.ptboss.result.entity.HmiResultObj;
import tr.com.abasus.ptboss.schedule.dao.ScheduleMembershipDao;
import tr.com.abasus.ptboss.schedule.entity.ScheduleFactory;
import tr.com.abasus.util.ResultStatuObj;

@Service
public class SaleMembershipFacade  implements SaleFacadeService{

	@Autowired
	@Qualifier(value="salePersonalFacade")
	SaleFacadeService saleFacadeService;
	
	@Autowired
	SaleFacadeDao saleFacadeDao;
	
	@Autowired
	ScheduleMembershipDao scheduleMembershipDao;
	
	@Override
	public HmiResultObj canSale(long userId,Date startDate) {
		
		List<ScheduleFactory> scheduleFactories= scheduleMembershipDao.findSchedulesbyUserId(userId);
		HmiResultObj hmiResultObj=new HmiResultObj();
		hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS);
		for (ScheduleFactory scheduleFactory : scheduleFactories) {
				if(startDate.before(scheduleFactory.getSmpEndDate())){
					hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
					hmiResultObj.setResultMessage("canNotStartBeforeFinishedPrevious");
				}
		}
		
		return hmiResultObj;
	}
	
	@Override
	public boolean canSaleDelete(PacketSaleFactory packetSaleFactory) {
		return saleFacadeDao.canMembershipSaleDelete(packetSaleFactory);
	}

	@Override
	public boolean canStaffDelete(long userId) {
		if(saleFacadeDao.canMembershipStaffDelete(userId)){
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
		
		List<PacketSaleFactory> packetSaleFactoriesToMembership=saleFacadeDao.getMembershipPacketSalesToUserId(userId);
		
		packetSaleFactoriesToChain.addAll(packetSaleFactoriesToMembership);
		return packetSaleFactoriesToChain;
	}

	@Override
	public boolean canUserDelete(long userId) {
		
		if(!saleFacadeService.canUserDelete(userId)){
			return false;
		}
		return saleFacadeDao.userHaveSaleInMembership(userId);
	}

	@Override
	public HmiResultObj canSaleChange(PacketSaleFactory packetSaleFactory) {
		// TODO Auto-generated method stub
		return null;
	}

}
