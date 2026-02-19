package ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class PaymentPage extends BasePage {

    // более мягкий заголовок (если есть)
    private final By paymentHeaderAny =
            By.xpath("//*[self::h2 or self::h3][contains(translate(normalize-space(.),'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ'),'PAYMENT')]");

    private final By payAndConfirmText =
            By.xpath("//*[contains(translate(normalize-space(.),'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ'),'PAY AND CONFIRM')]");

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
        // 1) убеждаемся, что мы реально на payment URL
        waitUntilTrue(d -> d.getCurrentUrl().contains("/payment"), 10);

        // 2) ждём любой якорь страницы оплаты (заголовок или поля/кнопку)
        waitUntilTrue(d ->
                        !d.findElements(paymentHeaderAny).isEmpty()
                                || !d.findElements(payAndConfirmText).isEmpty()
                                || !d.findElements(nameOnCard).isEmpty()
                                || !d.findElements(payBtn).isEmpty(),
                20
        );
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
