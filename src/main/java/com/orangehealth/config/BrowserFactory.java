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
        try {
            return new ChromeDriver(options);
        } catch (Exception e) {
            pauseBriefly();
            return new ChromeDriver(options);
        }
    }

    // Creates an EdgeDriver instance with default Chromium options, with fallback if Microsoft driver CDN is unreachable
    private static WebDriver createEdge(boolean headless) {
        EdgeOptions options = new EdgeOptions();
        addChromiumDefaults(options, headless);
        try {
            return new EdgeDriver(options);
        } catch (Exception e) {
            // If EdgeDriver fails (e.g. msedgedriver CDN unreachable/version mismatch), fallback to Chromium driver targeting Edge binary
            try {
                String edgeBinary = resolveEdgeBinary();
                String driverPath = resolveChromeDriverPath();
                if (edgeBinary != null) {
                    ChromeOptions chromeOptions = new ChromeOptions();
                    addChromiumDefaults(chromeOptions, headless);
                    chromeOptions.setBinary(edgeBinary);

                    if (driverPath != null) {
                        org.openqa.selenium.chrome.ChromeDriverService service =
                                new org.openqa.selenium.chrome.ChromeDriverService.Builder()
                                        .usingDriverExecutable(new java.io.File(driverPath))
                                        .build();
                        return new ChromeDriver(service, chromeOptions);
                    }
                    return new ChromeDriver(chromeOptions);
                }
            } catch (Exception fallbackEx) {
                System.err.println("[BrowserFactory] Edge fallback error: " + fallbackEx.getMessage());
            }
            throw new RuntimeException("Failed to initialize Microsoft Edge driver: " + e.getMessage(), e);
        }
    }

    // Resolves cached chromedriver executable to bypass version lookup
    private static String resolveChromeDriverPath() {
        String userHome = System.getProperty("user.home");
        java.io.File cacheDir = new java.io.File(userHome, ".cache/selenium/chromedriver");
        if (cacheDir.exists() && cacheDir.isDirectory()) {
            java.io.File[] matched = findFile(cacheDir, "chromedriver.exe");
            if (matched != null && matched.length > 0) {
                return matched[0].getAbsolutePath();
            }
        }
        return null;
    }

    private static java.io.File[] findFile(java.io.File dir, String filename) {
        java.util.List<java.io.File> results = new java.util.ArrayList<>();
        java.io.File[] files = dir.listFiles();
        if (files != null) {
            for (java.io.File f : files) {
                if (f.isDirectory()) {
                    java.io.File[] sub = findFile(f, filename);
                    if (sub != null) {
                        results.addAll(java.util.Arrays.asList(sub));
                    }
                } else if (f.getName().equalsIgnoreCase(filename)) {
                    results.add(f);
                }
            }
        }
        return results.toArray(new java.io.File[0]);
    }

    // Resolves installed Edge executable path on Windows
    private static String resolveEdgeBinary() {
        String[] paths = {
            "C:\\Program Files (x86)\\Microsoft\\Edge\\Application\\msedge.exe",
            "C:\\Program Files\\Microsoft\\Edge\\Application\\msedge.exe",
            System.getenv("LOCALAPPDATA") + "\\Microsoft\\Edge\\Application\\msedge.exe"
        };
        for (String path : paths) {
            if (path != null && new java.io.File(path).exists()) {
                return path;
            }
        }
        return null;
    }

    // Creates a FirefoxDriver instance with optional headless mode
    private static WebDriver createFirefox(boolean headless) {
        FirefoxOptions options = new FirefoxOptions();
        if (headless) {
            options.addArguments("-headless");
        }
        try {
            return new FirefoxDriver(options);
        } catch (Exception e) {
            pauseBriefly();
            return new FirefoxDriver(options);
        }
    }

    private static void pauseBriefly() {
        try {
            Thread.sleep(1500);
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
        }
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
