package com.georges.booknetwork.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.helpers.MessageFormatter;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Setter
@Getter
public class BookException extends RuntimeException {

    private int statusCode;

    public BookException() {
        setStatusCode(INTERNAL_SERVER_ERROR.value());
    }

    public BookException(String message) {
        this(INTERNAL_SERVER_ERROR.value(), message);
    }

    public BookException(String message, Object... args) {
        this(INTERNAL_SERVER_ERROR.value(), MessageFormatter.arrayFormat(message, args).getMessage());
    }

    public BookException(int code, String message) {
        super(message);
        setStatusCode(code);
    }
}
