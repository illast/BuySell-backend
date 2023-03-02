package ee.taltech.iti0302.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception ex) {
        String errorMessage = "Internal server error";
        log.error(errorMessage, ex);
        return new ResponseEntity<>(new ErrorResponse(errorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<Object> handleApplicationException(ApplicationException ex) {
        log.error(ex.getMessage(), ex);
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<Object> handleAuthorizationException(AuthorizationException ex) {
        log.error(ex.getMessage(), ex);
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage()), HttpStatus.FORBIDDEN);
    }
}
