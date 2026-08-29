package com.orangehealth.config;

import org.openqa.selenium.PageLoadStrategy;
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

    public static WebDriver createBrowser(String browser, boolean headless) {

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
                        "Unsupported browser: " + browser);
        }
    }

    private static WebDriver createChrome(boolean headless) {

        ChromeOptions options = new ChromeOptions();
        addChromiumDefaults(options, headless);

        return new ChromeDriver(options);
    }

    private static WebDriver createEdge(boolean headless) {

        EdgeOptions options = new EdgeOptions();
        addChromiumDefaults(options, headless);

        return new EdgeDriver(options);
    }

    private static WebDriver createFirefox(boolean headless) {

        FirefoxOptions options = new FirefoxOptions();

        if (headless) {
            options.addArguments("-headless");
            options.addArguments("--width=1920");
            options.addArguments("--height=1080");
        }

        options.setPageLoadStrategy(PageLoadStrategy.EAGER);

        return new FirefoxDriver(options);
    }

    private static void addChromiumDefaults(
            org.openqa.selenium.chromium.ChromiumOptions<?> options,
            boolean headless) {

        options.addArguments("--disable-notifications");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-gpu");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-first-run");
        options.addArguments("--no-default-browser-check");

        // Do NOT force a fake Chrome 130 user-agent.
        // Let Chrome/Edge use its real browser user-agent.

        options.setPageLoadStrategy(PageLoadStrategy.EAGER);

        if (headless) {
            options.addArguments("--headless=new");
            options.addArguments("--window-size=1920,1080");
        } else {
            options.addArguments("--start-maximized");
        }
    }
}