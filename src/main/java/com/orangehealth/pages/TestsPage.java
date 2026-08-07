package com.orangehealth.pages;

// Author: Nandhakumar J
// Page Object for the Orange Health Tests (Lab Tests) page.
// Covers: navigation, tests listing, test detail, add to cart, and proceed to login.

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.orangehealth.base.BasePage;

public class TestsPage extends BasePage {

    public TestsPage(WebDriver driver) {
        super(driver);
    }

    /*=========================================================
     * Navigation
     *=========================================================*/

    // Tests link in the top navigation bar
    private final By testsMenu =
            By.xpath("//nav//a[contains(normalize-space(.),'Tests')] | //header//a[contains(normalize-space(.),'Tests')]");

    /*=========================================================
     * Tests Page
     *=========================================================*/

    // Page heading on the Tests listing page
    private final By testsPageTitle =
            By.cssSelector("h1.oui-typography.procedure-description");

    // Section heading for the popular tests list
    private final By testsSection =
            By.xpath("//h2[contains(normalize-space(.),'Popular tests in')]");

    // First View Details button in the tests listing
    private final By firstViewDetailsButton =
            By.xpath("(//span[contains(normalize-space(.),'View Details')])[1]");

    /*=========================================================
     * Test Details Page
     *=========================================================*/

    // H1 title on the individual test detail page
    private final By testsTitle =
            By.xpath("//section[@class='primary-details']/div/h1");

    // Add to Cart button on the test detail page
    private final By addToCartButton =
            By.cssSelector("button.proceeding-button.add-to-cart-button");

    /*=========================================================
     * Cart Drawer
     *=========================================================*/

    // Cart drawer container that slides in after adding an item
    private final By cartDrawer =
            By.xpath("//div[@class='cart-modal-body']");

    // Name of the package added to the cart
    private final By addedPackageName =
            By.xpath("//section[@class='cart-item-details']/h3");

    // Proceed button inside the cart drawer
    private final By proceedButton =
            By.xpath("(//span[normalize-space(.)='Proceed']/ancestor::button)[1]");

    /*=========================================================
     * Login Page
     *=========================================================*/

    // Header text on the Sign In / Login page
    private final By loginPageTitle =
            By.cssSelector("div[class='auth-primary-header'] span");

    /*=========================================================
     * Business Methods
     *=========================================================*/

    // Clicks the Tests link in the navigation menu
    public void clickTestsMenu() {
        click(testsMenu);
    }

    // Returns true if the Tests listing page heading is visible
    public boolean isTestsPageDisplayed() {
        return isDisplayed(testsPageTitle);
    }

    // Scrolls the page until the popular tests section is in view
    public void scrollToTestsSection() {
        scrollIntoView(testsSection);
    }

    // Clicks the View Details button of the first test in the listing
    public void clickFirstViewDetails() {
        click(firstViewDetailsButton);
    }

    // Returns true if the test detail page title is visible
    public boolean isTestsDetailDisplayed() {
        return isDisplayed(testsTitle);
    }

    // Double-clicks the Add to Cart button on the test detail page
    public void addPackageToCart() {
        doubleClick(addToCartButton);
    }

    // Returns true if the cart drawer is visible after adding an item
    public boolean isCartDrawerDisplayed() {
        return isDisplayed(cartDrawer);
    }

    // Returns true if the added package name is shown in the cart drawer
    public boolean isSelectedPackageDisplayed() {
        return isDisplayed(addedPackageName);
    }

    // Returns the name of the package currently shown in the cart drawer
    public String getSelectedPackageName() {
        return getText(addedPackageName);
    }

    // Clicks the Proceed button inside the cart drawer
    public void clickProceed() {
        click(proceedButton);
    }

    // Returns true if the Login/Sign-in page is displayed
    public boolean isLoginPageDisplayed() {
        return isDisplayed(loginPageTitle);
    }

}
