package org.example;

import io.cucumber.java.en.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import java.util.Map;

import org.example.model.*;
import org.example.manager.*;

public class LocationStepDefinitions {
    private Location currentLocation;
    private Location newLocation;

    @When("I create a new charging location with all required details")
    public void i_create_a_new_charging_location_with_all_required_details() {
        newLocation = new Location();
        newLocation.setName("New Test Location");
        newLocation.setAddress("Test Street 123, Berlin");
        newLocation.setOperatingHours("24/7");
        newLocation.setContactPerson("Test Contact");
        newLocation.setContactEmail("test@energy.de");

        LocationManager.getInstance().addLocation(newLocation);
    }

    @Then("the location should be saved in the system")
    public void the_location_should_be_saved_in_the_system() {
        Location saved = LocationManager.getInstance().getLocationById(newLocation.getId());
        assertNotNull(saved);
        assertEquals(newLocation.getName(), saved.getName());
    }

    @And("the location should become visible in the location overview")
    public void the_location_should_become_visible_in_the_location_overview() {
        List<Location> allLocations = LocationManager.getInstance().getAllLocations();
        assertTrue(allLocations.stream()
                .anyMatch(l -> l.getId().equals(newLocation.getId())));
    }

    @And("I should be able to add charging points to this location")
    public void i_should_be_able_to_add_charging_points_to_this_location() {
        ChargingPoint cp = new ChargingPoint();
        cp.setId("CP-TEST-01");
        cp.setType("AC");
        cp.setMaxPower(22);

        newLocation.addChargingPoint(cp);
        assertTrue(newLocation.getChargingPoints().contains(cp));
    }

    @Given("I have an existing charging location in the system")
    public void i_have_an_existing_charging_location_in_the_system() {
        currentLocation = LocationManager.getInstance().getAllLocations().get(0);
        assertNotNull(currentLocation);
    }

    @When("I modify the location details \\(address, description, etc.)")
    public void i_modify_the_location_details_address_description_etc() {
        currentLocation.setAddress("Updated Address 456, Berlin");
        currentLocation.setDescription("Updated description for testing");
        LocationManager.getInstance().updateLocation(currentLocation);
    }

    @Then("the changes should be permanently saved")
    public void the_changes_should_be_permanently_saved() {
        Location updated = LocationManager.getInstance()
                .getLocationById(currentLocation.getId());
        assertEquals("Updated Address 456, Berlin", updated.getAddress());
        assertEquals("Updated description for testing", updated.getDescription());
    }

    @And("the updated information should be immediately visible to customers")
    public void the_updated_information_should_be_immediately_visible_to_customers() {
        Location customerView = LocationManager.getInstance()
                .getLocationById(currentLocation.getId());
        assertEquals(currentLocation.getAddress(), customerView.getAddress());
    }

    @Given("I select the location {string} \\(ID: {string})")
    public void i_select_the_location_id(String locationName, String locationId) {
        currentLocation = LocationManager.getInstance().getLocationById(locationId);
        assertNotNull(currentLocation);
        assertEquals(locationName, currentLocation.getName());
    }

    @When("I open the location details")
    public void i_open_the_location_details() {

        assertNotNull(currentLocation);
    }

    @Then("I see the following information:")
    public void i_see_the_following_information(io.cucumber.datatable.DataTable dataTable) {
        Map<String, String> expected = dataTable.asMap(String.class, String.class);

        assertEquals(expected.get("Name"), currentLocation.getName());
        assertEquals(expected.get("Address"), currentLocation.getAddress());
        assertEquals(expected.get("Operating Hours"), currentLocation.getOperatingHours());
        assertEquals(expected.get("Total Charging Points"), currentLocation.getTotalChargingPoints());
        assertEquals(expected.get("Operational Status"), currentLocation.getOperationalStatus());
        assertEquals(expected.get("Created Date"), currentLocation.getCreatedDate());
        assertEquals(expected.get("Last Maintenance"), currentLocation.getLastMaintenance());
        assertEquals(expected.get("Contact Person"), currentLocation.getContactPerson());
    }

    @And("I can view all technical specifications")
    public void i_can_view_all_technical_specifications() {
        String techSpecs = currentLocation.getTechnicalSpecifications();
        assertNotNull(techSpecs);
        assertFalse(techSpecs.isEmpty());
    }
}
