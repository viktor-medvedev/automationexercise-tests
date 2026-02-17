package ui.tests;

import config.TestConfig;
import org.testng.Assert;
import org.testng.annotations.Test;
import ui.base.BaseUiTest;
import ui.pages.LoginPage;

public class LoginPageSmokeTest extends BaseUiTest {

    @Test
    public void loginWithInvalidCredentialsShowsError() {
        LoginPage page = new LoginPage(driver)
                .open(TestConfig.baseUrl());

        Assert.assertTrue(page.isLoginBlockVisible(), "Login page did not open properly");

        page.login("fake_email_" + System.currentTimeMillis() + "@mail.com", "wrong_password");
        page.waitForIncorrectCredsError();

        Assert.assertTrue(page.isIncorrectCredsErrorVisible(),
                "Expected error message for invalid credentials");
    }
}
