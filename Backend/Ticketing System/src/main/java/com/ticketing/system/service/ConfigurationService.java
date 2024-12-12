package com.ticketing.system.service;

import com.ticketing.system.entity.SystemConfiguration;
import com.ticketing.system.repository.SystemConfigurationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ConfigurationService {
    private static final Logger logger = LoggerFactory.getLogger(ConfigurationService.class);

    @Autowired
    private SystemConfigurationRepository configRepository;

    @Transactional
    public SystemConfiguration saveConfiguration(SystemConfiguration config) {
        try {
            // Deactivate existing configurations
            SystemConfiguration existingConfig = configRepository.findByActive(true);
            if (existingConfig != null) {
                existingConfig.setActive(false);
                configRepository.save(existingConfig);
                logger.info("Deactivated previous configuration: {}", existingConfig.getId());
            }

            config.setActive(true);
            SystemConfiguration savedConfig = configRepository.save(config);
            logger.info("Saved new configuration: {}", savedConfig.getId());
            return savedConfig;
        } catch (Exception e) {
            logger.error("Error saving configuration: " + e.getMessage(), e);
            throw e;
        }
    }

    public SystemConfiguration getActiveConfiguration() {
        try {
            return configRepository.findByActive(true);
        } catch (Exception e) {
            logger.error("Error getting active configuration: " + e.getMessage(), e);
            throw e;
        }
    }
}