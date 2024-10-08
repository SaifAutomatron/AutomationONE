package Pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import lombok.Getter;

@Getter
public class SauceDemoHome {
WebDriver driver;
	
	public  SauceDemoHome(WebDriver driver){
		this.driver =  driver;
		PageFactory.initElements(driver, this);
	}

	@FindBy(id = "user-name")
	private WebElement userNameTxtbx;
	
	@FindBy(id = "password")
	private WebElement passwordTxtbx;
	
	@FindBy(id = "login-button")
	private WebElement loginButton;
	
	public boolean login(String userName,String password)
	{
		System.out.println("Logging in");
		try {
			userNameTxtbx.sendKeys(userName);
			passwordTxtbx.sendKeys(password);
			loginButton.click();
			System.out.println("Login Sucessful");
			return true;
		} catch (Exception e) {
			System.err.println("Login Failed!!");
			e.printStackTrace();
			return false;
		}
	}
	
}
