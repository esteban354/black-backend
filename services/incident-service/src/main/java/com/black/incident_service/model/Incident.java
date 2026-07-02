package com.black.incident_service.model;

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

    // Tipo de alerta: LATENCY, ERROR_RATE, CPU, MEMORY
    @Column(nullable = false)
    private String alertType;

    // Severidad: MEDIUM, HIGH, CRITICAL
    @Column(nullable = false)
    private String severity;

    // Estado del incidente: OPEN, INVESTIGATING, RESOLVED
    @Column(nullable = false)
    private String status;

    // Modo de operación: ASSISTED (usuario aprueba) o AUTONOMOUS (agente actúa solo)
    @Column(nullable = false)
    private String mode;

    // Timestamp de apertura, asignado automáticamente al crear el incidente
    @Column(nullable = false)
    private LocalDateTime openedAt;

    // Timestamp de resolución, nullable hasta que se cierre el incidente
    @Column
    private LocalDateTime resolvedAt;
}
