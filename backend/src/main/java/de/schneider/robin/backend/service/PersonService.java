package de.schneider.robin.backend.service;

import de.schneider.robin.backend.api.rest.in.model.CompletePersonPageResponse;
import de.schneider.robin.backend.db.model.Person;
import de.schneider.robin.backend.api.model.PersonMinimal;
import de.schneider.robin.backend.across.error.PersonNotFoundError;
import de.schneider.robin.lib.api.model.PersonListResponse;
import de.schneider.robin.lib.api.model.PersonRequest;
import de.schneider.robin.lib.api.model.PersonResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

public interface PersonService {

    PersonResponse getPersonByFirstname(
            String firstname
    ) throws PersonNotFoundError;

    PersonListResponse getPeopleUnderAge(
            int age
    );

    PersonListResponse getPeopleOverAge(
            int age
    );

    PersonListResponse getPeopleByTextInFirstname(
            String text
    );

    PersonListResponse getPeopleByTextEqLastname(
            String text
    );

    PersonListResponse getAllPeople(

    );

    String getAllPeopleAsCsv(

    );

    CompletePersonPageResponse getPeoplePage(
            int pageSize,
            int pageNumber
    );

    UUID createPerson(
            PersonRequest personRequest
    );

    String createPerson(
            MultipartFile csvFile
    ) throws IOException;

    String createPersonRandom(

    );

    Person mapAndSavePersonMinimal(
            PersonMinimal personMinimal
    );

    PersonResponse mapAndSavePersonMinimalToPersonResponse(
            PersonMinimal personMinimal
    );

    int deleteAllPeople(

    );
}
