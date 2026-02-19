package ui.tests;

import config.TestConfig;
import org.testng.Assert;
import org.testng.annotations.Test;
import ui.base.BaseUiTest;
import ui.pages.*;
import utils.TestData;

public class PlaceOrderWithPaymentTest extends BaseUiTest {

    @Test
    public void placeOrderWithPayment() {
        String email = TestData.uniqueEmail();
        String password = TestData.password();

        // Register user (автономно)
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

        // Add product -> Cart -> Checkout -> Payment
        ProductsPage products = new ProductsPage(driver).open(TestConfig.baseUrl());
        products.waitAllProductsVisible();
        products.addFirstProductToCartAndContinue();
        products.openCart();

        CartPage cart = new CartPage(driver);
        cart.waitCartVisible();

        CheckoutPage checkout = cart.proceedToCheckout();
        checkout.waitCheckoutVisible();

        PaymentPage payment = checkout.enterComment("Auto test order").clickPlaceOrder();
        payment.waitPaymentVisible();

        // Fake card data (test site)
        OrderPlacedPage done = payment
                .fillCard("Viktor Medvedev", "4111111111111111", "123", "12", "2030")
                .payAndConfirm();

        done.waitOrderPlacedVisible();
        Assert.assertTrue(driver.getCurrentUrl().contains("/payment_done"),
                "Expected payment_done page. Actual: " + driver.getCurrentUrl());
    }
}
