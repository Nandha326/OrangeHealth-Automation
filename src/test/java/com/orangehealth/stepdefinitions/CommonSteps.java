package com.orangehealth.stepdefinitions;

import static org.testng.Assert.assertTrue;

import com.orangehealth.base.DriverFactory;
import com.orangehealth.pages.CartPage;
import com.orangehealth.pages.HomePage;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class CommonSteps {

    private CartPage cartPage;
    private HomePage homePage;

    @When("the user clicks the Add to Cart button")
    public void theUserClicksTheAddToCartButton() {
        cartPage().addPackageToCart();
    }

    @Then("the Cart Drawer should be displayed")
    public void theCartDrawerShouldBeDisplayed() {
        assertTrue(
                cartPage().isCartDrawerDisplayed(),
                "Cart Drawer is not displayed.");
    }

    @When("the user clicks the Proceed button")
    public void theUserClicksTheProceedButton() {
        if (cartPage().isCartDrawerOpen()) {
            cartPage().clickProceed();
            return;
        }
        homePage().clickProceedButton();
    }

    @Then("the Sign in to Continue page should be displayed")
    public void theSignInToContinuePageShouldBeDisplayed() {
        assertTrue(
                cartPage().isLoginPageDisplayed(),
                "Sign in to Continue page is not displayed.");
    }

    private CartPage cartPage() {
        if (cartPage == null) {
            cartPage = new CartPage(DriverFactory.getDriver());
        }
        return cartPage;
    }

    private HomePage homePage() {
        if (homePage == null) {
            homePage = new HomePage(DriverFactory.getDriver());
        }
        return homePage;
    }
}
