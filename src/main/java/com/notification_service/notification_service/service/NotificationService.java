package com.notification_service.notification_service.service;

import com.notification_service.notification_service.common.NotificationStatus;
import com.notification_service.notification_service.domain.model.Notification;
import java.time.Instant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NotificationService {
    Notification create(Notification notification);

    Notification findById(String notificationId);

    Page<Notification> findByUserId(String userId, Pageable pageable);

    Page<Notification> findByStatus(NotificationStatus status, Pageable page);

    Page<Notification> findByEventId(String eventId, Pageable page);

    Page<Notification> findByCreatedAtBefore(Instant before, Pageable page);

    Long countUnread(String userId);

    Notification markAsRead(String id);
}
