package com.orangehealth.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.orangehealth.base.DriverFactory;

@SuppressWarnings("null")
public final class JavaScriptUtils {

    private final WebDriver driver;
    private final JavascriptExecutor js;
    private final WaitUtils waitUtils;

    public JavaScriptUtils() {

        this.driver = DriverFactory.getDriver();

        this.js = (JavascriptExecutor) driver;

        this.waitUtils = new WaitUtils();

    }

    /**
     * JavaScript Click
     */
    public void click(By locator) {

        WebElement element =
                waitUtils.waitForVisibility(locator);

        js.executeScript(
                "arguments[0].click();",
                element);

    }

    /**
     * Scroll Element Into View
     */
    public void scrollIntoView(By locator) {

        WebElement element =
                waitUtils.waitForVisibility(locator);

        js.executeScript(
                "arguments[0].scrollIntoView({block:'center'});",
                element);

    }

    /**
     * Highlight Element
     */
    public void highlight(By locator) {

        WebElement element =
                waitUtils.waitForVisibility(locator);

        js.executeScript(
                "arguments[0].style.border='3px solid red';",
                element);

    }

    /**
     * Scroll To Top
     */
    public void scrollToTop() {

        js.executeScript(
                "window.scrollTo(0,0);");

    }

    /**
     * Scroll To Bottom
     */
    public void scrollToBottom() {

        js.executeScript(
                "window.scrollTo(0,document.body.scrollHeight);");

    }

    /**
     * Set Value Using JavaScript
     */
    public void setValue(By locator,
                         String value) {

        WebElement element =
                waitUtils.waitForVisibility(locator);

        js.executeScript(
                "arguments[0].value=arguments[1];",
                element,
                value);

    }

    /**
     * Remove Attribute
     */
    public void removeAttribute(By locator,
                                String attribute) {

        WebElement element =
                waitUtils.waitForVisibility(locator);

        js.executeScript(
                "arguments[0].removeAttribute(arguments[1]);",
                element,
                attribute);

    }

    /**
     * Execute Custom JavaScript
     */
    public Object executeScript(String script,
                                Object... arguments) {

        return js.executeScript(script, arguments);

    }

    /**
     * Verify Page Ready State
     */
    public boolean isPageLoaded() {

        Object result =
                js.executeScript(
                        "return document.readyState");
        String readyState = result != null ? result.toString() : "";

        return "complete".equalsIgnoreCase(
                readyState);

    }

    /**
     * Current URL
     */
    public String getCurrentUrl() {

        return driver.getCurrentUrl();

    }

    /**
     * Page Title
     */
    public String getTitle() {

        return driver.getTitle();

    }

}