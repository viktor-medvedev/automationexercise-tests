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

        // Ждём, пока форма регистрации готова (мы ждём password field / или ошибку)
        signupPage.waitForAccountInfoOrError();

        // Если вместо формы показали ошибку (например, email already exists) — сразу валим тест
        if (signupPage.isEmailExistsErrorVisible()) {
            Assert.fail("Signup failed: Email already exists. URL=" + signupPage.currentUrl() + " email=" + email);
        }

        // Заполняем форму и создаём аккаунт
        signupPage
                .fillAccountInfo(password)
                .fillAddressInfo()
                .createAccount();

        // На success-странице ждём Continue и идём дальше
        signupPage.waitForContinueButton();
        signupPage.clickContinue();

        // Проверяем, что пользователь залогинен
        signupPage.waitForLoggedInAs();
        boolean loggedInAsVisible =
                driver.findElements(By.xpath("//*[contains(text(),'Logged in as')]")).size() > 0;

        Assert.assertTrue(loggedInAsVisible, "User is not logged in after registration");
    }
}
