-- Reset all tables
SET FOREIGN_KEY_CHECKS = 0;

TRUNCATE TABLE tickets;
TRUNCATE TABLE system_logs;
TRUNCATE TABLE system_configuration;

SET FOREIGN_KEY_CHECKS = 1;

-- Reset auto-increment counters
ALTER TABLE tickets AUTO_INCREMENT = 1;
ALTER TABLE system_logs AUTO_INCREMENT = 1;
ALTER TABLE system_configuration AUTO_INCREMENT = 1;

-- Insert initial system log
INSERT INTO system_logs (event_type, message, timestamp)
VALUES ('SYSTEM', 'Database reset completed', CURRENT_TIMESTAMP);