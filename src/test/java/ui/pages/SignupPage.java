package ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;
import config.ConfigReader;

public class SignupPage extends BasePage {

    // Account information (ключевой элемент формы)
    private final By passwordInput = By.id("password");

    // Title (иногда удобнее кликать по label)
    private final By titleMrLabel = By.cssSelector("label[for='id_gender1']");

    private final By daysSelect = By.id("days");
    private final By monthsSelect = By.id("months");
    private final By yearsSelect = By.id("years");

    // Address information
    private final By firstNameInput = By.id("first_name");
    private final By lastNameInput = By.id("last_name");
    private final By address1Input = By.id("address1");
    private final By countrySelect = By.id("country");
    private final By stateInput = By.id("state");
    private final By cityInput = By.id("city");
    private final By zipcodeInput = By.id("zipcode");
    private final By mobileNumberInput = By.id("mobile_number");

    private final By createAccountBtn = By.cssSelector("button[data-qa='create-account']");

    // Ошибка signup
    private final By emailExistsError =
            By.xpath("//*[contains(translate(normalize-space(.),'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ'),'EMAIL ADDRESS ALREADY EXIST')]");

    // Success page
    private final By continueBtn = By.cssSelector("a[data-qa='continue-button']");

    public SignupPage(WebDriver driver) {
        super(driver);
    }

    public SignupPage waitForAccountInfoOrError() {
        // Ждём либо пароль (форма готова), либо ошибку
        waitUntilTrue(d ->
                d.findElements(passwordInput).size() > 0 ||
                        d.findElements(emailExistsError).size() > 0, 15);
        return this;
    }

    public boolean isEmailExistsErrorVisible() {
        return isPresent(emailExistsError);
    }

    public String currentUrl() {
        return driver.getCurrentUrl();
    }

    public SignupPage fillAccountInfo(String password) {
        // ждём форму
        waitVisible(passwordInput);

        // title
        click(titleMrLabel);

        // password
        type(passwordInput, password);

        // DOB
        scrollIntoView(daysSelect);
        new Select(find(daysSelect)).selectByValue("10");
        new Select(find(monthsSelect)).selectByValue("5");
        new Select(find(yearsSelect)).selectByValue("1990");

        return this;
    }

    public SignupPage fillAddressInfo() {
        String firstName = ConfigReader.getOrDefault("TEST_USER_FIRSTNAME", ConfigReader.rawFromFile("user.firstName"));
        String lastName  = ConfigReader.getOrDefault("TEST_USER_LASTNAME",  ConfigReader.rawFromFile("user.lastName"));
        String address1  = ConfigReader.getOrDefault("TEST_USER_ADDRESS1",  ConfigReader.rawFromFile("user.address1"));
        String country   = ConfigReader.getOrDefault("TEST_USER_COUNTRY",   ConfigReader.rawFromFile("user.country"));
        String state     = ConfigReader.getOrDefault("TEST_USER_STATE",     ConfigReader.rawFromFile("user.state"));
        String city      = ConfigReader.getOrDefault("TEST_USER_CITY",      ConfigReader.rawFromFile("user.city"));
        String zipcode   = ConfigReader.getOrDefault("TEST_USER_ZIPCODE",   ConfigReader.rawFromFile("user.zipcode"));
        String mobile    = ConfigReader.getOrDefault("TEST_USER_MOBILE",    ConfigReader.rawFromFile("user.mobile"));

        type(firstNameInput, firstName);
        type(lastNameInput, lastName);
        type(address1Input, address1);

        scrollIntoView(countrySelect);
        new Select(find(countrySelect)).selectByVisibleText(country);

        type(stateInput, state);
        type(cityInput, city);
        type(zipcodeInput, zipcode);
        type(mobileNumberInput, mobile);

        return this;
    }


    public SignupPage createAccount() {
        clickRetryingStale(createAccountBtn);
        return this;
    }

    public SignupPage waitForContinueButton() {
        waitVisible(continueBtn);
        return this;
    }

    public void clickContinue() {
        // на этом сайте часто реклама/перекрытия — JS-клик надёжнее
        jsClick(continueBtn);
    }

    public void waitForLoggedInAs() {
        By loggedInAs = By.xpath("//*[contains(text(),'Logged in as')]");
        waitUntilTrue(d -> d.findElements(loggedInAs).size() > 0, 15);
    }
}
