package ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class OrderPlacedPage extends BasePage {

    private final By orderPlacedQa = By.cssSelector("[data-qa='order-placed']");

    private final By orderPlacedHeader =
            By.xpath("//*[contains(translate(normalize-space(.),'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ'),'ORDER PLACED')]");

    private final By continueBtn = By.cssSelector("a[data-qa='continue-button']");

    private final By downloadInvoiceBtn = By.cssSelector("a[href*='download_invoice']");

    public OrderPlacedPage(WebDriver driver) {
        super(driver);
    }

    /** Новый “гейт”: считаем страницу открытой, если есть ЛЮБОЙ признак успеха */
    public void waitOpened() {
        waitUntilTrue(d ->
                        d.getCurrentUrl().contains("payment_done")
                                || !d.findElements(orderPlacedQa).isEmpty()
                                || !d.findElements(downloadInvoiceBtn).isEmpty()
                                || !d.findElements(orderPlacedHeader).isEmpty(),
                20
        );

        if (!driver.getCurrentUrl().contains("payment_done")
                && driver.findElements(orderPlacedQa).isEmpty()
                && driver.findElements(downloadInvoiceBtn).isEmpty()
                && driver.findElements(orderPlacedHeader).isEmpty()) {
            throw new AssertionError("Not on OrderPlaced page. URL=" + driver.getCurrentUrl()
                    + " TITLE=" + driver.getTitle());
        }
    }

    public void waitOrderPlacedVisible() {
        waitOpened();
    }

    public void clickContinue() {
        click(continueBtn);
    }

    public InvoicePage openInvoiceSection() {
        click(downloadInvoiceBtn);          // <-- ВАЖНО: клик, а не просто wait
        return new InvoicePage(driver);
    }
}