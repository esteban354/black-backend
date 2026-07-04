package com.black.alert.repository;

import com.black.alert.enums.Severity;
import com.black.alert.model.Alert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlertRepository extends JpaRepository<Alert, Long> {

    List<Alert> findAllByOrderByTriggeredAtDesc();

    List<Alert> findByServiceIdOrderByTriggeredAtDesc(String serviceId);

    List<Alert> findBySeverityOrderByTriggeredAtDesc(Severity severity);

    List<Alert> findByServiceIdAndSeverityOrderByTriggeredAtDesc(String serviceId, Severity severity);
}
