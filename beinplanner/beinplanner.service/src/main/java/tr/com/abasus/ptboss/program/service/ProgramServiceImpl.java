package tr.com.abasus.ptboss.program.service;

import java.util.List;

import tr.com.abasus.ptboss.packetsale.dao.PacketSaleDao;
import tr.com.abasus.ptboss.program.dao.ProgramDao;
import tr.com.abasus.ptboss.program.entity.ProgramClass;
import tr.com.abasus.ptboss.program.entity.ProgramFactory;
import tr.com.abasus.ptboss.program.entity.ProgramFamily;
import tr.com.abasus.ptboss.program.entity.ProgramMembership;
import tr.com.abasus.ptboss.program.entity.ProgramMembershipDetail;
import tr.com.abasus.ptboss.program.entity.ProgramPersonal;
import tr.com.abasus.ptboss.result.entity.HmiResultObj;

public class ProgramServiceImpl implements ProgramService {

	
	ProgramDao programDao;
	
	PacketSaleDao packetSaleDao;
	
	
	
	
	
	@Override
	public List<ProgramFactory> findAllProgramPersonal(int firmId) {
		return programDao.findAllProgramPersonal(firmId);
	}

	@Override
	public List<ProgramFactory> findAllProgramsForPersonal() {
		return programDao.findAllProgramsForPersonal();
	}
	
	@Override
	public ProgramPersonal findProgramPersonal(int progId) {
		return programDao.findProgramPersonal(progId);
	}

	@Override
	public synchronized HmiResultObj createProgramPersonal(ProgramPersonal programPersonal) {
		return programDao.createProgramPersonal(programPersonal);
	}

	@Override
	public synchronized HmiResultObj deleteProgramPersonal(ProgramPersonal programPersonal) {
		/*
		if(packetSaleDao.isPersonalProgSaledBefore(programPersonal.getProgId())){
			HmiResultObj hmiResultObj=new HmiResultObj();
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
			hmiResultObj.setResultMessage("programUsedInSales");
			return hmiResultObj;
		}*/
		
		return programDao.deleteProgramPersonal(programPersonal);
	}

	
	
	@Override
	public List<ProgramFactory> findAllProgramsForClass() {
		return programDao.findAllProgramsForClass();
	}
	
	@Override
	public List<ProgramFactory> findAllProgramClass(int firmId) {
		return programDao.findAllProgramClass(firmId);
	}

	@Override
	public ProgramClass findProgramClass(int progId) {
		return programDao.findProgramClass(progId);
	}

	@Override
	public synchronized HmiResultObj createProgramClass(ProgramClass programClass) {
		return programDao.createProgramClass(programClass);
	}

	@Override
	public HmiResultObj deleteProgramClass(ProgramClass programClass) {
		
		
		return programDao.deleteProgramClass(programClass);
	}

	
	@Override
	public synchronized List<ProgramFactory> findAllProgramMembership(int firmId) {
		return programDao.findAllProgramMembership(firmId);
	}

	@Override
	public ProgramMembership findProgramMembership(int progId) {
		return programDao.findProgramMembership(progId);
	}

	@Override
	public synchronized HmiResultObj createProgramMembership(ProgramMembership programMembership) {
		return programDao.createProgramMembership(programMembership);
	}

	@Override
	public synchronized HmiResultObj deleteProgramMembership(ProgramMembership programMembership) {
		return programDao.deleteProgramMembership(programMembership);
	}

	@Override
	public List<ProgramMembershipDetail> findAllProgramMembershipDetail(int progId) {
		return programDao.findAllProgramMembershipDetail(progId);
	}

	@Override
	public ProgramMembershipDetail findProgramMembershipDetail(int progDetId) {
		return programDao.findProgramMembershipDetail(progDetId);
	}

	@Override
	public synchronized HmiResultObj createProgramMembershipDetail(ProgramMembershipDetail programMembershipDetail) {
		return programDao.createProgramMembershipDetail(programMembershipDetail);
	}

	@Override
	public synchronized HmiResultObj deleteProgramMembershipDetail(ProgramMembershipDetail programMembershipDetail) {
		return programDao.deleteProgramMembershipDetail(programMembershipDetail);
	}
	
	@Override
	public synchronized HmiResultObj deleteProgramMembershipDetailByProgId(int progId) {
		return programDao.deleteProgramMembershipDetailByProgId(progId);
	}

	public ProgramDao getProgramDao() {
		return programDao;
	}

	public void setProgramDao(ProgramDao programDao) {
		this.programDao = programDao;
	}

	public PacketSaleDao getPacketSaleDao() {
		return packetSaleDao;
	}

	public void setPacketSaleDao(PacketSaleDao packetSaleDao) {
		this.packetSaleDao = packetSaleDao;
	}

	@Override
	public List<ProgramFactory> findAllProgramFamily(int firmId) {
		return programDao.findAllProgramFamily(firmId);
	}

	@Override
	public ProgramFamily findProgramFamily(int progId) {
		return programDao.findProgramFamily(progId);
	}

	@Override
	public List<ProgramFactory> findAllProgramsForFamily() {
		return programDao.findAllProgramsForFamily();
	}

	@Override
	public HmiResultObj createProgramFamily(ProgramFamily programFamily) {
		return programDao.createProgramFamily(programFamily);
	}

	@Override
	public HmiResultObj deleteProgramFamily(ProgramFamily programFamily) {
		return programDao.deleteProgramFamily(programFamily);
	}

	

	

	

	

	

	
}
