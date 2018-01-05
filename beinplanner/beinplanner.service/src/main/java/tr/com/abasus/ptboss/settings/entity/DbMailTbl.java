package tr.com.abasus.ptboss.settings.entity;

public class DbMailTbl {

	private String hostName;
	private String fromName;
	private String mailUsername;
	private String mailPassword;
	
	private int smtpPort;
	private int smtpAuth;
	private int useSsl;
	
	
	public String getHostName() {
		return hostName;
	}
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String getMailUsername() {
		return mailUsername;
	}
	public void setMailUsername(String mailUsername) {
		this.mailUsername = mailUsername;
	}
	public String getMailPassword() {
		return mailPassword;
	}
	public void setMailPassword(String mailPassword) {
		this.mailPassword = mailPassword;
	}
	public String getFromName() {
		return fromName;
	}
	public void setFromName(String fromName) {
		this.fromName = fromName;
	}
	public int getSmtpPort() {
		return smtpPort;
	}
	public void setSmtpPort(int smtpPort) {
		this.smtpPort = smtpPort;
	}
	public int getSmtpAuth() {
		return smtpAuth;
	}
	public void setSmtpAuth(int smtpAuth) {
		this.smtpAuth = smtpAuth;
	}
	public int getUseSsl() {
		return useSsl;
	}
	public void setUseSsl(int useSsl) {
		this.useSsl = useSsl;
	}
}
