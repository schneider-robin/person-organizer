package de.schneider.robin.backend.api.kafka.out;

import de.schneider.robin.backend.api.model.PersonMinimal;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducer {

    private final KafkaTemplate<String, PersonMinimal> kafkaTemplate;

    public KafkaProducer(
            KafkaTemplate<String, PersonMinimal> kafkaTemplate
    ) {
        this.kafkaTemplate = kafkaTemplate;
    }

    // PersonTopic was created via docker-compose-kafka
    public void sendPersonMinimalToTopic(
            PersonMinimal personMinimal
    ) {
        kafkaTemplate.send("PersonTopic", personMinimal);
    }

    // PersonTopicTestcase was created via java class KafkaConfig
    public void sendPersonMinimalToTopic2(
            PersonMinimal personMinimal
    ) {
        kafkaTemplate.send("PersonTopicTestcase", personMinimal);
    }

}