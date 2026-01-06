package org.example;

import io.cucumber.java.en.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.*;
import org.example.model.*;
import org.example.manager.*;

public class MonitoringStepDefinitions {
    private Object currentOperator;
    private List<Location> filteredLocations;
    private Map<String, Object> dashboardMetrics;

    @Given("I am responsible for operational monitoring")
    public void i_am_responsible_for_operational_monitoring() {
        currentOperator = OperatorManager.getInstance().getCurrentOperator();
        assertNotNull(currentOperator);
    }

    @When("I access the status dashboard")
    public void i_access_the_status_dashboard() {

        List<ChargingPoint> allPoints = ChargingPointManager.getInstance().getAllChargingPoints();
        assertNotNull(allPoints);
    }

    @Then("I should see the current status of all charging points")
    public void i_should_see_the_current_status_of_all_charging_points() {
        List<ChargingPoint> points = ChargingPointManager.getInstance().getAllChargingPoints();
        assertFalse(points.isEmpty());

        for (ChargingPoint point : points) {
            assertNotNull(point.getStatus());
            assertFalse(point.getStatus().isEmpty());
        }
    }

    @And("I should be able to identify unavailable or faulty charging points")
    public void i_should_be_able_to_identify_unavailable_or_faulty_charging_points() {
        List<ChargingPoint> problematic = ChargingPointManager.getInstance()
                .getChargingPointsByStatus("MAINTENANCE");
        problematic.addAll(ChargingPointManager.getInstance()
                .getChargingPointsByStatus("FAULTY"));


        assertNotNull(problematic);
    }

    @Given("I need to review the pricing strategy")
    public void i_need_to_review_the_pricing_strategy() {

    }

    @When("I view the price overview")
    public void i_view_the_price_overview() {
        List<PricingProfile> allPrices = PricingManager.getInstance().getAllPricingProfiles();
        assertNotNull(allPrices);
    }

    @Then("I should see all current prices per location")
    public void i_should_see_all_current_prices_per_location() {
        Map<String, PricingProfile> pricesByLocation = PricingManager.getInstance()
                .getPricesByLocation();
        assertFalse(pricesByLocation.isEmpty());
    }

    @And("I should be able to compare pricing across different locations")
    public void i_should_be_able_to_compare_pricing_across_different_locations() {
        List<PricingProfile> allProfiles = PricingManager.getInstance().getAllPricingProfiles();
        assertTrue(allProfiles.size() >= 2, "Need at least 2 locations to compare");


        BigDecimal avgPrice1 = allProfiles.get(0).getAveragePrice();
        BigDecimal avgPrice2 = allProfiles.get(1).getAveragePrice();
        assertNotNull(avgPrice1);
        assertNotNull(avgPrice2);
    }

    @Given("I open the status dashboard")
    public void i_open_the_status_dashboard() {

    }

    @Given("the network has {int} locations with {int} charging points")
    public void the_network_has_locations_with_charging_points(int locationCount, int pointCount) {
        List<Location> locations = LocationManager.getInstance().getAllLocations();
        assertEquals(locationCount, locations.size());

        int totalPoints = locations.stream()
                .mapToInt(l -> l.getChargingPoints().size())
                .sum();
        assertEquals(pointCount, totalPoints);
    }

    @When("I apply the following monitoring filters:")
    public void i_apply_the_following_monitoring_filters(io.cucumber.datatable.DataTable dataTable) {
        Map<String, String> filters = dataTable.asMap(String.class, String.class);

        filteredLocations = MonitoringManager.getInstance().filterLocations(
                filters.get("Region"),
                filters.get("Availability"),
                filters.get("Charger Type"),
                filters.get("Last Hour"),
                filters.get("Maintenance Status")
        );
    }

    @Then("I see {int} locations in filtered results:")
    public void i_see_locations_in_filtered_results(int expectedCount,
                                                    io.cucumber.datatable.DataTable dataTable) {
        assertEquals(expectedCount, filteredLocations.size());

        List<Map<String, String>> expectedRows = dataTable.asMaps();
        for (int i = 0; i < expectedRows.size(); i++) {
            Map<String, String> expected = expectedRows.get(i);
            Location actual = filteredLocations.get(i);

            assertEquals(expected.get("Location"), actual.getName());

        }
    }

    @And("the overview shows:")
    public void the_overview_shows(io.cucumber.datatable.DataTable dataTable) {
        dashboardMetrics = MonitoringManager.getInstance()
                .calculateDashboardMetrics(filteredLocations);

        Map<String, String> expected = dataTable.asMap(String.class, String.class);

        assertEquals(Integer.parseInt(expected.get("Filtered Locations")),
                dashboardMetrics.get("filteredLocations"));
        assertEquals(Integer.parseInt(expected.get("Total DC Points")),
                dashboardMetrics.get("totalDcPoints"));
        assertEquals(expected.get("DC Availability"),
                dashboardMetrics.get("dcAvailability") + "%");
        assertEquals(Integer.parseInt(expected.get("Active Sessions")),
                dashboardMetrics.get("activeSessions"));
    }
}
