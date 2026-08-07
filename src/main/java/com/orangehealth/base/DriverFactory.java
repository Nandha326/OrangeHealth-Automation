package com.orangehealth.base;

// Author: Nandhakumar J
// Manages WebDriver lifecycle using ThreadLocal for thread-safe parallel execution.
// Provides init, get, and quit operations for the driver instance.

import java.time.Duration;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;

import com.orangehealth.config.BrowserFactory;
import com.orangehealth.config.ConfigManager;
import com.orangehealth.config.ConfigReader;

public final class DriverFactory {

    // ThreadLocal ensures each thread gets its own WebDriver instance
    private static final ThreadLocal<WebDriver> DRIVER =
            new ThreadLocal<>();

    private DriverFactory() {
    }

    // Initialises the WebDriver for the current thread if not already created
    public static void initDriver() {

        ConfigReader config = ConfigManager.getInstance().getConfigReader();

        // Skip initialisation if driver already exists for this thread
        if (DRIVER.get() != null) {
            return;
        }

        // Create browser instance based on config
        WebDriver driver =
                BrowserFactory.createBrowser(
                        config.getBrowser(),
                        config.isHeadless());

        // Apply timeout settings from config
        driver.manage().timeouts()
              .pageLoadTimeout(Duration.ofSeconds(config.getPageLoadTimeout()));

        driver.manage().timeouts()
              .implicitlyWait(Duration.ofSeconds(config.getImplicitWait()));

        driver.manage().timeouts()
              .scriptTimeout(Duration.ofSeconds(config.getScriptTimeout()));

        // Set window size: fixed for headless, maximised for headed
        if (config.isHeadless()) {
            driver.manage().window().setSize(new Dimension(1920, 1080));
        } else if (config.isMaximizeWindow()) {
            driver.manage().window().maximize();
        }

        // Clear cookies to ensure a clean session before each test
        if (config.isDeleteCookies()) {
            driver.manage().deleteAllCookies();
        }

        DRIVER.set(driver);

    }

    // Returns the WebDriver for the current thread; throws if not initialised
    public static WebDriver getDriver() {

        WebDriver driver = DRIVER.get();

        if (driver == null) {
            throw new IllegalStateException(
                    "WebDriver is not initialized. Ensure DriverFactory.initDriver() runs before accessing it.");
        }

        return driver;

    }

    // Quits the WebDriver and removes it from ThreadLocal to prevent memory leaks
    public static void quitDriver() {

        WebDriver driver = DRIVER.get();

        if (driver != null) {

            try {
                driver.quit();
            } finally {
                // Always remove from ThreadLocal even if quit() throws
                DRIVER.remove();
            }

        }

    }

}
