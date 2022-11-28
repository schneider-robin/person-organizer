package de.schneider.robin.frontend.backend;

import de.schneider.robin.lib.api.model.PersonResponse;

import java.util.List;

public interface BackendClient {

    List<PersonResponse> fetchPersonResponseList();
}
