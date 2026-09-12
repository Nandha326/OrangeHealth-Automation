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
 *
 * @author Nandhakumar J
 */
public final class WindowUtils {

    private WebDriver driver;

    public WindowUtils() {
        this.driver = null;
    }

    public WindowUtils(WebDriver driver) {
        this.driver = driver;
    }

    private WebDriver getDriver() {
        if (this.driver != null) {
            return this.driver;
        }
        return DriverFactory.getDriver();
    }

    /**
     * Current window handle.
     */
    public String getCurrentWindowHandle() {

        return getDriver().getWindowHandle();

    }

    /**
     * All window handles.
     */
    public Set<String> getAllWindowHandles() {

        return getDriver().getWindowHandles();
    }

    /**
     * Number of open windows.
     */
    public int getWindowCount() {

        return getDriver().getWindowHandles().size();

    }

    /**
     * Switch to window by index.
     * (Convenience method only)
     */
    public void switchToWindow(int index) {

        List<String> windows =
                new ArrayList<>(getDriver().getWindowHandles());

        if (index < 0 || index >= windows.size()) {

            throw new IllegalArgumentException(
                    "Invalid window index: " + index);

        }

        getDriver().switchTo().window(windows.get(index));

    }

    /**
     * Switch to window by title.
     */
    public boolean switchToWindowByTitle(String title) {

        for (String handle : getDriver().getWindowHandles()) {

            getDriver().switchTo().window(handle);

            String currentTitle = getDriver().getTitle();
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

        for (String handle : getDriver().getWindowHandles()) {

            getDriver().switchTo().window(handle);

            String currentTitle = getDriver().getTitle();
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

        for (String handle : getDriver().getWindowHandles()) {

            getDriver().switchTo().window(handle);

            String currentUrl = getDriver().getCurrentUrl();
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

        for (String handle : getDriver().getWindowHandles()) {

            getDriver().switchTo().window(handle);

            String currentUrl = getDriver().getCurrentUrl();
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

        getDriver().switchTo().window(parentWindowHandle);

    }

    /**
     * Close current window.
     */
    public void closeCurrentWindow() {

        getDriver().close();

    }

    /**
     * Close all child windows.
     */
    public void closeChildWindows(
            String parentWindowHandle) {

        for (String handle : getDriver().getWindowHandles()) {

            if (!handle.equals(parentWindowHandle)) {

                getDriver().switchTo().window(handle);

                getDriver().close();

            }

        }

        getDriver().switchTo().window(parentWindowHandle);

    }

}