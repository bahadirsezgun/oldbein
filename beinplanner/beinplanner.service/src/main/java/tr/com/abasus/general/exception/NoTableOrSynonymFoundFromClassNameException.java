package tr.com.abasus.general.exception;

public class NoTableOrSynonymFoundFromClassNameException extends Exception {

	private static final long serialVersionUID = 2644409202282562188L;

	public NoTableOrSynonymFoundFromClassNameException() {
		super();
	}

	public NoTableOrSynonymFoundFromClassNameException(String message) {
		super(message);
	}

	public NoTableOrSynonymFoundFromClassNameException(String message,
			Throwable cause) {
		super(message, cause);
	}

	public NoTableOrSynonymFoundFromClassNameException(Throwable cause) {
		super(cause);
	}

}
