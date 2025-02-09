package Utilities.Common;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.touch.TapOptions;
import io.appium.java_client.touch.offset.PointOption;
import io.appium.java_client.TouchAction;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.List;

public class AppiumUtilities {

    private AppiumDriver driver;
    private AppiumDriverLocalService appiumService;

    public AppiumDriver getDriver() {
        return driver;
    }

    /**
     * Starts the Appium server.
     *
     * @param ipAddress The IP address for the Appium server.
     * @param port      The port for the Appium server.
     */
    public void startAppiumServer(String ipAddress, int port) {
        appiumService = new AppiumServiceBuilder()
                .withIPAddress(ipAddress)
                .usingPort(port)
                .build();
        appiumService.start();
        System.out.println("Appium server started at: " + appiumService.getUrl());
    }

    /**
     * Stops the Appium server.
     */
    public void stopAppiumServer() {
        if (appiumService != null && appiumService.isRunning()) {
            appiumService.stop();
            System.out.println("Appium server stopped.");
        }
    }

    /**
     * Initializes the Appium driver for Android.
     *
     * @param appiumServerURL The Appium server URL.
     * @param appPath         The path to the application.
     * @param deviceName      The name of the Android device.
     * @throws MalformedURLException If the URL is invalid.
     */
    public void initializeAndroidDriver(String appiumServerURL, String appPath, String deviceName) throws MalformedURLException {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
        capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, deviceName);
        capabilities.setCapability(MobileCapabilityType.APP, appPath);
        capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "UiAutomator2");
        capabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, 60);

        driver = new AndroidDriver(new URL(appiumServerURL), capabilities);
    }

    /**
     * Initializes the Appium driver for iOS.
     *
     * @param appiumServerURL The Appium server URL.
     * @param appPath         The path to the application.
     * @param deviceName      The name of the iOS device.
     * @throws MalformedURLException If the URL is invalid.
     */
    public void initializeIOSDriver(String appiumServerURL, String appPath, String deviceName) throws MalformedURLException {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "iOS");
        capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, deviceName);
        capabilities.setCapability(MobileCapabilityType.APP, appPath);
        capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "XCUITest");
        capabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, 60);

        driver = new IOSDriver(new URL(appiumServerURL), capabilities);
    }

    /**
     * Quits the Appium driver.
     */
    public void quitDriver() {
        if (driver != null) {
            driver.quit();
            System.out.println("Appium driver terminated.");
        }
    }

    /**
     * Implicitly waits for a specified duration.
     *
     * @param seconds The duration to wait, in seconds.
     */
    public void setImplicitWait(int seconds) {
        if (driver != null) {
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(seconds));
        }
    }

    /**
     * Clicks on a WebElement.
     *
     * @param element The WebElement to click.
     */
    public void clickElement(WebElement element) {
        element.click();
    }

    /**
     * Sends text to a WebElement.
     *
     * @param element The WebElement.
     * @param text    The text to send.
     */
    public void sendText(WebElement element, String text) {
        element.clear();
        element.sendKeys(text);
    }

    /**
     * Gets the text of a WebElement.
     *
     * @param element The WebElement.
     * @return The text of the element.
     */
    public String getElementText(WebElement element) {
        return element.getText();
    }

    /**
     * Verifies if a WebElement is displayed.
     *
     * @param element The WebElement.
     * @return True if the element is displayed, false otherwise.
     */
    public boolean isElementDisplayed(WebElement element) {
        return element.isDisplayed();
    }

   

   
    /**
     * Finds a WebElement by XPath.
     *
     * @param xpath The XPath of the element.
     * @return The WebElement.
     */
    public WebElement findElementByXPath(String xpath) {
        return driver.findElement(By.xpath(xpath));
    }

    /**
     * Finds multiple WebElements by class name.
     *
     * @param className The class name of the elements.
     * @return A list of WebElements.
     */
    public List<WebElement> findElementsByClassName(String className) {
        return driver.findElements(By.className(className));
    }

    /**
     * Captures a screenshot and saves it to a specified file path.
     *
     * @param filePath The file path to save the screenshot.
     */
    public void captureScreenshot(String filePath) {
        org.openqa.selenium.OutputType<File> outputType = org.openqa.selenium.OutputType.FILE;
        File screenshot = driver.getScreenshotAs(outputType);
        try {
            java.nio.file.Files.copy(screenshot.toPath(), java.nio.file.Paths.get(filePath));
        } catch (java.io.IOException e) {
            throw new RuntimeException("Failed to save screenshot: " + filePath, e);
        }
    }

    /**
     * Performs a drag-and-drop action from one element to another.
     *
     * @param source The element to drag.
     * @param target The element to drop onto.
     */
    public void dragAndDrop(WebElement source, WebElement target) {
        new org.openqa.selenium.interactions.Actions(driver)
                .dragAndDrop(source, target)
                .perform();
    }

    /**
     * Gets the page source of the current screen.
     *
     * @return The page source as a String.
     */
    public String getPageSource() {
        return driver.getPageSource();
    }

    /**
     * Retrieves the current app package (for Android only).
     *
     * @return The app package name.
     */
    public String getAppPackage() {
        if (driver instanceof AndroidDriver) {
            return ((AndroidDriver) driver).getCurrentPackage();
        }
        return "Not applicable for iOS";
    }

    /**
     * Locks the device for a specified duration.
     *
     * @param seconds The duration to lock the device, in seconds.
     */
    public void lockDevice(int seconds) {
        if (driver instanceof AndroidDriver) {
            ((AndroidDriver) driver).lockDevice(Duration.ofSeconds(seconds));
        } else if (driver instanceof IOSDriver) {
            ((IOSDriver) driver).lockDevice(Duration.ofSeconds(seconds));
        } else {
            throw new UnsupportedOperationException("lockDevice is not supported on this driver.");
        }
    }

    /**
     * Unlocks the device.
     */
    public void unlockDevice() {
        if (driver instanceof AndroidDriver) {
            ((AndroidDriver) driver).unlockDevice();
        } else if (driver instanceof IOSDriver) {
            ((IOSDriver) driver).unlockDevice();
        } else {
            throw new UnsupportedOperationException("unlockDevice is not supported on this driver.");
        }
    }
}