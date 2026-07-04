package com.black.health_checker_service.service;

import com.black.health_checker_service.model.MetricPayload;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class MetricPublisherService {

    private static final Logger log = LoggerFactory.getLogger(MetricPublisherService.class);
    private final RestTemplate restTemplate;

    @Value("${metric-ingestion.url}")
    private String metricIngestionUrl;

    public void publish(MetricPayload payload) {
        try {
            restTemplate.postForEntity(metricIngestionUrl, payload, String.class);
            log.info("Metric published for service: {}", payload.getServiceId());
        } catch (Exception e) {
            log.error("Failed to publish metric for {}: {}", payload.getServiceId(), e.getMessage());
        }
    }
}
