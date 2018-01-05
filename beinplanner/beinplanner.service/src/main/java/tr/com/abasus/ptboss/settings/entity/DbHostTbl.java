package tr.com.abasus.ptboss.settings.entity;


public class DbHostTbl {

	public DbHostTbl() {
		// TODO Auto-generated constructor stub
	}

	private String dbUrl;
	private int dbPort;
	private String dbUsername;
	private String dbPassword;

	
    public String getDbUrl() {
    	return dbUrl;
    }

	
    public void setDbUrl(String dbUrl) {
    	this.dbUrl = dbUrl;
    }


	
    public int getDbPort() {
    	return dbPort;
    }


	
    public void setDbPort(int dbPort) {
    	this.dbPort = dbPort;
    }


	
    public String getDbUsername() {
    	return dbUsername;
    }


	
    public void setDbUsername(String dbUsername) {
    	this.dbUsername = dbUsername;
    }


	
    public String getDbPassword() {
    	return dbPassword;
    }


	
    public void setDbPassword(String dbPassword) {
    	this.dbPassword = dbPassword;
    }

}
