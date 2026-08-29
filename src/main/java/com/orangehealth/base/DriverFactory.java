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

/**
 * Factory for managing WebDriver instances per thread.
 *
 * @author Nandhakumar J
 */
@SuppressWarnings("null")
public final class DriverFactory {

    // ThreadLocal ensures each thread gets its own WebDriver instance
    private static final ThreadLocal<WebDriver> DRIVER =
            new ThreadLocal<>();

    // InheritableThreadLocal allows child threads/parallel scenarios to inherit the target browser
    private static final ThreadLocal<String> CURRENT_BROWSER =
            new InheritableThreadLocal<>();

    private DriverFactory() {
    }

    // Sets the browser type for the current execution thread
    public static void setBrowser(String browser) {
        if (browser != null && !browser.isBlank()) {
            CURRENT_BROWSER.set(browser.trim().toLowerCase());
        }
    }

    // Gets the active browser for the current thread, falling back to System Property or ConfigReader
    public static String getCurrentBrowser() {
        String threadBrowser = CURRENT_BROWSER.get();
        if (threadBrowser != null && !threadBrowser.isBlank()) {
            return threadBrowser;
        }

        String sysBrowser = System.getProperty("browser");
        if (sysBrowser != null && !sysBrowser.isBlank()) {
            return sysBrowser.trim().toLowerCase();
        }

        return ConfigManager.getInstance().getConfigReader().getBrowser();
    }

    // Clears the thread browser context
    public static void clearBrowser() {
        CURRENT_BROWSER.remove();
    }

    // Initialises the WebDriver for the current thread if not already created
    public static void initDriver() {

        ConfigReader config = ConfigManager.getInstance().getConfigReader();

        // Skip initialisation if driver already exists for this thread
        if (DRIVER.get() != null) {
            return;
        }

        String targetBrowser = getCurrentBrowser();

        // Create browser instance based on target browser & headless config
        WebDriver driver =
                BrowserFactory.createBrowser(
                        targetBrowser,
                        config.isHeadless());

        // Apply timeout settings from config
        WebDriver finalDriver = driver;
        Duration pageLoadTimeout = Duration.ofSeconds(config.getPageLoadTimeout());
        finalDriver.manage().timeouts()
              .pageLoadTimeout(pageLoadTimeout);

        Duration implicitWait = Duration.ofSeconds(config.getImplicitWait());
        finalDriver.manage().timeouts()
              .implicitlyWait(implicitWait);

        Duration scriptTimeout = Duration.ofSeconds(config.getScriptTimeout());
        finalDriver.manage().timeouts()
              .scriptTimeout(scriptTimeout);

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

    public static boolean isDriverInitialized() {
        return DRIVER.get() != null;
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
