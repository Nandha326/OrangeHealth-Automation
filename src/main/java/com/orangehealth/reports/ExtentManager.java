package com.orangehealth.reports;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.orangehealth.constants.FrameworkConstants;
import com.orangehealth.constants.PathConstants;
import com.orangehealth.constants.ReportConstants;

/**
 * Thread-safe Singleton for ExtentReports v5.
 * Injects custom CSS/JS theme at initialisation time.
 * Zero impact on existing test scripts — all lifecycle
 * management is handled via Hooks.java and ExtentTestListener.java.
 */
public final class ExtentManager {

    private static ExtentReports extentReports;

    private static final ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();

    private static final String CSS_RESOURCE = "extent-theme/custom-spark-theme.css";
    private static final String JS_RESOURCE  = "extent-theme/custom-spark-script.js";

    private ExtentManager() {
        throw new IllegalStateException("Utility class");
    }

    /* ── Singleton accessor ─────────────────────────────────────── */

    public static synchronized ExtentReports getExtentReports() {
        if (extentReports == null) {
            initializeReport();
        }
        return extentReports;
    }

    /* ── Initialisation ─────────────────────────────────────────── */

    private static void initializeReport() {
        createReportDirectory();

        String timestamp  = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String reportPath = PathConstants.REPORT_PATH + "ExtentReport_" + timestamp + ".html";

        ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);

        spark.config().setDocumentTitle(ReportConstants.REPORT_TITLE);
        spark.config().setReportName(ReportConstants.REPORT_NAME);
        spark.config().setTheme(Theme.DARK);
        spark.config().setTimelineEnabled(true);
        spark.config().setEncoding("UTF-8");

        /* Inject custom CSS theme */
        String css = loadResource(CSS_RESOURCE);
        if (!css.isBlank()) {
            spark.config().setCss(css);
        }

        /* Inject custom JS enhancements */
        String js = loadResource(JS_RESOURCE);
        if (!js.isBlank()) {
            spark.config().setJs(js);
        }

        extentReports = new ExtentReports();
        extentReports.attachReporter(spark);

        loadSystemInformation();
    }

    /* ── System Info ────────────────────────────────────────────── */

    private static void loadSystemInformation() {
        extentReports.setSystemInfo("Framework",        FrameworkConstants.FRAMEWORK_NAME);
        extentReports.setSystemInfo("Version",          FrameworkConstants.FRAMEWORK_VERSION);
        extentReports.setSystemInfo("Organization",     FrameworkConstants.ORGANIZATION);
        extentReports.setSystemInfo("Author",           FrameworkConstants.AUTHOR);
        extentReports.setSystemInfo("Operating System", System.getProperty("os.name"));
        extentReports.setSystemInfo("Java Version",     System.getProperty("java.version"));
        extentReports.setSystemInfo("User",             System.getProperty("user.name"));
        extentReports.setSystemInfo("Browser",
                System.getProperty("browser", "chrome"));
        extentReports.setSystemInfo("Environment",
                System.getProperty("environment", "PROD"));
    }

    /* ── Test lifecycle helpers ─────────────────────────────────── */

    public static synchronized void createTest(String scenarioName) {
        ExtentTest test = getExtentReports().createTest(scenarioName);
        extentTest.set(test);
    }

    public static ExtentTest getTest() {
        return extentTest.get();
    }

    public static void unload() {
        extentTest.remove();
    }

    public static synchronized void flushReport() {
        if (extentReports != null) {
            extentReports.flush();
        }
    }

    /* ── Utilities ──────────────────────────────────────────────── */

    private static void createReportDirectory() {
        File folder = new File(PathConstants.REPORT_PATH);
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }

    /**
     * Loads a classpath resource as a UTF-8 string.
     * Returns an empty string if the resource is not found,
     * so a missing theme file never breaks the test run.
     */
    private static String loadResource(String resourcePath) {
        try (InputStream is = ExtentManager.class
                .getClassLoader()
                .getResourceAsStream(resourcePath)) {

            if (is == null) {
                System.err.println("[ExtentManager] Theme resource not found: " + resourcePath);
                return "";
            }
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);

        } catch (IOException e) {
            System.err.println("[ExtentManager] Failed to load theme resource: "
                    + resourcePath + " — " + e.getMessage());
            return "";
        }
    }
}
