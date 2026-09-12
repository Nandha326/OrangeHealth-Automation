package com.orangehealth.runners;

import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;

import com.orangehealth.reports.ExtentManager;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

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

                "html:target/cucumber-reports/cucumber-report.html",

                "json:target/cucumber-reports/cucumber-report.json",

                "junit:target/cucumber-reports/cucumber-report.xml",

                "timeline:target/cucumber-reports/timeline",

                "rerun:target/failed-scenarios.txt",

                "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"

        },

        monochrome = true,

        publish = false,

        tags = "@Smoke"

)

public class TestRunner extends AbstractTestNGCucumberTests {

    /**
     * Framework initialization.
     */
    @BeforeSuite(alwaysRun = true)
    public void beforeSuite() {

        ExtentManager.getExtentReports();

    }

    /**
     * Enables parallel scenario execution.
     * Change parallel=false if sequential execution is required.
     */
    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {

        return super.scenarios();

    }

    /**
     * Flush Extent Report.
     */
    @AfterSuite(alwaysRun = true)
    public void afterSuite() {

        ExtentManager.flushReport();

    }

}