package ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class TestCasesPage extends BasePage {

    // Реальный заголовок страницы (а не пункт меню)
    private final By pageTitle = By.cssSelector("h2.title.text-center");

    public TestCasesPage(WebDriver driver) {
        super(driver);
    }

    public TestCasesPage open(String baseUrl) {
        driver.get(baseUrl + "/test_cases");
        waitUntilTrue(d -> d.getCurrentUrl().contains("/test_cases"), 10);
        return this;
    }

    public void waitHeaderVisible() {
        waitVisible(pageTitle);

        // ждём, что именно на странице "TEST CASES"
        waitUntilTrue(d -> {
            try {
                String t = d.findElement(pageTitle).getText().toUpperCase();
                return t.contains("TEST CASES");
            } catch (Exception e) {
                return false;
            }
        }, 10);
    }

    public boolean isHeaderPresent() {
        if (!isPresent(pageTitle)) return false;
        String t = driver.findElement(pageTitle).getText().toUpperCase();
        return t.contains("TEST CASES");
    }
}
