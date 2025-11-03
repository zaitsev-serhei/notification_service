package com.notification_service.notification_service.dto;

import com.notification_service.notification_service.common.NotificationChannel;
import com.notification_service.notification_service.common.NotificationType;

import java.util.List;
import java.util.Map;

public class NotificationRequest {
    private String userId;
    private NotificationType type;
    private String title;
    private String body;
    private Map<String, Object> payload;
    private List<NotificationChannel> channels;

    public NotificationRequest() {
    }

    public NotificationRequest(String userId,
                               NotificationType type,
                               String title,
                               String body,
                               Map<String, Object> payload,
                               List<NotificationChannel> channels) {
        this.userId = userId;
        this.type = type;
        this.title = title;
        this.body = body;
        this.payload = payload;
        this.channels = channels;
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
}
