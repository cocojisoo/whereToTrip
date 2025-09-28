package com.whereToTrip.whereToTrip;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication
public class WhereToTripApplication {

	public static void main(String[] args) {
		SpringApplication.run(WhereToTripApplication.class, args);
	}

    @Bean
    CommandLineRunner verifyDatabaseConnection(JdbcTemplate jdbcTemplate) {
        return args -> {
            try {
                Integer one = jdbcTemplate.queryForObject("SELECT 1", Integer.class);
                System.out.println("[DB] Connectivity OK, SELECT 1 => " + one);
            } catch (Exception ex) {
                System.err.println("[DB] Connectivity FAILED: " + ex.getMessage());
            }
        };
    }

}
