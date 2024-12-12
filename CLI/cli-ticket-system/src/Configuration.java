import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;

public class Configuration {
    private int totalTickets;
    private int ticketReleaseRate;
    private int customerRetrievalRate;
    private int maxTicketCapacity;

    public Configuration(int totalTickets, int ticketReleaseRate, int customerRetrievalRate, int maxTicketCapacity) {
        this.totalTickets = totalTickets;
        this.ticketReleaseRate = ticketReleaseRate;
        this.customerRetrievalRate = customerRetrievalRate;
        this.maxTicketCapacity = maxTicketCapacity;
    }

    public int getTotalTickets() {
        return totalTickets;
    }

    public int getTicketReleaseRate() {
        return ticketReleaseRate;
    }

    public int getCustomerRetrievalRate() {
        return customerRetrievalRate;
    }

    public int getMaxTicketCapacity() {
        return maxTicketCapacity;
    }

    public void saveConfiguration(String filePath) { // Save configuration as JSON file
        try (Writer writer = new FileWriter(filePath)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(this, writer);
            System.out.println("Configuration saved successfully as JSON.");
        } catch (IOException e) {
            System.out.println("Error saving configuration: " + e.getMessage());
        }
    }

    public static Configuration loadConfiguration(String filePath) { // Load configuration from a JSON file
        try (Reader reader = new FileReader(filePath)) {
            Gson gson = new Gson();
            Configuration config = gson.fromJson(reader, Configuration.class);
            System.out.println("Configuration loaded successfully from JSON.");
            return config;
        } catch (IOException e) {
            System.out.println("Error loading configuration: " + e.getMessage());
            return null;
        }
    }

    @Override
    public String toString() {
        return "Configuration:\n" +
                "Total Tickets: " + totalTickets + "\n" +
                "Ticket Release Rate (s): " + ticketReleaseRate + "\n" +
                "Customer Retrieval Rate (s): " + customerRetrievalRate + "\n" +
                "Max Ticket Capacity: " + maxTicketCapacity;
    }
}