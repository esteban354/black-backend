package com.black.incident_service.controller;

import com.black.incident_service.dto.IncidentResponseDto;
import com.black.incident_service.dto.UpdateIncidentDto;
import com.black.incident_service.service.IncidentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST del incident-service.
 * Expone endpoints para que el frontend consulte incidentes y el usuario gestione su ciclo de vida.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/incidents")
@RequiredArgsConstructor
public class IncidentController {

    private final IncidentService incidentService;

    /**
     * GET /api/v1/incidents
     * Retorna todos los incidentes registrados en el sistema.
     */
    @GetMapping
    public ResponseEntity<List<IncidentResponseDto>> getAllIncidents() {
        log.info("[IncidentController] GET /api/v1/incidents");
        List<IncidentResponseDto> incidents = incidentService.getAllIncidents();
        return ResponseEntity.ok(incidents);
    }

    /**
     * GET /api/v1/incidents/service/{serviceId}
     * Retorna todos los incidentes de un servicio específico.
     */
    @GetMapping("/service/{serviceId}")
    public ResponseEntity<List<IncidentResponseDto>> getIncidentsByServiceId(
            @PathVariable String serviceId) {
        log.info("[IncidentController] GET /api/v1/incidents/service/{}", serviceId);
        List<IncidentResponseDto> incidents = incidentService.getIncidentsByServiceId(serviceId);
        return ResponseEntity.ok(incidents);
    }

    /**
     * GET /api/v1/incidents/status/{status}
     * Retorna todos los incidentes con un estado específico (OPEN, INVESTIGATING, RESOLVED).
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<IncidentResponseDto>> getIncidentsByStatus(
            @PathVariable String status) {
        log.info("[IncidentController] GET /api/v1/incidents/status/{}", status);
        List<IncidentResponseDto> incidents = incidentService.getIncidentsByStatus(status);
        return ResponseEntity.ok(incidents);
    }

    /**
     * PUT /api/v1/incidents/{id}
     * Actualiza el status y/o mode de un incidente existente.
     * Permite al usuario aprobar o rechazar acciones del agente.
     */
    @PutMapping("/{id}")
    public ResponseEntity<IncidentResponseDto> updateIncident(
            @PathVariable Long id,
            @RequestBody UpdateIncidentDto dto) {
        log.info("[IncidentController] PUT /api/v1/incidents/{} | body={}", id, dto);
        IncidentResponseDto updated = incidentService.updateIncident(id, dto);
        return ResponseEntity.ok(updated);
    }

    /**
     * PUT /api/v1/incidents/{id}/resolve
     * Marca el incidente como RESOLVED y registra el timestamp de resolución.
     */
    @PutMapping("/{id}/resolve")
    public ResponseEntity<IncidentResponseDto> resolveIncident(@PathVariable Long id) {
        log.info("[IncidentController] PUT /api/v1/incidents/{}/resolve", id);
        IncidentResponseDto resolved = incidentService.resolveIncident(id);
        return ResponseEntity.ok(resolved);
    }
}
