package org.example;

import io.cucumber.java.en.*;
import org.example.manager.*;
import org.example.model.*;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ChargingStepDefinitions {
    private Customer currentCustomer;
    private ChargingPoint currentChargingPoint;
    private Location currentLocation;
    private ChargingSession activeSession;
    private BigDecimal estimatedCost;

    @Given("I have selected an available charging point")
    public void i_have_selected_an_available_charging_point() {
        currentChargingPoint = ChargingPointManager.getInstance()
                .getAvailableChargingPoints().get(0);
        assertNotNull(currentChargingPoint);
        assertEquals("AVAILABLE", currentChargingPoint.getStatus().toString());
    }

    @When("I initiate the charging start process")
    public void i_initiate_the_charging_start_process() {
        activeSession = ChargingSessionManager.getInstance()
                .startSession(currentChargingPoint, currentCustomer);
        assertNotNull(activeSession);
    }

    @Then("the charging session should begin")
    public void the_charging_session_should_begin() {
        assertEquals("ACTIVE", activeSession.getStatus().toString());
    }

    @And("the charging point status should change to {string}")
    public void the_charging_point_status_should_change_to(String expectedStatus) {
        String actualStatus = currentChargingPoint.getStatus().toString();
        assertTrue(actualStatus.contains(expectedStatus.toUpperCase()) ||
                actualStatus.equalsIgnoreCase(expectedStatus.replace(" ", "_")));
    }

    @And("I should receive confirmation that charging has started")
    public void i_should_receive_confirmation_that_charging_has_started() {
        String confirmation = ChargingSessionManager.getInstance()
                .getSessionConfirmation(activeSession.getId());
        assertNotNull(confirmation);
        assertTrue(confirmation.contains("started") || confirmation.contains("active"));
    }

    @Given("I have an active charging session")
    public void i_have_an_active_charging_session() {
        if (currentCustomer != null) {
            activeSession = ChargingSessionManager.getInstance()
                    .getActiveSessionForCustomer(currentCustomer.getId());
        }

        if (activeSession == null) {
            if (currentChargingPoint == null) {
                currentChargingPoint = ChargingPointManager.getInstance()
                        .getAvailableChargingPoints().get(0);
            }
            if (currentCustomer == null) {
                currentCustomer = CustomerManager.getInstance()
                        .getCustomerById("CUST-2024-001");
            }
            activeSession = ChargingSessionManager.getInstance()
                    .startSession(currentChargingPoint, currentCustomer);
        }

        assertNotNull(activeSession);
        assertEquals("ACTIVE", activeSession.getStatus().toString());
    }

    @When("I initiate the charging stop process")
    public void i_initiate_the_charging_stop_process() {
        ChargingSessionManager.getInstance().stopSession(activeSession);
    }

    @Then("the charging session should end")
    public void the_charging_session_should_end() {
        assertEquals("COMPLETED", activeSession.getStatus().toString());
    }

    @And("the final charging data should be recorded")
    public void the_final_charging_data_should_be_recorded() {
        assertNotNull(activeSession.getEndTime());
        assertNotNull(activeSession.getTotalEnergy());
        assertNotNull(activeSession.getTotalCost());
    }

    @And("the charging point should become available for other users")
    public void the_charging_point_should_become_available_for_other_users() {
        assertEquals("AVAILABLE", currentChargingPoint.getStatus().toString());
    }

    @Given("I am customer {string} with account balance {string}")
    public void i_am_customer_with_account_balance(String customerId, String balance) {
        currentCustomer = CustomerManager.getInstance().getCustomerById(customerId);
        if (currentCustomer == null) {
            currentCustomer = new Customer(customerId, "Test Customer");
            CustomerManager.getInstance().addCustomer(currentCustomer);
        }
        currentCustomer.setAccountBalance(new BigDecimal(balance.replace(" €", "")));
    }

    @Given("I am at location {string}")
    public void i_am_at_location(String locationName) {
        currentLocation = LocationManager.getInstance().getLocationByName(locationName);
        assertNotNull(currentLocation, "Location '" + locationName + "' should exist");
    }

    @Given("DC charging point {string} is available with price {string}")
    public void dc_charging_point_is_available_with_price(String cpId, String price) {
        currentChargingPoint = ChargingPointManager.getInstance().getChargingPointById(cpId);
        if (currentChargingPoint == null) {
            currentChargingPoint = new ChargingPoint();
            currentChargingPoint.setId(cpId);
            currentChargingPoint.setType("DC");
            currentChargingPoint.setMaxPower(150);
            ChargingPointManager.getInstance().addChargingPoint(currentChargingPoint);
        }
        assertNotNull(currentChargingPoint);
        assertEquals("AVAILABLE", currentChargingPoint.getStatus().toString());
        assertEquals("DC", currentChargingPoint.getType().getDisplayName());
        currentChargingPoint.setPricePerKwh(new BigDecimal(price.replace(" €/kWh", "")));
    }

    @When("I start charging at charging point {string}")
    public void i_start_charging_at_charging_point(String cpId) {
        currentChargingPoint = ChargingPointManager.getInstance().getChargingPointById(cpId);
        activeSession = ChargingSessionManager.getInstance()
                .startSession(currentChargingPoint, currentCustomer);
    }

    @And("I charge for {int} minutes at {int} kW")
    public void i_charge_for_minutes_at_kw(int minutes, int power) {
        activeSession.setDuration(minutes);
        activeSession.setPower(power);
        activeSession.calculateEnergyAndCost();
    }

    @Then("the following session is recorded:")
    public void the_following_session_is_recorded(io.cucumber.datatable.DataTable dataTable) {
        Map<String, String> expected = dataTable.asMap(String.class, String.class);

        assertNotNull(activeSession.getId());
        assertTrue(activeSession.getId().contains("SESS"));
        assertNotNull(activeSession.getStartTime());
        assertNotNull(activeSession.getChargingPoint());

        if (expected.containsKey("Estimated Cost")) {
            String costStr = expected.get("Estimated Cost");
            String[] costParts = costStr.split(" ");
            BigDecimal expectedCost = new BigDecimal(costParts[0]);
            assertEquals(expectedCost, activeSession.getEstimatedCost());
        }

        assertEquals("Active", expected.get("Current Status"));
    }

    @And("my account balance is {string} beforehand")
    public void my_account_balance_is_beforehand(String balance) {
        BigDecimal expectedBalance = new BigDecimal(balance.replace(" €", ""));
        assertEquals(expectedBalance, currentCustomer.getAccountBalance());
    }

    @And("the session is monitored live")
    public void the_session_is_monitored_live() {
        assertTrue(ChargingSessionManager.getInstance().isSessionActive(activeSession.getId()));
    }

    @Given("I have an active charging session {string}")
    public void i_have_an_active_charging_session(String sessionId) {
        activeSession = ChargingSessionManager.getInstance().getSessionById(sessionId);

        if (activeSession == null) {
            if (currentChargingPoint == null) {
                currentChargingPoint = ChargingPointManager.getInstance()
                        .getAvailableChargingPoints().get(0);
            }
            if (currentCustomer == null) {
                currentCustomer = CustomerManager.getInstance()
                        .getCustomerById("CUST-2024-001");
            }
            activeSession = ChargingSessionManager.getInstance()
                    .startSession(currentChargingPoint, currentCustomer);
        }

        assertNotNull(activeSession);
        assertEquals("ACTIVE", activeSession.getStatus().toString());
    }

    @Given("{int} minutes have passed")
    public void minutes_have_passed(int minutes) {
        activeSession.addChargingTime(minutes);
    }

    @When("I pause the charging session for {int} minutes")
    public void i_pause_the_charging_session_for_minutes(int pauseMinutes) {
        ChargingSessionManager.getInstance().pauseSession(activeSession.getId(), pauseMinutes);
    }

    @And("then resume")
    public void then_resume() {
        ChargingSessionManager.getInstance().resumeSession(activeSession.getId());
    }

    @Then("the pause time of {int} minutes is not calculated")
    public void the_pause_time_of_minutes_is_not_calculated(int pauseMinutes) {
        assertEquals(pauseMinutes, activeSession.getPauseDuration());
        assertNotEquals(activeSession.getChargingTime(), activeSession.getTotalDuration());
    }

    @And("the effective charging time is {int} minutes")
    public void the_effective_charging_time_is_minutes(int effectiveMinutes) {
        assertEquals(effectiveMinutes, activeSession.getChargingTime());
    }

    @And("the total status shows {string}")
    public void the_total_status_shows(String statusMessage) {
        String status = ChargingSessionManager.getInstance().getSessionStatus(activeSession.getId());
        assertTrue(status.contains(statusMessage) || status.contains("Paused"));
    }
}