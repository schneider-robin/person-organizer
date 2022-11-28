package de.schneider.robin.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PersonOrganizerClient {

    public static void main(String[] args) {
        SpringApplication.run(PersonOrganizerClient.class, args);
    }
}
