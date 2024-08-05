package com.page;


import org.openqa.selenium.By;

import com.functions.Methods.Methods;

import static com.functions.Methods.Methods.waitTime;


public class ProductPage extends HomePage {

    Methods methods = new Methods();
    By addToBasket = By.xpath("//button[@name='add-to-cart']");
    By paymentPageIcon = By.cssSelector(".button.checkout.wc-forward");
    By showBasket = By.cssSelector("main .wc-forward");
    By goToPaymentPage = By.xpath("//*[@class='checkout-button button alt wc-forward']");

    public ProductPage() {
    }


    // clicks on the element according to parameter given in feature file.

    public  boolean clickOnElement(String element) throws InterruptedException {
        if(element.equalsIgnoreCase("Sepete Ekle"))
        {

            methods.clickOnElementIfExists(addToBasket);
            waitTime(2);
            return true;
        }
        else if(element.equalsIgnoreCase(("Ödeme Sayfası")))
        {
            methods.clickOnElementIfExists(paymentPageIcon);
            waitTime(1);
            methods.clickOnElementIfExists(goToPaymentPage);
            waitTime(2);
            return true;
        }
        if(element.equalsIgnoreCase("Sepeti Görüntüle"))
        {
            methods.clickOnElementIfExists(showBasket);
            waitTime(2);
            return true;
        }
        if(element.equalsIgnoreCase("Payment"))
        {
            methods.clickOnElementIfExists(goToPaymentPage);
            waitTime(3);
            return true;
        }
        return false;
    }

    public boolean clickOnAddAndShowTheBasket() throws InterruptedException {
        clickOnElement("Sepete Ekle");
        clickOnElement("Sepeti Görüntüle");
        return true;
    }
}