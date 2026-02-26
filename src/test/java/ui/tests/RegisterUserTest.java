package ui.tests;

import config.TestConfig;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;
import ui.base.BaseUiTest;
import ui.pages.LoginPage;
import ui.pages.SignupPage;
import utils.TestData;

public class RegisterUserTest extends BaseUiTest {

    @Test
    public void registerUserSuccessfully() {
        String email = TestData.uniqueEmail();
        String password = TestData.password();

        LoginPage loginPage = new LoginPage(driver).open(TestConfig.baseUrl());

        SignupPage signupPage = loginPage.startSignup(TestData.name(), email);

        signupPage.waitForAccountInfoOrError();

        if (signupPage.isEmailExistsErrorVisible()) {
            Assert.fail("Signup failed: Email already exists. URL=" + signupPage.currentUrl() + " email=" + email);
        }

        signupPage
                .fillAccountInfo(password)
                .fillAddressInfo()
                .createAccount();

        signupPage.waitForContinueButton();
        signupPage.clickContinue();

        signupPage.waitForLoggedInAs();
        boolean loggedInAsVisible =
                driver.findElements(By.xpath("//*[contains(text(),'Logged in as')]")).size() > 0;

        Assert.assertTrue(loggedInAsVisible, "User is not logged in after registration");
    }
}
