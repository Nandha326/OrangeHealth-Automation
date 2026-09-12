package com.orangehealth.stepdefinitions;

import static org.testng.Assert.assertTrue;

import com.orangehealth.base.DriverFactory;
import com.orangehealth.pages.HealthCheckupPage;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class HealthCheckupSteps {

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

    @Then("the Full Body Checkup detail page should be displayed")
    public void theFullBodyCheckupDetailPageShouldBeDisplayed() {
        assertTrue(
                healthCheckupPage().isFullBodyCheckupDisplayed(),
                "Full Body Checkup detail page is not displayed.");
    }

    @And("the selected Full Body Checkup should be displayed in the cart")
    public void theSelectedFullBodyCheckupShouldBeDisplayedInTheCart() {
        assertTrue(
                healthCheckupPage().isSelectedPackageDisplayed(),
                "Selected Full Body Checkup is not displayed in the cart.");
    }

    private HealthCheckupPage healthCheckupPage() {
        return new HealthCheckupPage(DriverFactory.getDriver());
    }
}
