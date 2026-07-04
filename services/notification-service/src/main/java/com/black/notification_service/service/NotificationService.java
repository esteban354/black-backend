package com.black.notification_service.service;

import com.black.notification_service.dto.NotificationResponseDto;
import com.black.notification_service.exception.ResourceNotFoundException;
import com.black.notification_service.mapper.NotificationMapper;
import com.black.notification_service.model.Notification;
import com.black.notification_service.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;

    public List<NotificationResponseDto> getNotifications(String serviceId, String severity) {
        log.info("[NotificationService] Consultando notificaciones | serviceId={} severity={}", serviceId, severity);

        if (serviceId != null && severity != null) {
            return notificationRepository.findByServiceIdAndSeverityOrderByCreatedAtDesc(serviceId, severity)
                    .stream().map(notificationMapper::toDto).collect(Collectors.toList());
        } else if (serviceId != null) {
            return notificationRepository.findByServiceIdOrderByCreatedAtDesc(serviceId)
                    .stream().map(notificationMapper::toDto).collect(Collectors.toList());
        } else if (severity != null) {
            return notificationRepository.findBySeverityOrderByCreatedAtDesc(severity)
                    .stream().map(notificationMapper::toDto).collect(Collectors.toList());
        } else {
            return notificationRepository.findAllByOrderByCreatedAtDesc()
                    .stream().map(notificationMapper::toDto).collect(Collectors.toList());
        }
    }

    public NotificationResponseDto getNotificationById(Long id) {
        log.info("[NotificationService] Consultando notificacion id={}", id);
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("[NotificationService] Notificacion no encontrada con id={}", id);
                    return new ResourceNotFoundException("Notificacion no encontrada con id: " + id);
                });
        return notificationMapper.toDto(notification);
    }
}
