package com.black.incident_service.service;

import com.black.incident_service.dto.IncidentResponseDto;
import com.black.incident_service.dto.UpdateIncidentDto;
import com.black.incident_service.enums.IncidentMode;
import com.black.incident_service.enums.IncidentStatus;
import com.black.incident_service.exception.ResourceNotFoundException;
import com.black.incident_service.mapper.IncidentMapper;
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
    private final IncidentMapper incidentMapper;

    /**
     * Retorna todos los incidentes registrados en el sistema.
     */
    public List<IncidentResponseDto> getAllIncidents() {
        log.info("[IncidentService] Consultando todos los incidentes");
        return incidentRepository.findAll()
                .stream()
                .map(incidentMapper::toDto)
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
                .map(incidentMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Retorna todos los incidentes en un estado específico.
     * @param status Estado a filtrar: OPEN, INVESTIGATING, RESOLVED
     */
    public List<IncidentResponseDto> getIncidentsByStatus(IncidentStatus status) {
        log.info("[IncidentService] Consultando incidentes con status={}", status);
        return incidentRepository.findByStatus(status)
                .stream()
                .map(incidentMapper::toDto)
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
                    return new ResourceNotFoundException("Incidente no encontrado con id: " + id);
                });

        if (dto.getStatus() != null) {
            incident.setStatus(IncidentStatus.valueOf(dto.getStatus()));
        }
        if (dto.getMode() != null) {
            incident.setMode(IncidentMode.valueOf(dto.getMode()));
        }

        Incident updated = incidentRepository.save(incident);
        log.info("[IncidentService] Incidente id={} actualizado correctamente", id);

        IncidentResponseDto response = incidentMapper.toDto(updated);
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
                    return new ResourceNotFoundException("Incidente no encontrado con id: " + id);
                });

        incident.setStatus(IncidentStatus.RESOLVED);
        incident.setResolvedAt(LocalDateTime.now());

        Incident resolved = incidentRepository.save(incident);
        log.info("[IncidentService] Incidente id={} resuelto en {}", id, resolved.getResolvedAt());

        IncidentResponseDto response = incidentMapper.toDto(resolved);
        response.setMessage("Incidente resuelto exitosamente");
        return response;
    }
}
