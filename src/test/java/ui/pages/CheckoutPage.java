package ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

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
        click(placeOrderBtn);
        waitUntilTrue(d -> d.getCurrentUrl().contains("/payment"), 10);
        return new PaymentPage(driver);
    }
}
