package com.notification_service.notification_service.consumer;

import com.notification_service.notification_service.common.NotificationType;
import com.notification_service.notification_service.domain.model.Notification;
import com.notification_service.notification_service.service.NotificationService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.vavilonLearn.contracts.events.TaskCreatedEvent;

import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

@Service
public class NotificationKafkaListener {
    private static final Logger logger = LoggerFactory.getLogger(NotificationKafkaListener.class);
    private final NotificationService service;

    public NotificationKafkaListener(NotificationService service) {
        this.service = service;
    }

    @KafkaListener(topics = "task-created", groupId = "notification-service", containerFactory = "taskCreatedKafkaListenerContainerFactory")
    public void handleTaskCreatedEvent(ConsumerRecord<String, TaskCreatedEvent> record) {
        try {
            String rawKey = record.key();
            String rawValue = record.value() != null ? record.value().toString() : null; // якщо JsonDeserializer вже десеріалізував - це об'єкт.toString()
            logger.debug("RAW ConsumerRecord: topic={}, partition={}, offset={}, key={}, headers={}",
                    record.topic(), record.partition(), record.offset(), rawKey, record.headers());
            logger.info("Deserialized value (record.value()): {}", rawValue);

            TaskCreatedEvent event = record.value();
            if (event == null) {
                logger.warn("Record value is null — пропускаємо. offset={}", record.offset());
                return;
            }
            logger.info("Task created listener triggered for event {}" + event.getEventId());
            logger.debug("Kafka event received: " + event.toString());
            Notification notification = new Notification();
            notification.setEventId(event.getEventId());
            notification.setUserId(event.getCreatorId());
            notification.setType(NotificationType.TASK_CREATED);
            notification.setTitle("New Task " + event.getTitle() + " created");
            notification.setBody(event.getDescription());
            notification.setPayload(Map.of("assigneeId", event.getAssigneeId()));
            logger.debug("Notification from event created: " + notification.toString());
            service.create(notification);
        } catch (Exception exception) {
            logger.error("Error processing event" + record.value().getEventId() + "log: "+ Arrays.toString(exception.getStackTrace()));
        }
    }

//    @KafkaListener(
//            topics = "task-created.DLQ",
//            groupId = "notification-service-dlq"
//    )
//    public void reprocessDlq(TaskCreatedEvent event) {
//        logger.info("Task DLQ listener triggered for eventId {}", event.getEventId());
//        logger.debug("Kafka DLQ event received: " + event.toString());
//        try {
//        Notification notification = new Notification();
//        if (event.getEventId() == null || event.getEventId().isBlank()) {
//            notification.setEventId(UUID.randomUUID().toString());
//        }
//        if (event.getAssigneeId() == null || event.getAssigneeId().isBlank()) {
//            notification.getPayload().put("Assignee", UUID.randomUUID().toString());
//        }
//        notification.setUserId(event.getCreatorId());
//        notification.setType(NotificationType.TASK_CREATED);
//        notification.setTitle("New Task " + event.getTitle() + " created");
//        notification.setBody(event.getDescription());
//        notification.setPayload(Map.of("assigneeId", event.getAssigneeId()));
//        logger.debug("Notification from event created: " + notification.toString());
//        service.create(notification);
//        } catch (Exception exception) {
//            logger.error("Error processing event" + event.toString());
//        }
//    }
}
