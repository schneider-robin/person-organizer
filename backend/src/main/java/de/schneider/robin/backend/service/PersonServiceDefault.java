package de.schneider.robin.backend.service;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import de.schneider.robin.backend.across.aop.Printer;
import de.schneider.robin.backend.api.model.PersonMinimal;
import de.schneider.robin.backend.api.rest.in.model.CompletePersonPageResponse;
import de.schneider.robin.backend.api.rest.out.RandomUserClient;
import de.schneider.robin.backend.db.model.Hobby;
import de.schneider.robin.backend.db.model.MailAddress;
import de.schneider.robin.backend.db.model.Person;
import de.schneider.robin.backend.db.model.PhysicalAddress;
import de.schneider.robin.backend.db.repository.HobbyRepository;
import de.schneider.robin.backend.db.repository.PersonRepository;
import de.schneider.robin.backend.db.repository.PhysicalAddressRepository;
import de.schneider.robin.backend.across.mapper.PersonMapper;
import de.schneider.robin.backend.across.error.PersonNotFoundError;
import de.schneider.robin.lib.api.model.PersonListResponse;
import de.schneider.robin.lib.api.model.PersonRequest;
import de.schneider.robin.lib.api.model.PersonResponse;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.input.BOMInputStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
@Log4j2
public class PersonServiceDefault implements PersonService {

    private final PersonRepository personRepository;
    private final PhysicalAddressRepository physicalAddressRepository;
    private final HobbyRepository hobbyRepository;
    private final PersonMapper personMapper;
    private final QueryDslService queryDslService;
    private final RandomUserClient randomUserClient;

    public PersonServiceDefault(
            PersonRepository personRepository,
            PhysicalAddressRepository physicalAddressRepository,
            HobbyRepository hobbyRepository,
            PersonMapper personMapper,
            QueryDslService queryDslService,
            RandomUserClient randomUserClient
    ) {
        this.personRepository = personRepository;
        this.physicalAddressRepository = physicalAddressRepository;
        this.hobbyRepository = hobbyRepository;
        this.personMapper = personMapper;
        this.queryDslService = queryDslService;
        this.randomUserClient = randomUserClient;
    }

    @Override
    public PersonResponse getPersonByFirstname(
            String firstname
    ) {
        Person person = personRepository.findPersonByFirstname(firstname)
                .orElseThrow(() -> new PersonNotFoundError(firstname));
        PersonResponse personResponse = personMapper.entityToResponse(person);
        //log.info(personResponse);
        return personResponse;
    }

    @Override
    @Printer(message = "Service layer")
    public PersonListResponse getPeopleUnderAge(
            int age
    ) {
        List<Person> people = personRepository.findPeopleUnderAge(age);
        PersonListResponse personListResponse = personMapper.personListToPersonListResponse(people);
        //log.info(personListResponse);
        return personListResponse;
    }

    @Override
    public PersonListResponse getPeopleOverAge(
            int age
    ) {
        List<Person> people = personRepository.findPeopleOverAge(age);
        PersonListResponse personListResponse = personMapper.personListToPersonListResponse(people);
        //log.info(personListResponse);
        return personListResponse;
    }

    @Override
    public PersonListResponse getPeopleByTextInFirstname(
            String text
    ) {
        List<Person> people = queryDslService.getPeopleByTextInFirstname(text);
        PersonListResponse personListResponse = personMapper.personListToPersonListResponse(people);
        //log.info(personListResponse);
        return personListResponse;
    }

    @Override
    public PersonListResponse getPeopleByTextEqLastname(
            String text
    ) {
        List<Person> people = queryDslService.getPeopleByTextEqLastname(text);
        PersonListResponse personListResponse = personMapper.personListToPersonListResponse(people);
        //log.info(personListResponse);
        return personListResponse;
    }

    @Override
    public PersonListResponse getAllPeople(

    ) {
        List<Person> people = personRepository.findAll();
        PersonListResponse personListResponse = personMapper.personListToPersonListResponse(people);
        //log.info(personListResponse);
        return personListResponse;
    }

    @Override
    public String getAllPeopleAsCsv() {
        List<Person> people = personRepository.findAll();
        StringBuilder peopleAsCsv = new StringBuilder(createCsvStringHeader());
        for (Person person: people) {
            peopleAsCsv.append(createCsvStringPerson(person));
        }
        return peopleAsCsv.toString();
    }

    @Override
    public CompletePersonPageResponse getPeoplePage(
            int pageSize,
            int pageNumber
    ) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Person> peoplePage = personRepository.findAll(pageable);
        CompletePersonPageResponse completePersonPageResponse = personMapper.entityPageToResponsePage(peoplePage, pageSize, pageNumber);
        //log.info(completePersonPageResponse);
        return completePersonPageResponse;
    }

    @Override
    public UUID createPerson(
            PersonRequest personRequest
    ) {
        // query existing element via uuid references in personRequest
        PhysicalAddress physicalAddress = physicalAddressRepository.findPhysicalAddressByPhysicalAddressId(
                personRequest.getPhysicalAddressIdRequest());
        List<Hobby> hobbies = hobbyRepository.findHobbiesByHobbyIdIn(personRequest.getHobbyIdRequests());

        // mapping only needed for some parts of the request
        Person person = personMapper.requestToEntity(personRequest, physicalAddress, hobbies);

        // needed for @OneToMany mapping from person to mailAddress (@ManyToOne person field in mailAddress entity)
        for (MailAddress mailAddress : person.getMailAddresses()) {
            mailAddress.setPerson(person);
        }

        Person savedPerson = personRepository.save(person);
        return savedPerson.getPersonId();
    }

    @Override
    public String createPerson(
            MultipartFile csvFile
    ) throws IOException {
        InputStream inputStream = csvFile.getInputStream();
        final CsvToBean<PersonMinimal> csvPersonBean = new CsvToBeanBuilder<PersonMinimal>(
                new InputStreamReader(new BOMInputStream(inputStream), StandardCharsets.UTF_8))
                .withType(PersonMinimal.class)
                .withErrorLocale(Locale.ENGLISH)
                .withSeparator(';')
                .withThrowExceptions(false)
                .build();

        PersonMinimal personMinimal = csvPersonBean.parse().get(0); // only one person in the csv file
        Person savedPerson = mapAndSavePersonMinimal(personMinimal);
        return createCsvStringHeader() + createCsvStringPerson(savedPerson);
    }

    private String createCsvStringHeader(

    ) {
        final String SEPARATOR = ";";
        final String NEW_LINE = "\n";

        return "FIRST_NAME" + SEPARATOR + "LAST_NAME" + NEW_LINE;
    }

    private String createCsvStringPerson(
            Person person
    ) {
        final String SEPARATOR = ";";
        final String NEW_LINE = "\n";

        return person.getFirstname() + SEPARATOR + person.getLastname() + NEW_LINE;
    }

    @Override
    public String createPersonRandom(

    ) {
        Person person = randomUserClient.fetchRandomPerson();
        Person savedPerson = personRepository.save(person);
        return savedPerson.getFirstname();
    }

    @Override
    public Person mapAndSavePersonMinimal(
            PersonMinimal personMinimal
    ) {
        Person person = personMapper.requestToEntity(personMinimal);
        return personRepository.save(person);
    }

    @Override
    public PersonResponse mapAndSavePersonMinimalToPersonResponse(
            PersonMinimal personMinimal
    ) {
        Person person = mapAndSavePersonMinimal(personMinimal);
        return personMapper.entityToResponse(person);
    }

    @Override
    public int deleteAllPeople() {
        int size = personRepository.findAll().size();
        personRepository.deleteAll();
        if (personRepository.findAll().size() > 0) {
            throw new RuntimeException("Could not delete all people");
        }
        return size;
    }


}
