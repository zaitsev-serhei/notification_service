package com.notification_service.notification_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotificationByUserIdNotFoundException extends RuntimeException{
    private final String userId;

    public NotificationByUserIdNotFoundException(String userId) {
        super("Notification for user with id " + userId + " not found!");
        this.userId = userId;
    }

    public String getNotificationId() {
        return this.userId;
    }
}
