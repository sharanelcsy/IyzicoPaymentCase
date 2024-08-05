package com.page;


import com.functions.Methods.Methods;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;


import java.util.List;

import static com.functions.Methods.Methods.waitTime;
import static com.functions.Methods.WebDriverFactory.driver;



public class HomePage {

    Methods methods = new Methods();

    By productList = By.xpath("//div[@class='product_thumbnail_wrap']");

    public HomePage() {

    }


    public boolean clickOnProductIfExists(int element) throws InterruptedException {

            if (driver.findElements(productList).size() != 0) {
                List<WebElement> popUpExistsClose = driver.findElements(productList);
                popUpExistsClose.get(element).click();
                waitTime(2);
            }
         return true;
        }


    }
