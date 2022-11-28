package de.schneider.robin.backend.unit.api;

import de.schneider.robin.backend.api.graphql.PersonGraphqlController;
import de.schneider.robin.backend.service.PersonService;
import de.schneider.robin.lib.api.model.PersonResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.graphql.test.tester.GraphQlTester;

import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@GraphQlTest(PersonGraphqlController.class)
public class PersonGraphqlControllerTest {

    @Autowired
    GraphQlTester graphQlTester;

    @MockBean
    PersonService personService;

    @Test
    public void getPersonByFirstname() {

        when(personService.getPersonByFirstname(eq("Andreas")))
                .thenReturn(new PersonResponse().fullName("Andreas Schmidt"));

        // set this comment to enable code completion in the string
        // language=GraphQL
        String document =
                "query Person($firstname: String!){" +
                "  getPersonByFirstname(firstname: $firstname) {" +
                "    fullName" +
                "  }" +
                "}";

        graphQlTester.document(document)
                .variable("firstname", "Andreas")
                .execute()
                .path("getPersonByFirstname.fullName")
                .entity(String.class)
                .isEqualTo("Andreas Schmidt");
    }

    @Test
    public void getPersonByFirstnameViaExternalFile() {

        when(personService.getPersonByFirstname(eq("Andreas")))
                .thenReturn(new PersonResponse().fullName("Andreas Schmidt"));

        graphQlTester.documentName("person") // name of the file in resources/graphql-test
                .variable("firstname", "Andreas")
                .execute()
                .path("getPersonByFirstname.fullName")
                .entity(String.class)
                .isEqualTo("Andreas Schmidt");
    }

    @Test
    public void createPersonMinimal() {

        when(personService.mapAndSavePersonMinimalToPersonResponse(any()))
                .thenReturn(new PersonResponse().fullName("Andreas Schmidt"));

        // set this comment to enable code completion in the string
        // language=GraphQL
        String document =
                "mutation Person($personMinimal: PersonMinimal!){" +
                "  createPersonMinimal(personMinimal: $personMinimal) {" +
                "    fullName" +
                "  }" +
                "}";

        graphQlTester.document(document)
                .variable("personMinimal", Map.of("firstname", "Andreas", "lastname", "Schmidt"))
                .execute()
                .path("createPersonMinimal.fullName")
                .entity(String.class)
                .isEqualTo("Andreas Schmidt");
    }

}
