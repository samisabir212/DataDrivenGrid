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

public class AddCustomerTest extends TestBase {
	
	
	
	@Test(dataProviderClass=DataProviders.class,dataProvider="bankManagerDP")
	public void addCustomerTest(Hashtable<String,String> data) throws MalformedURLException{
	
		super.setUp();

		test = extentRep.startTest("Add Customer Test"+"   " + data.get("browser"));

		setExtentTest(test);

		ExcelReader excel = new ExcelReader(Constants.BankManagerSuite_XLPATH);

		DataUtil.checkExecution("BankManagerSuite", "AddCustomerTest", data.get("Runmode"), excel);

		openBrowser(data.get("browser"));

		navigate("testsiteurl");

		explicitWait(10,"bmlBtn_XPATH");
		click("bmlBtn_XPATH");

		getExtTest().log(LogStatus.INFO,"bmlbutton clicked by css");

		explicitWait(10,"addCustBtn_XPATH");
		click("addCustBtn_XPATH");

		explicitWait(10,"firstname_XPATH");
		type("firstname_XPATH",data.get("firstname"));

		explicitWait(10,"lastname_XPATH");
		type("lastname_XPATH",data.get("lastname"));

		explicitWait(10,"postcode_XPATH");
		type("postcode_XPATH",data.get("postcode"));


		explicitWait(10,"addbtn_XPATH");
		click("addbtn_XPATH");
		
		reportPass("Add customer test pass");


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
