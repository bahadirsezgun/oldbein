package tr.com.abasus.util;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

import tr.com.abasus.ptboss.settings.entity.DbMailTbl;


public class SmtpAuthenticator extends Authenticator {
public SmtpAuthenticator() {

    super();
}

@Override
public PasswordAuthentication getPasswordAuthentication() {
	
	DbMailTbl dbMailTbl=GlobalUtil.mailSettings;
	
	
 String username = dbMailTbl.getMailUsername();// "info@abasus.com.tr";
 String password = dbMailTbl.getMailPassword();//"xqEwff3k6LMy22fdsweeewsaa";
    if ((username != null) && (username.length() > 0) && (password != null) 
      && (password.length   () > 0)) {

        return new PasswordAuthentication(username, password);
    }

    return null;
}
}