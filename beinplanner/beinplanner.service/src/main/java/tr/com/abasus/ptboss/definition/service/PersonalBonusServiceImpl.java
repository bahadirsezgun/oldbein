package tr.com.abasus.ptboss.definition.service;

import java.util.List;

import tr.com.abasus.ptboss.definition.dao.PersonalBonusDao;
import tr.com.abasus.ptboss.definition.entity.DefBonus;
import tr.com.abasus.ptboss.result.entity.HmiResultObj;

public class PersonalBonusServiceImpl implements PersonalBonusService {

	PersonalBonusDao personalBonusDao;
	public PersonalBonusDao getPersonalBonusDao() {
		return personalBonusDao;
	}

	public void setPersonalBonusDao(PersonalBonusDao personalBonusDao) {
		this.personalBonusDao = personalBonusDao;
	}
	
	
	@Override
	public List<DefBonus> findPersonalRateBonus(long userId) {
		return personalBonusDao.findPersonalRateBonus(userId);
	}

	@Override
	public HmiResultObj createPersonalRateBonus(DefBonus defBonus) {
		return personalBonusDao.createPersonalRateBonus(defBonus);
	}

	@Override
	public HmiResultObj deletePersonalRateBonus(long bonusId) {
		return personalBonusDao.deletePersonalRateBonus(bonusId);
	}

	

	@Override
	public List<DefBonus> findPersonalStaticBonus(long userId) {
		return personalBonusDao.findPersonalStaticBonus(userId);
	}

	@Override
	public HmiResultObj createPersonalStaticBonus(DefBonus defBonus) {
		return personalBonusDao.createPersonalStaticBonus(defBonus);
	}

	@Override
	public HmiResultObj deletePersonalStaticBonus(long bonusId) {
		return personalBonusDao.deletePersonalStaticBonus(bonusId);
	}

	@Override
	public List<DefBonus> findPersonalStaticRateBonus(long userId) {
		return personalBonusDao.findPersonalStaticRateBonus(userId);
	}

	@Override
	public HmiResultObj createPersonalStaticRateBonus(DefBonus defBonus) {
		return personalBonusDao.createPersonalStaticRateBonus(defBonus);
	}

	@Override
	public HmiResultObj deletePersonalStaticRateBonus(long bonusId) {
		return personalBonusDao.deletePersonalStaticRateBonus(bonusId);
	}

}
