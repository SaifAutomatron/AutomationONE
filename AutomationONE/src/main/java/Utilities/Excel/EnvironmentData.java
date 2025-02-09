package Utilities.Excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Properties;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class EnvironmentData {

    private static EnvironmentData instance = null;
    private final Properties prop = new Properties();
    public final HashMap<String, String> environmentDataMap = new HashMap<>();

    private final String rootPath;
    private final String dataSheetsPath;
    private final String environmentsPath;
    private final String propertiesPath;
    private final String emailSheetsPath;

    private EnvironmentData() {
        String envCode = System.getProperty("Environment");
        String version = System.getProperty("Version");
        rootPath = System.getProperty("user.dir");
        dataSheetsPath = Paths.get(rootPath, "Data").toString();
        environmentsPath = Paths.get(rootPath, "Environments").toString();
        propertiesPath = Paths.get(rootPath, "config.properties").toString();
        emailSheetsPath = Paths.get(rootPath, "Email").toString();

        loadProperties();

        environmentDataMap.put("VERSION_CODE", version != null ? version : prop.getProperty("version"));
        environmentDataMap.put("REPORT_HEADER", prop.getProperty("reportheader"));
        environmentDataMap.put("ROOTPATH", rootPath);
        environmentDataMap.put("ENVIRONMENTXLSPATH", environmentsPath);
        environmentDataMap.put("PROPERTIESPATH", propertiesPath);
        environmentDataMap.put("DATASHEETPATH", dataSheetsPath);
        environmentDataMap.put("EMAILSHEETPATH", emailSheetsPath);

        environmentDataMap.put("ENV_CODE", envCode != null ? envCode : prop.getProperty("environment"));
        environmentDataMap.put("DATASHEET_NAME", prop.getProperty("dataSheet"));
        environmentDataMap.put("JSON_DATASHEET_NAME", prop.getProperty("jsonDataSheet"));
        environmentDataMap.put("EMAILSHEET_NAME", prop.getProperty("emailSheet"));
        environmentDataMap.put("REFDATASHEET_NAME", prop.getProperty("refDataSheet"));

        String versionCode = environmentDataMap.get("VERSION_CODE");
        environmentDataMap.put("JSONFILEPATH", rootPath+"/Data/InputData/"+environmentDataMap.get("VERSION_CODE")+"/JSON/");
        environmentDataMap.put("XMLFILEPATH", rootPath+"/Data/InputData/"+environmentDataMap.get("VERSION_CODE")+"/XML/");
    }

    public static synchronized EnvironmentData getInstance() {
        if (instance == null) {
            instance = new EnvironmentData();
        }
        return instance;
    }

    public HashMap<String, String> getEnvironmentDataMap() {
        return environmentDataMap;
    }

    private void loadProperties() {
        try (FileInputStream input = new FileInputStream(new File(propertiesPath))) {
            prop.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load properties file: " + propertiesPath, e);
        }
    }

    public boolean fetchEnvironmentData() {
        String environmentsExcel = Paths.get(environmentDataMap.get("ENVIRONMENTXLSPATH"), "Environmentdata.xlsx").toString();

        int environmentColIndex;
        try {
            environmentColIndex = fGetColumnIndex(environmentsExcel, "ENVIRONMENTS", "ENVIRONMENT");
        } catch (Exception e) {
            System.out.println("Failed to find the 'Environment' column in the file: " + environmentsExcel);
            return false;
        }

        try (FileInputStream file = new FileInputStream(new File(environmentsExcel));
             XSSFWorkbook workbook = new XSSFWorkbook(file)) {

            Sheet sheet = workbook.getSheet("ENVIRONMENTS");
            if (sheet == null) {
                throw new IllegalArgumentException("Sheet 'ENVIRONMENTS' does not exist in the Excel file.");
            }

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Skip header row

                Cell envCell = row.getCell(environmentColIndex);
                String strEnvironment = getCellValueAsString(envCell);

                if (!strEnvironment.equals(environmentDataMap.get("ENV_CODE"))) {
                    continue;
                }

                for (int iCell = 0; iCell < row.getLastCellNum(); iCell++) {
                    String key = getCellValueAsString(sheet.getRow(0).getCell(iCell)).toUpperCase();
                    String value = getCellValueAsString(row.getCell(iCell));
                    environmentDataMap.put(key.trim(), value.trim());
                }
                return true;
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading the environment data file: " + environmentsExcel, e);
        }

        System.out.println("Environment Code " + environmentDataMap.get("ENV_CODE") + " not found in the environment file.");
        return false;
    }

    private int fGetColumnIndex(String strXLSX, String strSheetName, String strColumnName) throws Exception {
        try (FileInputStream file = new FileInputStream(new File(strXLSX));
             XSSFWorkbook workbook = new XSSFWorkbook(file)) {

            Sheet sheet = workbook.getSheet(strSheetName);
            if (sheet == null) {
                throw new IllegalArgumentException("Sheet '" + strSheetName + "' does not exist in the Excel file.");
            }

            Row headerRow = sheet.getRow(0);
            for (int iCell = 0; iCell < headerRow.getLastCellNum(); iCell++) {
                String header = getCellValueAsString(headerRow.getCell(iCell));
                if (header.equalsIgnoreCase(strColumnName)) {
                    return iCell;
                }
            }
        }

        throw new IllegalArgumentException("Column '" + strColumnName + "' not found in the sheet '" + strSheetName + "'.");
    }

    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf(cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return "";
        }
    }
}
