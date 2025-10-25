package com.sadbob.CalendarConverter.dto.responseDTO.calendar;

import java.util.List;

public class CalendarWeekResponse {
    private List<CalendarDayResponse> days;

    public CalendarWeekResponse(List<CalendarDayResponse> days) {
        this.days = days;
    }

    public List<CalendarDayResponse> getDays() {
        return days;
    }

    public void setDays(List<CalendarDayResponse> days) {
        this.days = days;
    }
}
