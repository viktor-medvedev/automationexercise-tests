package ui.tests;

import config.TestConfig;
import org.testng.Assert;
import org.testng.annotations.Test;
import ui.base.BaseUiTest;
import ui.pages.*;
import utils.TestData;

public class SearchProductsVerifyCartAfterLoginTest extends BaseUiTest {

    @Test
    public void searchProductsAndVerifyCartAfterLogin() {
        String baseUrl = TestConfig.baseUrl();
        String email = TestData.uniqueEmail();
        String password = TestData.password();

        // 1) Register user
        LoginPage loginPage = new LoginPage(driver).open(baseUrl);
        SignupPage signup = loginPage.startSignup(TestData.name(), email);

        signup.waitForAccountInfoOrError();
        Assert.assertFalse(signup.isEmailExistsErrorVisible(), "Unexpected: email already exists: " + email);

        signup.fillAccountInfo(password)
                .fillAddressInfo()
                .createAccount()
                .waitForContinueButton();

        signup.clickContinue();

        // 2) Logout
        HomePage home = new HomePage(driver).open(baseUrl);
        home.logout();

        // 3) Search product and add to cart
        ProductsPage products = new ProductsPage(driver).open(baseUrl);
        products.waitAllProductsVisible();

        products.search("Top");
        products.waitSearchedProductsVisible();
        Assert.assertTrue(products.hasAnyResults(), "Expected search results");

        products.addFirstProductToCartAndContinue();
        products.openCart();

        CartPage cart = new CartPage(driver);
        cart.waitCartVisible();
        Assert.assertTrue(cart.getItemsCount() >= 1, "Expected at least 1 item in cart before login");

        // 4) Login
        loginPage = new LoginPage(driver).open(baseUrl);
        loginPage.login(email, password);

        // 5) Verify cart still has items after login
        cart = new CartPage(driver).open(baseUrl);
        cart.waitCartVisible();

        Assert.assertTrue(cart.getItemsCount() >= 1, "Cart should keep items after login");
    }
}
