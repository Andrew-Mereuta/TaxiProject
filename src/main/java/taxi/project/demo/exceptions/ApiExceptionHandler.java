package taxi.project.demo.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    private final static Logger logger = LoggerFactory.getLogger(ApiExceptionHandler.class);

    @ExceptionHandler(value = { TokenException.class })
    public ResponseEntity<Object> handleException(TokenException e, WebRequest request) {
        ApiException exception = new ApiException(e.getMessage(), ZonedDateTime.now(ZoneId.of("Z")), HttpStatus.FORBIDDEN, UUID.randomUUID());
        logger.warn(exception.toString());
        return new ResponseEntity<>(exception, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = {ResourceNotFoundException.class})
    public ResponseEntity<Object> handleResourceNotFoundException(RuntimeException e) {
        ApiException exception =  new ApiException(e.getMessage(), ZonedDateTime.now(ZoneId.of("Z")), HttpStatus.NOT_FOUND, UUID.randomUUID());
        logger.warn(exception.toString());
        return new ResponseEntity<>(exception, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {MethodNotAllowed.class})
    public ResponseEntity<Object> handleMethodNotAllowed(RuntimeException e) {
        ApiException exception =  new ApiException(e.getMessage(), ZonedDateTime.now(ZoneId.of("Z")), HttpStatus.METHOD_NOT_ALLOWED, UUID.randomUUID());
        logger.warn(exception.toString());
        return new ResponseEntity<>(exception, HttpStatus.METHOD_NOT_ALLOWED);
    }


}
