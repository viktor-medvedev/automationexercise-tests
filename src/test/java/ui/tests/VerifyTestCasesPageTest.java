package ui.tests;

import config.TestConfig;
import org.testng.Assert;
import org.testng.annotations.Test;
import ui.base.BaseUiTest;
import ui.pages.TestCasesPage;

public class VerifyTestCasesPageTest extends BaseUiTest {

    @Test
    public void verifyTestCasesPageOpens() {
        TestCasesPage page = new TestCasesPage(driver).open(TestConfig.baseUrl());
        page.waitHeaderVisible();

        Assert.assertTrue(page.isHeaderPresent(), "Test Cases header was not found");
        Assert.assertTrue(driver.getCurrentUrl().contains("/test_cases"), "URL is not /test_cases");
    }
}
