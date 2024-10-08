package Utilities.Excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ReferData {

	private static ReferData instance=null;
	public HashMap<String, String> referDataMap=new HashMap<String, String>();

	public HashMap<String, String> getReferDataMap()
	{
		return referDataMap;
	}

	private ReferData()
	{

	}

	public static synchronized ReferData getInstance()
	{
		if(instance==null) {
			instance=new ReferData();
		}
		return instance;
	}

	public boolean fetchReferData() throws Exception
	{
		int iData=-1;
		boolean bFlag=false;
		ReferData.getInstance().getReferDataMap().clear();
		String dataSheetPath=EnvironmentData.getInstance().environmentDataMap.get("DATASHEETPATH");
		String dataSheetName=EnvironmentData.getInstance().environmentDataMap.get("REFDATASHEET_NAME");
		String dataExcel=java.nio.file.Paths.get(dataSheetPath,dataSheetName+".xlsx").toString();

		iData=fGetColumnIndex(dataExcel,"DATA","KEY");

		if(iData==-1) {
			System.out.println("Failed to find the KEY Column in the file "+dataExcel);
			return false;
		}

		iData=-1;

		iData=fGetColumnIndex(dataExcel,"DATA","VALUE");


		if(iData==-1) {
			System.out.println("Failed to find the Value Column in the file "+dataExcel);
			return false;
		}

		FileInputStream file=new FileInputStream(new File(dataExcel));

		XSSFWorkbook workbook=new XSSFWorkbook(file);

		XSSFSheet sheet = workbook.getSheet("DATA");

		int iRowNum = sheet.getLastRowNum();

		for (int iRow = 0; iRow <= iRowNum; iRow++) {

			bFlag=true;
			String strKey="";
			String strValue="";

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
					strKey=sheet.getRow(iRow).getCell(1).getNumericCellValue()+"";
				}
			}
			referDataMap.put(strKey.trim(),strValue.trim());
		}

		workbook.close();
		file.close();

		if(bFlag==false)
		{
			System.out.println("Reference Data not found in data xls");
			return false;
		}


		return true;

	}

	public boolean saveReferData(HashMap<String, String> newReferDataMap) throws Exception
	{
		int iData=-1;
		String dataSheetPath=EnvironmentData.getInstance().environmentDataMap.get("DATASHEETPATH");
		String dataSheetName=EnvironmentData.getInstance().environmentDataMap.get("REFDATASHEET_NAME");
		String dataExcel=java.nio.file.Paths.get(dataSheetPath,dataSheetName+".xlsx").toString();

		iData=fGetColumnIndex(dataExcel,"DATA","KEY");

		if(iData==-1) {
			System.out.println("Failed to find the KEY Column in the file "+dataExcel);
			return false;
		}

		iData=-1;

		iData=fGetColumnIndex(dataExcel,"DATA","VALUE");


		if(iData==-1) {
			System.out.println("Failed to find the Value Column in the file "+dataExcel);
			return false;
		}

		FileInputStream file=new FileInputStream(new File(dataExcel));

		XSSFWorkbook workbook=new XSSFWorkbook(file);

		XSSFSheet sheet = workbook.getSheet("DATA");

		int iRowNum = sheet.getLastRowNum();

		String Key="";
		String value="";
		Row row=null;

		for (@SuppressWarnings("rawtypes") Map.Entry mapElement:newReferDataMap.entrySet()) {
			Key=(String)mapElement.getKey();
			value=(String)mapElement.getValue();

			if(referDataMap.containsKey(Key))
			{
				for (int iRow = 0; iRow <= iRowNum; iRow++) {

					String strKey="";

					if(sheet.getRow(iRow).getCell(0).getCellType()==CellType.STRING)
					{
						strKey=sheet.getRow(iRow).getCell(0).getStringCellValue().trim();
					}
					else if(sheet.getRow(iRow).getCell(0).getCellType()==CellType.NUMERIC)
					{
						strKey=sheet.getRow(iRow).getCell(0).getNumericCellValue()+"";
					}

					if(Key.equals(strKey))
					{
						sheet.getRow(iRow).getCell(1).setCellValue(value);
						break;
					}

				}
			}
			else
			{
				int iRowNumUpdated=sheet.getLastRowNum();
				row=sheet.createRow(iRowNumUpdated+1);
				row.createCell(0).setCellValue(Key);
				row.createCell(1).setCellValue(value);
			}


		}
		FileOutputStream fos=new FileOutputStream(dataExcel);
		workbook.write(fos);
		fos.close();
		workbook.close();

		return true;

	}

	@SuppressWarnings("resource")
	private int fGetColumnIndex(String strXLSX, String strSheetname, String strColumnName) throws Exception {


		FileInputStream file=new FileInputStream(new File(strXLSX));

		XSSFWorkbook workbook=new XSSFWorkbook(file);

		XSSFSheet sheet = workbook.getSheet(strSheetname);

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
