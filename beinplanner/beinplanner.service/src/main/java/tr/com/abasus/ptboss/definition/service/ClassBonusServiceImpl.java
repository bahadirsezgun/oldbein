package tr.com.abasus.ptboss.definition.service;

import java.util.List;

import tr.com.abasus.ptboss.definition.dao.ClassBonusDao;
import tr.com.abasus.ptboss.definition.entity.DefBonus;
import tr.com.abasus.ptboss.result.entity.HmiResultObj;

public class ClassBonusServiceImpl implements ClassBonusService {

	ClassBonusDao classBonusDao;
	
	
	
	@Override
	public List<DefBonus> findClassRateBonus(long userId) {
		return classBonusDao.findClassRateBonus(userId);
	}

	@Override
	public HmiResultObj createClassRateBonus(DefBonus defBonus) {
		return classBonusDao.createClassRateBonus(defBonus);
	}

	@Override
	public HmiResultObj deleteClassRateBonus(long bonusId) {
		return classBonusDao.deleteClassRateBonus(bonusId);
	}

	

	@Override
	public List<DefBonus> findClassStaticBonus(long userId) {
		return classBonusDao.findClassStaticBonus(userId);
	}

	@Override
	public HmiResultObj createClassStaticBonus(DefBonus defBonus) {
		return classBonusDao.createClassStaticBonus(defBonus);
	}

	@Override
	public HmiResultObj deleteClassStaticBonus(long bonusId) {
		return classBonusDao.deleteClassStaticBonus(bonusId);
	}

	@Override
	public List<DefBonus> findClassStaticRateBonus(long userId) {
		return classBonusDao.findClassStaticRateBonus(userId);
	}

	@Override
	public HmiResultObj createClassStaticRateBonus(DefBonus defBonus) {
		return classBonusDao.createClassStaticRateBonus(defBonus);
	}

	@Override
	public HmiResultObj deleteClassStaticRateBonus(long bonusId) {
		return classBonusDao.deleteClassStaticRateBonus(bonusId);
	}

	public ClassBonusDao getClassBonusDao() {
		return classBonusDao;
	}

	public void setClassBonusDao(ClassBonusDao classBonusDao) {
		this.classBonusDao = classBonusDao;
	}

}
