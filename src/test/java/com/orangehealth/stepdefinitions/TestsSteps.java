package com.orangehealth.stepdefinitions;

import static org.testng.Assert.assertTrue;

import com.orangehealth.base.DriverFactory;
import com.orangehealth.pages.TestsPage;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class TestsSteps {

    @When("the user clicks the Tests navigation menu")
    public void theUserClicksTheTestsNavigationMenu() {
        testsPage().clickTestsMenu();
    }

    @Then("the Tests page should be displayed")
    public void theTestsPageShouldBeDisplayed() {
        assertTrue(
                testsPage().isTestsPageDisplayed(),
                "Tests page is not displayed. Current URL: "
                        + DriverFactory.getDriver().getCurrentUrl());
    }

    @When("the user scrolls to the Tests section")
    public void theUserScrollsToTheTestsSection() {
        testsPage().scrollToTestsSection();
    }

    @And("the user clicks the View Details button of the first Test")
    public void theUserClicksTheViewDetailsButtonOfTheFirstTest() {
        testsPage().clickFirstViewDetails();
    }

    @Then("the Tests detail page should be displayed")
    public void theTestsDetailPageShouldBeDisplayed() {
        assertTrue(
                testsPage().isTestsDetailDisplayed(),
                "Tests detail page is not displayed.");
    }

    @And("the selected Test should be displayed in the cart")
    public void theSelectedTestShouldBeDisplayedInTheCart() {
        assertTrue(
                testsPage().isSelectedPackageDisplayed(),
                "Selected Test is not displayed in the cart.");
    }

    private TestsPage testsPage() {
        return new TestsPage(DriverFactory.getDriver());
    }
}