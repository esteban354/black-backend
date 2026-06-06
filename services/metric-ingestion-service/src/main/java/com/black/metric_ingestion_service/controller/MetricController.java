package com.black.metric_ingestion_service.controller;

import com.black.metric_ingestion_service.model.MetricPayload;
import com.black.metric_ingestion_service.service.MetricIngestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/metrics")
@RequiredArgsConstructor
public class MetricController {

    private final MetricIngestionService metricIngestionService;

    /**
     * Recibe métricas de Eluxar vía HTTP y las publica en Kafka.
     *
     * @param payload cuerpo de la petición con las métricas del servicio
     * @return 200 OK con mensaje de confirmación, o 500 si el envío a Kafka falla
     */
    @PostMapping
    public ResponseEntity<String> receiveMetric(@RequestBody @Valid MetricPayload payload) {
        log.info("[MetricController] Solicitud recibida | serviceId='{}'", payload.getServiceId());

        boolean success = metricIngestionService.ingest(payload);

        if (success) {
            return ResponseEntity.ok(
                    "Métrica recibida y publicada en Kafka exitosamente | serviceId: " + payload.getServiceId()
            );
        }

        log.error("[MetricController] Error publicando la métrica en Kafka | serviceId='{}'",
                payload.getServiceId());
        return ResponseEntity.internalServerError()
                .body("Error al publicar la métrica en Kafka | serviceId: " + payload.getServiceId());
    }
}
