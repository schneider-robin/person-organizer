package de.schneider.robin.client.backend.wiremock;

import com.github.tomakehurst.wiremock.WireMockServer;
import de.schneider.robin.client.backend.BackendClient;
import de.schneider.robin.client.backend.BackendClientDefault;
import de.schneider.robin.client.properties.BackendConnectionProperties;
import de.schneider.robin.lib.api.model.PersonAttributesReqRes;
import de.schneider.robin.lib.api.model.PersonAttributesResponse;
import de.schneider.robin.lib.api.model.PersonResponse;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.client.RestTemplateAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { BackendClientDefault.class, BackendConnectionProperties.class, RestTemplateAutoConfiguration.class })
@EnableConfigurationProperties
@TestPropertySource(properties = {
        "backend.host=localhost",
        "backend.port=8080"
})
public class WiremockClientTest {

    private static final int PORT = 8080;
    private static final WireMockServer wireMockServer = new WireMockServer(PORT);
    private static final String FIRSTNAME = "Andreas";

    @Autowired
    private BackendClient backendClient;

    // file must be present in target/test-classes
    // (otherwise FileNotFoundException is thrown)
    // therefore you have to run mvn test
    @Value("classpath:data/PersonResponseMinimal.json")
    private Resource personResponseMinimal;

    @BeforeAll
    public static void beforeAll() {
        wireMockServer.start();
    }

    @AfterAll
    public static void afterAll() {
        wireMockServer.stop();
    }

    @Test
    public void shouldFetchPersonResponseFromBackend() throws IOException {
        String body = StreamUtils.copyToString(personResponseMinimal.getInputStream(), StandardCharsets.UTF_8);
        wireMockServer.stubFor(get(urlPathEqualTo("/person/" + FIRSTNAME))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("content-type", "application/json")
                        .withBody(body)));

        PersonResponse personResponse = backendClient.fetchPersonResponseViaRest(FIRSTNAME);
        // use extra java class for assertion ... in BackendClientTest it's via server response body
        assertThat(personResponse).usingRecursiveComparison().isEqualTo(expectedPersonResponse());
    }

    private PersonResponse expectedPersonResponse() {
        PersonAttributesResponse personAttributesResponse = (PersonAttributesResponse) new PersonAttributesResponse()
                .age(45)
                .height(1.82)
                .country(PersonAttributesReqRes.CountryEnum.DE);
        return new PersonResponse()
                .fullName("Andreas Schmidt")
                .personAttributesResponse(personAttributesResponse);
    }
}
