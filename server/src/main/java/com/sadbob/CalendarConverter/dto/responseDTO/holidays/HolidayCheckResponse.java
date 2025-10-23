package com.sadbob.CalendarConverter.dto.responseDTO.holidays;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HolidayCheckResponse {
    private String date;
    private String calendarType;
    private boolean isHoliday;
    private String holidayName;
    private String holidayType;
}
