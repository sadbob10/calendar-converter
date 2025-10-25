package com.sadbob.CalendarConverter.dto.responseDTO.calendar;

import com.sadbob.CalendarConverter.enums.CalendarType;
import java.util.List;

public class CalendarResponse {
    private String currentDate;
    private String monthYear;
    private List<CalendarWeekResponse> weeks;
    private CalendarType calendarType;
    private int year;
    private int month;

    // Constructors
    public CalendarResponse() {}

    public CalendarResponse(String currentDate, String monthYear, List<CalendarWeekResponse> weeks,
                            CalendarType calendarType, int year, int month) {
        this.currentDate = currentDate;
        this.monthYear = monthYear;
        this.weeks = weeks;
        this.calendarType = calendarType;
        this.year = year;
        this.month = month;
    }

    // Getters and setters for all fields
    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public String getMonthYear() {
        return monthYear;
    }

    public void setMonthYear(String monthYear) {
        this.monthYear = monthYear;
    }

    public List<CalendarWeekResponse> getWeeks() {
        return weeks;
    }

    public void setWeeks(List<CalendarWeekResponse> weeks) {
        this.weeks = weeks;
    }

    public CalendarType getCalendarType() {
        return calendarType;
    }

    public void setCalendarType(CalendarType calendarType) {
        this.calendarType = calendarType;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }
}
