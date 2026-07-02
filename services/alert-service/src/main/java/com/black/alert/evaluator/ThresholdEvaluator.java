package com.black.alert.evaluator;

import com.black.alert.enums.AlertType;
import com.black.alert.enums.Severity;
import com.black.alert.model.Alert;
import com.black.alert.model.MetricPayload;
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

    public List<Alert> evaluate(MetricPayload payload) {
        List<Alert> alerts = new ArrayList<>();

        checkThreshold(payload, AlertType.LATENCY,    payload.getLatencyMs(),   thresholdLatencyMs,   alerts);
        checkThreshold(payload, AlertType.ERROR_RATE, payload.getErrorRate(),   thresholdErrorRate,   alerts);
        checkThreshold(payload, AlertType.CPU,        payload.getCpuPercent(),  thresholdCpuPercent,  alerts);
        checkThreshold(payload, AlertType.MEMORY,     payload.getMemoryPercent(), thresholdMemoryPercent, alerts);

        log.info("[ThresholdEvaluator] serviceId='{}' → {} alerta(s) generada(s)",
                payload.getServiceId(), alerts.size());

        return alerts;
    }

    private void checkThreshold(MetricPayload payload,
                                 AlertType type,
                                 Double actualValue,
                                 double threshold,
                                 List<Alert> alerts) {
        if (actualValue == null) {
            log.warn("[ThresholdEvaluator] Valor nulo para el tipo '{}' en serviceId='{}'",
                    type, payload.getServiceId());
            return;
        }

        if (actualValue > threshold) {
            Severity severity = calculateSeverity(actualValue, threshold);
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

    private Severity calculateSeverity(double actualValue, double threshold) {
        double excessPercent = ((actualValue - threshold) / threshold) * 100.0;

        if (excessPercent < 20.0) {
            return Severity.MEDIUM;
        } else if (excessPercent < 50.0) {
            return Severity.HIGH;
        } else {
            return Severity.CRITICAL;
        }
    }
}
