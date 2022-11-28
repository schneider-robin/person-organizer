package de.schneider.robin.backend.db.repository;

import de.schneider.robin.backend.db.model.Person;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PersonRepository extends
        PagingAndSortingRepository<Person, UUID>,
        QuerydslPredicateExecutor<Person>
{
    List<Person> findAll();

    Optional<Person> findPersonByFirstname(
            String firstname
    );

    @Query(value = "select * "
            + "from person_organizer.person p "
            + "inner join person_organizer.person_attributes pa "
            + "on p.person_attributes_id = pa.person_attributes_id "
            + "where pa.age < :age", nativeQuery = true)
    List<Person> findPeopleUnderAge(int age);

    @Query(value = "select p "
            + "from Person p "
            + "inner join p.personAttributes "
            + "where p.personAttributes.age > :age") // jpql
    List<Person> findPeopleOverAge(int age);

}
