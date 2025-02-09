package Utilities.Json;

import java.io.FileReader;
import java.util.HashMap;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.testng.SkipException;
import com.google.gson.Gson;
import Utilities.Excel.EnvironmentData;
import lombok.SneakyThrows;

/**
 * Utility class to fetch test data from a JSON file.
 * 
 * @author Saif
 */
public class TestDataJson {

    private static TestDataJson instance;
    private final HashMap<String, String> testDataMap = new HashMap<>();

    private TestDataJson() {
    }

    public static synchronized TestDataJson getInstance() {
        if (instance == null) {
            instance = new TestDataJson();
        }
        return instance;
    }

    @SuppressWarnings("unchecked")
    @SneakyThrows
    public HashMap<String, String> getTestDataMap(String testName) {
        testDataMap.clear();

        // Get the file path for the JSON data file
        String filePath = EnvironmentData.getInstance().environmentDataMap.get("DATASHEETPATH") + "/"
                + EnvironmentData.getInstance().environmentDataMap.get("JSON_DATASHEET_NAME") + ".json";

        // Parse the JSON file
        JSONParser jsonParser = new JSONParser();
        FileReader reader = new FileReader(filePath);
        Object obj = jsonParser.parse(reader);
        JSONObject jsonObject = (JSONObject) obj;
        JSONObject testObj = (JSONObject) jsonObject.get(testName);
        reader.close();

        try {
            // Convert JSON object to HashMap using Gson
            HashMap<String, String> testObjMap = new Gson().fromJson(testObj.toString(), HashMap.class);

            // Add test name to the map
            testDataMap.put("TEST_NAME", testName);

            // Add all key-value pairs to the test data map
            for (String key : testObjMap.keySet()) {
                testDataMap.put(key.trim(), testObjMap.get(key).trim());
            }
        } catch (Exception e) {
            String msg = "Test Data for test name " + testName + " not found in the Data.json file.";
            System.err.println(msg);
            throw new SkipException(msg);
        }

        return testDataMap;
    }
}
