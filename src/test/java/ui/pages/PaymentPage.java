package ui.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.WebDriver;

public class PaymentPage extends BasePage {

    // URL check
    private final String paymentPath = "/payment";

    // Более устойчивые локаторы (data-qa + name)
    private final By nameOnCard  = By.cssSelector("input[data-qa='name-on-card'], input[name='name_on_card']");
    private final By cardNumber  = By.cssSelector("input[data-qa='card-number'], input[name='card_number']");
    private final By cvc         = By.cssSelector("input[data-qa='cvc'], input[name='cvc']");
    private final By expiryMonth = By.cssSelector("input[data-qa='expiry-month'], input[name='expiry_month']");
    private final By expiryYear  = By.cssSelector("input[data-qa='expiry-year'], input[name='expiry_year']");
    private final By payBtn      = By.id("submit");

    public PaymentPage(WebDriver driver) {
        super(driver);
    }

    /** Ждём именно форму оплаты (без "visibility" — CI/headless часто флапает) */
    public void waitPaymentFormReady() {
        waitUntilTrue(d -> d.getCurrentUrl().contains(paymentPath), 10);

        // если страница подгрузилась криво — refresh помогает
        if (driver.findElements(nameOnCard).isEmpty()) {
            driver.navigate().refresh();
            waitUntilTrue(d -> d.getCurrentUrl().contains(paymentPath), 10);
        }

        waitPresent(nameOnCard);
        waitPresent(cardNumber);
        waitPresent(payBtn);
    }

    public PaymentPage fillCard(String name, String number, String cvcValue, String month, String year) {
        waitPaymentFormReady();

        setValue(nameOnCard, name);
        setValue(cardNumber, number);
        setValue(cvc, cvcValue);
        setValue(expiryMonth, month);
        setValue(expiryYear, year);

        return this;
    }

    public OrderPlacedPage payAndConfirm() {
        scrollIntoView(payBtn);
        click(payBtn);
        return new OrderPlacedPage(driver);
    }

    public void waitPaymentVisible() {
        String origin = getOrigin();

        for (int i = 0; i < 3; i++) {
            // если мы не на /payment — идём напрямую
            if (!driver.getCurrentUrl().contains("/payment")) {
                driver.get(origin + "/payment");
            }

            // ждём появления полей коротко
            try {
                waitUntilTrue(d -> !d.findElements(nameOnCard).isEmpty(), 5);
                waitUntilTrue(d -> !d.findElements(cardNumber).isEmpty(), 5);
                return; // всё ок
            } catch (Exception ignored) {
                driver.navigate().refresh();
            }
        }

        throw new AssertionError("Payment form not loaded. URL=" + driver.getCurrentUrl()
                + " TITLE=" + driver.getTitle());
    }

    private String getOrigin() {
        try {
            java.net.URL u = new java.net.URL(driver.getCurrentUrl());
            int port = u.getPort();
            String base = u.getProtocol() + "://" + u.getHost();
            if (port != -1 && port != u.getDefaultPort()) base += ":" + port;
            return base;
        } catch (Exception e) {
            return "";
        }
    }



    // ---- helpers ----
    private void setValue(By locator, String value) {
        WebElement el = waitPresent(locator);
        scrollIntoView(locator);

        try {
            el.clear();
            el.sendKeys(value);
        } catch (Exception e) {
            // JS fallback (если элемент не interactable в headless/оверлей)
            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].value = arguments[1];" +
                            "arguments[0].dispatchEvent(new Event('input', {bubbles:true}));" +
                            "arguments[0].dispatchEvent(new Event('change', {bubbles:true}));",
                    el, value
            );
        }
    }
}
