# 📢 Notification Microservice

The **Notification Microservice** is responsible for handling and delivering notifications triggered by events from other microservices (for example, *Task Creation Events*).  
It listens to Kafka topics, processes incoming events, and stores notifications in MongoDB Atlas for later delivery through different channels such as **Email** or **In-App notifications**.

---

## 🚀 Features

- ✅ Consumes events from **Kafka** (e.g., `task-created` topic)  
- 🔄 Deserializes and processes `TaskCreatedEvent` objects  
- 💾 Saves notifications in **MongoDB Atlas**  
- 📬 Supports multiple notification channels (EMAIL, IN_APP)  
- 🧠 Clean and modular structure using **Spring Boot**  
- 🪵 Detailed logging for event lifecycle (before and after deserialization)

# ⚙️ Configuration

Your application.properties file should contain the following settings:
# Kafka Configuration

spring.kafka.bootstrap-servers=localhost:9093
spring.kafka.consumer.group-id=notification-service
spring.kafka.consumer.auto-offset-reset=earliest
spring.data.mongodb.uri=mongodb://<MongoDB Atlas URL>/notifications_db

# Logging Configuration
logging.level.root=INFO
logging.level.com.notification_service=DEBUG

# 🧠 Key Classes
Class	Description
NotificationKafkaListener	--> Listens to task-created Kafka topic and converts messages into notification entities
NotificationServiceImpl	--> Handles notification creation, mapping, and persistence logic
NotificationRepositoryMongo	--> Interface extending MongoRepository for CRUD operations
NotificationDocumentEntity	--> MongoDB document model describing notification fields
KafkaConfig	 --> Defines consumer factory and listener configuration for Kafka

#Testing the setup
After starting the service, you can test Kafka & MongoDB integration by sending a sample message:
@Bean
	public CommandLineRunner testMongoConnection(NotificationRepositoryMongo notificationRepositoryMongo) {

		return args -> {
			TaskCreatedEvent evt = new TaskCreatedEvent(
					UUID.randomUUID().toString(),
					"12121",
					"111100",
					"2200",
					"Test title",
					"Test description",
					Instant.now()
			);
			kafkaTemplate.send("task-created", evt.getCreatorId(), evt)
					.whenComplete((result, ex) -> {
						if (ex == null) {
							System.out.println("✅ Sent test event at offset: " + result.getRecordMetadata().offset());
						} else {
							System.err.println("❌ Failed to send test event");
							ex.printStackTrace();
						}
					});
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
  
  # 🧰 How to Run Locally
  1. Start Kafka and MongoDB
  2. Build and Run the service:
    mvn clean package
    java -jar target/notification_service-1.0-SNAPSHOT.jar
or mvn spring-boot:run (from project directory * if Maven is added to PATH)
  3. Check logs

# 👤 Author
Serhii Zaitsev

  
