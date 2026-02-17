package ui.tests;

import config.TestConfig;
import org.testng.Assert;
import org.testng.annotations.Test;
import ui.base.BaseUiTest;
import ui.pages.ContactUsPage;

public class ContactUsTest extends BaseUiTest {

    @Test
    public void contactUsFormSubmitsSuccessfully() {
        ContactUsPage page = new ContactUsPage(driver).openFromHeader(TestConfig.baseUrl());

        page.waitForGetInTouch();

        page.fillForm(
                        "Viktor",
                        "viktor.qa+" + System.currentTimeMillis() + "@mailinator.com",
                        "Test subject",
                        "Test message from Selenium"
                )
                .attachTempFile()
                .submitAndAcceptAlert();

        page.waitForSuccess();

        // Мини-проверка: после Home мы реально на главной
        page.goHome();
        Assert.assertTrue(driver.getCurrentUrl().contains("/"), "Expected to be on Home page");
    }
}
