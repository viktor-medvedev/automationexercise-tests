package ui.tests;

import config.TestConfig;
import org.testng.Assert;
import org.testng.annotations.Test;
import ui.base.BaseUiTest;
import ui.pages.HomePage;

public class ScrollUpUsingArrowTest extends BaseUiTest {

    @Test
    public void scrollUpUsingArrow() {
        HomePage home = new HomePage(driver).open(TestConfig.baseUrl());

        home.scrollToBottomPage();
        home.waitScrollUpArrowVisible();

        Assert.assertTrue(home.currentScrollY() > 200, "Expected page to be scrolled down before clicking arrow");

        home.clickScrollUpArrow();
        home.waitTop();

        Assert.assertTrue(home.currentScrollY() < 50, "Expected page to be scrolled to top after clicking arrow");
    }
}
