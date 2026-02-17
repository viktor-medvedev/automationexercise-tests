package ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HomePage extends BasePage {

    private final By loggedInAs =
            By.xpath("//*[contains(text(),'Logged in as')]");

    private final By logoutLink =
            By.xpath("//a[contains(.,'Logout')]");

    public HomePage(WebDriver driver) {
        super(driver);
    }

    public HomePage open(String baseUrl) {
        driver.get(baseUrl + "/");
        return this;
    }

    public boolean isLoggedInAsVisible() {
        return isPresent(loggedInAs);
    }

    public void logout() {
        click(logoutLink);
    }

    public void waitLoggedInAs() {
        waitVisible(loggedInAs);
    }
}
