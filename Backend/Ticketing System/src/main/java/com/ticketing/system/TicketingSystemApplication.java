package com.ticketing.system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan("com.ticketing.system.entity")
@EnableJpaRepositories("com.ticketing.system.repository")
public class TicketingSystemApplication {
	public static void main(String[] args) {
		SpringApplication.run(TicketingSystemApplication.class, args);
	}
}