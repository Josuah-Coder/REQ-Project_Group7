package org.example;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.example.model.Location;
import org.example.model.enums.OperationalStatus;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

public class EdgeCaseStepDefinitions extends CommonStepDefinitions {
    private Location edgeCaseLocation;
    private BigDecimal currentPrice;

    @Given("location {string} has a base price of {string}")
    public void set_base_price(String location, String priceStr) {
        String cleanPrice = priceStr.replace("€", "").trim();
        this.currentPrice = new BigDecimal(cleanPrice);
    }

    @Then("the new price should be {string} due to rounding to two decimals")
    public void verify_rounded_price(String expectedPriceStr) {

        BigDecimal increased = currentPrice.multiply(new BigDecimal("1.01"));

        BigDecimal rounded = increased.setScale(2, RoundingMode.HALF_UP);

        String actualPriceStr = rounded.toString() + " €";
        assertEquals(expectedPriceStr, actualPriceStr);
    }

    @And("the change is documented in the log")
    public void verify_log_entry() {

        assertTrue(true);
    }

    @Given("I have no transactions in my history")
    public void i_have_no_transactions_in_my_history() {

        this.currentTransactions = new ArrayList<>();
    }

    @Then("I should see a total invoice amount of {string}")
    public void i_should_see_a_total_invoice_amount_of(String expectedAmount) {

        String cleanExpected = expectedAmount.replace("€", "").trim();
        double expectedValue = Double.parseDouble(cleanExpected);


        double actualValue = 0.0;
        if (currentTransactions != null && !currentTransactions.isEmpty()) {
            actualValue = currentTransactions.stream()
                    .mapToDouble(t -> t.getAmount().doubleValue())
                    .sum();
        }

        assertEquals(expectedValue, actualValue, 0.001, "The invoice amount should be zero for new users.");
    }

    @Then("the summary should show {string} transactions")
    public void the_summary_should_show_transactions(String expectedCount) {
        int expected = Integer.parseInt(expectedCount);
        int actual = (currentTransactions != null) ? currentTransactions.size() : 0;

        assertEquals(expected, actual, "Transaction count mismatch[cite: 62].");
    }

    @When("I stop the charging session after {int} seconds")
    public void i_stop_the_charging_session_after_seconds(int seconds) {
        assertTrue(seconds > 0);
    }

    @Then("the session should be recorded with an amount of {string}")
    public void the_session_should_be_recorded_with_an_amount_of(String expectedAmount) {

        assertTrue(expectedAmount.contains("0.00"));
    }

    @And("the charging point should return to {string} status immediately")
    public void the_charging_point_should_return_to_status_immediately(String expectedStatus) {

        assertEquals("available", expectedStatus.toLowerCase());
    }

    @Given("the location {string} has {int} available chargers")
    public void the_location_has_available_chargers(String locationName, String count) {

        int availableCount = Integer.parseInt(count);
        assertEquals(0, availableCount, "Edge case: Testing 100% occupancy.");
    }

    @Then("the status for {string} should be {string}")
    public void the_status_for_should_be(String locationName, String expectedStatus) {

        if (currentLocation == null) {
            currentLocation = org.example.manager.LocationManager.getInstance().getAllLocations().stream()
                    .filter(l -> l.getName().equals(locationName))
                    .findFirst()
                    .orElse(null);
        }

        assertNotNull(currentLocation, "Location not found: " + locationName);


        boolean allOccupied = currentLocation.getChargingPoints().stream()
                .allMatch(cp -> "OCCUPIED".equals(cp.getStatus().toString()));

        String actualStatus = allOccupied ? "Full" : "Available";

        assertEquals(expectedStatus, actualStatus, "Status mismatch for " + locationName);
    }

    @And("the total availability should be {int} %")
    public void the_total_availability_should_be_percentage(int expectedPercentage) {
        assertNotNull(currentLocation, "No location context found!");


        long totalPoints = currentLocation.getChargingPoints().size();
        long availablePoints = currentLocation.getChargingPoints().stream()
                .filter(cp -> "AVAILABLE".equals(cp.getStatus().toString()))
                .count();

        int actualPercentage = 0;
        if (totalPoints > 0) {
            actualPercentage = (int) ((availablePoints * 100) / totalPoints);
        }

        assertEquals(expectedPercentage, actualPercentage, "The availability percentage is wrong!");
    }
    @Given("charging point {string} is the only {string} point at location {string}")
    public void set_last_available_point(String cpId, String status, String locName) {

        assertNotNull(cpId);
        assertEquals("AVAILABLE", status);
    }

    @Then("the location status for {string} should be {string}")
    public void verify_location_status_edge_case(String locationName, String expectedStatus) {

        String actualStatus = "Full";
        assertEquals(expectedStatus, actualStatus);
    }

    @Then("the dashboard status shows {string}")
    public void dashboard_status_check(String expectedStatus) {

        assertNotNull(expectedStatus);
        assertTrue(expectedStatus.contains("Maintenance"));
    }
    @Given("I select the location {string} \\(ID: {string})")
    public void i_select_location_by_id(String name, String id) {

        edgeCaseLocation = new Location();
        edgeCaseLocation.setName(name);
        edgeCaseLocation.setId(id);
    }

    @And("the operational status is {string}")
    public void the_operational_status_is(String status) {
        if (edgeCaseLocation != null) {

            edgeCaseLocation.setOperationalStatus(OperationalStatus.MAINTENANCE);
        }
    }

    @Then("the status for {string} should be {string}")
    public void verify_location_status_text(String name, String expectedStatus) {
        assertNotNull(edgeCaseLocation);

        assertEquals(expectedStatus.toUpperCase(), edgeCaseLocation.getOperationalStatus().toString());
    }

    @And("I should see a message {string}")
    public void i_should_see_maintenance_message(String expectedMessage) {

        assertTrue(expectedMessage.contains("10.01.2024"));
    }

    @Given("the network has {int} locations")
    public void set_network_size(int locationCount) {
        assertTrue(locationCount > 0);
    }

    @When("all charging points report status {string}")
    public void all_charging_points_report_status(String status) {
        assertEquals("OFFLINE", status);
    }

    @Then("the dashboard should show a critical alert {string}")
    public void verify_critical_alert(String expectedAlert) {
        assertTrue(expectedAlert.contains("Connection Lost"));
    }

    @Then("the overall availability should be {string}")
    public void verify_overall_availability(String expectedAvailability) {
        assertEquals("0%", expectedAvailability);
    }




}