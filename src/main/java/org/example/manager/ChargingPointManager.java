package org.example.manager;

import org.example.model.ChargingPoint;
import org.example.model.enums.OperationalStatus;
import java.util.*;

public class ChargingPointManager {
    private static ChargingPointManager instance;
    private Map<String, ChargingPoint> chargingPoints;

    private ChargingPointManager() {
        chargingPoints = new HashMap<>();
        initializeTestData();
    }

    public static synchronized ChargingPointManager getInstance() {
        if (instance == null) {
            instance = new ChargingPointManager();
        }
        return instance;
    }

    private void initializeTestData() {
        ChargingPoint cp1 = new ChargingPoint();
        cp1.setId("CP-BER-DC-01");
        cp1.setName("Fast Charger Berlin-01");
        cp1.setType("DC");
        cp1.setMaxPower(150);
        cp1.setConnectorType("CCS2");
        cp1.setTariffGroup("Premium");
        cp1.setReservable(true);
        cp1.setReservationFee(new java.math.BigDecimal("5.00"));
        cp1.setStatus(OperationalStatus.AVAILABLE);
        cp1.setLocationId("LOC-BER-001");
        chargingPoints.put(cp1.getId(), cp1);

        ChargingPoint cp2 = new ChargingPoint();
        cp2.setId("CP-BER-AC-03");
        cp2.setType("AC");
        cp2.setMaxPower(22);
        cp2.setStatus(OperationalStatus.AVAILABLE);
        cp2.setLocationId("LOC-BER-001");
        chargingPoints.put(cp2.getId(), cp2);

        ChargingPoint cp3 = new ChargingPoint();
        cp3.setId("CP-A8-03");
        cp3.setType("DC");
        cp3.setMaxPower(150);
        cp3.setStatus(OperationalStatus.AVAILABLE);
        cp3.setLocationId("LOC-A8-001");
        chargingPoints.put(cp3.getId(), cp3);

        ChargingPoint cp4 = new ChargingPoint();
        cp4.setId("CP-BER-DC-02");
        cp4.setType("DC");
        cp4.setMaxPower(150);
        cp4.setStatus(OperationalStatus.AVAILABLE);
        cp4.setLocationId("LOC-BER-002");
        chargingPoints.put(cp4.getId(), cp4);

        ChargingPoint cp5 = new ChargingPoint();
        cp5.setId("CP-BER-DC-03");
        cp5.setType("DC");
        cp5.setMaxPower(150);
        cp5.setStatus(OperationalStatus.AVAILABLE);
        cp5.setLocationId("LOC-BER-002");
        chargingPoints.put(cp5.getId(), cp5);
    }

    public ChargingPoint getChargingPointById(String id) {
        return chargingPoints.get(id);
    }

    public List<ChargingPoint> getAllChargingPoints() {
        return new ArrayList<>(chargingPoints.values());
    }

    public List<ChargingPoint> getAvailableChargingPoints() {
        List<ChargingPoint> available = new ArrayList<>();
        for (ChargingPoint cp : chargingPoints.values()) {
            if (OperationalStatus.AVAILABLE.equals(cp.getStatus())) {
                available.add(cp);
            }
        }
        return available;
    }

    public List<ChargingPoint> getChargingPointsByStatus(String status) {
        List<ChargingPoint> result = new ArrayList<>();
        for (ChargingPoint cp : chargingPoints.values()) {
            if (status.equals(cp.getStatus().getDisplayName())) {
                result.add(cp);
            }
        }
        return result;
    }

    public void addChargingPoint(ChargingPoint point) {
        chargingPoints.put(point.getId(), point);
    }

    public void updateChargingPoint(ChargingPoint point) {
        chargingPoints.put(point.getId(), point);
    }

    public void changeStatus(String pointId, OperationalStatus newStatus, String reason, String timePeriod) {
        ChargingPoint point = getChargingPointById(pointId);
        if (point != null) {
            point.setStatus(newStatus);
        }
    }
}
