package org.example.manager;

import org.example.model.Customer;
import org.example.model.Transaction;
import java.math.BigDecimal;
import java.util.*;

public class CustomerManager {
    private static CustomerManager instance;
    private Map<String, Customer> customers;
    private Map<String, List<Transaction>> customerTransactions;

    private CustomerManager() {
        customers = new HashMap<>();
        customerTransactions = new HashMap<>();
        initializeTestData();
    }

    public static synchronized CustomerManager getInstance() {
        if (instance == null) {
            instance = new CustomerManager();
        }
        return instance;
    }

    private void initializeTestData() {
        Customer cust1 = new Customer("CUST-2024-001", "Max Mustermann")
                .withEmail("max.mustermann@email.de")
                .withAccountBalance(new BigDecimal("150.00"))
                .withCredit(new BigDecimal("100.00"));

        customers.put("CUST-2024-001", cust1);

        List<Transaction> transactions = new ArrayList<>();

        Transaction t1 = new Transaction();
        t1.setCustomerId("CUST-2024-001");
        t1.setLocation("Shopping Center Berlin");
        t1.setDate("2024-01-05");
        t1.setAmount(new BigDecimal("12.45"));
        t1.setType("AC Charging");
        transactions.add(t1);

        Transaction t2 = new Transaction();
        t2.setCustomerId("CUST-2024-001");
        t2.setLocation("Highway Rest Stop A8");
        t2.setDate("2024-01-10");
        t2.setAmount(new BigDecimal("28.56"));
        t2.setType("DC Charging");
        transactions.add(t2);

        Transaction t3 = new Transaction();
        t3.setCustomerId("CUST-2024-001");
        t3.setLocation("Hotel Admiral Cologne");
        t3.setDate("2024-01-15");
        t3.setAmount(new BigDecimal("18.90"));
        t3.setType("AC Charging");
        transactions.add(t3);

        Transaction t4 = new Transaction();
        t4.setCustomerId("CUST-2024-001");
        t4.setLocation("Highway Rest Stop A8");
        t4.setDate("2024-01-20");
        t4.setAmount(new BigDecimal("35.20"));
        t4.setType("DC Charging");
        transactions.add(t4);

        Transaction t5 = new Transaction();
        t5.setCustomerId("CUST-2024-001");
        t5.setLocation("Shopping Center Berlin");
        t5.setDate("2024-01-25");
        t5.setAmount(new BigDecimal("15.75"));
        t5.setType("AC Charging");
        transactions.add(t5);

        customerTransactions.put("CUST-2024-001", transactions);
    }

    public Customer getCustomerById(String id) {
        return customers.get(id);
    }

    public List<Customer> getAllCustomers() {
        return new ArrayList<>(customers.values());
    }

    public void addCustomer(Customer customer) {
        customers.put(customer.getId(), customer);
    }

    public List<Transaction> getCustomerTransactions(String customerId) {
        return customerTransactions.getOrDefault(customerId, new ArrayList<>());
    }

    public void saveTransactions(Customer customer, List<Transaction> transactions) {
        customerTransactions.put(customer.getId(), transactions);
    }

    public Map<String, String> getMonthlyOverview(String customerId, String month) {
        Map<String, String> overview = new HashMap<>();

        if ("CUST-2024-001".equals(customerId) && "January 2024".equals(month)) {
            overview.put("Number of Transactions", "5");
            overview.put("Total Expenses", "110.86 €");
            overview.put("Average per Charge", "22.17 €");
            overview.put("AC Charging Costs", "47.10 €");
            overview.put("DC Charging Costs", "63.76 €");
            overview.put("Most Used Location", "Highway Rest Stop A8 (2×)");
        }

        return overview;
    }

    public Map<String, BigDecimal> getWeeklyExpenses(String customerId, String month) {
        Map<String, BigDecimal> weekly = new HashMap<>();
        weekly.put("Week 1", new BigDecimal("12.45"));
        weekly.put("Week 2", new BigDecimal("46.66"));
        weekly.put("Week 3", new BigDecimal("35.20"));
        weekly.put("Week 4", new BigDecimal("15.75"));
        return weekly;
    }

    public Map<String, BigDecimal> getLocationDistribution(String customerId, String month) {
        Map<String, BigDecimal> distribution = new HashMap<>();
        distribution.put("Shopping Center Berlin", new BigDecimal("28.20"));
        distribution.put("Highway Rest Stop A8", new BigDecimal("63.76"));
        distribution.put("Hotel Admiral Cologne", new BigDecimal("18.90"));
        return distribution;
    }

    public List<Transaction> filterTransactions(String customerId, String timePeriod,
                                                String location, String chargerType,
                                                BigDecimal minAmount) {
        List<Transaction> all = getCustomerTransactions(customerId);
        List<Transaction> filtered = new ArrayList<>();

        for (Transaction t : all) {
            boolean matches = true;

            if (location != null && !location.equals(t.getLocation())) {
                matches = false;
            }

            if (chargerType != null && !chargerType.equals(t.getType().split(" ")[0])) {
                matches = false;
            }

            if (minAmount != null && t.getAmount().compareTo(minAmount) < 0) {
                matches = false;
            }

            if (timePeriod != null) {
                String[] dates = timePeriod.split(" to ");
                if (dates.length == 2) {
                    if (t.getDate().toString().compareTo(dates[0]) < 0 ||
                            t.getDate().toString().compareTo(dates[1]) > 0) {
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

    public List<Transaction> sortTransactions(List<Transaction> transactions, String criteria) {
        List<Transaction> sorted = new ArrayList<>(transactions);

        if ("Amount descending".equals(criteria)) {
            sorted.sort((t1, t2) -> t2.getAmount().compareTo(t1.getAmount()));
        } else if ("Date".equals(criteria)) {
            sorted.sort((t1, t2) -> t1.getDate().compareTo(t2.getDate()));
        }

        return sorted;
    }

    public Customer refreshCustomerData(Customer customer) {
        return getCustomerById(customer.getId());
    }
}
