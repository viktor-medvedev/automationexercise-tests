package ui.tests;

import config.TestConfig;
import org.testng.Assert;
import org.testng.annotations.Test;
import ui.base.BaseUiTest;
import ui.pages.CartPage;
import ui.pages.ProductsPage;

public class AddToCartTest extends BaseUiTest {

    @Test
    public void addProductToCartShowsItemInCart() {
        ProductsPage products = new ProductsPage(driver).open(TestConfig.baseUrl());
        products.waitAllProductsVisible();

        products.addFirstProductToCartAndContinue();

        CartPage cart = new CartPage(driver).open(TestConfig.baseUrl());
        cart.waitCartVisible();

        Assert.assertTrue(cart.getItemsCount() >= 1, "Expected at least 1 item in cart");
    }
}
