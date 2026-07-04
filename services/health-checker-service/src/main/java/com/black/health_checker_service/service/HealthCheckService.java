package com.black.health_checker_service.service;

import com.black.health_checker_service.model.MetricPayload;
import com.black.health_checker_service.model.MonitoredService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class HealthCheckService {

    private static final Logger log = LoggerFactory.getLogger(HealthCheckService.class);
    private final RestTemplate restTemplate;

    public MetricPayload check(MonitoredService service) {
        long start = System.currentTimeMillis();
        try {
            int statusCode = restTemplate.getForEntity(service.getUrl(), String.class).getStatusCode().value();
            long latency = System.currentTimeMillis() - start;
            double errorRate = statusCode >= 400 ? 1.0 : 0.0;
            log.info("Health check OK for {}: status={}, latency={}ms", service.getName(), statusCode, latency);
            return MetricPayload.builder()
                    .serviceId(service.getName())
                    .latencyMs((double) latency)
                    .errorRate(errorRate)
                    .cpuPercent(0.0)
                    .memoryPercent(0.0)
                    .build();
        } catch (Exception e) {
            long latency = System.currentTimeMillis() - start;
            log.error("Health check FAILED for {}: {} (latency={}ms)", service.getName(), e.getMessage(), latency);
            return MetricPayload.builder()
                    .serviceId(service.getName())
                    .latencyMs((double) latency)
                    .errorRate(1.0)
                    .cpuPercent(0.0)
                    .memoryPercent(0.0)
                    .build();
        }
    }
}
