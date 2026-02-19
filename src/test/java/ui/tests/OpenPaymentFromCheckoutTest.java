package ui.tests;

import config.TestConfig;
import org.testng.Assert;
import org.testng.annotations.Test;
import ui.base.BaseUiTest;
import ui.pages.*;
import utils.TestData;

public class OpenPaymentFromCheckoutTest extends BaseUiTest {

    @Test
    public void openPaymentFromCheckout() {
        String email = TestData.uniqueEmail();
        String password = TestData.password();

        // Register (автономность)
        LoginPage loginPage = new LoginPage(driver).open(TestConfig.baseUrl());
        SignupPage signup = loginPage.startSignup(TestData.name(), email);

        signup.waitForAccountInfoOrError();
        Assert.assertFalse(signup.isEmailExistsErrorVisible(), "Unexpected: email already exists: " + email);

        signup.fillAccountInfo(password)
                .fillAddressInfo()
                .createAccount()
                .waitForContinueButton();

        signup.clickContinue();

        // Logout -> Login
        HomePage home = new HomePage(driver).open(TestConfig.baseUrl());
        home.logout();

        loginPage = new LoginPage(driver).open(TestConfig.baseUrl());
        loginPage.login(email, password);

        // Add product -> Cart -> Checkout
        ProductsPage products = new ProductsPage(driver).open(TestConfig.baseUrl());
        products.waitAllProductsVisible();
        products.addFirstProductToCartAndContinue();
        products.openCart();

        CartPage cart = new CartPage(driver);
        cart.waitCartVisible();

        CheckoutPage checkout = cart.proceedToCheckout();
        checkout.waitCheckoutVisible();

        PaymentPage payment = checkout.enterComment("Test order comment").clickPlaceOrder();
        payment.waitPaymentVisible();

        Assert.assertTrue(driver.getCurrentUrl().contains("/payment"), "URL is not /payment");
    }
}
