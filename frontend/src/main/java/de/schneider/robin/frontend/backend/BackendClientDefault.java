package de.schneider.robin.frontend.backend;

import de.schneider.robin.frontend.properties.BackendConnectionProperties;
import de.schneider.robin.lib.api.model.PersonListResponse;
import de.schneider.robin.lib.api.model.PersonResponse;
import lombok.SneakyThrows;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Component
public class BackendClientDefault implements BackendClient {

    private final BackendConnectionProperties props;
    private final RestTemplate restTemplate;

    public BackendClientDefault(
            BackendConnectionProperties backendConnectionProperties,
            RestTemplateBuilder restTemplateBuilder
    ) {
        this.props = backendConnectionProperties;
        this.restTemplate = restTemplateBuilder.build();
    }

    @SneakyThrows
    public List<PersonResponse> fetchPersonResponseList() {
        UriComponents uriComponents = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host(props.getHost())
                .port(props.getPort())
                .path("/people/all")
                .buildAndExpand();

        System.out.println(""); // new line
        System.out.println(uriComponents.toUri());

        ResponseEntity<PersonListResponse> personListResponseEntity = restTemplate.getForEntity(
                uriComponents.toString(),
                PersonListResponse.class);

        PersonListResponse personListResponse = personListResponseEntity.getBody();
        System.out.println(personListResponse);

        return personListResponse.getPersonResponses();
    }
}
