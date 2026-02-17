package ui.base;

import config.TestConfig;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import io.qameta.allure.testng.AllureTestNg;
import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;



@Listeners({AllureTestNg.class})
public class BaseUiTest {
    protected WebDriver driver;

    @BeforeMethod
    public void setUp() {
        ChromeOptions options = new ChromeOptions();

        if (TestConfig.headless()) {
            options.addArguments("--headless=new");
        }

        options.addArguments("--window-size=1920,1080");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        driver = new ChromeDriver(options);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @AfterMethod(alwaysRun = true)
    public void attachArtifactsOnFailure(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE && driver != null) {

            // URL
            try {
                Allure.addAttachment("URL", driver.getCurrentUrl());
            } catch (Exception ignored) {}

            // Screenshot
            try {
                byte[] png = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                Allure.addAttachment("Screenshot", "image/png", new ByteArrayInputStream(png), ".png");
            } catch (Exception ignored) {}

            // Page source (HTML)
            try {
                String html = driver.getPageSource();
                Allure.addAttachment(
                        "Page Source",
                        "text/html",
                        new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8)),
                        ".html"
                );
            } catch (Exception ignored) {}
        }
    }

}