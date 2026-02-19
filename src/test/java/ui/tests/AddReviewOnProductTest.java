package ui.tests;

import config.TestConfig;
import org.testng.annotations.Test;
import ui.base.BaseUiTest;
import ui.pages.ProductsPage;
import utils.TestData;

public class AddReviewOnProductTest extends BaseUiTest {

    @Test
    public void addReviewOnProduct() {
        ProductsPage products = new ProductsPage(driver).open(TestConfig.baseUrl());
        products.waitAllProductsVisible();

        products.openFirstProduct();
        products.waitProductDetailVisible();

        products.waitReviewBlockVisible();
        products.submitReview(TestData.name(), TestData.uniqueEmail(), "Great product! Automated review.");
        products.waitReviewSuccess();
    }
}
