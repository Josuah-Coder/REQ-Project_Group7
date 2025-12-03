package org.example.model.enums;

public enum PaymentStatus {
    SUCCESSFUL("Successful"),
    PENDING("Pending"),
    FAILED("Failed"),
    REFUNDED("Refunded"),
    CANCELED("Canceled");

    private final String displayName;

    PaymentStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}