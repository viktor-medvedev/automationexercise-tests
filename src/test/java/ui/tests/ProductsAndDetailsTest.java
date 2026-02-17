package ui.tests;

import config.TestConfig;
import org.testng.Assert;
import org.testng.annotations.Test;
import ui.base.BaseUiTest;
import ui.pages.ProductsPage;

public class ProductsAndDetailsTest extends BaseUiTest {

    @Test
    public void verifyProductsPageAndFirstProductDetail() {
        ProductsPage products = new ProductsPage(driver).open(TestConfig.baseUrl());

        products.waitAllProductsVisible();

        products.openFirstProduct();

        // Проверка, что страница деталей реально открылась и есть блок с инфой
        products.waitProductDetailVisible();
        Assert.assertTrue(products.isProductNameVisible(), "Product name is not visible on details page");
    }
}
