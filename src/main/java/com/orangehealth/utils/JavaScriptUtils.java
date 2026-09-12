package com.orangehealth.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.orangehealth.base.DriverFactory;

public final class JavaScriptUtils {

    private final WebDriver customDriver;
    private final WaitUtils waitUtils;

    public JavaScriptUtils() {
        this.customDriver = null;
        this.waitUtils = new WaitUtils();
    }

    public JavaScriptUtils(WebDriver driver) {
        this.customDriver = driver;
        this.waitUtils = new WaitUtils(driver);
    }

    private WebDriver getDriver() {
        if (this.customDriver != null) {
            return this.customDriver;
        }
        return DriverFactory.getDriver();
    }

    private JavascriptExecutor getJs() {
        return (JavascriptExecutor) getDriver();
    }

    /**
     * JavaScript Click
     */
    public void click(By locator) {
        WebElement element = waitUtils.waitForVisibility(locator);
        getJs().executeScript("arguments[0].click();", element);
    }

    /**
     * Scroll Element Into View
     */
    public void scrollIntoView(By locator) {
        WebElement element = waitUtils.waitForVisibility(locator);
        getJs().executeScript("arguments[0].scrollIntoView({block:'center'});", element);
    }

    /**
     * Highlight Element
     */
    public void highlight(By locator) {
        WebElement element = waitUtils.waitForVisibility(locator);
        getJs().executeScript("arguments[0].style.border='3px solid red';", element);
    }

    /**
     * Scroll To Top
     */
    public void scrollToTop() {
        getJs().executeScript("window.scrollTo(0,0);");
    }

    /**
     * Scroll To Bottom
     */
    public void scrollToBottom() {
        getJs().executeScript("window.scrollTo(0,document.body.scrollHeight);");
    }

    /**
     * Set Value Using JavaScript
     */
    public void setValue(By locator, String value) {
        WebElement element = waitUtils.waitForVisibility(locator);
        getJs().executeScript("arguments[0].value=arguments[1];", element, value);
    }

    /**
     * Remove Attribute
     */
    public void removeAttribute(By locator, String attribute) {
        WebElement element = waitUtils.waitForVisibility(locator);
        getJs().executeScript("arguments[0].removeAttribute(arguments[1]);", element, attribute);
    }

    /**
     * Execute Custom JavaScript
     */
    public Object executeScript(String script, Object... arguments) {
        return getJs().executeScript(script, arguments);
    }

    /**
     * Verify Page Ready State
     */
    public boolean isPageLoaded() {
        Object result = getJs().executeScript("return document.readyState");
        String readyState = result != null ? result.toString() : "";
        return "complete".equalsIgnoreCase(readyState) || "interactive".equalsIgnoreCase(readyState);
    }

    /**
     * Current URL
     */
    public String getCurrentUrl() {
        return getDriver().getCurrentUrl();
    }

    /**
     * Page Title
     */
    public String getTitle() {
        return getDriver().getTitle();
    }
}