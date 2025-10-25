package com.sadbob.CalendarConverter.dto.responseDTO.age;



public record AgeResponse(
        int age,
        String birthDate,
        String nextBirthday,
        String message
) {}
