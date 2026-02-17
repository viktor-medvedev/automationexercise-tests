package ui.tests;

import config.TestConfig;
import org.testng.Assert;
import org.testng.annotations.Test;
import ui.base.BaseUiTest;
import ui.pages.ProductsPage;

public class SearchProductTest extends BaseUiTest {

    @Test
    public void searchProductShowsResults() {
        ProductsPage products = new ProductsPage(driver).open(TestConfig.baseUrl());
        products.waitAllProductsVisible();

        products.search("dress");
        products.waitSearchedProductsVisible();

        Assert.assertTrue(products.hasAnyResults(), "No search results found");
    }
}
