package com.w2a.datadriven.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Platform;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeSuite;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import com.w2a.utilities.ExtentManager;

public class TestBase {
	
	/*
	 * 
	 * WebDriver
	 * logs
	 * properties
	 * excel
	 * db
	 * mail
	 * Extent Reports
	 * Grid
	 * Docker
	 *
	 *
	 * 
	 */
	/*   dr is the remote driver   not a regular webdriver */
	public static ThreadLocal<RemoteWebDriver> dr = new ThreadLocal<RemoteWebDriver>();
	public RemoteWebDriver driver=null;
	public Properties OR = new Properties();
	public Properties Config = new Properties();
	public FileInputStream fis;
	public Logger log = Logger.getLogger("devpinoyLogger");
	public WebDriverWait wait;
	public ExtentReports extentRep = ExtentManager.getInstance();
	public ExtentTest test;

	/*
	* multithreaded extent report
	* */
	public static ThreadLocal<ExtentTest> exTest = new ThreadLocal<ExtentTest>();

	public String browser;

	//this is your log4j logger for each thread
	public void addLog(String message){


		log.debug("Thread : "+getThreadValue(dr.get())+"  "+"Browser : "+browser+"  "+message);
	}




	/*
	* this initial set up is without a browser launch
	* it is just loading the properites fi
	* */
	public void setUp(){

		if(driver==null){

			try {
				fis = new FileInputStream(System.getProperty("user.dir")+"/src/test/resources/properties/Config.properties");
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				Config.load(fis);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			try {
				fis = new FileInputStream(System.getProperty("user.dir")+"/src/test/resources/properties/OR.properties");
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				OR.load(fis);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

			}
		}
	}
	
	/*
	* this is getting the local thread webdriver driver of the thread remote driver  LOCAL driver
	* */
	public WebDriver getDriver(){
		
		return dr.get();
	}

	/*
	* setting the webdriver to the remote webdriver instance***
	*
	* the webdriver must be in par with the remotedriver... without the webdriver you cant initiate the remotewebdriver
	* */
	public void setWebDriver(RemoteWebDriver driver){
		
		dr.set(driver);
		
	}
	
	public void setExtentTest(ExtentTest et){
		
		exTest.set(et);
	}
	
	public ExtentTest getExtTest(){
		
		/*
		* exTest is the thread local extent report
		* getting it to create multiple threads inorder to report on multiple nodes
		* */
		return exTest.get();
	}




	public void openBrowser(String browser) throws MalformedURLException{

		this.browser = browser;
		DesiredCapabilities cap = null;

		if(browser.equals("firefox")){

			cap = DesiredCapabilities.firefox();
			cap.setBrowserName("firefox");
			cap.setPlatform(Platform.ANY);

		}else if(browser.equals("chrome")){

			cap = DesiredCapabilities.chrome();
			cap.setBrowserName("chrome");
			cap.setPlatform(Platform.ANY);

		}else if(browser.equals("iexplore")){

			cap = DesiredCapabilities.internetExplorer();
			cap.setBrowserName("iexplore");
			cap.setPlatform(Platform.WINDOWS);

		}


		driver = new RemoteWebDriver(new URL("http://192.168.1.175:4444/wd/hub"),cap);
		setWebDriver(driver);
//		getDriver().manage().timeouts().implicitlyWait(Integer.parseInt(Config.getProperty("implicit.wait")), TimeUnit.SECONDS);
		getDriver().manage().window().maximize();
		getExtTest().log(LogStatus.INFO, "Browser openend successfully"+browser);

		//System.out.println(dr.get());
		System.out.println("Thread value is : "+getThreadValue(dr.get()));

	}


	/*
	* getting the thread value of each browser
	* like getting the ID of the threade
	* */
	public String getThreadValue(Object value){
		
		String text = value.toString();
		String[] newText = text.split(" ");
		String text2 = newText[newText.length-1].replace("(", "").replace(")", "");
		//String[] newText2 = text2.split("-");
		//String reqText = newText2[newText2.length-1];
		return text2;

	}
	



	
	public void reportPass(String msg){
		
		getExtTest().log(LogStatus.PASS, msg);
	}


	public void reportFailure(String msg){

		/*
		* passing the locator as the msg
		* */
		
		getExtTest().log(LogStatus.FAIL, msg);
		captureScreenshot();
		Assert.fail(msg);
	}


	public void navigate(String url){
		
		getDriver().get(Config.getProperty(url));
		getExtTest().log(LogStatus.INFO, "Navigating to " + Config.getProperty(url));

		/*
		* the .log method only works when you ust the startTest Method
		* */
	}
	
	
	public void click(String locator) {

		/*
		* using the try block to catch the failure and get the screen shot on failure
		* */

		try{
		if (locator.endsWith("_CSS")) {
			getDriver().findElement(By.cssSelector(OR.getProperty(locator))).click();
		} else if (locator.endsWith("_XPATH")) {
			getDriver().findElement(By.xpath(OR.getProperty(locator))).click();
		} else if (locator.endsWith("_ID")) {
			getDriver().findElement(By.id(OR.getProperty(locator))).click();
		}
		
		addLog("Clicking on an Element : "+locator);

		}catch(Throwable t){
			
			reportFailure("Failing while clicking on an Element"+locator);
		}
	
	}

	public void type(String locator, String value) {

			/*
		* using the try block to catch the failure and get the screen shot on failure
		* */

		try{
		if (locator.endsWith("_CSS")) {
			getDriver().findElement(By.cssSelector(OR.getProperty(locator))).sendKeys(value);
		} else if (locator.endsWith("_XPATH")) {
			getDriver().findElement(By.xpath(OR.getProperty(locator))).sendKeys(value);
		} else if (locator.endsWith("_ID")) {
			getDriver().findElement(By.id(OR.getProperty(locator))).sendKeys(value);
		}
		addLog("Typing in an Element : "+locator);
		}catch(Throwable t){
			
			reportFailure("Failing while typing in an Element"+locator);
		}

	
	}
	
	static WebElement dropdown;

	public void select(String locator, String value) {

			/*
		* using the try block to catch the failure and get the screen shot on failure
		* */


		try{
		if (locator.endsWith("_CSS")) {
			dropdown = getDriver().findElement(By.cssSelector(OR.getProperty(locator)));
		} else if (locator.endsWith("_XPATH")) {
			dropdown = getDriver().findElement(By.xpath(OR.getProperty(locator)));
		} else if (locator.endsWith("_ID")) {
			dropdown = getDriver().findElement(By.id(OR.getProperty(locator)));
		}
		
		Select select = new Select(dropdown);
		select.selectByVisibleText(value);
		}catch(Throwable t){
			
			reportFailure("Failing while Selecting an Element"+locator);
		}

	
	}

	public boolean isElementPresent(By by) {

		try {

			getDriver().findElement(by);
			return true;

		} catch (NoSuchElementException e) {

			return false;

		}

	}




	public static String screenshotPath;
	public static String screenshotName;

	public void captureScreenshot() {

		File scrFile = ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.FILE);

		Date d = new Date();
		screenshotName = d.toString().replace(":", "_").replace(" ", "_") + ".jpg";

		try {
			FileUtils.copyFile(scrFile,
					new File(System.getProperty("user.dir") + "/reports/" + screenshotName));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		getExtTest().log(LogStatus.INFO,  " Screenshot -> "+ test.addScreenCapture(screenshotName));

	}









}
