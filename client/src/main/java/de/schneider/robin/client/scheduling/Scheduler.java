package de.schneider.robin.client.scheduling;

import de.schneider.robin.client.backend.BackendClientDefault;
import de.schneider.robin.lib.api.model.PersonResponse;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(prefix = "enable", name = "backend-scheduler")
public class Scheduler {

    private final BackendClientDefault backendClient;

    public Scheduler(
            BackendClientDefault backendClient
    ) {
        this.backendClient = backendClient;
    }

    @Scheduled(initialDelay = 2000, fixedDelay = 5000)
    public void callBackend() {
        PersonResponse personResponse = backendClient.fetchPersonResponseViaRest("Andreas");
        backendClient.fetchPersonResponseViaGraphQl("Andreas");
    }
}
