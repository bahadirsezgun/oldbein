package tr.com.abasus.ptboss.user.service;

import java.io.File;
import java.util.List;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import tr.com.abasus.ptboss.ptuser.dao.ProcessUserDao;
import tr.com.abasus.ptboss.ptuser.entity.User;
import tr.com.abasus.util.LangUtil;

public class MemberServiceImpl implements MemberService {

	ProcessUserDao processUserDao;
	

	@Override
	public synchronized void createMembersByExcelFile(File excelFile) throws Exception {
		
		
		
	}

	@Override
	public synchronized XSSFWorkbook downloadExcelTemplate(int firmId) {
		List<User> users = processUserDao.findAllToMember(firmId);
		
		

		XSSFWorkbook xssfWorkbook = new XSSFWorkbook();
		XSSFSheet sheet = xssfWorkbook.createSheet("MEMBERS");
		
		Font font = xssfWorkbook.createFont();
	    font.setBoldweight(Font.BOLDWEIGHT_BOLD);
	    font.setColor(IndexedColors.WHITE.getIndex());
		CellStyle style = xssfWorkbook.createCellStyle();
	    style.setFont(font);
	    style.setFillBackgroundColor(IndexedColors.BLUE.getIndex());
	
	    style.setFillForegroundColor(IndexedColors.BLUE.getIndex());
	    style.setFillPattern(CellStyle.SOLID_FOREGROUND);

	    style.setBorderBottom(CellStyle.BORDER_THICK);
	    style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
	    style.setBorderLeft(CellStyle.BORDER_THIN);
	    style.setLeftBorderColor(IndexedColors.WHITE.getIndex());
	    style.setBorderRight(CellStyle.BORDER_THIN);
	    style.setRightBorderColor(IndexedColors.WHITE.getIndex());
	    style.setBorderTop(CellStyle.BORDER_THIN);
	    style.setTopBorderColor(IndexedColors.WHITE.getIndex());
	    
		int satir=0;
		int kolon=0;
		Row row = sheet.createRow(satir++);
		row.createCell(kolon++).setCellValue(LangUtil.LANG_AD);row.getCell(kolon-1).setCellStyle(style);
		row.createCell(kolon++).setCellValue(LangUtil.LANG_SOYAD);row.getCell(kolon-1).setCellStyle(style);
		row.createCell(kolon++).setCellValue(LangUtil.LANG_CEP_TELEFON);row.getCell(kolon-1).setCellStyle(style);
		row.createCell(kolon++).setCellValue(LangUtil.LANG_IS_TELEFON);row.getCell(kolon-1).setCellStyle(style);
		row.createCell(kolon++).setCellValue(LangUtil.LANG_EMAIL);row.getCell(kolon-1).setCellStyle(style);
		row.createCell(kolon++).setCellValue(LangUtil.LANG_TC_KIMLIK);row.getCell(kolon-1).setCellStyle(style);
		row.createCell(kolon++).setCellValue(LangUtil.LANG_DOGUM_TARIH);row.getCell(kolon-1).setCellStyle(style);
		row.createCell(kolon++).setCellValue(LangUtil.LANG_SUBE);row.getCell(kolon-1).setCellStyle(style);
		if(users!=null){
		for (User userTbl : users) {
			row = sheet.createRow(satir++);
			kolon=0;
			row.createCell(kolon++).setCellValue(userTbl.getUserName());
			row.createCell(kolon++).setCellValue(userTbl.getUserSurname());
			row.createCell(kolon++).setCellValue(userTbl.getUserGsm());
			row.createCell(kolon++).setCellValue(userTbl.getUserPhone());
			row.createCell(kolon++).setCellValue(userTbl.getUserEmail());
			row.createCell(kolon++).setCellValue(userTbl.getUserSsn());
			row.createCell(kolon++).setCellValue(userTbl.getUserBirthdayStr());
			row.createCell(kolon++).setCellValue(userTbl.getFirmName());
			
		}
		}
		
		
		return xssfWorkbook;
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

	public ProcessUserDao getProcessUserDao() {
		return processUserDao;
	}

	public void setProcessUserDao(ProcessUserDao processUserDao) {
		this.processUserDao = processUserDao;
	}
	
	
}
