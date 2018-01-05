package tr.com.abasus.ptboss.ptuser.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import tr.com.abasus.ptboss.ptuser.entity.StaffTracking;
import tr.com.abasus.ptboss.ptuser.entity.User;
import tr.com.abasus.ptboss.ptuser.service.ProcessUserService;
import tr.com.abasus.ptboss.result.entity.HmiResultObj;
import tr.com.abasus.util.GlobalUtil;
import tr.com.abasus.util.OhbeUtil;
import tr.com.abasus.util.SessionUtil;

@Controller
@RequestMapping(value="/stafftracking")
public class StaffTrackingController {

	@Autowired 
	ProcessUserService processUserService;
	
	
	@RequestMapping(value="/findStaffInOut", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE) 
	public @ResponseBody List<StaffTracking> findStaffInOut(@RequestBody StaffTracking staffTracking,HttpServletRequest request){
		User sessionUser=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if(sessionUser==null){
			throw new UsernameNotFoundException("");
		}
		Date startDate=OhbeUtil.getThatDateForNight( staffTracking.getStartDateStr(), GlobalUtil.global.getPtDbDateFormat());
		Date endDate=OhbeUtil.getThatDateForNight( staffTracking.getEndDateStr(), GlobalUtil.global.getPtDbDateFormat());
		return processUserService.findStaffTracking(staffTracking.getUserId(), startDate,endDate, staffTracking.getFirmId());
	}
	
	@RequestMapping(value="/deleteStaffInOut/{ptIdx}", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE) 
	public @ResponseBody HmiResultObj deleteStaffInOut(@PathVariable long ptIdx,HttpServletRequest request){
		User sessionUser=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if(sessionUser==null){
			throw new UsernameNotFoundException("");
		}
		StaffTracking staffTracking=new StaffTracking();
		staffTracking.setPtIdx(ptIdx);
		
		return processUserService.deleteStaffTracking(staffTracking);
	}
	
	
}
