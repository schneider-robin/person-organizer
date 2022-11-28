package de.schneider.robin.backend.integration.testcontainer;

import de.schneider.robin.backend.db.model.Hobby;
import de.schneider.robin.backend.db.model.MailAddress;
import de.schneider.robin.backend.db.model.Person;
import de.schneider.robin.backend.db.model.PersonAttributes;
import de.schneider.robin.backend.db.model.PhysicalAddress;
import de.schneider.robin.backend.db.repository.PersonRepository;
import de.schneider.robin.backend.db.type.Country;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Disabled // only enable when docker is running !!!
@DataJpaTest
@Testcontainers
public class PersonRepositoryTestcontainersTest {

    private static final String FIRSTNAME = "TestFirstname";

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Container
    private static PostgreSQLContainer container = new PostgreSQLContainer<>("postgres:13.2");

    @DynamicPropertySource
    public static void overrideProps(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.username", container::getUsername);
        registry.add("spring.datasource.password", container::getPassword);
        System.out.println("Database container url: " + container.getJdbcUrl());
    }

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
