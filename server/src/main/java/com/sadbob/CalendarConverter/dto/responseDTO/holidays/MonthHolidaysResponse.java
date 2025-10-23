package com.sadbob.CalendarConverter.dto.responseDTO.holidays;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonthHolidaysResponse {
    private int year;
    private int month;
    private String calendarType;
    private List<HolidayResponse> holidays;
}
