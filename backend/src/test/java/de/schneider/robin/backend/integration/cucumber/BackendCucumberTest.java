package de.schneider.robin.backend.integration.cucumber;

import de.schneider.robin.backend.PersonOrganizerBackend;
import de.schneider.robin.lib.api.model.PersonResponse;
import io.cucumber.java.en.When;
import io.cucumber.junit.platform.engine.Cucumber;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@Cucumber // entry point to run all feature file tests
@CucumberContextConfiguration
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = PersonOrganizerBackend.class)
@ActiveProfiles(profiles={ "default" })
public class BackendCucumberTest {

    @Autowired
    protected TestRestTemplate testRestTemplate;

    @When("get person by firstname")
    public void getPersonByFirstname() {
        ResponseEntity<PersonResponse> responseEntity = testRestTemplate.getForEntity("/person/Andreas", PersonResponse.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody().getFullName()).isEqualTo("Andreas Schmidt");
    }
}
