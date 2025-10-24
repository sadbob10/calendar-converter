package com.sadbob.CalendarConverter.dto.responseDTO.export;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CalendarDataResponse {
    private String calendarType;
    private int year;
    private int month;
    private String monthName;
    private List<HolidayDataResponse> holidays;
}
