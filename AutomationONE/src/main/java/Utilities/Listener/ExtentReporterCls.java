package Utilities.Listener;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import Utilities.Excel.EnvironmentData;
import lombok.SneakyThrows;

public class ExtentReporterCls {
	
	static ExtentReports extent;
	static String path="";
	static String reportFolderPath="";
	static HashMap<String, String> envMap=null;
	
	@SneakyThrows
	public static ExtentReports ReportGenerator(String name) {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MMM-dd_HH_mm");
		String ReportFolderName="Report";
		reportFolderPath=java.nio.file.Paths.get(System.getProperty("user.dir"),"HtmlReports","Extent",ReportFolderName).toString();
		path=java.nio.file.Paths.get(reportFolderPath, name+"_Report.html").toString();
		ExtentSparkReporter esr=new ExtentSparkReporter(path);
		EnvironmentData.getInstance().fetchEnvironmentData();
		envMap=EnvironmentData.getInstance().getEnvironmentDataMap();
		esr.config().setReportName(envMap.get("REPORT_HEADER")+" | "+envMap.get("ENV_CODE"));
		esr.config().setDocumentTitle(sdf+"Test Results");
		extent=new ExtentReports();
		extent.attachReporter(esr);
		extent.setSystemInfo("Environment", envMap.get("ENV_CODE"));
		extent.setSystemInfo("Tester", System.getProperty("user.name"));
		extent.setSystemInfo("OS", System.getProperty("os.name"));
		return extent;
		
	}

}
