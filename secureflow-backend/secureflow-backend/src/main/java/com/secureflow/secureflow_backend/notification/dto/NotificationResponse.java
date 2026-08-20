package com.secureflow.secureflow_backend.notification.dto;

import com.secureflow.secureflow_backend.notification.entity.NotificationType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationResponse {

    private Long id;

    private String message;

    private NotificationType type;

    private boolean read;

    private LocalDateTime createdAt;
}
