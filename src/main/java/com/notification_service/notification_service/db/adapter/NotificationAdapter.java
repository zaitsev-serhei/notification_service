package com.notification_service.notification_service.db.adapter;

import com.notification_service.notification_service.common.NotificationStatus;
import com.notification_service.notification_service.db.entity.NotificationDocumentEntity;
import com.notification_service.notification_service.db.repository.NotificationRepositoryMongo;
import com.notification_service.notification_service.domain.model.Notification;
import com.notification_service.notification_service.domain.repository.NotificationRepository;
import com.notification_service.notification_service.util.mapper.NotificationMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;

@Repository
public class NotificationAdapter implements NotificationRepository {
    private final NotificationRepositoryMongo repositoryMongo;
    private final NotificationMapper mapper;

    public NotificationAdapter(NotificationRepositoryMongo repositoryMongo, NotificationMapper mapper) {
        this.repositoryMongo = repositoryMongo;
        this.mapper = mapper;
    }

    @Override
    public Notification save(Notification notification) {
        NotificationDocumentEntity entity = mapper.toEntity(notification);
        NotificationDocumentEntity saved = repositoryMongo.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Notification> findById(String notificationId) {
        Optional<NotificationDocumentEntity> result = repositoryMongo.findById(notificationId);
        return result.map(mapper::toDomain);
    }

    @Override
    public Page<Notification> findByUserIdOrderByCreatedAtDesc(String userId, Pageable page) {
        return repositoryMongo.findByUserIdOrderByCreatedAtDesc(userId, page).map(mapper::toDomain);
    }

    @Override
    public Long countByUserIdAndWasReadFalse(String userId) {
        return repositoryMongo.countByUserIdAndWasReadFalse(userId);
    }

    @Override
    public Page<Notification> findByStatus(NotificationStatus status, Pageable page) {
        return repositoryMongo.findByStatus(status, page).map(mapper::toDomain);
    }

    @Override
    public Page<Notification> findByEventId(String eventId, Pageable page) {
        return repositoryMongo.findByEventId(eventId, page).map(mapper::toDomain);
    }

    @Override
    public Page<Notification> findByCreatedAtBefore(Instant before, Pageable page) {
        return repositoryMongo.findByCreatedAtBefore(before, page).map(mapper::toDomain);
    }
}
