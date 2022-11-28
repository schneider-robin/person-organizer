package de.schneider.robin.backend.service;

import de.schneider.robin.backend.api.model.PersonMinimal;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class CreatePersonPublisher {

    private final ApplicationEventPublisher publisher;

    CreatePersonPublisher(
            ApplicationEventPublisher publisher
    ) {
        this.publisher = publisher;
    }

    public void publishPersonMinimalAsync(
            PersonMinimal personMinimal
    ) {
        publisher.publishEvent(personMinimal);
    }

}
