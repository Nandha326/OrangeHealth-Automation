package com.orangehealth.pages;

// Author: Nandhakumar J
// Page Object for the Orange Health Tests (Lab Tests) page.
// Covers: navigation, tests listing, test detail, add to cart, and proceed to login.

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.orangehealth.base.BasePage;

public class TestsPage extends BasePage {

    private static final Duration PAGE_WAIT = Duration.ofSeconds(10);

    private final CartPage cartPage;

    public TestsPage(WebDriver driver) {
        super(driver);
        this.cartPage = new CartPage(driver);
    }

    /*=========================================================
     * Navigation
     *=========================================================*/

    private final By testsMenu =
            By.xpath("//nav//a[contains(normalize-space(.),'Tests')] | //header//a[contains(normalize-space(.),'Tests')] | //a[contains(normalize-space(.),'Tests')]");

    /*=========================================================
     * Tests Page
     *=========================================================*/

    private final By testsPageTitle =
            By.cssSelector("h1.oui-typography.procedure-description, h1, [class*='procedure-description'], .category-test-page");

    private final By testsSection =
            By.xpath("//h2[contains(normalize-space(.),'Popular tests') or contains(normalize-space(.),'Most Booked Tests') or contains(normalize-space(.),'Lab Tests') or contains(normalize-space(.),'Popular Tests')]");

    private final By firstViewDetailsButton =
            By.xpath("(//h2[contains(normalize-space(.),'Popular') or contains(normalize-space(.),'Tests') or contains(normalize-space(.),'Most Booked')]/ancestor::section//a[contains(.,'View Details') or contains(.,'View details')] | "
                    + "//h2[contains(normalize-space(.),'Popular') or contains(normalize-space(.),'Tests') or contains(normalize-space(.),'Most Booked')]/following::a[contains(.,'View Details') or contains(.,'View details')] | "
                    + "//a[contains(@href,'test') and (contains(.,'View Details') or contains(.,'View details'))] | "
                    + "//a[contains(.,'View Details') or contains(.,'View details')])[1]");

    /*=========================================================
     * Business Methods
     *=========================================================*/

    public void clickTestsMenu() {
        click(testsMenu);
        waitForPageLoad();
    }

    public boolean isTestsPageDisplayed() {
        waitForPageLoad();
        return isDisplayed(testsPageTitle, PAGE_WAIT);
    }

    public void scrollToTestsSection() {
        scrollIntoView(testsSection);
    }

    public void clickFirstViewDetails() {
        click(firstViewDetailsButton);
        waitForPageLoad();
        cartPage.isProductDetailDisplayed();
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
}

