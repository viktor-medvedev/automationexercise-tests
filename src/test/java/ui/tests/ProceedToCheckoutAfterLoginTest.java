package ui.tests;

import config.TestConfig;
import org.testng.Assert;
import org.testng.annotations.Test;
import ui.base.BaseUiTest;
import ui.pages.*;
import utils.TestData;

public class ProceedToCheckoutAfterLoginTest extends BaseUiTest {

    @Test
    public void proceedToCheckoutAfterLogin() {
        String email = TestData.uniqueEmail();
        String password = TestData.password();

        // Register (чтобы тест был автономным)
        LoginPage loginPage = new LoginPage(driver).open(TestConfig.baseUrl());
        SignupPage signup = loginPage.startSignup(TestData.name(), email);

        signup.waitForAccountInfoOrError();
        Assert.assertFalse(signup.isEmailExistsErrorVisible(), "Unexpected: email already exists: " + email);

        signup.fillAccountInfo(password)
                .fillAddressInfo()
                .createAccount()
                .waitForContinueButton();

        signup.clickContinue();

        // Logout
        new HomePage(driver).open(TestConfig.baseUrl()).logout();

        // Login
        loginPage = new LoginPage(driver).open(TestConfig.baseUrl());
        loginPage.login(email, password);

        HomePage home = new HomePage(driver).open(TestConfig.baseUrl());
        home.waitLoggedInAs();
        Assert.assertTrue(home.isLoggedInAsVisible(), "Not logged in after login");

        // Add product -> Cart -> Checkout
        ProductsPage products = new ProductsPage(driver).open(TestConfig.baseUrl());
        products.waitAllProductsVisible();
        products.addFirstProductToCartAndContinue();
        products.openCart();

        CartPage cart = new CartPage(driver);
        cart.waitCartVisible();

        CheckoutPage checkout = cart.proceedToCheckout();
        checkout.waitCheckoutVisible();

        Assert.assertTrue(driver.getCurrentUrl().contains("/checkout"), "URL is not /checkout");
    }
}
