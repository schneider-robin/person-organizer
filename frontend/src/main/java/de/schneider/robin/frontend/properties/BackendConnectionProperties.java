package de.schneider.robin.frontend.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("backend")
public class BackendConnectionProperties {

    private String host;
    private int port;
}
