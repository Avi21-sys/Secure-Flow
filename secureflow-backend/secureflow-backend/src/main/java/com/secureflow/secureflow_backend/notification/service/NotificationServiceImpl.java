package com.secureflow.secureflow_backend.notification.service;

import com.secureflow.secureflow_backend.common.exception.ResourceNotFoundException;
import com.secureflow.secureflow_backend.notification.dto.NotificationResponse;
import com.secureflow.secureflow_backend.notification.entity.Notification;
import com.secureflow.secureflow_backend.notification.entity.NotificationType;
import com.secureflow.secureflow_backend.notification.repository.NotificationRepository;
import com.secureflow.secureflow_backend.user.entity.User;
import com.secureflow.secureflow_backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    private final UserRepository userRepository;



    @Override
    public void createNotification(
            Long userId,
            String message,
            NotificationType type
    ) {


        User user =
                userRepository.findById(userId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "User not found with id: "
                                                + userId
                                )
                        );


        Notification notification =
                Notification.builder()

                        .user(user)

                        .message(message)

                        .type(type)

                        .read(false)

                        .build();


        notificationRepository.save(notification);

    }





    @Override
    public List<NotificationResponse> getUserNotifications(
            Long userId
    ) {


        return notificationRepository
                .findByUserIdOrderByCreatedAtDesc(userId)

                .stream()

                .map(this::mapToResponse)

                .toList();

    }





    @Override
    public long getUnreadCount(
            Long userId
    ) {

        return notificationRepository
                .countByUserIdAndReadFalse(userId);

    }





    @Override
    public void markAsRead(
            Long notificationId
    ) {


        Notification notification =
                notificationRepository.findById(notificationId)

                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Notification not found"
                                )
                        );


        notification.setRead(true);


        notificationRepository.save(notification);

    }





    private NotificationResponse mapToResponse(
            Notification notification
    ) {


        return NotificationResponse.builder()

                .id(notification.getId())

                .message(notification.getMessage())

                .type(notification.getType())

                .read(notification.isRead())

                .createdAt(notification.getCreatedAt())

                .build();

    }

}
