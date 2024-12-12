import java.io.BufferedWriter;
import java.io.IOException;

public class Vendor implements Runnable {
    private final TicketPool ticketPool;
    private final int ticketsPerRelease;
    private final int releaseInterval;
    private final int vendorId;
    private final BufferedWriter logWriter; // Log writer to write log messages to a fil

    public Vendor(TicketPool ticketPool, int ticketsPerRelease, int releaseInterval, int vendorId, BufferedWriter logWriter) {
        this.ticketPool = ticketPool; // Initialize the shared TicketPool
        this.ticketsPerRelease = ticketsPerRelease; // Number of tickets to release
        this.releaseInterval = releaseInterval; // Release time interval (in seconds)
        this.vendorId = vendorId; // Unique identifier for the Vendor
        this.logWriter = logWriter; // Log writer for tracking activities
    }

    @Override
    public void run() {
        try {
            while (true) { // Attempt to add tickets to the TicketPool
                if (!ticketPool.addTickets(ticketsPerRelease, vendorId, logWriter)) {
                    logAction("[INFO]", "Vendor-" + vendorId + ": Total ticket limit reached. Cannot add more tickets.");
                    break; // Stop if the total ticket limit is reached
                }
                logAction("[INFO]", "Vendor-" + vendorId + " released " + ticketsPerRelease + " tickets.");
                Thread.sleep(releaseInterval * 1000L); // Wait for the specified release interval
            }
        } catch (InterruptedException e) {
            logAction("[INFO]", "Vendor-" + vendorId + " stopped.");
            System.out.println("Vendor-" + vendorId + " stopped."); // Log and print the message when the vendor is stopped
        }
    }

    private void logAction(String level, String message) { // Logs the action of the Vendor
        String logEntry = String.format("[%tF %<tT] %s %s%n", System.currentTimeMillis(), level, message);
        try {
            logWriter.write(logEntry);
            logWriter.flush();
        } catch (IOException e) {
            System.out.println("Error logging vendor action: " + e.getMessage());
        }
    }
}