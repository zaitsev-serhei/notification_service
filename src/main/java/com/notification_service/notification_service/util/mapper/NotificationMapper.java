package com.notification_service.notification_service.util.mapper;

import com.notification_service.notification_service.db.entity.NotificationDocumentEntity;
import com.notification_service.notification_service.domain.model.Notification;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NotificationMapper {
    Notification toDomain(NotificationDocumentEntity entity);
    NotificationDocumentEntity toEntity(Notification model);
}
