package de.schneider.robin.backend.api.graphql;

import de.schneider.robin.backend.api.model.PersonMinimal;
import de.schneider.robin.backend.across.error.PersonNotFoundError;
import de.schneider.robin.backend.service.PersonService;
import de.schneider.robin.lib.api.model.PersonListResponse;
import de.schneider.robin.lib.api.model.PersonResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.log4j.Log4j2;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
@Log4j2
@Tag(name = "PersonGraphqlController", description = "retrieve people")
public class PersonGraphqlController {

    private final PersonService personService;

    public PersonGraphqlController(
            PersonService personService
    ) {
        this.personService = personService;
    }

    // methods names must match with the query names in the graphql schema

    @QueryMapping()
    public PersonListResponse allPeople(

    ) {
        return personService.getAllPeople();
    }

    @QueryMapping()
    public PersonResponse getPersonByFirstname(
            @Argument String firstname
    ) throws PersonNotFoundError {
        return personService.getPersonByFirstname(firstname);
    }

    @MutationMapping()
    public PersonResponse createPersonMinimal(
            @Argument PersonMinimal personMinimal
    ) {
        return personService.mapAndSavePersonMinimalToPersonResponse(personMinimal);
    }

}
