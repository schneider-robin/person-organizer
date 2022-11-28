package de.schneider.robin.backend.integration.nativ;

import de.schneider.robin.backend.PersonOrganizerBackend;
import de.schneider.robin.backend.across.error.ErrorCode;
import de.schneider.robin.backend.across.error.ErrorDetails;
import de.schneider.robin.lib.api.model.PersonResponse;
import de.schneider.robin.lib.api.model.ProblemResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT, classes = PersonOrganizerBackend.class)
@ActiveProfiles({ "default" }) // use h2 db
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BackendRestTemplateTest {

    final private static String ENDPOINT = "/person/";

    @Autowired
    // auto configured host and port
    private TestRestTemplate testRestTemplate;

    @Test
    public void backendShouldReturnPersonResponse() {

        ResponseEntity<PersonResponse> responseEntity = testRestTemplate.getForEntity(ENDPOINT + "Andreas", PersonResponse.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody().getFullName()).isEqualTo("Andreas Schmidt");
    }

    @Test
    public void backendShouldReturnProblem() {
        ResponseEntity<ErrorDetails> responseEntity = testRestTemplate.getForEntity(ENDPOINT + "Robin", ErrorDetails.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseEntity.getBody().getErrorCode()).isEqualTo(ErrorCode.PERSON_NOT_FOUND.getErrorCode());
    }

}
