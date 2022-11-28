package de.schneider.robin.backend.unit.db;

import de.schneider.robin.backend.db.model.Hobby;
import de.schneider.robin.backend.db.model.MailAddress;
import de.schneider.robin.backend.db.model.Person;
import de.schneider.robin.backend.db.model.PersonAttributes;
import de.schneider.robin.backend.db.model.PhysicalAddress;
import de.schneider.robin.backend.db.repository.PersonRepository;
import de.schneider.robin.backend.db.type.Country;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles({ "default" }) // use h2 db
//@TestPropertySource(properties = {
        // "spring.liquibase.enabled=false" // not possible, is required
//})
public class PersonRepositoryTest {

    private static final String FIRSTNAME = "TestFirstname";

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void findPersonByFirstnameShouldReturnPerson() {
        givenPerson();
        Person foundPerson = personRepository.findPersonByFirstname(FIRSTNAME).get();
        assertThat(foundPerson.getFirstname()).isEqualTo(FIRSTNAME);
    }

    private void givenPerson() {
        PersonAttributes personAttributes = PersonAttributes.builder()
                .age(32)
                .height(1.80)
                .country(Country.DE)
                .build();
        PhysicalAddress physicalAddress = PhysicalAddress.builder()
                .street("TestStreet")
                .city("TestCity")
                .build();
        MailAddress mailAddress = MailAddress.builder()
                .contact("test-mail.de")
                .isBusiness(true)
                .build();
        Hobby hobby = Hobby.builder()
                .name("TestHobby")
                .cost(new BigDecimal("125.8"))
                .build();
        Person person = Person.builder()
                .firstname(FIRSTNAME)
                .lastname("TestLastname")
                .personAttributes(personAttributes)
                .physicalAddress(physicalAddress)
                .mailAddresses(List.of(mailAddress))
                .hobbies(List.of(hobby))
                .build();
        mailAddress.setPerson(person);
        entityManager.persistAndFlush(person);
        //personRepository.save(person);

    }
}
