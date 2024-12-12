package com.ticketing.system.repository;

import com.ticketing.system.entity.SystemLogEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SystemLogRepository extends JpaRepository<SystemLogEntry, Long> {
    List<SystemLogEntry> findAllByOrderByTimestampDesc();
    List<SystemLogEntry> findByEventTypeOrderByTimestampDesc(String eventType);
    List<SystemLogEntry> findTop100ByOrderByTimestampDesc();
}