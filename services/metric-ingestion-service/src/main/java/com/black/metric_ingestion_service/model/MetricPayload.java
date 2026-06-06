package com.black.metric_ingestion_service.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MetricPayload {

    // Identificador único del servicio/aplicación que reporta la métrica (ej. "orders-service").
    private String serviceId;

    // Fecha y hora exacta en la que se recolectó la métrica en origen, con formato ISO-8601 sin zona horaria.
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime collectedAt;

    // Latencia promedio registrada en las peticiones del servicio medida en milisegundos.
    private Double latencyMs;

    // Tasa de error o porcentaje de peticiones fallidas registradas por el servicio.
    private Double errorRate;

    // Porcentaje de uso del procesador (CPU) asignado o consumido por el servicio.
    private Double cpuPercent;

    // Porcentaje de uso de memoria RAM consumida por el servicio.
    private Double memoryPercent;
}
