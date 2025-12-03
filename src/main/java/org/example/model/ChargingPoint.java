package org.example.model;

import org.example.model.enums.ChargerType;
import org.example.model.enums.OperationalStatus;
import java.math.BigDecimal;

public class ChargingPoint {
    private String id;
    private String name;
    private ChargerType type;
    private int maxPower;
    private String connectorType;
    private String tariffGroup;
    private BigDecimal pricePerKwh;
    private boolean reservable;
    private BigDecimal reservationFee;
    private OperationalStatus status;
    private String locationId;

    public ChargingPoint() {
        this.status = OperationalStatus.AVAILABLE;
        this.reservable = false;
        this.reservationFee = BigDecimal.ZERO;
    }

    public ChargingPoint withId(String id) {
        this.id = id;
        return this;
    }

    public ChargingPoint withName(String name) {
        this.name = name;
        return this;
    }

    public ChargingPoint withType(String type) {
        this.type = "DC".equalsIgnoreCase(type) ? ChargerType.DC : ChargerType.AC;
        return this;
    }

    public ChargingPoint withMaxPower(int power) {
        this.maxPower = power;
        return this;
    }

    public ChargingPoint withPrice(BigDecimal price) {
        this.pricePerKwh = price;
        return this;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public ChargerType getType() { return type; }
    public void setType(ChargerType type) { this.type = type; }
    public void setType(String type) {
        this.type = "DC".equalsIgnoreCase(type) ? ChargerType.DC : ChargerType.AC;
    }

    public int getMaxPower() { return maxPower; }
    public void setMaxPower(int maxPower) { this.maxPower = maxPower; }

    public String getConnectorType() { return connectorType; }
    public void setConnectorType(String connectorType) { this.connectorType = connectorType; }

    public String getTariffGroup() { return tariffGroup; }
    public void setTariffGroup(String tariffGroup) { this.tariffGroup = tariffGroup; }

    public BigDecimal getPricePerKwh() { return pricePerKwh; }
    public void setPricePerKwh(BigDecimal pricePerKwh) { this.pricePerKwh = pricePerKwh; }

    public boolean isReservable() { return reservable; }
    public void setReservable(boolean reservable) { this.reservable = reservable; }

    public BigDecimal getReservationFee() { return reservationFee; }
    public void setReservationFee(BigDecimal reservationFee) {
        this.reservationFee = reservationFee;
    }

    public OperationalStatus getStatus() { return status; }
    public void setStatus(OperationalStatus status) { this.status = status; }

    public String getLocationId() { return locationId; }
    public void setLocationId(String locationId) { this.locationId = locationId; }

    public String getDisplayInfo() {
        return String.format("%s (%s, %dkW, %.2f€/kWh)",
                name != null ? name : id,
                type.getDisplayName(),
                maxPower,
                pricePerKwh != null ? pricePerKwh : BigDecimal.ZERO);
    }

    @Override
    public String toString() {
        return String.format("ChargingPoint{id='%s', type='%s', power=%d, status='%s'}",
                id, type.getDisplayName(), maxPower, status.getDisplayName());
    }
}
