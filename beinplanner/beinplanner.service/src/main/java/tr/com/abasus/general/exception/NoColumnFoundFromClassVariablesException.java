package tr.com.abasus.general.exception;

public class NoColumnFoundFromClassVariablesException extends Exception {

	private static final long serialVersionUID = -6823307115490094370L;

	public NoColumnFoundFromClassVariablesException() {
		super();
	}

	public NoColumnFoundFromClassVariablesException(String message) {
		super(message);
	}

	public NoColumnFoundFromClassVariablesException(String message,
			Throwable cause) {
		super(message, cause);
	}

	public NoColumnFoundFromClassVariablesException(Throwable cause) {
		super(cause);
	}

}
