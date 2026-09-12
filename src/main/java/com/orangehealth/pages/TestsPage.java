package com.orangehealth.pages;

// Author: Nandhakumar J
// Page Object for the Orange Health Tests (Lab Tests) page.
// Covers: navigation, tests listing, test detail, add to cart, and proceed to login.

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.orangehealth.base.BasePage;

public class TestsPage extends BasePage {

    private static final Duration PAGE_WAIT = Duration.ofSeconds(30);

    public static Duration getPageWait() {
        return PAGE_WAIT;
    }

    private final CartPage cartPage;

    public TestsPage(WebDriver driver) {
        super(driver);
        this.cartPage = new CartPage(driver);
    }

    /*=========================================================
     * Navigation
     *=========================================================*/

    private final By testsMenu = By.xpath(
            "//nav//*[self::a or self::button][normalize-space(.)='Tests']"
            + " | //header//*[self::a or self::button][normalize-space(.)='Tests']"
            + " | //a[contains(@href,'tests') or contains(translate(.,'TESTS','tests'),'tests')]"
            + " | //*[self::a or self::button or self::span][contains(text(),'Tests') or contains(text(),'Lab Tests')]"
    );

    /*=========================================================
     * Tests Page
     *=========================================================*/

    private final By testsPageTitle = By.xpath(
            "//h1[contains(translate(normalize-space(.), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'test')]"
            + " | //*[@class='category-test-page']"
            + " | //*[contains(@class,'category-test-page')]"
            + " | //*[contains(@class,'procedure-description')]"
    );

    private final By testsSection = By.xpath(
            "//h2[contains(normalize-space(.),'Popular tests')"
            + " or contains(normalize-space(.),'Most Booked Tests')"
            + " or contains(normalize-space(.),'Lab Tests')"
            + " or contains(normalize-space(.),'Popular Tests')]"
            + " | //section"
    );

    private final By firstViewDetailsButton = By.xpath(
            "(//h2[contains(normalize-space(.),'Popular')"
            + " or contains(normalize-space(.),'Tests')"
            + " or contains(normalize-space(.),'Most Booked')]"
            + "/ancestor::section//a[contains(normalize-space(.),'View Details')"
            + " or contains(normalize-space(.),'View details')])"
            + " | "
            + "(//a[contains(@href,'test')"
            + " and (contains(normalize-space(.),'View Details')"
            + " or contains(normalize-space(.),'View details'))])"
            + " | "
            + "(//a[contains(normalize-space(.),'View Details') or contains(normalize-space(.),'View details')])[1]"
    );

    /*=========================================================
     * Business Methods
     *=========================================================*/

    public void clickTestsMenu() {
        try {
            WebElement visibleTestsMenu = wait.until(d -> {
                List<WebElement> elements = d.findElements(testsMenu);
                for (WebElement element : elements) {
                    try {
                        if (element.isDisplayed() && element.isEnabled()) {
                            return element;
                        }
                    } catch (Exception ignored) {
                    }
                }
                return null;
            });

            scrollIntoView(visibleTestsMenu);
            try {
                visibleTestsMenu.click();
            } catch (Exception e) {
                jsClick(testsMenu);
            }
        } catch (Exception e) {
            jsClick(testsMenu);
        }

        try {
            wait.until(d -> isTestsUrl(d.getCurrentUrl()));
        } catch (Exception ignored) {
        }
        waitForPageLoad();
    }

    public boolean isTestsPageDisplayed() {
        waitForPageLoad();
        try {
            if (isTestsUrl(getCurrentUrl())) {
                return true;
            }
        } catch (Exception ignored) {
        }

        try {
            return wait.until(d -> {
                List<WebElement> elements = d.findElements(testsPageTitle);
                for (WebElement element : elements) {
                    try {
                        if (element.isDisplayed()) {
                            return true;
                        }
                    } catch (Exception ignored) {
                    }
                }
                return false;
            });
        } catch (Exception e) {
            return false;
        }
    }

    public void scrollToTestsSection() {
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(testsSection));
            scrollIntoView(testsSection);
        } catch (Exception ignored) {
        }
    }

    public void clickFirstViewDetails() {
        try {
            WebElement viewDetails = wait.until(ExpectedConditions.elementToBeClickable(firstViewDetailsButton));
            scrollIntoView(viewDetails);
            viewDetails.click();
        } catch (Exception e) {
            jsClick(firstViewDetailsButton);
        }
        waitForPageLoad();
    }

    public boolean isTestsDetailDisplayed() {
        return cartPage.isProductDetailDisplayed();
    }

    public void addPackageToCart() {
        cartPage.addPackageToCart();
    }

    public boolean isCartDrawerDisplayed() {
        return cartPage.isCartDrawerDisplayed();
    }

    public boolean isSelectedPackageDisplayed() {
        return cartPage.isSelectedPackageDisplayed();
    }

    public void clickProceed() {
        cartPage.clickProceed();
    }

    public boolean isLoginPageDisplayed() {
        return cartPage.isLoginPageDisplayed();
    }

    /*=========================================================
     * Helper Methods
     *=========================================================*/

    private boolean isTestsUrl(String url) {
        if (url == null || url.isBlank()) {
            return false;
        }

        String normalizedUrl = url.toLowerCase();
        return normalizedUrl.contains("test")
                || normalizedUrl.contains("lab")
                || normalizedUrl.contains("category");
    }
}