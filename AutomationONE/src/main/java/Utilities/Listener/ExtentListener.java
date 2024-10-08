package Utilities.Listener;

import java.io.File;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ISuiteResult;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.CodeLanguage;
import com.aventstack.extentreports.markuputils.MarkupHelper;

import Utilities.Common.SendMail;
import lombok.SneakyThrows;

/**
 * 
 * @author Saif
 *
 */

public class ExtentListener implements ITestListener,ISuiteListener {

	ITestContext ITC;
	ExtentReports extent;
	ExtentTest test;
	ExtentTest node;
	ITestResult result;
	public static int ssNumber;

	private static final ThreadLocal<ExtentTest> LocalThread=new ThreadLocal<ExtentTest>();


	public void logInfo(String message)
	{
		LocalThread.get().info(message);

	}

	public void logJsonInfo(String json)
	{
		LocalThread.get().info(MarkupHelper.createCodeBlock(json,CodeLanguage.JSON));
	}

	public void logXMLInfo(String xml)
	{
		LocalThread.get().info(MarkupHelper.createCodeBlock(xml,CodeLanguage.XML));
	}
	public void logReportPass(String message)
	{
		LocalThread.get().log(Status.PASS, message);
	}

	public void logReportFail(String message)
	{
		LocalThread.get().log(Status.FAIL, message);
	}

	@SneakyThrows
	public boolean reoprtResult(String status,String message,boolean ssFlag,WebDriver driver)
	{
		String dest="";
		String screenshotPath="";
		if(ssFlag) {
			String sNumber=ssNumber+"";
			TakesScreenshot ts=(TakesScreenshot)driver;
			File source=ts.getScreenshotAs(OutputType.FILE);
			dest=java.nio.file.Paths.get(ExtentReporterCls.reportFolderPath, "Screenshots",sNumber+".png").toString();
			File destination=new File(dest);
			FileUtils.copyFile(source, destination);
			screenshotPath=".\\Screenshots\\"+ssNumber+".png";
		}
		if(status.equalsIgnoreCase("PASS"))
		{
			if(ssFlag)
			{
				LocalThread.get().log(Status.PASS, message,MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
			}
			else {
				LocalThread.get().log(Status.PASS, message);
			}

		}
		else if(status.equalsIgnoreCase("FAIL"))
		{
			if(ssFlag)
			{
				LocalThread.get().log(Status.FAIL, message,MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
			}
			else {
				LocalThread.get().log(Status.FAIL, message);
			}

		}
		else
		{
			if(ssFlag)
			{
				LocalThread.get().log(Status.INFO, message,MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
			}
			else {
				LocalThread.get().log(Status.INFO, message);
			}

		}


		return true;

	}
	
	
	@SneakyThrows
	public boolean reoprtResultRobot(String status,String message,boolean ssFlag,WebDriver driver)
	{
		String dest="";
		if(ssFlag) {
			String sNumber=ssNumber+"";
			BufferedImage screencapture = new Robot().createScreenCapture((new Rectangle(Toolkit.getDefaultToolkit().getScreenSize())));
			dest=java.nio.file.Paths.get(ExtentReporterCls.reportFolderPath, "Screenshots",sNumber+".png").toString();
			File file = new File(dest);
            ImageIO.write(screencapture, "png", file);
		}
		if(status.equalsIgnoreCase("PASS"))
		{
			if(ssFlag)
			{
				LocalThread.get().log(Status.PASS, message,MediaEntityBuilder.createScreenCaptureFromPath(dest).build());
			}
			else {
				LocalThread.get().log(Status.PASS, message);
			}

		}
		else if(status.equalsIgnoreCase("FAIL"))
		{
			if(ssFlag)
			{
				LocalThread.get().log(Status.FAIL, message,MediaEntityBuilder.createScreenCaptureFromPath(dest).build());
			}
			else {
				LocalThread.get().log(Status.FAIL, message);
			}

		}
		else
		{
			if(ssFlag)
			{
				LocalThread.get().log(Status.INFO, message,MediaEntityBuilder.createScreenCaptureFromPath(dest).build());
			}
			else {
				LocalThread.get().log(Status.INFO, message);
			}

		}


		return true;

	}


	@Override
	public void onStart(ISuite suite) {
		extent=ExtentReporterCls.ReportGenerator(suite.getName());
	}

	@Override
	public void onTestStart(ITestResult result) {
		node=test.createNode(result.getMethod().getMethodName());
		LocalThread.set(node);
	}
	@Override
	public void onTestSuccess(ITestResult result) {
		LocalThread.get().log(Status.PASS, "----------------TEST CASE PASSED----------------");
	}
	@Override
	public void onTestFailure(ITestResult result) {
		LocalThread.get().fail(result.getThrowable());
	}
	@Override
	public void onTestSkipped(ITestResult result) {
		LocalThread.get().skip(result.getThrowable());
	}
	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {

	}
	@Override
	public void onTestFailedWithTimeout(ITestResult result) {

	}
	@Override
	public void onStart(ITestContext context) {
		test=extent.createTest(context.getName());
	}
	@Override
	public void onFinish(ITestContext context) {

	}


	@Override
	public void onFinish(ISuite suite) {
		extent.flush();
		int failCount=0;
		String status="Passed";
		Map<String, ISuiteResult> suiteResults = suite.getResults();
		for( ISuiteResult sr:suiteResults.values())
		{
			ITestContext tc = sr.getTestContext();
			if(tc.getFailedTests().getAllResults().size()>0)
				failCount++;
			if(tc.getFailedConfigurations().getAllResults().size()>0)
				failCount++;

			if(failCount>0)
				status="Failed";
			SendMail.sendOutputMail(ExtentReporterCls.path, status);
		}
	}





}
