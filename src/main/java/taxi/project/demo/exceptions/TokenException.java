package taxi.project.demo.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.ZonedDateTime;


@ResponseStatus(HttpStatus.FORBIDDEN)
public class TokenException extends RuntimeException{

    private final static Logger logger = LoggerFactory.getLogger(TokenException.class);

    public TokenException(String s) {
        ZonedDateTime time = ZonedDateTime.now();
        logger.warn("Exception:  \n" +"time: " + time + ";; " + s);
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return null;
    }

    public TokenException(String message, Throwable cause) {
        super(message, cause);
    }
}
