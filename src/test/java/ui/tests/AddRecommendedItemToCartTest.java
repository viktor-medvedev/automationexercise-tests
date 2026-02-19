package ui.tests;

import config.TestConfig;
import org.testng.Assert;
import org.testng.annotations.Test;
import ui.base.BaseUiTest;
import ui.pages.CartPage;
import ui.pages.HomePage;

public class AddRecommendedItemToCartTest extends BaseUiTest {

    @Test
    public void addRecommendedItemToCart() {
        HomePage home = new HomePage(driver).open(TestConfig.baseUrl());

        home.scrollToRecommendedItems();
        home.waitRecommendedItemsVisible();

        home.addFirstRecommendedItemToCartAndOpenCart();

        CartPage cart = new CartPage(driver);
        cart.waitCartVisible();

        Assert.assertTrue(cart.getItemsCount() >= 1, "Expected at least 1 item in cart");
    }
}
