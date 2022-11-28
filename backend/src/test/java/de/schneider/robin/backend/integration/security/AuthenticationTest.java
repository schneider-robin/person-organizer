package de.schneider.robin.backend.integration.security;

import de.schneider.robin.lib.api.model.PersonRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthenticationTest {

    // Use httpBasic() to go through the whole security process (authentication and authorization)
    // To separate concerns, use different tests for authentication and authorization
    // So in this test class we use an endpoint without any authorization
    // https://docs.spring.io/spring-security/site/docs/4.2.x/reference/html/test-mockmvc.html#testing-http-basic-authentication
    // https://youtu.be/Yl145xLqG4E

    private final String URL = "/protected/person"; // only authentication required

    @Autowired
    private MockMvc mvc;

    @Test
    public void shouldReturn200WhenBasicUserWantToCreateNewPerson() throws Exception {
        mvc.perform(post(URL).content(getDummyBody()).with(httpBasic("basic", "basic")))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturn200WhenSuperUserWantToCreateNewPerson() throws Exception {
        mvc.perform(post(URL).content(getDummyBody()).with(httpBasic("super", "super")))
                .andExpect(status().isOk());
    }
    
    @Test
    public void shouldReturn200WhenAdminUserWantToCreateNewPerson() throws Exception {
        mvc.perform(post(URL).content(getDummyBody()).with(httpBasic("admin", "admin")))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturn401WhenUnknownUserWantToCreateNewPerson() throws Exception {
        mvc.perform(post(URL).content(getDummyBody()).with(httpBasic("unknown", "unknown")))
                //.andExpect(unauthenticated()); // alternative way
                .andExpect(status().isUnauthorized());
    }

    private String getDummyBody() {
        return new PersonRequest().toString();
    }

}
