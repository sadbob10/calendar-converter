package com.sadbob.CalendarConverter.dto.responseDTO.calendar;


import java.util.Map;

public record TodayResponse(
        Map<String, String> todayDates,
        Map<String, String> formattedDates
) {}
