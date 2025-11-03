package com.notification_service.notification_service.domain.repository;

import com.notification_service.notification_service.common.NotificationStatus;
import com.notification_service.notification_service.domain.model.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.Optional;

public interface NotificationRepository {
    Notification save(Notification notification);

    Optional<Notification> findById(String notificationId);

    Page<Notification> findByUserIdOrderByCreatedAtDesc(String userId, Pageable page);

    Long countByUserIdAndWasReadFalse(String userId);

    Page<Notification> findByStatus(NotificationStatus status, Pageable page);

    Page<Notification> findByEventId(String eventId, Pageable page);

    Page<Notification> findByCreatedAtBefore(Instant before, Pageable page);
}
