package com.orangehealth.stepdefinitions;

import static org.testng.Assert.assertTrue;

import com.orangehealth.base.DriverFactory;
import com.orangehealth.pages.HealthCheckupPage;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class HealthCheckupSteps {

    private HealthCheckupPage healthCheckupPage;

    @When("the user clicks the Checkups navigation menu")
    public void theUserClicksTheCheckupsNavigationMenu() {
        healthCheckupPage().clickCheckupsMenu();
    }

    @Then("the Health Checkups page should be displayed")
    public void theHealthCheckupsPageShouldBeDisplayed() {
        assertTrue(
                healthCheckupPage().isHealthCheckupsPageDisplayed(),
                "Health Checkups page is not displayed.");
    }

    @When("the user scrolls to the Full Body Checkups section")
    public void theUserScrollsToTheFullBodyCheckupsSection() {
        healthCheckupPage().scrollToFullBodyCheckups();
    }

    @And("the user clicks the View Details button of the first Full Body Checkup")
    public void theUserClicksTheViewDetailsButtonOfTheFirstFullBodyCheckup() {
        healthCheckupPage().clickFirstViewDetails();
    }

    @Then("the Full Body Checkup Basic page should be displayed")
    public void theFullBodyCheckupBasicPageShouldBeDisplayed() {
        assertTrue(
                healthCheckupPage().isFullBodyCheckupDisplayed(),
                "Full Body Checkup Basic page is not displayed.");
    }

    @When("the user double-clicks the Add to Cart button")
    public void theUserDoubleClicksTheAddToCartButton() {
        healthCheckupPage().addPackageToCart();
    }

    @Then("the Cart Drawer should be displayed")
    public void theCartDrawerShouldBeDisplayed() {
        assertTrue(
                healthCheckupPage().isCartDrawerDisplayed(),
                "Cart Drawer is not displayed.");
    }

    @And("the selected Full Body Checkup should be displayed in the cart")
    public void theSelectedFullBodyCheckupShouldBeDisplayedInTheCart() {
        assertTrue(
                healthCheckupPage().isSelectedPackageDisplayed(),
                "Selected Full Body Checkup is not displayed in the cart.");
    }

    @Then("the Sign in to Continue page should be displayed")
    public void theSignInToContinuePageShouldBeDisplayed() {
        assertTrue(
                healthCheckupPage().isLoginPageDisplayed(),
                "Sign in to Continue page is not displayed.");
    }

    private HealthCheckupPage healthCheckupPage() {
        if (healthCheckupPage == null) {
            healthCheckupPage = new HealthCheckupPage(DriverFactory.getDriver());
        }
        return healthCheckupPage;
    }
}
