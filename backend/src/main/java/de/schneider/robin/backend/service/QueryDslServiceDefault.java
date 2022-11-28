package de.schneider.robin.backend.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import de.schneider.robin.backend.db.model.Person;
import de.schneider.robin.backend.db.model.QPerson;
import de.schneider.robin.backend.db.repository.PersonRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.List;

@Log4j2
@Service
public class QueryDslServiceDefault implements QueryDslService {

    private final PersonRepository personRepository;
    private final EntityManager entityManager;

    public QueryDslServiceDefault(
            PersonRepository personRepository,
            EntityManager entityManager
    ) {
        this.personRepository = personRepository;
        this.entityManager = entityManager;
    }

    @Override
    public List<Person> getPeopleByTextInFirstname(
            String text
    ) {
        QPerson person = QPerson.person;
        BooleanExpression personWithTextInFirstname = person.firstname.contains(text);
        List<Person> people = (List<Person>) personRepository.findAll(personWithTextInFirstname);
        return people;
    }

    @Override
    public List<Person> getPeopleByTextEqLastname(
            String text
    ) {
        QPerson person = QPerson.person;

        // version 1 via JPAQuery
        JPAQuery query = new JPAQuery(entityManager);
        query.from(person).where(person.lastname.eq(text));
        List<Person> result1 = query.fetch();

        // version 2 via factory
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        List<Person> results2 = queryFactory.selectFrom(person).where(person.lastname.eq(text)).fetch();

        return results2;
    }

}
