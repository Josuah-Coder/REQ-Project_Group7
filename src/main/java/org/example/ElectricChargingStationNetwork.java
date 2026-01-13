package org.example;

import org.example.manager.*;
import org.example.model.*;
import org.example.model.enums.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class ElectricChargingStationNetwork {

    private static Scanner scanner = new Scanner(System.in);
    private static Customer currentCustomer = null;
    private static Operator currentOperator = null;

    public static void main(String[] args) {
        System.out.println("═══════════════════════════════════════════════════════");
        System.out.println("  ELECTRIC VEHICLE CHARGING STATION NETWORK");
        System.out.println("═══════════════════════════════════════════════════════");
        System.out.println();

        initializeSystem();

        boolean running = true;
        while (running) {
            displayMainMenu();
            int choice = getIntInput();

            switch (choice) {
                case 1:
                    customerMenu();
                    break;
                case 2:
                    operatorMenu();
                    break;
                case 3:
                    displayNetworkStatistics();
                    break;
                case 4:
                    runDemoScenario();
                    break;
                case 0:
                    running = false;
                    System.out.println("\n✓ Thank you for using the EV Charging Network!");
                    break;
                default:
                    System.out.println("✗ Invalid choice. Please try again.");
            }
        }

        scanner.close();
    }


    private static void initializeSystem() {
        System.out.println("⚙ Initializing system...");


        LocationManager.getInstance();
        ChargingPointManager.getInstance();
        CustomerManager.getInstance();
        PricingManager.getInstance();
        TransactionManager.getInstance();

        System.out.println("✓ System initialized successfully!");
        System.out.println();
    }


    private static void displayMainMenu() {
        System.out.println("\n╔════════════════ MAIN MENU ════════════════╗");
        System.out.println("║  1. Customer Portal                       ║");
        System.out.println("║  2. Operator Dashboard                    ║");
        System.out.println("║  3. View Network Statistics               ║");
        System.out.println("║  4. Run Demo Scenario                     ║");
        System.out.println("║  0. Exit                                  ║");
        System.out.println("╚═══════════════════════════════════════════╝");
        System.out.print("Your choice: ");
    }


    private static void customerMenu() {
        System.out.println("\n╔════════════════ CUSTOMER PORTAL ════════════════╗");
        System.out.println("║  1. Find Charging Locations                    ║");
        System.out.println("║  2. Start Charging Session                     ║");
        System.out.println("║  3. Stop Charging Session                      ║");
        System.out.println("║  4. View My Transactions                       ║");
        System.out.println("║  5. View Account Balance                       ║");
        System.out.println("║  6. Login as Different Customer                ║");
        System.out.println("║  0. Back to Main Menu                          ║");
        System.out.println("╚════════════════════════════════════════════════╝");
        System.out.print("Your choice: ");

        int choice = getIntInput();

        switch (choice) {
            case 1:
                findChargingLocations();
                break;
            case 2:
                startChargingSession();
                break;
            case 3:
                stopChargingSession();
                break;
            case 4:
                viewCustomerTransactions();
                break;
            case 5:
                viewAccountBalance();
                break;
            case 6:
                customerLogin();
                break;
            case 0:
                return;
            default:
                System.out.println("✗ Invalid choice.");
        }
    }


    private static void operatorMenu() {
        System.out.println("\n╔════════════════ OPERATOR DASHBOARD ════════════════╗");
        System.out.println("║  1. View All Locations                            ║");
        System.out.println("║  2. Add New Location                              ║");
        System.out.println("║  3. Manage Charging Points                        ║");
        System.out.println("║  4. View Pricing Overview                         ║");
        System.out.println("║  5. Update Location Prices                        ║");
        System.out.println("║  6. Monitor Network Status                        ║");
        System.out.println("║  7. View Billing Statistics                       ║");
        System.out.println("║  8. Login as Different Operator                   ║");
        System.out.println("║  0. Back to Main Menu                             ║");
        System.out.println("╚═══════════════════════════════════════════════════╝");
        System.out.print("Your choice: ");

        int choice = getIntInput();

        switch (choice) {
            case 1:
                viewAllLocations();
                break;
            case 2:
                addNewLocation();
                break;
            case 3:
                manageChargingPoints();
                break;
            case 4:
                viewPricingOverview();
                break;
            case 5:
                updateLocationPrices();
                break;
            case 6:
                monitorNetworkStatus();
                break;
            case 7:
                viewBillingStatistics();
                break;
            case 8:
                operatorLogin();
                break;
            case 0:
                return;
            default:
                System.out.println("✗ Invalid choice.");
        }
    }



    private static void customerLogin() {
        System.out.print("\nEnter Customer ID (e.g., CUST-2024-001): ");
        String customerId = scanner.nextLine();

        currentCustomer = CustomerManager.getInstance().getCustomerById(customerId);

        if (currentCustomer != null) {
            System.out.println("✓ Logged in as: " + currentCustomer.getName());
            System.out.println("  Balance: " + currentCustomer.getAccountBalance() + " €");
        } else {
            System.out.println("✗ Customer not found. Using guest mode.");
        }
    }

    private static void findChargingLocations() {
        System.out.println("\n════════════ AVAILABLE CHARGING LOCATIONS ════════════");
        List<Location> locations = LocationManager.getInstance().getAllLocations();

        if (locations.isEmpty()) {
            System.out.println("✗ No locations found.");
            return;
        }

        for (int i = 0; i < locations.size(); i++) {
            Location loc = locations.get(i);
            int total = loc.getChargingPoints().size();
            long available = loc.getChargingPoints().stream()
                    .filter(cp -> OperationalStatus.AVAILABLE.equals(cp.getStatus()))
                    .count();

            System.out.println("\n" + (i + 1) + ". " + loc.getName());
            System.out.println("   Address: " + loc.getAddress());
            System.out.println("   Hours: " + loc.getOperatingHours());
            System.out.println("   Available: " + available + "/" + total + " charging points");

            PricingProfile pricing = PricingManager.getInstance().getPricingProfile(loc.getId());
            if (pricing != null) {
                System.out.println("   Avg Price: " + pricing.getAveragePrice() + " €/kWh");
            }
        }
        System.out.println("══════════════════════════════════════════════════════");
    }

    private static void startChargingSession() {
        if (currentCustomer == null) {
            System.out.println("✗ Please login first.");
            customerLogin();
            if (currentCustomer == null) return;
        }

        List<ChargingPoint> available = ChargingPointManager.getInstance().getAvailableChargingPoints();

        if (available.isEmpty()) {
            System.out.println("✗ No available charging points at the moment.");
            return;
        }

        System.out.println("\n════════════ AVAILABLE CHARGING POINTS ════════════");
        for (int i = 0; i < Math.min(5, available.size()); i++) {
            ChargingPoint cp = available.get(i);
            System.out.println((i + 1) + ". " + cp.getId() + " - " +
                    cp.getType().getDisplayName() + " " + cp.getMaxPower() + "kW");
        }

        System.out.print("\nSelect charging point (1-" + Math.min(5, available.size()) + "): ");
        int choice = getIntInput() - 1;

        if (choice < 0 || choice >= available.size()) {
            System.out.println("✗ Invalid choice.");
            return;
        }

        ChargingPoint selectedPoint = available.get(choice);


        BigDecimal estimatedCost = new BigDecimal("30.00");
        if (currentCustomer.getAccountBalance().compareTo(estimatedCost) < 0) {
            System.out.println("\n✗ Insufficient balance!");
            System.out.println("  Your balance: " + currentCustomer.getAccountBalance() + " €");
            System.out.println("  Estimated cost: " + estimatedCost + " €");
            return;
        }


        ChargingSession session = ChargingSessionManager.getInstance()
                .startSession(selectedPoint, currentCustomer);

        System.out.println("\n✓ Charging session started!");
        System.out.println("  Session ID: " + session.getId());
        System.out.println("  Charging Point: " + selectedPoint.getId());
        System.out.println("  Status: " + session.getStatus().getDisplayName());
        System.out.println("  Your balance: " + currentCustomer.getAccountBalance() + " €");
    }

    private static void stopChargingSession() {
        if (currentCustomer == null) {
            System.out.println("✗ Please login first.");
            return;
        }

        ChargingSession session = ChargingSessionManager.getInstance()
                .getActiveSessionForCustomer(currentCustomer.getId());

        if (session == null) {
            System.out.println("✗ No active charging session found.");
            return;
        }


        session.setDuration(30);
        session.setPower(150);
        session.calculateEnergyAndCost();

        ChargingSessionManager.getInstance().stopSession(session);

        System.out.println("\n✓ Charging session stopped!");
        System.out.println("  Session ID: " + session.getId());
        System.out.println("  Duration: " + session.getDuration() + " minutes");
        System.out.println("  Energy delivered: " + session.getEnergyConsumed() + " kWh");
        System.out.println("  Cost: " + session.getEstimatedCost() + " €");


        currentCustomer.chargeAccount(session.getEstimatedCost());
        System.out.println("  New balance: " + currentCustomer.getAccountBalance() + " €");
    }

    private static void viewCustomerTransactions() {
        if (currentCustomer == null) {
            System.out.println("✗ Please login first.");
            customerLogin();
            if (currentCustomer == null) return;
        }

        List<Transaction> transactions = CustomerManager.getInstance()
                .getCustomerTransactions(currentCustomer.getId());

        System.out.println("\n════════════ YOUR TRANSACTIONS ════════════");

        if (transactions.isEmpty()) {
            System.out.println("No transactions found.");
            return;
        }

        BigDecimal total = BigDecimal.ZERO;
        for (Transaction t : transactions) {
            System.out.println("\n" + t.getDate() + " - " + t.getLocation());
            System.out.println("  Amount: " + t.getAmount() + " €");
            System.out.println("  Type: " + t.getType());
            System.out.println("  Status: " + t.getPaymentStatus().getDisplayName());
            total = total.add(t.getAmount());
        }

        System.out.println("\n────────────────────────────────────────────");
        System.out.println("Total: " + total + " €");
        System.out.println("═══════════════════════════════════════════");
    }

    private static void viewAccountBalance() {
        if (currentCustomer == null) {
            System.out.println("✗ Please login first.");
            customerLogin();
            if (currentCustomer == null) return;
        }

        System.out.println("\n════════════ ACCOUNT INFORMATION ════════════");
        System.out.println("Customer: " + currentCustomer.getName());
        System.out.println("ID: " + currentCustomer.getId());
        System.out.println("Balance: " + currentCustomer.getAccountBalance() + " €");
        System.out.println("Available Credit: " + currentCustomer.getAvailableCredit() + " €");
        System.out.println("Total Available: " +
                currentCustomer.getAccountBalance().add(currentCustomer.getAvailableCredit()) + " €");
        System.out.println("═════════════════════════════════════════════");
    }


    private static void operatorLogin() {
        System.out.print("\nEnter Operator Email: ");
        String email = scanner.nextLine();

        currentOperator = OperatorManager.getInstance().login(email, "password");

        if (currentOperator != null) {
            System.out.println("✓ Logged in as: " + currentOperator.getName());
        } else {
            System.out.println("✗ Login failed. Using guest mode.");
        }
    }

    private static void viewAllLocations() {
        System.out.println("\n════════════ ALL LOCATIONS ════════════");
        List<Location> locations = LocationManager.getInstance().getAllLocations();

        for (Location loc : locations) {
            System.out.println("\n" + loc.getName() + " (" + loc.getId() + ")");
            System.out.println("  Address: " + loc.getAddress());
            System.out.println("  Hours: " + loc.getOperatingHours());
            System.out.println("  Status: " + loc.getOperationalStatus().getDisplayName());
            System.out.println("  Charging Points: " + loc.getTotalChargingPointsFormatted());
        }
        System.out.println("\n═══════════════════════════════════════");
    }

    private static void addNewLocation() {
        System.out.println("\n════════════ ADD NEW LOCATION ════════════");

        System.out.print("Location Name: ");
        String name = scanner.nextLine();

        System.out.print("Address: ");
        String address = scanner.nextLine();

        System.out.print("City: ");
        String city = scanner.nextLine();

        System.out.print("Operating Hours (e.g., 24/7): ");
        String hours = scanner.nextLine();

        Location newLocation = new Location()
                .withName(name)
                .withAddress(address)
                .withCity(city)
                .withOperatingHours(hours);

        LocationManager.getInstance().addLocation(newLocation);

        System.out.println("\n✓ Location added successfully!");
        System.out.println("  ID: " + newLocation.getId());
        System.out.println("  Name: " + newLocation.getName());
    }

    private static void manageChargingPoints() {
        System.out.println("\n════════════ CHARGING POINTS OVERVIEW ════════════");
        List<ChargingPoint> points = ChargingPointManager.getInstance().getAllChargingPoints();

        System.out.println("Total Charging Points: " + points.size());

        long available = points.stream()
                .filter(cp -> OperationalStatus.AVAILABLE.equals(cp.getStatus()))
                .count();
        long inUse = points.stream()
                .filter(cp -> OperationalStatus.IN_USE.equals(cp.getStatus()))
                .count();
        long maintenance = points.stream()
                .filter(cp -> OperationalStatus.MAINTENANCE.equals(cp.getStatus()))
                .count();

        System.out.println("\nStatus Distribution:");
        System.out.println("  Available: " + available);
        System.out.println("  In Use: " + inUse);
        System.out.println("  Maintenance: " + maintenance);

        System.out.println("\nRecent Charging Points:");
        for (int i = 0; i < Math.min(5, points.size()); i++) {
            ChargingPoint cp = points.get(i);
            System.out.println("  " + cp.getId() + " - " + cp.getType().getDisplayName() +
                    " " + cp.getMaxPower() + "kW - " + cp.getStatus().getDisplayName());
        }
        System.out.println("══════════════════════════════════════════════════");
    }

    private static void viewPricingOverview() {
        System.out.println("\n════════════ PRICING OVERVIEW ════════════");
        List<PricingProfile> profiles = PricingManager.getInstance().getAllPricingProfiles();

        for (PricingProfile profile : profiles) {
            Location loc = LocationManager.getInstance().getLocationById(profile.getLocationId());
            if (loc != null) {
                System.out.println("\n" + loc.getName());
                System.out.println("  Average Price: " + profile.getAveragePrice() + " €/kWh");
                System.out.println("  Tariffs:");
                for (Tariff tariff : profile.getTariffs()) {
                    System.out.println("    - " + tariff.getName() + ": " +
                            tariff.getPricePerKwh() + " €/kWh (" + tariff.getTimePeriod() + ")");
                }
            }
        }
        System.out.println("\n══════════════════════════════════════════");
    }

    private static void updateLocationPrices() {
        System.out.println("\n════════════ UPDATE PRICES ════════════");

        viewAllLocations();

        System.out.print("\nEnter Location ID: ");
        String locationId = scanner.nextLine();

        Location loc = LocationManager.getInstance().getLocationById(locationId);
        if (loc == null) {
            System.out.println("✗ Location not found.");
            return;
        }

        System.out.print("Enter new price per kWh (e.g., 0.55): ");
        double price = Double.parseDouble(scanner.nextLine());

        PricingProfile profile = new PricingProfile();
        profile.setLocationId(locationId);
        profile.addTariff(new Tariff("Standard", "All day",
                BigDecimal.valueOf(price), BigDecimal.valueOf(1.00)));

        PricingManager.getInstance().updatePricingProfile(locationId, profile, "Manual update");

        System.out.println("✓ Prices updated successfully!");
    }

    private static void monitorNetworkStatus() {
        System.out.println("\n════════════ NETWORK STATUS ════════════");

        List<Location> locations = LocationManager.getInstance().getAllLocations();
        List<ChargingPoint> points = ChargingPointManager.getInstance().getAllChargingPoints();
        List<ChargingSession> activeSessions = ChargingSessionManager.getInstance().getActiveSessions();

        System.out.println("Total Locations: " + locations.size());
        System.out.println("Total Charging Points: " + points.size());
        System.out.println("Active Sessions: " + activeSessions.size());

        long availablePoints = points.stream()
                .filter(cp -> OperationalStatus.AVAILABLE.equals(cp.getStatus()))
                .count();

        int availability = points.isEmpty() ? 0 : (int)((availablePoints * 100) / points.size());

        System.out.println("Network Availability: " + availability + "%");

        System.out.println("\nStatus by Type:");
        long acPoints = points.stream()
                .filter(cp -> ChargerType.AC.equals(cp.getType()))
                .count();
        long dcPoints = points.stream()
                .filter(cp -> ChargerType.DC.equals(cp.getType()))
                .count();

        System.out.println("  AC Chargers: " + acPoints);
        System.out.println("  DC Chargers: " + dcPoints);

        System.out.println("═══════════════════════════════════════");
    }

    private static void viewBillingStatistics() {
        System.out.println("\n════════════ BILLING STATISTICS ════════════");

        List<Transaction> allTransactions = TransactionManager.getInstance()
                .getTransactionsForMonth("January 2024");

        BigDecimal totalRevenue = allTransactions.stream()
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        long successfulCount = allTransactions.stream()
                .filter(t -> PaymentStatus.SUCCESSFUL.equals(t.getPaymentStatus()))
                .count();

        System.out.println("Total Transactions: " + allTransactions.size());
        System.out.println("Successful Transactions: " + successfulCount);
        System.out.println("Total Revenue: " + totalRevenue + " €");

        if (!allTransactions.isEmpty()) {
            BigDecimal avgTransaction = totalRevenue.divide(
                    BigDecimal.valueOf(allTransactions.size()), 2, BigDecimal.ROUND_HALF_UP);
            System.out.println("Average Transaction: " + avgTransaction + " €");
        }

        System.out.println("════════════════════════════════════════════");
    }



    private static void displayNetworkStatistics() {
        System.out.println("\n╔════════════════ NETWORK STATISTICS ════════════════╗");

        List<Location> locations = LocationManager.getInstance().getAllLocations();
        List<ChargingPoint> points = ChargingPointManager.getInstance().getAllChargingPoints();
        List<Customer> customers = CustomerManager.getInstance().getAllCustomers();

        System.out.println("║ Total Locations: " + String.format("%-32d", locations.size()) + "║");
        System.out.println("║ Total Charging Points: " + String.format("%-25d", points.size()) + "║");
        System.out.println("║ Registered Customers: " + String.format("%-26d", customers.size()) + "║");

        long availablePoints = points.stream()
                .filter(cp -> OperationalStatus.AVAILABLE.equals(cp.getStatus()))
                .count();
        int availability = points.isEmpty() ? 0 : (int)((availablePoints * 100) / points.size());

        System.out.println("║ Network Availability: " + String.format("%-24d", availability) + "% ║");

        List<ChargingSession> activeSessions = ChargingSessionManager.getInstance().getActiveSessions();
        System.out.println("║ Active Charging Sessions: " + String.format("%-21d", activeSessions.size()) + "║");

        System.out.println("╚═══════════════════════════════════════════════════╝");
    }

    private static void runDemoScenario() {
        System.out.println("\n════════════ RUNNING DEMO SCENARIO ════════════");
        System.out.println("This demonstrates the complete charging workflow...\n");


        System.out.println("1️⃣  Customer searches for charging locations...");
        List<Location> locations = LocationManager.getInstance().getAllLocations();
        if (!locations.isEmpty()) {
            System.out.println("   ✓ Found " + locations.size() + " locations");
        }


        System.out.println("\n2️⃣  Customer starts charging session...");
        Customer demoCustomer = CustomerManager.getInstance().getCustomerById("CUST-2024-001");
        if (demoCustomer == null) {
            demoCustomer = new Customer("CUST-2024-001", "Demo Customer")
                    .withAccountBalance(new BigDecimal("100.00"));
            CustomerManager.getInstance().addCustomer(demoCustomer);
        }

        List<ChargingPoint> available = ChargingPointManager.getInstance().getAvailableChargingPoints();
        if (!available.isEmpty()) {
            ChargingPoint cp = available.get(0);
            ChargingSession session = ChargingSessionManager.getInstance().startSession(cp, demoCustomer);
            System.out.println("   ✓ Session " + session.getId() + " started at " + cp.getId());


            System.out.println("\n3️⃣  Charging in progress...");
            session.setDuration(25);
            session.setPower(150);
            session.calculateEnergyAndCost();
            System.out.println("   ⚡ Energy: " + session.getEnergyConsumed() + " kWh");
            System.out.println("   💰 Cost: " + session.getEstimatedCost() + " €");


            System.out.println("\n4️⃣  Stopping charging session...");
            ChargingSessionManager.getInstance().stopSession(session);
            System.out.println("   ✓ Session completed");


            System.out.println("\n5️⃣  Processing payment...");
            demoCustomer.chargeAccount(session.getEstimatedCost());
            System.out.println("   ✓ Payment processed");
            System.out.println("   💳 New balance: " + demoCustomer.getAccountBalance() + " €");
        }

        System.out.println("\n✓ Demo scenario completed successfully!");
        System.out.println("════════════════════════════════════════════════");
    }



    private static int getIntInput() {
        try {
            String input = scanner.nextLine();
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}