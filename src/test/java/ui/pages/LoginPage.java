package ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage {
    private final WebDriver driver;

    private final By loginHeader = By.xpath("//*[contains(text(),'Login to your account')]");
    private final By emailInput = By.cssSelector("input[data-qa='login-email']");
    private final By passwordInput = By.cssSelector("input[data-qa='login-password']");
    private final By loginButton = By.cssSelector("button[data-qa='login-button']");
    private final By errorText = By.xpath("//*[contains(text(),'Your email or password is incorrect!')]");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }

    public LoginPage open(String baseUrl) {
        driver.get(baseUrl + "/login");
        return this;
    }

    public boolean isLoginBlockVisible() {
        return driver.findElements(loginHeader).size() > 0;
    }

    public LoginPage login(String email, String password) {
        driver.findElement(emailInput).sendKeys(email);
        driver.findElement(passwordInput).sendKeys(password);
        driver.findElement(loginButton).click();
        return this;
    }

    public boolean isIncorrectCredsErrorVisible() {
        return driver.findElements(errorText).size() > 0;
    }
}
