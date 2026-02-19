package ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class PaymentPage extends BasePage {

    private final By paymentHeader = By.xpath("//h2[normalize-space()='Payment']");
    private final By payAndConfirmBtn = By.id("submit"); // fallback если понадобится
    private final By payAndConfirmText = By.xpath("//*[contains(.,'Pay and Confirm Order')]");

    public PaymentPage(WebDriver driver) {
        super(driver);
    }

    public void waitPaymentVisible() {
        waitVisible(paymentHeader);
        // на всякий случай проверим, что есть кнопка/текст оплаты
        waitVisible(payAndConfirmText);
    }
}
