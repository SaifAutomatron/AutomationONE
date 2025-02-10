package V1;

import java.util.HashMap;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.ITestContext;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import Pages.SauceDemoCart;
import Pages.SauceDemoHome;
import Pages.SauceDemoInventory;
import Utilities.Common.JavaUtilities;
import Utilities.Excel.EnvironmentData;
import Utilities.Json.TestDataJson;
import Utilities.Listener.LoginExtent;
import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.SneakyThrows;

public class UiTest  extends BaseClass{
	
	HashMap<String, String> envMap=null;
	HashMap<String, String> testDataMap=null;
	LoginExtent logextent=new LoginExtent();
	SoftAssert sa=new SoftAssert();


	@BeforeTest
	@SneakyThrows
	public void readConfig(ITestContext context)
	{
		EnvironmentData.getInstance().fetchEnvironmentData();
		envMap = EnvironmentData.getInstance().getEnvironmentDataMap();
		String testName=context.getCurrentXmlTest().getName();
		testDataMap = TestDataJson.getInstance().getTestDataMap(testName);
	}
	
	
	@Test
	public void orderItem()
	{
		
		WebDriverManager.chromedriver().setup();
		WebDriver driver=new ChromeDriver();
		driver.manage().window().maximize();	 
	    driver.get(envMap.get("GUI_URL"));
	    
		SauceDemoHome homePage=new SauceDemoHome(driver);
		SauceDemoInventory inventorypage=new SauceDemoInventory(driver);
		SauceDemoCart cartPage=new SauceDemoCart(driver);
		
		if (homePage.login(testDataMap.get("UserName"), testDataMap.get("Password")))
			logextent.reportUIResultRobot("PASS","Login Sucess!!", true, driver);
		else
			logextent.reportUIResultRobot("FAIL", "Login failed!!", true, driver);
		
		
		if (inventorypage.addItemToCart(testDataMap.get("Item")))
			logextent.reportUIResultRobot("PASS", "Item sucesssfully added to cart", true, driver);
		else
			logextent.reportUIResultRobot("FAIL", "add item to cart failed!!", true, driver);
		
		inventorypage.getShoppingCartBtn().click();
		
		
		if (cartPage.confirmOrder(testDataMap.get("firstName"),testDataMap.get("lastName"), ""+JavaUtilities.getRandomNumber()))
			logextent.reportUIResultRobot("PASS", "order confirmed", false, driver);
		else
			logextent.reportUIResultRobot("FAIL", "order confirmed failed!!", true, driver);
		
		String successText = cartPage.getOrderConformText().getText();
		sa.assertEquals(successText, testDataMap.get("Expected Text"));
		if (successText.equalsIgnoreCase(testDataMap.get("Expected Text")))
			logextent.reportUIResultRobot("PASS", "Order Successfull", true, driver);
		else
			logextent.reportUIResultRobot("FAIL", "Order failed", true, driver);
		
		driver.close();
		sa.assertAll();
		
	}

}
