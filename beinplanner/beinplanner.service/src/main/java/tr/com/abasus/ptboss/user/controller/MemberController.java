package tr.com.abasus.ptboss.user.controller;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
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
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import tr.com.abasus.ptboss.ptuser.entity.User;
import tr.com.abasus.ptboss.ptuser.iuser.ProcessMember;
import tr.com.abasus.ptboss.ptuser.service.ProcessUserService;
import tr.com.abasus.ptboss.result.entity.HmiResultObj;
import tr.com.abasus.ptboss.user.service.MemberService;
import tr.com.abasus.util.OhbeUtil;
import tr.com.abasus.util.ResultStatuObj;
import tr.com.abasus.util.SessionUtil;
import tr.com.abasus.util.UserTypes;

@Controller
@RequestMapping(value="/member")
public class MemberController {

	@Autowired 
	ProcessUserService processUserService;
	
	@Autowired 
	MemberService memberService;
	
	@Autowired
	ProcessMember processMember;
	
	
	
	
	@RequestMapping(value="/createProfileUrl", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody HmiResultObj createMemberProfileUrl(@RequestBody User user) {
	
		User userInDb= processUserService.findById(user.getUserId());
		deleteExistProfileFile(userInDb.getProfileUrl());
		userInDb.setProfileUrl(user.getProfileUrl());
		userInDb.setUrlType(0);
		return processUserService.updateUser(userInDb);
		
	}
	
	@RequestMapping(value = "/get/profile/{userId}/{random}", method=RequestMethod.GET)
	public ResponseEntity<byte[]> showImageOnId(@PathVariable("userId") long userId,@PathVariable("random") int random,HttpServletRequest request) throws IOException {
		ResponseEntity<byte[]> responseEntity =null;
		User user=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		
		if(user!=null){
			User member=processUserService.findById(userId);
			
		String fileName=OhbeUtil.ROOT_FIRM_FOLDER+"/"+member.getProfileUrl();
		File file=new File(fileName);
		InputStream in=new FileInputStream(file);
		byte[] media = IOUtils.toByteArray(in);
		HttpHeaders headers = new HttpHeaders();
		 responseEntity = new ResponseEntity(media, headers, HttpStatus.OK);
		}
	    return responseEntity;
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
							processMember.createUser(user);
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
	
	private void deleteExistProfileFile(String fileName){
		
		File file=new File(OhbeUtil.ROOT_FIRM_FOLDER+"/"+fileName);
		if(file.exists()){
			file.delete();
		}
		
	}
	
	

	
	@RequestMapping(value="/downloadExcelTemplate", method = RequestMethod.POST) 
	public void downloadExcelTemplate(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
	
		User user=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		int firmIdx=Integer.parseInt(request.getParameter("firmId"));
		
		////System.out.println("FIRM ID IS "+firmIdx);
		
		XSSFWorkbook wb = memberService.downloadExcelTemplate(firmIdx);
		ByteArrayOutputStream bo = new ByteArrayOutputStream();
		wb.write(bo);
		ServletOutputStream outStream = response.getOutputStream();

		String mimetype = "application/octet-stream";
		// }
		response.setContentType(mimetype);
		response.setContentLength(bo.size());
		String fileName = "UserList.xlsx";

		// sets HTTP header
		response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

		outStream.write(bo.toByteArray());

		outStream.flush();
		outStream.close();
	}
	
	@RequestMapping(value="/progressListener", method = RequestMethod.POST) 
	public @ResponseBody HmiResultObj progressListener(HttpServletRequest request) {
		HttpSession session = request.getSession(true);
		HmiResultObj hmiResultObj=(HmiResultObj)session.getAttribute(ResultStatuObj.EXCEL_STATU);
		return (HmiResultObj)session.getAttribute(ResultStatuObj.EXCEL_STATU);
	}
	
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/uploadMembers/{firmId}", method = RequestMethod.POST) 
	public void uploadMembers(@PathVariable int firmId ,HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		User user=(User)request.getSession().getAttribute(SessionUtil.SESSION_USER);
		 
		HttpSession session = request.getSession(true);
		FileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		
		HmiResultObj hmiResultObj=new HmiResultObj();
		hmiResultObj.setResultStatu(1);
		hmiResultObj.setResultMessage("DATA YÜKLEME BAŞLADI. LÜTFEN BEKLEYİNİZ.........");
		hmiResultObj.setLoadedValue(20);
		session.setAttribute(ResultStatuObj.EXCEL_STATU,hmiResultObj);
		
		List<FileItem> uploadedItems = null;
		FileItem fileItem = null;
		
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
						String myFullFileName = fileItem.getName(), myFileName = "", slashType = (myFullFileName.lastIndexOf("\\") > 0) ? "\\" : "/"; // Windows
						
						int startIndex = myFullFileName.lastIndexOf(slashType);
						myFileName =myFullFileName.substring(startIndex + 1, myFullFileName.length());
                       
						File file= createFileInAmazon(request,fileItem, myFileName);
						hmiResultObj.setResultStatu(1);
						hmiResultObj.setResultMessage("");
						session.setAttribute(ResultStatuObj.EXCEL_STATU,hmiResultObj);
						loginListesiniExcelFileIleYukle(file, session, firmId);
						//file.delete();
						
					}
				}
			}
			
			
			
			
			hmiResultObj=(HmiResultObj)session.getAttribute(ResultStatuObj.EXCEL_STATU);
			hmiResultObj.setResultStatu(0);
			hmiResultObj.setResultMessage("DATA YÜKLEME BAŞLADI. LÜTFEN BEKLEYİNİZ.........");
			hmiResultObj.setLoadedValue(100);
			
			
			session.setAttribute(ResultStatuObj.EXCEL_STATU,hmiResultObj);
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
	
	
	
	
	public String loginListesiniExcelFileIleYukle(File excelFile,HttpSession session,int firmIdx) {
	       
		HmiResultObj excelSessionObj=(HmiResultObj)session.getAttribute(ResultStatuObj.EXCEL_STATU);
    	
    	List<User> list = new ArrayList<User>();
		String result="";
		XSSFWorkbook wb=null;
        try {
	        wb = new XSSFWorkbook(new FileInputStream(excelFile));
        } catch (FileNotFoundException e1) {
	        // TODO Auto-generated catch block
	        e1.printStackTrace();
        } catch (IOException e1) {
	        // TODO Auto-generated catch block
	        e1.printStackTrace();
        }
		XSSFSheet sheet = wb.getSheetAt(0);
		int satirSayisi=sheet.getLastRowNum();
		int satir = 0;
		int kolon = 0;

		excelSessionObj.setLoadedValue(20);
		
		Row row1 = sheet.getRow(satir++);
		
		
		for (  Row row:sheet) {
			if(row.getRowNum()==0)continue;
			
			
			 if(row.getCell(0)!=null && row.getCell(1)!=null){
        			String usrAd=row.getCell(0).getStringCellValue();
        			
        			String usrSoyad=row.getCell(1).getStringCellValue();
        			String usrGsm="";
        			
        			if(usrAd.equals("") || usrAd.length()<2){
        				
        			}else{
        			
        			String userName=(usrAd.substring(0,1)).toLowerCase()+usrSoyad.toLowerCase();
        		
        			userName=userName.replaceAll("ö", "o");
        			userName=userName.replaceAll("ı", "i");
        			userName=userName.replaceAll("ü", "u");
        			userName=userName.replaceAll("ş", "s");
        			userName=userName.replaceAll("ğ", "g");
        			userName=userName.replaceAll("ç", "c");
        			
        			
        			userName=userName.replaceAll("Ö", "o");
        			userName=userName.replaceAll("İ", "i");
        			userName=userName.replaceAll("Ü", "u");
        			userName=userName.replaceAll("Ş", "s");
        			userName=userName.replaceAll("Ğ", "g");
        			userName=userName.replaceAll("Ç", "c");
        			
        			
        			if(row.getCell(2)!=null){
            			if(row.getCell(2).getCellType()==1)
            			 usrGsm=convertPhoneToLegal(row.getCell(2).getStringCellValue());
            			else if(row.getCell(2).getCellType()==0){
            				usrGsm=convertPhoneToLegal(new DecimalFormat("#").format(row.getCell(2).getNumericCellValue()));
            				
            			}
        			}
        			
        			String usrTel="";
        			if(row.getCell(3)!=null){
            			if(row.getCell(3).getCellType()==1)
            				usrTel=convertPhoneToLegal(row.getCell(3).getStringCellValue());
            			else if(row.getCell(3).getCellType()==0)
            				usrTel=convertPhoneToLegal(new DecimalFormat("#").format(row.getCell(3).getNumericCellValue()));
        			}
        			
        			String usrEmail="";
        			if(row.getCell(4)!=null){
            			if(row.getCell(4).getCellType()==1)
            				usrEmail=row.getCell(4).getStringCellValue();
        			}
        			
        			if(usrEmail.equals("")){
        				usrEmail=userName;
        			};
        			
        			String usrSsn="";
        			if(row.getCell(5)!=null){
            			if(row.getCell(5).getCellType()==1)
            				usrSsn=row.getCell(5).getStringCellValue();
            			else if(row.getCell(5).getCellType()==0)
            				usrSsn=new DecimalFormat("#").format(row.getCell(5).getNumericCellValue());
        			}
        			
        			Date dogumTarih=new Date();
        			if(row.getCell(6)!=null){
        				
        				if(row.getCell(6).getCellType()==1){
        					
        					String dogumTarihStr=row.getCell(6).getStringCellValue();
        					
        					dogumTarihStr=dogumTarihStr.replaceAll("[\\.]", "/");
        					dogumTarihStr=dogumTarihStr.replaceAll("-", "/");
        					
        					String[] dogumTarihiArr=dogumTarihStr.split("/");
        					
        					if(dogumTarihiArr.length>1){
        					   int gun=Integer.parseInt(dogumTarihiArr[0]);
        					   int ay=Integer.parseInt(dogumTarihiArr[1]);
        					   String yil=dogumTarihiArr[2];
        					   
        					   String gunStr=""+gun;
        					   if(gun<10)
        						   gunStr="0"+gun;
        						
        					   String ayStr=""+ay;
        					   if(ay<10)
        						   ayStr="0"+ay;
        						
        					   dogumTarihStr=gunStr+"/"+ayStr+"/"+yil;
        						
        					  
        					  dogumTarih=OhbeUtil.getThatDayFormatNotNull(dogumTarihStr, "dd/MM/yyyy");
        					}
        					
        				}else{
        				try {
	                        dogumTarih=row.getCell(6).getDateCellValue();
                        } catch (Exception e) {
                        	dogumTarih=new Date();
                        }
        				}
        			}
        			
        			User member=processUserService.findUserByEMail(usrEmail);
        			if(member==null){
        				member=new User();
        				
        				
        				int randomPIN = (int)(Math.random()*9000)+1000;
            			String password = ""+randomPIN;
            			member.setPassword(password);
            			
            			
        				member.setUserType(UserTypes.USER_TYPE_MEMBER_INT);
        				
        				member.setUserAddress("");
        				if(dogumTarih==null)
            				dogumTarih=new Date();
        				
        				member.setUserBirthday(dogumTarih);
        				member.setCityId(0);
        				member.setStateId(0);
        				
        				member.setUserName(usrAd);
        				member.setUserSurname(usrSoyad);
            			//dogumTarih=dogumTarih.replaceAll("[\\.]", "/");
            			
            			
        				member.setFirmId(firmIdx);
        				member.setUserSsn(usrSsn);
        				member.setUserGsm(usrGsm);
        				member.setUserPhone(usrTel);
        				member.setUserEmail(usrEmail);
            			
        				member.setUserComment("");
        				
        				
        				processMember.createUser(member);
        				
        			}else{
        				
        				/*
        				usrSoyad=usrSoyad.replaceAll(" ","");
        				
        				String userName2=usrAd.toLowerCase()+usrSoyad.trim().toLowerCase();
                		
        				userName2=userName2.replaceAll("ö", "o");
        				userName2=userName2.replaceAll("ı", "i");
        				userName2=userName2.replaceAll("ü", "u");
        				userName2=userName2.replaceAll("ş", "s");
        				userName2=userName2.replaceAll("ğ", "g");
        				userName2=userName2.replaceAll("ç", "c");
        				
        				userName2=userName2.replaceAll("Ö", "o");
        				userName2=userName2.replaceAll("İ", "i");
        				userName2=userName2.replaceAll("Ü", "u");
        				userName2=userName2.replaceAll("Ş", "s");
        				userName2=userName2.replaceAll("Ğ", "g");
        				userName2=userName2.replaceAll("Ç", "c");
        				
        				usrEmail=userName2;
        				*/
        				
        			}
        			
        			
        			
        			int loadedValue=Math.round(((row.getRowNum()*100)/satirSayisi));
        			
        			
        			excelSessionObj.setLoadedValue(Integer.parseInt(""+loadedValue));
        			//session.removeAttribute(ResultStatuObj.EXCEL_STATU);
        			try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
					}
        			session.setAttribute(ResultStatuObj.EXCEL_STATU,excelSessionObj);
        		}
			}
			
			
		}
		
		excelSessionObj.setResultStatu(0);
		result+="Yükleme Tamamlandı";
		//urunDao.urunListesiniKaydet(list);
	    return result;
		
    }
	
	
    private String convertPhoneToLegal(String phoneNumber){
    	String convertedPhoneNumber="";
    	
    	if(!phoneNumber.equals("")){
    		
    		phoneNumber=phoneNumber.replaceAll("[(]", "");
    		phoneNumber=phoneNumber.replaceAll("[)]", "");
    		phoneNumber=phoneNumber.replaceAll("[ ]", "");
    		phoneNumber=phoneNumber.replaceAll("[-]", "");
    		phoneNumber=phoneNumber.replaceAll("[\\.]", "");
    		phoneNumber=phoneNumber.trim();
    		if(phoneNumber.startsWith("0")){
    				if(phoneNumber.length()==11){
    				    String newPhone="("+phoneNumber.substring(1,4)+") ";
    				    newPhone+=phoneNumber.substring(4,7)+"-"+phoneNumber.substring(7,11);
    				    phoneNumber=newPhone;
    				}else{
    					phoneNumber="";
    				}
    		}else{
    			
    			if(phoneNumber.length()==10){
    				phoneNumber="0"+phoneNumber;
    				 String newPhone="("+phoneNumber.substring(1,4)+") ";
 				    newPhone+=phoneNumber.substring(4,7)+"-"+phoneNumber.substring(7,11);
 				    phoneNumber=newPhone;
    			}else{
    				
    				
    				phoneNumber="";
    			}
    			
    		}
    		
    	}
    	
    	convertedPhoneNumber=phoneNumber;
    	
    	
    	
    	return convertedPhoneNumber;
    }
	
    private int getUserCompletePercent(User user){
    	
    	int percent=100;
    	if(user.getStateId()>0 
    			&& user.getCityId()>0 
    			&& !user.getUserGsm().equals("")
    			&& !user.getUserAddress().equals("")
    			&& !user.getUserComment().equals("")
    			&& user.getUserGender()>0
    			){
    		percent=100;
    	}else if(!user.getUserGsm().equals("")
    			&& !user.getUserAddress().equals("")
    			&& !user.getUserComment().equals("")
    			&& user.getUserGender()>0
    			){
    		percent=80;
    	}else if(!user.getUserAddress().equals("")
    			&& !user.getUserComment().equals("")
    			&& user.getUserGender()>0
    			){
    		percent=60;
    	}else if(user.getProfileUrl()!=null){
    		percent=50;
    	}else if( !user.getUserComment().equals("")
        			&& user.getUserGender()>0
        		){
        	percent=45;
        }else if(!user.getUserComment().equals("")
    			&& user.getUserGender()>0
    			){
    		percent=40;
    	}else if( user.getUserGender()>0
    			){
    		percent=20;
    	}else{
    		percent=10;
    	}
    	
    	return percent;
    }
    
	public MemberService getMemberService() {
		return memberService;
	}
	public void setMemberService(MemberService memberService) {
		this.memberService = memberService;
	}

	
}
