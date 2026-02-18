package ui.tests;

import config.TestConfig;
import org.testng.Assert;
import org.testng.annotations.Test;
import ui.base.BaseUiTest;
import ui.pages.ProductsPage;

public class VerifyAllProductsAndDetailTest extends BaseUiTest {

    @Test
    public void verifyAllProductsAndFirstProductDetail() {
        ProductsPage page = new ProductsPage(driver).open(TestConfig.baseUrl());

        // Проверяем, что список товаров и ссылки "View Product" появились
        page.waitAllProductsVisible();

        Assert.assertTrue(driver.getCurrentUrl().contains("/products"),
                "URL is not /products. Actual: " + driver.getCurrentUrl());

        // Открываем первый продукт
        page.openFirstProduct();
        page.waitProductDetailVisible();

        Assert.assertTrue(page.isProductNameVisible(), "Product name is not visible on detail page");
        Assert.assertTrue(driver.getCurrentUrl().contains("/product_details"),
                "URL is not /product_details. Actual: " + driver.getCurrentUrl());
    }
}
