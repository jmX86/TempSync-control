package backend.rest;

import backend.service.exceptions.DataNotFoundException;
import backend.service.exceptions.MqttServiceException;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.HashMap;
import java.util.Map;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(RestExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<?> handleException(Exception e, WebRequest req){
        return getResponseData(e, req, null, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    protected ResponseEntity<?> handleIllArgException(Exception e, WebRequest req){
        return getResponseData(e, req, e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataNotFoundException.class)
    protected ResponseEntity<?> handleDataNotFoundException(Exception e, WebRequest req){
        return getResponseData(e, req, e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MqttServiceException.class)
    protected ResponseEntity<?> handleMqttServiceException(Exception e, WebRequest req){
        return getResponseData(e, req, e.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    protected ResponseEntity<?> handleNoHandleException(Exception e, WebRequest req){
        return getResponseData(e, req, e.getMessage(), HttpStatus.NOT_FOUND);
    }

    private ResponseEntity<?> getResponseData(Exception e, WebRequest req, String responseMessage, HttpStatus httpStatus){
        logger.error("{}\n{}\n{}", req.getDescription(true), e.getMessage(), e.toString());

        Map<String, String> props = new HashMap<>();

        if(responseMessage != null){
            props.put("message", responseMessage);
        }

        props.put("status", String.valueOf(httpStatus.value()));
        props.put("error", httpStatus.getReasonPhrase());

        return new ResponseEntity<>(props, httpStatus);
    }
}
