package de.schneider.robin.backend.integration.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthorizationTest {

    // Use WithMockUser() to check the roles of the user via mvcMatchers (authorization)
    // Important: no authentication process at all (so no UserDetailsService/etc.)
    // And therefore: username and password are not relevant, only the roles
    // https://docs.spring.io/spring-security/site/docs/current/reference/html5/#test-method-withmockuser
    // https://youtu.be/Ld9_M3QEC1U

    private final String URL = "/protected/people/delete"; // only allowed to access for ADMIN, not BASIC or SUPER

    @Autowired
    private MockMvc mvc;

    @Test
    @WithMockUser(roles = "BASIC")
    public void shouldReturn403WhenBasicRoleWantToDeleteAllPeople() throws Exception {
        mvc.perform(post(URL)).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "SUPER")
    public void shouldReturn403WhenSuperRoleWantToDeleteAllPeople() throws Exception {
        mvc.perform(post(URL)).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void shouldReturn200WhenAdminRoleWantToDeleteAllPeople() throws Exception {
        mvc.perform(post(URL)).andExpect(status().isOk());
    }

    // the default role is "USER" (check roles() in annotation)
    // and therefore the test will return 403 (as with "BASIC" and "SUPER" user)
    // but normally (after the authentication process) there is no unknown user with role "USER"
    // so this test is not really necessary
    
    @Test
    @WithMockUser
    public void shouldReturn403WhenUnknownRoleWantToDeleteAllPeople() throws Exception {
        mvc.perform(post(URL)).andExpect(status().isForbidden());
    }

}
