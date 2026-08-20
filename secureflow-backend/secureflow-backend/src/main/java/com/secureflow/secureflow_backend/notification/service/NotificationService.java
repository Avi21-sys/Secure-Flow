package com.secureflow.secureflow_backend.notification.service;

import com.secureflow.secureflow_backend.notification.dto.NotificationResponse;
import com.secureflow.secureflow_backend.notification.entity.NotificationType;

import java.util.List;

public interface NotificationService {

    void createNotification(
            Long userId,
            String message,
            NotificationType type
    );

    List<NotificationResponse> getUserNotifications(
            Long userId
    );

    long getUnreadCount(
            Long userId
    );

    void markAsRead(
            Long notificationId
    );
}
