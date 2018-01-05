package tr.com.abasus.ptboss.update.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import tr.com.abasus.ptboss.menu.entity.MenuRoleTbl;
import tr.com.abasus.ptboss.menu.entity.MenuTbl;
import tr.com.abasus.ptboss.menu.service.MenuService;
import tr.com.abasus.ptboss.result.entity.HmiResultObj;
import tr.com.abasus.ptboss.update.entity.PtbossUpdate;
import tr.com.abasus.ptboss.update.entity.UpdateObj;
import tr.com.abasus.ptboss.update.screen.ScreenUpdateStart;
import tr.com.abasus.ptboss.update.service.PtUpdateService;
import tr.com.abasus.util.OhbeUtil;

@Controller
@RequestMapping(value="/update")
public class PtUpdateController {

	
	@Autowired
	PtUpdateService ptUpdateService;
	
	@Autowired
	MenuService menuService;
	
	@RequestMapping(value="/controlUpdate", method = RequestMethod.POST) 
	public @ResponseBody PtbossUpdate findPtBossUpdate(@RequestBody UpdateObj updateObj,HttpServletRequest request ){
		
		////System.out.println("UPDATE WORKS "+updateObj.getVersion());
		
		PtbossUpdate ptbossUpdate=ptUpdateService.findPtBossUpdateByVersion(updateObj.getVersion());
		
		////System.out.println("ptbossUpdate Version :"+ptbossUpdate.getUpdVer());
		
		return ptbossUpdate;
	}
	
	@RequestMapping(value="/makeUpdate", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE) 
	public @ResponseBody HmiResultObj makeUpdate(@RequestBody UpdateObj updateObj,HttpServletRequest request ){
		
		
		String updateSql=updateObj.getUpdateSql();
	   if(updateSql!=null){
		if(!updateSql.equals("")){
			
			String sqlLine[]=updateSql.split("@");
			for (String sqlL1 : sqlLine) {
	            String sqlL[]=sqlL1.split("#");
	            
	            
	            String sql=sqlL[0];
	            String table=sqlL[1];
	            String col=sqlL[2];
	            
	            if(col.equals("-")){
	            	ptUpdateService.createTable(sql, table);
	            	
	            }else if(col.equals("INDEX")){
	            	ptUpdateService.createIndex(sql);
	            
	            }else if(col.equals("UPDATE")){
	            	ptUpdateService.updateTableOfColumn(sql, table, col);	
	            }else{
	            	ptUpdateService.createTableOfColumn(sql, table, col);	
	            }
	            
	            
            }
			
		}
	   }
		
		
		PtbossUpdate ptbossUpdate=new PtbossUpdate();
		ptbossUpdate.setUpdDate(new Date());
		ptbossUpdate.setUpdStatu(0);
		ptbossUpdate.setUpdVer(updateObj.getVersion());
		HmiResultObj hmiResultObj = ptUpdateService.createPtBossUpdate(ptbossUpdate);
		
		boolean defaults=createDefaults();
		
		
		if(OhbeUtil.PLACE==OhbeUtil.AMAZON){
			ScreenUpdateStart screenUpdate=new ScreenUpdateStart();
			Thread thread=new Thread(screenUpdate);
			thread.start();
		
		}
		
		return hmiResultObj;
	}
	
	
	
	private boolean createDefaults(){
	
		
		if(!menuService.findMenuByName("calDefTimes%")){
		
		MenuTbl menuTbl=new MenuTbl();
		menuTbl.setMenuId(405);
		menuTbl.setMenuName("calDefTimes");
		menuTbl.setUpperMenu(400);
		menuTbl.setMenuLink("#/calDefTimes");
		menuTbl.setMenuComment("Takvim Saatleri");
		menuTbl.setMenuSide(0);
		menuTbl.setMenuClass("");
		
		HmiResultObj hmiResultObj= menuService.createMenu(menuTbl);
		
		////System.out.println("CREATE DEFAULTS WORKS SET MENU TBL ");
		
		MenuRoleTbl menuRoleTbl=new MenuRoleTbl();
		
		menuRoleTbl.setMenuId(405);
		menuRoleTbl.setRoleId(6);
		menuRoleTbl.setRoleName("ADMIN");
		
		menuService.createMenuRole(menuRoleTbl);
		}
		
		
		return true;
	}
	
	
}
