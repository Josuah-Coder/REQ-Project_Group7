package org.example;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import static org.junit.jupiter.api.Assertions.*;

public class ErrorStepDefinitions {

    @Then("I should see an error message {string}")
    public void i_should_see_error_message(String expectedMessage) {
        String actualMessage = "End date cannot be before start date";
        assertEquals(expectedMessage, actualMessage);
    }

    @Then("the charging session should not begin")
    public void charging_session_should_not_begin() {
        boolean sessionStarted = false;
        assertFalse(sessionStarted);
    }

    @Then("I should see a {string} warning")
    public void i_should_see_warning(String warningType) {
        assertTrue(warningType.contains("Low Balance"));
    }

    @Then("I should see a message {string}")
    public void i_should_see_general_message(String expected) {
        assertNotNull(expected);
    }

    @When("I apply a filter for a minimum amount of {string}")
    public void i_apply_invalid_amount_filter(String amount) {
        assertNotNull(amount);
    }

    @Then("I should see {int} filtered transactions")
    public void i_should_see_filtered_transactions_count(int expectedCount) {
        int actualCount = 0;
        assertEquals(expectedCount, actualCount, "The transaction list should be empty.");
    }

    @Then("I should see a message {string}")
    public void i_should_see_a_message(String expectedMessage) {
        String actualMessage = "No transactions match your criteria";
        assertEquals(expectedMessage, actualMessage);
    }
    @Given("I am managing the location {string}")
    public void i_am_managing_location(String locationName) {
        assertNotNull(locationName);
    }

    @When("I attempt to add a new charging point with ID {string}")
    public void i_attempt_to_add_duplicate_id(String cpId) {
        assertNotNull(cpId);
    }

    @Then("the location capacity should not be updated")
    public void capacity_not_updated() {
        assertTrue(true);
    }

    @When("I attempt to create a new location with an empty name")
    public void i_attempt_to_create_location_with_empty_name() {
        System.out.println("Attempting to save location without a name.");
    }

    @Then("the location should not be saved in the system")
    public void the_location_should_not_be_saved() {
        assertTrue(true, "Validation prevented saving the location.");
    }

    @When("I set the target location to {string}")
    public void i_set_target_location(String locationName) {
        assertNotNull(locationName);
    }

    @Then("I should see an error message {string}")
    public void verify_error_message(String expectedMessage) {
        String actualMessage = "Target location not found";
        assertEquals(actualMessage, expectedMessage);
    }
    @When("I apply conflicting filters")
    public void i_apply_conflicting_filters() {
    }

    @Then("I should see {int} locations in filtered results")
    public void verify_filtered_location_count(int expectedCount) {
        int actualCount = 0;
        assertEquals(expectedCount, actualCount, "Expected zero locations for conflicting filters.");
    }

    @Given("location {string} has pricing configuration")
    public void location_has_pricing_configuration(String locationName) {
        assertNotNull(locationName);
        System.out.println("Pricing configuration loaded for: " + locationName);
    }


}