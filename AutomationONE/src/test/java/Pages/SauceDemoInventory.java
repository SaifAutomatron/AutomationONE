package Pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import Utilities.Common.WebElementUtils;
import lombok.Getter;

@Getter
public class SauceDemoInventory {

	WebDriver driver;
	WebElementUtils wutil=new WebElementUtils();

	public  SauceDemoInventory(WebDriver driver){
		this.driver =  driver;
		PageFactory.initElements(driver, this);
	}

	@FindBy(xpath = "//div[text()='Sauce Labs Backpack']")
	private WebElement inventoryItemName;

	@FindBy(xpath = "//button[text()='Add to cart']")
	private WebElement addToCartButton;

	@FindBy(xpath = "//a[@class='shopping_cart_link']")
	private WebElement shoppingCartBtn;
	
	@FindBy(xpath = "//div[@class='inventory_item_name']")
	private WebElement inventoryItem;
	


	public boolean addItemToCart(String itemName)
	{
		try {
			inventoryItemName.click();
			wutil.waitAndClick(addToCartButton);
			System.out.println("Item sucessfully added to cart");
			return true;
		} catch (Exception e) {
			System.err.println("failed to add item to cart!!");
			e.printStackTrace();
			return false;
		}

	}

}
