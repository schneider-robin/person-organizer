package de.schneider.robin.backend.across.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class StartupRunner implements CommandLineRunner {

    @Override
    public void run(String... args) {

        log.info("http://localhost:8080/swagger-ui.html >> REST");
        log.info("http://localhost:8080/graphiql >> GraphQL");
        log.info("http://localhost:8080/h2-console >> Postgres");
        log.info("http://localhost:8080/actuator >> Monitoring");
        log.info("http://localhost:16686 >> Jaeger (optional)");
    }
}
