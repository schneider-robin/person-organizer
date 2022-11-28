package de.schneider.robin.client.backend;

import de.schneider.robin.lib.api.model.PersonResponse;

public interface BackendClient {

    PersonResponse fetchPersonResponseViaRest(
            String firstname
    );

    void fetchPersonResponseViaGraphQl(
            String firstname
    );
}
