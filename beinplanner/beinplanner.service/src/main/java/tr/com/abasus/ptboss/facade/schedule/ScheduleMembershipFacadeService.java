package tr.com.abasus.ptboss.facade.schedule;

import tr.com.abasus.ptboss.packetsale.entity.PacketSaleFactory;
import tr.com.abasus.ptboss.program.entity.ProgramFactory;
import tr.com.abasus.ptboss.result.entity.HmiResultObj;
import tr.com.abasus.ptboss.schedule.entity.ScheduleFactory;
import tr.com.abasus.ptboss.schedule.entity.ScheduleMembershipTimePlan;

public interface ScheduleMembershipFacadeService {

	public abstract HmiResultObj canScheduleCreate(ScheduleFactory scheduleFactory);


	public abstract HmiResultObj canScheduleFreeze(ScheduleFactory smp,ScheduleFactory smpInDb,ProgramFactory pmf);

	public abstract HmiResultObj canScheduleUnFreeze(ScheduleFactory smp,ScheduleMembershipTimePlan scheduleMembershipTimePlan,ProgramFactory pmf);

	public abstract HmiResultObj canScheduleDelete(PacketSaleFactory packetSaleFactory);

	
}
