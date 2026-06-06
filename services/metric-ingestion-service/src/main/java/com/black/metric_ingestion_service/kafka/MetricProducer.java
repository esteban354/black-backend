package com.black.metric_ingestion_service.kafka;

import com.black.metric_ingestion_service.model.MetricPayload;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MetricProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Value("${kafka.topics.metrics}")
    private String metricsTopic;

    /**
     * Serializa el MetricPayload a JSON y lo publica en Kafka.
     *
     * @param payload métricas recibidas del servicio externo (Eluxar)
     * @return true si el mensaje fue enviado exitosamente, false en caso de error de serialización
     */
    public boolean sendMetric(MetricPayload payload) {
        try {
            String message = objectMapper.writeValueAsString(payload);
            String key = payload.getServiceId();

            kafkaTemplate.send(metricsTopic, key, message);
            log.info("[MetricProducer] Métrica enviada a Kafka | topic='{}' key='{}'",
                    metricsTopic, key);
            return true;

        } catch (JsonProcessingException e) {
            log.error("[MetricProducer] Error serializando MetricPayload a JSON | serviceId='{}' error='{}'",
                    payload.getServiceId(), e.getMessage());
            return false;
        }
    }
}
