package com.black.incident_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de entrada para actualizar el estado y modo de un incidente desde el frontend.
 * Permite al usuario aprobar o cambiar la estrategia de respuesta del agente.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateIncidentDto {

    // Nuevo estado: OPEN, INVESTIGATING, RESOLVED
    private String status;

    // Nuevo modo: ASSISTED o AUTONOMOUS
    private String mode;
}
