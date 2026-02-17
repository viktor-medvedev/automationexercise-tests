package ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ProductDetailPage extends BasePage {

    private final By quantityInput = By.id("quantity");
    private final By addToCartBtn = By.cssSelector("button.cart");
    private final By viewCartLink = By.xpath("//a[contains(.,'View Cart')]");

    public ProductDetailPage(WebDriver driver) {
        super(driver);
    }

    public ProductDetailPage setQuantity(String qty) {
        type(quantityInput, qty);
        return this;
    }

    public void addToCartAndOpenCart() {
        click(addToCartBtn);
        waitVisible(viewCartLink);
        click(viewCartLink);
    }
}
