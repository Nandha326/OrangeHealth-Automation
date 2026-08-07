package com.orangehealth.base;

// Author: Nandhakumar J
// Base class for all Page Objects.
// Provides reusable Selenium WebDriver actions with built-in waits,
// stale element retry, and JavaScript fallback support.

import java.time.Duration;
import java.util.Objects;
import java.util.function.Supplier;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.orangehealth.config.ConfigManager;

public class BasePage {

    // Maximum number of retries when a StaleElementReferenceException occurs
    private static final int RETRY_ATTEMPTS = 3;

    protected final WebDriver driver;
    protected final WebDriverWait wait;
    protected final Actions actions;

    private final JavascriptExecutor js;

    public BasePage(WebDriver driver) {
        this.driver = Objects.requireNonNull(driver, "WebDriver cannot be null.");
        // Initialise wait using explicit wait timeout from config
        this.wait = new WebDriverWait(
                driver,
                Duration.ofSeconds(ConfigManager.getInstance().getConfigReader().getExplicitWait()));
        this.actions = new Actions(driver);
        this.js = (JavascriptExecutor) driver;
    }

    // Waits until the element is visible using the default explicit wait
    protected WebElement waitForVisibility(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    // Waits until the element is visible using a custom timeout
    protected WebElement waitForVisibility(By locator, Duration timeout) {
        return wait(timeout).until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    // Waits until the element is present in the DOM (not necessarily visible)
    protected WebElement waitForPresence(By locator) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    // Waits until the element is clickable using the default explicit wait
    protected WebElement waitForClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    // Waits until the element is clickable using a custom timeout
    protected WebElement waitForClickable(By locator, Duration timeout) {
        return wait(timeout).until(ExpectedConditions.elementToBeClickable(locator));
    }

    // Waits until the element is no longer visible using the default explicit wait
    protected boolean waitForInvisibility(By locator) {
        return wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    // Waits until the element is no longer visible using a custom timeout
    protected boolean waitForInvisibility(By locator, Duration timeout) {
        return wait(timeout).until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    // Returns the first visible element matching the locator
    protected WebElement waitForFirstVisible(By locator) {
        return wait.until(currentDriver -> currentDriver.findElements(locator)
                .stream()
                .filter(e -> e != null && e.isDisplayed())
                .findFirst()
                .orElse(null));
    }

    // Waits until the page document readyState is 'interactive' or 'complete'
    protected boolean waitForPageLoad() {
        return wait.until(currentDriver -> {
            String state = (String) js.executeScript("return document.readyState");
            return "interactive".equals(state) || "complete".equals(state);
        });
    }

    // Returns true if the element is currently visible on the page
    protected boolean isElementVisible(By locator) {
        return isDisplayed(locator);
    }

    // Scrolls to the element and clicks it; retries on stale element
    protected void click(By locator) {
        retryOnStale(() -> {
            WebElement element = waitForClickable(locator);
            scrollIntoView(element);
            clickElement(element);
            return null;
        });
    }

    // Clicks the element using JavaScript (bypasses overlays)
    protected void jsClick(By locator) {
        WebElement element = waitForVisibility(locator);
        scrollIntoView(element);
        js.executeScript("arguments[0].click();", element);
    }

    // Clears the field and types the given value; retries on stale element
    protected void enterText(By locator, String value) {
        retryOnStale(() -> {
            WebElement element = waitForClickable(locator);
            scrollIntoView(element);
            element.sendKeys(Keys.chord(Keys.CONTROL, "a"));
            element.sendKeys(Keys.DELETE);
            element.sendKeys(value);
            return null;
        });
    }

    // Appends text to an existing field value without clearing it
    protected void appendText(By locator, String value) {
        retryOnStale(() -> {
            waitForVisibility(locator).sendKeys(value);
            return null;
        });
    }

    // Returns the trimmed visible text of the element
    protected String getText(By locator) {
        return retryOnStale(() -> waitForVisibility(locator).getText().trim());
    }

    // Returns the value of the specified DOM attribute
    protected String getAttribute(By locator, String attribute) {
        return retryOnStale(() -> waitForVisibility(locator).getDomAttribute(attribute));
    }

    // Selects a dropdown option by its visible text
    protected void selectByVisibleText(By locator, String text) {
        new Select(waitForVisibility(locator)).selectByVisibleText(text);
    }

    // Selects a dropdown option by its value attribute
    protected void selectByValue(By locator, String value) {
        new Select(waitForVisibility(locator)).selectByValue(value);
    }

    // Selects a dropdown option by its zero-based index
    protected void selectByIndex(By locator, int index) {
        new Select(waitForVisibility(locator)).selectByIndex(index);
    }

    // Moves the mouse cursor over the element (hover)
    protected void hover(By locator) {
        actions.moveToElement(waitForVisibility(locator)).perform();
    }

    // Performs a double-click on the element
    protected void doubleClick(By locator) {
        actions.doubleClick(waitForVisibility(locator)).perform();
    }

    // Performs a right-click (context click) on the element
    protected void rightClick(By locator) {
        actions.contextClick(waitForVisibility(locator)).perform();
    }

    // Sends the ENTER key to the element
    protected void pressEnter(By locator) {
        waitForVisibility(locator).sendKeys(Keys.ENTER);
    }

    // Sends the TAB key to the element
    protected void pressTab(By locator) {
        waitForVisibility(locator).sendKeys(Keys.TAB);
    }

    // Scrolls the element into the visible viewport area
    protected void scrollIntoView(By locator) {
        scrollIntoView(waitForVisibility(locator));
    }

    // Returns true if the element is visible within the default 2-second timeout
    protected boolean isDisplayed(By locator) {
        return isDisplayed(locator, Duration.ofSeconds(2));
    }

    // Returns true if the element is visible within the given timeout
    protected boolean isDisplayed(By locator, Duration timeout) {
        try {
            return waitForVisibility(locator, timeout).isDisplayed();
        } catch (TimeoutException | StaleElementReferenceException e) {
            return false;
        }
    }

    // Returns true if the element is enabled (interactable)
    protected boolean isEnabled(By locator) {
        return waitForVisibility(locator).isEnabled();
    }

    // Returns true if the element is selected (checkbox/radio)
    protected boolean isSelected(By locator) {
        return waitForVisibility(locator).isSelected();
    }

    // Returns the current browser page title
    protected String getTitle() {
        return driver.getTitle();
    }

    // Returns the current browser URL
    protected String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    // Refreshes the current page and waits for it to load
    protected void refreshPage() {
        driver.navigate().refresh();
        waitForPageLoad();
    }

    // Navigates back in browser history and waits for page load
    protected void navigateBack() {
        driver.navigate().back();
        waitForPageLoad();
    }

    // Navigates forward in browser history and waits for page load
    protected void navigateForward() {
        driver.navigate().forward();
        waitForPageLoad();
    }

    // Executes arbitrary JavaScript in the browser context
    protected Object executeScript(String script, Object... arguments) {
        return js.executeScript(script, arguments);
    }

    // Creates a new WebDriverWait with the specified custom timeout
    private WebDriverWait wait(Duration timeout) {
        return new WebDriverWait(driver, timeout);
    }

    // Scrolls the given WebElement to the centre of the viewport
    protected void scrollIntoView(WebElement element) {
        js.executeScript("arguments[0].scrollIntoView({block:'center', inline:'nearest'});", element);
    }

    // Clicks the element; falls back to JS click if intercepted by an overlay
    private void clickElement(WebElement element) {
        try {
            element.click();
        } catch (ElementClickInterceptedException e) {
            js.executeScript("arguments[0].click();", element);
        }
    }

    // Retries the given action up to RETRY_ATTEMPTS times on StaleElementReferenceException
    private <T> T retryOnStale(Supplier<T> action) {
        StaleElementReferenceException lastException = new StaleElementReferenceException(
                "Element was stale after " + RETRY_ATTEMPTS + " attempts.");

        for (int attempt = 0; attempt < RETRY_ATTEMPTS; attempt++) {
            try {
                return action.get();
            } catch (StaleElementReferenceException e) {
                lastException = e;
            }
        }

        throw lastException;
    }

    // Safely builds an XPath string literal that handles both single and double quotes
    protected String xpathLiteral(String value) {
        if (value == null) {
            return "''";
        }
        // No single quotes: wrap in single quotes
        if (!value.contains("'")) {
            return "'" + value + "'";
        }
        // No double quotes: wrap in double quotes
        if (!value.contains("\"")) {
            return "\"" + value + "\"";
        }

        // Contains both: use XPath concat() to combine parts
        String[] parts = value.split("'");
        StringBuilder builder = new StringBuilder("concat(");

        for (int i = 0; i < parts.length; i++) {
            if (i > 0) {
                builder.append(", \"'\", ");
            }
            builder.append("'").append(parts[i]).append("'");
        }

        return builder.append(")").toString();
    }
}
