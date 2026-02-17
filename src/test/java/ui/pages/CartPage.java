package ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CartPage extends BasePage {

    private final By cartLink = By.xpath("//a[contains(.,'Cart')]");
    private final By cartTable = By.id("cart_info_table");
    private final By cartRows = By.cssSelector("#cart_info_table tbody tr");
    private final By firstItemQty = By.cssSelector("#cart_info_table tbody tr:first-child .cart_quantity button");


    public CartPage(WebDriver driver) {
        super(driver);
    }

    public CartPage open(String baseUrl) {
        driver.get(baseUrl + "/");
        click(cartLink);
        waitUntilTrue(d -> d.getCurrentUrl().contains("/view_cart"), 10);
        return this;
    }

    public void waitCartVisible() {
        waitVisible(cartTable);
    }

    public int getItemsCount() {
        return driver.findElements(cartRows).size();
    }

    public int getFirstItemQuantity() {
        String text = waitVisible(firstItemQty).getText().trim();
        return Integer.parseInt(text);
    }

}
