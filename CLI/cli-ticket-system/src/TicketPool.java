import java.io.BufferedWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class TicketPool {
    private final List<String> tickets = new LinkedList<>(); // List to store tickets
    private final int maxCapacity;
    private int totalTicketsAdded = 0;
    private final int totalTickets;

    public TicketPool(int maxCapacity, int totalTickets) {
        this.maxCapacity = maxCapacity; // Set maximum capacity of the pool
        this.totalTickets = totalTickets; // Set total tickets for the system
    }

    public synchronized boolean addTickets(int numTickets, int vendorId, BufferedWriter logWriter) {
        if (totalTicketsAdded >= totalTickets) {
            logAction(logWriter, "[INFO]", "Vendor-" + vendorId + ": Total ticket limit reached. Cannot add more tickets.");
            System.out.println("Vendor-" + vendorId + ": Total ticket limit reached. Cannot add more tickets.");
            return false; // No more tickets can be added
        }

        if (tickets.size() >= maxCapacity) {
            logAction(logWriter, "[WARNING]", "Vendor-" + vendorId + ": Cannot add more tickets. Ticket pool is full. Waiting for space...");
            System.out.println("Vendor-" + vendorId + ": Cannot add more tickets. Ticket pool is full. Waiting for space...");
            return true;
        }

        int ticketsToAdd = Math.min(numTickets, totalTickets - totalTicketsAdded);
        for (int i = 0; i < ticketsToAdd; i++) {
            tickets.add("Ticket-" + (totalTicketsAdded + 1));
            totalTicketsAdded++;
        }
        System.out.println("Vendor-" + vendorId + " added " + ticketsToAdd + " tickets. Current total: " + tickets.size());
        logAction(logWriter, "[INFO]", "Vendor-" + vendorId + " added " + ticketsToAdd + " tickets.");
        notifyAll();
        return true; // Notify waiting threads
    }

    public synchronized String removeTicket(int customerId, BufferedWriter logWriter) {
        while (tickets.isEmpty()) {
            logAction(logWriter, "[WARNING]", "Customer-" + customerId + " tried to purchase a ticket, but no tickets were available.");
            System.out.println("Customer-" + customerId + ": No tickets available. Waiting...");
            try {
                wait(); // Wait if there are no tickets
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return null;
            }
        }

        String ticket = tickets.remove(0);
        System.out.println("Customer-" + customerId + " purchased " + ticket + ". Remaining: " + tickets.size());
        logAction(logWriter, "[INFO]", "Customer-" + customerId + " purchased " + ticket + ".");
        notifyAll(); // Notify other threads
        return ticket;
    }

    private void logAction(BufferedWriter logWriter, String level, String message) {
        String logEntry = String.format("[%tF %<tT] %s %s%n", System.currentTimeMillis(), level, message);
        try {
            logWriter.write(logEntry);
            logWriter.flush();
        } catch (IOException e) {
            System.out.println("Error logging pool action: " + e.getMessage());
        }
    }

    public synchronized int getTicketCount() {
        return tickets.size();
    }
}