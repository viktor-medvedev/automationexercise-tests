package ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class PaymentPage extends BasePage {

    private final By paymentHeader = By.xpath("//h2[normalize-space()='Payment']");
    private final By payAndConfirmText = By.xpath("//*[contains(.,'Pay and Confirm Order')]");

    private final By nameOnCard = By.cssSelector("input[data-qa='name-on-card']");
    private final By cardNumber = By.cssSelector("input[data-qa='card-number']");
    private final By cvc = By.cssSelector("input[data-qa='cvc']");
    private final By expiryMonth = By.cssSelector("input[data-qa='expiry-month']");
    private final By expiryYear = By.cssSelector("input[data-qa='expiry-year']");

    private final By payBtn = By.id("submit"); // Pay and Confirm Order

    public PaymentPage(WebDriver driver) {
        super(driver);
    }

    public void waitPaymentVisible() {
        waitVisible(paymentHeader);
        waitVisible(payAndConfirmText);
    }

    public PaymentPage fillCard(String name, String number, String cvcValue, String month, String year) {
        type(nameOnCard, name);
        type(cardNumber, number);
        type(cvc, cvcValue);
        type(expiryMonth, month);
        type(expiryYear, year);
        return this;
    }

    public OrderPlacedPage payAndConfirm() {
        click(payBtn);
        return new OrderPlacedPage(driver);
    }
}
