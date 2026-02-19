package ui.tests;

import config.TestConfig;
import org.testng.Assert;
import org.testng.annotations.Test;
import ui.base.BaseUiTest;
import ui.pages.HomePage;

public class OpenSignupLoginPageTest extends BaseUiTest {

    @Test
    public void openSignupLoginPage() {
        HomePage home = new HomePage(driver).open(TestConfig.baseUrl());
        home.openSignupLogin();

        Assert.assertTrue(driver.getCurrentUrl().contains("/login"),
                "URL is not /login. Actual: " + driver.getCurrentUrl());
    }
}
