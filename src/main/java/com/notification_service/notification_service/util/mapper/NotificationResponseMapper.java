package com.notification_service.notification_service.util.mapper;

import com.notification_service.notification_service.domain.model.Notification;
import com.notification_service.notification_service.dto.NotificationResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NotificationResponseMapper {
    NotificationResponse toDto(Notification notification);
}
