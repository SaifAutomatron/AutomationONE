package V1;

import java.net.URL;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import lombok.SneakyThrows;

public class BaseClass {

	protected WebDriver driver;

	public RequestSpecification httpCouchBaseSpecBuilder() {
		RequestSpecification spec=new RequestSpecBuilder()
				.addHeader("Authorization", "Basic YWRtaW46bWFuYWdlcg==")
				.setBaseUri("http://localhost:8091")
				.build();

		return spec;
	}
	
	@SneakyThrows
	//@BeforeTest
	public void setupDriver() {
		String host="localhost";
		DesiredCapabilities dc=DesiredCapabilities.chrome();
		
		if(System.getProperty("BROWSER")!=null && System.getProperty("BROWSER").equalsIgnoreCase("firefox")) {
			dc=DesiredCapabilities.firefox();
		}
		
		if(System.getProperty("HUB_HOST")!=null) {
			host=System.getProperty("HUB_HOST");
		}
		
		
		String completeUrl="http://"+host+":4444/wd/hub";
		this.driver=new RemoteWebDriver(new URL(completeUrl), dc);
		
	}
	
	

}
