package ui.pages;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import java.net.URL;

public class HomePage extends BasePage {

    private final By subscriptionHeader = By.xpath("//h2[normalize-space()='Subscription']");
    private final By subscribeEmail = By.id("susbscribe_email");
    private final By subscribeBtn = By.id("subscribe");
    private final By subscribeSuccess = By.cssSelector(".alert-success");
    private final By signupLoginLink = By.xpath("//a[contains(.,'Signup / Login')]");




    private final By womenDress = By.xpath("//div[@id='Women']//a[contains(@href,'category_products') and normalize-space()='Dress']");


    private final By menTshirts = By.xpath("//div[@id='Men']//a[contains(@href,'category_products') and normalize-space()='Tshirts']");

    private final By loggedInAs =
            By.xpath("//*[contains(text(),'Logged in as')]");

    private final By logoutLink =
            By.xpath("//a[contains(.,'Logout')]");

    private final By categoriesSidebar = By.id("accordian");

    private final By womenCategory = By.xpath("//div[@id='accordian']//a[@href='#Women']");
    private final By menCategory   = By.xpath("//div[@id='accordian']//a[@href='#Men']");

    private final By categoryTitle = By.cssSelector("h2.title.text-center");

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
        click(signupLoginLink);
        waitUntilTrue(d -> d.getCurrentUrl().contains("/login"), 10);
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

    public void scrollToSubscription() {
        scrollIntoView(subscriptionHeader);
    }

    public void waitSubscriptionVisible() {
        waitVisible(subscriptionHeader);
    }

    public void subscribe(String email) {
        type(subscribeEmail, email);
        click(subscribeBtn);
    }

    public void waitSubscriptionSuccess() {
        waitVisible(subscribeSuccess);
    }

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
            // на случай если href относительный
            String abs = new URL(new URL(driver.getCurrentUrl()), href).toString();
            driver.get(abs);
        } catch (Exception e) {
            driver.get(href);
        }
    }

    public void scrollToRecommendedItems() {
        scrollToBottom(); // у тебя уже есть в BasePage
    }

    public void waitRecommendedItemsVisible() {
        waitVisible(recommendedItemsHeader);
        waitPresent(firstRecommendedAddToCart);
    }

    public void addFirstRecommendedItemToCartAndOpenCart() {
        // клик по Add to cart в recommended блоке
        scrollIntoView(firstRecommendedAddToCart);
        click(firstRecommendedAddToCart);

        // модалка
        waitVisible(viewCartLink);
        click(viewCartLink);

        waitUntilTrue(d -> d.getCurrentUrl().contains("/view_cart"), 10);
    }





}
