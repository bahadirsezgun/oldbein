package tr.com.abasus.ptboss.update.screen;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import org.apache.commons.io.FileUtils;

import tr.com.abasus.util.OhbeUtil;

public class ScreenUpdateStart extends Thread {

	public ScreenUpdateStart() {
		
	}

	@Override
    public void run() {
		 try {
	        Thread.sleep(1000);
        } catch (InterruptedException e1) {
	        // TODO Auto-generated catch block
	        // e1.printStackTrace();
        }
          if(OhbeUtil.PLACE==OhbeUtil.AMAZON){
  			
        	boolean fd=downloadFileToLocal();
        	////System.out.println("FILE IS DOWNLOADED "+fd);      	
        	
        	
        	try {Thread.sleep(1000);} catch (InterruptedException e1) {}  
        	  
        	  
  			Process prc=null;
  			try {
  				////System.out.println("SHELL SCRIPT CALLED BEFORE ...");
  				String cmds[]={"/bin/bash","-c","sudo /home/ec2-user/autoUpdate.sh"};
  				prc = Runtime.getRuntime().exec(cmds);
  				
  				InputStream errorIS= prc.getErrorStream();
  				InputStream inputIS= prc.getInputStream();
  				int b;
  				while ( ( b = errorIS.read() ) != -1 )
  				 {
  						char c = (char)b;         
  						System.out.print(""+(char)c); //This prints out content that is unreadable.
  				 }
  				////System.out.println("--------------------------------------------------------");
  				int bK;
  				while ( ( bK = inputIS.read() ) != -1 )
  				 {
  						char c = (char)bK;         
  						System.out.print(""+(char)c); //This prints out content that is unreadable.
  				 }
  				
  				////System.out.println("SHELL SCRIPT CALLED ...");
  				
  				
  	 
  			} catch (Exception e) {
  				e.printStackTrace();
  			}
  		}
    }
	
	
	private static boolean downloadFileToLocal(){
		String urlFile="https://s3-us-west-2.amazonaws.com/beinplanner/beinplanner.war";
		 File beinplanner=new File(OhbeUtil.ROOT_FIRM_FOLDER +"/beinplanner.war");
	        
		try {
	         URL url=new URL(urlFile);
	         if(beinplanner.exists()){
	        	 beinplanner.delete();
	         }
	         
	         ////System.out.println("FILE IS START TO DOWNLOAD "+beinplanner.length()+"  Time "+new Date());
	         
	         FileUtils.copyURLToFile(url, beinplanner);
	         
	         ////System.out.println("FILE IS DOWNLOADED "+beinplanner.length()+"  Time "+new Date());
	         
	        
	         
       } catch (MalformedURLException e) {
       	if(beinplanner.exists()){
	        	 beinplanner.delete();
	         }
       	  return false;
       } catch (IOException e) {
       	if(beinplanner.exists()){
	        	 beinplanner.delete();
	         }
       	  return false;
       }
		
		return true;
	}

}
