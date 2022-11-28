package de.schneider.robin.backend.service;

import de.schneider.robin.backend.db.model.Person;

import java.util.List;

public interface QueryDslService {

    List<Person> getPeopleByTextInFirstname(
            String text
    );

    List<Person> getPeopleByTextEqLastname(
            String text
    );
}
