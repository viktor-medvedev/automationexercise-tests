package ui.pages;

import java.nio.file.Files;
import java.nio.file.Path;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ContactUsPage extends BasePage {

    private final By contactUsLink = By.xpath("//a[contains(.,'Contact us')]");
    private final By getInTouchHeader =
            By.xpath("//*[contains(translate(normalize-space(.),'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ'),'GET IN TOUCH')]");

    private final By nameInput = By.cssSelector("input[data-qa='name']");
    private final By emailInput = By.cssSelector("input[data-qa='email']");
    private final By subjectInput = By.cssSelector("input[data-qa='subject']");
    private final By messageTextArea = By.cssSelector("textarea[data-qa='message']");
    private final By uploadFileInput = By.name("upload_file");
    private final By submitBtn = By.cssSelector("input[data-qa='submit-button']");

    private final By successMsg =
            By.xpath("//*[contains(translate(normalize-space(.),'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ'),'SUCCESS! YOUR DETAILS HAVE BEEN SUBMITTED SUCCESSFULLY')]");

    private final By homeBtn = By.xpath("//a[contains(.,'Home')]");

    public ContactUsPage(WebDriver driver) {
        super(driver);
    }

    public ContactUsPage openFromHeader(String baseUrl) {
        driver.get(baseUrl + "/");
        click(contactUsLink);
        return this;
    }

    public void waitForGetInTouch() {
        waitVisible(getInTouchHeader);
    }

    public ContactUsPage fillForm(String name, String email, String subject, String message) {
        type(nameInput, name);
        type(emailInput, email);
        type(subjectInput, subject);
        type(messageTextArea, message);
        return this;
    }

    public ContactUsPage attachTempFile() {
        try {
            Path tmp = Files.createTempFile("ae-upload-", ".txt");
            Files.writeString(tmp, "hello from automation");
            // Для upload всегда sendKeys абсолютный путь
            find(uploadFileInput).sendKeys(tmp.toAbsolutePath().toString());
        } catch (Exception e) {
            throw new RuntimeException("Failed to create/upload temp file", e);
        }
        return this;
    }

    public ContactUsPage submitAndAcceptAlert() {
        click(submitBtn);
        // На сайте появляется JS alert "Press OK to proceed!"
        driver.switchTo().alert().accept();
        driver.switchTo().defaultContent();
        return this;
    }

    public void waitForSuccess() {
        waitVisible(successMsg);
    }

    public void goHome() {
        click(homeBtn);
    }
}
