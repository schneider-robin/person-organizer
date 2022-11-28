package de.schneider.robin.backend.unit.service;

import de.schneider.robin.backend.api.rest.out.RandomUserClient;
import de.schneider.robin.backend.db.model.Person;
import de.schneider.robin.backend.db.repository.HobbyRepository;
import de.schneider.robin.backend.db.repository.PersonRepository;
import de.schneider.robin.backend.db.repository.PhysicalAddressRepository;
import de.schneider.robin.backend.across.mapper.PersonMapper;
import de.schneider.robin.backend.service.PersonService;
import de.schneider.robin.backend.service.PersonServiceDefault;
import de.schneider.robin.backend.service.QueryDslService;
import de.schneider.robin.lib.api.model.PersonResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = PersonServiceDefault.class)
public class PersonServiceSpringTest {

    private static final String FIRSTNAME = "Hans";
    private static final String FULL_NAME = "Hans Ebert";

    @Autowired
    private PersonService personService;

    @MockBean
    private PersonRepository personRepository;
    @MockBean
    private PhysicalAddressRepository physicalAddressRepository;
    @MockBean
    private HobbyRepository hobbyRepository;
    @MockBean
    private PersonMapper personMapper;
    @MockBean
    private QueryDslService queryDslService;
    @MockBean
    private RandomUserClient randomUserClient;

    @Test
    public void getPersonByFirstnameShouldReturnPersonResponse() {
        when(personRepository.findPersonByFirstname(FIRSTNAME)).thenReturn(Optional.of(new Person()));
        when(personMapper.entityToResponse(any())).thenReturn(new PersonResponse().fullName(FULL_NAME));

        PersonResponse personResponse = personService.getPersonByFirstname(FIRSTNAME);

        verify(personRepository, times(1)).findPersonByFirstname(eq(FIRSTNAME));
        verify(personMapper, times(1)).entityToResponse(any());
        assertThat(personResponse.getFullName()).isEqualTo(FULL_NAME);
    }
}
