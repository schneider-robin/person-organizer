package de.schneider.robin.backend.api.rest.in;

import de.schneider.robin.backend.api.kafka.out.KafkaProducer;
import de.schneider.robin.backend.api.model.PersonMinimal;
import de.schneider.robin.backend.api.rest.in.model.CompletePersonPageResponse;
import de.schneider.robin.backend.service.CreatePersonPublisher;
import de.schneider.robin.backend.service.PersonService;
import de.schneider.robin.lib.api.model.PersonListResponse;
import de.schneider.robin.lib.api.model.PersonRequest;
import de.schneider.robin.lib.api.model.PersonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.UUID;

@RestController
@Log4j2
@Tag(name = "PersonController", description = "store and retrieve people")
public class PersonRestController {

    private final PersonService personService;
    private final CreatePersonPublisher createPersonPublisher;
    private final KafkaProducer kafkaProducer;

    public PersonRestController(
            PersonService personService,
            CreatePersonPublisher createPersonPublisher,
            KafkaProducer kafkaProducer
    ) {
        this.personService = personService;
        this.createPersonPublisher = createPersonPublisher;
        this.kafkaProducer = kafkaProducer;
    }

    // ----------------------------
    // GET unprotected

    @Operation(summary = "Get person by first name")
    @GetMapping(path = { "/person/{firstname}"})
    public ResponseEntity<PersonResponse> getPersonByFirstname(
            @PathVariable String firstname
    ) {
        PersonResponse personResponse = personService.getPersonByFirstname(firstname);
        return ResponseEntity.ok(personResponse);
    }

    @GetMapping(path = { "/people/under-age/{age}"})
    public ResponseEntity<PersonListResponse> getPeopleUnderAge(
            @PathVariable int age
    ) {
        PersonListResponse personListResponse = personService.getPeopleUnderAge(age);
        return ResponseEntity.ok(personListResponse);
    }

    @GetMapping(path = { "/people/over-age/{age}"})
    public ResponseEntity<PersonListResponse> getPeopleOverAge(
            @PathVariable int age
    ) {
        PersonListResponse personListResponse = personService.getPeopleOverAge(age);
        return ResponseEntity.ok(personListResponse);
    }

    @GetMapping(path = { "/people/text-in-firstname"})
    public ResponseEntity<PersonListResponse> getPeopleByTextInFirstname(
            @RequestParam(name = "text") String text
    ) {
        PersonListResponse personListResponse = personService.getPeopleByTextInFirstname(text);
        return ResponseEntity.ok(personListResponse);
    }

    @GetMapping(path = { "/people/text-eq-lastname"})
    public ResponseEntity<PersonListResponse> getPeopleByTextEqLastname(
            @RequestParam(name = "text") String text
    ) {
        PersonListResponse personListResponse = personService.getPeopleByTextEqLastname(text);
        return ResponseEntity.ok(personListResponse);
    }

    @GetMapping(path = { "/people/all"}, produces = "application/json")
    public ResponseEntity<PersonListResponse> getAllPeople(

    ) {
        PersonListResponse personListResponse = personService.getAllPeople();
        return ResponseEntity.ok(personListResponse);
    }

    @GetMapping(path = { "/people/all"}, produces = "text/csv")
    public ResponseEntity<String> getAllPeopleAsCsv(

    ) {
        String peopleAsCsv = personService.getAllPeopleAsCsv();
        return ResponseEntity.ok(peopleAsCsv);
    }

    @GetMapping(path = { "/people/page"})
    public ResponseEntity<CompletePersonPageResponse> getPeoplePage(
            @RequestParam(name = "page_size", defaultValue = "10") int pageSize,
            @RequestParam(name = "page_number", defaultValue = "0") int pageNumber
    ) {
        CompletePersonPageResponse completePersonPageResponse = personService.getPeoplePage(pageSize, pageNumber);
        return ResponseEntity.ok(completePersonPageResponse);
    }

    // ----------------------------
    // POST unprotected

    @PostMapping(path = {"/person/minimal"})
    public void createPerson(
            @RequestBody PersonMinimal personMinimal
    ) {
        personService.mapAndSavePersonMinimal(personMinimal);
    }

    @PostMapping(path = {"/person/minimal/app-event"})
    public void sendAppEventToListener(
            @RequestBody PersonMinimal personMinimal
    ) {
        createPersonPublisher.publishPersonMinimalAsync(personMinimal);
    }

    @PostMapping(path = {"person/minimal/kafka"})
    public String sendMessageToKafka(
            @RequestBody PersonMinimal personMinimal
    ) {
        kafkaProducer.sendPersonMinimalToTopic(personMinimal);
        return "Person sent successfully to topic";
    }

    // request header vs. spring boot endpoint:
    // Content-Type   ==  'consumes'
    // Accept         ==  'produces'
    // if one of the two is not set, it will not be compared
    // if one of them don't match, the request is rejected with a 406 (Not Acceptable)
    @PostMapping(path = {"/person/minimal/csv"}, consumes = "multipart/form-data", produces = "text/csv")
    public ResponseEntity<String> createPersonFromCsv(
            @RequestBody MultipartFile csvFile,
            @RequestHeader HttpHeaders headers // optional parameter, just for this demo
    ) throws IOException {
        log.info("Headers: {}", headers);
        String csvPersonString = personService.createPerson(csvFile);
        return ResponseEntity.ok(csvPersonString);
    }

    // ----------------------------
    // Protected

    @PostMapping(path = {"/protected/person"})
    public ResponseEntity<UUID> createPersonProtected(
            @RequestBody PersonRequest personRequest
    ) {
        UUID newPersonId = personService.createPerson(personRequest);
        return ResponseEntity.ok(newPersonId);
    }

    @PostMapping(path = {"/protected/person/valid-body"})
    public ResponseEntity<UUID> createPersonValidBodyProtected(
            @RequestBody @Valid PersonRequest personRequest
    ) {
        UUID newPersonId = personService.createPerson(personRequest);
        return ResponseEntity.ok(newPersonId);
    }

    // @PreAuthorize("hasRole('SUPER')") // enabled via path in SecurityConfig
    @PostMapping(path = {"/protected/person/random"})
    public String createPersonRandomProtected() {
        return "created: " + personService.createPersonRandom();
    }

    // @PreAuthorize("hasRole('ADMIN')") // enabled via path in SecurityConfig
    @DeleteMapping(path = {"/protected/people/delete"})
    public int deleteAllPeopleProtected() {
        return personService.deleteAllPeople();
    }

}
