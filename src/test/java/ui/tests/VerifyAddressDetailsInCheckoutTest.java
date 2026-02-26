package ui.tests;

import config.TestConfig;
import org.testng.Assert;
import org.testng.annotations.Test;
import ui.base.BaseUiTest;
import ui.pages.*;
import utils.TestData;

public class VerifyAddressDetailsInCheckoutTest extends BaseUiTest {

    @Test
    public void verifyAddressDetailsInCheckout() {
        String baseUrl = TestConfig.baseUrl();
        String email = TestData.uniqueEmail();
        String password = TestData.password();

        LoginPage loginPage = new LoginPage(driver).open(baseUrl);
        SignupPage signup = loginPage.startSignup(TestData.name(), email);

        signup.waitForAccountInfoOrError();
        Assert.assertFalse(signup.isEmailExistsErrorVisible(), "Unexpected: email already exists: " + email);

        signup.fillAccountInfo(password)
                .fillAddressInfo()
                .createAccount()
                .waitForContinueButton();

        signup.clickContinue();

        ProductsPage products = new ProductsPage(driver).open(baseUrl);
        products.waitAllProductsVisible();
        products.addFirstProductToCartAndContinue();

        CartPage cart = new CartPage(driver).open(baseUrl);
        cart.waitCartVisible();

        CheckoutPage checkout = cart.proceedToCheckout();
        checkout.waitCheckoutVisible();

        String delivery = checkout.getDeliveryAddressText();
        String billing = checkout.getBillingAddressText();

        assertContains(delivery, "Viktor");
        assertContains(delivery, "Medvedev");
        assertContains(delivery, "Niceville");
        assertContains(delivery, "Florida");
        assertContains(delivery, "32578");
        assertContains(delivery, "1234567890");

        assertContains(billing, "Viktor");
        assertContains(billing, "Medvedev");
        assertContains(billing, "Niceville");
        assertContains(billing, "Florida");
        assertContains(billing, "32578");
        assertContains(billing, "1234567890");
    }

    private void assertContains(String text, String expected) {
        Assert.assertTrue(text.contains(expected),
                "Expected to contain '" + expected + "' but was:\n" + text);
    }
}
