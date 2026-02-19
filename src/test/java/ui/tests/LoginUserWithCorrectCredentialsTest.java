package ui.tests;

import config.TestConfig;
import org.testng.Assert;
import org.testng.annotations.Test;
import ui.base.BaseUiTest;
import ui.pages.HomePage;
import ui.pages.LoginPage;
import ui.pages.SignupPage;
import utils.TestData;

public class LoginUserWithCorrectCredentialsTest extends BaseUiTest {

    @Test
    public void loginUserWithCorrectEmailAndPassword() {
        String email = TestData.uniqueEmail();
        String password = TestData.password();

        // 1) Register new user (чтобы тест был автономным)
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

        // 2) Verify logged in
        HomePage home = new HomePage(driver).open(TestConfig.baseUrl());
        home.waitLoggedInAs();
        Assert.assertTrue(home.isLoggedInAsVisible(), "Expected 'Logged in as' after registration");

        // 3) Logout
        home.logout();
        Assert.assertTrue(new LoginPage(driver).isLoginBlockVisible(), "Expected Login page after logout");

        // 4) Login with correct credentials
        loginPage = new LoginPage(driver).open(TestConfig.baseUrl());
        loginPage.login(email, password);

        // 5) Verify logged in again
        home = new HomePage(driver).open(TestConfig.baseUrl());
        home.waitLoggedInAs();
        Assert.assertTrue(home.isLoggedInAsVisible(), "Expected 'Logged in as' after login");
    }
}
