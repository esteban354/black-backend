package com.black.alert.service;

import com.black.alert.dto.AlertResponseDto;
import com.black.alert.enums.Severity;
import com.black.alert.exception.ResourceNotFoundException;
import com.black.alert.mapper.AlertMapper;
import com.black.alert.model.Alert;
import com.black.alert.repository.AlertRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlertService {

    private final AlertRepository alertRepository;
    private final AlertMapper alertMapper;

    public List<AlertResponseDto> getAlerts(String serviceId, Severity severity) {
        log.info("[AlertService] Consultando alertas | serviceId={} severity={}", serviceId, severity);

        if (serviceId != null && severity != null) {
            return alertRepository.findByServiceIdAndSeverityOrderByTriggeredAtDesc(serviceId, severity)
                    .stream().map(alertMapper::toDto).collect(Collectors.toList());
        } else if (serviceId != null) {
            return alertRepository.findByServiceIdOrderByTriggeredAtDesc(serviceId)
                    .stream().map(alertMapper::toDto).collect(Collectors.toList());
        } else if (severity != null) {
            return alertRepository.findBySeverityOrderByTriggeredAtDesc(severity)
                    .stream().map(alertMapper::toDto).collect(Collectors.toList());
        } else {
            return alertRepository.findAllByOrderByTriggeredAtDesc()
                    .stream().map(alertMapper::toDto).collect(Collectors.toList());
        }
    }

    public AlertResponseDto getAlertById(Long id) {
        log.info("[AlertService] Consultando alerta id={}", id);
        Alert alert = alertRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("[AlertService] Alerta no encontrada con id={}", id);
                    return new ResourceNotFoundException("Alerta no encontrada con id: " + id);
                });
        return alertMapper.toDto(alert);
    }
}
