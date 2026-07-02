package com.black.incident_service.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * POJO publicado en el topic black.incidents para que el ai-agent-service lo consuma.
 * Contiene los datos clave del incidente recién creado, serializado en formato ISO-8601.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IncidentEvent {

    // ID del incidente generado en PostgreSQL
    private Long incidentId;

    // ID de la alerta que originó el incidente
    private Long alertId;

    // Servicio afectado
    private String serviceId;

    // Severidad heredada de la alerta: MEDIUM, HIGH, CRITICAL
    private String severity;

    // Estado inicial del incidente (siempre OPEN al publicar)
    private String status;

    // Modo de operación: ASSISTED o AUTONOMOUS
    private String mode;

    // Timestamp de apertura serializado en formato ISO-8601
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime openedAt;
}
