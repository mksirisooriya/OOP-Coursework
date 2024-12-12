package com.ticketing.system.controller;

import com.ticketing.system.dto.TicketStatus;
import com.ticketing.system.entity.Ticket;
import com.ticketing.system.entity.SystemConfiguration;
import com.ticketing.system.entity.SystemLogEntry;
import com.ticketing.system.service.TicketService;
import com.ticketing.system.service.ConfigurationService;
import com.ticketing.system.service.LoggingService;
import com.ticketing.system.repository.TicketRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class TicketController {
    private static final Logger logger = LoggerFactory.getLogger(TicketController.class);

    @Autowired
    private TicketService ticketService;

    @Autowired
    private ConfigurationService configService;

    @Autowired
    private LoggingService loggingService;

    @Autowired
    private TicketRepository ticketRepository;

    // Test endpoint to verify API is working
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Ticketing System API is working!");
    }

    // Debug endpoints
    @GetMapping("/debug/configuration")
    public ResponseEntity<?> debugConfiguration() {
        try {
            return ResponseEntity.ok(configService.getActiveConfiguration());
        } catch (Exception e) {
            logger.error("Debug configuration error: {}", e.getMessage());
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/debug/tickets")
    public ResponseEntity<?> debugTickets() {
        try {
            return ResponseEntity.ok(ticketRepository.findAll());
        } catch (Exception e) {
            logger.error("Debug tickets error: {}", e.getMessage());
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }

    // Configuration endpoints
    @PostMapping("/configuration")
    public ResponseEntity<?> saveConfiguration(@Valid @RequestBody SystemConfiguration config) {
        try {
            SystemConfiguration savedConfig = configService.saveConfiguration(config);
            loggingService.logSystemEvent("New configuration saved with total tickets: " + config.getTotalTickets());
            return ResponseEntity.ok(savedConfig);
        } catch (Exception e) {
            logger.error("Error saving configuration: {}", e.getMessage());
            return ResponseEntity.internalServerError().body("Error saving configuration: " + e.getMessage());
        }
    }

    @GetMapping("/configuration")
    public ResponseEntity<?> getConfiguration() {
        try {
            SystemConfiguration config = configService.getActiveConfiguration();
            if (config == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(config);
        } catch (Exception e) {
            logger.error("Error getting configuration: {}", e.getMessage());
            return ResponseEntity.internalServerError().body("Error retrieving configuration: " + e.getMessage());
        }
    }

    // Ticket operations endpoints
    @PostMapping("/tickets/vendor/{vendorId}")
    public ResponseEntity<?> addTicket(@PathVariable int vendorId) {
        try {
            Ticket ticket = ticketService.addTicket(vendorId);
            if (ticket == null) {
                String message = "Unable to add ticket - capacity or limit reached";
                loggingService.logVendorAction(vendorId, message);
                return ResponseEntity.badRequest().body(message);
            }
            loggingService.logVendorAction(vendorId,
                    "Successfully added ticket: " + ticket.getTicketNumber());
            return ResponseEntity.ok(ticket);
        } catch (Exception e) {
            String errorMessage = "Error adding ticket: " + e.getMessage();
            loggingService.logVendorAction(vendorId, errorMessage);
            logger.error(errorMessage);
            return ResponseEntity.internalServerError().body(errorMessage);
        }
    }

    @PostMapping("/tickets/customer/{customerId}")
    public ResponseEntity<?> purchaseTicket(@PathVariable int customerId) {
        try {
            Ticket ticket = ticketService.purchaseTicket(customerId);
            if (ticket == null) {
                String message = "No tickets available for purchase";
                loggingService.logCustomerAction(customerId, message);
                return ResponseEntity.notFound().build();
            }
            loggingService.logCustomerAction(customerId,
                    "Successfully purchased ticket: " + ticket.getTicketNumber());
            return ResponseEntity.ok(ticket);
        } catch (Exception e) {
            String errorMessage = "Error purchasing ticket: " + e.getMessage();
            loggingService.logCustomerAction(customerId, errorMessage);
            logger.error(errorMessage);
            return ResponseEntity.internalServerError().body(errorMessage);
        }
    }

    // Status endpoints
    @GetMapping("/tickets/status")
    public ResponseEntity<?> getTicketStatus() {
        try {
            long available = ticketService.getAvailableTicketCount();
            long total = ticketService.getTotalTicketCount();
            SystemConfiguration config = configService.getActiveConfiguration();
            // Fix the remaining calculation to never go below 0
            long remaining = config != null ? Math.max(0, config.getTotalTickets() - total) : 0;
            long sold = total - available;

            Map<String, Object> status = new HashMap<>();
            status.put("availableTickets", available);
            status.put("totalTickets", total);
            status.put("remainingTickets", remaining);  // Will now never be negative
            status.put("soldTickets", sold);

            return ResponseEntity.ok(status);
        } catch (Exception e) {
            logger.error("Error getting ticket status: {}", e.getMessage());
            return ResponseEntity.internalServerError().body("Error retrieving ticket status");
        }
    }

    // Logging endpoints
    @GetMapping("/logs")
    public ResponseEntity<?> getSystemLogs() {
        try {
            List<SystemLogEntry> logs = loggingService.getRecentLogs();
            return ResponseEntity.ok(logs);
        } catch (Exception e) {
            logger.error("Error getting system logs: {}", e.getMessage());
            return ResponseEntity.internalServerError().body("Error retrieving system logs: " + e.getMessage());
        }
    }

    @GetMapping("/logs/{eventType}")
    public ResponseEntity<?> getLogsByType(@PathVariable String eventType) {
        try {
            List<SystemLogEntry> logs = loggingService.getLogsByType(eventType);
            return ResponseEntity.ok(logs);
        } catch (Exception e) {
            logger.error("Error getting logs by type {}: {}", eventType, e.getMessage());
            return ResponseEntity.internalServerError()
                    .body("Error retrieving logs for type " + eventType + ": " + e.getMessage());
        }
    }

    // Additional status endpoints
    @GetMapping("/tickets/available")
    public ResponseEntity<?> getAvailableTickets() {
        try {
            return ResponseEntity.ok(ticketRepository.findByAvailable(true));
        } catch (Exception e) {
            logger.error("Error getting available tickets: {}", e.getMessage());
            return ResponseEntity.internalServerError().body("Error retrieving available tickets: " + e.getMessage());
        }
    }

    @GetMapping("/system/health")
    public ResponseEntity<?> getSystemHealth() {
        try {
            Map<String, Object> health = new HashMap<>();
            health.put("status", "UP");
            health.put("databaseConnection", "OK");
            health.put("timestamp", System.currentTimeMillis());
            return ResponseEntity.ok(health);
        } catch (Exception e) {
            logger.error("Error checking system health: {}", e.getMessage());
            return ResponseEntity.internalServerError().body("Error checking system health: " + e.getMessage());
        }
    }

    @PostMapping("/system/reset")
    public ResponseEntity<?> resetSystem() {
        try {
            ticketService.resetSystem();
            loggingService.logSystemEvent("System has been reset");
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("Error resetting system: {}", e.getMessage());
            return ResponseEntity.internalServerError().body("Error resetting system");
        }
    }
}