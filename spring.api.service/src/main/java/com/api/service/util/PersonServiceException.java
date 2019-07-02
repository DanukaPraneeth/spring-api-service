package com.api.service.util;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class PersonServiceException extends RuntimeException{

    public PersonServiceException(String message) {
        super(message);
    }

}
