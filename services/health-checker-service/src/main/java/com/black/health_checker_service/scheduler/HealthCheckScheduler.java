package com.black.health_checker_service.scheduler;

import com.black.health_checker_service.model.MonitoredService;
import com.black.health_checker_service.service.HealthCheckService;
import com.black.health_checker_service.service.MetricPublisherService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class HealthCheckScheduler {

    private static final Logger log = LoggerFactory.getLogger(HealthCheckScheduler.class);
    private final List<MonitoredService> monitoredServices;
    private final HealthCheckService healthCheckService;
    private final MetricPublisherService metricPublisherService;

    @Scheduled(fixedDelayString = "${polling.interval-ms}")
    public void runHealthChecks() {
        log.info("Starting health checks for {} service(s)", monitoredServices.size());
        for (MonitoredService service : monitoredServices) {
            try {
                var payload = healthCheckService.check(service);
                metricPublisherService.publish(payload);
            } catch (Exception e) {
                log.error("Unexpected error checking service {}: {}", service.getName(), e.getMessage());
            }
        }
        log.info("Health checks completed");
    }
}
