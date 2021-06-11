package taxi.project.demo.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

@Getter
public class ApiException {

    private final String message;
    private final ZonedDateTime date;
    private final HttpStatus status;


    public ApiException(String message, ZonedDateTime date, HttpStatus status) {
        this.message = message;
        this.date = date;
        this.status = status;
    }
}
