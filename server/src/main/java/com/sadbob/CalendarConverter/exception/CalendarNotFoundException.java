package com.sadbob.CalendarConverter.exception;

import org.springframework.http.HttpStatus;

public class CalendarNotFoundException extends AppException {
    public CalendarNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND, "CALENDAR_NOT_FOUND");
    }
}
