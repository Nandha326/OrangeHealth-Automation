package com.orangehealth.pages;

// Author: Nandhakumar J
// Page Object for the Orange Health Checkups page.
// Covers: navigation, full body checkup listing, checkup detail,
// add to cart, and proceed to login flow.

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.orangehealth.base.BasePage;

public class HealthCheckupPage extends BasePage {

    public HealthCheckupPage(WebDriver driver) {
        super(driver);
    }

    /*=========================================================
     * Navigation
     *=========================================================*/

    // Checkups link in the top navigation bar
    private final By checkupsMenu =
            By.xpath("//nav//a[contains(normalize-space(.),'Checkups')] | //header//a[contains(normalize-space(.),'Checkups')]");

    /*=========================================================
     * Health Checkups Page
     *=========================================================*/

    // Page heading on the Health Checkups listing page
    private final By healthCheckupsPageTitle =
            By.cssSelector("h1.oui-typography.procedure-description");

    // Section heading for the Full Body Checkups group
    private final By fullBodyCheckupsSection =
            By.xpath("//h2[contains(normalize-space(.),'4 Full body')]");

    // First View Details button in the checkup listing
    private final By firstViewDetailsButton =
            By.xpath("(//span[contains(normalize-space(.),'View Details')])[1]");

    /*=========================================================
     * Checkup Details Page
     *=========================================================*/

    // H1 title on the individual checkup detail page
    private final By fullBodyCheckupTitle =
            By.xpath("//section[@class='primary-details']/div/h1");

    // Add to Cart button on the checkup detail page
    private final By addToCartButton =
            By.cssSelector("button.proceeding-button.add-to-cart-button");

    /*=========================================================
     * Cart Drawer
     *=========================================================*/

    // Cart drawer container that slides in after adding a package
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

    // Clicks the Checkups link in the navigation menu
    public void clickCheckupsMenu() {
        click(checkupsMenu);
    }

    // Returns true if the Health Checkups listing page heading is visible
    public boolean isHealthCheckupsPageDisplayed() {
        return isDisplayed(healthCheckupsPageTitle);
    }

    // Scrolls the page until the Full Body Checkups section is in view
    public void scrollToFullBodyCheckups() {
        scrollIntoView(fullBodyCheckupsSection);
    }

    // Clicks the View Details button of the first checkup in the listing
    public void clickFirstViewDetails() {
        click(firstViewDetailsButton);
    }

    // Returns true if the Full Body Checkup detail page title is visible
    public boolean isFullBodyCheckupDisplayed() {
        return isDisplayed(fullBodyCheckupTitle);
    }

    // Double-clicks the Add to Cart button on the checkup detail page
    public void addPackageToCart() {
        doubleClick(addToCartButton);
    }

    // Returns true if the cart drawer is visible after adding a package
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
