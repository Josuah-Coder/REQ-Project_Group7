package org.example.model;

import org.example.model.enums.OperationalStatus;

import java.util.ArrayList;
import java.util.List;

public class Location {
    private String id;
    private String name;
    private String address;
    private String city;
    private String operatingHours;
    private OperationalStatus operationalStatus;
    private String createdDate;
    private String lastMaintenance;
    private String contactPerson;
    private String contactEmail;
    private String description;
    private List<ChargingPoint> chargingPoints;


    public Location() {
        this.id = "LOC-" + System.currentTimeMillis();
        this.operationalStatus = OperationalStatus.ACTIVE;
        this.chargingPoints = new ArrayList<>();
        this.createdDate = java.time.LocalDate.now().toString();
    }

    public Location withName(String name) {
        this.name = name;
        return this;
    }

    public Location withAddress(String address) {
        this.address = address;
        return this;
    }

    public Location withCity(String city) {
        this.city = city;
        return this;
    }

    public Location withOperatingHours(String hours) {
        this.operatingHours = hours;
        return this;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getOperatingHours() {
        return operatingHours;
    }

    public void setOperatingHours(String operatingHours) {
        this.operatingHours = operatingHours;
    }

    public OperationalStatus getOperationalStatus() {
        return operationalStatus;
    }

    public void setOperationalStatus(OperationalStatus status) {
        this.operationalStatus = status;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String date) {
        this.createdDate = date;
    }

    public String getLastMaintenance() {
        return lastMaintenance;
    }

    public void setLastMaintenance(String date) {
        this.lastMaintenance = date;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String person) {
        this.contactPerson = person;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String email) {
        this.contactEmail = email;
    }

    public List<ChargingPoint> getChargingPoints() {
        return chargingPoints;
    }

    public void addChargingPoint(ChargingPoint point) {
        this.chargingPoints.add(point);
        point.setLocationId(this.id);
    }

    public int getTotalChargingPoints() {
        return chargingPoints.size();
    }

    public String getTotalChargingPointsFormatted() {
        long acCount = chargingPoints.stream()
                .filter(cp -> "AC".equals(cp.getType()))
                .count();
        long dcCount = chargingPoints.stream()
                .filter(cp -> "DC".equals(cp.getType()))
                .count();
        return String.format("%d (%d AC, %d DC)", chargingPoints.size(), acCount, dcCount);
    }

    public String getTechnicalSpecifications() {
        return String.format("Location: %s\nAddress: %s\nOperating Hours: %s\nStatus: %s\nTotal Points: %s",
                name, address, operatingHours, operationalStatus.getDisplayName(), getTotalChargingPointsFormatted());
    }

    @Override
    public String toString() {
        return String.format("Location{id='%s', name='%s', address='%s', points=%d}",
                id, name, address, chargingPoints.size());
    }

    public String getDescription() {
            return description;
    }


    public void setDescription(String updatedDescriptionForTesting) {
        this.description = updatedDescriptionForTesting;
    }
}