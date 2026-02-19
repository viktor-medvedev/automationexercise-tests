package ui.tests;

import config.TestConfig;
import org.testng.Assert;
import org.testng.annotations.Test;
import ui.base.BaseUiTest;
import ui.pages.CartPage;
import ui.pages.ProductsPage;

public class RemoveProductsFromCartTest extends BaseUiTest {

    @Test
    public void removeProductFromCart() {
        ProductsPage products = new ProductsPage(driver).open(TestConfig.baseUrl());
        products.waitAllProductsVisible();

        products.addFirstProductToCartAndContinue();
        products.openCart();

        CartPage cart = new CartPage(driver);
        cart.waitCartVisible();

        Assert.assertTrue(cart.getItemsCount() >= 1, "Expected at least 1 item in cart before removal");

        cart.removeFirstItem();
        cart.waitCartEmpty();

        Assert.assertEquals(cart.getItemsCount(), 0, "Cart should be empty after removal");
    }
}
