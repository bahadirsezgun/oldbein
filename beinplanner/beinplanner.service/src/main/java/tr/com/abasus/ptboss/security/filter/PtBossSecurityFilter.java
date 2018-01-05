package tr.com.abasus.ptboss.security.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;

import tr.com.abasus.ptboss.ptuser.entity.User;
import tr.com.abasus.util.SessionUtil;

public class PtBossSecurityFilter extends OncePerRequestFilter {

	
	
	 protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws ServletException, IOException {
		 
		 
	     if(req.getContextPath().equals("/ptboss") && (req.getPathInfo()!=null && !req.getPathInfo().startsWith("/login/loginUser") && !req.getPathInfo().startsWith("/mobile") && !req.getPathInfo().startsWith("/update/controlUpdate") )){
	    	
	    	 User user=null;
			 
			 if(req.getSession().getAttribute(SessionUtil.SESSION_USER)!=null){
				 user=(User)(req.getSession().getAttribute(SessionUtil.SESSION_USER));
		 	 }else{
		 		////System.out.println("USER IN PAGE NOT ..... IN SESSION ... ");
		 		
		 		RequestDispatcher requestDispatcher = req
	                    .getRequestDispatcher("/ptboss/lock.html");
	            requestDispatcher.forward(req, res);
	            
		 	 }
		 	 
		 }
	  
	        chain.doFilter(req, res);
	 }


	

	


}
