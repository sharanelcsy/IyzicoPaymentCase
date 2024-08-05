package com.saf.framework;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import static java.util.concurrent.TimeUnit.SECONDS;


public class MethodsLibrary {




    //-----------------------------------------------
    //Define the method of Desired capability of the browsers - javascript = true and set the Proxy for the browser
    //-----------------------------------------------
    public static DesiredCapabilities getCapability() {
        DesiredCapabilities oCapability = new DesiredCapabilities();
        oCapability.setJavascriptEnabled(true);
        //oCapability.setCapability("proxy", getProxy());

        return oCapability;
    }

    //-----------------------------------------------
    //set InterExplorer options by merging the Desired Capability
    //-----------------------------------------------
    public static InternetExplorerOptions getIEOptions() {
        InternetExplorerOptions oIEOptions = new InternetExplorerOptions();
        oIEOptions.merge(getCapability());
        oIEOptions.ignoreZoomSettings();
        oIEOptions.introduceFlakinessByIgnoringSecurityDomains();

        return oIEOptions;
    }

    //-----------------------------------------------
    //set ChromeOptions by merging the Desired Capability
    //-----------------------------------------------
    public static ChromeOptions getChromeOptions() {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.merge(getCapability());
        chromeOptions.addArguments("test-type");
        //Dil çevirme penceresini kapattırma.
        chromeOptions.addArguments("disable-translate");
        //Browser tam ekranda gösterilir.
        // chromeOptions.addArguments("start-maximized");
        //Pop-uplar bloklanır.
        chromeOptions.addArguments("disable-popup-blocking");
        chromeOptions.setPageLoadStrategy(PageLoadStrategy.NONE);
        return chromeOptions;
    }

    //-----------------------------------------------
    //set FirefoxOptions by merging the Desired Capability
    //-----------------------------------------------
    public static FirefoxOptions getFirefoxOptions() {
        FirefoxOptions oFirefoxOptions = new FirefoxOptions();
        oFirefoxOptions.merge(getCapability());

        return oFirefoxOptions;
    }

    //----------------------------------------
    //Define browserId
    //----------------------------------------
    public static int getBrowserId(String sBrowserName) {
        if (sBrowserName.equalsIgnoreCase("ie")) return 1;
        if (sBrowserName.equalsIgnoreCase("firefox")) return 2;
        if (sBrowserName.equalsIgnoreCase("chrome")) return 3;
        if (sBrowserName.equalsIgnoreCase("htmlunit")) return 4;

        return -1;
    }




    //-------------------------------------------
    //Define remoteDriver type
    //-------------------------------------------
    public static WebDriver getRemoteDriver(String sHubUrl, String sBrowserName) throws Exception {
        WebDriver oDriver;
        DesiredCapabilities oCapability = getCapability();

        switch (getBrowserId(sBrowserName)) {
            case 1:
                oCapability.setBrowserName("internet explorer");
                break;

            case 2:
                oCapability.setBrowserName("firefox");
                break;

            case 3:
                oCapability.setBrowserName("chrome");
                break;

            case 4:
                oCapability.setBrowserName("htmlunit");

            default:
                throw new Exception("Unknown browsername = " + sBrowserName +
                        "  Valid names are: ie,firefox,chrome,htmlunit");
        }

        oCapability.setPlatform(Platform.WINDOWS);

        oDriver = new RemoteWebDriver(new URL(sHubUrl), oCapability);

        if (getBrowserId(sBrowserName) != 4) {
            oDriver.manage().window().maximize();
        }


        oDriver.manage().deleteAllCookies();
        oDriver.manage().timeouts().pageLoadTimeout(Constants.lngPageLoadTimeout, SECONDS);
        oDriver.manage().timeouts().implicitlyWait(Constants.lngImplicitWaitTimeout, SECONDS);

        return oDriver;
    }
    //-----------------------------------------------------------------------------------------------------------------------------------------------------------------------
    //This method is used to perform Send Keys after validation
    //-----------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public static boolean sendKeys(WebElement element, String text) {
        boolean flag = false;
        try {
            if (element.isDisplayed() && element.isEnabled()) {
                waitSeconds(1);
                element.click();
                if (element.getText().equals("")) {
                    element.clear();
                    waitSeconds(1);
                }
                element.sendKeys(text);
                TestNGBase.reportResult("PASS", "A value has been entered in the Input field. Text: " + text, true);
                TestNGBase.allureReport("PASS", "A value has been entered in the Input field.Text : " + text, true);
                return true;
            }
        } catch (Exception e) {
            TestNGBase.reportResult("FAIL", "Could not enter value for " + element.getText() + " element.", true);
            TestNGBase.allureReport("FAIL", "Could not enter value for " + element.getText() + " element.", true);
            Assert.fail("Could not enter value for element." + element.getText());
            flag = false;
        }
        return flag;
    }

    //-----------------------------------------------------------------------------------------------------------------------------------------------------------------------
    //This method is used to perform click element after validation
    //-----------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public static boolean clickElement(WebDriver oDriver, WebElement element) {
        boolean flag = false;
        try {
            Actions actions = new Actions(oDriver);
            waitElementClickable(oDriver, element);
            actions.moveToElement(element).click().perform();
            TestNGBase.reportResult("PASS", "A value has been clicked in the " + element.getText() + " Input field.", true);
            TestNGBase.allureReport("PASS", "A value has been clicked in the " + element.getText() + " Input field.", true);
            flag = true;
        } catch (Exception e) {
            Assert.fail("Could not enter value for element." + element.getText());
            TestNGBase.reportResult("FAIL", "Could not enter value for " + element.getText() + " element.", true);
            TestNGBase.allureReport("FAIL", "Could not enter value for " + element.getText() + " element.", true);
        }
        return flag;
    }

    //-----------------------------------------------------------------------------------------------------------------------------------------------------------------------
    //This method is used to set a particular attribute using JavaScript
    //-----------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public static boolean setAttribute(WebDriver oDriver, By identifier, String attribute, String value) {
        if (oDriver.findElements(identifier).size() > 0) {
            WebElement oElement = oDriver.findElement(identifier);
            if (oElement.isDisplayed() && oElement.isEnabled()) {
                JavascriptExecutor jsExec = (JavascriptExecutor) oDriver;
                jsExec.executeScript("arguments[0].setAttribute('" + attribute + "','" + value + "')", oElement);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }


    //-----------------------------------------------------------------------------------------------------------------------------------------------------------------------
    //This method is used to validate if an element is visible
    //Make sure the same method is used throughout the pages classes as well as other generic methods
    //-----------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public static boolean checkElementVisibility(WebElement element) {
        boolean res = false;
        if (element.isDisplayed()) {
            res = true;
        } else {
            return res;
        }
        return res;
    }



    public static boolean checkElementDisplayed(WebDriver oDriver, By identifier) {
        boolean status = false;
        oDriver.manage().timeouts().implicitlyWait(Constants.lngImplicitWaitTimeout, SECONDS);
        try {
            if (oDriver.findElement(identifier).isDisplayed()) {
                status = true;
            }
        } catch (NoSuchElementException e) {
            System.out.println("Element not found");
        }
        return status;
    }


    public static void waitSeconds(int sec) {
        try {
            Thread.sleep(sec * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }






    public static boolean clickOnDesiredElementInList(WebDriver oDriver, List<WebElement> oList, String expectedText) {
        boolean flag = false;
        if (oList.size() != 0) {
            scrollDownUntilSeenElement(oDriver, oList.get(0));
            for (WebElement element : oList) {
                if (element.getText().toUpperCase(Locale.ROOT).contains(expectedText.toUpperCase(Locale.ROOT))) {
                    clickElement(oDriver, element);
                    flag = true;
                    break;
                } else {
                    scrollDownUntilSeenElement(oDriver, element);
                }
            }
        } else {
            System.out.println("List not found");
            TestNGBase.reportResult("FAIL", oList.size() + " is null", true);
            TestNGBase.allureReport("FAIL", oList.size() + " is null", true);

            flag = false;
        }
        return flag;
    }


    public static boolean clickOnDesiredEqualElementInList(WebDriver oDriver, List<WebElement> oList, String expectedText) {
        boolean flag = false;
        try {
            scrollDownUntilSeenElement(oDriver, oList.get(0));
            for (WebElement element : oList) {
                if (element.getText().equals(expectedText)) {
                    element.click();
                    flag = true;
                    TestNGBase.allureReport("PASS", "The desired value was found in the list and clicked. Desired: " + expectedText, true);
                    break;
                } else {
                    scrollDownUntilSeenElement(oDriver, element);
                }
            }
        } catch (Exception e) {
            System.out.println("List not found");
            TestNGBase.reportResult("FAIL", oList.size() + " is null", true);
            TestNGBase.allureReport("FAIL", oList.size() + " is null", true);
            flag = false;
        }
        return flag;
    }


    public static boolean clickOnDesiredEqualElementInList(List<WebElement> oList, String expectedText) {
        boolean flag = false;
        try {
            for (WebElement element : oList) {
                if (element.getText().equals(expectedText)) {
                    element.click();
                    flag = true;
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("List not found");
            TestNGBase.reportResult("FAIL", oList.size() + " is null", true);
            TestNGBase.allureReport("FAIL", oList.size() + " is null", true);

            flag = false;
        }
        return flag;
    }


    public static boolean clickOnDesiredElementInList(List<WebElement> oList, String expectedText) {
        boolean flag = false;
        try {
            for (WebElement element : oList) {
                if (element.getText().toUpperCase(Locale.ROOT).contains(expectedText.toUpperCase(Locale.ROOT))) {
                    element.click();
                    flag = true;
                    break;
                }
            }
        } catch (Exception e) {
            TestNGBase.reportResult("FAIL", oList.size() + " is null", true);
            TestNGBase.allureReport("FAIL", oList.size() + " is null", true);

            Assert.fail("List is null");
            flag = false;
        }
        return flag;
    }
    public static boolean clickOnDesiredElementInList(List<WebElement> oList, String expectedText, int sec) {
        boolean flag = false;
        try {
            for (WebElement element : oList) {
                if (element.getText().toUpperCase(Locale.ROOT).contains(expectedText.toUpperCase(Locale.ROOT))) {
                    element.click();
                    waitSeconds(sec);
                    flag = true;
                    break;
                }
            }
        } catch (Exception e) {
            TestNGBase.reportResult("FAIL", oList.size() + " is null", true);
            TestNGBase.allureReport("FAIL", oList.size() + " is null", true);

            Assert.fail("List is null");
            flag = false;
        }
        return flag;
    }


    public static boolean clickOnDesiredElementInList(List<WebElement> oList, int selected) {
        boolean status = false;
        if (oList.size() != 0) {
            oList.get(selected - 1).click();
            status = true;
        }
        return status;
    }


    public static boolean waitElementClickable(WebDriver oDriver, WebElement element) {
        boolean flag = false;
        WebDriverWait wait = new WebDriverWait(oDriver, 60);
        try {
            wait.until(ExpectedConditions.elementToBeClickable(element));
            flag = true;
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }



    public static boolean scrollDownUntilSeenElement(WebDriver oDriver, WebElement element) {
        boolean flag = false;
        JavascriptExecutor js = (JavascriptExecutor) oDriver;
        //This will scroll the page till the element is found
        js.executeScript("arguments[0].scrollIntoView();", element);
        if (MethodsLibrary.waitElementVisible(oDriver, element)) {
            flag = true;
        }
        if (!flag) {
            TestNGBase.reportResult("FAIL", "Could not scroll to element.", false);
            TestNGBase.allureReport("FAIL", "Could not scroll to element.", false);
        }
        return flag;
    }

    public static boolean scrollUpUntilSeenElement(WebDriver oDriver, WebElement element) {
        boolean flag = false;
        JavascriptExecutor js = (JavascriptExecutor) oDriver;
        //This will scroll the page till the element is found
        js.executeScript("window.scrollTo(0, -document.body.scrollHeight);");
        if (MethodsLibrary.waitElementVisible(oDriver, element)) {
            flag = true;
        }
        if (!flag) {
            TestNGBase.reportResult("FAIL", "Could not scroll to element.", false);
            TestNGBase.allureReport("FAIL", "Could not scroll to element.", false);
        }
        return flag;
    }

    public static boolean refreshPage(WebDriver oDriver) {
        boolean flag = false;
        try {
            oDriver.navigate().refresh();
        }catch(Exception e)
        {

        }
        return flag;

    }


    public static boolean waitElementVisible(WebDriver oDriver, WebElement element) {
        boolean flag = false;
        WebDriverWait wait = new WebDriverWait(oDriver, 90);
        try {
            wait.until(ExpectedConditions.visibilityOf(element));
            flag = true;
        } catch (Exception e) {
        }
        return flag;
    }


    public static boolean waitElementVisible(WebDriver oDriver, WebElement element, int timeOutSec) {
        boolean flag;
        oDriver.manage().timeouts().implicitlyWait(timeOutSec, SECONDS);
        try {
            if (element.isDisplayed()) {
                flag = true;
            } else {
                flag = false;
            }
        } catch (Exception exception) {
            flag = false;
        }
        return flag;
    }


    public static boolean mouseHover(WebDriver oDriver, WebElement element) {
        boolean flag = false;
        try {
            Actions actions = new Actions(oDriver);
            //Mouse hover element
            actions.moveToElement(element).perform();
            flag = true;
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    public static void switchToNextTab(WebDriver driver) {
        ArrayList<String> tab = new ArrayList<>(driver.getWindowHandles());
        driver.switchTo().window(tab.get(1));
    }

    public static void closeAndSwitchToNextTab(WebDriver driver) {
        driver.close();
        ArrayList<String> tab = new ArrayList<>(driver.getWindowHandles());
        driver.switchTo().window(tab.get(0));
    }

    public static void webDriverVMSize(WebDriver driver) {
        driver.manage().window().setSize(new Dimension(1900, 1080));
        driver.manage().window().setPosition(new Point(0, 0));

    }

    public static void webDriverReSize(WebDriver driver) {
        driver.manage().window().setSize(new Dimension(1036, 1040));
        driver.manage().window().setPosition(new Point(0, 0));

    }


    public static void webDriverZoomOut(WebDriver driver) {
        ((JavascriptExecutor) driver).executeScript("document.body.style.zoom='70%'");
    }

    public static void webDriverZoomReset(WebDriver driver) {
        ((JavascriptExecutor) driver).executeScript("document.body.style.zoom='100%'");
    }


    public static void switchToPreviousTab(WebDriver driver) {
        ArrayList<String> tab = new ArrayList<>(driver.getWindowHandles());
        driver.switchTo().window(tab.get(0));
    }

    public static void closeTabAndReturn(WebDriver driver) {
        driver.close();
        ArrayList<String> tab = new ArrayList<>(driver.getWindowHandles());
        driver.switchTo().window(tab.get(0));
    }

    public static void switchToPreviousTabAndClose(WebDriver driver) {
        ArrayList<String> tab = new ArrayList<>(driver.getWindowHandles());
        driver.switchTo().window(tab.get(1));
        driver.close();
    }

    public static String prepJson(Object dataClass) {
        // Creating Object of ObjectMapper define in Jackson API
        ObjectMapper Obj = new ObjectMapper();

        try {
            // Converting the Java object into a JSON string
            // Java objects to JSON string - pretty-print
            return Obj.writerWithDefaultPrettyPrinter().writeValueAsString(dataClass);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static <T> T getResponse(String dbResponse, Class<T> dataClass) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(dbResponse, dataClass);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String addZeroLeft(String value) {
        boolean status = false;
        StringBuilder valueBuilder = new StringBuilder(value);
        do {
            if (valueBuilder.length() == 10) {
                status = true;
            } else {
                valueBuilder.insert(0, "0");
            }
        } while (!status);
        value = valueBuilder.toString();
        return value;
    }

    public static long generateRandomNumber(int numberCount) {
        Random rnd = new Random();
        char[] digits = new char[numberCount];
        digits[0] = (char) (rnd.nextInt(9) + '1');
        for (int i = 1; i < digits.length; i++) {
            digits[i] = (char) (rnd.nextInt(10) + '0');
        }
        return Long.parseLong(new String(digits));
    }
}
