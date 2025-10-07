package com.sadbob.CalendarConverter.service;



import com.sadbob.CalendarConverter.dto.responseDTO.CalendarResponse;
import com.sadbob.CalendarConverter.util.CalendarType;

public interface CalendarService {
    CalendarResponse getCalendar(int year, int month);
    CalendarResponse getPreviousMonth(int year, int month);
    CalendarResponse getNextMonth(int year, int month);
    CalendarType getCalendarType();
}