package com.black.alert_service.evaluator;

import com.black.alert_service.model.Alert;
import com.black.alert_service.model.MetricPayload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class ThresholdEvaluator {

    @Value("${thresholds.latencyMs}")
    private double thresholdLatencyMs;

    @Value("${thresholds.errorRate}")
    private double thresholdErrorRate;

    @Value("${thresholds.cpuPercent}")
    private double thresholdCpuPercent;

    @Value("${thresholds.memoryPercent}")
    private double thresholdMemoryPercent;

    /**
     * Evalúa el payload contra los cuatro umbrales configurados.
     * Por cada umbral superado genera un objeto Alert con la severidad calculada.
     *
     * Regla de severidad:
     *   - exceso  < 20%  → MEDIUM
     *   - exceso [20%–50%) → HIGH
     *   - exceso >= 50%  → CRITICAL
     *
     * @param payload métricas recibidas desde Kafka
     * @return lista de alertas generadas (vacía si ningún umbral fue superado)
     */
    public List<Alert> evaluate(MetricPayload payload) {
        List<Alert> alerts = new ArrayList<>();

        checkThreshold(payload, "LATENCY",    payload.getLatencyMs(),   thresholdLatencyMs,   alerts);
        checkThreshold(payload, "ERROR_RATE", payload.getErrorRate(),   thresholdErrorRate,   alerts);
        checkThreshold(payload, "CPU",        payload.getCpuPercent(),  thresholdCpuPercent,  alerts);
        checkThreshold(payload, "MEMORY",     payload.getMemoryPercent(), thresholdMemoryPercent, alerts);

        log.info("[ThresholdEvaluator] serviceId='{}' → {} alerta(s) generada(s)",
                payload.getServiceId(), alerts.size());

        return alerts;
    }

    // ── Helpers ──────────────────────────────────────────────────────────────────

    private void checkThreshold(MetricPayload payload,
                                 String type,
                                 Double actualValue,
                                 double threshold,
                                 List<Alert> alerts) {
        if (actualValue == null) {
            log.warn("[ThresholdEvaluator] Valor nulo para el tipo '{}' en serviceId='{}'",
                    type, payload.getServiceId());
            return;
        }

        if (actualValue > threshold) {
            String severity = calculateSeverity(actualValue, threshold);
            Alert alert = Alert.builder()
                    .serviceId(payload.getServiceId())
                    .type(type)
                    .thresholdValue(threshold)
                    .actualValue(actualValue)
                    .severity(severity)
                    .build();
            alerts.add(alert);

            log.warn("[ThresholdEvaluator] Umbral superado | type='{}' actual={} threshold={} severity='{}'",
                    type, actualValue, threshold, severity);
        }
    }

    /**
     * Calcula la severidad en función del porcentaje de exceso sobre el umbral.
     *
     *   exceso = (actualValue - threshold) / threshold * 100
     */
    private String calculateSeverity(double actualValue, double threshold) {
        double excessPercent = ((actualValue - threshold) / threshold) * 100.0;

        if (excessPercent < 20.0) {
            return "MEDIUM";
        } else if (excessPercent < 50.0) {
            return "HIGH";
        } else {
            return "CRITICAL";
        }
    }
}
