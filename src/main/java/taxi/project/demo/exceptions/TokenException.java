package taxi.project.demo.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TokenException extends RuntimeException {

    private final static Logger logger = LoggerFactory.getLogger(TokenException.class);

    public TokenException(String s) {
        super(s);
    }

}
