package de.schneider.robin.backend.across.error;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;

@Log4j2
public class SecurityUserNotFoundError extends CustomError {

    private static final String DEFAULT_MESSAGE = "Wrong username ('%s') or password";

    public SecurityUserNotFoundError(
            String username
    ) {
        super(
                HttpStatus.NOT_FOUND,
                ErrorCode.SECURITY_USER_NOT_FOUND.name(),
                ErrorCode.SECURITY_USER_NOT_FOUND.getErrorCode(),
                String.format(DEFAULT_MESSAGE, username)
        );
        log.error(String.format(DEFAULT_MESSAGE, username));
    }
}
