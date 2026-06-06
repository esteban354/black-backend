package com.black.alert_service.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Espejo local del MetricPayload del metric-ingestion-service.
 * Se usa exclusivamente para deserializar el JSON consumido desde el topic black.metrics.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MetricPayload {

    private String serviceId;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime collectedAt;

    private Double latencyMs;
    private Double errorRate;
    private Double cpuPercent;
    private Double memoryPercent;
}
