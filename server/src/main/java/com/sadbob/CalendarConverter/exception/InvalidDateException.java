package com.sadbob.CalendarConverter.exception;

import org.springframework.http.HttpStatus;

public class InvalidDateException extends AppException {
    public InvalidDateException(String message) {
        super(message, HttpStatus.BAD_REQUEST, "INVALID_DATE");
    }
}
