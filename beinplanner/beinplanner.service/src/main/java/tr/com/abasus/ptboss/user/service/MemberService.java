package tr.com.abasus.ptboss.user.service;

import java.io.File;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public interface MemberService {

	
	public void createMembersByExcelFile(File excelFile) throws Exception;

	public XSSFWorkbook downloadExcelTemplate(int firmId);
}
