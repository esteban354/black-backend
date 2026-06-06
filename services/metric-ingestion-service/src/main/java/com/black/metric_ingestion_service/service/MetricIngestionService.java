package com.black.metric_ingestion_service.service;

import com.black.metric_ingestion_service.kafka.MetricProducer;
import com.black.metric_ingestion_service.model.MetricPayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MetricIngestionService {

    private final MetricProducer metricProducer;

    /**
     * Valida que el payload no sea nulo y delega su publicación en Kafka al MetricProducer.
     *
     * @param payload métricas recibidas del cliente (Eluxar)
     * @return true si la métrica fue procesada y enviada con éxito, false en caso contrario
     */
    public boolean ingest(MetricPayload payload) {
        if (payload == null) {
            log.warn("[MetricIngestionService] Payload recibido es nulo, operación abortada.");
            return false;
        }

        log.info("[MetricIngestionService] Procesando métrica | serviceId='{}'", payload.getServiceId());

        boolean success = metricProducer.sendMetric(payload);

        if (success) {
            log.info("[MetricIngestionService] Métrica procesada exitosamente | serviceId='{}'",
                    payload.getServiceId());
        } else {
            log.error("[MetricIngestionService] Fallo al procesar la métrica | serviceId='{}'",
                    payload.getServiceId());
        }

        return success;
    }
}
