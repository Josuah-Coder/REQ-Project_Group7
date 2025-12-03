package org.example.model.enums;

public enum ChargerType {
    AC("AC"),
    DC("DC"),
    UNKNOWN("Unknown");

    private final String displayName;

    ChargerType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
