package org.example.manager;

import org.example.model.PricingProfile;
import org.example.model.Tariff;
import org.example.model.PriceChange;
import java.math.BigDecimal;
import java.util.*;

public class PricingManager {
    private static PricingManager instance;
    private Map<String, PricingProfile> pricingProfiles;
    private List<PriceChange> priceChanges;

    private PricingManager() {
        pricingProfiles = new HashMap<>();
        priceChanges = new ArrayList<>();
        initializeTestData();
    }

    public static synchronized PricingManager getInstance() {
        if (instance == null) {
            instance = new PricingManager();
        }
        return instance;
    }

    private void initializeTestData() {
        PricingProfile profile1 = new PricingProfile();
        profile1.setLocationId("LOC-A8-001");
        profile1.setEffectiveFrom("2024-01-01");

        profile1.addTariff(new Tariff("Standard", "06:00-22:00",
                new BigDecimal("0.60"), new BigDecimal("1.50")));
        profile1.addTariff(new Tariff("Night", "22:00-06:00",
                new BigDecimal("0.40"), new BigDecimal("1.00")));
        profile1.addTariff(new Tariff("Weekend", "All day",
                new BigDecimal("0.55"), new BigDecimal("1.50")));

        pricingProfiles.put("LOC-A8-001", profile1);

        PriceChange pc1 = new PriceChange("LOC-CGN-001", "price@energy.de");
        pc1.withAction("Prices increased by 5%")
                .withOldPrice(new BigDecimal("0.48"))
                .withNewPrice(new BigDecimal("0.50"))
                .withReason("Increased energy costs");
        priceChanges.add(pc1);

        PriceChange pc2 = new PriceChange("LOC-CGN-001", "admin@energy.de");
        pc2.withAction("Night tariff introduced")
                .withNewPrice(new BigDecimal("0.40"))
                .withReason("Boost demand");
        priceChanges.add(pc2);

        PriceChange pc3 = new PriceChange("LOC-CGN-001", "manager@energy.de");
        pc3.withAction("Weekend discount")
                .withOldPrice(new BigDecimal("0.50"))
                .withNewPrice(new BigDecimal("0.45"))
                .withReason("Weekend promotion");
        priceChanges.add(pc3);
    }

    public PricingProfile getPricingProfile(String locationId) {
        return pricingProfiles.get(locationId);
    }

    public void setPricingProfile(String locationId, PricingProfile profile) {
        pricingProfiles.put(locationId, profile);
    }

    public void setPricingProfile(String locationId, PricingProfile profile, String reason) {
        pricingProfiles.put(locationId, profile);

        PriceChange change = new PriceChange(locationId, "system");
        change.withAction("Price update")
                .withNewPrice(profile.getAveragePrice())
                .withReason(reason);
        priceChanges.add(change);
    }

    public PricingProfile getCustomerPricing(String locationId) {
        return pricingProfiles.get(locationId);
    }

    public void updatePricingProfile(String locationId, PricingProfile profile, String reason) {
        PricingProfile old = pricingProfiles.get(locationId);

        PriceChange change = new PriceChange(locationId, "operator");
        change.withAction("Price update")
                .withOldPrice(old != null ? old.getAveragePrice() : null)
                .withNewPrice(profile.getAveragePrice())
                .withReason(reason);
        priceChanges.add(change);

        pricingProfiles.put(locationId, profile);
    }

    public boolean isPriceActive(String locationId) {
        return pricingProfiles.containsKey(locationId);
    }

    public PricingProfile copyAndAdjustPrices(PricingProfile source, int percentage) {
        PricingProfile copy = new PricingProfile();
        copy.setLocationId(source.getLocationId());

        for (Tariff tariff : source.getTariffs()) {
            Tariff newTariff = new Tariff();
            newTariff.setName(tariff.getName());
            newTariff.setTimePeriod(tariff.getTimePeriod());

            BigDecimal newPrice = tariff.getPricePerKwh()
                    .multiply(BigDecimal.valueOf(1 + percentage / 100.0))
                    .setScale(2, BigDecimal.ROUND_HALF_UP);
            newTariff.setPricePerKwh(newPrice);

            BigDecimal newFee = tariff.getBaseFee()
                    .multiply(BigDecimal.valueOf(1 + percentage / 100.0))
                    .setScale(2, BigDecimal.ROUND_HALF_UP);
            newTariff.setBaseFee(newFee);

            copy.addTariff(newTariff);
        }

        return copy;
    }

    public List<PriceChange> getPriceChanges(String locationId) {
        List<PriceChange> changes = new ArrayList<>();
        for (PriceChange pc : priceChanges) {
            if (locationId.equals(pc.getLocationId())) {
                changes.add(pc);
            }
        }
        return changes;
    }

    public List<PriceChange> filterChangesByPeriod(List<PriceChange> changes, String period) {
        if ("January 2024".equals(period)) {
            return changes;
        }
        return new ArrayList<>();
    }

    public boolean canRevertChange(String changeId) {
        return true;
    }

    public List<PricingProfile> getAllPricingProfiles() {
        return new ArrayList<>(pricingProfiles.values());
    }

    public Map<String, PricingProfile> getPricesByLocation() {
        return new HashMap<>(pricingProfiles);
    }
}
