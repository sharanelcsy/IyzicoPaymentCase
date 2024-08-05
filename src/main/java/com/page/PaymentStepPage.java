package com.page;


import org.openqa.selenium.By;

import com.functions.Methods.Methods;

import static com.functions.Methods.WebDriverFactory.driver;


public class PaymentStepPage extends HomePage {

    Methods methods = new Methods();

    public String creditCardNumber = "5890040000000016";
    public String nameAndSurname = "Test Tester";
    public int monthDate1;
    public int monthDate2;
    public int yearDate;
    public int yearDate1;
    public int yearDate2;
    public int cvCode;

    By paymentMethodsList = By.xpath("//*[@name=\"payment_method\"]");

    By submitOrder = By.xpath("/html//button[@id='place_order']");

    By doPaymentButton = By.xpath("//button[@id='iyz-payment-button']");

    By placeOrder = By.xpath("/html//button[@id='place_order']");

    By smsCodeArea = By.xpath("//input[@id='smsCode']");

    By orderText = By.xpath("//*[@class='page-title']");

    By cardholderName = By.xpath("//input[@name='cardHolderName']");

    By cardNumber = By.xpath("//input[@name='cardNumber']");
    By expireDate = By.xpath("//input[@name='expireDate']");
    By cvc = By.xpath("//input[@name='cvc']");
    By submitPayment = By.xpath("//*[@id='submitBtn']");


    public PaymentStepPage() {

    }



    // generates credit card information
    // enters monthDates digit by digit, if first digit is 1, second digit can't be higher than 2 (12)
    // yearDate digits can be between 2-9 (1st) and 5,9 ( to avoid 2024)
    // cvc code should be > 99 and < 1000
    public void generateLastUsageDate() {
        monthDate1 = methods.generateRandomNumber(0, 1);
        if (monthDate1 == 1) {
            monthDate2 = methods.generateRandomNumber(0, 2);
        }
        else {
            monthDate2 = methods.generateRandomNumber(1, 9);
        }
        yearDate = methods.generateRandomNumber(25, 35);
        yearDate1 = 2;
        yearDate2 = methods.generateRandomNumber(5, 9);
        cvCode = methods.generateRandomNumber(100, 999);
    }

    public void enterCreditCardInfo() throws InterruptedException {
        methods.waitTime(2);
        generateLastUsageDate();
        methods.enterTextToElement(nameAndSurname, cardholderName);
        methods.waitTime(1);
        methods.enterTextToElement(creditCardNumber, cardNumber);
        methods.enterTextToElement(String.valueOf(monthDate1), expireDate);
        methods.enterTextToElement(String.valueOf(monthDate2), expireDate);
        methods.enterTextToElement(String.valueOf(yearDate1), expireDate);
        methods.enterTextToElement(String.valueOf(yearDate2), expireDate);
        methods.enterTextToElement(String.valueOf(cvCode), cvc);
        methods.waitTime(1);
    }



    // validating the message and returns true if its correct.
    public boolean verifyOrderCompleted(String message) {
        return driver.findElement(orderText).getText().equalsIgnoreCase(message);
    }


    // chooses payment option: 2nd option is Debit/Credit card
    // clicks on place order
    public boolean chooseDebitOrCreditCardOptionAndPay() throws InterruptedException {
        methods.clickOnElementIfExistsWithIndex(paymentMethodsList, 1);
        methods.clickOnElementIfExists(placeOrder);
        methods.waitTime(2);
        return true;

    }

    public boolean fillTheRequiredCreditCardInformation() throws InterruptedException {
        enterCreditCardInfo();
        return true;
    }


    // clicks on Pay Button and verifies SMS
    public boolean clickOnPayButtonAndVerifySMSCode() throws InterruptedException {
        methods.clickOnElementIfExists(doPaymentButton);
        methods.enterTextToElement("283126", smsCodeArea);
        methods.waitTime(1);
        methods.clickOnElementIfExists(submitPayment);
        methods.waitTime(5);
        return true;
    }


    // verify the "Sipariş Alındı" message.
    public boolean verifyIfMessageIsDisplayed(String message) {
        return verifyOrderCompleted(message);
    }
}