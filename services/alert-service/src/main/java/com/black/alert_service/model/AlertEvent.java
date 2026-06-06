package com.black.alert_service.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlertEvent {

    // ID de la alerta persistida en PostgreSQL
    private Long alertId;

    // Identificador del servicio que generó la alerta
    private String serviceId;

    // Tipo de anomalía detectada: LATENCY, ERROR_RATE, CPU, MEMORY
    private String type;

    // Severidad calculada: MEDIUM, HIGH o CRITICAL
    private String severity;

    // Timestamp exacto en que se confirmó la alerta, serializado en formato ISO-8601
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime triggeredAt;
}
