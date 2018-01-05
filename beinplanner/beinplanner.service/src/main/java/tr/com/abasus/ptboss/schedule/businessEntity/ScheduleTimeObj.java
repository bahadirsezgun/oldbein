package tr.com.abasus.ptboss.schedule.businessEntity;

public class ScheduleTimeObj {

	
	private int progDay;
	private String progStartTime;
	private String progDayName;
	
	
	public int getProgDay() {
		return progDay;
	}
	public void setProgDay(int progDay) {
		this.progDay = progDay;
	}
	public String getProgStartTime() {
		return progStartTime;
	}
	public void setProgStartTime(String progStartTime) {
		this.progStartTime = progStartTime;
	}
	@Override
	public String toString() {
		////System.out.println("------------------------------------------");
		////System.out.println("-------------SCHEDULE TIME OBJ----------------");
		////System.out.println("------------------------------------------");
		////System.out.println("progDay:"+progDay);
		////System.out.println("progStartTime:"+progStartTime);
		////System.out.println("------------------------------------------");
		////System.out.println("---------END-SCHEDULE TIME OBJ----------------");
		////System.out.println("------------------------------------------");
		return super.toString();
	}
	public String getProgDayName() {
		return progDayName;
	}
	public void setProgDayName(String progDayName) {
		this.progDayName = progDayName;
	}
	
	
	
}
