DROP TABLE IF EXISTS tickets;
DROP TABLE IF EXISTS system_logs;
DROP TABLE IF EXISTS system_configuration;

CREATE TABLE IF NOT EXISTS tickets (
                                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                       ticket_number VARCHAR(255) NOT NULL,
    available BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    vendor_id INT,
    customer_id INT
    );

CREATE TABLE IF NOT EXISTS system_logs (
                                           id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                           event_type VARCHAR(50) NOT NULL,
    message VARCHAR(1000) NOT NULL,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    actor_id INT
    );

CREATE TABLE IF NOT EXISTS system_configuration (
                                                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                                    total_tickets INT NOT NULL,
                                                    ticket_release_rate INT NOT NULL,
                                                    customer_retrieval_rate INT NOT NULL,
                                                    max_ticket_capacity INT NOT NULL,
                                                    active BOOLEAN DEFAULT TRUE
);