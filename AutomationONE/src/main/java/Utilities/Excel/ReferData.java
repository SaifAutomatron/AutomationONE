package Utilities.Excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ReferData {

    private static ReferData instance = null;
    public final HashMap<String, String> referDataMap = new HashMap<>();

    private ReferData() {
    }

    public static synchronized ReferData getInstance() {
        if (instance == null) {
            instance = new ReferData();
        }
        return instance;
    }

    public HashMap<String, String> getReferDataMap() {
        return referDataMap;
    }

    public boolean fetchReferData() {
        referDataMap.clear();
        boolean dataFound = false;

        String dataSheetPath = EnvironmentData.getInstance().environmentDataMap.get("DATASHEETPATH");
        String dataSheetName = EnvironmentData.getInstance().environmentDataMap.get("REFDATASHEET_NAME");
        String dataExcel = java.nio.file.Paths.get(dataSheetPath, dataSheetName + ".xlsx").toString();

        try (FileInputStream file = new FileInputStream(new File(dataExcel));
             XSSFWorkbook workbook = new XSSFWorkbook(file)) {

            XSSFSheet sheet = workbook.getSheet("DATA");
            if (sheet == null) {
                throw new IllegalArgumentException("Sheet 'DATA' does not exist in the Excel file.");
            }

            for (Row row : sheet) {
                dataFound = true;
                String key = getCellValueAsString(row.getCell(0));
                String value = getCellValueAsString(row.getCell(1));
                referDataMap.put(key.trim(), value.trim());
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch refer data from Excel file: " + dataExcel, e);
        }

        if (!dataFound) {
            System.out.println("Reference data not found in Excel file.");
            return false;
        }

        return true;
    }

    public boolean saveReferData(HashMap<String, String> newReferDataMap) {
        String dataSheetPath = EnvironmentData.getInstance().environmentDataMap.get("DATASHEETPATH");
        String dataSheetName = EnvironmentData.getInstance().environmentDataMap.get("REFDATASHEET_NAME");
        String dataExcel = java.nio.file.Paths.get(dataSheetPath, dataSheetName + ".xlsx").toString();

        try (FileInputStream file = new FileInputStream(new File(dataExcel));
             XSSFWorkbook workbook = new XSSFWorkbook(file);
             FileOutputStream fos = new FileOutputStream(dataExcel)) {

            XSSFSheet sheet = workbook.getSheet("DATA");
            if (sheet == null) {
                throw new IllegalArgumentException("Sheet 'DATA' does not exist in the Excel file.");
            }

            for (Map.Entry<String, String> entry : newReferDataMap.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();

                boolean keyExists = false;
                for (Row row : sheet) {
                    String existingKey = getCellValueAsString(row.getCell(0));
                    if (key.equals(existingKey)) {
                        row.createCell(1, CellType.STRING).setCellValue(value);
                        keyExists = true;
                        break;
                    }
                }

                if (!keyExists) {
                    Row newRow = sheet.createRow(sheet.getLastRowNum() + 1);
                    newRow.createCell(0, CellType.STRING).setCellValue(key);
                    newRow.createCell(1, CellType.STRING).setCellValue(value);
                }
            }

            workbook.write(fos);
        } catch (Exception e) {
            throw new RuntimeException("Failed to save refer data to Excel file: " + dataExcel, e);
        }

        return true;
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