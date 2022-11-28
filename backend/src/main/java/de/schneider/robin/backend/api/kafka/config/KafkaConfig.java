package de.schneider.robin.backend.api.kafka.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("kafka")
public class KafkaConfig {

    // PersonTopic is created via docker-compose-kafka

    @Bean
    public NewTopic createTopic() {
        return new NewTopic("PersonTopicTestcase", 1, (short) 1);
    }

}
