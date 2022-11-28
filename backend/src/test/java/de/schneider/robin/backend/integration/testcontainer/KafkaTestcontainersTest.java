package de.schneider.robin.backend.integration.testcontainer;

import de.schneider.robin.backend.api.kafka.in.KafkaTopicListener;
import de.schneider.robin.backend.api.kafka.out.KafkaProducer;
import de.schneider.robin.backend.api.model.PersonMinimal;
import de.schneider.robin.backend.service.PersonService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;

@Disabled // only enable when docker is running !!!
@SpringBootTest
@Testcontainers
@ActiveProfiles("kafka")
public class KafkaTestcontainersTest {

    @Autowired
    private KafkaProducer kafkaProducer;

    @Autowired
    private KafkaTopicListener kafkaTopicListener;

    @MockBean
    private PersonService personService;

    @Container
    static KafkaContainer container = new KafkaContainer(
            DockerImageName.parse("confluentinc/cp-kafka:6.2.1"));

    @DynamicPropertySource
    public static void overrideProps(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", container::getBootstrapServers);
    }

    @Test
    void checkPersonTopic() throws InterruptedException {
        ArgumentCaptor<PersonMinimal> captor = ArgumentCaptor.forClass(PersonMinimal.class);
        kafkaProducer.sendPersonMinimalToTopic2(PersonMinimal.builder()
                .firstname("Marc")
                .lastname("Fischer")
                .build());
        Thread.sleep(1000);
        verify(personService).mapAndSavePersonMinimal(captor.capture());
        System.out.println("Captured person: " + captor.getValue());
        assertThat(captor.getValue().getFirstname()).isEqualTo("Marc");
    }
}
