package de.schneider.robin.backend.across.error;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Log4j2
@ControllerAdvice
@RestController
public class GlobalExceptionHandler {

    // https://www.baeldung.com/exception-handling-for-rest-with-spring

    @ExceptionHandler(Exception.class)
    public Exception handleException(
            Exception exception,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        return exception;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleMethodArgumentNotValidException( // e.g. wrong email (MailContactValidator)
            MethodArgumentNotValidException exception
    ) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(exception.getClass().getSimpleName());
    }

    @ExceptionHandler(CustomError.class)
    public ResponseEntity<ErrorDetails> handleCustomError( // for all subclasses of CustomError
             CustomError error
    ) {
        return ResponseEntity
                .status(error.getErrorDetails().getStatus())
                .body(error.getErrorDetails());
    }
}