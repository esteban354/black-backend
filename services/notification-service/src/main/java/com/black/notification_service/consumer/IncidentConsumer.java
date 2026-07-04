package com.black.notification_service.consumer;

import com.black.notification_service.model.IncidentEvent;
import com.black.notification_service.model.Notification;
import com.black.notification_service.repository.NotificationRepository;
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
    private final NotificationRepository notificationRepository;

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

            String summary = String.format(
                    "Incidente %d en servicio %s: severidad %s, estado %s, modo %s",
                    event.getIncidentId(), event.getServiceId(),
                    event.getSeverity(), event.getStatus(), event.getMode());

            Notification notification = Notification.builder()
                    .incidentId(event.getIncidentId())
                    .alertId(event.getAlertId())
                    .serviceId(event.getServiceId())
                    .severity(event.getSeverity())
                    .status(event.getStatus())
                    .mode(event.getMode())
                    .message(summary)
                    .build();

            notificationRepository.save(notification);
            log.info("[NotificationService] Notificacion persistida | id={} incidenteId={}",
                    notification.getId(), event.getIncidentId());

        } catch (JsonProcessingException e) {
            log.error("[NotificationService] Error al deserializar IncidentEvent | mensaje={} | error={}",
                    message, e.getMessage(), e);
        }
    }
}