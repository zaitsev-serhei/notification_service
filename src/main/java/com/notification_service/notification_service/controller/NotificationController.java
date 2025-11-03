package com.notification_service.notification_service.controller;

import com.notification_service.notification_service.domain.model.Notification;
import com.notification_service.notification_service.dto.NotificationRequest;
import com.notification_service.notification_service.dto.NotificationResponse;
import com.notification_service.notification_service.service.NotificationService;
import com.notification_service.notification_service.util.mapper.NotificationRequestMapper;
import com.notification_service.notification_service.util.mapper.NotificationResponseMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    private final NotificationService service;
    private final NotificationResponseMapper responseMapper;
    private final NotificationRequestMapper requestMapper;

    public NotificationController(NotificationService service,
                                  NotificationResponseMapper responseMapper,
                                  NotificationRequestMapper requestMapper) {
        this.service = service;
        this.responseMapper = responseMapper;
        this.requestMapper = requestMapper;
    }

    @PostMapping("/create")
    public ResponseEntity<NotificationResponse> createNotification(@RequestBody NotificationRequest request) {
        Notification created = service.create(requestMapper.toDomain(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(responseMapper.toDto(created));
    }

    @PostMapping("/{notificationId}/read")
    public ResponseEntity<NotificationResponse> markAsRead(@PathVariable String notificationId) {
        return ResponseEntity.ok(responseMapper.toDto(service.markAsRead(notificationId)));
    }

    @GetMapping("/get/{notificationId}")
    public ResponseEntity<NotificationResponse> findNotificationById(@PathVariable String notificationId) {
        return ResponseEntity.ok(responseMapper.toDto(service.findById(notificationId)));
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<Page<NotificationResponse>> findNotificationsForUserById(@PathVariable String userId) {
        return ResponseEntity.ok(service.findByUserId(userId, PageRequest.of(0, 10)).map(responseMapper::toDto));
    }
}
