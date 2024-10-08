package Utilities.Database;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellBase;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Color;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import Utilities.Excel.EnvironmentData;
import lombok.SneakyThrows;

public class DataBase {


	static DataBase d;
	ArrayList<HashMap<String,String>> dbResutMaplist;
	HashMap<String, String> envMap=null;
	HashMap<String, String> dbResultMap=null;

	@SneakyThrows
	private DataBase()
	{
		EnvironmentData.getInstance().fetchEnvironmentData();
		envMap = EnvironmentData.getInstance().getEnvironmentDataMap();
	}

	public static synchronized DataBase getInstance()
	{

		if(d==null)
		{
			d=new DataBase();
		}
		else
			return d;

		return d;
	}


	@SneakyThrows
	public ArrayList<HashMap<String,String>> runSQLQuery(String sqlQuery)
	{

		ResultSet rs=null;
		Statement stmt=null;
		Connection con=null;

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection(envMap.get("DB_STRING"), envMap.get("DB_USER"), envMap.get("DB_PASSWORD"));

			stmt=con.createStatement();
			rs=stmt.executeQuery(sqlQuery);
			if(rs!=null)
			{
				ResultSetMetaData metatdata = rs.getMetaData();
				dbResutMaplist=new ArrayList<HashMap<String, String>>();

				while(rs.next()){
					dbResultMap=new HashMap<>();

					for(int i=0;i<metatdata.getColumnCount();i++)
					{
						dbResultMap.put(metatdata.getColumnName(i+1),""+rs.getObject(i+1));
					}
					dbResutMaplist.add(dbResultMap);

				}

			}
		}
		finally 
		{
			if(rs!=null)
				rs.close();
			if(stmt!=null)
				stmt.close();
			if(con!=null)
				con.close();

		}
		return dbResutMaplist;

	}

	@SneakyThrows
	public int updateSQLQuery(String sqlQuery)
	{

		ResultSet rs=null;
		Statement stmt=null;
		Connection con=null;
		int count=0;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection(envMap.get("DB_STRING"), envMap.get("DB_USER"), envMap.get("DB_PASSWORD"));
			stmt=con.createStatement();
			count=stmt.executeUpdate(sqlQuery);
		}
		finally 
		{
			if(rs!=null)
				rs.close();
			if(stmt!=null)
				stmt.close();
			if(con!=null)
				con.close();

		}
		return count;

	}

	@SneakyThrows
	public void exportToExcel(String fileName,String sqlQuery)
	{

		ResultSet rs=null;
		Statement stmt=null;
		Connection con=null;
		List<String> columns=null;
		ResultSetMetaData metadata;

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection(envMap.get("DB_STRING"), envMap.get("DB_USER"), envMap.get("DB_PASSWORD"));

			stmt=con.createStatement();
			rs=stmt.executeQuery(sqlQuery);
			if(rs!=null)
			{
				metadata = rs.getMetaData();
				columns=new ArrayList<String>() {{
					for (int i = 1; i <= metadata.getColumnCount(); i++) {
						add(metadata.getColumnLabel(i));
					}
				}};
			}



			XSSFWorkbook book=new XSSFWorkbook();
			XSSFCellStyle borderStyle=book.createCellStyle();
			borderStyle.setBorderBottom(BorderStyle.THIN);
			borderStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
			borderStyle.setBorderLeft(BorderStyle.THIN);
			borderStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
			borderStyle.setBorderRight(BorderStyle.THIN);
			borderStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
			borderStyle.setBorderTop(BorderStyle.THIN);
			borderStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
			XSSFCellStyle style=book.createCellStyle();
			style.cloneStyleFrom(borderStyle);
			XSSFFont font = book.createFont();
			font.setFontHeightInPoints((short) 11);
			font.setBold(true);
			style.setFont(font); 
			XSSFCellStyle colourStyle=book.createCellStyle();
			colourStyle.cloneStyleFrom(style);
			colourStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
			colourStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			
			

			Sheet sheet=book.createSheet();
			Row headingRow = sheet.createRow(0);
			Cell cel0 = headingRow.createCell(0);
			cel0.setCellStyle(style);
			cel0.setCellValue("ENVIRONMENT->");
			Cell cel1 = headingRow.createCell(1);
			cel1.setCellStyle(style);
			cel1.setCellValue(envMap.get("ENVIRONMENT"));
			Cell cel2 = headingRow.createCell(2);
			cel2.setCellStyle(style);
			cel2.setCellValue("TABLE NAME->");
			Cell cel3 = headingRow.createCell(3);
			cel3.setCellStyle(style);
			cel3.setCellValue(rs.getMetaData().getTableName(1));


			Row header = sheet.createRow(2);

			for (int i = 0; i < columns.size(); i++) {
				Cell colCell = header.createCell(i);
				colCell.setCellValue(columns.get(i));
				colCell.setCellStyle(colourStyle);
				sheet.autoSizeColumn(i);

			}
			int rowIndex=2;
			while(rs.next()) {
				Row row=sheet.createRow(++rowIndex);
				for (int j = 0; j < columns.size(); j++) {
					Cell cell = row.createCell(j);
					cell.setCellValue(Objects.toString(rs.getObject(columns.get(j)), ""));
					cell.setCellStyle(borderStyle);
					sheet.autoSizeColumn(j);
				}
			}
			try(FileOutputStream fos=new FileOutputStream(new File("./TableExports/"+fileName+".xlsx"))) {
				book.write(fos);
				book.close();

			} catch (Exception e) {
				System.err.println("Table Export Failed");
				throw new RuntimeException(e.getMessage());
			}

		}
		finally 
		{
			if(rs!=null)
				rs.close();
			if(stmt!=null)
				stmt.close();
			if(con!=null)
				con.close();

		}
		System.out.println("Table sucessfully exported to excel file");

	}
}
