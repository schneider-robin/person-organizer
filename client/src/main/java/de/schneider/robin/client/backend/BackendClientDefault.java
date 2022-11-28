package de.schneider.robin.client.backend;

import de.schneider.robin.client.properties.BackendConnectionProperties;
import de.schneider.robin.lib.api.model.PersonResponse;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.graphql.ResponseField;
import org.springframework.graphql.client.HttpGraphQlClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Component
@Log4j2
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
    public PersonResponse fetchPersonResponseViaRest(
            String firstname
    ) {
        UriComponents uriComponents = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host(props.getHost())
                .port(props.getPort())
                .path("/person/")
                .path(firstname)
                .buildAndExpand();

        System.out.println(""); // new line
        System.out.println(uriComponents.toUri());

//        ResponseEntity<PersonResponse> personResponseEntityExchange = restTemplate.exchange(
//                uriComponents.toUri(),
//                HttpMethod.GET,
//                HttpEntity.EMPTY,
//                PersonResponse.class
//        );
//        System.out.println("restTemplate.exchange.getStatusCode():   " + personResponseEntityExchange.getStatusCode());

//        PersonResponse personResponse = restTemplate.getForObject(
//                uriComponents.toString(),
//                PersonResponse.class
//        );
//        System.out.println("restTemplate.getForObject.getFullName(): " + personResponse.getFullName());

        ResponseEntity<PersonResponse> personResponseEntity = restTemplate.getForEntity(
                uriComponents.toString(),
                PersonResponse.class);
        System.out.println("REST call, http status code: " + personResponseEntity.getStatusCode()); //.getBody()
        //System.out.println("restTemplate.getForEntity.getHeaders():  " + personResponseEntity.getHeaders());

        return personResponseEntity.getBody();
    }

    // similar to PersonGraphqlControllerTest in backend
    @Override
    public void fetchPersonResponseViaGraphQl(
            String firstname
    ) {
        // uses the webclient in the background (webflux dependency)
        HttpGraphQlClient graphQlClient = HttpGraphQlClient
                .builder()
                .url("http://localhost:8080/graphql")
                //.header("Content-Type", "application/json")
                .build();

        // set this comment to enable code completion in the string
        // language=GraphQL
        String document =
        "query Person($firstname: String!){" +
        "  getPersonByFirstname(firstname: $firstname) {" +
        "    fullName" +
        "    creationTimestamp" +
        "  }" +
        "}";

        // not possible to use the PersonResponse from lib (like in REST call)
        // maybe because constructor is missing
        PersonResponseGraphql personResponseGraphql = graphQlClient
                .document(document)
                .variable("firstname", "Andreas")
                .retrieve("getPersonByFirstname")
                .toEntity(PersonResponseGraphql.class)
                .block();

        System.out.println("GraphQL call, response: " + personResponseGraphql);

//        String fullName = graphQlClient
//                .document(document)
//                .variable("firstname", "Andreas")
//                .retrieve("getPersonByFirstname.fullName")
//                .toEntity(String.class)
//                .block();
//
//        System.out.println("GraphQL call, person fullName: " + fullName);
    }
}
