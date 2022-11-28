package de.schneider.robin.backend.across.mapper;

import de.schneider.robin.backend.api.rest.in.model.CompletePersonPageResponse;
import de.schneider.robin.backend.api.rest.out.model.RandomUser;
import de.schneider.robin.backend.db.model.Hobby;
import de.schneider.robin.backend.db.model.Person;
import de.schneider.robin.backend.api.model.PersonMinimal;
import de.schneider.robin.backend.db.model.PhysicalAddress;
import de.schneider.robin.lib.api.model.PersonListResponse;
import de.schneider.robin.lib.api.model.PersonRequest;
import de.schneider.robin.lib.api.model.PersonResponse;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

import javax.validation.Valid;
import java.util.List;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, uses = {
        OffsetDateTimeMapper.class,
        PersonAttributesMapper.class,
        PhysicalAddressMapper.class,
        MailAddressMapper.class,
        HobbyMapper.class
} )
public interface PersonMapper {

    @Mapping(target = "id", source = "personId")
    @Mapping(target = "fullName", expression = "java(mergeNames(person))")
    @Mapping(target = "personAttributesResponse", source = "personAttributes")
    @Mapping(target = "physicalAddressResponse", source = "physicalAddress")
    @Mapping(target = "mailAddressResponses", source = "mailAddresses")
    @Mapping(target = "hobbyResponses", source = "hobbies")
    PersonResponse entityToResponse(
            @Valid Person person
    );

    default String mergeNames(
            Person person
    ) {
        return person.getFirstname() + " " + person.getLastname();
    }

    CompletePersonPageResponse entityPageToResponsePage(
            Page<Person> personPage,
            int pageSize,
            int pageNumber
    );

    default PersonListResponse personListToPersonListResponse(List<Person> personList){
        return new PersonListResponse().personResponses(entitiesToResponses(personList));
    }

    List<PersonResponse> entitiesToResponses(
            List<Person> people
    );

    @Mapping(target = "personAttributes", source = "request.personAttributesRequest")
    @Mapping(target = "physicalAddress", source = "physicalAddressEntity")
    @Mapping(target = "mailAddresses", source = "request.mailAddressRequests")
    @Mapping(target = "hobbies", source = "hobbiesEntity")
    Person requestToEntity(
            PersonRequest request,
            PhysicalAddress physicalAddressEntity,
            List<Hobby> hobbiesEntity
    );

    Person requestToEntity(
           PersonMinimal personMinimal
    );

    @Mapping(target = "firstname", source = "randomUser.name.first")
    @Mapping(target = "lastname", source = "randomUser.name.last")
    Person requestToEntity(
            RandomUser randomUser
    );
}
