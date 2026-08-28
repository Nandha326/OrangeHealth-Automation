package com.orangehealth.hooks;

// Author: Nandhakumar J
// Cucumber hooks that manage WebDriver lifecycle and Extent Report logging
// before and after each scenario.

import com.orangehealth.base.DriverFactory;
import com.orangehealth.config.ConfigManager;
import com.orangehealth.config.ConfigReader;
import com.orangehealth.reports.ExtentManager;
import com.orangehealth.reports.ReportAttachments;
import com.orangehealth.reports.ScreenshotManager;
import com.orangehealth.utils.TestDataContext;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

public class Hooks {

    // Runs before each scenario: initialises WebDriver and creates an Extent test node
    @Before(order = 0)
    public void setUp(Scenario scenario) {
        DriverFactory.initDriver();

        String browser = DriverFactory.getCurrentBrowser();
        String testName = ExtentManager.isCrossBrowserMode()
                ? scenario.getName() + " [" + browser.toUpperCase() + "]"
                : scenario.getName();

        ExtentManager.createTest(testName);

        if (ExtentManager.getTest() != null) {
            ExtentManager.getTest().assignCategory(browser.toUpperCase());
            ExtentManager.getTest().assignDevice(browser);

            for (String tag : scenario.getSourceTagNames()) {
                ExtentManager.getTest().assignCategory(tag.replace("@", ""));
            }

            ExtentManager.getTest().info("<b>Browser:</b> " + browser.toUpperCase()
                    + " | <b>Scenario:</b> " + scenario.getName());
        }
    }

    // Runs after each scenario: logs result, clears test data, and quits the driver
    @After(order = 0)
    public void tearDown(Scenario scenario) {
        try {
            logScenarioResult(scenario);
        } finally {
            // Always clear context and quit driver regardless of pass/fail
            TestDataContext.clear();
            ExtentManager.unload();
            DriverFactory.quitDriver();
        }
    }

    // Logs pass/fail status to the Extent report and attaches a screenshot if configured
    private void logScenarioResult(Scenario scenario) {
        // Skip logging if the Extent test node was never created
        if (ExtentManager.getTest() == null) {
            return;
        }

        ConfigReader config = ConfigManager.getInstance().getConfigReader();

        if (scenario.isFailed()) {
            ExtentManager.getTest().fail("Scenario failed: " + scenario.getName());
            attachScreenshotIfEnabled(scenario, config.isScreenshotOnFail());
            return;
        }

        ExtentManager.getTest().pass("Scenario passed: " + scenario.getName());
        attachScreenshotIfEnabled(scenario, config.isScreenshotOnPass());
    }

    // Captures and attaches a screenshot to the Extent report if the flag is enabled
    private void attachScreenshotIfEnabled(Scenario scenario, boolean enabled) {
        if (!enabled) {
            return;
        }

        try {
            String screenshotPath = ScreenshotManager.captureScreenshot(scenario.getName());
            ReportAttachments.attachScreenshot("Scenario Screenshot", screenshotPath);
        } catch (Exception e) {
            // Log a warning rather than failing the scenario for a screenshot issue
            ExtentManager.getTest().warning("Unable to attach screenshot: " + e.getMessage());
        }
    }
}
