
package Utilities.Common;

import java.io.File; import java.io.IOException; import java.util.Set; import
java.util.concurrent.TimeUnit;

import org.openqa.selenium.By; import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType; import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement; import
org.openqa.selenium.interactions.Actions; import
org.openqa.selenium.support.events.EventFiringWebDriver; import
org.openqa.selenium.support.ui.ExpectedConditions; import
org.openqa.selenium.support.ui.Select; import
org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.io.Files;

import Utilities.Listener.ExtentListener;

/**
 * 
 * @author Saif
 *
 */


public class WebElementUtils extends ExtentListener{

	/**
	 * selects dropdown element by index number
	 * 
	 * @param driver
	 * @param webelement
	 * @param int        index
	 */


	public void selectDropdownOption(WebElement selectele,int index) { Select
		sel=new Select(selectele); sel.selectByIndex(index); }
	/**
	 * selects dropdown element by visible text
	 * 
	 * @param driver
	 * @param webelement
	 * @param String     text
	 */


	public void selectDropdownOption(WebElement selectele,String text) { Select
		sel=new Select(selectele); sel.selectByVisibleText(text); }
	/**
	 * selects dropdown element by value
	 * 
	 * @param text
	 * @param selectele
	 */


	public void selectDropdownOption(String text,WebElement selectele) { Select
		sel=new Select(selectele); sel.selectByValue(text); }

	/**
	 * wait for element to be clickable and the click it(Explicit wait)
	 * 
	 * @param driver
	 * @param locator
	 */


	public void waitForElementToclick(WebDriver driver,By locator) {
		WebDriverWait wait=new WebDriverWait(driver, PathConst.EXPLICITWAIT);
		WebElement elementclickable =
				wait.until(ExpectedConditions.elementToBeClickable(locator));
		elementclickable.click(); }
	/**
	 * wait for element to be visible
	 * 
	 * @param driver
	 * @param locator
	 * @return
	 */


	public WebElement waitForElementVisibility(WebDriver driver,WebElement
			element) { WebDriverWait wait=new WebDriverWait(driver,
					PathConst.EXPLICITWAIT); WebElement visibleEle =
					wait.until(ExpectedConditions.visibilityOf(element));

					return visibleEle; }
	/**
	 * wait until webelement is present in webpage
	 * 
	 * @param driver
	 * @param element
	 * @return
	 */


	public WebElement waitForElementPresent(WebDriver driver,WebElement element)
	{ WebDriverWait wait=new WebDriverWait(driver, PathConst.EXPLICITWAIT);
	WebElement visibleEle = wait.until(ExpectedConditions.visibilityOf(element));

	return visibleEle; }
	/**
	 * used to wait for expected element in GUI(customised wait)
	 * 
	 * @param element
	 * @throws Throwable
	 */


	public void waitforElement( WebElement element) throws Throwable { int count
		= 0; while(count < 40) { try { element.isDisplayed(); break; }catch
			(Throwable e) { Thread.sleep(500); count++; } } }
	/**
	 * used to wait & click for expected element in GUI(customised wait)
	 * 
	 * @param element
	 * @throws Throwable
	 */


	public void waitAndClick( WebElement element) throws Exception { int count =
			0; while(count < 40) { try { element.click(); break; }catch (Throwable e) {
				Thread.sleep(500); count++; } } }
	/**
	 * scrolls upto webelement
	 * 
	 * @param driver
	 * @param webelement
	 */


	public void jsScrollToWebElement(WebDriver driver,WebElement element) {
		JavascriptExecutor js=(JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView(true)", element); }
	/**
	 * scroll to mentioned x and y coordinates
	 * 
	 * @param driver
	 * @param x      coordinate
	 * @param y      coordinate
	 */


	public void jsScroll(WebDriver driver,int x,int y) { JavascriptExecutor
		js=(JavascriptExecutor) driver;
	js.executeScript("window.scrollBy("+x+","+y+")"); }

	public void jsClick(WebDriver driver,WebElement element) { JavascriptExecutor
		js=(JavascriptExecutor) driver;
	js.executeScript("arguments[0].click();",element); }



	/**
	 * performs click at offset coordinates
	 * 
	 * @param driver
	 * @param xOffset
	 * @param yOffset
	 */


	public void mouseActionMoveByoffsetClick(WebDriver driver,int xOffset, int
			yOffset) { Actions act=new Actions(driver); act.moveByOffset(xOffset,
					yOffset).click().perform(); }
	/**
	 * perform mouse hover action
	 * 
	 * @param driver
	 * @param element
	 */


	public void mouseActionHoverTo(WebDriver driver,WebElement element) { Actions
		act=new Actions(driver); act.moveToElement(element).perform(); }
	/**
	 * 
	 * @param driver
	 * @param element
	 * @param xOffset
	 * @param yOffset
	 */


	public void mouseActionRangeSlider(WebDriver driver,WebElement element,int
			xOffset, int yOffset) { Actions act=new Actions(driver);
			act.clickAndHold(element).perform(); act.moveByOffset(xOffset,
					yOffset).perform(); act.release(element).perform(); }
	/**
	 * 
	 * @param driver
	 * @param sourceElement
	 * @param targetElement
	 */


	public void mouseActionDragandDrop(WebDriver driver,WebElement
			sourceElement,WebElement targetElement) { Actions act=new Actions(driver);
			act.clickAndHold(sourceElement).perform(); act.dragAndDrop(sourceElement,
					targetElement).perform(); act.release(targetElement).perform(); }

	/**
	 * switch to frame by frame index
	 * 
	 * @param driver
	 * @param index
	 */


	public void swithToFrame(WebDriver driver,int index) {
		driver.switchTo().frame(index); }
	/**
	 * switch to frame by frame element
	 * 
	 * @param driver
	 * @param frameElement
	 */


	public void swithToFrame(WebDriver driver,WebElement frameElement) {
		driver.switchTo().frame(frameElement); }
	/**
	 * switch to frame by frame number or id
	 * 
	 * @param driver
	 * @param frameNameorID
	 */


	public void swithToFrame(WebDriver driver,String frameNameorID) {
		driver.switchTo().frame(frameNameorID); }
	/**
	 * used to switch to another browser window based on browser partial / complete
	 * title
	 * 
	 * @param driver
	 * @param browserTitle
	 */


	public void switchToWindow(WebDriver driver,String browserTitle) {
		Set<String> set = driver.getWindowHandles();

		for(String act : set) { driver.switchTo().window(act); String actPageTile =
				driver.getTitle(); if(actPageTile.contains(browserTitle)) { break; } } }
	/**
	 * used to Switch to Alert Popup & click on OK button
	 * 
	 * @param driver
	 */


	public void alertOK(WebDriver driver) {

		driver.switchTo().alert().accept(); }

	/**
	 * used to Switch to Alert Popup & click on cancel button
	 * 
	 * @param driver
	 */


	public void alertCancel(WebDriver driver) {

		driver.switchTo().alert().dismiss(); }
	/**
	 * used to refresh the page
	 * 
	 * @param driver
	 */


	public void refresh(WebDriver driver) { driver.navigate().refresh(); }
	/**
	 * waits for page to load all of its components
	 * 
	 * @param driver
	 */


	public void waitForPageToLoad(WebDriver driver) {
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(PathConst.IMPLICITWAIT,
				TimeUnit.SECONDS); }
	/**
	 * 
	 * @param methodName
	 * @throws IOException
	 */
	public String takeScreenShot(String methodName,WebDriver driver) throws
	IOException { EventFiringWebDriver event=new EventFiringWebDriver(driver);
	File src=event.getScreenshotAs(OutputType.FILE); String
	screenshotPath=System.getProperty("user.dir")+"/Screenshots/"+methodName+
	".PNG"; File dest=new File(screenshotPath); Files.copy(src, dest);

	return screenshotPath;

	}

	public static void flashWebElement(WebDriver driver,WebElement element) {
		for (int i = 0; i < 5; i++) {
			highlightWebElement("yellow", element, driver);
		}

	}

	public static void highlightWebElement(String colour, WebElement element, WebDriver driver) {
		JavascriptExecutor js=((JavascriptExecutor)driver);
		js.executeScript("arguments[0].setAttribute('style', 'background: "+colour+"; border: 2px solid red;');", element);

		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {

		}
	}



}
