package com.sadbob.CalendarConverter.dto.responseDTO.calendar;

public class CalendarDayResponse {
    private int day;
    private String displayDay;
    private boolean isCurrentMonth;
    private boolean isToday;
    private String otherCalendarDate; // Ethiopian/Hijri equivalent

    public CalendarDayResponse(int day, String displayDay, boolean isCurrentMonth, boolean isToday, String otherCalendarDate) {
        this.day = day;
        this.displayDay = displayDay;
        this.isCurrentMonth = isCurrentMonth;
        this.isToday = isToday;
        this.otherCalendarDate = otherCalendarDate;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String getDisplayDay() {
        return displayDay;
    }

    public void setDisplayDay(String displayDay) {
        this.displayDay = displayDay;
    }

    public boolean isCurrentMonth() {
        return isCurrentMonth;
    }

    public void setCurrentMonth(boolean currentMonth) {
        isCurrentMonth = currentMonth;
    }

    public boolean isToday() {
        return isToday;
    }

    public void setToday(boolean today) {
        isToday = today;
    }

    public String getOtherCalendarDate() {
        return otherCalendarDate;
    }

    public void setOtherCalendarDate(String otherCalendarDate) {
        this.otherCalendarDate = otherCalendarDate;
    }
}