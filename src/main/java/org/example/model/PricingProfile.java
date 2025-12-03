package org.example.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class PricingProfile {
    private String locationId;
    private String effectiveFrom;
    private List<Tariff> tariffs;

    public PricingProfile() {
        this.tariffs = new ArrayList<>();
    }

    public PricingProfile forLocation(String locationId) {
        this.locationId = locationId;
        return this;
    }

    public PricingProfile effectiveFrom(String date) {
        this.effectiveFrom = date;
        return this;
    }

    public PricingProfile withTariff(Tariff tariff) {
        this.tariffs.add(tariff);
        return this;
    }

    public String getLocationId() { return locationId; }
    public void setLocationId(String locationId) { this.locationId = locationId; }

    public String getEffectiveFrom() { return effectiveFrom; }
    public void setEffectiveFrom(String effectiveFrom) { this.effectiveFrom = effectiveFrom; }

    public List<Tariff> getTariffs() { return tariffs; }
    public void addTariff(Tariff tariff) { this.tariffs.add(tariff); }

    public BigDecimal getAveragePrice() {
        if (tariffs.isEmpty()) return BigDecimal.ZERO;

        BigDecimal sum = tariffs.stream()
                .map(Tariff::getPricePerKwh)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return sum.divide(BigDecimal.valueOf(tariffs.size()), 2, BigDecimal.ROUND_HALF_UP);
    }

    public PricingProfile adjustPrices(int percentage) {
        double factor = 1.0 + (percentage / 100.0);

        for (Tariff tariff : tariffs) {
            BigDecimal newPrice = tariff.getPricePerKwh()
                    .multiply(BigDecimal.valueOf(factor))
                    .setScale(2, BigDecimal.ROUND_HALF_UP);
            tariff.setPricePerKwh(newPrice);

            BigDecimal newBaseFee = tariff.getBaseFee()
                    .multiply(BigDecimal.valueOf(factor))
                    .setScale(2, BigDecimal.ROUND_HALF_UP);
            tariff.setBaseFee(newBaseFee);
        }

        return this;
    }

    @Override
    public String toString() {
        return String.format("PricingProfile{location='%s', tariffs=%d, avgPrice=%.2f€}",
                locationId, tariffs.size(), getAveragePrice());
    }
}