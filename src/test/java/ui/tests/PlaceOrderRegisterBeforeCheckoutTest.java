package ui.tests;

import config.TestConfig;
import org.testng.Assert;
import org.testng.annotations.Test;
import ui.base.BaseUiTest;
import ui.pages.CartPage;
import ui.pages.HomePage;
import ui.pages.LoginPage;
import ui.pages.ProductsPage;
import ui.pages.SignupPage;
import utils.TestData;

public class PlaceOrderRegisterBeforeCheckoutTest extends BaseUiTest {

    @Test
    public void placeOrderRegisterBeforeCheckout() {
        String baseUrl = TestConfig.baseUrl();

        // 1) Register new user (before cart/checkout)
        String email = TestData.uniqueEmail();
        String password = TestData.password();

        LoginPage login = new LoginPage(driver).open(baseUrl);
        SignupPage signup = login.startSignup(TestData.name(), email);

        signup.waitForAccountInfoOrError();
        Assert.assertFalse(signup.isEmailExistsErrorVisible(), "Signup failed: email exists " + email);

        signup.fillAccountInfo(password)
                .fillAddressInfo()
                .createAccount();

        signup.waitForContinueButton();
        signup.clickContinue();

        HomePage home = new HomePage(driver);
        home.waitLoggedInAs();

        // 2) Add product to cart
        ProductsPage products = new ProductsPage(driver).open(baseUrl);
        products.waitAllProductsVisible();
        products.addFirstProductToCartAndContinue();

        // 3) Proceed to checkout (already logged in)
        CartPage cart = new CartPage(driver).open(baseUrl);
        cart.waitCartVisible();
        Assert.assertTrue(cart.getItemsCount() >= 1, "Precondition failed: cart should have item(s)");

        cart.proceedToCheckout();

        Assert.assertTrue(driver.getCurrentUrl().contains("/checkout"), "URL is not /checkout");
    }
}
