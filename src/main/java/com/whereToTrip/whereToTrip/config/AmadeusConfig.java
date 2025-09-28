package com.whereToTrip.whereToTrip.config;

import com.amadeus.Amadeus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmadeusConfig {

	@Value("${amadeus.api.key}")
	private String apiKey;

	@Value("${amadeus.api.secret}")
	private String apiSecret;

	@Value("${amadeus.environment:test}")
	private String environment;

	@Bean
	public Amadeus amadeus() {
		if ("prod".equalsIgnoreCase(environment) || "production".equalsIgnoreCase(environment)) {
			return Amadeus.builder(apiKey, apiSecret).setHostname("api.amadeus.com").build();
		}
		return Amadeus.builder(apiKey, apiSecret).setHostname("test.api.amadeus.com").build();
	}
}


