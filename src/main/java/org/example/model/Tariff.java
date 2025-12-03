package org.example.model;

import java.math.BigDecimal;

public class Tariff {
    private String name;
    private String timePeriod;
    private BigDecimal pricePerKwh;
    private BigDecimal baseFee;

    public Tariff() {
        this.pricePerKwh = BigDecimal.ZERO;
        this.baseFee = BigDecimal.ZERO;
    }

    public Tariff(String name, String timePeriod, BigDecimal pricePerKwh, BigDecimal baseFee) {
        this.name = name;
        this.timePeriod = timePeriod;
        this.pricePerKwh = pricePerKwh;
        this.baseFee = baseFee;
    }

    public Tariff withName(String name) {
        this.name = name;
        return this;
    }

    public Tariff withTimePeriod(String period) {
        this.timePeriod = period;
        return this;
    }

    public Tariff withPrice(BigDecimal price) {
        this.pricePerKwh = price;
        return this;
    }

    public Tariff withBaseFee(BigDecimal fee) {
        this.baseFee = fee;
        return this;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getTimePeriod() { return timePeriod; }
    public void setTimePeriod(String timePeriod) { this.timePeriod = timePeriod; }

    public BigDecimal getPricePerKwh() { return pricePerKwh; }
    public void setPricePerKwh(BigDecimal pricePerKwh) { this.pricePerKwh = pricePerKwh; }

    public BigDecimal getBaseFee() { return baseFee; }
    public void setBaseFee(BigDecimal baseFee) { this.baseFee = baseFee; }

    @Override
    public String toString() {
        return String.format("Tariff{name='%s', period='%s', price=%.2f€/kWh, fee=%.2f€}",
                name, timePeriod, pricePerKwh, baseFee);
    }
}