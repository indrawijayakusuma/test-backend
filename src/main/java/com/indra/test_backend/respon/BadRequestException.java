package com.indra.test_backend.respon;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class BadRequestException extends Exception{
    public BadRequestException(String message) {
        super(message);
    }
}
