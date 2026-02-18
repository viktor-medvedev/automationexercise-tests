package ui.tests;

import config.TestConfig;
import org.testng.Assert;
import org.testng.annotations.Test;
import ui.base.BaseUiTest;
import ui.pages.CartPage;
import ui.pages.ProductsPage;

public class AddTwoProductsToCartTest extends BaseUiTest {

    @Test
    public void addTwoProductsToCart() {
        ProductsPage products = new ProductsPage(driver).open(TestConfig.baseUrl());
        products.waitAllProductsVisible();

        products.addFirstProductToCartAndContinue();
        products.addSecondProductToCartAndContinue();

        products.openCart();

        CartPage cart = new CartPage(driver);
        cart.waitCartVisible();

        Assert.assertEquals(cart.getItemsCount(), 2, "Cart should contain 2 items");
    }
}
