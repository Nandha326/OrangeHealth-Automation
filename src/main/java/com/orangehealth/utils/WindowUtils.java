package com.orangehealth.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.WebDriver;

import com.orangehealth.base.DriverFactory;

/**
 * WindowUtils
 *
 * Provides reusable browser window and tab management
 * operations for the automation framework.
 */
@SuppressWarnings("null")
public final class WindowUtils {

    private final WebDriver driver;

    public WindowUtils() {

        this.driver = DriverFactory.getDriver();

    }

    /**
     * Current window handle.
     */
    public String getCurrentWindowHandle() {

        return driver.getWindowHandle();

    }

    /**
     * All window handles.
     */
    public Set<String> getAllWindowHandles() {

        return driver.getWindowHandles();

    }

    /**
     * Number of open windows.
     */
    public int getWindowCount() {

        return driver.getWindowHandles().size();

    }

    /**
     * Switch to window by index.
     * (Convenience method only)
     */
    public void switchToWindow(int index) {

        List<String> windows =
                new ArrayList<>(driver.getWindowHandles());

        if (index < 0 || index >= windows.size()) {

            throw new IllegalArgumentException(
                    "Invalid window index: " + index);

        }

        driver.switchTo().window(windows.get(index));

    }

    /**
     * Switch to window by title.
     */
    public boolean switchToWindowByTitle(String title) {

        for (String handle : driver.getWindowHandles()) {

            driver.switchTo().window(handle);

            String currentTitle = driver.getTitle();
            if (currentTitle != null && currentTitle.equals(title)) {

                return true;

            }

        }

        return false;

    }

    /**
     * Switch to window containing title.
     */
    public boolean switchToWindowContainingTitle(
            String partialTitle) {

        for (String handle : driver.getWindowHandles()) {

            driver.switchTo().window(handle);

            String currentTitle = driver.getTitle();
            if (currentTitle != null && currentTitle.contains(partialTitle)) {

                return true;

            }

        }

        return false;

    }

    /**
     * Switch to window by URL.
     */
    public boolean switchToWindowByUrl(
            String url) {

        for (String handle : driver.getWindowHandles()) {

            driver.switchTo().window(handle);

            String currentUrl = driver.getCurrentUrl();
            if (currentUrl != null && currentUrl.equals(url)) {

                return true;

            }

        }

        return false;

    }

    /**
     * Switch to window containing URL.
     */
    public boolean switchToWindowContainingUrl(
            String partialUrl) {

        for (String handle : driver.getWindowHandles()) {

            driver.switchTo().window(handle);

            String currentUrl = driver.getCurrentUrl();
            if (currentUrl != null && currentUrl.contains(partialUrl)) {

                return true;

            }

        }

        return false;

    }

    /**
     * Switch back to parent window.
     */
    public void switchToParentWindow(
            String parentWindowHandle) {

        driver.switchTo().window(parentWindowHandle);

    }

    /**
     * Close current window.
     */
    public void closeCurrentWindow() {

        driver.close();

    }

    /**
     * Close all child windows.
     */
    public void closeChildWindows(
            String parentWindowHandle) {

        for (String handle : driver.getWindowHandles()) {

            if (!handle.equals(parentWindowHandle)) {

                driver.switchTo().window(handle);

                driver.close();

            }

        }

        driver.switchTo().window(parentWindowHandle);

    }

}