package com.sadbob.CalendarConverter.service.interf;



import com.sadbob.CalendarConverter.dto.responseDTO.calendar.CalendarResponse;
import com.sadbob.CalendarConverter.enums.CalendarType;

public interface CalendarService {
    CalendarResponse getCalendar(int year, int month);
    CalendarResponse getPreviousMonth(int year, int month);
    CalendarResponse getNextMonth(int year, int month);
    CalendarType getCalendarType();
}