package de.schneider.robin.backend.unit.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.schneider.robin.backend.across.error.ErrorCode;
import de.schneider.robin.backend.across.error.PersonNotFoundError;
import de.schneider.robin.backend.api.kafka.out.KafkaProducer;
import de.schneider.robin.backend.api.rest.in.PersonRestController;
import de.schneider.robin.backend.service.CreatePersonPublisher;
import de.schneider.robin.backend.service.PersonService;
import de.schneider.robin.lib.api.model.PersonRequest;
import de.schneider.robin.lib.api.model.PersonResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.StreamUtils;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = PersonRestController.class)
public class PersonRestControllerTest {

    private static final String FULL_NAME = "TestFirstname TestLastname";

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PersonService personService;

    @MockBean
    private CreatePersonPublisher createPersonPublisher;

    @MockBean
    private KafkaProducer kafkaProducer;

    @Value("classpath:data/PersonRequestInvalidMail.json")
    private Resource personRequest;

    @Test
    @WithMockUser // skip authentication, no authorization required on this endpoint
    public void shouldReturnPersonResponse() throws Exception {
        when(personService.getPersonByFirstname(any())).thenReturn(givenPersonResponse());
        String expectedResponseContent = objectMapper.writeValueAsString(givenPersonResponse());
        ResultActions serverResponse = mvc.perform(get("/person/Andreas"));
        serverResponse
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.full_name", is(FULL_NAME))) // check specific fields via jsonPath
                .andExpect(content().json(expectedResponseContent)); // alternative check of all fields via converted java object
    }

    private PersonResponse givenPersonResponse() {
        return new PersonResponse().fullName(FULL_NAME);
    }

    @Test
    @WithMockUser // skip authentication, no authorization required on this endpoint
    public void shouldReturnBadRequestWhenPostPersonWithInvalidMail() throws Exception {
        when(personService.createPerson(any(PersonRequest.class))).thenReturn(UUID.randomUUID());
        String personRequestString = StreamUtils.copyToString(personRequest.getInputStream(), StandardCharsets.UTF_8);
        ResultActions serverResponse = mvc
                .perform(post("/protected/person/valid-body").with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(personRequestString)
                );
        serverResponse
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser // skip authentication, no authorization required on this endpoint
    public void shouldReturnPersonNotFoundErrorWhenRequestingUnknownPerson() throws Exception {
        String firstname = "Andi";
        String responseMessage = String.format(PersonNotFoundError.DEFAULT_MESSAGE, firstname);

        when(personService.getPersonByFirstname(firstname)).thenThrow(new PersonNotFoundError(firstname));
        mvc.perform(get("/person/" + firstname))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.type", is(ErrorCode.PERSON_NOT_FOUND.name())))
                .andExpect(jsonPath("$.error_code", is(ErrorCode.PERSON_NOT_FOUND.getErrorCode())))
                .andExpect(jsonPath("$.message", is(responseMessage)));
    }

}
