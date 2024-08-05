package com.page;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public class BasePage {


    public WebDriver oDriver;
    public ExtentReports oExtentReports;
    public ExtentTest oExtentTest;


    //initializing the driver
    public BasePage(WebDriver oDriver, ExtentReports oExtentReports, ExtentTest oExtentTest) {
        this.oDriver = oDriver;
        this.oExtentReports = oExtentReports;
        this.oExtentTest = oExtentTest;

        // PageFactory.initElements(oDriver, this);
    }










}
