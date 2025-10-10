package com.sadbob.CalendarConverter.exception;

import org.springframework.http.HttpStatus;

public class CalendarServiceException extends AppException {
    public CalendarServiceException(String message) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR, "CALENDAR_SERVICE_ERROR");
    }

    public CalendarServiceException(String message, Throwable cause) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR, "CALENDAR_SERVICE_ERROR");
        this.initCause(cause);
    }
}