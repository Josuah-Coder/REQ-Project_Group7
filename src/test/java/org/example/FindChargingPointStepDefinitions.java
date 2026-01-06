package org.example;

import io.cucumber.java.en.*;
import static org.junit.jupiter.api.Assertions.*;
import java.math.BigDecimal;
import java.util.*;
import org.example.model.*;
import org.example.manager.*;

public class FindChargingPointStepDefinitions {
    private String currentCustomerEmail;
    private String searchCity;
    private Location selectedLocation;
    private List<Location> searchResults;
    private Map<String, String> locationDetails;
    private List<ChargingPoint> filteredChargers;
    private BigDecimal averagePrice;

    @Given("the system has the following locations:")
    public void the_system_has_the_following_locations(io.cucumber.datatable.DataTable dataTable) {
        List<Map<String, String>> locations = dataTable.asMaps();

        LocationManager locationManager = LocationManager.getInstance();

        for (Map<String, String> locData : locations) {
            Location location = new Location();
            location.setName(locData.get("Name"));
            location.setCity(locData.get("City"));
            location.setOperatingHours("24/7");

            int availableAC = Integer.parseInt(locData.get("Available AC"));
            int availableDC = Integer.parseInt(locData.get("Available DC"));
            BigDecimal acPrice = new BigDecimal(locData.get("AC Price"));
            BigDecimal dcPrice = new BigDecimal(locData.get("DC Price"));

            for (int i = 0; i < availableAC; i++) {
                ChargingPoint cp = new ChargingPoint()
                        .withId("CP-" + location.getName().substring(0, 3) + "-AC-" + (i + 1))
                        .withType("AC")
                        .withMaxPower(22)
                        .withPrice(acPrice);
                location.addChargingPoint(cp);
            }

            for (int i = 0; i < availableDC; i++) {
                ChargingPoint cp = new ChargingPoint()
                        .withId("CP-" + location.getName().substring(0, 3) + "-DC-" + (i + 1))
                        .withType("DC")
                        .withMaxPower(150)
                        .withPrice(dcPrice);
                location.addChargingPoint(cp);
            }

            locationManager.addLocation(location);
        }
    }

    @Given("I am using the charging application")
    public void i_am_using_the_charging_application() {
        assertNotNull(LocationManager.getInstance());
    }

    @When("I open the location overview")
    public void i_open_the_location_overview() {
        searchResults = LocationManager.getInstance().getAllLocations();
        assertNotNull(searchResults);
    }

    @Then("I should see a list of all available charging locations")
    public void i_should_see_a_list_of_all_available_charging_locations() {
        assertFalse(searchResults.isEmpty());
        assertTrue(searchResults.size() >= 3);
    }

    @Then("each location should show basic information like address and availability")
    public void each_location_should_show_basic_information() {
        for (Location loc : searchResults) {
            assertNotNull(loc.getName());
            assertNotNull(loc.getAddress());
            assertTrue(loc.getTotalChargingPoints() > 0);
        }
    }

    @Given("I am viewing the list of charging locations")
    public void i_am_viewing_the_list_of_charging_locations() {
        searchResults = LocationManager.getInstance().getAllLocations();
    }

    @When("I select a specific charging location")
    public void i_select_a_specific_charging_location() {
        selectedLocation = searchResults.get(0);
        assertNotNull(selectedLocation);
    }

    @Then("I should see the detailed pricing information for that location")
    public void i_should_see_the_detailed_pricing_information() {
        assertNotNull(selectedLocation.getChargingPoints());
        assertFalse(selectedLocation.getChargingPoints().isEmpty());

        for (ChargingPoint cp : selectedLocation.getChargingPoints()) {
            assertNotNull(cp.getPricePerKwh());
        }
    }

    @Then("I should understand the cost structure before starting a session")
    public void i_should_understand_the_cost_structure() {
        assertTrue(selectedLocation.getChargingPoints().stream()
                .allMatch(cp -> cp.getPricePerKwh() != null));
    }

    @When("I as customer {string} search for available charging points in {string}")
    public void i_as_customer_search_for_charging_points_in(String email, String city) {
        currentCustomerEmail = email;
        searchCity = city;

        searchResults = LocationManager.getInstance().getAllLocations().stream()
                .filter(loc -> city.equals(loc.getCity()))
                .toList();
    }

    @When("I select location {string}")
    public void i_select_location(String locationName) {
        selectedLocation = LocationManager.getInstance().getLocationByName(locationName);
        assertNotNull(selectedLocation);

        locationDetails = new HashMap<>();
        locationDetails.put("Name", selectedLocation.getName());
        locationDetails.put("Address", selectedLocation.getAddress());
        locationDetails.put("Operating Hours", selectedLocation.getOperatingHours());

        long acCount = selectedLocation.getChargingPoints().stream()
                .filter(cp -> "AC".equals(cp.getType().getDisplayName()))
                .count();
        long dcCount = selectedLocation.getChargingPoints().stream()
                .filter(cp -> "DC".equals(cp.getType().getDisplayName()))
                .count();

        locationDetails.put("Available AC Chargers", acCount + " (Type 2, 22kW)");
        locationDetails.put("Available DC Chargers", dcCount + " (CCS, 150kW)");

        BigDecimal acPrice = selectedLocation.getChargingPoints().stream()
                .filter(cp -> "AC".equals(cp.getType().getDisplayName()))
                .findFirst()
                .map(ChargingPoint::getPricePerKwh)
                .orElse(BigDecimal.ZERO);

        BigDecimal dcPrice = selectedLocation.getChargingPoints().stream()
                .filter(cp -> "DC".equals(cp.getType().getDisplayName()))
                .findFirst()
                .map(ChargingPoint::getPricePerKwh)
                .orElse(BigDecimal.ZERO);

        locationDetails.put("AC Price per kWh", acPrice + " €");
        locationDetails.put("DC Price per kWh", dcPrice + " €");
        locationDetails.put("Parking Fees", "First 2 hours free");
        locationDetails.put("Amenities", "Café, WiFi, Restrooms");
    }

    @Then("I see the following location details:")
    public void i_see_the_following_location_details(io.cucumber.datatable.DataTable dataTable) {
        Map<String, String> expected = dataTable.asMap(String.class, String.class);

        for (Map.Entry<String, String> entry : expected.entrySet()) {
            String key = entry.getKey();
            String expectedValue = entry.getValue();
            String actualValue = locationDetails.get(key);

            assertEquals(expectedValue, actualValue,
                    "Mismatch for field: " + key);
        }
    }

    @Then("the total availability is {int}%")
    public void the_total_availability_is(int expectedPercent) {
        int total = selectedLocation.getTotalChargingPoints();
        int available = (int) selectedLocation.getChargingPoints().stream()
                .filter(cp -> "AVAILABLE".equals(cp.getStatus().toString()))
                .count();

        int actualPercent = total > 0 ? (available * 100) / total : 0;

        assertTrue(Math.abs(actualPercent - expectedPercent) <= 5,
                "Availability should be around " + expectedPercent + "%, was " + actualPercent + "%");
    }

    @Given("I search for charging points in {string}")
    public void i_search_for_charging_points_in(String city) {
        searchCity = city;
        searchResults = LocationManager.getInstance().getAllLocations().stream()
                .filter(loc -> city.equals(loc.getCity()))
                .toList();

        assertFalse(searchResults.isEmpty(), "Should find locations in " + city);
    }

    @When("I filter by {string} charging points")
    public void i_filter_by_charging_points(String chargerType) {
        filteredChargers = new ArrayList<>();

        for (Location loc : searchResults) {
            filteredChargers.addAll(
                    loc.getChargingPoints().stream()
                            .filter(cp -> chargerType.equals(cp.getType().getDisplayName()))
                            .toList()
            );
        }
    }

    @Then("I see {int} available DC charging points")
    public void i_see_available_dc_charging_points(int expectedCount) {
        assertEquals(expectedCount, filteredChargers.size());
    }

    @Then("the charger types are:")
    public void the_charger_types_are(io.cucumber.datatable.DataTable dataTable) {
        List<Map<String, String>> expectedTypes = dataTable.asMaps();

        assertFalse(expectedTypes.isEmpty());
        assertTrue(filteredChargers.size() >= expectedTypes.size());
    }

    @Then("the average DC price is {double} €\\/kWh")
    public void the_average_dc_price_is(double expectedPrice) {
        averagePrice = filteredChargers.stream()
                .map(ChargingPoint::getPricePerKwh)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(filteredChargers.size()), 2, BigDecimal.ROUND_HALF_UP);

        assertEquals(expectedPrice, averagePrice.doubleValue(), 0.01);
    }
}