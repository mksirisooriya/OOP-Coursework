import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TicketingSystemCLI {
    private static final String CONFIG_FILE = "config.json"; // File path to save/load configuration
    private static final String LOG_FILE = "system.log"; // Log file path for system activities
    private Configuration configuration; // Stores system configuration details
    private TicketPool ticketPool; // Shared ticket pool for customers and vendors
    private List<Thread> vendorThreads = new ArrayList<>(); // List of all vendor threads
    private List<Thread> customerThreads = new ArrayList<>(); // List of all customer threads
    private int numVendors; // Number of vendor threads
    private int numCustomers; // Number of customer threads

    public static void main(String[] args) {
        new TicketingSystemCLI().run(); // Start the system by calling run method
    }

    public void run() {
        try (BufferedWriter logWriter = new BufferedWriter(new FileWriter(LOG_FILE, true))) {
            logMessage(logWriter, "System initiated.");

            Scanner scanner = new Scanner(System.in);
            System.out.println("Welcome to the Real-Time Ticketing System CLI!");

            System.out.print("Do you want to load the previous configuration? (yes/no): ");
            String loadConfigChoice = getValidYesNoInput(scanner);

            if (loadConfigChoice.equals("yes")) {
                configuration = Configuration.loadConfiguration(CONFIG_FILE); // Load saved configuration
            }

            if (configuration == null) {
                configuration = createConfiguration(scanner); // Create a new configuration if no previous config found
                configuration.saveConfiguration(CONFIG_FILE); // Save the new configuration to file
            }

            System.out.print("Enter number of vendors: ");
            numVendors = validatePositiveInput(scanner);

            System.out.print("Enter number of customers: ");
            numCustomers = validatePositiveInput(scanner);

            ticketPool = new TicketPool(configuration.getMaxTicketCapacity(), configuration.getTotalTickets());

            displayCurrentConfiguration(); // Display current configuration details

            while (true) {
                System.out.println("\nMenu:");
                System.out.println("1. Start ticketing");
                System.out.println("2. Stop ticketing");
                System.out.println("3. View ticket pool status");
                System.out.println("4. Exit");
                System.out.print("Enter your choice: ");
                int choice = validatePositiveInput(scanner);

                switch (choice) {
                    case 1 -> startTicketing(logWriter);
                    case 2 -> stopTicketing(logWriter);
                    case 3 -> System.out.println("Current Tickets: " + ticketPool.getTicketCount()); // Show tickets left
                    case 4 -> {
                        logMessage(logWriter, "System stopped.");
                        stopTicketing(logWriter);
                        System.exit(0);
                    }
                    default -> System.out.println("Invalid choice. Try again.");
                }
            }
        } catch (IOException e) {
            System.out.println("Error with logging: " + e.getMessage());
        }
    }

    private void logMessage(BufferedWriter logWriter, String message) throws IOException {
        String logEntry = String.format("[%tF %<tT] %s%n", System.currentTimeMillis(), message);
        System.out.print(logEntry);
        logWriter.write(logEntry);
        logWriter.flush();
    }

    private String getValidYesNoInput(Scanner scanner) {
        String input;
        while (true) {
            input = scanner.nextLine().trim().toLowerCase();
            if (input.equals("yes") || input.equals("y")) {
                return "yes";
            } else if (input.equals("no") || input.equals("n")) {
                return "no";
            } else {
                System.out.print("Invalid input. Please enter 'yes', 'y', 'no', or 'n': ");
            }
        }
    }

    private Configuration createConfiguration(Scanner scanner) {
        System.out.print("Enter total number of tickets: ");
        int totalTickets = validatePositiveInput(scanner);

        System.out.print("Enter ticket release rate (seconds): ");
        int ticketReleaseRate = validatePositiveInput(scanner);

        System.out.print("Enter customer retrieval rate (seconds): ");
        int customerRetrievalRate = validatePositiveInput(scanner);

        System.out.print("Enter max ticket capacity: ");
        int maxTicketCapacity = validatePositiveInput(scanner);

        return new Configuration(totalTickets, ticketReleaseRate, customerRetrievalRate, maxTicketCapacity);
    }

    private void displayCurrentConfiguration() {
        System.out.println("\nCurrent Configuration:");
        System.out.println("Total Tickets: " + configuration.getTotalTickets());
        System.out.println("Ticket Release Rate (s): " + configuration.getTicketReleaseRate());
        System.out.println("Customer Retrieval Rate (s): " + configuration.getCustomerRetrievalRate());
        System.out.println("Max Ticket Capacity: " + configuration.getMaxTicketCapacity());
        System.out.println("Number of Vendors: " + numVendors);
        System.out.println("Number of Customers: " + numCustomers);
    }

    private void startTicketing(BufferedWriter logWriter) throws IOException {
        // Start vendor threads
        for (int i = 1; i <= numVendors; i++) {
            Thread vendorThread = new Thread(new Vendor(ticketPool, 1, configuration.getTicketReleaseRate(), i, logWriter));
            vendorThreads.add(vendorThread);
            vendorThread.start();
        }
        // Start customer threads
        for (int i = 1; i <= numCustomers; i++) {
            Thread customerThread = new Thread(new Customer(ticketPool, configuration.getCustomerRetrievalRate(), i, logWriter));
            customerThreads.add(customerThread);
            customerThread.start();
        }

        logMessage(logWriter, "Ticketing system started with " + numVendors + " vendors and " + numCustomers + " customers.");
    }

    private void stopTicketing(BufferedWriter logWriter) throws IOException {
        // Stop all vendor and customer threads
        for (Thread thread : vendorThreads) thread.interrupt();
        for (Thread thread : customerThreads) thread.interrupt();
        vendorThreads.clear();
        customerThreads.clear();
        logMessage(logWriter, "Ticketing system stopped. All vendor and customer threads have been terminated.");
    }

    private int validatePositiveInput(Scanner scanner) {
        int input;
        while (true) {
            try {
                input = Integer.parseInt(scanner.nextLine().trim());
                if (input > 0) break;
                System.out.print("Please enter a positive number: ");
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Please enter a positive number: ");
            }
        }
        return input;
    }
}