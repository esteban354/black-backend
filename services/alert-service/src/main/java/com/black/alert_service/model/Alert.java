package com.black.alert_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "alerts")
public class Alert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Identificador del servicio que generó la métrica que disparó la alerta
    @Column(nullable = false)
    private String serviceId;

    // Tipo de métrica que superó el umbral: LATENCY, ERROR_RATE, CPU, MEMORY
    @Column(nullable = false)
    private String type;

    // Valor del umbral configurado que fue superado
    @Column(nullable = false)
    private Double thresholdValue;

    // Valor real recibido en la métrica que disparó la alerta
    @Column(nullable = false)
    private Double actualValue;

    // Severidad calculada: MEDIUM, HIGH o CRITICAL según el porcentaje de exceso
    @Column(nullable = false)
    private String severity;

    // Timestamp exacto en que se evaluó y confirmó la alerta
    @Column(nullable = false)
    private LocalDateTime triggeredAt;

    @PrePersist
    public void prePersist() {
        this.triggeredAt = LocalDateTime.now();
    }
}
