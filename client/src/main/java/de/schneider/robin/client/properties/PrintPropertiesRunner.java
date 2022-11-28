package de.schneider.robin.client.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class PrintPropertiesRunner implements CommandLineRunner {

    private final BackendConnectionProperties backendConnectionProperties;

    @Value("${enable.runner-print}")
    private boolean isPrintEnabled;

    @Value("${enable.backend-scheduler}")
    private boolean isBackendSchedulerEnabled;

    public PrintPropertiesRunner(
            BackendConnectionProperties backendConnectionProperties
    ) {
        this.backendConnectionProperties = backendConnectionProperties;
    }

    @Override public void run(String... args) throws Exception {
        if (isPrintEnabled) {
            System.out.println(backendConnectionProperties);
            System.out.println("isBackendSchedulerEnabled: " + isBackendSchedulerEnabled);
        }
    }
}
