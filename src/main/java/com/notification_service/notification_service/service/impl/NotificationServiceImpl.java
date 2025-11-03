package com.notification_service.notification_service.service.impl;

import com.notification_service.notification_service.common.NotificationChannel;
import com.notification_service.notification_service.common.NotificationStatus;
import com.notification_service.notification_service.domain.model.Notification;
import com.notification_service.notification_service.domain.repository.NotificationRepository;
import com.notification_service.notification_service.exception.NotificationByUserIdNotFoundException;
import com.notification_service.notification_service.exception.NotificationNotFoundException;
import com.notification_service.notification_service.service.NotificationService;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {
    private static final Logger logger = LoggerFactory.getLogger(NotificationServiceImpl.class);
    private final NotificationRepository repository;

    public NotificationServiceImpl(NotificationRepository repository) {
        this.repository = repository;
    }

    @Override
    public Notification create(Notification notification) {
        logger.info("NotificationService: create() call. Input notification " + notification.toString());
        if(notification.getEventId().isBlank()||notification.getEventId()==null){
            notification.setEventId(UUID.randomUUID().toString());
        }
        notification.setCreatedAt(Instant.now());
        notification.setWasRead(false);
        notification.setReadAt(null);
        notification.setAttempt(0);
        notification.setStatus(NotificationStatus.PENDING);
        notification.setChannels(List.of(NotificationChannel.IN_APP.name()));
        return repository.save(notification);
    }

    @Override
    public Notification findById(String notificationId) {
        logger.info("NotificationService: findById() call. Input notificationId = " + notificationId);
        return repository.findById(notificationId).orElseThrow(() -> new NotificationNotFoundException(notificationId));
    }

    @Override
    public Page<Notification> findByUserId(String userId, Pageable pageable) {
        logger.info("NotificationService: findByUserId() call. Input userId = " + userId);
        if (userId == null || userId.isBlank()) {
            throw new NotificationByUserIdNotFoundException(userId);
        }
        return repository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
    }

    @Override
    public Page<Notification> findByStatus(NotificationStatus status, Pageable page) {
        logger.info("NotificationService: findByStatus() call. Input status = " + status);
        return repository.findByStatus(status, page);
    }

    @Override
    public Page<Notification> findByEventId(String eventId, Pageable page) {
        logger.info("NotificationService: findByEventId() call. Input eventId = " + eventId);
        if (eventId == null || eventId.isBlank()) {
            throw new IllegalArgumentException("User id can not be blank");
        }
        return repository.findByEventId(eventId, page);
    }

    @Override
    public Page<Notification> findByCreatedAtBefore(Instant before, Pageable page) {
        logger.info("NotificationService: findByCreatedAtBefore() call. Input time = " + before.toString());
        return repository.findByCreatedAtBefore(before, page);
    }

    @Override
    public Long countUnread(String userId) {
        logger.info("NotificationService: countUnread() call. Input userId = " + userId);
        if (userId == null || userId.isBlank()) {
            throw new NotificationByUserIdNotFoundException(userId);
        }
        return repository.countByUserIdAndWasReadFalse(userId);
    }

    @Override
    public Notification markAsRead(String notificationId) {
        logger.info("NotificationService: markAsRead() call. Input notificationId = " + notificationId);
        Notification result = repository.findById(notificationId).orElseThrow(() -> new NotificationNotFoundException(notificationId));
        result.setWasRead(true);
        result.setReadAt(Instant.now());
        return repository.save(result);
    }
}
