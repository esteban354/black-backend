package com.black.incident_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@EnableKafka
@SpringBootApplication
public class IncidentServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(IncidentServiceApplication.class, args);
	}

}
