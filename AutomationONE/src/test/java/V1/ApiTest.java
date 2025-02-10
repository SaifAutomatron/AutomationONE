package V1;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import Utilities.Common.JavaUtilities;
import Utilities.Excel.EnvironmentData;
import Utilities.Excel.TestData;
import Utilities.Listener.LoginExtent;
import Utilities.Unix.UnixUtils;
import io.restassured.response.Response;
import lombok.SneakyThrows;
import static org.hamcrest.Matchers.lessThan;  // Import for `lessThan` matcher  

public class ApiTest extends BaseClass {

	HashMap<String, String> envMap = null;
	HashMap<String, String> testDataMap = null;
	LoginExtent logExtent = new LoginExtent();
	SoftAssert sa = new SoftAssert();

	@BeforeTest
	@SneakyThrows
	public void readConfig(ITestContext context) {
		String testName = context.getCurrentXmlTest().getName();
		TestData.getInstance().fetchTestData(testName);
		testDataMap = TestData.getInstance().getTestDataMap();
		EnvironmentData.getInstance().fetchEnvironmentData();
		envMap = EnvironmentData.getInstance().getEnvironmentDataMap();
	}

	@Test(priority = 1, retryAnalyzer = Utilities.Common.RetryAnalyzer.class)
	public void getUsers() {
		Response response = given().log().all().when()
				.get(envMap.get("REQ_RES_BASE_URI") + envMap.get("USERS_RESOURCE"))
				.then().assertThat()
				.statusCode(200) 																								// Status																									// Code
				.time(lessThan(2000L), TimeUnit.MILLISECONDS) 
				.body(matchesJsonSchemaInClasspath("schemas/users-schema.json")) 
				.extract().response();
		response.prettyPrint();
		logExtent.logJSONResults("GET_USERS", response.asString());
		sa.assertAll();
	}

	@Test(priority = 2)
	public void saveUser() {
		HashMap<String, String> GDictionary = new HashMap<>();
		GDictionary.put("NAME", testDataMap.get("NAME"));
		GDictionary.put("JOB", testDataMap.get("JOB"));
		String strBody = JavaUtilities.replaceTemplatewithValues(envMap.get("JSONFILEPATH") + testDataMap.get("JSON_FILE"), GDictionary);
        System.out.println("Template output------------------>\n"+strBody);
		Response response = given().log().all()
				.body(strBody)
				.when()
				.post(envMap.get("REQ_RES_BASE_URI") + envMap.get("SAVE_USER_RESOURCE"))
				.then().extract().response();
		sa.assertEquals(response.getStatusCode(), 201);
		response.prettyPrint();
		logExtent.logJSONResults(strBody, response.asString());

		sa.assertAll();

	}

	// @Test(priority = 3)
	public void getCouchBaseDocument() {

		Response response = given().log().all().spec(httpCouchBaseSpecBuilder()).when()
				.get(testDataMap.get("BUCKET_ENDPOINT")).then().extract().response();
		sa.assertEquals(response.getStatusCode(), 200);

		response.prettyPrint();
		logExtent.logJSONResults("", response.asString());

		sa.assertAll();

	}

	// @Test(priority = 4)
	public void unixTest() {

		UnixUtils.connectServer(envMap.get("LINUX_HOST"), Integer.parseInt(envMap.get("LINUX_PORT")),
				envMap.get("LINUX_USER"), envMap.get("LINUX_PASSWORD"));
		UnixUtils.openCommandChannel();
		UnixUtils.executeShellCommand("banner AutoONE", 2);
		UnixUtils.executeShellCommand("cd ./Desktop", 2);
		UnixUtils.executeShellCommand("./test.sh", 2);
		UnixUtils.closeSession();
		String jobResponse = UnixUtils.executeShellCommand("shoeb", 2);

		logExtent.logResults("Linux Job Response", jobResponse);

	}

}
