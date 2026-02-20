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

public class PlaceOrderRegisterWhileCheckoutTest extends BaseUiTest {

    @Test
    public void placeOrderRegisterWhileCheckout() {
        String baseUrl = TestConfig.baseUrl();

        // 1) Add product to cart
        ProductsPage products = new ProductsPage(driver).open(baseUrl);
        products.waitAllProductsVisible();
        products.addFirstProductToCartAndContinue();

        CartPage cart = new CartPage(driver).open(baseUrl);
        cart.waitCartVisible();
        Assert.assertTrue(cart.getItemsCount() >= 1, "Precondition failed: cart should have item(s)");

        // 2) Proceed to checkout (guest) -> should require login/signup
        // (если у тебя есть метод checkout в CartPage — используй его.
        // если нет — нажимаем по тексту)
        cart.proceedToCheckout();

        // 3) On checkout/login step: go to Signup/Login
        HomePage home = new HomePage(driver);
        home.openSignupLogin();

        // 4) Register new user
        String email = TestData.uniqueEmail();
        String password = TestData.password();

        LoginPage login = new LoginPage(driver);
        SignupPage signup = login.startSignup(TestData.name(), email);

        signup.waitForAccountInfoOrError();
        Assert.assertFalse(signup.isEmailExistsErrorVisible(), "Signup failed: email exists " + email);

        signup.fillAccountInfo(password)
                .fillAddressInfo()
                .createAccount();

        signup.waitForContinueButton();
        signup.clickContinue();

        // 5) Return to cart/checkout and verify we can proceed (logged in)
        home.waitLoggedInAs();

        CartPage cart2 = new CartPage(driver).open(baseUrl);
        cart2.waitCartVisible();
        cart2.proceedToCheckout();

        Assert.assertTrue(driver.getCurrentUrl().contains("/checkout"), "URL is not /checkout");
    }
}
