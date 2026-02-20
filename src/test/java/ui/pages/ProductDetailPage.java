package ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ProductDetailPage extends BasePage {

    private final By quantityInput = By.id("quantity");
    private final By addToCartBtn = By.cssSelector("button.cart");
    private final By viewCartLink = By.xpath("//a[contains(.,'View Cart')]");
    private final By productInfoBlock = By.cssSelector(".product-information");

    public ProductDetailPage(WebDriver driver) {
        super(driver);
    }

    public ProductDetailPage setQuantity(String qty) {
        waitUntilTrue(d -> d.getCurrentUrl().contains("/product_details"), 10);
        waitVisible(productInfoBlock);
        type(quantityInput, qty);
        return this;
    }

    public void addToCartAndOpenCart() {
        click(addToCartBtn);
        waitVisible(viewCartLink);
        click(viewCartLink);
    }


}
