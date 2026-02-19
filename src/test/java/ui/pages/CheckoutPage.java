package ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CheckoutPage extends BasePage {

    private final By addressDetailsHeader =
            By.xpath("//*[contains(translate(normalize-space(.),'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ'),'ADDRESS DETAILS')]");

    private final By reviewYourOrderHeader =
            By.xpath("//*[contains(translate(normalize-space(.),'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ'),'REVIEW YOUR ORDER')]");

    public CheckoutPage(WebDriver driver) {
        super(driver);
    }

    public void waitCheckoutVisible() {
        waitVisible(addressDetailsHeader);
        waitVisible(reviewYourOrderHeader);
    }
}
