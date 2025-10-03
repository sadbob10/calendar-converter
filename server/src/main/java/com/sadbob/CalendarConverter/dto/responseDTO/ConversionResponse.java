package com.sadbob.CalendarConverter.dto.responseDTO;


import java.util.Map;

public record ConversionResponse(
        String originalDate,
        String originalCalendar,
        Map<String, String> conversions,
        Map<String, String> formattedDates,
        String message
) {}
