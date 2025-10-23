package com.sadbob.CalendarConverter.dto.responseDTO;

import java.util.List;
import java.util.Map;

public record ConversionResponse(
        String sourceDate,
        String sourceCalendar,
        Map<String, String> conversions,
        Map<String, String> formattedDates,
        List<String> targetCalendars,
        List<String> sourceHolidays,
        List<String> targetHolidays,
        String message
) {
    // Optional constructor for backward compatibility
    public ConversionResponse(String sourceDate, String sourceCalendar,
                              Map<String, String> conversions,
                              Map<String, String> formattedDates,
                              List<String> targetCalendars,
                              List<String> sourceHolidays,
                              List<String> targetHolidays) {
        this(sourceDate, sourceCalendar, conversions, formattedDates,
                targetCalendars, sourceHolidays, targetHolidays, "Date converted successfully");
    }

    // Original constructor for minimal info
    public ConversionResponse(String sourceDate, String sourceCalendar,
                              Map<String, String> conversions,
                              Map<String, String> formattedDates) {
        this(sourceDate, sourceCalendar, conversions, formattedDates, null, null, null, "Date converted successfully");
    }
}
