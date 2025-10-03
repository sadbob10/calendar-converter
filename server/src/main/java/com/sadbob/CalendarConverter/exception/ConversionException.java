package com.sadbob.CalendarConverter.exception;

import org.springframework.http.HttpStatus;

public class ConversionException extends AppException {
    public ConversionException(String message) {
        super(message, HttpStatus.BAD_REQUEST, "CONVERSION_ERROR");
    }

    public ConversionException(String message, Throwable cause) {
        super(message, HttpStatus.BAD_REQUEST, "CONVERSION_ERROR");
        this.initCause(cause);
    }
}
