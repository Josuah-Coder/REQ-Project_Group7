package org.example.manager;

import org.example.model.Location;
import org.example.model.ChargingPoint;
import java.util.*;

public class LocationManager {
    private static LocationManager instance;
    private Map<String, Location> locations;

    private LocationManager() {
        locations = new HashMap<>();
        initializeTestData();
    }

    public static synchronized LocationManager getInstance() {
        if (instance == null) {
            instance = new LocationManager();
        }
        return instance;
    }

    private void initializeTestData() {
        Location loc1 = new Location();
        loc1.setId("LOC-BER-001");
        loc1.setName("Shopping Center Berlin");
        loc1.setAddress("Alexanderplatz 1, 10178 Berlin");
        loc1.setOperatingHours("24/7");
        loc1.setContactPerson("Max Mustermann");
        loc1.setContactEmail("max@energy.de");
        loc1.setCreatedDate("15.03.2023");
        loc1.setLastMaintenance("10.01.2024");

        for (int i = 1; i <= 4; i++) {
            ChargingPoint cp = new ChargingPoint();
            cp.setId("CP-BER-AC-0" + i);
            cp.setType("AC");
            cp.setMaxPower(22);
            loc1.addChargingPoint(cp);
        }

        for (int i = 1; i <= 2; i++) {
            ChargingPoint cp = new ChargingPoint();
            cp.setId("CP-BER-DC-0" + i);
            cp.setType("DC");
            cp.setMaxPower(150);
            loc1.addChargingPoint(cp);
        }

        locations.put(loc1.getId(), loc1);

        Location loc2 = new Location();
        loc2.setId("LOC-BER-002");
        loc2.setName("Berlin-Alex");
        loc2.setAddress("Alexanderplatz, Berlin");
        loc2.setOperatingHours("24/7");
        locations.put(loc2.getId(), loc2);

        Location loc3 = new Location();
        loc3.setId("LOC-A8-001");
        loc3.setName("Highway Rest Stop A8");
        loc3.setAddress("A8 Autobahn, Augsburg");
        loc3.setOperatingHours("24/7");
        locations.put(loc3.getId(), loc3);

        Location loc4 = new Location();
        loc4.setId("LOC-MUC-001");
        loc4.setName("Munich-Center");
        loc4.setAddress("Marienplatz, Munich");
        loc4.setOperatingHours("24/7");
        locations.put(loc4.getId(), loc4);

        Location loc5 = new Location();
        loc5.setId("LOC-HAM-001");
        loc5.setName("Hamburg-Port");
        loc5.setAddress("HafenCity, Hamburg");
        loc5.setOperatingHours("24/7");
        locations.put(loc5.getId(), loc5);

        Location loc6 = new Location();
        loc6.setId("LOC-CGN-001");
        loc6.setName("Hotel Admiral Cologne");
        loc6.setAddress("Cologne City Center");
        loc6.setOperatingHours("24/7");
        locations.put(loc6.getId(), loc6);
    }

    public Location getLocationById(String id) {
        return locations.get(id);
    }

    public Location getLocationByName(String name) {
        return locations.values().stream()
                .filter(loc -> name.equals(loc.getName()))
                .findFirst()
                .orElse(null);
    }

    public List<Location> getAllLocations() {
        return new ArrayList<>(locations.values());
    }

    public void addLocation(Location location) {
        locations.put(location.getId(), location);
    }

    public void updateLocation(Location location) {
        locations.put(location.getId(), location);
    }

    public void deleteLocation(String id) {
        locations.remove(id);
    }
}