package com.sadbob.CalendarConverter.dto.responseDTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(
        String path,
        String errorCode,
        String message,
        HttpStatus status,
        LocalDateTime timestamp,
        Object details
) {
    public ErrorResponse {
        timestamp = LocalDateTime.now();
    }
}
