package de.schneider.robin.backend.api.rest.out;

import de.schneider.robin.backend.api.rest.out.model.RandomUser;
import de.schneider.robin.backend.api.rest.out.model.RandomUserList;
import de.schneider.robin.backend.db.model.Person;
import de.schneider.robin.backend.across.mapper.PersonMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class RandomUserClient {

    private final RestTemplate restTemplate;
    private final PersonMapper personMapper;

    @Value("${randomuser.url}")
    private String randomUserUrl;

    public RandomUserClient(
            RestTemplateBuilder restTemplateBuilder,
            PersonMapper personMapper
    ) {
        this.restTemplate = restTemplateBuilder.build();
        this.personMapper = personMapper;
    }

    public Person fetchRandomPerson() {
        var randomUserResponseEntity = restTemplate.getForEntity(
                randomUserUrl,
                RandomUserList.class);

        RandomUser randomUser = randomUserResponseEntity.getBody().getResults().get(0);
        return personMapper.requestToEntity(randomUser);
    }
}
