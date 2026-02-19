package ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import java.net.URL;

public class HomePage extends BasePage {

    // ===== Header / Nav =====
    private final By signupLoginLink = By.xpath("//a[contains(.,'Signup / Login')]");
    private final By logoutLink      = By.xpath("//a[contains(.,'Logout')]");
    private final By cartHeaderLink  = By.xpath("//a[contains(.,'Cart')]");

    // более устойчиво, чем contains(text(),'Logged in as')
    private final By loggedInAs =
            By.xpath("//*[contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'logged in as')]");

    // ===== Subscription =====
    private final By subscriptionHeader = By.xpath("//h2[normalize-space()='Subscription']");
    private final By subscribeEmail     = By.id("susbscribe_email");
    private final By subscribeBtn       = By.id("subscribe");
    private final By subscribeSuccess   = By.cssSelector(".alert-success");

    // ===== Categories =====
    private final By categoriesSidebar  = By.id("accordian");
    private final By womenCategory      = By.xpath("//div[@id='accordian']//a[@href='#Women']");
    private final By menCategory        = By.xpath("//div[@id='accordian']//a[@href='#Men']");
    private final By categoryTitle      = By.cssSelector("h2.title.text-center");

    // ===== Recommended =====
    private final By recommendedItemsHeader =
            By.xpath("//*[contains(translate(normalize-space(.),'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ'),'RECOMMENDED ITEMS')]");

    private final By firstRecommendedAddToCart =
            By.cssSelector("#recommended-item-carousel a.add-to-cart");

    private final By viewCartLink = By.xpath("//a[contains(.,'View Cart')]");

    public HomePage(WebDriver driver) {
        super(driver);
    }

    public HomePage open(String baseUrl) {
        driver.get(baseUrl + "/");
        return this;
    }

    public void openSignupLogin() {
        // чтобы точно видеть меню
        scrollToTopPage();
        click(signupLoginLink);
        waitUntilTrue(d -> d.getCurrentUrl().contains("/login"), 10);
    }

    public void logout() {
        scrollToTopPage();
        click(logoutLink);
    }

    public boolean isLoggedInAsVisible() {
        return isPresent(loggedInAs) || isPresent(logoutLink);
    }

     public void waitLoggedInAsVisible() {
        boolean ok = waitUntilTrue(d ->
                        !d.findElements(loggedInAs).isEmpty()
                                || !d.findElements(logoutLink).isEmpty(),
                20
        );

        if (!ok) {
            throw new AssertionError("Login did not complete. URL=" + driver.getCurrentUrl());
        }
    }

    public CartPage openCartFromHeader() {
        // после add-to-cart/скролла меню может быть не видно
        scrollToTopPage();

        waitClickable(cartHeaderLink);
        click(cartHeaderLink);
        waitUntilTrue(d -> d.getCurrentUrl().contains("/view_cart"), 10);
        return new CartPage(driver);
    }

    public void scrollToSubscription() {
        scrollIntoView(subscriptionHeader);
    }

    public void waitSubscriptionVisible() {
        waitVisible(subscriptionHeader);
    }

    public void waitLoggedInAs() {
        waitLoggedInAsVisible();
    }

    public void subscribe(String email) {
        type(subscribeEmail, email);
        click(subscribeBtn);
    }

    public void waitSubscriptionSuccess() {
        waitVisible(subscribeSuccess);
    }

    // ===== Categories =====
    public CategoryPage openWomenDressCategory() {
        waitVisible(categoriesSidebar);
        scrollIntoView(categoriesSidebar);

        safeClick(womenCategory);

        By womenDressStable = By.xpath(
                "//div[@id='Women']//a[contains(@href,'category_products') and " +
                        "contains(translate(normalize-space(.),'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ'),'DRESS')]"
        );

        openByHref(womenDressStable);

        waitUntilTrue(d -> {
            try {
                String t = d.findElement(categoryTitle).getText().toUpperCase();
                return t.contains("WOMEN") && t.contains("DRESS");
            } catch (Exception ex) { return false; }
        }, 10);

        return new CategoryPage(driver);
    }

    public CategoryPage openMenTshirtsCategory() {
        waitVisible(categoriesSidebar);
        scrollIntoView(categoriesSidebar);

        safeClick(menCategory);

        By menTshirtsStable = By.xpath(
                "//div[@id='Men']//a[contains(@href,'category_products') and " +
                        "contains(translate(normalize-space(.),'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ'),'TSHIRTS')]"
        );

        openByHref(menTshirtsStable);

        waitUntilTrue(d -> {
            try {
                String t = d.findElement(categoryTitle).getText().toUpperCase();
                return t.contains("MEN") && t.contains("TSHIRTS");
            } catch (Exception ex) { return false; }
        }, 10);

        return new CategoryPage(driver);
    }

    // ===== Recommended =====
    public void scrollToRecommendedItems() {
        scrollToBottom(); // protected в BasePage — тут можно
    }

    public void waitRecommendedItemsVisible() {
        waitVisible(recommendedItemsHeader);
        waitPresent(firstRecommendedAddToCart);
    }

    public void addFirstRecommendedItemToCartAndOpenCart() {
        scrollIntoView(firstRecommendedAddToCart);
        click(firstRecommendedAddToCart);

        waitVisible(viewCartLink);
        click(viewCartLink);

        waitUntilTrue(d -> d.getCurrentUrl().contains("/view_cart"), 10);
    }

    // ===== helpers =====
    private void safeClick(By locator) {
        scrollIntoView(locator);
        try {
            WebElement el = waitPresent(locator);
            new Actions(driver).moveToElement(el).perform();
            el.click();
        } catch (Exception e) {
            WebElement el = waitPresent(locator);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
        }
    }

    private void openByHref(By linkLocator) {
        WebElement link = waitVisible(linkLocator);
        String href = link.getAttribute("href");

        try {
            String abs = new URL(new URL(driver.getCurrentUrl()), href).toString();
            driver.get(abs);
        } catch (Exception e) {
            driver.get(href);
        }
    }
}
