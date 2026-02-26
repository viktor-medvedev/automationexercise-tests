package ui.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;

import java.util.List;
import java.net.URL;
import org.openqa.selenium.WebElement;

public class ProductsPage extends BasePage {

    private final By productsList = By.cssSelector(".features_items");
    private final By anyViewProduct = By.xpath("//a[contains(.,'View Product')]");
    private final By firstViewProduct = By.xpath("(//a[contains(.,'View Product')])[1]");

    private final By productInfoBlock = By.cssSelector(".product-information");
    private final By productName = By.cssSelector(".product-information h2");

    private final By searchInput = By.id("search_product");
    private final By searchButton = By.id("submit_search");
    private final By searchedProductsHeader = By.xpath(
            "//*[contains(translate(normalize-space(.),'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ'),'SEARCHED PRODUCTS')]"
    );

    private final By cartLink = By.xpath("//a[contains(.,'Cart')]");
    private final By continueShoppingBtn = By.xpath("//button[contains(.,'Continue Shopping')]");
    private final By viewCartLink = By.xpath("//a[contains(.,'View Cart')]");

    private final By modalBackdrop = By.cssSelector(".modal-backdrop");
    private final By modalDialog = By.cssSelector(".modal-content");

    private final By addProduct1 = By.cssSelector("a.add-to-cart[data-product-id='1']");
    private final By addProduct2 = By.cssSelector("a.add-to-cart[data-product-id='2']");

    private final By addToCartVisibleButtons = By.cssSelector("a.add-to-cart");

    private final By brandsBlock = By.cssSelector(".brands_products");
    private final By listingTitle = By.cssSelector("h2.title.text-center");

    private final By writeYourReviewHeader =
            By.xpath("//*[contains(translate(normalize-space(.),'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ'),'WRITE YOUR REVIEW')]");

    private final By reviewName = By.id("name");
    private final By reviewEmail = By.id("email");
    private final By reviewText = By.id("review");
    private final By submitReviewBtn = By.id("button-review");
    private final By reviewSuccess =
            By.xpath("//*[contains(.,'Thank you for your review')]");
    private final By closeModalBtn = By.cssSelector("button.close-modal");

    public ProductsPage(WebDriver driver) {
        super(driver);
    }

    public ProductsPage open(String baseUrl) {
        driver.get(baseUrl + "/products");
        waitUntilTrue(d -> d.getCurrentUrl().contains("/products"), 10);
        return this;
    }

    public void openCart() {
        click(cartLink);
        waitUntilTrue(d -> d.getCurrentUrl().contains("/view_cart"), 10);
    }

    public void waitAllProductsVisible() {
        waitPresent(productsList);
        waitPresent(anyViewProduct);
    }

    public void waitProductDetailVisible() {
        waitVisible(productInfoBlock);
        waitVisible(productName);
    }

    public boolean isProductNameVisible() {
        return isPresent(productName);
    }

    public void waitSearchedProductsVisible() {
        waitVisible(searchedProductsHeader);
        waitPresent(anyViewProduct);
    }

    public boolean hasAnyResults() {
        return isPresent(anyViewProduct);
    }

    public void openFirstProduct() {
        WebElement link = waitVisible(firstViewProduct);
        String href = link.getAttribute("href");

        try {
            if (href != null && !href.isBlank()) {

                String abs = new URL(new URL(driver.getCurrentUrl()), href).toString();
                driver.get(abs);
            } else {

                jsClick(firstViewProduct);
            }
        } catch (Exception e) {
            jsClick(firstViewProduct);
        }

        waitUntilTrue(d -> d.getCurrentUrl().contains("/product_details"), 10);
    }

    public ProductsPage search(String query) {
        type(searchInput, query);
        click(searchButton);
        return this;
    }

    public ProductsPage addFirstProductToCartAndContinue() {
        addProductToCartAndContinue(addProduct1);
        return this;
    }

    public ProductsPage addSecondProductToCartAndContinue() {
        addProductToCartAndContinue(addProduct2);
        return this;
    }

    public void openCartFromModalAfterAddingFirst() {
        clickAddToCart(resolveAddButton(addProduct1, 1));
        waitVisible(viewCartLink);
        click(viewCartLink);
        waitUntilTrue(d -> d.getCurrentUrl().contains("/view_cart"), 10);
    }

    private void addProductToCartAndContinue(By addBtn) {
        clickAddToCart(addBtn);

        waitUntilTrue(d ->
                        !d.findElements(modalDialog).isEmpty()
                                || !d.findElements(viewCartLink).isEmpty(),
                5
        );

        if (!driver.findElements(closeModalBtn).isEmpty()) {
            jsClick(closeModalBtn);
            waitUntilTrue(d -> d.findElements(modalDialog).isEmpty(), 10);
            waitUntilTrue(d -> d.findElements(modalBackdrop).isEmpty(), 10);
        }

        scrollToTopPage();
    }

    /**
     * Пытаемся кликать по стабильному селектору data-product-id.
     * Если он вдруг не найден (или найден, но скрыт), берём N-ю видимую кнопку add-to-cart.
     */
    private By resolveAddButton(By preferred, int fallbackIndex1Based) {
        try {

            WebElement el = driver.findElement(preferred);
            if (el.isDisplayed()) return preferred;
        } catch (Exception ignored) {}

        List<WebElement> all = driver.findElements(addToCartVisibleButtons);
        int seen = 0;
        for (WebElement el : all) {
            try {
                if (!el.isDisplayed()) continue;
                seen++;
                if (seen == fallbackIndex1Based) {
                    String outer = el.getAttribute("outerHTML");

                    String pid = el.getAttribute("data-product-id");
                    if (pid != null && !pid.isBlank()) {
                        return By.cssSelector("a.add-to-cart[data-product-id='" + pid + "']");
                    }

                    return addToCartVisibleButtons;
                }
            } catch (Exception ignored) {}
        }

        return preferred;
    }

    private void clickAddToCart(By addToCartBtn) {

        waitUntilTrue(d -> !d.findElements(addToCartBtn).isEmpty(), 10);

        WebElement el = driver.findElements(addToCartBtn).stream()
                .filter(WebElement::isDisplayed)
                .findFirst()
                .orElse(driver.findElements(addToCartBtn).get(0));

        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block:'center', inline:'nearest'});", el
        );

        try { new Actions(driver).moveToElement(el).perform(); } catch (Exception ignored) {}

        try {
            el.click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
        }
    }

    private void waitModalToClose() {

        waitUntilTrue(d -> d.findElements(modalBackdrop).isEmpty(), 10);

        waitUntilTrue(d -> d.findElements(modalDialog).isEmpty(), 10);
    }

    public void waitBrandsVisible() {
        waitVisible(brandsBlock);
    }

    private By brandLink(String brandName) {
        String up = brandName.toUpperCase();
        return By.xpath("//div[contains(@class,'brands-name')]//a[" +
                "contains(translate(normalize-space(.),'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ'),'" + up + "')]");
    }

    public void openBrand(String brandName) {
        waitBrandsVisible();

        WebElement link = waitVisible(brandLink(brandName));
        String href = link.getAttribute("href");

        try {
            String abs = new URL(new URL(driver.getCurrentUrl()), href).toString();
            driver.get(abs);
        } catch (Exception e) {
            driver.get(href);
        }

        waitUntilTrue(d -> d.getCurrentUrl().contains("/brand_products/"), 10);
    }

    public String getListingTitle() {
        return waitVisible(listingTitle).getText().trim();
    }

    public void waitReviewBlockVisible() {
        waitVisible(writeYourReviewHeader);
        waitVisible(reviewName);
        waitVisible(reviewEmail);
        waitVisible(reviewText);
    }

    public void submitReview(String name, String email, String text) {
        type(reviewName, name);
        type(reviewEmail, email);
        type(reviewText, text);
        click(submitReviewBtn);
    }

    public void waitReviewSuccess() {
        waitVisible(reviewSuccess);
    }

    private void closeAddToCartPopupIfAppears() {

        if (driver.findElements(modalDialog).isEmpty()) return;

        if (!driver.findElements(closeModalBtn).isEmpty()) {
            jsClick(closeModalBtn);
        }

        waitUntilTrue(d -> d.findElements(modalDialog).isEmpty(), 10);
        waitUntilTrue(d -> d.findElements(modalBackdrop).isEmpty(), 10);
    }

}
