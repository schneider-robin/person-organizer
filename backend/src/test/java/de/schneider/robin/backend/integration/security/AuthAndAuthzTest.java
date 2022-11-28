package de.schneider.robin.backend.integration.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthAndAuthzTest {

    // Use WithUserDetails() to search in UserDetailsService for given user (authentication)
    // Check afterwards the roles of the user via mvcMatchers (authorization)
    // So combined authentication and authorization in this class
    // https://docs.spring.io/spring-security/site/docs/5.0.x/reference/html/test-method.html#test-method-withuserdetails
    // https://youtu.be/Ld9_M3QEC1U

    private final String URL = "/protected/people/delete"; // only allowed to access for ADMIN, not BASIC or SUPER

    @Autowired
    private MockMvc mvc;

    @Test // value == username
    @WithUserDetails(value = "basic", userDetailsServiceBeanName = "customUserDetailsService")
    public void shouldReturn403WhenBasicUserWantToDeleteAllPeople() throws Exception {
        mvc.perform(post(URL)).andExpect(status().isForbidden());
    }

    @Test // value == username
    @WithUserDetails(value = "super", userDetailsServiceBeanName = "customUserDetailsService")
    public void shouldReturn403WhenSuperUserWantToDeleteAllPeople() throws Exception {
        mvc.perform(post(URL)).andExpect(status().isForbidden());
    }

    @Test // value == username
    @WithUserDetails(value = "admin", userDetailsServiceBeanName = "customUserDetailsService")
    public void shouldReturn200WhenAdminUserWantToDeleteAllPeople() throws Exception {
        mvc.perform(post(URL)).andExpect(status().isOk());
    }

    // test with unknown user throws SecurityUserNotFoundError which leads to IllegalStateException

//    @Test // value == username
//    @WithUserDetails(value = "unknown", userDetailsServiceBeanName = "customUserDetailsService")
//    public void shouldReturn401WhenUnknownUserWantToDeleteAllPeople() throws Exception {
//        mvc.perform(post(URL)).andExpect(status().isUnauthorized());
//    }

}
