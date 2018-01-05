package tr.com.abasus.ptboss.definition.dao;

import java.util.List;

import tr.com.abasus.ptboss.definition.entity.DefBonus;
import tr.com.abasus.ptboss.result.entity.HmiResultObj;

public interface PersonalBonusDao {

	public List<DefBonus> findPersonalRateBonus(long userId);
	public HmiResultObj createPersonalRateBonus(DefBonus defBonus);
	public HmiResultObj deletePersonalRateBonus(long bonusId);
	
	
	public List<DefBonus> findPersonalStaticBonus(long userId);
	public HmiResultObj createPersonalStaticBonus(DefBonus defBonus);
	public HmiResultObj deletePersonalStaticBonus(long bonusId);
	
	public List<DefBonus> findPersonalStaticRateBonus(long userId);
	public HmiResultObj createPersonalStaticRateBonus(DefBonus defBonus);
	public HmiResultObj deletePersonalStaticRateBonus(long bonusId);
	
	
}
