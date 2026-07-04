package com.black.incident_service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO de respuesta REST que expone los datos de un incidente al frontend.
 * Incluye todos los campos de la entidad Incident más un mensaje opcional de contexto.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IncidentResponseDto {

    private Long id;
    private Long alertId;
    private String serviceId;
    private String alertType;
    private String severity;

    // Estado actual: OPEN, INVESTIGATING, RESOLVED
    private String status;

    // Modo de operación: ASSISTED o AUTONOMOUS
    private String mode;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime openedAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime resolvedAt;

    // Mensaje opcional de contexto o resultado de una acción
    private String message;
}
