package com.black.incident_service.service;

import com.black.incident_service.dto.IncidentResponseDto;
import com.black.incident_service.dto.UpdateIncidentDto;
import com.black.incident_service.model.Incident;
import com.black.incident_service.repository.IncidentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Capa de negocio del incident-service.
 * Gestiona el ciclo de vida completo de los incidentes: consulta, actualización y resolución.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class IncidentService {

    private final IncidentRepository incidentRepository;

    /**
     * Retorna todos los incidentes registrados en el sistema.
     */
    public List<IncidentResponseDto> getAllIncidents() {
        log.info("[IncidentService] Consultando todos los incidentes");
        return incidentRepository.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Retorna todos los incidentes de un servicio específico.
     * @param serviceId ID del servicio monitoreado
     */
    public List<IncidentResponseDto> getIncidentsByServiceId(String serviceId) {
        log.info("[IncidentService] Consultando incidentes para serviceId={}", serviceId);
        return incidentRepository.findByServiceId(serviceId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Retorna todos los incidentes en un estado específico.
     * @param status Estado a filtrar: OPEN, INVESTIGATING, RESOLVED
     */
    public List<IncidentResponseDto> getIncidentsByStatus(String status) {
        log.info("[IncidentService] Consultando incidentes con status={}", status);
        return incidentRepository.findByStatus(status)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Actualiza el status y/o mode de un incidente existente.
     * Permite al usuario aprobar acciones (cambiar a AUTONOMOUS) o escalar (INVESTIGATING).
     * @param id ID del incidente a actualizar
     * @param dto DTO con los nuevos valores de status y mode
     */
    public IncidentResponseDto updateIncident(Long id, UpdateIncidentDto dto) {
        log.info("[IncidentService] Actualizando incidente id={} | nuevoStatus={} | nuevoMode={}",
                id, dto.getStatus(), dto.getMode());

        Incident incident = incidentRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("[IncidentService] Incidente no encontrado con id={}", id);
                    return new RuntimeException("Incidente no encontrado con id: " + id);
                });

        if (dto.getStatus() != null) {
            incident.setStatus(dto.getStatus());
        }
        if (dto.getMode() != null) {
            incident.setMode(dto.getMode());
        }

        Incident updated = incidentRepository.save(incident);
        log.info("[IncidentService] Incidente id={} actualizado correctamente", id);

        IncidentResponseDto response = toDto(updated);
        response.setMessage("Incidente actualizado correctamente");
        return response;
    }

    /**
     * Cambia el estado del incidente a RESOLVED y registra el timestamp de resolución.
     * @param id ID del incidente a resolver
     */
    public IncidentResponseDto resolveIncident(Long id) {
        log.info("[IncidentService] Resolviendo incidente id={}", id);

        Incident incident = incidentRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("[IncidentService] Incidente no encontrado con id={}", id);
                    return new RuntimeException("Incidente no encontrado con id: " + id);
                });

        incident.setStatus("RESOLVED");
        incident.setResolvedAt(LocalDateTime.now());

        Incident resolved = incidentRepository.save(incident);
        log.info("[IncidentService] Incidente id={} resuelto en {}", id, resolved.getResolvedAt());

        IncidentResponseDto response = toDto(resolved);
        response.setMessage("Incidente resuelto exitosamente");
        return response;
    }

    /**
     * Convierte una entidad Incident en su DTO de respuesta.
     */
    private IncidentResponseDto toDto(Incident incident) {
        return IncidentResponseDto.builder()
                .id(incident.getId())
                .alertId(incident.getAlertId())
                .serviceId(incident.getServiceId())
                .alertType(incident.getAlertType())
                .severity(incident.getSeverity())
                .status(incident.getStatus())
                .mode(incident.getMode())
                .openedAt(incident.getOpenedAt())
                .resolvedAt(incident.getResolvedAt())
                .build();
    }
}
