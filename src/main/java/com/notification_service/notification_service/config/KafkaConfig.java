package com.notification_service.notification_service.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.DeserializationException;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.util.backoff.FixedBackOff;
import org.vavilonLearn.contracts.events.TaskCreatedEvent;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaConfig {
    private final static Logger logger = LoggerFactory.getLogger(KafkaConfig.class);
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServer;

    @Bean
    public ObjectMapper objectMapper() {
        logger.info("Object mapper created");
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule()); //for Instant mapping
        mapper.disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        logger.debug("Object mapper registered modules include "+ mapper.getRegisteredModuleIds());
        return mapper;
    }

    @Bean
    public KafkaTemplate<String, Object> kafkaTemplateForDlq(ProducerFactory<String, Object> pf) {
        return new KafkaTemplate<>(pf);
    }

    @Bean
    public ProducerFactory<String, Object> producerFactoryForDlq(ObjectMapper mapper) {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put(JsonSerializer.ADD_TYPE_INFO_HEADERS, true);
        return new DefaultKafkaProducerFactory<>(props);
    }

    @Bean
    public DefaultErrorHandler errorHandler(KafkaTemplate<String, Object> dlqTemplate) {
        // DeadLetterPublishingRecoverer: перекидає запис у topic + ".DLQ" на ту саму partition
        DeadLetterPublishingRecoverer recoverer =
                new DeadLetterPublishingRecoverer(dlqTemplate,
                        (consumerRecord, e) -> new TopicPartition(consumerRecord.topic() + ".DLQ", consumerRecord.partition()));

        // FixedBackOff: спробувати 3 рази з інтервалом 1s, потім DLQ
        FixedBackOff fixedBackOff = new FixedBackOff(1000L, 3L);
        DefaultErrorHandler handler = new DefaultErrorHandler(recoverer, fixedBackOff);

        // Не ретраїти десеріалізаційні помилки — відправляти в DLQ одразу
        handler.addNotRetryableExceptions(SerializationException.class);
        handler.addNotRetryableExceptions(DeserializationException.class);

        return handler;
    }

    @Bean("main")
    public ConsumerFactory<String, TaskCreatedEvent> taskEventConsumerFactory(ObjectMapper mapper) {
        logger.info("TaskEvent consumer factory initialized");
        JsonDeserializer<TaskCreatedEvent> deserializer =
                new JsonDeserializer<>(TaskCreatedEvent.class, mapper);
        deserializer.setUseTypeMapperForKey(false);
        deserializer.addTrustedPackages(
                "org.vavilonLearn.contracts.events",
                "org.vavilonLearn.contracts",
                "com.notification_service"
        );
        deserializer.setUseTypeHeaders(true);

        ErrorHandlingDeserializer<TaskCreatedEvent> errorHandlingDeserializer = new ErrorHandlingDeserializer<>(deserializer);

        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "notification-service");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        logger.info("TaskEvent consumer factory properties are set");
        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), errorHandlingDeserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, TaskCreatedEvent> dlqKafkaListenerContainerFactory(
            @Qualifier("dlq") ConsumerFactory<String, TaskCreatedEvent> taskEventDlqFactory) {

        ConcurrentKafkaListenerContainerFactory<String, TaskCreatedEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(taskEventDlqFactory);
        factory.setConcurrency(1);
        return factory;
    }
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, TaskCreatedEvent> taskCreatedKafkaListenerContainerFactory(
            @Qualifier("main") ConsumerFactory<String,TaskCreatedEvent> taskCreatedConsumerFactory) {
        logger.info("TaskCreated Listener Factory initialized");
        ConcurrentKafkaListenerContainerFactory<String, TaskCreatedEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(taskCreatedConsumerFactory);
        factory.setConcurrency(3); // підлаштуй під кількість partition
        // Optional: configure error handler, backoff, DLQ publishing, etc.
        // DefaultErrorHandler errHandler = new DefaultErrorHandler(...);
        // factory.setCommonErrorHandler(errHandler);
        return factory;
    }

    @Bean("dlq")
    public ConsumerFactory<String, TaskCreatedEvent> taskEventDlqConsumerFactory(ObjectMapper mapper) {
        JsonDeserializer<TaskCreatedEvent> deserializer =
                new JsonDeserializer<>(TaskCreatedEvent.class, mapper);
        deserializer.addTrustedPackages(
                "org.vavilonLearn.contracts.events",
                "org.vavilonLearn.contracts",
                "com.notification_service"
        );

        ErrorHandlingDeserializer<TaskCreatedEvent> errorHandlingDeserializer =
                new ErrorHandlingDeserializer<>(deserializer);

        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "notification-service-dlq");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, TaskCreatedEvent.class.getName());
        props.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, true);

        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), errorHandlingDeserializer);
    }
}
