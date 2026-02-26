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

        LoginPage loginPage = new LoginPage(driver).open(TestConfig.baseUrl());
        SignupPage signup = loginPage.startSignup(TestData.name(), email);

        signup.waitForAccountInfoOrError();
        Assert.assertFalse(signup.isEmailExistsErrorVisible(), "Unexpected: email already exists: " + email);

        signup.fillAccountInfo(password)
                .fillAddressInfo()
                .createAccount()
                .waitForContinueButton();

        signup.clickContinue();

        HomePage home = new HomePage(driver).open(TestConfig.baseUrl());
        home.logout();

        loginPage = new LoginPage(driver).open(TestConfig.baseUrl());
        loginPage.login(email, password);

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

        OrderPlacedPage done = payment
                .fillCard("Name Surname", "4111111111111111", "123", "12", "2030")
                .payAndConfirm();

        done.waitOrderPlacedVisible();
        Assert.assertTrue(driver.getCurrentUrl().contains("/payment_done"),
                "Expected payment_done page. Actual: " + driver.getCurrentUrl());
    }
}
