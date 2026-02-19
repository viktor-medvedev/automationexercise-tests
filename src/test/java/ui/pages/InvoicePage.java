package ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class InvoicePage extends BasePage {

    private final By downloadInvoiceBtn = By.xpath("//a[contains(.,'Download Invoice')]");
    private final By continueBtn = By.cssSelector("a[data-qa='continue-button']");

    public InvoicePage(WebDriver driver) {
        super(driver);
    }

    public void waitInvoiceButtonsVisible() {
        waitVisible(downloadInvoiceBtn);
        waitVisible(continueBtn);
    }

    public void downloadInvoice() {
        click(downloadInvoiceBtn);
    }
}
