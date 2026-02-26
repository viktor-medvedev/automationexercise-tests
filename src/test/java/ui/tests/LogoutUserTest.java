package ui.tests;

import config.TestConfig;
import org.testng.Assert;
import org.testng.annotations.Test;
import ui.base.BaseUiTest;
import ui.pages.HomePage;
import ui.pages.LoginPage;
import ui.pages.SignupPage;
import utils.TestData;

public class LogoutUserTest extends BaseUiTest {

    @Test
    public void logoutUserSuccessfully() {
        String email = TestData.uniqueEmail();
        String password = TestData.password();

        LoginPage loginPage = new LoginPage(driver).open(TestConfig.baseUrl());
        SignupPage signupPage = loginPage.startSignup(TestData.name(), email);

        signupPage.waitForAccountInfoOrError();
        Assert.assertFalse(signupPage.isEmailExistsErrorVisible(),
                "Signup failed: Email already exists. URL=" + signupPage.currentUrl());

        signupPage
                .fillAccountInfo(password)
                .fillAddressInfo()
                .createAccount()
                .waitForContinueButton();

        signupPage.clickContinue();

        HomePage home = new HomePage(driver).open(TestConfig.baseUrl());
        home.waitLoggedInAs();
        Assert.assertTrue(home.isLoggedInAsVisible(), "Expected 'Logged in as' on Home page");

        home.logout();

        Assert.assertTrue(new LoginPage(driver).isLoginBlockVisible(), "Expected Login page after logout");
    }
}
