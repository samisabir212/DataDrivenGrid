package com.w2a.suite.bankmanager.testcases;

import com.w2a.utilities.DataUtil;
import com.w2a.utilities.ExcelReader;

/**
 * Created by sami on 9/28/17.
 */
public class checkExecution {


    public static void main(String[] args) {


        String suiteName = "DemoQASuite";

        boolean suiteRunmode = DataUtil.isSuiteRunnable(suiteName, new ExcelReader(System.getProperty("user.dir") + "/src/TestData/DemoQA_Excel.xlsx"));

        System.out.println(suiteRunmode);

    }


}
