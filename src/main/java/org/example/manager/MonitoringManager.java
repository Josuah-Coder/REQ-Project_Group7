package org.example.manager;

import org.example.model.Location;
import org.example.model.ChargingPoint;
import org.example.model.enums.OperationalStatus;
import java.util.*;

public class MonitoringManager {
    private static MonitoringManager instance;

    private MonitoringManager() {}

    public static synchronized MonitoringManager getInstance() {
        if (instance == null) {
            instance = new MonitoringManager();
        }
        return instance;
    }

    public List<Location> filterLocations(String region, String availability,
                                          String chargerType, String lastHour,
                                          String maintenanceStatus) {
        List<Location> allLocations = LocationManager.getInstance().getAllLocations();
        List<Location> filtered = new ArrayList<>();

        for (Location loc : allLocations) {
            boolean matches = true;

            if (region != null && !loc.getCity().contains(region)) {
                matches = false;
            }

            if (chargerType != null) {
                boolean hasType = false;
                for (ChargingPoint cp : loc.getChargingPoints()) {
                    if (chargerType.equals(cp.getType().getDisplayName())) {
                        hasType = true;
                        break;
                    }
                }
                if (!hasType) {
                    matches = false;
                }
            }

            if (matches) {
                filtered.add(loc);
            }
        }

        return filtered;
    }

    public Map<String, Object> calculateDashboardMetrics(List<Location> locations) {
        Map<String, Object> metrics = new HashMap<>();

        int totalDcPoints = 0;
        int availableDcPoints = 0;
        int activeSessions = 0;

        for (Location loc : locations) {
            for (ChargingPoint cp : loc.getChargingPoints()) {
                if ("DC".equals(cp.getType().getDisplayName())) {
                    totalDcPoints++;
                    if (OperationalStatus.AVAILABLE.equals(cp.getStatus())) {
                        availableDcPoints++;
                    }
                }
            }
        }

        int dcAvailability = totalDcPoints > 0 ? (availableDcPoints * 100) / totalDcPoints : 0;

        metrics.put("filteredLocations", locations.size());
        metrics.put("totalDcPoints", totalDcPoints);
        metrics.put("dcAvailability", dcAvailability);
        metrics.put("activeSessions", activeSessions);

        return metrics;
    }

    public Map<String, String> getLocationStatus(String locationId) {
        Map<String, String> status = new HashMap<>();
        Location loc = LocationManager.getInstance().getLocationById(locationId);

        if (loc != null) {
            int total = loc.getChargingPoints().size();
            int available = 0;
            for (ChargingPoint cp : loc.getChargingPoints()) {
                if (OperationalStatus.AVAILABLE.equals(cp.getStatus())) {
                    available++;
                }
            }

            int availability = total > 0 ? (available * 100) / total : 0;

            status.put("totalPoints", String.valueOf(total));
            status.put("availablePoints", String.valueOf(available));
            status.put("availability", availability + "%");
        }

        return status;
    }
}
