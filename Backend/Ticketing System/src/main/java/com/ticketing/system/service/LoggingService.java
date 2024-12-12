package com.ticketing.system.service;

import com.ticketing.system.entity.SystemLogEntry;
import com.ticketing.system.repository.SystemLogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class LoggingService {
    private static final Logger logger = LoggerFactory.getLogger(LoggingService.class);

    @Autowired
    private SystemLogRepository logRepository;

    private final Map<String, Long> lastLogTimes = new ConcurrentHashMap<>();
    private static final long LOG_INTERVAL = 2000; // 2 seconds between similar logs

    public void logVendorAction(int vendorId, String message) {
        String key = "VENDOR-" + vendorId + "-" + message;
        if (canLog(key)) {
            SystemLogEntry log = new SystemLogEntry();
            log.setEventType("VENDOR");
            log.setMessage(message);
            log.setActorId(vendorId);
            log.setTimestamp(LocalDateTime.now());
            logRepository.save(log);
            logger.info("Vendor {}: {}", vendorId, message);
            updateLastLogTime(key);
        }
    }

    public void logCustomerAction(int customerId, String message) {
        String key = "CUSTOMER-" + customerId + "-" + message;
        if (canLog(key)) {
            SystemLogEntry log = new SystemLogEntry();
            log.setEventType("CUSTOMER");
            log.setMessage(message);
            log.setActorId(customerId);
            log.setTimestamp(LocalDateTime.now());
            logRepository.save(log);
            logger.info("Customer {}: {}", customerId, message);
            updateLastLogTime(key);
        }
    }

    public void logSystemEvent(String message) {
        String key = "SYSTEM-" + message;
        if (canLog(key)) {
            SystemLogEntry log = new SystemLogEntry();
            log.setEventType("SYSTEM");
            log.setMessage(message);
            log.setTimestamp(LocalDateTime.now());
            logRepository.save(log);
            logger.info("System Event: {}", message);
            updateLastLogTime(key);
        }
    }

    private synchronized boolean canLog(String key) {
        long currentTime = System.currentTimeMillis();
        Long lastLogTime = lastLogTimes.get(key);
        return lastLogTime == null || (currentTime - lastLogTime) >= LOG_INTERVAL;
    }

    private void updateLastLogTime(String key) {
        lastLogTimes.put(key, System.currentTimeMillis());

        // Clean up old entries
        long currentTime = System.currentTimeMillis();
        lastLogTimes.entrySet().removeIf(entry ->
                (currentTime - entry.getValue()) > LOG_INTERVAL * 10);
    }

    public List<SystemLogEntry> getRecentLogs() {
        return logRepository.findTop100ByOrderByTimestampDesc();
    }

    public List<SystemLogEntry> getLogsByType(String eventType) {
        return logRepository.findByEventTypeOrderByTimestampDesc(eventType);
    }

    public void clearOldLogs() {
        lastLogTimes.clear();
    }
}