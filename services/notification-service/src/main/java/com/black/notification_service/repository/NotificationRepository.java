package com.black.notification_service.repository;

import com.black.notification_service.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findAllByOrderByCreatedAtDesc();

    List<Notification> findByServiceIdOrderByCreatedAtDesc(String serviceId);

    List<Notification> findBySeverityOrderByCreatedAtDesc(String severity);

    List<Notification> findByServiceIdAndSeverityOrderByCreatedAtDesc(String serviceId, String severity);
}
