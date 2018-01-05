package tr.com.abasus.ptboss.definition.controller;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import tr.com.abasus.ptboss.definition.entity.DefCalendarTimes;
import tr.com.abasus.ptboss.definition.entity.DefCity;
import tr.com.abasus.ptboss.definition.entity.DefFirm;
import tr.com.abasus.ptboss.definition.entity.DefLevelInfo;
import tr.com.abasus.ptboss.definition.entity.DefState;
import tr.com.abasus.ptboss.definition.entity.DefStudio;
import tr.com.abasus.ptboss.definition.service.DefinitionService;
import tr.com.abasus.ptboss.exceptions.UnAuthorizedUserException;
import tr.com.abasus.ptboss.facade.definitions.DefinitionFacade;
import tr.com.abasus.ptboss.facade.definitions.DefinitionFacadeService;
import tr.com.abasus.ptboss.menu.entity.MenuRoleTbl;
import tr.com.abasus.ptboss.menu.entity.MenuTbl;
import tr.com.abasus.ptboss.menu.service.MenuService;
import tr.com.abasus.ptboss.ptuser.entity.User;
import tr.com.abasus.ptboss.result.entity.HmiResultObj;
import tr.com.abasus.util.GlobalUtil;
import tr.com.abasus.util.ResultStatuObj;
import tr.com.abasus.util.SessionUtil;
import tr.com.abasus.util.StatuTypes;
import tr.com.abasus.util.UserTypes;

@Controller
@RequestMapping(value="/definition")
public class DefinitionController {

	
	@Autowired
	DefinitionService definitionService;
	
	@Autowired
	DefinitionFacade definitionFacade; 
	
	
	@Autowired
	MenuService menuService;
	
	
	@RequestMapping(value="/menu/findRolMenu/{userType}", method = RequestMethod.POST) 
	public @ResponseBody List<MenuTbl> findRoleMenuByUserType(@PathVariable("userType") int userType , HttpServletRequest request ){
		User user=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if(!(user.getUserType()==UserTypes.USER_TYPE_ADMIN_INT)){
			return null;
		}
		
		return menuService.findUserAuthorizedMenus(userType);
	}
	
	@RequestMapping(value="/menu/findTopRolMenu/{userType}", method = RequestMethod.POST) 
	public @ResponseBody List<MenuTbl> findTopRolMenu(@PathVariable("userType") int userType , HttpServletRequest request ){
		User user=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if(!(user.getUserType()==UserTypes.USER_TYPE_ADMIN_INT)){
			return null;
		}
		
		return menuService.findAllTopMenu(userType);
	}
	
	@RequestMapping(value="/menu/findDashboardMenu/{userType}", method = RequestMethod.POST) 
	public @ResponseBody MenuTbl findDashboardMenu(@PathVariable("userType") int userType , HttpServletRequest request ){
		User user=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if(!(user.getUserType()==UserTypes.USER_TYPE_ADMIN_INT)){
			return null;
		}
		
		return menuService.findDashBoardByUserType(userType);
	}
	
	
	@RequestMapping(value="/menu/addRolMenu", method = RequestMethod.POST) 
	public @ResponseBody HmiResultObj addRolMenu(@RequestBody MenuRoleTbl menuRoleTbl , HttpServletRequest request ){
		User user=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if(!(user.getUserType()==UserTypes.USER_TYPE_ADMIN_INT)){
			return null;
		}
		
		
		return menuService.addRolMenu(menuRoleTbl);
	}
	
	@RequestMapping(value="/menu/changeDashboard/{userType}", method = RequestMethod.POST) 
	public @ResponseBody HmiResultObj changeDashboard(@RequestBody MenuRoleTbl menuRoleTbl,@PathVariable int userType , HttpServletRequest request ){
		User user=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if(!(user.getUserType()==UserTypes.USER_TYPE_ADMIN_INT)){
			return null;
		}
		
		MenuRoleTbl menuRoleTblInDb=menuService.findDashBoardRoleMenuByUserType(userType);
		menuService.removeDashboardRolMenu(menuRoleTblInDb);
		
		return menuService.addRolMenu(menuRoleTbl);
	}
	
	@RequestMapping(value="/menu/removeRolMenu", method = RequestMethod.POST) 
	public @ResponseBody HmiResultObj removeRolMenu(@RequestBody MenuRoleTbl menuRoleTbl , HttpServletRequest request ){
		User user=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if(!(user.getUserType()==UserTypes.USER_TYPE_ADMIN_INT)){
			return null;
		}
		
		return menuService.removeRolMenu(menuRoleTbl);
	}
	
	
	
	@RequestMapping(value="/studio/find", method = RequestMethod.POST) 
	public @ResponseBody List<DefStudio> findAllStudiosByUser(HttpServletRequest request ) throws UnAuthorizedUserException{
		User user=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if((user.getUserType()==UserTypes.USER_TYPE_MEMBER_INT)){
			throw new UnAuthorizedUserException("USER NOT AUTHORIZED");
		}
		
		
		return definitionService.findAllStudios(user.getFirmId());
	}
	
	@RequestMapping(value="/studio/findAll/{firmId}", method = RequestMethod.POST) 
	public @ResponseBody List<DefStudio> findAllStudios(@PathVariable int firmId, HttpServletRequest request ) throws UnAuthorizedUserException{
		User user=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		
		
		if(user==null || user.getUserType()==UserTypes.USER_TYPE_MEMBER_INT){
			throw new UnAuthorizedUserException("USER NOT AUTHORIZED");
		}
		
		
		return definitionService.findAllStudiosByStatus(firmId, StatuTypes.ALL);
	}
	
	@RequestMapping(value="/studio/findStudio/{studioId}", method = RequestMethod.POST) 
	public @ResponseBody DefStudio findStudioById(@PathVariable("studioId") int studioId ,HttpServletRequest request ){
		User user=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if(user==null || user.getUserType()==UserTypes.USER_TYPE_MEMBER_INT){
				return null;
		}
		
		
		return definitionService.findStudioById(studioId);
	}
	
	@RequestMapping(value="/studio/create", method = RequestMethod.POST) 
	public @ResponseBody HmiResultObj createStudio(@RequestBody DefStudio defStudio, HttpServletRequest request ){
		User user=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if(user.getUserType()!=UserTypes.USER_TYPE_ADMIN_INT){
			return null;
		}
		
		return definitionService.createStudio(defStudio);
	}
	
	@RequestMapping(value="/studio/update", method = RequestMethod.POST) 
	public @ResponseBody HmiResultObj updateStudio(@RequestBody DefStudio defStudio, HttpServletRequest request ){
		User user=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if(user.getUserType()!=UserTypes.USER_TYPE_ADMIN_INT){
			return null;
		}
		
		return definitionService.updateStudio(defStudio);
	}
	
	@RequestMapping(value="/studio/delete", method = RequestMethod.POST) 
	public @ResponseBody HmiResultObj deleteStudio(@RequestBody DefStudio defStudio, HttpServletRequest request ){
		User user=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if(user.getUserType()!=UserTypes.USER_TYPE_ADMIN_INT){
				return null;
		}
		
		
		if(!definitionFacade.canStudioDelete(defStudio.getStudioId())){
			return definitionService.deleteStudio(defStudio);
		}else{
			HmiResultObj hmiResultObj=new HmiResultObj();
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
			hmiResultObj.setResultMessage("studioCanNotDeleted");
			return hmiResultObj;
		}
		
		
	}
	
	
	
	
	/**
	 * @author bahadir
	 * @category Definitions
	 * @comment 
	 *   İlçelerin bulunması için yazılmış olan rest service.
	 * 
	 * @param request
	 * @return HmiResultObj
	 */
	@RequestMapping(value="/city/findCities/{stateId}", method = RequestMethod.POST) 
	public @ResponseBody List<DefCity> findCities(@PathVariable("stateId") int stateId,HttpServletRequest request ){
		User user=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if(user==null || user.getUserType()==UserTypes.USER_TYPE_MEMBER_INT){
				return null;
		}
		
		
		return definitionService.findCities(stateId);
	}
	
	@RequestMapping(value="/city/create", method = RequestMethod.POST) 
	public @ResponseBody HmiResultObj createCity(@RequestBody DefCity defCity, HttpServletRequest request ){
		User user=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if(user==null || user.getUserType()==UserTypes.USER_TYPE_MEMBER_INT){
					return null;
		}
		return definitionService.createCity(defCity);
	}
	
	@RequestMapping(value="/city/update", method = RequestMethod.POST) 
	public @ResponseBody HmiResultObj updateCity(@RequestBody DefCity defCity, HttpServletRequest request ){
		User user=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if(user.getUserType()!=UserTypes.USER_TYPE_ADMIN_INT){
				return null;
		}
		return definitionService.updateCity(defCity);
	}
	
	@RequestMapping(value="/city/delete", method = RequestMethod.POST) 
	public @ResponseBody HmiResultObj deleteCity(@RequestBody DefCity defCity, HttpServletRequest request ){
		User user=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if(user==null || user.getUserType()==UserTypes.USER_TYPE_MEMBER_INT){
					return null;
		}
		return definitionService.deleteCity(defCity);
	}
	
	/**
	 * @author bahadir
	 * @category Definitions
	 * @comment 
	 *   İllerin bulunması için yazılmış olan rest service.
	 * 
	 * @param request
	 * @return HmiResultObj
	 */
	
	@RequestMapping(value="/state/findStates", method = RequestMethod.POST) 
	public @ResponseBody List<DefState> findStates(HttpServletRequest request ){
		
		
		return definitionService.findStates();
	}
	
	@RequestMapping(value="/state/create", method = RequestMethod.POST) 
	public @ResponseBody HmiResultObj createState(@RequestBody DefState defState, HttpServletRequest request ){
		User user=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if(user.getUserType()!=UserTypes.USER_TYPE_ADMIN_INT){
				return null;
		}
		return definitionService.createState(defState);
	}
	
	@RequestMapping(value="/state/update", method = RequestMethod.POST) 
	public @ResponseBody HmiResultObj updateState(@RequestBody DefState defState, HttpServletRequest request ){
		User user=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if(user.getUserType()!=UserTypes.USER_TYPE_ADMIN_INT){
				return null;
		}
		return definitionService.updateState(defState);
	}
	
	@RequestMapping(value="/state/delete", method = RequestMethod.POST) 
	public @ResponseBody HmiResultObj deleteState(@RequestBody DefState defState, HttpServletRequest request ){
		User user=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if(user.getUserType()!=UserTypes.USER_TYPE_ADMIN_INT){
				return null;
		}
		return definitionService.deleteState(defState);
	}
	
	
	
	
	
	@RequestMapping(value="/firm/findFirm/{firmId}", method = RequestMethod.POST) 
	public @ResponseBody DefFirm findFirmById(@PathVariable("firmId") int firmId,HttpServletRequest request ){
		return definitionService.findFirmsById(firmId);
	}
	
	@RequestMapping(value="/firm/findFirms", method = RequestMethod.POST) 
	public @ResponseBody List<DefFirm> findFirms(HttpServletRequest request ){
		User user=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if((user.getUserType()==UserTypes.USER_TYPE_MEMBER_INT)){
			return null;
		}
		
		return definitionService.findFirms();
	}
	
	
	@RequestMapping(value="/firm/create", method = RequestMethod.POST) 
	public @ResponseBody HmiResultObj createFirm(@RequestBody DefFirm defFirm, HttpServletRequest request ){
		User user=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if(user.getUserType()!=UserTypes.USER_TYPE_ADMIN_INT){
				return null;
		}
		
		HmiResultObj hmiResultObj=definitionService.createFirms(defFirm);
		if(hmiResultObj.getResultStatu()==ResultStatuObj.RESULT_STATU_SUCCESS){
			List<DefFirm> defFirms=definitionService.findFirms();
			//if(GlobalUtil.defFirms==null){
				GlobalUtil.defFirms=new HashMap<Integer, DefFirm>();
			//}
			for (DefFirm defF : defFirms) {
				GlobalUtil.defFirms.put(defFirm.getFirmId(), defF);
			}
		
		}
		
		return hmiResultObj;
	}
	
	
	@RequestMapping(value="/firm/update", method = RequestMethod.POST,consumes="application/json",produces="application/json") 
	public @ResponseBody HmiResultObj updateFirm(@RequestBody DefFirm defFirm, HttpServletRequest request ){
		User user=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if(user.getUserType()!=UserTypes.USER_TYPE_ADMIN_INT){
				return null;
		}
		return definitionService.updateFirms(defFirm);
	}
	
	@RequestMapping(value="/firm/delete", method = RequestMethod.POST) 
	public @ResponseBody HmiResultObj deleteFirm(@RequestBody DefFirm defFirm, HttpServletRequest request ){
		User user=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if(user.getUserType()!=UserTypes.USER_TYPE_ADMIN_INT){
				return null;
		}
		
		DefinitionFacadeService definitionFacadeService=definitionFacade;
		
		HmiResultObj hmiResultObj=definitionFacadeService.canFirmDelete(defFirm.getFirmId());
		if(hmiResultObj.getResultStatu()==ResultStatuObj.RESULT_STATU_FAIL){
			return hmiResultObj;
		}
		
		
		return definitionService.deleteFirms(defFirm);
	}
	
	

	
	
	
	
	
	
	
	
	

	@RequestMapping(value="/level/findLevel", method = RequestMethod.POST) 
	public @ResponseBody List<DefLevelInfo> findLevel(HttpServletRequest request ) throws UnAuthorizedUserException{
		User user=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if((user.getUserType()==UserTypes.USER_TYPE_MEMBER_INT)){
			throw new UnAuthorizedUserException("USER NOT AUTHORIZED");
		}
		return definitionService.findLevelInfo();
	}
	
	
	
	@RequestMapping(value="/level/findLevelById/{levelId}", method = RequestMethod.POST) 
	public @ResponseBody DefLevelInfo findLevelById(@PathVariable("levelId") int levelId ,HttpServletRequest request ){
		User user=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if(user.getUserType()!=UserTypes.USER_TYPE_ADMIN_INT){
			return null;
		}
		return definitionService.findLevelInfoById(levelId);
	}
	
	@RequestMapping(value="/level/create", method = RequestMethod.POST) 
	public @ResponseBody HmiResultObj createLevelInfo(@RequestBody DefLevelInfo defLevelInfo, HttpServletRequest request ){
		User user=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if(user.getUserType()!=UserTypes.USER_TYPE_ADMIN_INT){
			return null;
		}
		return definitionService.createLevelInfo(defLevelInfo);
	}
	
	@RequestMapping(value="/level/update", method = RequestMethod.POST) 
	public @ResponseBody HmiResultObj updateLevelInfo(@RequestBody DefLevelInfo defLevelInfo, HttpServletRequest request ){
		User user=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if(user.getUserType()!=UserTypes.USER_TYPE_ADMIN_INT){
			return null;
		}
		return definitionService.updateLevelInfo(defLevelInfo);
	}
	
	@RequestMapping(value="/level/delete", method = RequestMethod.POST) 
	public @ResponseBody HmiResultObj deleteLevelInfo(@RequestBody DefLevelInfo defLevelInfo, HttpServletRequest request ){
		User user=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if(user.getUserType()!=UserTypes.USER_TYPE_ADMIN_INT){
				return null;
		}
		return definitionService.deleteLevelInfo(defLevelInfo);
	}
	
	
	
	
	@RequestMapping(value="/calendar/time/find", method = RequestMethod.POST) 
	public @ResponseBody DefCalendarTimes findCalendarTimes(HttpServletRequest request ){
		User user=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if(user.getUserType()!=UserTypes.USER_TYPE_ADMIN_INT){
			return null;
		}
		return definitionService.findCalendarTimes();
	}
	
	@RequestMapping(value="/calendar/time/create", method = RequestMethod.POST) 
	public @ResponseBody HmiResultObj createDefCalendarTimes(@RequestBody DefCalendarTimes defCalendarTimes, HttpServletRequest request ){
		User user=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if(user.getUserType()!=UserTypes.USER_TYPE_ADMIN_INT){
				return null;
		}
		
		GlobalUtil.defCalendarTimes=defCalendarTimes;
		
		return definitionService.createDefCalendarTimes(defCalendarTimes);
	}
	
	
	
	
	
	
	public DefinitionService getDefinitionService() {
		return definitionService;
	}

	public void setDefinitionService(DefinitionService definitionService) {
		this.definitionService = definitionService;
	}

	public MenuService getMenuService() {
		return menuService;
	}

	public void setMenuService(MenuService menuService) {
		this.menuService = menuService;
	}
}
