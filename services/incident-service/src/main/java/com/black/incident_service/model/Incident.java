package com.black.incident_service.model;

import com.black.incident_service.enums.AlertType;
import com.black.incident_service.enums.IncidentMode;
import com.black.incident_service.enums.IncidentStatus;
import com.black.incident_service.enums.Severity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entidad JPA que representa un incidente generado a partir de una alerta.
 * Persiste en la tabla "incidents" de PostgreSQL y gestiona su ciclo de vida completo.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "incidents")
public class Incident {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ID de la alerta que originó este incidente
    @Column(nullable = false)
    private Long alertId;

    // Servicio afectado
    @Column(nullable = false)
    private String serviceId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AlertType alertType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Severity severity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private IncidentStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private IncidentMode mode;

    // Timestamp de apertura, asignado automáticamente al crear el incidente
    @Column(nullable = false)
    private LocalDateTime openedAt;

    // Timestamp de resolución, nullable hasta que se cierre el incidente
    @Column
    private LocalDateTime resolvedAt;
}
