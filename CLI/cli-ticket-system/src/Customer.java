import java.io.BufferedWriter;
import java.io.IOException;

public class Customer implements Runnable {
    private final TicketPool ticketPool;
    private final int retrievalInterval;
    private final int customerId;
    private final BufferedWriter logWriter;

    public Customer(TicketPool ticketPool, int retrievalInterval, int customerId, BufferedWriter logWriter) {
        this.ticketPool = ticketPool; // Set TicketPool object
        this.retrievalInterval = retrievalInterval; // Set the interval for ticket retrieval
        this.customerId = customerId; // Set the customer ID
        this.logWriter = logWriter; // Set the log writer for customer actions
    }

    @Override
    public void run() {
        try {
            while (true) { // Customer tries to purchase a ticket from the pool
                String ticket = ticketPool.removeTicket(customerId, logWriter);
                if (ticket != null) {
                    logAction("[INFO]", "Customer-" + customerId + " purchased " + ticket + ".");
                }
                Thread.sleep(retrievalInterval * 1000L); // Wait for the next attempt
            }
        } catch (InterruptedException e) {
            logAction("[INFO]", "Customer-" + customerId + " stopped.");
            System.out.println("Customer-" + customerId + " stopped.");
        }
    }

    private void logAction(String level, String message) { // Logs the action of the customer
        String logEntry = String.format("[%tF %<tT] %s %s%n", System.currentTimeMillis(), level, message);
        try {
            logWriter.write(logEntry);
            logWriter.flush();
        } catch (IOException e) {
            System.out.println("Error logging customer action: " + e.getMessage());
        }
    }
}