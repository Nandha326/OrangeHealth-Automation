package com.orangehealth.utils;

import java.time.Duration;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.orangehealth.base.DriverFactory;
import com.orangehealth.config.ConfigManager;

public final class WaitUtils {

    private final WebDriver driver;
    private final Duration defaultTimeout;

    public WaitUtils() {
        this.driver = DriverFactory.getDriver();
        this.defaultTimeout = Duration.ofSeconds(
                ConfigManager.getInstance().getConfigReader().getExplicitWait());
    }

    @SuppressWarnings("null")
    public WebElement waitForVisibility(By locator) {
        return wait(defaultTimeout)
                .until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    @SuppressWarnings("null")
    public WebElement waitForVisibility(By locator, Duration timeout) {
        return wait(timeout)
                .until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    @SuppressWarnings("null")
    public WebElement waitForClickable(By locator) {
        return wait(defaultTimeout)
                .until(ExpectedConditions.elementToBeClickable(locator));
    }

    @SuppressWarnings("null")
    public WebElement waitForClickable(By locator, Duration timeout) {
        return wait(timeout)
                .until(ExpectedConditions.elementToBeClickable(locator));
    }

    @SuppressWarnings("null")
    public WebElement waitForPresence(By locator) {
        return wait(defaultTimeout)
                .until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    @SuppressWarnings("null")
    public boolean waitForInvisibility(By locator) {
        return wait(defaultTimeout)
                .until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    @SuppressWarnings("null")
    public boolean waitForInvisibility(By locator, Duration timeout) {
        return wait(timeout)
                .until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    @SuppressWarnings("null")
    public boolean waitForTitleContains(String title) {
        return wait(defaultTimeout)
                .until(ExpectedConditions.titleContains(title));
    }

    @SuppressWarnings("null")
    public boolean waitForUrlContains(String url) {
        return wait(defaultTimeout)
                .until(ExpectedConditions.urlContains(url));
    }

    public Alert waitForAlert() {
        return wait(defaultTimeout)
                .until(ExpectedConditions.alertIsPresent());
    }

    @SuppressWarnings("null")
    public boolean waitForSelection(By locator) {
        return wait(defaultTimeout)
                .until(ExpectedConditions.elementToBeSelected(locator));
    }

    @SuppressWarnings("null")
    private WebDriverWait wait(Duration timeout) {
        return new WebDriverWait(driver, timeout);
    }
}
