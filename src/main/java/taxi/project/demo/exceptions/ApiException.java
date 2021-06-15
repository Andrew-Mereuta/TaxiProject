package taxi.project.demo.exceptions;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;
import java.util.UUID;

@Getter
@Setter
@ToString
public class ApiException {

    private final String message;
    private final ZonedDateTime date;
    private final HttpStatus status;
    private final UUID correlationId;

    public ApiException(String message, ZonedDateTime date, HttpStatus status, UUID correlationId) {
        this.message = message;
        this.date = date;
        this.status = status;
        this.correlationId = correlationId;
    }
}
