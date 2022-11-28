package de.schneider.robin.backend.api.kafka.in;

import de.schneider.robin.backend.api.model.PersonMinimal;
import de.schneider.robin.backend.service.PersonService;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Profile("kafka")
@Log4j2
public class KafkaTopicListener {

    private final PersonService personService;

    public KafkaTopicListener(
            PersonService personService
    ) {
        this.personService = personService;
    }

    // PersonTopic was created via docker-compose-kafka
    @KafkaListener(topics = "PersonTopic", groupId = "person-organizer-group")
    public void listenToPersonTopic(
            PersonMinimal personMinimal
    ) {
        log.info("Received person from PersonTopic: " + personMinimal);
        personService.mapAndSavePersonMinimal(personMinimal);
    }

    // PersonTopicTestcase was created via java class KafkaConfig
    @KafkaListener(topics = "PersonTopicTestcase", groupId = "person-organizer-group")
    public void listenToPersonTopicTestcase(
            PersonMinimal personMinimal
    ) {
        log.info("Received person from PersonTopicTestcase: " + personMinimal);
        personService.mapAndSavePersonMinimal(personMinimal);
    }
}
