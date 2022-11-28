package de.schneider.robin.backend.unit.service;

import de.schneider.robin.backend.db.model.Person;
import de.schneider.robin.backend.db.repository.PersonRepository;
import de.schneider.robin.backend.across.mapper.PersonMapper;
import de.schneider.robin.backend.across.error.PersonNotFoundError;
import de.schneider.robin.backend.service.PersonServiceDefault;
import de.schneider.robin.lib.api.model.PersonResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PersonServiceNativeTest {

    // mockito instead of spring annotations
    // e.g. @Mock instead of @MockBean

    private static final String FIRSTNAME = "Hans";
    private static final String FULL_NAME = "Hans Ebert";
    private static final String FIRSTNAME_WRONG = "Peter";

    @InjectMocks
    private PersonServiceDefault personService;

    @Mock
    private PersonRepository personRepository;

    @Mock
    private PersonMapper personMapper;

    // no additional mocks for other dependencies necessary (compared to PersonServiceSpringTest)

    @Test
    public void getPersonByFirstnameShouldReturnPersonResponse() throws PersonNotFoundError {
        when(personRepository.findPersonByFirstname(FIRSTNAME)).thenReturn(Optional.of(new Person()));
        when(personMapper.entityToResponse(any())).thenReturn(new PersonResponse().fullName(FULL_NAME));

        PersonResponse personResponse = personService.getPersonByFirstname(FIRSTNAME);

        verify(personRepository, times(1)).findPersonByFirstname(eq(FIRSTNAME));
        verify(personMapper, times(1)).entityToResponse(any());
        assertThat(personResponse.getFullName()).isEqualTo(FULL_NAME);
    }

    @Test
    public void getPersonByFirstnameShouldReturnPersonNotFoundError() throws PersonNotFoundError {
        when(personRepository.findPersonByFirstname(FIRSTNAME_WRONG)).thenReturn(Optional.empty());
        assertThatExceptionOfType(PersonNotFoundError.class)
                .isThrownBy(() -> personService.getPersonByFirstname(FIRSTNAME_WRONG));
    }

    @Test
    public void getPersonByFirstnameShouldReturnPersonNotFoundError_v2() throws PersonNotFoundError {
        when(personRepository.findPersonByFirstname(FIRSTNAME_WRONG)).thenReturn(Optional.empty());
        assertThrows(PersonNotFoundError.class, () -> personService.getPersonByFirstname(FIRSTNAME_WRONG));
    }

}
