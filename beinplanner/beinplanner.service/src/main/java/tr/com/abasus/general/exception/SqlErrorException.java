package tr.com.abasus.general.exception;

public class SqlErrorException extends Exception{
	
	private static final long serialVersionUID = 2972378749688465306L;

	public SqlErrorException() {
		super();
	}
	
	public SqlErrorException(String msg) {
		super(msg);
	}
	
}