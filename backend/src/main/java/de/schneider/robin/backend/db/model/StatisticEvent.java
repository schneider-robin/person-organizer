package de.schneider.robin.backend.db.model;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.time.Instant;

@Data
@Document
public class StatisticEvent {

    @Id
    private String id;

    private Instant createdDate;

    private String message;

    public StatisticEvent(
            String message
    ) {
        this.message = message;
        this.createdDate = Instant.now();
    }

}
