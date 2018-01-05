package tr.com.abasus.ptboss.user.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import tr.com.abasus.ptboss.ptuser.entity.User;
import tr.com.abasus.ptboss.ptuser.service.ProcessUserService;
import tr.com.abasus.ptboss.result.entity.HmiResultObj;
import tr.com.abasus.util.OhbeUtil;
import tr.com.abasus.util.ResultStatuObj;
import tr.com.abasus.util.SessionUtil;

@Controller
@RequestMapping(value="/staff")
public class StaffController {

	
	
	@Autowired 
	ProcessUserService processUserService;
	
	
	
	@RequestMapping(value="/testMth", method = RequestMethod.POST,consumes="application/json",produces="application/json") 
	public @ResponseBody HmiResultObj test(@RequestBody User user,HttpServletRequest request){
		 String yourIP = request.getRemoteAddr();
		 
		 ////System.out.println("YOUR IP IS "+yourIP);
		//////System.out.println("USER IS "+user.getUserId()+" USERNAME "+user.getUserName());
		HmiResultObj hmiResultObj=new HmiResultObj();
		hmiResultObj.setResultStatu(1);
		hmiResultObj.setResultMessage("NoPlanFound");
		return hmiResultObj;
	}
	
	
	@RequestMapping(value="/createProfileUrl", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody HmiResultObj createProfileUrl(@RequestBody User user) {
	
		HmiResultObj hmiResultObj=new HmiResultObj();
		User userInDb=processUserService.findById(user.getUserId());
		
		if(userInDb==null){
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
			hmiResultObj.setResultMessage("canNotFindUser");
			
		}else{
			
			deleteExistProfileFile(userInDb.getProfileUrl());
			userInDb.setProfileUrl(user.getProfileUrl());
			userInDb.setUrlType(0);
			hmiResultObj=processUserService.updateUser(userInDb);
		}
		
		
		return hmiResultObj;
		
	}
	
	
	@RequestMapping(value="/setPersonalBonusType/{userId}/{bonusTypeP}", method = RequestMethod.POST)
	public @ResponseBody HmiResultObj setPersonalBonusType(@PathVariable("userId") long userId,@PathVariable("bonusTypeP") int bonusTypeP) {
		User user= processUserService.findById(userId);
		
		if(user==null){
			HmiResultObj hmiResultObj=new HmiResultObj();
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
			hmiResultObj.setResultMessage("canNotFindUser");
			return hmiResultObj;
		}
		
		user.setBonusTypeP(bonusTypeP);
		return processUserService.updateUser(user);
	}
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/uploadProfileFile", method = RequestMethod.POST,consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE) 
	public  void uploadProfileFile(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		
			User sessionUser=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
			if(sessionUser!=null){
			HttpSession session = request.getSession(true);
			FileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);
			List<FileItem> uploadedItems = null;
			FileItem fileItem = null;
			
			long userId=Long.parseLong(request.getParameter("userId"));
			
			User user=processUserService.findById(userId);
			deleteExistProfileFile(user.getProfileUrl());
			try {
				// iterate over all uploaded files
				////System.out.println("BASLADI");
				uploadedItems = upload.parseRequest(request);
				
				Iterator<FileItem> i = uploadedItems.iterator();
				while (i.hasNext()) {
					fileItem =  i.next();
		
					if (fileItem.isFormField() == false) {
						if (fileItem.getSize() > 0) {
							//File uploadedFile = null;
							String myFullFileName = fileItem.getName();
							String myFileNameArr[] = myFullFileName.split("\\.");
							String extension = myFileNameArr[myFileNameArr.length-1];// Windows
							
							String myFileName=userId+"."+extension;
							
							user.setProfileUrl(myFileName);
							user.setUrlType(1);
							processUserService.updateUser(user);
		            		createFileInAmazon(request,fileItem, myFileName);
		            		
		            		
						}
					}
				}
			} catch (FileUploadException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			}
			
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
	
	@RequestMapping(value="/setClassBonusType/{userId}/{bonusTypeC}", method = RequestMethod.POST)
	public @ResponseBody HmiResultObj setClassBonusType(@PathVariable("userId") long userId,@PathVariable("bonusTypeC") int bonusTypeC) {
		User user= processUserService.findById(userId);
		
		if(user==null){
			HmiResultObj hmiResultObj=new HmiResultObj();
			hmiResultObj.setResultStatu(ResultStatuObj.RESULT_STATU_FAIL);
			hmiResultObj.setResultMessage("canNotFindUser");
			return hmiResultObj;
		}
		user.setBonusTypeC(bonusTypeC);
		return processUserService.updateUser(user);
	}
	
	
	private void deleteExistProfileFile(String fileName){
		
		File file=new File(OhbeUtil.ROOT_FIRM_FOLDER+"/"+fileName);
		if(file.exists()){
			file.delete();
		}
		
	}
	
}
