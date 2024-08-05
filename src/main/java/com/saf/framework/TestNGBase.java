package com.saf.framework;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import io.qameta.allure.Allure;
import io.qameta.allure.model.Status;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.testng.annotations.*;

import java.io.*;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static com.functions.Methods.WebDriverFactory.driver;

public class TestNGBase {

    public static WebDriver myDriver;
    public static ExtentReports oExtentReport;
    public static ExtentTest oExtentTest;
    public static int ssNumber;
    public static String reportPath;
    public static boolean dbFlag;
    public static int testCaseId = 0;
    public static String sDriverName = "";
    public static String className = "";



    @Parameters({"browserName"})
    @BeforeSuite
    //@BeforeTest
    public void BeforeSuite(@Optional("") String browserName) throws Throwable {
        //public void BeforeSuite() throws Throwable{
        reportPath = "Report_" + new Date().getDate() + "-" + (new Date().getMonth() + 1) + "-" + new Date().getHours() + "-" + new Date().getMinutes() + "-" + new Date().getSeconds();
        File f = new File("Reports/" + reportPath);
        File ss = new File("Reports/" + reportPath + "/Screenshots");
        try {
            f.mkdir();
            ss.mkdir();
        } catch (Exception e) {
            e.printStackTrace();
        }

        oExtentReport = new ExtentReports("Reports/" + reportPath + "/TestSuiteReport.html", true);
        oExtentReport.loadConfig(new File("config.xml"));
        ssNumber = 0;

        if (browserName.equalsIgnoreCase("ie")) {
            sDriverName = "ie";
        } else if (browserName.equalsIgnoreCase("firefox")) {
            sDriverName = "firefox";
        } else if (browserName.equalsIgnoreCase("chrome")) {
            sDriverName = "chrome";
        } else if (browserName.equalsIgnoreCase("htmlunit")) {
            sDriverName = "htmlunit";
        } else {
            throw new Exception("Unknown driver name = " + sDriverName +
                    "  Valid names are: ie,firefox,chrome,htmlunit");
        }



    }


    //********************************************************
    //End of execution
    //********************************************************
    @AfterClass
    public void automationTeardown() throws Exception {

        driver.quit();
        testCaseId = 0;

    }


    @AfterSuite
//	@AfterTest
    public void afterSuite() throws Throwable {
        oExtentReport.endTest(oExtentTest);
        oExtentReport.flush();

        //EmailReporting.sendMail();
        driver.quit();
        //oDriver.remove();
    }




    // click on element if exists
    public void clickOnElementIfExists(By popup) throws InterruptedException {
        if (myDriver.findElements(popup).size() != 0) {
            WebElement popUpExistsClose = myDriver.findElement(popup);
            popUpExistsClose.click();
        }
        Thread.sleep(500);
    }




    public void scrollToElementAndClick(WebElement webElement) throws Exception {
        ((JavascriptExecutor) myDriver).executeScript("arguments[0].scrollIntoView(true);", webElement);
        Thread.sleep(100);
        webElement.click();
        Thread.sleep(500);
    }



   public static boolean reportResult(String status, String message, boolean ssFlag) {
        try {
            String dest = "";
            if (ssFlag) {
                ssNumber++;
                TakesScreenshot ts = (TakesScreenshot) myDriver;
                File source = ts.getScreenshotAs(OutputType.FILE);
                dest = System.getProperty("user.dir") + "/Reports/" + reportPath + "/Screenshots/" + ssNumber + ".png";
                File destination = new File(dest);
                FileUtils.copyFile(source, destination);
            }


            if (status.equalsIgnoreCase("PASS")) {
                if (ssFlag) {
                    oExtentTest.log(LogStatus.PASS, message + "\n" + oExtentTest.addScreenCapture(dest));
                } else {
                    oExtentTest.log(LogStatus.PASS, message);
                }
                //DBReporting.insertExecutionDetailsIntoDB(testCaseId, "PASS", message, className, System.getProperty("user.name"));
            } else if (status.equalsIgnoreCase("FAIL")) {
                if (ssFlag) {
                    oExtentTest.log(LogStatus.FAIL, message + "\n" + oExtentTest.addScreenCapture(dest));
                } else {
                    oExtentTest.log(LogStatus.FAIL, message);
                }
                //DBReporting.insertExecutionDetailsIntoDB(testCaseId, "FAIL", message, className, System.getProperty("user.name"));
            } else {
                if (ssFlag) {
                    oExtentTest.log(LogStatus.INFO, message + "\n" + oExtentTest.addScreenCapture(dest));
                } else {
                    oExtentTest.log(LogStatus.INFO, message);
                }
                //DBReporting.insertExecutionDetailsIntoDB(testCaseId, "INFO", message, className, System.getProperty("user.name"));
            }
            //oExtentTest.log(LogStatus.INFO, oExtentTest.addScreenCapture(dest));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }


    public static void allureReport(String status, String message, boolean ssFlag) {
        try {
            if (ssFlag) {
                Allure.addAttachment("Screenshot : " + message, new ByteArrayInputStream(((TakesScreenshot) myDriver).getScreenshotAs(OutputType.BYTES)));
            }
            if (status.equalsIgnoreCase("PASS")) {
                Allure.step(message, Status.PASSED);
            } else if (status.equalsIgnoreCase("FAIL")) {
                Allure.step(message, Status.FAILED);
            } else {
                Allure.step(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
