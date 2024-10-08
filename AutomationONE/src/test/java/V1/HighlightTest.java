package V1;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;

import Utilities.Common.WebElementUtils;
import io.github.bonigarcia.wdm.WebDriverManager;

public class HighlightTest {

	@Test
	public  void test() {

		WebDriverManager.chromedriver().setup();
		WebDriver driver=new ChromeDriver();
		driver.manage().window().maximize();
		
		driver.get("https://www.lambdatest.com/");
		
		
		WebElement seacrhButton = driver.findElement(By.xpath("//button[contains(text(),'Book a Demo')]"));
		
		//WebElementUtils.flashWebElement(driver, seacrhButton);
		WebElementUtils.highlightWebElement("blue", seacrhButton, driver);
		
		driver.close();
		
	}

}
