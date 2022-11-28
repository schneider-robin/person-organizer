package de.schneider.robin.backend.across.error;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;

@Log4j2
public class PersonNotFoundError extends CustomError {

    public static final String DEFAULT_MESSAGE = "Person '%s' not found";

    public PersonNotFoundError(
            String firstname
    ) {
        super(
                HttpStatus.NOT_FOUND,
                ErrorCode.PERSON_NOT_FOUND.name(),
                ErrorCode.PERSON_NOT_FOUND.getErrorCode(),
                String.format(DEFAULT_MESSAGE, firstname)
        );
        log.error(String.format(DEFAULT_MESSAGE, firstname));
    }
}
