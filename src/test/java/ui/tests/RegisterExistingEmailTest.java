package ui.tests;

import config.TestConfig;
import org.testng.Assert;
import org.testng.annotations.Test;
import ui.base.BaseUiTest;
import ui.pages.LoginPage;
import ui.pages.SignupPage;
import utils.TestData;

public class RegisterExistingEmailTest extends BaseUiTest {

    @Test
    public void registerWithExistingEmailShowsError() {
        String email = TestData.uniqueEmail();
        String password = TestData.password();

        LoginPage loginPage = new LoginPage(driver).open(TestConfig.baseUrl());
        SignupPage signupPage = loginPage.startSignup(TestData.name(), email);

        signupPage.waitForAccountInfoOrError();
        Assert.assertFalse(signupPage.isEmailExistsErrorVisible(),
                "Unexpected: email already exists. URL=" + signupPage.currentUrl());

        signupPage
                .fillAccountInfo(password)
                .fillAddressInfo()
                .createAccount()
                .waitForContinueButton();

        signupPage.clickContinue();

        new ui.pages.HomePage(driver).open(TestConfig.baseUrl()).logout();

        LoginPage loginAgain = new LoginPage(driver).open(TestConfig.baseUrl());
        SignupPage secondAttempt = loginAgain.startSignup(TestData.name(), email);

        secondAttempt.waitForAccountInfoOrError();
        Assert.assertTrue(secondAttempt.isEmailExistsErrorVisible(),
                "Expected 'Email Address already exist!' error on second registration attempt");
    }
}
