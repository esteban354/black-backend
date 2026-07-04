package com.black.notification_service.consumer;

import com.black.notification_service.model.IncidentEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class IncidentConsumer {

    private final ObjectMapper objectMapper;

    @KafkaListener(
            topics = "${kafka.topics.incidents}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void consume(String message) {
        log.info("[NotificationService] Mensaje recibido desde topic black.incidents: {}", message);

        try {
            IncidentEvent event = objectMapper.readValue(message, IncidentEvent.class);

            log.info("[NotificationService] Nuevo incidente | id={} serviceId={} severity={} status={} mode={} openedAt={}",
                    event.getIncidentId(),
                    event.getServiceId(),
                    event.getSeverity(),
                    event.getStatus(),
                    event.getMode(),
                    event.getOpenedAt());

        } catch (JsonProcessingException e) {
            log.error("[NotificationService] Error al deserializar IncidentEvent | mensaje={} | error={}",
                    message, e.getMessage(), e);
        }
    }
}