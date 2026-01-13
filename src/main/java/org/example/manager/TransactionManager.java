package org.example.manager;

import org.example.model.Transaction;
import org.example.model.enums.PaymentStatus;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

public class TransactionManager {
    private static TransactionManager instance;
    private List<Transaction> allTransactions;

    private TransactionManager() {
        allTransactions = new ArrayList<>();
        initializeTestData();
    }

    public static synchronized TransactionManager getInstance() {
        if (instance == null) {
            instance = new TransactionManager();
        }
        return instance;
    }

    private void initializeTestData() {
        for (int i = 1; i <= 1245; i++) {
            Transaction t = new Transaction();
            t.setId("TRX-2024-01-" + String.format("%04d", i));
            t.setCustomerId("CUST-" + (2024 + (i % 10)));
            t.setLocation(i % 3 == 0 ? "Berlin-Alex" : i % 3 == 1 ? "Munich-Center" : "Hamburg-Port");
            t.setDate(LocalDate.of(2024, 1, (i % 28) + 1).toString());
            t.setTime(java.time.LocalTime.of((i % 24), (i % 60), 0).toString());
            t.setAmount(new BigDecimal(30 + (i % 160)));
            t.setType(i % 3 == 0 ? "DC Charging" : "AC Charging");
            t.setDuration(30 + (i % 60));
            t.setEnergy(new BigDecimal(20 + (i % 80)));
            t.setChargingPointId("CP-" + (i % 100));
            t.setPaymentStatus(PaymentStatus.SUCCESSFUL);
            allTransactions.add(t);
        }

        for (int i = 1; i <= 28; i++) {
            Transaction t = allTransactions.get(i);
            t.setPaymentStatus(PaymentStatus.CANCELED);
        }
    }

    public List<Transaction> getTransactionsForMonth(String month) {
        return new ArrayList<>(allTransactions);
    }

    public List<Transaction> filterTransactions(String location, String dateRange,
                                                BigDecimal minAmount, String chargerType,
                                                String paymentStatus) {
        List<Transaction> filtered = new ArrayList<>();

        for (Transaction t : allTransactions) {
            boolean matches = true;

            if (location != null && !location.equals(t.getLocation())) {
                matches = false;
            }

            if (chargerType != null && !t.getType().contains(chargerType)) {
                matches = false;
            }

            if (paymentStatus != null && !paymentStatus.equals(t.getPaymentStatus().getDisplayName())) {
                matches = false;
            }

            if (minAmount != null && t.getAmount().compareTo(minAmount) < 0) {
                matches = false;
            }

            if (dateRange != null) {
                String[] dates = dateRange.split(" - ");
                if (dates.length == 2) {
                    LocalDate start = LocalDate.parse(dates[0].replace(".", "-"));
                    LocalDate end = LocalDate.parse(dates[1].replace(".", "-"));
                    LocalDate txDate = t.getDate();

                    if (txDate.isBefore(start) || txDate.isAfter(end)) {
                        matches = false;
                    }
                }
            }

            if (matches) {
                filtered.add(t);
            }
        }

        return filtered;
    }

    public List<Transaction> sortTransactions(List<Transaction> transactions, String sortBy) {
        List<Transaction> sorted = new ArrayList<>(transactions);

        if ("date".equalsIgnoreCase(sortBy)) {
            sorted.sort((t1, t2) -> t1.getDate().compareTo(t2.getDate()));
        } else if ("amount".equalsIgnoreCase(sortBy)) {
            sorted.sort((t1, t2) -> t1.getAmount().compareTo(t2.getAmount()));
        }

        return sorted;
    }

    public String exportToCSV(List<Transaction> transactions) {
        StringBuilder csv = new StringBuilder();
        csv.append("Date,Time,Amount,Duration,Energy,Charging Point,Location,Status\n");

        for (Transaction t : transactions) {
            csv.append(t.getDate()).append(",")
                    .append(t.getTime()).append(",")
                    .append(t.getAmount()).append(" €,")
                    .append(t.getDuration()).append("min,")
                    .append(t.getEnergy()).append(" kWh,")
                    .append(t.getChargingPointId()).append(",")
                    .append(t.getLocation()).append(",")
                    .append(t.getPaymentStatus().getDisplayName())
                    .append("\n");
        }

        return csv.toString();
    }

    public void addTransaction(Transaction transaction) {
        allTransactions.add(transaction);
    }

    public BigDecimal calculateTotal(List<Transaction> transactions) {
        return transactions.stream()
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public List<Transaction> getCustomerTransactions(String customerId) {
        List<Transaction> customerTransactions = new ArrayList<>();

        if (customerId == null || customerId.trim().isEmpty()) {
            return customerTransactions;
        }

        for (Transaction transaction : allTransactions) {
            if (customerId.equals(transaction.getCustomerId())) {
                customerTransactions.add(transaction);
            }
        }


        customerTransactions.sort((t1, t2) -> {
            LocalDate date1 = t1.getDate();
            LocalDate date2 = t2.getDate();
            return date2.compareTo(date1);
        });

        return customerTransactions;

    }
    public List<Transaction> getAllTransactions() {
        return this.allTransactions;
    }
}
