package org.example.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PriceChange {
    private String id;
    private String locationId;
    private LocalDateTime changeDate;
    private String changedBy;
    private String action;
    private BigDecimal oldPrice;
    private BigDecimal newPrice;
    private String reason;

    public PriceChange(String locationId, String changedBy) {
        this.id = "PC-" + System.currentTimeMillis();
        this.locationId = locationId;
        this.changeDate = LocalDateTime.now();
        this.changedBy = changedBy;
    }

    public PriceChange withAction(String action) {
        this.action = action;
        return this;
    }

    public PriceChange withOldPrice(BigDecimal price) {
        this.oldPrice = price;
        return this;
    }

    public PriceChange withNewPrice(BigDecimal price) {
        this.newPrice = price;
        return this;
    }

    public PriceChange withReason(String reason) {
        this.reason = reason;
        return this;
    }

    public String getId() { return id; }

    public String getLocationId() { return locationId; }

    public LocalDateTime getChangeDate() { return changeDate; }
    public String getDate() { return changeDate.toLocalDate().toString(); }

    public String getChangedBy() { return changedBy; }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    public BigDecimal getOldPrice() { return oldPrice; }
    public void setOldPrice(BigDecimal oldPrice) { this.oldPrice = oldPrice; }

    public BigDecimal getNewPrice() { return newPrice; }
    public void setNewPrice(BigDecimal newPrice) { this.newPrice = newPrice; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    @Override
    public String toString() {
        return String.format("PriceChange{location='%s', date=%s, by='%s', action='%s'}",
                locationId, getDate(), changedBy, action);
    }

    public String getDescription() {
        StringBuilder description = new StringBuilder();

        if (action != null) {
            description.append(action);
        }

        if (oldPrice != null && newPrice != null) {
            if (description.length() > 0) description.append(": ");
            description.append(String.format("Price changed from €%s to €%s",
                    oldPrice, newPrice));
        }

        if (reason != null) {
            if (description.length() > 0) description.append(" - ");
            description.append("Reason: ").append(reason);
        }

        return description.toString();
    }
}