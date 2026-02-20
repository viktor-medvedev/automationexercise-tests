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

    // =========================
    // Navigation
    // =========================
    public ProductsPage open(String baseUrl) {
        driver.get(baseUrl + "/products");
        waitUntilTrue(d -> d.getCurrentUrl().contains("/products"), 10);
        return this;
    }

    public void openCart() {
        click(cartLink);
        waitUntilTrue(d -> d.getCurrentUrl().contains("/view_cart"), 10);
    }

    // =========================
    // Waits / asserts
    // =========================
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

    // =========================
    // Actions
    // =========================
    public void openFirstProduct() {
        WebElement link = waitVisible(firstViewProduct);
        String href = link.getAttribute("href");

        try {
            if (href != null && !href.isBlank()) {
                // делаем абсолютный url, даже если href относительный
                String abs = new URL(new URL(driver.getCurrentUrl()), href).toString();
                driver.get(abs);
            } else {
                // fallback если href вдруг пустой
                jsClick(firstViewProduct);
            }
        } catch (Exception e) {
            jsClick(firstViewProduct);
        }

        // гарантируем, что реально ушли на detail
        waitUntilTrue(d -> d.getCurrentUrl().contains("/product_details"), 10);
    }

    public ProductsPage search(String query) {
        type(searchInput, query);
        click(searchButton);
        return this;
    }

    // TC12
    public ProductsPage addFirstProductToCartAndContinue() {
        addProductToCartAndContinue(addProduct1);
        return this;
    }

    public ProductsPage addSecondProductToCartAndContinue() {
        addProductToCartAndContinue(addProduct2);
        return this;
    }

    // Если нужно открыть корзину через модалку (TC12 не советую, но метод оставим)
    public void openCartFromModalAfterAddingFirst() {
        clickAddToCart(resolveAddButton(addProduct1, 1));
        waitVisible(viewCartLink);
        click(viewCartLink);
        waitUntilTrue(d -> d.getCurrentUrl().contains("/view_cart"), 10);
    }

    // =========================
    // Internal helpers
    // =========================
    private void addProductToCartAndContinue(By addBtn) {
        clickAddToCart(addBtn);

        // ждём, что появится либо модалка, либо ссылка View Cart (обычно обе)
        waitUntilTrue(d ->
                        !d.findElements(modalDialog).isEmpty()
                                || !d.findElements(viewCartLink).isEmpty(),
                5
        );

        // если появилось всплывающее окно — закрываем его (это и есть "Continue Shopping")
        if (!driver.findElements(closeModalBtn).isEmpty()) {
            jsClick(closeModalBtn);
            waitUntilTrue(d -> d.findElements(modalDialog).isEmpty(), 10);
            waitUntilTrue(d -> d.findElements(modalBackdrop).isEmpty(), 10);
        }

        // возвращаемся наверх, чтобы были видны кнопки меню
        scrollToTopPage();
    }


    /**
     * Пытаемся кликать по стабильному селектору data-product-id.
     * Если он вдруг не найден (или найден, но скрыт), берём N-ю видимую кнопку add-to-cart.
     */
    private By resolveAddButton(By preferred, int fallbackIndex1Based) {
        try {
            // Если preferred присутствует и отображается — используем его
            WebElement el = driver.findElement(preferred);
            if (el.isDisplayed()) return preferred;
        } catch (Exception ignored) {}

        // fallback: ищем N-ю видимую add-to-cart
        List<WebElement> all = driver.findElements(addToCartVisibleButtons);
        int seen = 0;
        for (WebElement el : all) {
            try {
                if (!el.isDisplayed()) continue;
                seen++;
                if (seen == fallbackIndex1Based) {
                    String outer = el.getAttribute("outerHTML");
                    // возвращаем xpath по data-product-id если есть, иначе кликаем через JS по найденному WebElement ниже
                    String pid = el.getAttribute("data-product-id");
                    if (pid != null && !pid.isBlank()) {
                        return By.cssSelector("a.add-to-cart[data-product-id='" + pid + "']");
                    }
                    // если data-product-id нет, вернём общий локатор и кликнем через JS по первому видимому
                    return addToCartVisibleButtons;
                }
            } catch (Exception ignored) {}
        }

        // если вообще ничего не нашли — вернём preferred (пусть упадёт информативно)
        return preferred;
    }

    private void clickAddToCart(By addToCartBtn) {
        // дождаться, что элементы Add to cart вообще есть
        waitUntilTrue(d -> !d.findElements(addToCartBtn).isEmpty(), 10);

        // взять ПЕРВЫЙ ВИДИМЫЙ элемент по locator
        WebElement el = driver.findElements(addToCartBtn).stream()
                .filter(WebElement::isDisplayed)
                .findFirst()
                .orElse(driver.findElements(addToCartBtn).get(0));

        // scroll
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block:'center', inline:'nearest'});", el
        );

        // hover (на всякий случай)
        try { new Actions(driver).moveToElement(el).perform(); } catch (Exception ignored) {}

        // обычный клик -> если не вышло, JS click
        try {
            el.click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
        }
    }

    private void waitModalToClose() {
        // ждём пока исчезнет backdrop (если появился)
        waitUntilTrue(d -> d.findElements(modalBackdrop).isEmpty(), 10);
        // и на всякий случай модалку
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

        // на всякий случай делаем абсолютный URL
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
        // если всплывающего окна нет — ничего не делаем
        if (driver.findElements(modalDialog).isEmpty()) return;

        // если есть кнопка закрыть — нажимаем
        if (!driver.findElements(closeModalBtn).isEmpty()) {
            jsClick(closeModalBtn);
        }

        // ждём что всплывашка и затемнение исчезнут
        waitUntilTrue(d -> d.findElements(modalDialog).isEmpty(), 10);
        waitUntilTrue(d -> d.findElements(modalBackdrop).isEmpty(), 10);
    }

}
