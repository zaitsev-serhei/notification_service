package com.notification_service.notification_service.domain.model;

import com.notification_service.notification_service.common.NotificationStatus;
import com.notification_service.notification_service.common.NotificationType;
import com.notification_service.notification_service.db.entity.NotificationDocumentEntity;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Notification {
    private String id;
    private String eventId;
    private NotificationType type;
    private String userId;
    private String title;
    private String body;
    private Map<String, Object> payload;
    private List<String> channels;
    private NotificationStatus status;
    private Instant createdAt;
    private Instant readAt;
    private boolean wasRead;
    private int attempt;

    public Notification() {
    }

    public Notification(String id,
                        String eventId,
                        NotificationType type,
                        String userId,
                        String title,
                        String body,
                        Map<String, Object> payload,
                        List<String> channels,
                        NotificationStatus status) {
        this.id = id;
        this.eventId = eventId;
        this.type = type;
        this.userId = userId;
        this.title = title;
        this.body = body;
        this.payload = payload;
        this.channels = channels;
        this.status = status;
        this.attempt = 0;
        this.readAt = null;
        this.wasRead = false;
        this.createdAt = Instant.now();
    }

    public Notification(String id,
                        String eventId,
                        NotificationType type,
                        String userId,
                        String title,
                        String body,
                        Map<String, Object> payload,
                        List<String> channels,
                        NotificationStatus status,
                        Instant createdAt,
                        Instant readAt,
                        boolean wasRead,
                        int attempt) {
        this.id = id;
        this.eventId = eventId;
        this.type = type;
        this.userId = userId;
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

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public List<String> getChannels() {
        return channels;
    }

    public void setChannels(List<String> channels) {
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

    public boolean wasRead() {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NotificationDocumentEntity that = (NotificationDocumentEntity) o;
        return getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "Notification{" +
                "id='" + id + '\'' +
                ", eventId='" + eventId + '\'' +
                ", type=" + type +
                ", userId='" + userId + '\'' +
                ", title='" + title + '\'' +
                ", payload=" + payload +
                ", channels=" + channels +
                ", status=" + status +
                ", createdAt=" + createdAt +
                '}';
    }
}
