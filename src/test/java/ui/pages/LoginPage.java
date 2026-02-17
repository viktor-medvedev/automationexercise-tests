package ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage {

    private final By loginHeader =
            By.xpath("//*[contains(translate(normalize-space(.),'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ'),'LOGIN TO YOUR ACCOUNT')]");

    private final By emailInput = By.cssSelector("input[data-qa='login-email']");
    private final By passwordInput = By.cssSelector("input[data-qa='login-password']");
    private final By loginButton = By.cssSelector("button[data-qa='login-button']");

    private final By incorrectCredsError =
            By.xpath("//*[contains(translate(normalize-space(.),'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ'),'YOUR EMAIL OR PASSWORD IS INCORRECT')]");

    // Signup (New User Signup!)
    private final By signupNameInput = By.cssSelector("input[data-qa='signup-name']");
    private final By signupEmailInput = By.cssSelector("input[data-qa='signup-email']");
    private final By signupButton = By.cssSelector("button[data-qa='signup-button']");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public LoginPage open(String baseUrl) {
        driver.get(baseUrl + "/login");
        return this;
    }

    public boolean isLoginBlockVisible() {
        return isPresent(loginHeader);
    }

    public LoginPage login(String email, String password) {
        type(emailInput, email);
        type(passwordInput, password);
        click(loginButton);
        return this;
    }

    public boolean isIncorrectCredsErrorVisible() {
        return isPresent(incorrectCredsError);
    }

    public SignupPage startSignup(String name, String email) {
        type(signupNameInput, name);
        type(signupEmailInput, email);

        String beforeUrl = driver.getCurrentUrl();
        click(signupButton);

        // ждём, что URL сменится (обычно на /signup)
        waitUntilTrue(d -> !d.getCurrentUrl().equals(beforeUrl), 10);

        return new SignupPage(driver);
    }

    public LoginPage waitForIncorrectCredsError() {
        waitVisible(incorrectCredsError);
        return this;
    }
}
