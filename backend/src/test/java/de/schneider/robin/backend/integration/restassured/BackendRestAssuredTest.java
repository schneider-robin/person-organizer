package de.schneider.robin.backend.integration.restassured;

import de.schneider.robin.backend.across.error.ErrorCode;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles({ "default" }) // use h2 db
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BackendRestAssuredTest {

    @Value("classpath:data/PersonRequest.json")
    private Resource personRequest;

    @Value("classpath:data/personRequestMinimal.json")
    private Resource personRequestMinimal;

    @Value("classpath:data/PersonRequestInvalidMail.json")
    private Resource personRequestInvalidMail;

    @LocalServerPort
    private int port;

    @BeforeAll
    public void setUp() {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";
    }

    @Test
    public void backendShouldReturnPersonResponse() {
        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
        .when()
                .get("/person/Andreas")
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("full_name", is("Andreas Schmidt"))
                .body("hobby_responses.name", hasItems("Fussball", "Basketball"));
    }

    @Test
    public void backendShouldReturnProblem() {
        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
        .when()
                .get("/person/Robin")
        .then()
                .log().all()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("error_code", is(ErrorCode.PERSON_NOT_FOUND.getErrorCode()));
    }

    @Test
    public void backendShouldProcessNewPerson() throws IOException {
        String personRequestString = StreamUtils.copyToString(personRequestMinimal.getInputStream(), StandardCharsets.UTF_8);
        given()
                .body(personRequestString)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
        .when()
                .post("/person/minimal")
        .then()
                .log().all()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    public void backendShouldReturnBadRequestWhenPostPersonWithInvalidMail() throws IOException {
        String personRequestString = StreamUtils.copyToString(personRequestInvalidMail.getInputStream(), StandardCharsets.UTF_8);
        given()
                .body(personRequestString)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                // to pass the authentication, we provide a valid user, no authorization roles required
                .auth().preemptive().basic("basic", "basic")
        .when()
                .post("/protected/person/valid-body")
        .then()
                .log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value()); // MethodArgumentNotValidException in GlobalExceptionHandler
    }

}
