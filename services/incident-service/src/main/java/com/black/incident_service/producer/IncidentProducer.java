package com.black.incident_service.producer;

import com.black.incident_service.model.IncidentEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * Producer Kafka responsable de publicar IncidentEvents en el topic black.incidents.
 * Usa el incidentId como clave para garantizar que eventos del mismo incidente
 * vayan siempre a la misma partición (ordering por incidente).
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class IncidentProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Value("${kafka.topics.incidents}")
    private String incidentsTopic;

    /**
     * Publica un IncidentEvent en el topic de incidentes.
     * @param incidentEvent evento a publicar, ya construido con datos del incidente persistido
     */
    public void publishIncidentEvent(IncidentEvent incidentEvent) {
        try {
            String payload = objectMapper.writeValueAsString(incidentEvent);
            String key = String.valueOf(incidentEvent.getIncidentId());

            kafkaTemplate.send(incidentsTopic, key, payload);

            log.info("[IncidentProducer] Evento publicado en topic '{}' | key={} | incidentId={} | serviceId={} | severity={}",
                    incidentsTopic,
                    key,
                    incidentEvent.getIncidentId(),
                    incidentEvent.getServiceId(),
                    incidentEvent.getSeverity());

        } catch (JsonProcessingException e) {
            log.error("[IncidentProducer] Error al serializar IncidentEvent para incidentId={} | error={}",
                    incidentEvent.getIncidentId(), e.getMessage(), e);
        }
    }
}
