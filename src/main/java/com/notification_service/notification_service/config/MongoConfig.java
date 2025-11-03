package com.notification_service.notification_service.config;

import com.notification_service.notification_service.db.entity.NotificationDocumentEntity;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.index.IndexOperations;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Configuration
public class MongoConfig {

    public MongoConfig(ObjectProvider<MongoTemplate> mongoTemplateProvider, ApplicationContext applicationContext) {
        this.mongoTemplateProvider = mongoTemplateProvider;
        this.applicationContext = applicationContext;
    }

    @Bean
    public MongoCustomConversions mongoCustomConversions() {
        List<Object> converters = new ArrayList<>();
        converters.add(new InstantToDateConverter());
        converters.add(new DateToInstantConverter());
        return new MongoCustomConversions(converters);
    }

    private final ObjectProvider<MongoTemplate> mongoTemplateProvider;
    private final ApplicationContext applicationContext;

    @EventListener(ContextRefreshedEvent.class)
    public void initIndexes() {
        MongoTemplate template = mongoTemplateProvider.getIfAvailable();
        if (template == null) {
            // Альтернативно, можна кинути помилку або логувати
            // але здебільшого в нормальному запуску template має бути доступний
            template = applicationContext.getBean(MongoTemplate.class);
        }
        IndexOperations ops = template.indexOps(NotificationDocumentEntity.class);

        ops.createIndex(new Index().on("eventId", Sort.Direction.ASC).unique());
        ops.createIndex(new Index().on("userId", Sort.Direction.ASC).on("createdAt", Sort.Direction.DESC));

        long ttlSeconds = 90L * 24 * 60 * 60;
        ops.createIndex(new Index().on("createdAt", Sort.Direction.ASC).expire(ttlSeconds));
    }

    @WritingConverter
    static class InstantToDateConverter implements Converter<Instant, Date> {

        @Override
        public Date convert(Instant source) {
            return source == null ? null : Date.from(source);
        }
    }

    @ReadingConverter
    static class DateToInstantConverter implements Converter<Date, Instant> {

        @Override
        public Instant convert(Date source) {
            return source == null ? null : source.toInstant();
        }
    }
}
