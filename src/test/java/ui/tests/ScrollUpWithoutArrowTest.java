package ui.tests;

import config.TestConfig;
import org.testng.Assert;
import org.testng.annotations.Test;
import ui.base.BaseUiTest;
import ui.pages.HomePage;

public class ScrollUpWithoutArrowTest extends BaseUiTest {

    @Test
    public void scrollUpWithoutArrow() {
        HomePage home = new HomePage(driver).open(TestConfig.baseUrl());

        home.scrollToBottomPage();
        Assert.assertTrue(home.currentScrollY() > 200, "Expected page to be scrolled down");

        // без стрелки — просто скроллим наверх
        home.scrollToTopPage();
        home.waitTop();

        Assert.assertTrue(home.currentScrollY() < 50, "Expected page to be scrolled to top");
    }
}
