package com.notification_service.notification_service.dto;

import com.notification_service.notification_service.common.NotificationChannel;
import com.notification_service.notification_service.common.NotificationStatus;
import com.notification_service.notification_service.common.NotificationType;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public class NotificationResponse {
    private String id;
    private String eventId;
    private String userId;
    private NotificationType type;
    private String title;
    private String body;
    private Map<String, Object> payload;
    private List<NotificationChannel> channels;
    private NotificationStatus status;
    private Instant createdAt;
    private Instant readAt;
    private boolean wasRead;
    private int attempt;

    public NotificationResponse() {
    }

    public NotificationResponse(String id,
                                String eventId,
                                String userId,
                                NotificationType type,
                                String title,
                                String body,
                                Map<String, Object> payload,
                                List<NotificationChannel> channels,
                                NotificationStatus status,
                                Instant createdAt,
                                Instant readAt,
                                boolean wasRead,
                                int attempt) {
        this.id = id;
        this.eventId = eventId;
        this.userId = userId;
        this.type = type;
        this.title = title;
        this.body = body;
        this.payload = payload;
        this.channels = channels;
        this.status = status;
        this.createdAt = createdAt;
        this.readAt = readAt;
        this.wasRead = wasRead;
        this.attempt = attempt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Map<String, Object> getPayload() {
        return payload;
    }

    public void setPayload(Map<String, Object> payload) {
        this.payload = payload;
    }

    public List<NotificationChannel> getChannels() {
        return channels;
    }

    public void setChannels(List<NotificationChannel> channels) {
        this.channels = channels;
    }

    public NotificationStatus getStatus() {
        return status;
    }

    public void setStatus(NotificationStatus status) {
        this.status = status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getReadAt() {
        return readAt;
    }

    public void setReadAt(Instant readAt) {
        this.readAt = readAt;
    }

    public boolean isWasRead() {
        return wasRead;
    }

    public void setWasRead(boolean wasRead) {
        this.wasRead = wasRead;
    }

    public int getAttempt() {
        return attempt;
    }

    public void setAttempt(int attempt) {
        this.attempt = attempt;
    }
}
