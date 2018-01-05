package tr.com.abasus.util;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class SmtpGmailAuthenticator extends Authenticator{

	@Override
	protected PasswordAuthentication getPasswordAuthentication() {
		 String username = "bbcsezgun@gmail.com";
		 String password = "2162atilberkin";
		    if ((username != null) && (username.length() > 0) && (password != null) 
		      && (password.length   () > 0)) {

		        return new PasswordAuthentication(username, password);
		    }

		    return null;
	}

}
