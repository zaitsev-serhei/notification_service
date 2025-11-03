package com.notification_service.notification_service.db.entity;

import com.notification_service.notification_service.common.NotificationStatus;
import com.notification_service.notification_service.common.NotificationType;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Document("notifications")
@CompoundIndex(name = "user_createdAt_idx", def = "{'user_id':1, 'createdAt':-1}")
public class NotificationDocumentEntity {
    @Id
    private String id;
    @Indexed(unique = true, sparse = true)
    @Field("eventId")
    private String eventId;
    @Field("type")
    private NotificationType type;
    @Field("userId")
    private String userId;
    @Field("title")
    private String title;
    @Field("body")
    private String body;
    @Field("payload")
    private Map<String, Object> payload;
    @Field("channels")
    private List<String> channels;
    @Field("status")
    private NotificationStatus status;
    @Field("createdAt")
    private Instant createdAt;
    @Field("readAt")
    private Instant readAt;
    @Field("wasRead")
    private boolean wasRead;
    @Field("attempt")
    private int attempt;

    public NotificationDocumentEntity() {
    }

    public NotificationDocumentEntity(String id,
                                      String eventId,
                                      String userId,
                                      NotificationType type,
                                      String title,
                                      String body,
                                      Map<String, Object> payload,
                                      List<String> channels,
                                      NotificationStatus status,
                                      Instant createdAt,
                                      boolean wasRead,
                                      Instant readAt,
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
        this.wasRead = wasRead;
        this.readAt = readAt;
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

    public boolean wasRead() {
        return wasRead;
    }

    public void setWasRead(boolean wasRead) {
        this.wasRead = wasRead;
    }

    public void setReadAt(Instant readAt) {
        this.readAt = readAt;
    }

    public int getAttempt() {
        return attempt;
    }

    public void setAttempt(int attempt) {
        this.attempt = attempt;
    }


    @Override
    public String toString() {
        return "NotificationDocument{" +
                "id='" + id + '\'' +
                ", eventId='" + eventId + '\'' +
                ", userId='" + userId + '\'' +
                ", type=" + type +
                ", title='" + title + '\'' +
                ", body='" + body + '\'' +
                ", payload=" + payload +
                ", channels=" + channels +
                ", status=" + status +
                ", createdAt=" + createdAt +
                ", wasRead=" + wasRead +
                ", readAt=" + readAt +
                '}';
    }

    public static NotificationDocumentEntity fromEvent(String eventId,
                                                       String userId,
                                                       NotificationType type,
                                                       String title,
                                                       String body,
                                                       Map<String, Object> payload,
                                                       List<String> channels,
                                                       NotificationStatus status,
                                                       Instant createdAt,
                                                       Instant readAt) {
        NotificationDocumentEntity doc = new NotificationDocumentEntity();
        doc.setEventId(eventId);
        doc.setUserId(userId);
        doc.setType(type);
        doc.setTitle(title);
        doc.setBody(body);
        doc.setPayload(payload);
        doc.setChannels(channels);
        doc.setStatus(NotificationStatus.PENDING);
        doc.setAttempt(0);
        doc.setCreatedAt(Instant.now());
        doc.setWasRead(false);
        doc.setReadAt(null);
        return doc;
    }
}
