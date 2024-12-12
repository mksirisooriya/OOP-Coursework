package com.ticketing.system.service;

import com.ticketing.system.entity.Ticket;
import com.ticketing.system.entity.SystemConfiguration;
import com.ticketing.system.repository.TicketRepository;
import com.ticketing.system.repository.SystemConfigurationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class TicketService {
    private static final Logger logger = LoggerFactory.getLogger(TicketService.class);

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private SystemConfigurationRepository configRepository;

    @Autowired
    private LoggingService loggingService;

    private final AtomicLong ticketCounter = new AtomicLong(0);
    private boolean hasLoggedTotalLimit = false;
    private boolean hasLoggedCapacityLimit = false;
    private boolean hasLoggedNoTickets = false;

    @PostConstruct
    private synchronized void init() {
        try {
            // Initialize counter based on actual database state
            long count = ticketRepository.count();
            ticketCounter.set(count);
            logger.info("Initialized ticket counter to: {}", count);
        } catch (Exception e) {
            logger.error("Error initializing ticket counter: {}", e.getMessage());
            ticketCounter.set(0);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public synchronized Ticket addTicket(int vendorId) {
        try {
            SystemConfiguration config = configRepository.findByActive(true);
            if (config == null) {
                logger.error("No active configuration found");
                loggingService.logSystemEvent("No active configuration found. Please configure the system.");
                return null;
            }

            // Get current state atomically
            long currentTotal = ticketCounter.get();
            long availableCount = ticketRepository.countByAvailable(true);

            // Check total limit
            if (currentTotal >= config.getTotalTickets()) {
                if (!hasLoggedTotalLimit) {
                    loggingService.logSystemEvent("Total ticket limit reached (" + config.getTotalTickets() + " tickets)");
                    hasLoggedTotalLimit = true;
                }
                return null;
            }

            // Check capacity
            if (availableCount >= config.getMaxTicketCapacity()) {
                if (!hasLoggedCapacityLimit) {
                    loggingService.logSystemEvent("Maximum capacity reached (" + config.getMaxTicketCapacity() + " tickets)");
                    hasLoggedCapacityLimit = true;
                }
                return null;
            }

            // Increment counter and create ticket
            long newTicketNumber = ticketCounter.incrementAndGet();
            if (newTicketNumber > config.getTotalTickets()) {
                ticketCounter.decrementAndGet(); // Roll back the increment
                return null;
            }

            Ticket ticket = new Ticket();
            ticket.setTicketNumber("Ticket-" + newTicketNumber);
            ticket.setAvailable(true);
            ticket.setVendorId(vendorId);

            Ticket savedTicket = ticketRepository.save(ticket);
            loggingService.logVendorAction(vendorId, "Added ticket: " + savedTicket.getTicketNumber());

            hasLoggedTotalLimit = false;
            hasLoggedCapacityLimit = false;

            return savedTicket;

        } catch (Exception e) {
            String errorMsg = "Error adding ticket for vendor " + vendorId + ": " + e.getMessage();
            logger.error(errorMsg, e);
            loggingService.logSystemEvent("Error: " + errorMsg);
            throw new RuntimeException(errorMsg);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public synchronized Ticket purchaseTicket(int customerId) {
        try {
            List<Ticket> availableTickets = ticketRepository.findFirstAvailableTicket();

            if (availableTickets.isEmpty()) {
                if (!hasLoggedNoTickets) {
                    loggingService.logSystemEvent("No tickets available for purchase");
                    hasLoggedNoTickets = true;
                }
                return null;
            }

            hasLoggedNoTickets = false;

            Ticket ticket = availableTickets.get(0);
            ticket.setAvailable(false);
            ticket.setCustomerId(customerId);

            Ticket purchasedTicket = ticketRepository.save(ticket);
            loggingService.logCustomerAction(customerId, "Purchased " + ticket.getTicketNumber());

            long remainingAvailable = ticketRepository.countByAvailable(true);
            if (remainingAvailable == 0) {
                loggingService.logSystemEvent("All tickets have been sold");
            }

            return purchasedTicket;

        } catch (Exception e) {
            String errorMsg = "Error purchasing ticket for customer " + customerId + ": " + e.getMessage();
            logger.error(errorMsg, e);
            loggingService.logSystemEvent("Error: " + errorMsg);
            throw new RuntimeException(errorMsg);
        }
    }

    public long getAvailableTicketCount() {
        try {
            return ticketRepository.countByAvailable(true);
        } catch (Exception e) {
            logger.error("Error getting available ticket count: {}", e.getMessage());
            return 0;
        }
    }

    public long getTotalTicketCount() {
        try {
            return ticketCounter.get();
        } catch (Exception e) {
            logger.error("Error getting total ticket count: {}", e.getMessage());
            return 0;
        }
    }

    public List<Ticket> getAllAvailableTickets() {
        try {
            return ticketRepository.findByAvailable(true);
        } catch (Exception e) {
            logger.error("Error getting all available tickets: {}", e.getMessage());
            throw new RuntimeException("Failed to get available tickets");
        }
    }

    public long getRemainingTicketCapacity() {
        try {
            SystemConfiguration config = configRepository.findByActive(true);
            if (config == null) {
                return 0;
            }
            return Math.max(0, config.getTotalTickets() - ticketCounter.get());
        } catch (Exception e) {
            logger.error("Error getting remaining ticket capacity: {}", e.getMessage());
            throw new RuntimeException("Failed to get remaining ticket capacity");
        }
    }

    @Transactional
    public synchronized void resetSystem() {
        try {
            ticketCounter.set(0);
            hasLoggedTotalLimit = false;
            hasLoggedCapacityLimit = false;
            hasLoggedNoTickets = false;
            loggingService.logSystemEvent("System has been reset");
            logger.info("System reset completed");
        } catch (Exception e) {
            logger.error("Error resetting system: {}", e.getMessage());
            throw new RuntimeException("Failed to reset system");
        }
    }
}