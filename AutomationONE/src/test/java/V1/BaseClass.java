package V1;

import java.net.URL;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import lombok.SneakyThrows;

public class BaseClass {

    protected WebDriver driver;

    public RequestSpecification httpCouchBaseSpecBuilder() {
        return new RequestSpecBuilder()
                .addHeader("Authorization", "Basic YWRtaW46bWFuYWdlcg==")
                .setBaseUri("http://localhost:8091")
                .build();
    }

    @SneakyThrows
    //@BeforeTest
    public void setupDriver() {
        String host = "localhost";
        String browser = System.getProperty("BROWSER", "chrome").toLowerCase();

        if (System.getProperty("HUB_HOST") != null) {
            host = System.getProperty("HUB_HOST");
        }

        String completeUrl = "http://" + host + ":4444/wd/hub";

        switch (browser) {
            case "firefox":
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                this.driver = new RemoteWebDriver(new URL(completeUrl), firefoxOptions);
                break;

            case "chrome":
            default:
                ChromeOptions chromeOptions = new ChromeOptions();
                this.driver = new RemoteWebDriver(new URL(completeUrl), chromeOptions);
                break;
        }

        System.out.println("Driver setup completed for browser: " + browser);
    }

    //@AfterTest
    public void tearDown() {
        if (driver != null) {
            driver.quit();
            System.out.println("Driver terminated.");
        }
    }
}
