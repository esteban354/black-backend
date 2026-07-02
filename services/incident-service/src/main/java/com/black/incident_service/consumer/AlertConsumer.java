package com.black.incident_service.consumer;

import com.black.incident_service.model.AlertEvent;
import com.black.incident_service.model.Incident;
import com.black.incident_service.model.IncidentEvent;
import com.black.incident_service.producer.IncidentProducer;
import com.black.incident_service.repository.IncidentRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Consumer Kafka que escucha el topic black.alerts.
 * Por cada AlertEvent recibido, crea un Incident en PostgreSQL y publica un IncidentEvent en Kafka.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AlertConsumer {

    private final ObjectMapper objectMapper;
    private final IncidentRepository incidentRepository;
    private final IncidentProducer incidentProducer;

    /**
     * Listener que procesa cada AlertEvent del topic black.alerts.
     * Crea el incidente con estado OPEN y modo ASSISTED (el usuario puede cambiarlo después).
     * @param message JSON del AlertEvent publicado por el alert-service
     */
    @KafkaListener(
            topics = "${kafka.topics.alerts}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void consume(String message) {
        log.info("[AlertConsumer] Mensaje recibido desde topic black.alerts: {}", message);

        try {
            AlertEvent alertEvent = objectMapper.readValue(message, AlertEvent.class);
            log.info("[AlertConsumer] AlertEvent deserializado | alertId={} | serviceId={} | severity={}",
                    alertEvent.getAlertId(), alertEvent.getServiceId(), alertEvent.getSeverity());

            // Crear y persistir el incidente
            Incident incident = Incident.builder()
                    .alertId(alertEvent.getAlertId())
                    .serviceId(alertEvent.getServiceId())
                    .alertType(alertEvent.getType())
                    .severity(alertEvent.getSeverity())
                    .status("OPEN")
                    .mode("ASSISTED")
                    .openedAt(LocalDateTime.now())
                    .build();

            Incident saved = incidentRepository.save(incident);
            log.info("[AlertConsumer] Incidente creado en PostgreSQL | incidentId={} | alertId={} | status=OPEN | mode=ASSISTED",
                    saved.getId(), saved.getAlertId());

            // Construir y publicar el IncidentEvent en Kafka
            IncidentEvent incidentEvent = IncidentEvent.builder()
                    .incidentId(saved.getId())
                    .alertId(saved.getAlertId())
                    .serviceId(saved.getServiceId())
                    .severity(saved.getSeverity())
                    .status(saved.getStatus())
                    .mode(saved.getMode())
                    .openedAt(saved.getOpenedAt())
                    .build();

            incidentProducer.publishIncidentEvent(incidentEvent);

        } catch (JsonProcessingException e) {
            log.error("[AlertConsumer] Error al deserializar AlertEvent | mensaje={} | error={}",
                    message, e.getMessage(), e);
        }
    }
}
