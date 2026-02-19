package ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class OrderPlacedPage extends BasePage {

    private final By orderPlacedHeader =
            By.xpath("//*[contains(translate(normalize-space(.),'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ'),'ORDER PLACED')]");

    private final By continueBtn = By.cssSelector("a[data-qa='continue-button']");

    public OrderPlacedPage(WebDriver driver) {
        super(driver);
    }

    public void waitOrderPlacedVisible() {
        waitVisible(orderPlacedHeader);
    }

    public void clickContinue() {
        click(continueBtn);
    }
}
