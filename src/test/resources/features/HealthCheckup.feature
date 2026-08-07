@HealthCheckup
@Smoke
@Regression

Feature: Book a Health Checkup Package

  Background:
    Given the user launches the Orange Health application

  Scenario: Verify user can book a Full Body Checkup package

    When the user clicks the Checkups navigation menu

    Then the Health Checkups page should be displayed

    When the user scrolls to the Full Body Checkups section

    And the user clicks the View Details button of the first Full Body Checkup

    Then the Full Body Checkup Basic page should be displayed

    When the user double-clicks the Add to Cart button

    Then the Cart Drawer should be displayed

    And the selected Full Body Checkup should be displayed in the cart

    When the user clicks the Proceed button

    Then the Sign in to Continue page should be displayed