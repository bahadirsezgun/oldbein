package tr.com.abasus.ptboss.ptuser.iuser;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import tr.com.abasus.ptboss.facade.payment.PaymentClassFacade;
import tr.com.abasus.ptboss.facade.payment.PaymentFacadeService;
import tr.com.abasus.ptboss.facade.sale.SaleFacadeService;
import tr.com.abasus.ptboss.facade.schedule.ScheduleClassFacade;
import tr.com.abasus.ptboss.facade.schedule.ScheduleFacadeService;
import tr.com.abasus.ptboss.facade.schedule.SchedulePersonalFacade;
import tr.com.abasus.ptboss.menu.entity.MenuLevel2;
import tr.com.abasus.ptboss.menu.entity.MenuTbl;
import tr.com.abasus.ptboss.menu.service.MenuService;
import tr.com.abasus.ptboss.packetpayment.entity.PacketPaymentFactory;
import tr.com.abasus.ptboss.packetsale.entity.PacketSaleClass;
import tr.com.abasus.ptboss.packetsale.entity.PacketSaleFactory;
import tr.com.abasus.ptboss.packetsale.entity.PacketSaleInterface;
import tr.com.abasus.ptboss.packetsale.entity.PacketSalePersonal;
import tr.com.abasus.ptboss.program.entity.ProgramClass;
import tr.com.abasus.ptboss.program.entity.ProgramInterface;
import tr.com.abasus.ptboss.program.entity.ProgramPersonal;
import tr.com.abasus.ptboss.ptuser.dao.ProcessUserDao;
import tr.com.abasus.ptboss.ptuser.entity.User;
import tr.com.abasus.ptboss.result.entity.HmiResultObj;
import tr.com.abasus.ptboss.schedule.entity.ScheduleFactory;
import tr.com.abasus.ptboss.schedule.entity.ScheduleInterface;
import tr.com.abasus.ptboss.schedule.entity.SchedulePlan;
import tr.com.abasus.ptboss.schedule.entity.ScheduleUsersClassPlan;
import tr.com.abasus.ptboss.schedule.entity.ScheduleUsersPersonalPlan;
import tr.com.abasus.ptboss.schedule.service.ScheduleService;
import tr.com.abasus.util.ProgramTypes;
import tr.com.abasus.util.ResultStatuObj;
import tr.com.abasus.util.SaleStatus;
import tr.com.abasus.util.UserTypes;

@Component(value="processMember")
@Scope("prototype")
public class ProcessMember  implements ProcessInterface  {

	@Autowired
	ProcessUserDao processUserDao;
	
	@Autowired
	MenuService menuService;

	@Autowired
	PaymentClassFacade paymentClassFacade;
	
	@Autowired
	@Qualifier(value="saleClassFacade")
	SaleFacadeService saleFacadeService;
	
	
	
	@Autowired
	ScheduleClassFacade scheduleClassFacade;
	
	@Autowired
	SchedulePersonalFacade schedulePersonalFacade;
	
	
	@Autowired
	PacketSalePersonal packetSalePersonal;
	
	@Autowired
	PacketSaleClass packetSaleClass;
	
	@Autowired
	ScheduleUsersClassPlan scheduleUsersClassPlan;
	
	@Autowired
	ScheduleUsersPersonalPlan scheduleUsersPersonalPlan;
	
	
	@Override
	public int getUserCompletePercent(User user) {
		return processUserDao.getUserCompletePercentToMember(user);
	}

	@Override
	public List<User> findAll(int firmId) {
		return processUserDao.findAllToMember(firmId);
	}
	
	@Override
	public List<User> findAllInChain(int firmId) {
		return null;
	}
	
	
	
	@Override
	public List<User> findByUserNameAndSurnameInChain(String userName, String userSurname, int firmId) {
		return null;
	}
	
	
	public int findTotalMemberInSystem(int firmId){
		return processUserDao.findTotalMemberInSystem(firmId);
	}
	
	
	@Override
	public HmiResultObj deleteUser(User user) {
		HmiResultObj hmiResultObj=new HmiResultObj();
		
		if(!saleFacadeService.canUserDelete(user.getUserId())){
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
			hmiResultObj.setResultMessage("userHaveSale");
			return hmiResultObj;
		}
		
		
		if(paymentClassFacade.canUserDelete(user.getUserId())){
			HmiResultObj hmrc=scheduleClassFacade.canUserDelete(user.getUserId());
			if(hmrc.getResultStatu()==ResultStatuObj.RESULT_STATU_SUCCESS){
				hmiResultObj=processUserDao.deleteUser(user);
			}else{
				hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
				hmiResultObj.setResultMessage("userHaveBooking");
			}
		}else{
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
			hmiResultObj.setResultMessage("userHavePayment");
		}
		
		
		return hmiResultObj;
	}
	
	@Override
	public HmiResultObj createUser(User user) {
		return processUserDao.createUser(user);
	}
	
	@Override
	public List<MenuTbl> getMenuSide() {
		List<MenuTbl> menuTbls=menuService.findUpperMenuByUserType(UserTypes.USER_TYPE_MEMBER_INT);
		for (MenuTbl menuTbl : menuTbls) {
			List<MenuLevel2> menuLevel2s=menuService.findLevel2MenuByUserType(UserTypes.USER_TYPE_MEMBER_INT, menuTbl.getMenuId());
			menuTbl.setMenuLevel2s(menuLevel2s);
		}
		
		return menuTbls;
		
	}

	@Override
	public MenuTbl getMenuDashBoard() {
		return menuService.findDashBoardByUserType(UserTypes.USER_TYPE_MEMBER_INT);
	}
	
	@Override
	public List<MenuTbl> getMenuTop() {
		List<MenuTbl> menuTopTbls=menuService.findTopMenuByUserType(UserTypes.USER_TYPE_MEMBER_INT);
		return menuTopTbls;
	}

	@Override
	public List<User> findByNameAndSurname(String userName, String userSurname,int firmId) {
		
		List<User> users=processUserDao.findByNameAndSurnameToMember(userName, userSurname,firmId);
		for (User user : users) {
			
			
			List<PacketSaleFactory> packetSaleFactories=saleFacadeService.getPacketSalesToUserId(user.getUserId());
			user.setPacketCount(packetSaleFactories.size());
			double packetAmount=0;
			for (PacketSaleFactory packetSaleFactory : packetSaleFactories) {
				packetAmount+=packetSaleFactory.getPacketPrice();
			}
			
			
			if(user.getPacketCount()>0){
				List<PacketPaymentFactory> packetPaymentFactories= paymentClassFacade.getPaymentsToUserId(user.getUserId());
				double leftPayment=0;
				double payAmount=0;
				for (PacketPaymentFactory packetPaymentFactory : packetPaymentFactories) {
					payAmount+=packetPaymentFactory.getPayAmount();
				}
				leftPayment=packetAmount-payAmount;
				user.setLeftPayment(leftPayment);
				user.setPayAmount(payAmount);
				
			}else{
				user.setLeftPayment(0);
				user.setPayAmount(0);
			}
		}
		
		
		return users;
	}

	public ProcessUserDao getProcessUserDao() {
		return processUserDao;
	}

	public void setProcessUserDao(ProcessUserDao processUserDao) {
		this.processUserDao = processUserDao;
	}

	public MenuService getMenuService() {
		return menuService;
	}

	public void setMenuService(MenuService menuService) {
		this.menuService = menuService;
	}

	
	
	
	
	public List<User> findByUserNameForSales(String userName, String userSurname,int type,int firmId) {
		PacketSaleInterface packetSaleInterface=null;
		if(type==ProgramTypes.PROGRAM_CLASS){
			packetSaleInterface=packetSaleClass;
		}else if(type==ProgramTypes.PROGRAM_PERSONAL){
			packetSaleInterface=packetSalePersonal;	
		}
		
		List<User> users=packetSaleInterface.findByUserNameForSales(userName, userSurname, firmId);
		return users;
	}
	
	
	@Override
	public List<User> findByNameAndSaleProgramWithNoPlan(String userName, String userSurname, long progId, int type,int firmId,long schId) {
		PacketSaleInterface packetSaleInterface=null;
		ScheduleFactory scheduleFactory=null;
		if(type==ProgramTypes.PROGRAM_CLASS){
			packetSaleInterface=packetSaleClass;
			scheduleFactory=scheduleUsersClassPlan;
		}else if(type==ProgramTypes.PROGRAM_PERSONAL){
			packetSaleInterface=packetSalePersonal;	
			scheduleFactory=scheduleUsersPersonalPlan;
		}
		
		List<User> users=packetSaleInterface.findByUserNameAndSaleProgramWithNoPlan(userName, userSurname, progId, firmId);
		
		if(schId!=0){
			List<User> usersInSamePlan=packetSaleInterface.findByUsersAndSaledProgram(schId);
			users.addAll(usersInSamePlan);
		}
		
		
		for (User user : users) {
			List<SchedulePlan> schedulePlans= scheduleFactory.findSchedulePlansbyUserId(user.getUserId(),user.getSaleId());
			if(schedulePlans.size()>0){
				for (SchedulePlan schedulePlan : schedulePlans) {
					user.setSaleStatu(SaleStatus.SALE_SAME_PLANNED);
					user.setSchId(schedulePlan.getSchId());
				}
				
			}else{
				user.setSaleStatu(SaleStatus.SALE_NO_PLANNED);
			}
		}
		
		
		return users;
	}
	
	
	@Override
	public List<User> findByNameAndSaleProgramWithPlan(String userName, String userSurname, long progId, int type,int firmId,long schId) {
		PacketSaleInterface packetSaleInterface=null;
		ScheduleFactory scheduleFactory=null;
		if(type==ProgramTypes.PROGRAM_CLASS){
			packetSaleInterface=packetSaleClass;
			scheduleFactory=scheduleUsersClassPlan;
		}else if(type==ProgramTypes.PROGRAM_PERSONAL){
			packetSaleInterface=packetSalePersonal;	
			scheduleFactory=scheduleUsersPersonalPlan;
		}
		
		List<User> users=packetSaleInterface.findByUserNameAndSaleProgramWithPlan(userName, userSurname, progId, firmId);
		
		
		
		
		for (User user : users) {
			List<SchedulePlan> schedulePlans= scheduleFactory.findSchedulePlansbyUserId(user.getUserId(),user.getSaleId());
			if(schedulePlans.size()>0){
				for (SchedulePlan schedulePlan : schedulePlans) {
					user.setSaleStatu(SaleStatus.SALE_HAS_PLANNED);
					user.setSchId(schedulePlan.getSchId());
				}
				
			}else{
				user.setSaleStatu(SaleStatus.SALE_NO_PLANNED);
			}
		}
		
		
		return users;
	}

	public List<User> findByUsersAndSaledPrograms(long schId,int type){
		PacketSaleInterface packetSaleInterface=null;
		if(type==ProgramTypes.PROGRAM_CLASS){
			packetSaleInterface=packetSaleClass;
		}else if(type==ProgramTypes.PROGRAM_PERSONAL){
			packetSaleInterface=packetSalePersonal;	
		}
		
		List<User> users=packetSaleInterface.findByUsersAndSaledProgram(schId);
		for (User user : users) {
			user.setSaleStatu(SaleStatus.SALE_HAS_USER);
			user.setSchId(schId);
		}
		return users;
	}
	

	public List<User> findUsersInSameSchedulePlanBySaleId(long saleId,int type){
		PacketSaleInterface packetSaleInterface=null;
		if(type==ProgramTypes.PROGRAM_CLASS){
			packetSaleInterface=packetSaleClass;
		}else if(type==ProgramTypes.PROGRAM_PERSONAL){
			packetSaleInterface=packetSalePersonal;	
		}
		
		
		SchedulePlan schedulePlan= scheduleUsersPersonalPlan.findSchedulePlanBySaleId(saleId);
		if(schedulePlan!=null){
			List<User> users=packetSaleInterface.findByUsersAndSaledProgram(schedulePlan.getSchId());
			return users;
		}else{
			return null;
		}
		
	}
	
	

}
