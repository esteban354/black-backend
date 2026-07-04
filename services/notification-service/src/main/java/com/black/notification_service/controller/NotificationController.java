package com.black.notification_service.controller;

import com.black.notification_service.common.ApiResponse;
import com.black.notification_service.dto.NotificationResponseDto;
import com.black.notification_service.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<NotificationResponseDto>>> getNotifications(
            @RequestParam(required = false) String serviceId,
            @RequestParam(required = false) String severity) {
        log.info("[NotificationController] GET /api/v1/notifications?serviceId={}&severity={}", serviceId, severity);
        List<NotificationResponseDto> notifications = notificationService.getNotifications(serviceId, severity);
        return ResponseEntity.ok(ApiResponse.<List<NotificationResponseDto>>builder()
                .success(true)
                .message("Notificaciones obtenidas correctamente")
                .data(notifications)
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<NotificationResponseDto>> getNotificationById(@PathVariable Long id) {
        log.info("[NotificationController] GET /api/v1/notifications/{}", id);
        NotificationResponseDto notification = notificationService.getNotificationById(id);
        return ResponseEntity.ok(ApiResponse.<NotificationResponseDto>builder()
                .success(true)
                .message("Notificacion obtenida correctamente")
                .data(notification)
                .build());
    }
}
