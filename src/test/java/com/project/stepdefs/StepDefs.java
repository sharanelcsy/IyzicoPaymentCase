package com.project.stepdefs;


import com.functions.Methods.WebDriverFactory;
import com.page.HomePage;
import com.page.PaymentStepPage;
import com.page.ProductPage;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;

import static com.functions.Methods.Methods.waitTime;
import static com.functions.Methods.WebDriverFactory.driver;

public class StepDefs {




    WebDriverFactory webFactory = new WebDriverFactory();
    private final HomePage homePage = new HomePage();
    private final ProductPage productPage = new ProductPage();
    private final PaymentStepPage paymentStepPage = new PaymentStepPage();

    @When("The user opens {string}")
    public void usersOpenSite(String siteName) throws InterruptedException {
        webFactory.initializeWebDriver();
        driver.get(siteName);
        waitTime(5);
    }

    @Then("Click on product number {int}.")
    public void clickOn(int index) throws InterruptedException {
        Assert.assertTrue(homePage.clickOnProductIfExists(index));
    }

    @Then("Add product to basket and show the basket.")
    public void addProductToBasketAndShowTheBasket() throws InterruptedException {
        Assert.assertTrue(productPage.clickOnAddAndShowTheBasket());
    }

    @Then("Go to {string} page.")
    public void goToPaymentPage(String site) throws InterruptedException {
        Assert.assertTrue(productPage.clickOnElement(site));
    }

    @Then("Choose debit or credit card option and pay.")
    public void chooseDebitOrCreditCardOptionAndPay() throws InterruptedException {
        Assert.assertTrue(paymentStepPage.chooseDebitOrCreditCardOptionAndPay());
    }

    @Then("Fill the required credit card information.")
    public void fillTheRequiredCreditCardInformation() throws InterruptedException {
        Assert.assertTrue(paymentStepPage.fillTheRequiredCreditCardInformation());
    }

    @Then("Click on pay button and verify SMS code {int}")
    public void clickOnPayButtonAndVerifySMSCode(int sms) throws InterruptedException {
        Assert.assertTrue(paymentStepPage.clickOnPayButtonAndVerifySMSCode(sms));
    }

    @Then("Verify if {string} message is displayed.")
    public void verifyIfMessageIsDisplayed(String message) {
        Assert.assertTrue(paymentStepPage.verifyIfMessageIsDisplayed(message));

    }

    @Then("Close the page.")
    public void closeThePage() {
        driver.quit();
    }



}
