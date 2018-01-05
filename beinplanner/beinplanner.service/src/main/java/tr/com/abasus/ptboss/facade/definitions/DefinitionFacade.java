package tr.com.abasus.ptboss.facade.definitions;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tr.com.abasus.ptboss.facade.dao.SchedulerFacadeDao;
import tr.com.abasus.ptboss.ptuser.dao.ProcessUserDao;
import tr.com.abasus.ptboss.ptuser.entity.User;
import tr.com.abasus.ptboss.result.entity.HmiResultObj;
import tr.com.abasus.util.ResultStatuObj;

@Service
public class DefinitionFacade implements DefinitionFacadeService {

	
	@Autowired
	ProcessUserDao processUserDao;
	
	@Autowired
	SchedulerFacadeDao schedulerFacadeDao;
	
	
	
	@Override
	public HmiResultObj canFirmDelete(int firmId) {
		HmiResultObj hmiResultObj=new HmiResultObj();
		hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS);
		List<User> members=processUserDao.findAllToMember(firmId);
	
		
		List<User> admin=processUserDao.findAllToAdmin(firmId);
		if(admin.size()>0){
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
			hmiResultObj.setResultMessage("youHaveAdminInFirm");
			return hmiResultObj;
		}
		
		if(members.size()>0){
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
			hmiResultObj.setResultMessage("youHaveMemberInFirm");
			return hmiResultObj;
		}
		
		List<User> mananers=processUserDao.findAllToManager(firmId);
		if(mananers.size()>0){
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
			hmiResultObj.setResultMessage("youHaveManagerInFirm");
			return hmiResultObj;
		}
		
		List<User> schStaffs=processUserDao.findAllToSchedulerStaff(firmId);
		if(schStaffs.size()>0){
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
			hmiResultObj.setResultMessage("youHaveInstructorsInFirm");
			return hmiResultObj;
		}
		
		List<User> staffs=processUserDao.findAllToStaff(firmId);
		if(staffs.size()>0){
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
			hmiResultObj.setResultMessage("youHaveStaffsInFirm");
			return hmiResultObj;
		}
		
		
		
		List<User> superManagers=processUserDao.findAllToSuperManager(firmId);
		if(superManagers.size()>0){
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
			hmiResultObj.setResultMessage("youHaveSuperManagersInFirm");
			return hmiResultObj;
		}
		
		return hmiResultObj;
	}



	@Override
	public boolean canStudioDelete(int studioId) {
		return schedulerFacadeDao.isStudioUsedBefore(studioId);
	}
	
	
}
