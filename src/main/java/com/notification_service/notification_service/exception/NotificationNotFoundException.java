package com.notification_service.notification_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotificationNotFoundException extends RuntimeException {
    private final String notificationId;

    public NotificationNotFoundException(String notificationId) {
        super("Notification with id " + notificationId + " not found!");
        this.notificationId = notificationId;
    }

    public String getNotificationId() {
        return this.notificationId;
    }
}
