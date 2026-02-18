package ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class TestCasesPage extends BasePage {

    private final By testCasesLink = By.xpath("//a[contains(.,'Test Cases')]");
    private final By testCasesHeader =
            By.xpath("//*[contains(translate(normalize-space(.),'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ'),'TEST CASES')]");

    public TestCasesPage(WebDriver driver) {
        super(driver);
    }

    public TestCasesPage open(String baseUrl) {
        driver.get(baseUrl + "/");
        click(testCasesLink);
        waitUntilTrue(d -> d.getCurrentUrl().contains("/test_cases"), 10);
        return this;
    }

    public void waitHeaderVisible() {
        waitPresent(testCasesHeader); // presence надёжнее в headless/CI
    }

    public boolean isHeaderPresent() {
        return isPresent(testCasesHeader);
    }
}
