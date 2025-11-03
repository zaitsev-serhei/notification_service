package com.notification_service.notification_service;

import com.notification_service.notification_service.common.NotificationChannel;
import com.notification_service.notification_service.common.NotificationStatus;
import com.notification_service.notification_service.common.NotificationType;
import com.notification_service.notification_service.db.entity.NotificationDocumentEntity;
import com.notification_service.notification_service.db.repository.NotificationRepositoryMongo;
import com.notification_service.notification_service.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.PageRequest;
import org.springframework.kafka.core.KafkaTemplate;
import org.vavilonLearn.contracts.events.TaskCreatedEvent;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@SpringBootApplication
public class NotificationServiceApplication {
	@Autowired
	private KafkaTemplate<String,Object> kafkaTemplate;

	public static void main(String[] args) {
		SpringApplication.run(NotificationServiceApplication.class, args);
	}

	@Bean
	public CommandLineRunner testMongoConnection(NotificationRepositoryMongo notificationRepositoryMongo) {

		return args -> {
//			TaskCreatedEvent evt = new TaskCreatedEvent(
//					UUID.randomUUID().toString(),
//					"12121",
//					"111100",
//					"2200",
//					"Test title",
//					"Test description",
//					Instant.now()
//			);
//			kafkaTemplate.send("task-created", evt.getCreatorId(), evt)
//					.whenComplete((result, ex) -> {
//						if (ex == null) {
//							System.out.println("✅ Sent test event at offset: " + result.getRecordMetadata().offset());
//						} else {
//							System.err.println("❌ Failed to send test event");
//							ex.printStackTrace();
//						}
//					});
			System.out.println("Перевірка з'єднання з MongoDB...");

			NotificationDocumentEntity notification = new NotificationDocumentEntity();
			notification.setUserId("123");
			notification.setType(NotificationType.SYSTEM);
			notification.setTitle("Тестове повідомлення");
			notification.setBody("MongoDB підключення працює успішно");
			notification.setPayload(Map.of("extra_info", "це тест"));
			notification.setChannels(List.of(NotificationChannel.EMAIL.name(), NotificationChannel.IN_APP.name()));
			notification.setStatus(NotificationStatus.PENDING);
			notification.setCreatedAt(Instant.now());


			NotificationDocumentEntity result = notificationRepositoryMongo.findByStatus(NotificationStatus.PENDING, PageRequest.of(0,10)).getContent().get(0);

			System.out.println("Збережено повідомлення з ID: " + result.toString());
			System.out.println("Усього повідомлень у колекції: " + notificationRepositoryMongo.count());
		};
	}
}
