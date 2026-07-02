package com.black.incident_service.model;

import com.black.incident_service.enums.AlertType;
import com.black.incident_service.enums.Severity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * POJO local que replica el AlertEvent publicado por el alert-service.
 * Se usa únicamente para deserializar mensajes JSON del topic black.alerts.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlertEvent {

    // ID de la alerta persistida en PostgreSQL del alert-service
    private Long alertId;

    // Identificador del servicio que generó la alerta
    private String serviceId;

    private AlertType type;

    private Severity severity;

    // Timestamp exacto en que se confirmó la alerta
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime triggeredAt;
}
