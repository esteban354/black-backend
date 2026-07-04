package com.black.alert.controller;

import com.black.alert.common.ApiResponse;
import com.black.alert.dto.AlertResponseDto;
import com.black.alert.enums.Severity;
import com.black.alert.service.AlertService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/alerts")
@RequiredArgsConstructor
public class AlertController {

    private final AlertService alertService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<AlertResponseDto>>> getAlerts(
            @RequestParam(required = false) String serviceId,
            @RequestParam(required = false) String severity) {
        log.info("[AlertController] GET /api/v1/alerts?serviceId={}&severity={}", serviceId, severity);
        Severity severityEnum = severity != null ? Severity.valueOf(severity.toUpperCase()) : null;
        List<AlertResponseDto> alerts = alertService.getAlerts(serviceId, severityEnum);
        return ResponseEntity.ok(ApiResponse.<List<AlertResponseDto>>builder()
                .success(true)
                .message("Alertas obtenidas correctamente")
                .data(alerts)
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AlertResponseDto>> getAlertById(@PathVariable Long id) {
        log.info("[AlertController] GET /api/v1/alerts/{}", id);
        AlertResponseDto alert = alertService.getAlertById(id);
        return ResponseEntity.ok(ApiResponse.<AlertResponseDto>builder()
                .success(true)
                .message("Alerta obtenida correctamente")
                .data(alert)
                .build());
    }
}
