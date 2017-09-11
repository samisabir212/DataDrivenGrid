package com.w2a.utilities;

import java.lang.reflect.Method;

import org.testng.annotations.DataProvider;

public class DataProviders {
	
	


	/*
	this is where we are getting our data.
	must look at DataUtil to see how data is being read and implemented into the test
	*/
	@DataProvider(name="bankManagerDP",parallel=true)
	public static Object[][] getDataSuite1(Method m) {

		System.out.println(m.getName());
		
		ExcelReader excel = new ExcelReader(Constants.BankManagerSuite_XLPATH);
		String testcase = m.getName();
		return DataUtil.getData(testcase, excel);
	
	}



	@DataProvider(name="customerDP")
	public static Object[][] getDataSuite2(Method m) {

		System.out.println(m.getName());
		
		ExcelReader excel = new ExcelReader(Constants.SUITE2_XL_PATH);
		String testcase = m.getName();
		return DataUtil.getData(testcase, excel);
	
	}


}
