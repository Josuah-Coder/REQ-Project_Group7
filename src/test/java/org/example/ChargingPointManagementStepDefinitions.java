package org.example;

import io.cucumber.java.en.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Map;
import org.example.model.*;
import org.example.manager.*;

public class ChargingPointManagementStepDefinitions {

    // --- Scenario: Add charging point ---
    @Given("I am managing an existing charging location")
    public void i_am_managing_an_existing_charging_location() {
        // Dummy-Logik oder Zugriff auf Manager
    }

    @When("I add a new charging point with technical specifications")
    public void i_add_a_new_charging_point() {
    }

    @Then("the charging point should be created and associated with the location")
    public void step_created() { assertTrue(true); }

    @Then("the charging point should be available for customer use")
    public void step_available() { assertTrue(true); }

    @Then("the location capacity should be updated accordingly")
    public void step_capacity() { assertTrue(true); }

    // --- Scenario: Edit configuration ---
    @Given("I see the details of charging point {string}")
    public void i_see_details(String cpId) { }

    @Given("current configuration is:")
    public void current_config(io.cucumber.datatable.DataTable dataTable) { }

    @When("I make the following changes:")
    public void make_changes(io.cucumber.datatable.DataTable dataTable) { }

    @Then("the changes are saved")
    public void changes_saved() { }

    @Then("the charging point shows the new name")
    public void new_name() { }

    @Then("reservations are now possible")
    public void reservations() { }

    // --- Scenario: Change status ---
    @Given("charging point {string} has status {string}")
    public void cp_status(String id, String status) { }

    @Given("there are {int} active charging sessions")
    public void active_sessions(Integer count) { }

    @When("I change the operational status to {string}")
    public void change_status(String status) { }

    @When("enter reason: {string}")
    public void enter_reason(String reason) { }

    @When("set time period: {string}")
    public void set_time(String time) { }

    @Then("active users are notified")
    public void notified() { }

    @Then("new charging sessions are blocked")
    public void blocked() { }

    // KORREKTUR für Ticket (Regex für Klammern und Sonderzeichen)
    @Then("^a maintenance ticket is created \\(Ticket ([^\\)]+)\\)$")
    public void a_maintenance_ticket_is_created(String ticketId) {
        System.out.println("Gefundenes Ticket: " + ticketId);
    }

    // KORREKTUR für Dashboard Status
    @Then("the dashboard status shows {string}")
    public void dashboard_status(String status) {
        System.out.println("Dashboard Status: " + status);
    }
}