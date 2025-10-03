package com.sadbob.CalendarConverter.dto.responseDTO;



public record AgeResponse(
        int age,
        String birthDate,
        String nextBirthday,
        String message
) {}
