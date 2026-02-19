package ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CartPage extends BasePage {

    private final By cartLink = By.xpath("//a[contains(.,'Cart')]");
    private final By cartTable = By.id("cart_info_table");
    private final By cartRows = By.cssSelector("#cart_info_table tbody tr[id^='product-']");
    private final By firstItemQty = By.cssSelector("#cart_info_table tbody tr:first-child .cart_quantity button");
    private final By subscriptionHeader = By.xpath("//h2[normalize-space()='Subscription']");
    private final By subscribeEmail = By.id("susbscribe_email");
    private final By subscribeBtn = By.id("subscribe");
    private final By subscribeSuccess = By.cssSelector(".alert-success");
    private final By cartItemsSection = By.cssSelector("section#cart_items");
    private final By proceedToCheckoutBtn = By.xpath("//a[contains(.,'Proceed To Checkout')]");
    private final By firstDeleteBtn =
            By.cssSelector("#cart_info_table tbody tr[id^='product-']:first-child a.cart_quantity_delete");


    private final By emptyCartText =
            By.xpath("//*[contains(.,'Cart is empty')]");



    public CartPage(WebDriver driver) {
        super(driver);
    }

    public CartPage open(String baseUrl) {
        driver.get(baseUrl + "/view_cart");
        waitUntilTrue(d -> d.getCurrentUrl().contains("/view_cart"), 10);
        return this;
    }

    public void waitCartVisible() {
        waitVisible(cartItemsSection);
    }

    public int getItemsCount() {
        return driver.findElements(cartRows).size();
    }

    public int getFirstItemQuantity() {
        String text = waitVisible(firstItemQty).getText().trim();
        return Integer.parseInt(text);
    }

    public void scrollToSubscription() {
        scrollToBottom();
    }

    public void waitSubscriptionVisible() {
        waitVisible(subscriptionHeader);
    }

    public void subscribe(String email) {
        type(subscribeEmail, email);
        click(subscribeBtn);
    }

    public void waitSubscriptionSuccess() {
        waitVisible(subscribeSuccess);
    }

    public CheckoutPage proceedToCheckout() {
        click(proceedToCheckoutBtn);
        waitUntilTrue(d -> d.getCurrentUrl().contains("/checkout"), 10);
        return new CheckoutPage(driver);
    }

    public void removeFirstItem() {
        scrollIntoView(firstDeleteBtn);
        click(firstDeleteBtn);
    }

    public void waitCartEmpty() {
        waitUntilTrue(d -> d.findElements(cartRows).isEmpty(), 10);
    }


}
