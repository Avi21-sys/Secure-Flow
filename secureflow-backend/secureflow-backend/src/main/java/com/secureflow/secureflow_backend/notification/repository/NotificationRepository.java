package com.secureflow.secureflow_backend.notification.repository;

import com.secureflow.secureflow_backend.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByUserOrderByCreatedAtDesc(Long userId);

    long countByUserAndIsReadFalse(Long userId);
}
