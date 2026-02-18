package ui.tests;

import config.TestConfig;
import org.testng.annotations.Test;
import ui.base.BaseUiTest;
import ui.pages.HomePage;

import java.util.UUID;

public class VerifySubscriptionHomeTest extends BaseUiTest {

    @Test
    public void verifySubscriptionInHomePage() {
        HomePage home = new HomePage(driver);
        home.open(TestConfig.baseUrl());

        home.scrollToSubscription();
        home.waitSubscriptionVisible();

        String email = "viktor+" + UUID.randomUUID() + "@example.com";
        home.subscribe(email);
        home.waitSubscriptionSuccess();
    }
}
