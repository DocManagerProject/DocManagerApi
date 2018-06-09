package pl.docmanager.web.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import pl.docmanager.web.controllers.exception.EntityValidationException;
import pl.docmanager.web.security.AccessValidationException;
import pl.docmanager.web.security.AccessValidator;
import pl.docmanager.web.security.ApiTokenDecoder;

import java.util.NoSuchElementException;

public abstract class RestControllerBase {
    protected AccessValidator accessValidator;
    protected ApiTokenDecoder apiTokenDecoder;

    public RestControllerBase(AccessValidator accessValidator, ApiTokenDecoder apiTokenDecoder) {
        this.accessValidator = accessValidator;
        this.apiTokenDecoder = apiTokenDecoder;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(EntityValidationException.class)
    protected String return400(Exception e) {
        return e.getMessage();
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({NoSuchElementException.class, AccessValidationException.class})
    protected String return404(Exception e) {
        return e.getMessage();
    }
}
