package de.schneider.robin.client.backend.nativ;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.schneider.robin.client.backend.BackendClient;
import de.schneider.robin.client.backend.BackendClientDefault;
import de.schneider.robin.client.properties.BackendConnectionProperties;
import de.schneider.robin.lib.api.model.PersonResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RestClientTest(components = { BackendClientDefault.class, BackendConnectionProperties.class }) // enable MockRestServiceServer
public class BackendClientTest {

    private static final String FIRSTNAME = "Andreas";

    @Autowired
    private BackendClient backendClient;

    @Autowired
    private MockRestServiceServer server;

    @Autowired
    private ObjectMapper objectMapper;

    // file must be present in target/test-classes
    // (otherwise FileNotFoundException is thrown)
    // therefore you have to run mvn test
    @Value("classpath:data/PersonResponseMinimal.json")
    private Resource personResponseMinimal;

    @Test
    public void shouldFetchPersonResponseFromBackend() throws IOException {
        String body = StreamUtils.copyToString(personResponseMinimal.getInputStream(), StandardCharsets.UTF_8);
        server.expect(requestTo("http://localhost:8080/person/" + FIRSTNAME))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(body, MediaType.APPLICATION_JSON));

        PersonResponse personResponse = backendClient.fetchPersonResponseViaRest(FIRSTNAME);
        // use server response body for assertion ... in WiremockClientTest it's via extra java class
        PersonResponse expectedPersonResponse = objectMapper.readValue(body, PersonResponse.class);
        assertThat(personResponse).isEqualTo(expectedPersonResponse);
    }

}
