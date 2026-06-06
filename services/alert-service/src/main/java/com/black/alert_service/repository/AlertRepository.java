package com.black.alert_service.repository;

import com.black.alert_service.model.Alert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlertRepository extends JpaRepository<Alert, Long> {
    // Spring Data JPA genera automáticamente la implementación de save, findAll, findById, etc.
}
