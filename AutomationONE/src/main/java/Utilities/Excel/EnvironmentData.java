package Utilities.Excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class EnvironmentData {

	String rootPath;
	String datasheetPath;
	String curExecutionFolder;
	String dataSheetsPath;
	String User;
	String environmentsPath;
	String propertiesPath;
	String jsonFilePath;
	String xmlFilePath;
	String emailSheetsPath;
	Properties prop=new Properties();
	InputStream input=null;
	XSSFSheet Sheet=null;
	private static EnvironmentData instance=null;
	public HashMap<String,String> environmentDataMap=new HashMap<>();

	public HashMap<String,String> getEnvironmentDataMap(){
		return environmentDataMap;
	}

	private EnvironmentData()
	{
		String envCode=System.getProperty("Environment");
		String version=System.getProperty("Version");
		User=System.getProperty("user.name");
		rootPath=System.getProperty("user.dir");
		dataSheetsPath=java.nio.file.Paths.get(rootPath,"Data").toString();
		environmentsPath=java.nio.file.Paths.get(rootPath,"Environments").toString();
		propertiesPath=java.nio.file.Paths.get(rootPath,"config.properties").toString();
		emailSheetsPath=java.nio.file.Paths.get(rootPath,"Email").toString();
		File f=new File(propertiesPath);

		try {
			input=new FileInputStream(f);
			prop.load(input);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(version==null)
		{
			environmentDataMap.put("VERSION_CODE", prop.getProperty("version"));
		}
		else {
			environmentDataMap.put("VERSION_CODE",version);
		}
		environmentDataMap.put("REPORT_HEADER", prop.getProperty("reportheader"));
		jsonFilePath=rootPath+"/Data/InputData/"+environmentDataMap.get("VERSION_CODE")+"/JSON/";
		xmlFilePath=rootPath+"/Data/InputData/"+environmentDataMap.get("VERSION_CODE")+"/XML/";

		environmentDataMap.put("ROOTPATH",rootPath);
		environmentDataMap.put("ENVIRONMENTXLSPATH",environmentsPath);
		environmentDataMap.put("PROPERTIESPATH",propertiesPath);
		environmentDataMap.put("DATASHEETPATH",dataSheetsPath);
		environmentDataMap.put("JSONFILEPATH",jsonFilePath);
		environmentDataMap.put("XMLFILEPATH",xmlFilePath);
		environmentDataMap.put("EMAILSHEETPATH",emailSheetsPath);

		if(envCode==null)
		{
			environmentDataMap.put("ENV_CODE", prop.getProperty("environment"));
		}
		else {
			environmentDataMap.put("ENV_CODE",envCode);
		}

		environmentDataMap.put("DATASHEET_NAME", prop.getProperty("dataSheet"));
		environmentDataMap.put("JSON_DATASHEET_NAME", prop.getProperty("jsonDataSheet"));
		environmentDataMap.put("EMAILSHEET_NAME", prop.getProperty("emailSheet"));
		environmentDataMap.put("REFDATASHEET_NAME", prop.getProperty("refDataSheet"));

	}

	public static synchronized EnvironmentData getInstance()
	{
		if(instance==null) {
			instance=new EnvironmentData();
		}
		return instance;
	}

	public boolean fetchEnvironmentData() throws Exception
	{

		int iEnvironment=-1;
		boolean bFlag=false;
		
		String environmentsExcel=java.nio.file.Paths.get(environmentDataMap.get("ENVIRONMENTXLSPATH"),"Environmentdata.xlsx").toString();

		iEnvironment=fGetColumnIndex(environmentsExcel,"ENVIRONMENTS","ENVIRONMENT");

		if(iEnvironment==-1) {
			System.out.println("Failed to find the Environment Column in the file "+environmentsExcel);
			return false;
		}


		FileInputStream file=new FileInputStream(new File(environmentsExcel));

		XSSFWorkbook workbook=new XSSFWorkbook(file);

		Sheet sheet = workbook.getSheet("ENVIRONMENTS");

		int iRowNum = sheet.getLastRowNum();
		
		int iColCount=sheet.getRow(0).getLastCellNum();
		

		String strEnvironment =null;
		for (int iRow = 0; iRow <= iRowNum; iRow++) {

			if(sheet.getRow(iRow).getCell(iEnvironment).getCellType()==CellType.STRING)
			{
				strEnvironment=sheet.getRow(iRow).getCell(iEnvironment).getStringCellValue().trim();
			}
			else if(sheet.getRow(iRow).getCell(iEnvironment).getCellType()==CellType.NUMERIC)
			{
				strEnvironment=sheet.getRow(iRow).getCell(iEnvironment).getNumericCellValue()+"";
			}
			
			if(!strEnvironment.equals(environmentDataMap.get("ENV_CODE")))
			{
				continue;
			}

			bFlag=true;
			String strKey="";
			String strValue="";
			for (int iCell = 0; iCell < iColCount; iCell++) {

				if(sheet.getRow(0).getCell(iCell).getCellType()==CellType.STRING)
				{
					strKey=sheet.getRow(0).getCell(iCell).getStringCellValue().trim().toUpperCase();
				}
				else if(sheet.getRow(0).getCell(iCell).getCellType()==CellType.NUMERIC)
				{
					strKey=sheet.getRow(0).getCell(iCell).getNumericCellValue()+"";
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
				environmentDataMap.put(strKey.trim(),strValue.trim());
			}
			break;
		}

		workbook.close();
		file.close();

		if(bFlag==false)
		{
			System.out.println("Environment Code "+environmentDataMap.get("ENV_CODE")+" not found in the ENvironment xls");
			return false;
		}


		return true;

	}

    public int fGetColumnIndex(String strXLSX, String strSheetName, String strColumnName) throws Exception {
		FileInputStream file=new FileInputStream(new File(strXLSX));

		XSSFWorkbook workbook=new XSSFWorkbook(file);

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
