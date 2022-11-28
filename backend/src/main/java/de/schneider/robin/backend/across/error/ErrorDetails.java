package de.schneider.robin.backend.across.error;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ErrorDetails {

    private final HttpStatus status;
    private final String type;
    @JsonProperty("error_code")
    private final String errorCode;;
    private final String message;

    protected ErrorDetails(
            HttpStatus status,
            String type,
            String errorCode,
            String message
    ) {
        this.status = status;
        this.type = type;
        this.errorCode = errorCode;
        this.message = message;
    }
}
