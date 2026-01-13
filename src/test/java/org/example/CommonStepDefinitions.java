package org.example;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.And;
import static org.junit.jupiter.api.Assertions.*;
import java.math.BigDecimal;
import java.util.*;
import org.example.model.*;
import org.example.manager.*;

public class CommonStepDefinitions {
    protected Customer currentCustomer;
    protected Operator currentOperator;
    protected Location currentLocation;
    protected List<Transaction> currentTransactions;
    protected Map<String, String> currentStats;



    @Given("I am logged in as an operator with appropriate permissions")
    public void i_am_logged_in_as_operator_with_permissions() {

        currentOperator = OperatorManager.getInstance().getCurrentOperator();
        assertNotNull(currentOperator, "Default Operator not found");
    }

    @Given("I am logged in as operator {string}")
    public void i_am_logged_in_as_operator(String operatorEmail) {

        currentOperator = OperatorManager.getInstance().login(operatorEmail, "any_password");
        assertNotNull(currentOperator, "Login failed for operator: " + operatorEmail);
    }

    @Given("I have authenticated my account")
    public void i_have_authenticated_my_account() {

        assertTrue(true);
    }



    @And("^I select the location \"([^\"]*)\" \\(ID: ([^\"]*)\\)$")
    public void i_select_the_location(String name, String id) {

        currentLocation = LocationManager.getInstance().getLocationById(id);

        assertNotNull(currentLocation, "Location with ID " + id + " not found in LocationManager!");
        assertEquals(name, currentLocation.getName(), "Location name mismatch!");
    }

    @When("I open the location details")
    public void i_open_the_location_details() {
        assertNotNull(currentLocation, "No location selected. Please select one first.");
    }

    @Then("I see the following information:")
    public void i_see_the_following_information(io.cucumber.datatable.DataTable dataTable) {
        Map<String, String> expected = dataTable.asMap(String.class, String.class);


        assertEquals(expected.get("Name"), currentLocation.getName());
        assertEquals(expected.get("Address"), currentLocation.getAddress());
        assertEquals(expected.get("Operating Hours"), currentLocation.getOperatingHours());

        if (expected.containsKey("Contact Person")) {
            assertEquals(expected.get("Contact Person"),
                    currentLocation.getContactPerson() + " (" + currentLocation.getContactEmail() + ")");
        }
    }

    @And("I can view all technical specifications")
    public void i_can_view_all_technical_specifications() {
        assertNotNull(currentLocation.getChargingPoints());
        assertFalse(currentLocation.getChargingPoints().isEmpty());
    }






}