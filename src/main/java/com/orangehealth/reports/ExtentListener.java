package com.orangehealth.reports;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentTest;

public class ExtentListener implements ITestListener {

    @Override
    public void onStart(ITestContext context) {

        ExtentManager.getExtentReports();

    }

    @Override
    public void onTestStart(ITestResult result) {

        if (ExtentManager.getTest() == null) {
            ExtentManager.createTest(result.getMethod().getMethodName());
        }

        ExtentTest test = ExtentManager.getTest();
        if (test != null) {
            test.info(
                    "Execution Started : "
                            + result.getMethod().getMethodName());
        }

    }

    @Override
    public void onTestSuccess(ITestResult result) {

        ExtentTest test = ExtentManager.getTest();
        if (test != null) {
            test.pass(
                    "Test Passed : "
                            + result.getMethod().getMethodName());
        }

    }

    @Override
    public void onTestFailure(ITestResult result) {

        ExtentTest test = ExtentManager.getTest();
        if (test == null) {
            return;
        }

        test.fail(
                "Test Failed : "
                        + result.getMethod().getMethodName());

        if (result.getThrowable() != null) {

            test.fail(result.getThrowable());

        }

        try {

            String screenshotPath =
                    ScreenshotManager.captureScreenshot(
                            result.getMethod().getMethodName());

            ReportAttachments.attachScreenshot(
                    "Failure Screenshot",
                    screenshotPath);

        } catch (Exception e) {

            test.warning(
                    "Unable to attach screenshot : "
                            + e.getMessage());

        }

    }

    @Override
    public void onTestSkipped(ITestResult result) {

        ExtentTest test = ExtentManager.getTest();
        if (test != null) {
            test.skip(
                    "Test Skipped : "
                            + result.getMethod().getMethodName());

            if (result.getThrowable() != null) {
                test.warning(
                        result.getThrowable().getMessage());
            }
        }

    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(
            ITestResult result) {

        ExtentTest test = ExtentManager.getTest();
        if (test != null) {
            test.warning(
                    "Test partially failed but is within success percentage.");
        }

    }

    @Override
    public void onTestFailedWithTimeout(ITestResult result) {

        onTestFailure(result);

    }

    @Override
    public void onFinish(ITestContext context) {

        ExtentManager.flushReport();

        ExtentManager.unload();

    }

}