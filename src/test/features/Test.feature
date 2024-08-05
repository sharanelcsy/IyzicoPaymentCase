Feature: Make payment on iyzico


  @Test
  Scenario: Do payment on iyzico
    Then The user opens "https://www.iyzico.com/demo"
    Then Click on product number 2.
    Then Add product to basket and show the basket.
    Then Go to "payment" page.
    Then Choose debit or credit card option and pay.
    Then Fill the required credit card information.
    Then Click on pay button and verify SMS code.
    Then Verify if "Sipariş Alındı" message is displayed.
    Then Close the page.
