package com.black.alert_service.consumer;

import com.black.alert_service.evaluator.ThresholdEvaluator;
import com.black.alert_service.model.Alert;
import com.black.alert_service.model.AlertEvent;
import com.black.alert_service.model.MetricPayload;
import com.black.alert_service.repository.AlertRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class MetricConsumer {

    private final ObjectMapper objectMapper;
    private final ThresholdEvaluator thresholdEvaluator;
    private final AlertRepository alertRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${kafka.topics.alerts}")
    private String alertsTopic;

    /**
     * Escucha el topic black.metrics, deserializa el payload, evalúa umbrales,
     * persiste cada alerta en PostgreSQL y publica un AlertEvent en black.alerts.
     *
     * @param message mensaje JSON recibido desde Kafka
     */
    @KafkaListener(topics = "${kafka.topics.metrics}", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(String message) {
        log.info("[MetricConsumer] Mensaje recibido desde Kafka | payload='{}'", message);

        MetricPayload payload;
        try {
            payload = objectMapper.readValue(message, MetricPayload.class);
        } catch (JsonProcessingException e) {
            log.error("[MetricConsumer] Error deserializando el mensaje de Kafka | error='{}'", e.getMessage());
            return;
        }

        List<Alert> alerts = thresholdEvaluator.evaluate(payload);

        if (alerts.isEmpty()) {
            log.info("[MetricConsumer] Sin anomalías detectadas para serviceId='{}'", payload.getServiceId());
            return;
        }

        for (Alert alert : alerts) {
            // 1. Persistir la alerta en PostgreSQL
            Alert savedAlert = alertRepository.save(alert);
            log.info("[MetricConsumer] Alerta persistida | id={} serviceId='{}' type='{}' severity='{}'",
                    savedAlert.getId(), savedAlert.getServiceId(), savedAlert.getType(), savedAlert.getSeverity());

            // 2. Construir el evento para Kafka
            AlertEvent event = AlertEvent.builder()
                    .alertId(savedAlert.getId())
                    .serviceId(savedAlert.getServiceId())
                    .type(savedAlert.getType())
                    .severity(savedAlert.getSeverity())
                    .triggeredAt(savedAlert.getTriggeredAt())
                    .build();

            // 3. Serializar y publicar en black.alerts
            try {
                String eventJson = objectMapper.writeValueAsString(event);
                kafkaTemplate.send(alertsTopic, savedAlert.getServiceId(), eventJson);
                log.info("[MetricConsumer] AlertEvent publicado en Kafka | topic='{}' alertId={}",
                        alertsTopic, savedAlert.getId());
            } catch (JsonProcessingException e) {
                log.error("[MetricConsumer] Error serializando AlertEvent | alertId={} error='{}'",
                        savedAlert.getId(), e.getMessage());
            }
        }
    }
}
