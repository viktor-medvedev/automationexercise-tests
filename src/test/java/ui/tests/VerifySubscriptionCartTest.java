package ui.tests;

import config.TestConfig;
import org.testng.annotations.Test;
import ui.base.BaseUiTest;
import ui.pages.CartPage;

import java.util.UUID;

public class VerifySubscriptionCartTest extends BaseUiTest {

    @Test
    public void verifySubscriptionInCartPage() {
        CartPage cart = new CartPage(driver).open(TestConfig.baseUrl());
        cart.waitCartVisible();

        cart.scrollToSubscription();
        cart.waitSubscriptionVisible();

        String email = "viktor+" + UUID.randomUUID() + "@example.com";
        cart.subscribe(email);
        cart.waitSubscriptionSuccess();
    }
}
