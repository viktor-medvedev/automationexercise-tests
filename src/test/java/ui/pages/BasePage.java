package ui.pages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public abstract class BasePage {
    protected final WebDriver driver;
    protected final WebDriverWait wait;

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    protected WebElement find(By locator) {
        return driver.findElement(locator);
    }

    protected WebElement waitVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected WebElement waitPresent(By locator) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    protected WebElement waitClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    protected void scrollIntoView(By locator) {
        WebElement el = waitPresent(locator);
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block:'center', inline:'nearest'});", el
        );
    }

    protected void type(By locator, String text) {
        WebElement el = waitVisible(locator);
        scrollIntoView(locator);
        el.clear();
        el.sendKeys(text);
    }

    protected void click(By locator) {
        scrollIntoView(locator);
        WebElement el = waitClickable(locator);
        try {
            el.click();
        } catch (Exception e) {
            jsClick(locator);
        }
    }

    protected void jsClick(By locator) {
        WebElement el = waitPresent(locator);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
    }

    protected void moveTo(By locator) {
        WebElement el = waitPresent(locator);
        new Actions(driver).moveToElement(el).perform();
    }

    protected boolean isPresent(By locator) {
        return driver.findElements(locator).size() > 0;
    }

    protected boolean waitUntilTrue(java.util.function.Function<WebDriver, Boolean> condition, int seconds) {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(seconds)).until(condition);
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    // Иногда элементы "протухают" на динамических страницах
    protected void clickRetryingStale(By locator) {
        int attempts = 0;
        while (attempts < 2) {
            try {
                click(locator);
                return;
            } catch (StaleElementReferenceException ignored) {
                attempts++;
            }
        }
        click(locator);
    }
}
