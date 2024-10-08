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
 * 
 * @author Saif
 *
 */
public class TestDataJson {

	static TestDataJson r;
	HashMap<String, String> testDataMap=new HashMap<>();

	private TestDataJson()
	{
	}

	public static synchronized TestDataJson getInstance()
	{

		if(r==null)
		{
			r=new TestDataJson();
		}

		return r;
	}

	

	@SuppressWarnings("unchecked")
	@SneakyThrows
	public HashMap<String,String> getTestDataMap(String testname)
	{    
		testDataMap.clear();
		String filePath=EnvironmentData.getInstance().environmentDataMap.get("DATASHEETPATH")+"/"+EnvironmentData.getInstance().environmentDataMap.get("JSON_DATASHEET_NAME")+".json";
		JSONParser jsonParser=new JSONParser();
		FileReader reader=new FileReader(filePath);
		Object obj = jsonParser.parse(reader);
		JSONObject jobj=((JSONObject) obj);
		JSONObject testObj = (JSONObject) jobj.get(testname);
		reader.close();
		try {
		HashMap<String,String> testObjMap = new Gson().fromJson(testObj.toString(),HashMap.class);

				testDataMap.put("TEST_NAME", testname);
				
				for(  String k:testObjMap.keySet())
				{
					testDataMap.put(k.trim(), testObjMap.get(k).trim());
				}
				
		}
		catch (Exception e) {
			String msg="Test Data for test name "+testname+" not found in Data.json";
			System.err.println(msg);
			throw new SkipException(msg);
		}

		return testDataMap;

	}


}
