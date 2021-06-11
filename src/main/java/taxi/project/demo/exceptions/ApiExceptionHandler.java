package taxi.project.demo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(value = {TokenException.class})
    public ResponseEntity<Object> handleException(TokenException e) {
        ApiException z = new ApiException(e.getMessage(), ZonedDateTime.now(ZoneId.of("Z")), HttpStatus.FORBIDDEN);
        return new ResponseEntity<>(z, HttpStatus.FORBIDDEN);
    }
}
