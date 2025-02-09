package Utilities.Common;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.io.FileHandler;
import Utilities.Listener.ExtentListener;

public class WebElementUtils extends ExtentListener {

	public void selectDropdownOption(WebElement selectele, int index) {
		new Select(selectele).selectByIndex(index);
	}

	public void selectDropdownOption(WebElement selectele, String text) {
		new Select(selectele).selectByVisibleText(text);
	}

	public void selectDropdownOption(String text, WebElement selectele) {
		new Select(selectele).selectByValue(text);
	}

	public void waitForElementToClick(WebDriver driver, By locator, Duration timeout) {
		WebElement element = new WebDriverWait(driver, timeout).until(ExpectedConditions.elementToBeClickable(locator));
		element.click();
	}

	public WebElement waitForElementVisibility(WebDriver driver, WebElement element, Duration timeout) {
		return new WebDriverWait(driver, timeout).until(ExpectedConditions.visibilityOf(element));
	}

	public void waitForElementAndClick(WebDriver driver, WebElement element, Duration timeout) {
		new WebDriverWait(driver, timeout).until(ExpectedConditions.elementToBeClickable(element)).click();
	}

	public void waitforElement(WebElement element) throws Throwable {
		int count = 0;
		while (count < 40) {
			try {
				element.isDisplayed();
				break;
			} catch (Throwable e) {
				Thread.sleep(500);
				count++;
			}
		}
	}

	public void waitAndClick(WebElement element) throws Exception {
		int count = 0;
		while (count < 40) {
			try {
				element.click();
				break;
			} catch (Throwable e) {
				Thread.sleep(500);
				count++;
			}
		}
	}

	public WebElement fluentWait(WebDriver driver, WebElement element, Duration timeout, Duration polling) {
		Wait<WebDriver> wait = new FluentWait<>(driver).withTimeout(timeout).pollingEvery(polling)
				.ignoring(NoSuchElementException.class);
		return wait.until(ExpectedConditions.visibilityOf(element));
	}

	public void fluentWaitAndClick(WebDriver driver, WebElement element, Duration timeout, Duration polling) {
		Wait<WebDriver> wait = new FluentWait<>(driver).withTimeout(timeout).pollingEvery(polling)
				.ignoring(NoSuchElementException.class);
		wait.until(ExpectedConditions.elementToBeClickable(element)).click();
	}

	public void jsScrollToWebElement(WebDriver driver, WebElement element) {
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
	}

	public void jsScroll(WebDriver driver, int x, int y) {
		((JavascriptExecutor) driver).executeScript("window.scrollBy(arguments[0], arguments[1]);", x, y);
	}

	public void jsClick(WebDriver driver, WebElement element) {
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
	}

	public void mouseHover(WebDriver driver, WebElement element) {
		new Actions(driver).moveToElement(element).perform();
	}

	public void mouseDragAndDrop(WebDriver driver, WebElement source, WebElement target) {
		new Actions(driver).dragAndDrop(source, target).perform();
	}

	public void switchToFrame(WebDriver driver, int index) {
		driver.switchTo().frame(index);
	}

	public void switchToFrame(WebDriver driver, WebElement frameElement) {
		driver.switchTo().frame(frameElement);
	}

	public void switchToFrame(WebDriver driver, String frameNameOrId) {
		driver.switchTo().frame(frameNameOrId);
	}

	public void switchToWindow(WebDriver driver, String windowTitle) {
		for (String handle : driver.getWindowHandles()) {
			driver.switchTo().window(handle);
			if (driver.getTitle().contains(windowTitle)) {
				break;
			}
		}
	}

	public void alertAccept(WebDriver driver) {
		driver.switchTo().alert().accept();
	}

	public void alertDismiss(WebDriver driver) {
		driver.switchTo().alert().dismiss();
	}

	public void refreshPage(WebDriver driver) {
		driver.navigate().refresh();
	}

	public void waitForPageToLoad(WebDriver driver, Duration timeout) {
		driver.manage().timeouts().implicitlyWait(timeout);
	}

	public String takeScreenshot(String methodName, WebDriver driver) throws IOException {
		File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		String screenshotPath = System.getProperty("user.dir") + "/Screenshots/" + methodName + ".png";
		FileHandler.copy(src, new File(screenshotPath));
		return screenshotPath;
	}

	public static void flashWebElement(WebDriver driver, WebElement element) {
		for (int i = 0; i < 5; i++) {
			highlightElement("yellow", element, driver);
		}
	}

	public static void highlightElement(String color, WebElement element, WebDriver driver) {
		((JavascriptExecutor) driver).executeScript(
				"arguments[0].setAttribute('style', 'background: " + color + "; border: 2px solid red;');", element);
	}
}
