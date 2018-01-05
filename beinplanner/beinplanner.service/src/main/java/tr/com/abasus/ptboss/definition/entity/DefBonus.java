package tr.com.abasus.ptboss.definition.entity;

public class DefBonus {
  private long bonusId;
  private long userId;
  private double bonusValue;
  private int bonusCount;
  private int bonusProgId;
  private int bonusType;
  private int bonusIsType;
  
  
    private String progName;
  
  
	public long getBonusId() {
		return bonusId;
	}
	public void setBonusId(long bonusId) {
		this.bonusId = bonusId;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public double getBonusValue() {
		return bonusValue;
	}
	public void setBonusValue(double bonusValue) {
		this.bonusValue = bonusValue;
	}
	public int getBonusCount() {
		return bonusCount;
	}
	public void setBonusCount(int bonusCount) {
		this.bonusCount = bonusCount;
	}
	public int getBonusProgId() {
		return bonusProgId;
	}
	public void setBonusProgId(int bonusProgId) {
		this.bonusProgId = bonusProgId;
	}
	public String getProgName() {
		return progName;
	}
	public void setProgName(String progName) {
		this.progName = progName;
	}
	public int getBonusType() {
		return bonusType;
	}
	public void setBonusType(int bonusType) {
		this.bonusType = bonusType;
	}
	public int getBonusIsType() {
		return bonusIsType;
	}
	public void setBonusIsType(int bonusIsType) {
		this.bonusIsType = bonusIsType;
	}
  
  
  
  
  
}
