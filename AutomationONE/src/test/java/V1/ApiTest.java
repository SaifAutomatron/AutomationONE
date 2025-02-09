package V1;

import java.util.HashMap;

import org.testng.ITestContext;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import Utilities.Common.JavaUtilities;
import Utilities.Database.CouchBase;
import Utilities.Excel.EnvironmentData;
import Utilities.Excel.ReferData;
import Utilities.Excel.TestData;
import Utilities.Listener.LoginExtent;
import Utilities.Unix.UnixUtils;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import static io.restassured.RestAssured.*;
import lombok.SneakyThrows;

public class ApiTest extends BaseClass {

	HashMap<String, String> envMap=null;
	HashMap<String, String> testDataMap=null;
	LoginExtent logExtent=new LoginExtent();
	SoftAssert sa=new SoftAssert();
	HashMap<String, String> saveMap= new HashMap<String, String>();
	

	@BeforeTest
	@SneakyThrows
	public void readConfig(ITestContext context)
	{
		String testName=context.getCurrentXmlTest().getName();
		TestData.getInstance().fetchTestData(testName);
		testDataMap = TestData.getInstance().getTestDataMap();
		EnvironmentData.getInstance().fetchEnvironmentData();
		envMap = EnvironmentData.getInstance().getEnvironmentDataMap();
		ReferData.getInstance().fetchReferData();
		HashMap<String, String> map = ReferData.getInstance().getReferDataMap();
		System.out.println("REFER DATA MAP---------->\n"+map);
		
		
	}

	@Test(priority = 1)
	public void getUsers() {

		Response response = given().log().all().
				when().
				get(envMap.get("REQ_RES_BASE_URI")+envMap.get("USERS_RESOURCE")).
				then().extract().response();
		sa.assertEquals(response.getStatusCode(), 200);

		response.prettyPrint();
		logExtent.logJSONResults("GET_USERS", response.asString());

		sa.assertAll();
	}

	@Test(priority = 2)
	public void saveUser() {
		HashMap<String, String> GDictionary=new HashMap<>();
		GDictionary.put("NAME", testDataMap.get("NAME"));
		GDictionary.put("JOB",  testDataMap.get("JOB"));
		String strBody = JavaUtilities.replaceTemplatewithValues(envMap.get("JSONFILEPATH")+testDataMap.get("JSON_FILE"), GDictionary);


		Response response = given().log().all().
				body(strBody).
				when().
				post(envMap.get("REQ_RES_BASE_URI")+envMap.get("SAVE_USER_RESOURCE")).
				then().extract().response();
		sa.assertEquals(response.getStatusCode(), 201);

		response.prettyPrint();
		logExtent.logJSONResults(strBody, response.asString());
		
		JsonPath jp =new JsonPath(response.asString());
		saveMap.put("Test", "save");

		sa.assertAll();                     

	}
	
	@Test(priority = 3)
	public void getCouchBaseDocument() {


	  Response response = given().log().all().
				spec(httpCouchBaseSpecBuilder()).
				when().
				get(testDataMap.get("BUCKET_ENDPOINT")).
				then().extract().response();
		sa.assertEquals(response.getStatusCode(), 200);

		response.prettyPrint();
		logExtent.logJSONResults("", response.asString());
		
		JsonPath jp =new JsonPath(response.asString());

		sa.assertAll();                     

	}
	
	
	
	@Test(priority = 4)
	public void unixTest() {
		
		UnixUtils.connectServer(envMap.get("LINUX_HOST"), Integer.parseInt(envMap.get("LINUX_PORT")), envMap.get("LINUX_USER"), envMap.get("LINUX_PASSWORD"));
		UnixUtils.openCommandChannel();
		UnixUtils.executeShellCommand("banner AutoONE", 2);
		UnixUtils.executeShellCommand("cd ./Desktop", 2);
		UnixUtils.executeShellCommand("./test.sh", 2);
		UnixUtils.closeSession();
		String jobResponse = UnixUtils.executeShellCommand("shoeb", 2);

		logExtent.logResults("Linux Job Response",jobResponse);



	}

}
