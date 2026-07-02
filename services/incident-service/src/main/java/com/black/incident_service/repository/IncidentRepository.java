package com.black.incident_service.repository;

import com.black.incident_service.enums.IncidentStatus;
import com.black.incident_service.model.Incident;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio JPA para la entidad Incident.
 * Extiende JpaRepository para operaciones CRUD estándar y agrega búsquedas específicas del dominio.
 */
@Repository
public interface IncidentRepository extends JpaRepository<Incident, Long> {

    /**
     * Busca todos los incidentes asociados a un servicio específico.
     * @param serviceId ID del servicio monitoreado
     * @return lista de incidentes del servicio
     */
    List<Incident> findByServiceId(String serviceId);

    /**
     * Busca todos los incidentes que se encuentran en un estado específico.
     * @param status Estado del incidente: OPEN, INVESTIGATING, RESOLVED
     * @return lista de incidentes con ese estado
     */
    List<Incident> findByStatus(IncidentStatus status);
}
