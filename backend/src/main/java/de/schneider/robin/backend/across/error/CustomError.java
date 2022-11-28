package de.schneider.robin.backend.across.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomError extends RuntimeException {

    private final ErrorDetails errorDetails;

    public CustomError(
            HttpStatus status,
            String type,
            String errorCode,
            String message
    ) {
        errorDetails = new ErrorDetails(status, type, errorCode, message);
    }
}
