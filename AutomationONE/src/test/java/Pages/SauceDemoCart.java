package Pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import Utilities.Common.WebElementUtils;
import lombok.Getter;

@Getter
public class SauceDemoCart {

	WebDriver driver;
	WebElementUtils wutil=new WebElementUtils();

	public  SauceDemoCart(WebDriver driver){
		this.driver =  driver;
		PageFactory.initElements(driver, this);
	}

	@FindBy(id = "checkout")
	private WebElement checkoutButton;
	
	@FindBy(id = "first-name")
	private WebElement firstNameTxtbx;
	
	@FindBy(id = "last-name")
	private WebElement lastNameTxtbx;
	
	@FindBy(id = "postal-code")
	private WebElement postalCodeTxtbx;
	
	@FindBy(id = "continue")
	private WebElement continueButton;
	
	@FindBy(id = "finish")
	private WebElement finishButton;
	
	@FindBy(xpath ="//div[@id='checkout_complete_container']/h2")
	private WebElement orderConformText;
	
	
	public boolean confirmOrder(String firstName,String LastName,String postalCode)
	{
		try {
			checkoutButton.click();
			firstNameTxtbx.sendKeys(firstName);
			System.out.println("First name entered");
			lastNameTxtbx.sendKeys(LastName);
			System.out.println("Last name entered");
			postalCodeTxtbx.sendKeys(postalCode);
			System.out.println("Postal code entered");
			wutil.waitAndClick(continueButton);
			wutil.waitAndClick(finishButton);
			System.out.println("Order sucessfully completed");
			return true;
		} catch (Exception e) {
			System.err.println("Order confirmation failed!!");
			e.printStackTrace();
			return false;
		}
		
	}
	
	
	
	
}
