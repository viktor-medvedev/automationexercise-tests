package ui.tests;

import config.TestConfig;
import org.testng.Assert;
import org.testng.annotations.Test;
import ui.base.BaseUiTest;
import ui.pages.CartPage;
import ui.pages.ProductDetailPage;
import ui.pages.ProductsPage;

public class VerifyQuantityInCartTest extends BaseUiTest {

    @Test
    public void verifyProductQuantityInCart() {
        ProductsPage products = new ProductsPage(driver).open(TestConfig.baseUrl());
        products.waitAllProductsVisible();

        // Открываем детальную страницу первого товара
        products.openFirstProduct();

        ProductDetailPage detail = new ProductDetailPage(driver);
        detail.setQuantity("4").addToCartAndOpenCart();

        CartPage cart = new CartPage(driver);
        cart.waitCartVisible();

        Assert.assertEquals(cart.getFirstItemQuantity(), 4, "Quantity in cart is not 4");
    }
}
