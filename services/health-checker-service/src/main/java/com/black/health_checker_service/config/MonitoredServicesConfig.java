package com.black.health_checker_service.config;

import com.black.health_checker_service.model.MonitoredService;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class MonitoredServicesConfig {

    @Bean
    @ConfigurationProperties(prefix = "monitored-services")
    public List<MonitoredService> monitoredServices() {
        return new ArrayList<>();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
