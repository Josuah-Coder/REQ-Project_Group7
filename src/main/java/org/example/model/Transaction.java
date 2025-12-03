package org.example.model;

import org.example.model.enums.PaymentStatus;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public class Transaction {
    private String id;
    private String customerId;
    private String location;
    private LocalDate date;
    private LocalTime time;
    private BigDecimal amount;
    private String type;
    private int duration;
    private BigDecimal energy;
    private String chargingPointId;
    private PaymentStatus paymentStatus;

    public Transaction() {
        this.id = "TRX-" + System.currentTimeMillis();
        this.paymentStatus = PaymentStatus.SUCCESSFUL;
        this.date = LocalDate.now();
        this.time = LocalTime.now();
    }

    public Transaction withCustomer(String customerId) {
        this.customerId = customerId;
        return this;
    }

    public Transaction withLocation(String location) {
        this.location = location;
        return this;
    }

    public Transaction withAmount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public Transaction withType(String type) {
        this.type = type;
        return this;
    }

    public String getId() { return id; }

    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public void setDate(String date) {
        this.date = LocalDate.parse(date);
    }

    public LocalTime getTime() { return time; }
    public void setTime(LocalTime time) { this.time = time; }
    public void setTime(String time) {
        this.time = LocalTime.parse(time);
    }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }

    public BigDecimal getEnergy() { return energy; }
    public void setEnergy(BigDecimal energy) { this.energy = energy; }

    public String getChargingPointId() { return chargingPointId; }
    public void setChargingPointId(String chargingPointId) {
        this.chargingPointId = chargingPointId;
    }

    public PaymentStatus getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    @Override
    public String toString() {
        return String.format("Transaction{id='%s', date=%s, amount=%.2f€, location='%s', status='%s'}",
                id, date, amount, location, paymentStatus.getDisplayName());
    }
}