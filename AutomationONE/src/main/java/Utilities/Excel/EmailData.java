package Utilities.Excel;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Paths;
import java.util.HashMap;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class EmailData {

    private static EmailData instance;
    private final HashMap<String, String> emailDataMap = new HashMap<>();

    private EmailData() {
    }

    public static synchronized EmailData getInstance() {
        if (instance == null) {
            instance = new EmailData();
        }
        return instance;
    }

    public HashMap<String, String> getEmailDataMap() {
        return emailDataMap;
    }

    public boolean fetchEmailData() throws Exception {
        emailDataMap.clear();

        // Get file path and name from environment data
        String dataSheetPath = EnvironmentData.getInstance().environmentDataMap.get("EMAILSHEETPATH");
        String dataSheetName = EnvironmentData.getInstance().environmentDataMap.get("EMAILSHEET_NAME");
        String dataExcelPath = Paths.get(dataSheetPath, dataSheetName + ".xlsx").toString();

        // Open the Excel file
        try (FileInputStream fileInputStream = new FileInputStream(new File(dataExcelPath));
             XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream)) {

            Sheet sheet = workbook.getSheet("EMAIL");

            if (sheet == null) {
                throw new IllegalArgumentException("Sheet 'EMAIL' does not exist in the Excel file: " + dataExcelPath);
            }

            for (Row row : sheet) {
                // Skip the header row
                if (row.getRowNum() == 0) {
                    continue;
                }

                // Read key and value
                String key = getCellValueAsString(row.getCell(0));
                String value = getCellValueAsString(row.getCell(1));

                // Add to the map
                if (key != null && !key.isEmpty()) {
                    emailDataMap.put(key.trim(), value != null ? value.trim() : "");
                }
            }
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
