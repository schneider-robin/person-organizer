package de.schneider.robin.backend.unit.db;

import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.spring.api.DBRider;
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

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles({ "default" }) // use h2 db
@DBRider
@DBUnit(qualifiedTableNames = true)
public class PersonRepositoryDbRiderTest {

    private static final String FIRSTNAME = "Peter";

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @DataSet(
            value = "data/PersonEntity.yaml",
            cleanBefore = true,
            skipCleaningFor = { "PUBLIC.DATABASECHANGELOGLOCK", "PUBLIC.DATABASECHANGELOG" }
    )
    public void findPersonByFirstnameShouldReturnPerson() {
        Person person = personRepository.findPersonByFirstname(FIRSTNAME).get();
        assertThat(person.getLastname()).isEqualTo("Fischer");
    }

    @Test
    @DataSet(cleanBefore = true, skipCleaningFor = { "PUBLIC.DATABASECHANGELOGLOCK", "PUBLIC.DATABASECHANGELOG" })
    @ExpectedDataSet(
            value = "data/PersonEntity.yaml",
            ignoreCols = {
                    "PERSON_ID", "PERSON_ATTRIBUTES_ID", "PHYSICAL_ADDRESS_ID"
            }
    )
    public void savePerson() {
        personRepository.save(givenPerson());
        entityManager.flush();
        // DbRider will automatically check the ExpectedDataSet
    }

    private Person givenPerson() {
        PersonAttributes personAttributes = PersonAttributes.builder()
                .age(12)
                .height(1.56)
                .country(Country.DE)
                .build();
        PhysicalAddress physicalAddress = PhysicalAddress.builder()
                .street("Wolfgang 34")
                .city("Stuttgart")
                .build();

        return Person.builder()
                .firstname(FIRSTNAME)
                .lastname("Fischer")
                .personAttributes(personAttributes)
                .physicalAddress(physicalAddress)
                .build();
    }
}
