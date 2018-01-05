package tr.com.abasus.ptboss.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.session.HttpSessionEventPublisher;

//@Configuration
//@ImportResource({ "classpath:webSecurityConfig.xml" })
//@EnableWebSecurity
public class SecSecurityConfig extends WebSecurityConfigurerAdapter {

 public SecSecurityConfig() {
     super();
 }

 @Override
 protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
    
 }

 @Override
 protected void configure(final HttpSecurity http) throws Exception {
     // @formatter:off
	 
	 http
     .csrf().disable();
	 
	 http.sessionManagement()
     .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);
	 
	 
	 http.sessionManagement().invalidSessionUrl("/sessionExpired.html");
	 
	 //http.sessionManagement().maximumSessions(50).expiredUrl("/sessionExpired.html");
	 
	 http.sessionManagement()
	    .sessionFixation().migrateSession();
	 /*
     http
     .csrf().disable()
     .sessionManagement()
     .sessionFixation().migrateSession()
     .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
     .invalidSessionUrl("/invalidSession.html")
     .maximumSessions(2)
     .expiredUrl("/sessionExpired.html");
*/
     // @formatter:on
 }


 @Bean
 public HttpSessionEventPublisher httpSessionEventPublisher() {
     return new HttpSessionEventPublisher();
 }
 
 @Bean
 public SessionRegistry sessionRegistry() {            
     return new SessionRegistryImpl();
 }



}
