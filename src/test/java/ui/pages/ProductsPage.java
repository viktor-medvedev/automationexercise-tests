package ui.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;

import java.util.List;

public class ProductsPage extends BasePage {

    // =========================
    // Locators: page anchors
    // =========================
    private final By productsList = By.cssSelector(".features_items");
    private final By anyViewProduct = By.xpath("//a[contains(.,'View Product')]");
    private final By firstViewProduct = By.xpath("(//a[contains(.,'View Product')])[1]");

    // Product detail page
    private final By productInfoBlock = By.cssSelector(".product-information");
    private final By productName = By.cssSelector(".product-information h2");

    // Search
    private final By searchInput = By.id("search_product");
    private final By searchButton = By.id("submit_search");
    private final By searchedProductsHeader = By.xpath(
            "//*[contains(translate(normalize-space(.),'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ'),'SEARCHED PRODUCTS')]"
    );

    // =========================
    // Locators: cart / modal
    // =========================
    private final By cartLink = By.xpath("//a[contains(.,'Cart')]");
    private final By continueShoppingBtn = By.xpath("//button[contains(.,'Continue Shopping')]");
    private final By viewCartLink = By.xpath("//a[contains(.,'View Cart')]");

    // Modal/backdrop часто мешают клику / второму добавлению
    private final By modalBackdrop = By.cssSelector(".modal-backdrop");
    private final By modalDialog = By.cssSelector(".modal-content");

    // =========================
    // Locators: add to cart (stable)
    // =========================
    // Самый стабильный путь на automationexercise — по data-product-id
    private final By addProduct1 = By.cssSelector("a.add-to-cart[data-product-id='1']");
    private final By addProduct2 = By.cssSelector("a.add-to-cart[data-product-id='2']");

    // Fallback: все видимые add-to-cart на странице (иногда в DOM много скрытых)
    private final By addToCartVisibleButtons = By.cssSelector("a.add-to-cart");

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
        click(firstViewProduct);
    }

    public ProductsPage search(String query) {
        type(searchInput, query);
        click(searchButton);
        return this;
    }

    // TC12
    public ProductsPage addFirstProductToCartAndContinue() {
        addProductToCartAndContinue(addProduct1, 1);
        return this;
    }

    public ProductsPage addSecondProductToCartAndContinue() {
        addProductToCartAndContinue(addProduct2, 2);
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
    private void addProductToCartAndContinue(By preferredAddBtn, int fallbackIndex1Based) {
        By target = resolveAddButton(preferredAddBtn, fallbackIndex1Based);

        clickAddToCart(target);

        // Ждём модалку и кнопку
        waitVisible(continueShoppingBtn);
        click(continueShoppingBtn);

        // Важно: дождаться, что оверлей/модалка ушли, иначе следующий add-to-cart не сработает
        waitModalToClose();
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
        // 1) прокрутить
        scrollIntoView(addToCartBtn);

        // 2) hover (часто нужно)
        try {
            WebElement el = waitPresent(addToCartBtn);
            new Actions(driver).moveToElement(el).perform();
        } catch (Exception ignored) {}

        // 3) обычный клик
        try {
            click(addToCartBtn);
            return;
        } catch (Exception ignored) {}

        // 4) JS fallback
        WebElement el = waitPresent(addToCartBtn);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
    }

    private void waitModalToClose() {
        // ждём пока исчезнет backdrop (если появился)
        waitUntilTrue(d -> d.findElements(modalBackdrop).isEmpty(), 10);
        // и на всякий случай модалку
        waitUntilTrue(d -> d.findElements(modalDialog).isEmpty(), 10);
    }
}
