package ui.tests;

import config.TestConfig;
import org.testng.Assert;
import org.testng.annotations.Test;
import ui.base.BaseUiTest;
import ui.pages.CartPage;
import ui.pages.HomePage;
import ui.pages.LoginPage;
import ui.pages.ProductsPage;
import ui.pages.SignupPage;
import utils.TestData;

public class SearchProductsVerifyCartAfterLoginTest extends BaseUiTest {

    @Test
    public void searchProductsAndVerifyCartAfterLogin() {
        String baseUrl = TestConfig.baseUrl();

        String email = TestData.uniqueEmail();
        String password = TestData.password();

        LoginPage login = new LoginPage(driver).open(baseUrl);
        SignupPage signup = login.startSignup(TestData.name(), email);

        signup.waitForAccountInfoOrError();
        Assert.assertFalse(signup.isEmailExistsErrorVisible(),
                "Signup failed: email already exists. email=" + email);

        signup.fillAccountInfo(password)
                .fillAddressInfo()
                .createAccount();

        signup.waitForContinueButton();
        signup.clickContinue();

        HomePage home = new HomePage(driver);
        home.waitLoggedInAs();
        home.logout();

        ProductsPage products = new ProductsPage(driver).open(baseUrl);
        products.search("Top");
        products.waitSearchedProductsVisible();

        products.addFirstProductToCartAndContinue();

        CartPage cartBeforeLogin = new CartPage(driver).open(baseUrl);
        cartBeforeLogin.waitCartVisible();
        Assert.assertTrue(cartBeforeLogin.getItemsCount() >= 1, "Cart should have items before login");

        HomePage header = new HomePage(driver);
        header.openSignupLogin();

        new LoginPage(driver).login(email, password);

        HomePage afterLogin = new HomePage(driver);
        afterLogin.waitLoggedInAs();

        CartPage cartAfterLogin = afterLogin.openCartFromHeader();
        cartAfterLogin.waitCartVisible();

        Assert.assertTrue(cartAfterLogin.getItemsCount() >= 1, "Cart should keep items after login");
    }
}
