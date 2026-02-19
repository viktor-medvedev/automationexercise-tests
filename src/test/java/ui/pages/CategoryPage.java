package ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CategoryPage extends BasePage {

    private final By title = By.cssSelector("h2.title.text-center");

    public CategoryPage(WebDriver driver) {
        super(driver);
    }

    public void waitTitleVisible() {
        waitVisible(title);
    }

    public String getTitleText() {
        return waitVisible(title).getText().trim();
    }
}
