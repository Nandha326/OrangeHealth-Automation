package com.orangehealth.config;

// Author: Nandhakumar J
// Factory class responsible for creating WebDriver instances.
// Supports Chrome, Edge, and Firefox with optional headless mode.

import org.openqa.selenium.WebDriver;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;

import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import com.orangehealth.constants.BrowserConstants;

public final class BrowserFactory {

    private BrowserFactory() {
    }

    // Creates and returns a WebDriver instance for the specified browser
    public static WebDriver createBrowser(String browser,
                                          boolean headless) {

        if (browser == null || browser.isBlank()) {
            throw new IllegalArgumentException("Browser name cannot be empty.");
        }

        switch (browser.trim().toLowerCase()) {

            case BrowserConstants.CHROME:
                return createChrome(headless);

            case BrowserConstants.EDGE:
                return createEdge(headless);

            case BrowserConstants.FIREFOX:
                return createFirefox(headless);

            default:
                throw new IllegalArgumentException(
                        "Unsupported browser : " + browser);
        }

    }

    // Creates a ChromeDriver instance with default Chromium options
    private static WebDriver createChrome(boolean headless) {

        ChromeOptions options = new ChromeOptions();
        addChromiumDefaults(options, headless);
        return new ChromeDriver(options);

    }

    // Creates an EdgeDriver instance with default Chromium options
    private static WebDriver createEdge(boolean headless) {

        EdgeOptions options = new EdgeOptions();
        addChromiumDefaults(options, headless);
        return new EdgeDriver(options);

    }

    // Creates a FirefoxDriver instance with optional headless mode
    private static WebDriver createFirefox(boolean headless) {

        FirefoxOptions options = new FirefoxOptions();

        if (headless) {
            options.addArguments("-headless");
        }

        return new FirefoxDriver(options);

    }

    // Applies common Chromium browser arguments for stable test execution
    private static void addChromiumDefaults(
            org.openqa.selenium.chromium.ChromiumOptions<?> options,
            boolean headless) {

        options.addArguments("--disable-notifications");       // Suppress browser notifications
        options.addArguments("--disable-popup-blocking");      // Allow popups during tests
        options.addArguments("--remote-allow-origins=*");      // Allow remote WebDriver connections
        options.addArguments("--disable-extensions");          // Disable browser extensions
        options.addArguments("--disable-gpu");                 // Disable GPU acceleration
        options.addArguments("--disable-dev-shm-usage");       // Prevent shared memory issues in CI
        options.addArguments("--no-first-run");                // Skip first-run setup
        options.addArguments("--no-default-browser-check");    // Skip default browser prompt
        options.addArguments("--disable-infobars");            // Hide automation info bar
        options.addArguments("--disable-background-networking"); // Reduce background network calls
        options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/130.0.0.0 Safari/537.36");
        options.addArguments("--blink-settings=imagesEnabled=false"); // Disable images for faster load

        // Use EAGER page load strategy: proceed once DOM is ready, without waiting for all resources
        options.setPageLoadStrategy(org.openqa.selenium.PageLoadStrategy.EAGER);

        if (headless) {
            // Headless mode with fixed resolution
            options.addArguments("--headless=new");
            options.addArguments("--window-size=1920,1080");
        } else {
            // Maximise window for headed runs
            options.addArguments("--start-maximized");
        }

    }

}
