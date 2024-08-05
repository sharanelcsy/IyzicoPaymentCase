package com.functions.Methods;


import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.Random;

import static com.functions.Methods.WebDriverFactory.driver;


public class Methods {



    // click on element if exists - First validates if element isn't null.
    public void clickOnElementIfExists(By path) throws InterruptedException {
        if (driver.findElements(path).size() != 0) {

            WebElement elementFound = driver.findElement(path);
            elementFound.click();
        }
        Thread.sleep(1100);
    }


    //random generator
    public int generateRandomNumber(int min, int max) {
        Random r = new Random();
        int randomNum = r.nextInt((max - min) + 1) + min;
        return randomNum;
    }



    // enter text to element , after finding WebElement with path.
    // checks if it'snot null
    public void enterTextToElement(String input, By path) {

        if (driver.findElements(path).size() != 0) {
            WebElement findingElement = driver.findElement(path);
            findingElement.sendKeys(input);
        }

    }



    // checks if element exists, then clicks on index element
    // scrolls beforehand.
    public void clickOnElementIfExistsWithIndex(By pathList, int index) throws InterruptedException {

        if (driver.findElements(pathList).size() != 0) {
            List<WebElement> elementList = driver.findElements(pathList);
            scrollToElement(elementList.get(index));
            elementList.get(index).click();
        }
        Thread.sleep(500);
    }


    public static boolean waitTime(int number) throws InterruptedException {
        Thread.sleep(number * 1000L);
        return true;
    }

    public void scrollToElement(WebElement webElement)  {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", webElement);
    }




}

