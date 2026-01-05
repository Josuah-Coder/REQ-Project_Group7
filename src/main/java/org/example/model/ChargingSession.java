package org.example.model;

import org.example.model.enums.SessionStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ChargingSession {
    private String id;
    private String chargingPointId;
    private String customerId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int duration;
    private int pauseDuration;
    private int power;
    private BigDecimal energyConsumed;
    private BigDecimal estimatedCost;
    private BigDecimal actualCost;
    private SessionStatus status;

    public ChargingSession(String chargingPointId, String customerId) {
        this.id = "SESS-" + System.currentTimeMillis();
        this.chargingPointId = chargingPointId;
        this.customerId = customerId;
        this.startTime = LocalDateTime.now();
        this.status = SessionStatus.ACTIVE;
        this.energyConsumed = BigDecimal.ZERO;
        this.estimatedCost = BigDecimal.ZERO;
        this.actualCost = BigDecimal.ZERO;
    }

    public ChargingSession withDuration(int minutes) {
        this.duration = minutes;
        return this;
    }

    public ChargingSession withPower(int power) {
        this.power = power;
        return this;
    }

    public String getId() { return id; }

    public String getChargingPointId() { return chargingPointId; }

    public String getCustomerId() { return customerId; }

    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }

    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }

    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }

    public int getPauseDuration() { return pauseDuration; }
    public void setPauseDuration(int pauseDuration) { this.pauseDuration = pauseDuration; }

    public int getPower() { return power; }
    public void setPower(int power) { this.power = power; }

    public BigDecimal getEnergyConsumed() { return energyConsumed; }
    public void setEnergyConsumed(BigDecimal energyConsumed) {
        this.energyConsumed = energyConsumed;
    }

    public BigDecimal getEstimatedCost() { return estimatedCost; }
    public void setEstimatedCost(BigDecimal estimatedCost) {
        this.estimatedCost = estimatedCost;
    }

    public BigDecimal getActualCost() { return actualCost; }
    public void setActualCost(BigDecimal actualCost) { this.actualCost = actualCost; }

    public SessionStatus getStatus() { return status; }
    public void setStatus(SessionStatus status) { this.status = status; }

    public int getChargingTime() {
        return duration - pauseDuration;
    }

    public int getTotalDuration() {
        return duration;
    }

    public void calculateEnergyAndCost() {
        double hours = getChargingTime() / 60.0;
        this.energyConsumed = BigDecimal.valueOf(power * hours);
        this.estimatedCost = energyConsumed.multiply(BigDecimal.valueOf(0.60));
    }

    public void pause(int minutes) {
        this.pauseDuration += minutes;
        this.status = SessionStatus.PAUSED;
    }

    public void resume() {
        this.status = SessionStatus.ACTIVE;
    }

    public void end() {
        this.endTime = LocalDateTime.now();
        this.status = SessionStatus.COMPLETED;

        if (startTime != null && endTime != null) {
            this.duration = (int) java.time.Duration.between(startTime, endTime).toMinutes() - pauseDuration;
        }
    }

    public void addChargingTime(int minutes) {
        this.duration += minutes;
    }

    @Override
    public String toString() {
        return String.format("ChargingSession{id='%s', point='%s', customer='%s', status='%s', duration=%dmin}",
                id, chargingPointId, customerId, status.getDisplayName(), duration);
    }

    public Object getTotalEnergy() {
        return energyConsumed;
    }

    public Object getTotalCost() {
        return actualCost;
    }


    public Object getChargingPoint() {
        return chargingPointId;
    }
}
