package Utilities.Excel;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * 
 * @author Saif
 *
 */
public class TestData {

	static TestData r;
	public HashMap<String, String> testDataMap=new HashMap<String, String>();

	public HashMap<String, String> getTestDataMap()
	{
		return testDataMap;
	}

	private TestData()
	{
	}

	public static synchronized TestData getInstance()
	{

		if(r==null)
		{
			r=new TestData();
		}

		return r;
	}

	public boolean fetchTestData(String testName) throws Exception
	{

		int iData=-1;
		boolean bFlag=false;
		TestData.getInstance().getTestDataMap().clear();
		String dataSheetPath=EnvironmentData.getInstance().environmentDataMap.get("DATASHEETPATH");
		String dataSheetName=EnvironmentData.getInstance().environmentDataMap.get("DATASHEET_NAME");
		String dataExcel=java.nio.file.Paths.get(dataSheetPath,dataSheetName+".xlsx").toString();

		iData=fGetColumnIndex(dataExcel,"MAIN","TEST_NAME");

		if(iData==-1) {
			System.out.println("Failed to find the TEST_NAME Column in the file "+dataExcel);
			return false;
		}


		FileInputStream file=new FileInputStream(new File(dataExcel));

		XSSFWorkbook workbook=new XSSFWorkbook(file);

		Sheet sheet = workbook.getSheet("MAIN");

		int iRowNum = sheet.getLastRowNum();
		
		int iColCount=sheet.getRow(0).getLastCellNum();
		

		String strTestName =null;
		for (int iRow = 0; iRow <= iRowNum; iRow++) {

			if(sheet.getRow(iRow).getCell(iData).getCellType()==CellType.STRING)
			{
				strTestName=sheet.getRow(iRow).getCell(iData).getStringCellValue().trim();
			}
			else if(sheet.getRow(iRow).getCell(iData).getCellType()==CellType.NUMERIC)
			{
				strTestName=sheet.getRow(iRow).getCell(iData).getNumericCellValue()+"";
			}
			
			if(!strTestName.equals(testName))
			{
				continue;
			}

			bFlag=true;
			String strKey="";
			String strValue="";
			for (int iCell = 0; iCell < iColCount; iCell++) {

				if(sheet.getRow(iRow-1).getCell(iCell).getCellType()==CellType.STRING)
				{
					strKey=sheet.getRow(iRow-1).getCell(iCell).getStringCellValue().trim();
				}
				else if(sheet.getRow(iRow-1).getCell(iCell).getCellType()==CellType.NUMERIC)
				{
					strKey=sheet.getRow(iRow-1).getCell(iCell).getNumericCellValue()+"";
				}

				if(sheet.getRow(iRow).getCell(iCell,Row.MissingCellPolicy.CREATE_NULL_AS_BLANK)==null)
				{
					strValue="";
				}
				else {
					if(sheet.getRow(iRow).getCell(iCell).getCellType()==CellType.STRING)
					{
						strValue=sheet.getRow(iRow).getCell(iCell).getStringCellValue().trim();
					}
					else if(sheet.getRow(iRow).getCell(iCell).getCellType()==CellType.NUMERIC)
					{
						strValue=sheet.getRow(iRow).getCell(iCell).getNumericCellValue()+"";
					}
				}
				testDataMap.put(strKey.trim(),strValue.trim());
			}
			break;
		}

		workbook.close();
		file.close();

		if(bFlag==false)
		{
			System.out.println("Test Data for test name "+testName+" not found in data xlsx");
			return false;
		}


		return true;

	}

    public int fGetColumnIndex(String strXLSX, String strSheetName, String strColumnName) throws Exception {
		FileInputStream file=new FileInputStream(new File(strXLSX));

		try (XSSFWorkbook workbook = new XSSFWorkbook(file)) {
			Sheet sheet = workbook.getSheet(strSheetName);

			Row row = sheet.getRow(0);

			int iColCount=row.getLastCellNum();
			int iCell=0;
			int iIndex=-1;
			String strTemp="";

			for (iCell= 0; iCell <iColCount; iCell++) {

				strTemp=sheet.getRow(0).getCell(iCell).getStringCellValue().trim();


				if(strColumnName.equals("HEADER_IND")||strColumnName.equals("HEADER")) {
					if(strTemp.equals("HEADER")||strTemp.equals("HEADER_IND"))
					{
						iIndex=iCell;
						break;
					}
				}
				else {
					if(strTemp.equals(strColumnName.trim()))
					{
						iIndex=iCell;
						break;
					}
				}

			}

			workbook.close();
			file.close();

			if(iIndex!=-1)
			{
				return iIndex;
			}
			else {
				System.out.println("Failed to find the Column Id for column "+strColumnName);
				return -1;
			}
		}

	}
}