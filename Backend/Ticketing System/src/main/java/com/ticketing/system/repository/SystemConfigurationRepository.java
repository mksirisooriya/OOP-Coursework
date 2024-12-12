package com.ticketing.system.repository;

import com.ticketing.system.entity.SystemConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SystemConfigurationRepository extends JpaRepository<SystemConfiguration, Long> {
    SystemConfiguration findByActive(boolean active);
}