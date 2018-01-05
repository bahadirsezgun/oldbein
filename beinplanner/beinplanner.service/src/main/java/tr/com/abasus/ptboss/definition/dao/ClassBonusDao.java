package tr.com.abasus.ptboss.definition.dao;

import java.util.List;

import tr.com.abasus.ptboss.definition.entity.DefBonus;
import tr.com.abasus.ptboss.result.entity.HmiResultObj;

public interface ClassBonusDao {

	public List<DefBonus> findClassRateBonus(long userId);
	public HmiResultObj createClassRateBonus(DefBonus defBonus);
	public HmiResultObj deleteClassRateBonus(long bonusId);
	
	
	public List<DefBonus> findClassStaticBonus(long userId);
	public HmiResultObj createClassStaticBonus(DefBonus defBonus);
	public HmiResultObj deleteClassStaticBonus(long bonusId);
	
	public List<DefBonus> findClassStaticRateBonus(long userId);
	public HmiResultObj createClassStaticRateBonus(DefBonus defBonus);
	public HmiResultObj deleteClassStaticRateBonus(long bonusId);
	
}
