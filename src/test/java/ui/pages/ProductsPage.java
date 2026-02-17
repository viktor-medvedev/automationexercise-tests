package ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ProductsPage extends BasePage {

    private final By productsLink = By.xpath("//a[contains(.,'Products')]");
    // Контейнер со списком товаров
    private final By productsList = By.cssSelector(".features_items");

    // На странице обычно есть ссылки View Product
    private final By anyViewProduct = By.xpath("//a[contains(.,'View Product')]");

    // Первый продукт: "View Product"
    private final By firstViewProduct = By.xpath("(//a[contains(.,'View Product')])[1]");

    // Product detail page
    private final By productInfoBlock = By.cssSelector(".product-information");
    private final By productName = By.cssSelector(".product-information h2");

    private final By searchInput = By.id("search_product");
    private final By searchButton = By.id("submit_search");

    private final By searchedProductsHeader =
            By.xpath("//*[contains(translate(normalize-space(.),'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ'),'SEARCHED PRODUCTS')]");

    private final By firstAddToCart = By.xpath("(//a[contains(@class,'add-to-cart')])[1]");
    private final By continueShoppingBtn = By.xpath("//button[contains(.,'Continue Shopping')]");
    private final By viewCartLink = By.xpath("//a[contains(.,'View Cart')]");



    public ProductsPage(WebDriver driver) {
        super(driver);
    }

    public ProductsPage open(String baseUrl) {
        driver.get(baseUrl + "/products");
        waitUntilTrue(d -> d.getCurrentUrl().contains("/products"), 10);
        return this;
    }


    public void waitAllProductsVisible() {
        waitPresent(productsList);
        waitPresent(anyViewProduct);
    }



    public void openFirstProduct() {
        click(firstViewProduct);
    }

    public void waitProductDetailVisible() {
        waitVisible(productInfoBlock);
        waitVisible(productName);
    }

    public boolean isProductNameVisible() {
        return isPresent(productName);
    }
    public ProductsPage search(String query) {
        type(searchInput, query);
        click(searchButton);
        return this;
    }

    public void waitSearchedProductsVisible() {
        // заголовок + что есть хотя бы один View Product
        waitVisible(searchedProductsHeader);
        waitVisible(anyViewProduct);
    }

    public boolean hasAnyResults() {
        return isPresent(anyViewProduct);
    }

    public ProductsPage addFirstProductToCartAndContinue() {
        // На странице продукты могут требовать hover, но сайт обычно позволяет клик
        click(firstAddToCart);
        waitVisible(continueShoppingBtn);
        click(continueShoppingBtn);
        return this;
    }

    public void openCartFromModal() {
        // Если нужно сразу в корзину через модалку
        click(firstAddToCart);
        waitVisible(viewCartLink);
        click(viewCartLink);
    }



}
