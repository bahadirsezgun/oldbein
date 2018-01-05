package tr.com.abasus.ptboss.backup.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import tr.com.abasus.ptboss.exceptions.UnAuthorizedUserException;
import tr.com.abasus.ptboss.ptuser.entity.User;
import tr.com.abasus.ptboss.result.entity.HmiResultObj;
import tr.com.abasus.util.OhbeUtil;
import tr.com.abasus.util.ResultStatuObj;
import tr.com.abasus.util.SessionUtil;
import tr.com.abasus.util.UserTypes;

@Controller
@RequestMapping(value="/dbbackup")
public class DbBackupController {

	
	@RequestMapping(value="/downloadDbBackupStatu", method = RequestMethod.POST) 
	public HmiResultObj downloadDbBackupStatu(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, UnAuthorizedUserException{
		HttpSession session = request.getSession(true);
		return (HmiResultObj)session.getAttribute(SessionUtil.DB_BACKUP);
	}
	@RequestMapping(value="/downloadDbBackup", method = RequestMethod.POST) 
	public void downloadDbBackup(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, UnAuthorizedUserException{
		
		HttpSession session = request.getSession(true);
		HmiResultObj hmiResultObj=new HmiResultObj();
		hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
		hmiResultObj.setResultMessage("startToBackUp");
		hmiResultObj.setLoadedValue(20);
		session.setAttribute(SessionUtil.DB_BACKUP, hmiResultObj);
		
		
		if(OhbeUtil.PLACE==OhbeUtil.AMAZON){
  			
  			Process prc=null;
  			try {
  				////System.out.println("SHELL SCRIPT CALLED BEFORE ...");
  				hmiResultObj.setLoadedValue(50);
  				session.setAttribute(SessionUtil.DB_BACKUP, hmiResultObj);
  				
  				String cmds[]={"/bin/bash","-c","sudo /home/ec2-user/dbbackup.sh sagali7611"};
  				prc = Runtime.getRuntime().exec(cmds);
  				prc.waitFor();
  				hmiResultObj.setLoadedValue(60);
  				hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
  				hmiResultObj.setResultMessage("backupProcessDone");
  				session.setAttribute(SessionUtil.DB_BACKUP, hmiResultObj);
  				
  				Thread.sleep(1500);
  				
  	 
  			} catch (Exception e) {
  				e.printStackTrace();
  			}
  		}
		hmiResultObj.setLoadedValue(100);
        hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS);
		hmiResultObj.setResultMessage("backupFileUploadStart");
		session.setAttribute(SessionUtil.DB_BACKUP, hmiResultObj);
		
		
		File fileBackUp=new File(OhbeUtil.ROOT_FIRM_FOLDER+"beinplanner.sql");
		byte[] myByteArray=new byte[(int) fileBackUp.length()];
		
		
		 ByteArrayOutputStream ous = null;
		 InputStream ios = null;
		
		 try {
		        ous = new ByteArrayOutputStream();
		        ios = new FileInputStream(fileBackUp);
		        int read = 0;
		        while ((read = ios.read(myByteArray)) != -1) {
		            ous.write(myByteArray, 0, read);
		        }
		 }finally {
		        try {
		            if (ous != null)
		                ous.close();
		        } catch (IOException e) {
		        }

		        try {
		            if (ios != null)
		                ios.close();
		        } catch (IOException e) {
		        }
		    }
		 
		
		ServletOutputStream outStream = response.getOutputStream();

		String mimetype = "application/octet-stream";
		// }
		response.setContentType(mimetype);
		response.setContentLength(myByteArray.length);
		String fileName = "beinplanner_uploaded.sql";

		// sets HTTP header
		response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

		outStream.write(myByteArray);

		outStream.flush();
		outStream.close();
		
	}
	
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/uploadDbBackup", method = RequestMethod.POST) 
	public void uploadDbBackup(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, UnAuthorizedUserException{
		User user=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		if((user.getUserType()!=UserTypes.USER_TYPE_ADMIN_INT)){
			throw new UnAuthorizedUserException("");
		}
		HttpSession session = request.getSession(true);
		FileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		
		HmiResultObj hmiResultObj=new HmiResultObj();
		hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
		hmiResultObj.setResultMessage("DATA YÜKLEME BAŞLADI. LÜTFEN BEKLEYİNİZ.........");
		hmiResultObj.setLoadedValue(20);
		session.setAttribute(ResultStatuObj.DB_STATU,hmiResultObj);
		
		List<FileItem> uploadedItems = null;
		FileItem fileItem = null;
		
		try {
			// iterate over all uploaded files
			uploadedItems = upload.parseRequest(request);
			
			Iterator<FileItem> i = uploadedItems.iterator();
			while (i.hasNext()) {
				fileItem =  i.next();

				if (fileItem.isFormField() == false) {
					if (fileItem.getSize() > 0) {
						//File uploadedFile = null;
						//String myFullFileName = fileItem.getName(), myFileName = "", slashType = (myFullFileName.lastIndexOf("\\") > 0) ? "\\" : "/"; // Windows
						
						//int startIndex = myFullFileName.lastIndexOf(slashType);
						String myFileName ="beinplanner_uploaded.sql";//myFullFileName.substring(startIndex + 1, myFullFileName.length());
                       
						File file=createFileInAmazon(request,fileItem, myFileName);
						hmiResultObj.setLoadedValue(50);
						hmiResultObj.setResultStatu(1);
						hmiResultObj.setResultMessage(file.getName());
						session.setAttribute(ResultStatuObj.DB_STATU,hmiResultObj);
						
					}
				}
			}
			
			
			
			
			hmiResultObj=(HmiResultObj)session.getAttribute(ResultStatuObj.DB_STATU);
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
			hmiResultObj.setResultMessage("dbRestoreFileUploaded");
			hmiResultObj.setLoadedValue(100);
			session.setAttribute(ResultStatuObj.DB_STATU,hmiResultObj);
			
			Thread.sleep(1500);
			
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS);
			hmiResultObj.setResultMessage("dbRestoreStarted");
			hmiResultObj.setLoadedValue(100);
			session.setAttribute(ResultStatuObj.DB_STATU,hmiResultObj);
			
			
			if(OhbeUtil.PLACE==OhbeUtil.AMAZON){
	  			
	  			Process prc=null;
	  			try {
	  				String cmds[]={"/bin/bash","-c","sudo /home/ec2-user/dbrestore.sh sagali7611"};
	  				prc = Runtime.getRuntime().exec(cmds);
	  				prc.waitFor();
	  				hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_SUCCESS);
	  				hmiResultObj.setResultMessage("restoreProcessDone");
	  				session.setAttribute(SessionUtil.DB_BACKUP, hmiResultObj);
	  				
	  				
	  	 
	  			} catch (Exception e) {
	  				e.printStackTrace();
	  			}
	  		}
			
			
			
		} catch (FileUploadException e) {
			e.printStackTrace();
			hmiResultObj.setResultStatu(1);
			hmiResultObj.setResultMessage("error_file_upload");
		} catch (Exception e) {
			e.printStackTrace();
			hmiResultObj.setResultStatu(1);
			hmiResultObj.setResultMessage("error_file_upload");
		}
		
	}
	
	@RequestMapping(value="/progressListener", method = RequestMethod.POST) 
	public @ResponseBody HmiResultObj progressListener(HttpServletRequest request) {
		HttpSession session = request.getSession(true);
		/*HmiResultObj hmiResultObj=(HmiResultObj)session.getAttribute(ResultStatuObj.DB_STATU);*/
		return (HmiResultObj)session.getAttribute(ResultStatuObj.DB_STATU);
	}
	
	
	private File createFileInAmazon(HttpServletRequest request,FileItem itemFile,String newFileName) throws IOException{
		
		File dir=new File(OhbeUtil.ROOT_FIRM_FOLDER);
		if(!dir.exists()){
			dir.mkdirs();
		}
		
		File file=new File(OhbeUtil.ROOT_FIRM_FOLDER+newFileName);
		FileOutputStream out = new FileOutputStream(file);
		InputStream    filecontent = itemFile.getInputStream();

	        int read = 0;
	        final byte[] bytes = new byte[1024];

	        while ((read = filecontent.read(bytes)) != -1) {
	            out.write(bytes, 0, read);
	        }
		
	        if (out != null) {
	            out.close();
	        }
	        if (filecontent != null) {
	            filecontent.close();
	        }
		return file;
		
	}
}
