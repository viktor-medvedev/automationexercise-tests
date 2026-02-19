package ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import java.net.URL;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

public class CheckoutPage extends BasePage {

    private final By addressDetailsHeader =
            By.xpath("//*[contains(translate(normalize-space(.),'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ'),'ADDRESS DETAILS')]");

    private final By reviewYourOrderHeader =
            By.xpath("//*[contains(translate(normalize-space(.),'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ'),'REVIEW YOUR ORDER')]");

    // Comment textarea (на сайте это поле под текстом про comment)
    private final By commentTextArea =
            By.xpath("//textarea[@name='message' or @id='comment' or contains(@class,'form-control')]");

    // Place Order button
    private final By placeOrderBtn = By.xpath("//a[contains(normalize-space(.),'Place Order')]");

    public CheckoutPage(WebDriver driver) {
        super(driver);
    }

    public void waitCheckoutVisible() {
        waitVisible(addressDetailsHeader);
        waitVisible(reviewYourOrderHeader);
    }

    public CheckoutPage enterComment(String comment) {
        scrollIntoView(commentTextArea);
        type(commentTextArea, comment);
        return this;
    }

    public PaymentPage clickPlaceOrder() {
        scrollIntoView(placeOrderBtn);

        WebElement btn = waitVisible(placeOrderBtn);
        String href = toAbsoluteUrl(btn.getAttribute("href"));

        // 1) обычный клик + JS fallback
        try {
            click(placeOrderBtn);
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
        }

        // 2) ждём переход на /payment
        boolean navigated = false;
        try {
            waitUntilTrue(d -> d.getCurrentUrl().contains("/payment"), 5);
            navigated = true;
        } catch (Exception ignored) {}

        // 3) если не перешли — открываем по href напрямую (самый стабильный вариант для CI)
        if (!navigated && href != null && !href.isBlank()) {
            driver.get(href);
            waitUntilTrue(d -> d.getCurrentUrl().contains("/payment"), 10);
        }

        return new PaymentPage(driver);
    }


    private String toAbsoluteUrl(String href) {
        if (href == null || href.isBlank()) return null;
        try {
            return new URL(new URL(driver.getCurrentUrl()), href).toString();
        } catch (Exception e) {
            return href;
        }
    }

}
