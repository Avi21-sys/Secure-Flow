package com.secureflow.secureflow_backend.notification.controller;

import com.secureflow.secureflow_backend.common.response.ApiResponse;
import com.secureflow.secureflow_backend.notification.dto.NotificationResponse;
import com.secureflow.secureflow_backend.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;



    @GetMapping("/user/{userId}")
    @PreAuthorize(
            "hasAnyRole('ADMIN','ANALYST','MANAGER','DEVELOPER')"
    )
    public ResponseEntity<ApiResponse<List<NotificationResponse>>> getNotifications(
            @PathVariable Long userId
    ) {


        return ResponseEntity.ok(

                ApiResponse.<List<NotificationResponse>>builder()

                        .success(true)

                        .message(
                                "Notifications fetched successfully"
                        )

                        .data(
                                notificationService
                                        .getUserNotifications(userId)
                        )

                        .build()

        );

    }





    @GetMapping("/user/{userId}/unread-count")
    @PreAuthorize(
            "hasAnyRole('ADMIN','ANALYST','MANAGER','DEVELOPER')"
    )
    public ResponseEntity<ApiResponse<Long>> getUnreadCount(
            @PathVariable Long userId
    ) {


        return ResponseEntity.ok(

                ApiResponse.<Long>builder()

                        .success(true)

                        .message(
                                "Unread count fetched successfully"
                        )

                        .data(
                                notificationService
                                        .getUnreadCount(userId)
                        )

                        .build()

        );

    }





    @PutMapping("/{notificationId}/read")
    @PreAuthorize(
            "hasAnyRole('ADMIN','ANALYST','MANAGER','DEVELOPER')"
    )
    public ResponseEntity<ApiResponse<Void>> markAsRead(
            @PathVariable Long notificationId
    ) {


        notificationService.markAsRead(notificationId);


        return ResponseEntity.ok(

                ApiResponse.<Void>builder()

                        .success(true)

                        .message(
                                "Notification marked as read"
                        )

                        .data(null)

                        .build()

        );

    }
}
