package com.orangehealth.reports;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.testng.IConfigurationListener;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.orangehealth.base.DriverFactory;
import com.orangehealth.config.ConfigManager;

/**
 * Unified TestNG listener covering ALL error levels:
 *
 *  Level 1 — Suite        : @BeforeSuite / @AfterSuite       (ISuiteListener)
 *  Level 2 — Configuration: @BeforeClass / @AfterClass /
 *                           @BeforeMethod / @AfterMethod      (IConfigurationListener)
 *  Level 3 — Test         : @Test methods / Cucumber scenarios (ITestListener)
 *
 * Registered once in testng.xml — zero modifications to any test script.
 */
public class ExtentTestListener implements ITestListener, ISuiteListener, IConfigurationListener {

    /* ── Dedicated node for suite/config errors ─────────────────── */
    private static final String INFRA_NODE = "⚙ Framework Infrastructure";

    /* ================================================================
       LEVEL 1 — ISuiteListener  (@BeforeSuite / @AfterSuite)
       ================================================================ */

    @Override
    public void onStart(ISuite suite) {
        if (suite != null) {
            String suiteName = suite.getName();
            String isCrossBrowserParam = suite.getParameter("crossBrowser");
            if ((suiteName != null && suiteName.toLowerCase().contains("cross browser"))
                    || "true".equalsIgnoreCase(isCrossBrowserParam)
                    || "true".equalsIgnoreCase(System.getProperty("crossBrowser"))) {
                ExtentManager.setCrossBrowserMode(true);
            }
        }
        ExtentManager.getExtentReports();
    }

    @Override
    public void onFinish(ISuite suite) {
        ExtentManager.flushReport();
    }

    /* ================================================================
       LEVEL 2 — IConfigurationListener
       (@BeforeClass, @AfterClass, @BeforeMethod, @AfterMethod, etc.)
       ================================================================ */

    @Override
    public void onConfigurationSuccess(ITestResult result) {
        /* Silently pass — no noise for successful setup/teardown */
    }

    @Override
    public void onConfigurationFailure(ITestResult result) {
        String annotation = resolveAnnotation(result);
        String label      = annotation + " failed in " + result.getTestClass().getRealClass().getSimpleName();

        ExtentTest node = getOrCreateInfraTest(label);
        node.log(Status.FAIL, "&#10007; " + label);

        Throwable cause = result.getThrowable();
        if (cause != null) {
            node.log(Status.FAIL,
                    "<pre class=\"exception\">" + escapeHtml(stackTraceOf(cause)) + "</pre>");
        }

        captureScreenshot(node, result.getName());
        ExtentManager.flushReport();
    }

    @Override
    public void onConfigurationSkip(ITestResult result) {
        String annotation = resolveAnnotation(result);
        String label      = annotation + " skipped in " + result.getTestClass().getRealClass().getSimpleName();

        ExtentTest node = getOrCreateInfraTest(label);
        Throwable cause = result.getThrowable();
        node.log(Status.SKIP, "&#9654; " + label
                + (cause != null ? " — " + cause.getMessage() : ""));
    }

    /* ================================================================
       LEVEL 3 — ITestListener  (@Test / Cucumber scenarios)
       ================================================================ */

    @Override
    public void onStart(ITestContext context) {
        ExtentManager.getExtentReports();
    }

    @Override
    public void onFinish(ITestContext context) {
        ExtentManager.flushReport();
    }

    @Override
    public void onTestStart(ITestResult result) {
        /* Cucumber scenarios are created in Hooks.@Before.
           If not created by Hooks (e.g. dry-run or plain TestNG), create here with full context. */
        if (ExtentManager.getTest() == null) {
            String testName = getTestName(result);
            ExtentManager.createTest(testName);

            String browser = resolveBrowser(result);
            if (browser != null && !browser.isBlank()) {
                ExtentManager.getTest().assignCategory(browser.toUpperCase());
                ExtentManager.getTest().assignDevice(browser);
            }
        }
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        if (ExtentManager.getTest() != null) {
            ExtentManager.getTest().log(Status.PASS,
                    "&#10003; Passed: " + getTestName(result));
        }
        ExtentManager.unload();
    }

    @Override
    public void onTestFailure(ITestResult result) {
        if (ExtentManager.getTest() == null) {
            ExtentManager.createTest(getTestName(result));
        }

        Throwable cause = result.getThrowable();
        ExtentManager.getTest().log(Status.FAIL,
                "&#10007; Failed: " + getTestName(result));

        if (cause != null) {
            ExtentManager.getTest().log(Status.FAIL,
                    "<pre class=\"exception\">" + escapeHtml(stackTraceOf(cause)) + "</pre>");
        }

        captureScreenshot(ExtentManager.getTest(), result.getName());
        ExtentManager.unload();
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        if (ExtentManager.getTest() == null) {
            ExtentManager.createTest(getTestName(result));
        }
        Throwable cause = result.getThrowable();
        ExtentManager.getTest().log(Status.SKIP,
                "&#9654; Skipped: " + getTestName(result)
                        + (cause != null ? " — " + cause.getMessage() : ""));
        ExtentManager.unload();
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        if (ExtentManager.getTest() != null) {
            ExtentManager.getTest().log(Status.WARNING,
                    "&#9888; Failed within success %: " + getTestName(result));
        }
    }

    /* ================================================================
       Private helpers
       ================================================================ */

    /**
     * Returns a dedicated ExtentTest node for infrastructure errors.
     * Creates it on first use so it only appears in the report when needed.
     */
    private ExtentTest getOrCreateInfraTest(String nodeName) {
        return ExtentManager.getExtentReports()
                .createTest(INFRA_NODE)
                .createNode(nodeName);
    }

    /**
     * Maps ITestResult.getMethod() back to its annotation name.
     */
    private String resolveAnnotation(ITestResult result) {
        var method = result.getMethod().getConstructorOrMethod().getMethod();
        if (method.isAnnotationPresent(org.testng.annotations.BeforeSuite.class))  return "@BeforeSuite";
        if (method.isAnnotationPresent(org.testng.annotations.AfterSuite.class))   return "@AfterSuite";
        if (method.isAnnotationPresent(org.testng.annotations.BeforeClass.class))  return "@BeforeClass";
        if (method.isAnnotationPresent(org.testng.annotations.AfterClass.class))   return "@AfterClass";
        if (method.isAnnotationPresent(org.testng.annotations.BeforeMethod.class)) return "@BeforeMethod";
        if (method.isAnnotationPresent(org.testng.annotations.AfterMethod.class))  return "@AfterMethod";
        if (method.isAnnotationPresent(org.testng.annotations.BeforeTest.class))   return "@BeforeTest";
        if (method.isAnnotationPresent(org.testng.annotations.AfterTest.class))    return "@AfterTest";
        return "@Configuration";
    }

    private String getTestName(ITestResult result) {
        Object[] params = result.getParameters();
        if (params != null && params.length > 0) {
            for (Object param : params) {
                if (param instanceof io.cucumber.testng.PickleWrapper) {
                    String scenarioName = ((io.cucumber.testng.PickleWrapper) param).getPickle().getName();
                    String browser = resolveBrowser(result);
                    if (ExtentManager.isCrossBrowserMode() && browser != null && !browser.isBlank()) {
                        return scenarioName + " [" + browser.toUpperCase() + "]";
                    }
                    return scenarioName;
                }
            }
        }
        return result.getTestClass().getRealClass().getSimpleName()
                + " :: " + result.getName();
    }

    private String resolveBrowser(ITestResult result) {
        if (result != null && result.getTestContext() != null
                && result.getTestContext().getCurrentXmlTest() != null) {
            String browserParam = result.getTestContext().getCurrentXmlTest().getParameter("browser");
            if (browserParam != null && !browserParam.isBlank()) {
                return browserParam.trim().toLowerCase();
            }
        }
        return DriverFactory.getCurrentBrowser();
    }

    private String stackTraceOf(Throwable t) {
        StringWriter sw = new StringWriter();
        t.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }

    private String escapeHtml(String raw) {
        return raw
                .replace("&",  "&amp;")
                .replace("<",  "&lt;")
                .replace(">",  "&gt;")
                .replace("\"", "&quot;");
    }

    private void captureScreenshot(ExtentTest node, String name) {
        try {
            if (!ConfigManager.getInstance().getConfigReader().isScreenshotOnFail()) return;
            if (!DriverFactory.isDriverInitialized()) return;
            String path = ScreenshotManager.captureScreenshot(name);
            ReportAttachments.attachScreenshot("Failure Screenshot", path);
        } catch (Exception e) {
            if (node != null) {
                node.warning("Could not capture screenshot: " + e.getMessage());
            }
        }
    }
}
