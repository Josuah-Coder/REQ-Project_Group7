package org.example.model.enums;

public enum OperationalStatus {
    AVAILABLE("Available"),
    IN_USE("In Use"),
    MAINTENANCE("Maintenance"),
    FAULTY("Faulty"),
    OFFLINE("Offline"),
    ACTIVE("Active");

    private final String displayName;

    OperationalStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean isEmpty() {
        return this == OperationalStatus.OFFLINE;
    }
}