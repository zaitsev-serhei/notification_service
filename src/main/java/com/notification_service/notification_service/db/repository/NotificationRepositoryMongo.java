package com.notification_service.notification_service.db.repository;

import com.notification_service.notification_service.common.NotificationStatus;
import com.notification_service.notification_service.db.entity.NotificationDocumentEntity;
import java.time.Instant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;



public interface NotificationRepositoryMongo extends MongoRepository<NotificationDocumentEntity, String> {
    Page<NotificationDocumentEntity> findByUserIdOrderByCreatedAtDesc(String userId, Pageable page);

    Long countByUserIdAndWasReadFalse(String userId);

    Page<NotificationDocumentEntity> findByStatus(NotificationStatus status, Pageable page);

    Page<NotificationDocumentEntity> findByEventId(String eventId, Pageable page);

    Page<NotificationDocumentEntity> findByCreatedAtBefore(Instant before, Pageable page);
}
