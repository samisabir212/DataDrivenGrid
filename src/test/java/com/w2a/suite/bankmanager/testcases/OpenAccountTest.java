package com.w2a.suite.bankmanager.testcases;

import java.net.MalformedURLException;
import java.util.Hashtable;

import com.relevantcodes.extentreports.LogStatus;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import com.w2a.datadriven.base.TestBase;
import com.w2a.utilities.Constants;
import com.w2a.utilities.DataProviders;
import com.w2a.utilities.DataUtil;
import com.w2a.utilities.ExcelReader;

public class OpenAccountTest extends TestBase {
	
	
	
	@Test(dataProviderClass=DataProviders.class,dataProvider="bankManagerDP")
	public void openAccountTest(Hashtable<String,String> data) throws MalformedURLException{
	
		//super.setUp();
		setUp();
		test = extentRep.startTest("Open Account Test"+"   "+data.get("browser"));
		setExtentTest(test);
		ExcelReader excel = new ExcelReader(Constants.BankManagerSuite_XLPATH);
		DataUtil.checkExecution("BankManagerSuite", "OpenAccountTest", data.get("Runmode"), excel);
		openBrowser(data.get("browser"));
		navigate("testsiteurl");
		click("bmlBtn_CSS");
		getExtTest().log(LogStatus.INFO,"clicked to openaccount butten by css");
		click("openaccount_CSS");
		select("customer_XPATH",data.get("customer"));
		select("currency_CSS",data.get("currency"));
		click("process_XPATH");
		reportPass("Open Account test pass");
	}
	
	@AfterMethod
	public void tearDown(){
		
		if(extentRep !=null){
			
			extentRep.endTest(getExtTest());
			extentRep.flush();
		}
		getDriver().quit();
	}
	
	
	

}
