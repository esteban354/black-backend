package com.black.health_checker_service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MetricPayload {
    private String serviceId;
    private Double latencyMs;
    private Double errorRate;
    private Double cpuPercent;
    private Double memoryPercent;
}
