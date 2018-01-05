package tr.com.abasus.util;

import java.util.Date;

public class StatuTypes {

	public static final int ALL=-1;
	public static final int ACTIVE=0;
	public static final int PASSIVE=1;
	public static final int FREEZE=2;
	public static final int NO_STARTED=3;
	public static final int FINISHED=4;
	
	public static final int TIMEPLAN_CANCEL=1;
	public static final int TIMEPLAN_NORMAL=0;
	public static final int TIMEPLAN_POSTPONE=2;
	
	
	public static String getStatuTypes(int statu){
		switch (statu) {
		case 0:
			return "active";
		case 1:
			return "passive";
		case 2:
			return "freeze";
		case 3:
			return "noStarted";
		case 4:
			return "finished";
		default:
			return "none";
		}
	}
	
	public static String getStatuByDate(Date startDate,Date endDate){
		
		Date currentDate=GlobalUtil.getCurrentDateByTimeZone();
		
		if(startDate.before(currentDate) && endDate.after(currentDate)){
			return getStatuTypes(ACTIVE);
		}else if(startDate.after(currentDate)){
			return getStatuTypes(NO_STARTED);
		}else if(endDate.before(currentDate)){
			return getStatuTypes(FINISHED);
		}else{
			return getStatuTypes(ALL);
		}
	}
	
	
	
}
