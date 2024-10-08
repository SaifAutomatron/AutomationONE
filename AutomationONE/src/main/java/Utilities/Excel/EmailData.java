package Utilities.Excel;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class EmailData {

	static EmailData r;
	public HashMap<String, String> emailDataMap=new HashMap<String, String>();

	public HashMap<String, String> getEmailDataMap()
	{
		return emailDataMap;
	}

	private EmailData()
	{
	}

	public static synchronized EmailData getInstance()
	{

		if(r==null)
		{
			r=new EmailData();
		}

		return r;
	}

	public boolean fetchEmailData() throws Exception
	{

		int iData=-1;
		boolean bFlag=false;
		EmailData.getInstance().getEmailDataMap().clear();
		String dataSheetPath=EnvironmentData.getInstance().environmentDataMap.get("EMAILSHEETPATH");
		String dataSheetName=EnvironmentData.getInstance().environmentDataMap.get("EMAILSHEET_NAME");
		String dataExcel=java.nio.file.Paths.get(dataSheetPath,dataSheetName+".xlsx").toString();


		FileInputStream file=new FileInputStream(new File(dataExcel));

		XSSFWorkbook workbook=new XSSFWorkbook(file);

		Sheet sheet = workbook.getSheet("EMAIL");

		int iRowNum = sheet.getLastRowNum();

		int iColCount=sheet.getRow(0).getLastCellNum();


		for (int iRow = 0; iRow <= iRowNum; iRow++) {

			bFlag=true;
			String strKey="";
			String strValue="";

			if(iRow==0)
				continue;

			if(sheet.getRow(iRow).getCell(0).getCellType()==CellType.STRING)
			{
				strKey=sheet.getRow(iRow).getCell(0).getStringCellValue().trim();
			}
			else if(sheet.getRow(iRow).getCell(0).getCellType()==CellType.NUMERIC)
			{
				strKey=sheet.getRow(iRow).getCell(0).getNumericCellValue()+"";
			}



			if(sheet.getRow(iRow).getCell(1,Row.MissingCellPolicy.CREATE_NULL_AS_BLANK)==null)
			{
				strValue="";
			}
			else {
				if(sheet.getRow(iRow).getCell(1).getCellType()==CellType.STRING)
				{
					strValue=sheet.getRow(iRow).getCell(1).getStringCellValue().trim();
				}
				else if(sheet.getRow(iRow).getCell(1).getCellType()==CellType.NUMERIC)
				{
					strValue=sheet.getRow(iRow).getCell(1).getNumericCellValue()+"";
				}
			}
			emailDataMap.put(strKey.trim(),strValue.trim());

			
		}

		workbook.close();
		file.close();

		return true;

	}
}
