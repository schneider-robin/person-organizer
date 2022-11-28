package de.schneider.robin.backend.service;

import de.schneider.robin.backend.api.model.PersonMinimal;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Async
public class CreatePersonListener {

    private final PersonService personService;

    public CreatePersonListener(
            PersonService personService
    ) {
        this.personService = personService;
    }

    @Async
    @EventListener
    public void handleAsyncEvent(
            PersonMinimal personMinimal
    ) {
        personService.mapAndSavePersonMinimal(personMinimal);
    }

}
