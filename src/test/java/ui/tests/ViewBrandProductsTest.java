package ui.tests;

import config.TestConfig;
import org.testng.Assert;
import org.testng.annotations.Test;
import ui.base.BaseUiTest;
import ui.pages.ProductsPage;

public class ViewBrandProductsTest extends BaseUiTest {

    @Test
    public void viewBrandProducts() {
        ProductsPage products = new ProductsPage(driver).open(TestConfig.baseUrl());
        products.waitAllProductsVisible();
        products.waitBrandsVisible();

        // Brand 1
        products.openBrand("Polo");
        String title1 = products.getListingTitle().toUpperCase();
        Assert.assertTrue(title1.contains("BRAND") && title1.contains("POLO"),
                "Unexpected brand title: " + products.getListingTitle());

        // Brand 2
        products.openBrand("H&M");
        String title2 = products.getListingTitle().toUpperCase();
        Assert.assertTrue(title2.contains("BRAND") && title2.contains("H&M"),
                "Unexpected brand title: " + products.getListingTitle());
    }
}
