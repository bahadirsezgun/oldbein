package tr.com.abasus.util;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.TimeZone;

import tr.com.abasus.ptboss.definition.entity.DefCalendarTimes;
import tr.com.abasus.ptboss.definition.entity.DefFirm;
import tr.com.abasus.ptboss.settings.entity.DbMailTbl;
import tr.com.abasus.ptboss.settings.entity.PtGlobal;
import tr.com.abasus.ptboss.settings.entity.PtRestrictions;
import tr.com.abasus.ptboss.settings.entity.Rules;

public class GlobalUtil {

	
	public static PtGlobal global=null;
	
	public static DbMailTbl mailSettings;
	
	public static Rules rules=null;
	
	public static DefCalendarTimes defCalendarTimes;
	
	
	public static PtRestrictions ptRestrictionsForCount=null;
	
	public static PtRestrictions ptRestrictionsForPacket=null;
	
	public static HashMap<Integer, DefFirm> defFirms=null;
	
	public static void setRestrictions(){
		
		String[] rstrs=ptRestrictionsForPacket.getFirmCount().split("-");
		
		if(rstrs[0].equals("1")){
			ptRestrictionsForPacket.setIndividualRestriction(1);
		}else{
			ptRestrictionsForPacket.setIndividualRestriction(0);
		}
		
		
		if(rstrs[1].equals("1")){
			ptRestrictionsForPacket.setGroupRestriction(1);
		}else{
			ptRestrictionsForPacket.setGroupRestriction(0);
		}
		
		
		if(rstrs[2].equals("1")){
			ptRestrictionsForPacket.setMembershipRestriction(1);
		}else{
			ptRestrictionsForPacket.setMembershipRestriction(0);
		}
		
	}
	
	
	
	
	public static Date getCurrentDateByTimeZone(){
		
		
		Calendar calendar = new GregorianCalendar();
		TimeZone timeZone = TimeZone.getTimeZone(GlobalUtil.global.getPtTz());
		calendar.setTimeZone(timeZone);
		String timeOfCome=calendar.get(Calendar.DATE)+"/"+(calendar.get(Calendar.MONTH)+1)+"/"+calendar.get(Calendar.YEAR)+" "+calendar.get(Calendar.HOUR_OF_DAY)+":"+calendar.get(Calendar.MINUTE);
		Date currentDate=OhbeUtil.getThatDayFormatNotNull(timeOfCome, "dd/MM/yyyy HH:mm");
	    return currentDate;
	}
	
	public static String getCurrentDateByTimeZoneStr(){
		Calendar calendar = new GregorianCalendar();
		TimeZone timeZone = TimeZone.getTimeZone(GlobalUtil.global.getPtTz());
		calendar.setTimeZone(timeZone);
		String timeOfCome=calendar.get(Calendar.DATE)+"/"+(calendar.get(Calendar.MONTH)+1)+"/"+calendar.get(Calendar.YEAR)+" "+calendar.get(Calendar.HOUR_OF_DAY)+":"+calendar.get(Calendar.MINUTE);
		return timeOfCome;
	}
	
	
}
