package com.orangehealth.runners;

import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import com.orangehealth.base.DriverFactory;
import com.orangehealth.reports.ExtentManager;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

/**
 * Cross-Browser Test Runner covering all modules across Chrome, Edge, and Firefox.
 * Receives the target browser from TestNG suite parameters.
 */
@CucumberOptions(

        features = {
                "src/test/resources/features"
        },

        glue = {
                "com.orangehealth.hooks",
                "com.orangehealth.stepdefinitions"
        },

        plugin = {
                "pretty",
                "summary",
                "html:target/cucumber-reports/crossbrowser-report.html",
                "json:target/cucumber-reports/crossbrowser-report.json",
                "junit:target/cucumber-reports/crossbrowser-report.xml",
                "timeline:target/cucumber-reports/crossbrowser-timeline",
                "rerun:target/crossbrowser-failed-scenarios.txt"
        },

        monochrome = true,

        publish = false,

        tags = "@Smoke or @Regression"

)
public class CrossBrowserTestRunner extends AbstractTestNGCucumberTests {

    /**
     * Initializes the dedicated Cross-Browser Extent Report before suite execution.
     */
    @BeforeSuite(alwaysRun = true)
    public void beforeSuite() {
        ExtentManager.setCrossBrowserMode(true);
        ExtentManager.getExtentReports();
    }

    /**
     * Captures the browser parameter from testng-crossbrowser.xml for the current test class/thread.
     */
    @Parameters({"browser"})
    @BeforeClass(alwaysRun = true)
    public void setUpBrowser(@Optional("chrome") String browser) {
        DriverFactory.setBrowser(browser);
    }

    /**
     * Enables parallel scenario execution if required.
     */
    @Override
    @DataProvider(parallel = false)
    public Object[][] scenarios() {
        return super.scenarios();
    }

    /**
     * Flushes the Cross-Browser Extent Report after all tests complete.
     */
    @AfterSuite(alwaysRun = true)
    public void afterSuite() {
        ExtentManager.flushReport();
    }

}
