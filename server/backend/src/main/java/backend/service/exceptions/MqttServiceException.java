package backend.service.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
public class MqttServiceException extends RuntimeException{
    public MqttServiceException(String message) {
        super(message);
    }
}
